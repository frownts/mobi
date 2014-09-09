package com.join.mobi.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.CourseManager;
import com.join.mobi.adapter.LiveCourseAdapter;
import com.join.mobi.dto.MainContentDto;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import com.join.mobi.rpc.RPCTestData;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午12:27
 */
@EActivity(R.layout.livecourse_activity_layout)
public class LiveCourseActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    String TAG = getClass().getName();

    @Pref
    PrefDef_ myPref;
    @RestService
    RPCService rpcService;

    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    ListView listView;

    LiveCourseAdapter mAdapter;

    @AfterViews
    void afterViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mAdapter = new LiveCourseAdapter(this, CourseManager.getInstance().findAll());
        listView.setAdapter(mAdapter);

        swipeRefreshLayout.performClick();
    }

    @Background
    void retrieveDataFromServer() {
//        MainContentDto mainContent = rpcService.getMainContent(myPref.userId().get());
        MainContentDto mainContent = RPCTestData.getMainContentDto();

        updateMainContent(mainContent);
        updateView();
    }
    @UiThread
    public void updateView() {
        mAdapter.updateItems(CourseManager.getInstance().findAll());
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Click
    void backClicked() {
        finish();
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh() called.");
        retrieveDataFromServer();
    }

}
