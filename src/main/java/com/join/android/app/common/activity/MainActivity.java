package com.join.android.app.common.activity;

import android.content.Intent;
import android.widget.Button;
import com.BaseActivity;
import com.join.android.app.common.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-26
 * Time: 上午11:32
 */

@EActivity(R.layout.main_activity_layout)
public class MainActivity extends BaseActivity {

    @ViewById
    Button btnImgListView;
    @ViewById
    Button btnDB;
    @ViewById
    Button btnPref;
    @ViewById
    Button btnRestFul;
    @ViewById
    Button btnFragment;
    @ViewById
    Button btnAA;
    @ViewById
    Button btnProgressBar;

    @AfterViews
    void afterViews() {

    }

    @Click
    void btnImgListViewClicked() {
        ImageCacheActivity_.intent(this).start();
    }

    @Click
    void btnDBClicked() {
        DBActivity_.intent(this).start();
    }

    @Click
    void btnFragmentClicked() {
        MyFragmentActivity_.intent(this).start();
    }

    @Click
    void btnAAClicked() {
        MyActivity_.intent(this).start();
    }

    @Click
    void btnPrefClicked() {
        PrefActivity_.intent(this).start();
    }

    @Click
    void btnRestFulClicked() {
        RestFulActivity_.intent(this).start();
    }

    @Click
    void btnProgressBarClicked() {
        ProgressActivity_.intent(this).start();
    }

    @Click
    void mediaPlayClicked(){
        VideoViewBuffer_.intent(this).start();
    }


}
