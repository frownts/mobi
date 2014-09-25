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
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.LiveManager;
import com.join.android.app.common.db.tables.Live;
import com.join.android.app.common.manager.DialogManager;
import com.join.android.app.common.utils.BeanUtils;
import com.join.mobi.adapter.LiveAdapter;
import com.join.mobi.dto.MainContentDto;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午12:27
 * <p/>
 * 直播
 */
@EActivity(R.layout.live_activity_layout)
public class LiveActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
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

    LiveAdapter mAdapter;

    List<Live> origLives = new ArrayList<Live>(0);
    List<Live> filterLives = new ArrayList<Live>(0);
    MainContentDto mainContent;


    @AfterViews
    void afterViews() {
        setWebService(rpcService);
        setPref(myPref);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        origLives = LiveManager.getInstance().findAll();
        mAdapter = new LiveAdapter(this, origLives);
        listView.setAdapter(mAdapter);

        wrapEvent();
        try{
            showLoading();
        }catch (Exception e){}

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
                filterLives.clear();

                if (charSequence.toString().equals("")) {
                    mAdapter.updateItems(origLives);
                } else {
                    for (Live live : origLives) {
                        if (live.getTitle().contains(charSequence)) {
                            Live _live = new Live();
                            BeanUtils.copyProperties(_live, live);
                            filterLives.add(_live);
                        }
                    }
                    mAdapter.updateItems(filterLives);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Background
    void retrieveDataFromServer() {
        mainContent = refreshMainData();
        updateView();
    }

    @UiThread
    public void updateView() {
        origLives = LiveManager.getInstance().findAll();

        mAdapter.updateItems(origLives);
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        dismissLoading();
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

    @UiThread
    @Override
    public void rpcException(Throwable e) {
        DialogManager.getInstance().makeText(this, getString(R.string.net_excption), DialogManager.DIALOG_TYPE_WARRING);
        dismissLoading();
    }

}
