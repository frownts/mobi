package com.join.mobi.activity;

import android.content.ActivityNotFoundException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.BeanUtils;
import com.join.android.app.common.utils.FileOpenUtils;
import com.join.mobi.adapter.LocalAdapter;
import com.join.mobi.enums.Dtype;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadFile;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.*;

import java.util.*;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-13
 * Time: 下午5:10
 * 本地资源
 */

@EActivity(R.layout.share_activity_layout)
public class LocalActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @ViewById
    LinearLayout titleContainer;
    @ViewById
    EditText search;
    @ViewById
    ImageView searchIcon;
    @ViewById
    ImageView filterCup;
    @ViewById
    ImageView trash;
    @ViewById
    GridView listView;
    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;

    LocalAdapter localAdapter;

    List<DownloadFile> origDownloadFile = new ArrayList<DownloadFile>(0);
    List<DownloadFile> filterDownloadFile = new ArrayList<DownloadFile>(0);
    PopupWindow popFilter;
    PopupWindow popDownloadHint;

    @AfterViews
    void afterViews() {
        wrapEvent();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        localAdapter = new LocalAdapter(this);
        origDownloadFile = DownloadTool.getAllDownloaded((DownloadApplication) getApplicationContext(), Dtype.Share);
        listView.setAdapter(localAdapter);


        retrieveDataFromServer();
    }

    @ItemClick
    void listViewItemClicked(DownloadFile downloadFile) {
        int type = Integer.parseInt(downloadFile.getFileType());

        if (type == 1 || type == 2) {//todo 调用视频播放器
            MyVideoViewBufferFullScreen_.intent(this).path(downloadFile.getAbsolutePath()).start();
        } else if (type == 3) {
//            Uri path = Uri.fromFile(new File(downloadFile.getAbsolutePath()));
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(path, "application/pdf");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(FileOpenUtils.getPdfFileIntent(downloadFile.getAbsolutePath()));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (type == 4) {
            //todo 图片查看器
            startActivity(FileOpenUtils.getImageFileIntent(downloadFile.getAbsolutePath()));
        }

    }

    @Click
    void trashClicked() {
        localAdapter.setTrashIsShowing(!localAdapter.isTrashIsShowing());
        localAdapter.notifyDataSetChanged();
    }

    @UiThread
    void dismissDownloadHint() {
        try {
            if (popDownloadHint != null && popDownloadHint.isShowing()) popDownloadHint.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Background
    void retrieveDataFromServer() {
        updateView();
    }


    @UiThread
    public void updateView() {
        origDownloadFile = DownloadTool.getAllDownloaded((DownloadApplication) getApplicationContext(), Dtype.Share);
        localAdapter.setItems(origDownloadFile);
        localAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Click
    void backClicked() {
        finish();
    }

    @Click
    void filterCupClicked() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_share_filter, null);

        wrapTypeFilterEvent(view);
        if (popFilter != null && popFilter.isShowing()) popFilter.dismiss();
        else {
            popFilter = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
            popFilter.setOutsideTouchable(true);
            popFilter.showAsDropDown(titleContainer);
        }
    }

    private void wrapTypeFilterEvent(View view) {
        selectAll = (TextView) view.findViewById(R.id.selectAll);
        filterImg = (TextView) view.findViewById(R.id.filterImg);
        filterDoc = (TextView) view.findViewById(R.id.filterDoc);
        filterAudio = (TextView) view.findViewById(R.id.filterAudio);
        filterVideo = (TextView) view.findViewById(R.id.filterVideo);

        imgSelectAll = (ImageView) view.findViewById(R.id.imgSelectAll);
        imgFilterImg = (ImageView) view.findViewById(R.id.imgFilterImg);
        imgFilterDoc = (ImageView) view.findViewById(R.id.imgFilterDoc);
        imgFilterAudio = (ImageView) view.findViewById(R.id.imgFilterAudio);
        imgFilterVideo = (ImageView) view.findViewById(R.id.imgFilterVideo);

        selectAll.setOnClickListener(this);
        filterImg.setOnClickListener(this);
        filterDoc.setOnClickListener(this);
        filterAudio.setOnClickListener(this);
        filterVideo.setOnClickListener(this);
        imgSelectAll.setOnClickListener(this);
        imgFilterImg.setOnClickListener(this);
        imgFilterDoc.setOnClickListener(this);
        imgFilterAudio.setOnClickListener(this);
        imgFilterVideo.setOnClickListener(this);

    }


    @Override
    public void onRefresh() {
        retrieveDataFromServer();
    }

    private void wrapEvent() {
        //搜索图标事件，聚焦时，图片隐藏
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) {
                    searchIcon.setVisibility(View.INVISIBLE);
                } else
                    searchIcon.setVisibility(View.VISIBLE);
            }
        });

        //搜索事件
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                filterDownloadFile.clear();

                if (charSequence.toString().equals("")) {
                    localAdapter.setItems(origDownloadFile);
                } else {
                    for (DownloadFile share : origDownloadFile) {
                        if (share.getShowName().contains(charSequence)) {
                            DownloadFile _share = new DownloadFile();
                            BeanUtils.copyProperties(_share, share);
                            filterDownloadFile.add(_share);
                        }
                    }
                    localAdapter.setItems(filterDownloadFile);
                }
                localAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }


    void filterByType() {
        filterDownloadFile.clear();

        Iterator keys = types.keySet().iterator();
        while (keys.hasNext()) {
            int _type = types.get(keys.next());
            for (DownloadFile share : origDownloadFile) {

                if (_type == Integer.parseInt(share.getFileType())) {
                    DownloadFile _share = new DownloadFile();
                    BeanUtils.copyProperties(_share, share);
                    filterDownloadFile.add(_share);
                }
            }
        }
        localAdapter.setItems(filterDownloadFile);
        localAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.selectAll || view.getId() == R.id.imgSelectAll) {
            if (imgSelectAll.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.flter_uncheck).getConstantState())) {//进行全选
                imgSelectAll.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                imgFilterImg.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                imgFilterDoc.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                imgFilterAudio.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                imgFilterVideo.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                types.put(1, 1);
                types.put(2, 2);
                types.put(3, 3);
                types.put(4, 4);
            } else {//取消全选
                imgSelectAll.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                imgFilterImg.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                imgFilterDoc.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                imgFilterAudio.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                imgFilterVideo.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                types.clear();
            }
        }

        if (view.getId() == R.id.filterImg || view.getId() == R.id.imgFilterImg) {
            if (imgFilterImg.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.flter_uncheck).getConstantState())) {
                imgFilterImg.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                types.put(4, 4);
            } else {
                imgFilterImg.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                types.remove(4);
            }

        }

        if (view.getId() == R.id.filterDoc || view.getId() == R.id.imgFilterDoc) {
            if (imgFilterDoc.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.flter_uncheck).getConstantState())) {
                imgFilterDoc.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                types.put(3, 3);
            } else {
                imgFilterDoc.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                types.remove(3);
            }

        }

        if (view.getId() == R.id.filterAudio || view.getId() == R.id.imgFilterAudio) {
            if (imgFilterAudio.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.flter_uncheck).getConstantState())) {
                imgFilterAudio.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                types.put(2, 2);
            } else {
                imgFilterAudio.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                types.remove(2);
            }

        }

        if (view.getId() == R.id.filterVideo || view.getId() == R.id.imgFilterVideo) {
            if (imgFilterVideo.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.flter_uncheck).getConstantState())) {
                imgFilterVideo.setImageDrawable(getResources().getDrawable(R.drawable.flter_check));
                types.put(1, 1);
            } else {
                imgFilterVideo.setImageDrawable(getResources().getDrawable(R.drawable.flter_uncheck));
                types.remove(1);
            }

        }

        filterByType();
    }

    TextView selectAll;
    TextView filterImg;
    TextView filterDoc;
    TextView filterAudio;
    TextView filterVideo;
    ImageView imgSelectAll;
    ImageView imgFilterImg;
    ImageView imgFilterDoc;
    ImageView imgFilterAudio;
    ImageView imgFilterVideo;
    Map<Integer, Integer> types = new HashMap<Integer, Integer>(0);
}
