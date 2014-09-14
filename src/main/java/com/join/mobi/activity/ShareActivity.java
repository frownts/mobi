package com.join.mobi.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.ResourceShareManager;
import com.join.android.app.common.db.tables.ResourceShare;
import com.join.android.app.common.manager.DialogManager;
import com.join.android.app.common.utils.BeanUtils;
import com.join.mobi.adapter.ShareAdapter;
import com.join.mobi.dto.MainContentDto;
import org.androidannotations.annotations.*;

import java.util.*;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-13
 * Time: 下午5:10
 */

@EActivity(R.layout.share_activity_layout)
public class ShareActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @ViewById
    LinearLayout titleContainer;
    @ViewById
    EditText search;
    @ViewById
    ImageView searchIcon;
    @ViewById
    ImageView filterCup;
    @ViewById
    GridView listView;
    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;

    MainContentDto mainContent = null;
    ShareAdapter shareAdapter;

    List<ResourceShare> origResourceShare = new ArrayList<ResourceShare>(0);
    List<ResourceShare> filterResourceShare = new ArrayList<ResourceShare>(0);
    PopupWindow popFilter;
    PopupWindow popDownloadHint;

    @AfterViews
    void afterViews() {
        wrapEvent();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        shareAdapter = new ShareAdapter(this);
        origResourceShare = ResourceShareManager.getInstance().findAll();

        listView.setAdapter(shareAdapter);

        showLoading();
        retrieveDataFromServer();
    }

    @ItemClick
    void listViewItemClicked(ResourceShare resourceShare) {

        //数据库中检查是否正在下载

        View view = LayoutInflater.from(this).inflate(R.layout.pop_share_download, null);
        popDownloadHint = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popDownloadHint.setOutsideTouchable(true);
        popDownloadHint.showAsDropDown(titleContainer);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    dismissDownloadHint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @UiThread
    void dismissDownloadHint() {
        if (popDownloadHint != null && popDownloadHint.isShowing()) popDownloadHint.dismiss();
    }

    @Background
    void retrieveDataFromServer() {
        mainContent = refreshMainData();
        updateView();
    }


    @UiThread
    public void updateView() {
        origResourceShare = ResourceShareManager.getInstance().findAll();
        shareAdapter.setItems(origResourceShare);
        shareAdapter.notifyDataSetChanged();
        dismissLoading();
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

    @UiThread
    @Override
    public void rpcException(Throwable e) {
        DialogManager.getInstance().makeText(this, getString(R.string.net_excption), DialogManager.DIALOG_TYPE_WARRING);
        dismissLoading();
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
                filterResourceShare.clear();

                if (charSequence.toString().equals("")) {
                    shareAdapter.setItems(origResourceShare);
                } else {
                    for (ResourceShare share : origResourceShare) {
                        if (share.getName().contains(charSequence)) {
                            ResourceShare _share = new ResourceShare();
                            BeanUtils.copyProperties(_share, share);
                            filterResourceShare.add(_share);
                        }
                    }
                    shareAdapter.setItems(filterResourceShare);
                }
                shareAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }


    void filterByType() {
        filterResourceShare.clear();

        Iterator keys = types.keySet().iterator();
        while (keys.hasNext()) {
            int _type = types.get(keys.next());
            for (ResourceShare share : origResourceShare) {
                if (_type == share.getType()) {
                    ResourceShare _share = new ResourceShare();
                    BeanUtils.copyProperties(_share, share);
                    filterResourceShare.add(_share);
                }
            }
        }
        shareAdapter.setItems(filterResourceShare);
        shareAdapter.notifyDataSetChanged();
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
