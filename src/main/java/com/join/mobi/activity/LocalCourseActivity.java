package com.join.mobi.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.BaseActivity;
import com.j256.ormlite.dao.CloseableIterable;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.LocalCourseManager;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.db.tables.Reference;
import com.join.android.app.common.manager.DialogManager;
import com.join.android.app.common.utils.BeanUtils;
import com.join.mobi.adapter.LocalCourseAdapter;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午12:27
 * <p/>
 * 本地课程
 */
@EActivity(R.layout.localcourse_activity_layout)
public class LocalCourseActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    String TAG = getClass().getName();

    @Pref
    PrefDef_ myPref;
    @RestService
    RPCService rpcService;
    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    ListView listView;

    @ViewById
    EditText search;
    @ViewById
    ImageView searchIcon;

    LocalCourseAdapter mAdapter;
         
    List<LocalCourse> origLocalCourses = new ArrayList<LocalCourse>(0);
    List<LocalCourse> filterLocalCourses = new ArrayList<LocalCourse>(0);

    @AfterViews
    void afterViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mAdapter = new LocalCourseAdapter(this, null);
        listView.setAdapter(mAdapter);

        wrapEvent();
        showLoading();
        retrieveDataFromDB();
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
                filterLocalCourses.clear();

                if (charSequence.toString().equals("")) {
                    mAdapter.updateItems(origLocalCourses);
                } else {
                    for (LocalCourse course : origLocalCourses) {
                        if (course.getTitle().contains(charSequence)) {
                            LocalCourse _course = new LocalCourse();
                            BeanUtils.copyProperties(_course, course);
                            filterLocalCourses.add(_course);
                        }
                    }
                    mAdapter.updateItems(filterLocalCourses);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }

    @UiThread
    public void retrieveDataFromDB() {
        origLocalCourses.clear();
//        origLocalCourses = LocalCourseManager.getInstance().findAll();
        List<LocalCourse> temp = LocalCourseManager.getInstance().findAll();
        if(temp==null)return;

        for(LocalCourse course: temp){

            boolean existsContent = false;

            CloseableIterable<Chapter> closeableIterable = course.getChapters().getWrappedIterable();
            Iterator<Chapter> chapterIterator = closeableIterable.iterator();

            while (chapterIterator.hasNext()){
                Chapter c = chapterIterator.next();
                if(DownloadTool.isFinished((DownloadApplication)getApplicationContext(),c.getDownloadUrl())){
                    existsContent=true;
                    course.setChapterNum(course.getChapterNum()+1);
                    break;
                }
            }
            closeableIterable.closeableIterator();

            CloseableIterable<Reference> _closeableIterable = course.getReferences().getWrappedIterable();
            Iterator<Reference> refIterator = _closeableIterable.iterator();

            while (refIterator.hasNext()){
                Reference _c = refIterator.next();
                if(DownloadTool.isFinished((DownloadApplication)getApplicationContext(),_c.getUrl())){
                    existsContent=true;
                    course.setRefNum(course.getRefNum()+1);
                    break;
                }
            }
            _closeableIterable.closeableIterator();
            if(existsContent){
                origLocalCourses.add(course);
            }
        }

        mAdapter.updateItems(origLocalCourses);
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        dismissLoading();
    }


    @Click
    void backClicked() {
        finish();
    }

    @Click
    void trashClicked(){
        mAdapter.setTrash(!mAdapter.isTrash());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh() called.");
        retrieveDataFromDB();
    }

    @UiThread
    @Override
    public void rpcException(Throwable e) {
        DialogManager.getInstance().makeText(this, getString(R.string.net_excption), DialogManager.DIALOG_TYPE_WARRING);
        dismissLoading();
    }

    @Receiver(actions = "org.androidannotations.updateLearningTimeAfterCommitLog", registerAt = Receiver.RegisterAt.OnCreateOnDestroy)
    protected void onActionULTACRegisteredOnAttachOnDetach() {
        onRefresh();
    }

}
