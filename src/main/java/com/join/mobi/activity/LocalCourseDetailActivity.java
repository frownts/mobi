package com.join.mobi.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.LocalCourseManager;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.dialog.CommonDialogLoading;
import com.join.mobi.fragment.LocalCourseChapterFragment_;
import com.join.mobi.fragment.LocalCourseDetailFragment_;
import com.join.mobi.fragment.LocalCourseReferenceFragment_;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午10:25
 * 本地课程
 */
@EActivity(R.layout.localcourse_detail_activity_layout)
public class LocalCourseDetailActivity extends FragmentActivity {

    @Pref
    PrefDef_ myPref;
    @RestService
    RPCService rpcService;

    @ViewById
    ImageView back;
    @ViewById
    TextView detailTab;
    @ViewById
    TextView title;
    @ViewById
    TextView chapterTab;
    @ViewById
    TextView examTab;
    @ViewById
    TextView referenceTab;

    @ViewById
    ImageView curveMarkerDetail;
    @ViewById
    ImageView curveMarkerChapter;
    @ViewById
    ImageView curveMarkerExam;
    @ViewById
    ImageView curveMarkerReference;

    @ViewById
    ImageView trash;

    @Extra
    String courseId;

    @Extra
    String name;

    LocalCourse localCourse;
    LocalCourseDetailFragment_ localCourseDetailFragment;
    LocalCourseChapterFragment_ localCourseChapterFragment;
    LocalCourseReferenceFragment_ localCourseReferenceFragment;

    FragmentManager fragmentManager;
    Fragment currentFragment;

    CommonDialogLoading loading;

    boolean trashShowing;

    @AfterViews
    void afterViews() {
        title.setText(name);
        List<LocalCourse> courseList =  LocalCourseManager.getInstance().findByCourseId(courseId);
        if(courseList!=null&&courseList.size()>0) localCourse = courseList.get(0);
        afterRetrieveDataFromServer();
    }


    void afterRetrieveDataFromServer() {
        fragmentManager = getSupportFragmentManager();
        localCourseDetailFragment = new LocalCourseDetailFragment_();
        localCourseChapterFragment = new LocalCourseChapterFragment_();
        localCourseReferenceFragment = new LocalCourseReferenceFragment_();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.liveCourseFragment, localCourseDetailFragment);
        transaction.add(R.id.liveCourseFragment, localCourseChapterFragment);
        transaction.add(R.id.liveCourseFragment, localCourseReferenceFragment);
        transaction.hide(localCourseReferenceFragment);
        transaction.hide(localCourseChapterFragment);
        transaction.show(localCourseDetailFragment);
        currentFragment = localCourseDetailFragment;

        transaction.commit();
//        loading.dismiss();
    }

    @Click
    void detailTabClicked() {
        switchFragment(localCourseDetailFragment);
        invisibleAll();
        curveMarkerDetail.setVisibility(View.VISIBLE);
    }

    @Click
    void chapterTabClicked() {
        switchFragment(localCourseChapterFragment);
        invisibleAll();
        curveMarkerChapter.setVisibility(View.VISIBLE);
    }

    @Click
    void referenceTabClicked() {
        switchFragment(localCourseReferenceFragment);
        invisibleAll();
        curveMarkerReference.setVisibility(View.VISIBLE);
    }

    @Click
    void backClicked() {
        finish();
    }


    @Click
    void trashClicked(){
        trashShowing = !trashShowing;
        Intent intent = new Intent();
        intent.setAction("org.androidannotations.ACTION_1");
        sendBroadcast(intent);
    }

    void invisibleAll() {
        curveMarkerDetail.setVisibility(View.INVISIBLE);
        curveMarkerChapter.setVisibility(View.INVISIBLE);
        curveMarkerReference.setVisibility(View.INVISIBLE);
    }

    void switchFragment(Fragment to) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(currentFragment).show(to).commit();
        currentFragment = to;
    }

    public LocalCourse getLocalCourse() {
        return localCourse;
    }

    public void setLocalCourse(LocalCourse localCourse) {
        this.localCourse = localCourse;
    }

    public boolean isTrashShowing() {
        return trashShowing;
    }

    public void setTrashShowing(boolean trashShowing) {
        this.trashShowing = trashShowing;
    }
}
