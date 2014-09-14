package com.join.mobi.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.dialog.CommonDialogLoading;
import com.join.mobi.dto.CourseDetailDto;
import com.join.mobi.fragment.LiveCourseChapterFragment_;
import com.join.mobi.fragment.LiveCourseDetailFragment_;
import com.join.mobi.fragment.LiveCourseExamFragment_;
import com.join.mobi.fragment.LiveCourseReferenceFragment_;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import com.join.mobi.rpc.RPCTestData;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午10:25
 */
@EActivity(R.layout.livecourse_detail_activity_layout)
public class LiveCourseDetailActivity extends FragmentActivity {

    public static final String EXTRA_COURSE_ID = "myStringExtra";
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

    @Extra(EXTRA_COURSE_ID)
    String courseId;

    @Extra
    String name;

    LiveCourseDetailFragment_ liveCourseDetailFragment;
    LiveCourseChapterFragment_ liveCourseChapterFragment;
    LiveCourseExamFragment_ liveCourseExamFragment;
    LiveCourseReferenceFragment_ liveCourseReferenceFragment;

    FragmentManager fragmentManager;
    Fragment currentFragment;

    CourseDetailDto courseDetail;
    CommonDialogLoading loading;

    @AfterViews
    void afterViews() {
        title.setText(name);

        //加载数据
        retrieveDataFromServer();
        loading = new CommonDialogLoading(this);
        loading.show();
    }

    @Background
    void retrieveDataFromServer() {
//        courseDetail = rpcService.getCourseDetail(myPref.userId().get(),courseId);
        courseDetail = RPCTestData.getCourseDetailDto();
        afterRetrieveDataFromServer();

    }

    @UiThread
    void afterRetrieveDataFromServer() {
        fragmentManager = getSupportFragmentManager();
        liveCourseDetailFragment = new LiveCourseDetailFragment_();
        liveCourseChapterFragment = new LiveCourseChapterFragment_();
        liveCourseExamFragment = new LiveCourseExamFragment_();
        liveCourseReferenceFragment = new LiveCourseReferenceFragment_();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.liveCourseFragment, liveCourseDetailFragment);
        transaction.add(R.id.liveCourseFragment, liveCourseChapterFragment);
        transaction.add(R.id.liveCourseFragment, liveCourseExamFragment);
        transaction.add(R.id.liveCourseFragment, liveCourseReferenceFragment);
        transaction.hide(liveCourseReferenceFragment);
        transaction.hide(liveCourseExamFragment);
        transaction.hide(liveCourseChapterFragment);
        transaction.show(liveCourseDetailFragment);
        currentFragment = liveCourseDetailFragment;

        transaction.commit();
        loading.dismiss();
    }

    @Click
    void detailTabClicked() {
        switchFragment(liveCourseDetailFragment);
        invisibleAll();
        curveMarkerDetail.setVisibility(View.VISIBLE);
    }

    @Click
    void chapterTabClicked() {
        switchFragment(liveCourseChapterFragment);
        invisibleAll();
        curveMarkerChapter.setVisibility(View.VISIBLE);
    }

    @Click
    void examTabClicked() {
        switchFragment(liveCourseExamFragment);
        invisibleAll();
        curveMarkerExam.setVisibility(View.VISIBLE);
    }

    @Click
    void referenceTabClicked() {
        switchFragment(liveCourseReferenceFragment);
        invisibleAll();
        curveMarkerReference.setVisibility(View.VISIBLE);
    }

    @Click
    void backClicked(){finish();}

    void invisibleAll() {
        curveMarkerDetail.setVisibility(View.INVISIBLE);
        curveMarkerChapter.setVisibility(View.INVISIBLE);
        curveMarkerExam.setVisibility(View.INVISIBLE);
        curveMarkerReference.setVisibility(View.INVISIBLE);
    }

    void switchFragment(Fragment to) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(currentFragment).show(to).commit();
        currentFragment = to;
    }

    public CourseDetailDto getCourseDetail() {
        return courseDetail;
    }


//    @Override
//    protected void onDestroy() {
//        loading = null;
//        super.onDestroy();
//    }

}
