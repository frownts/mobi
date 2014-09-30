package com.join.mobi.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.dialog.CommonDialogLoading;
import com.join.android.app.common.utils.DateUtils;
import com.join.mobi.dto.ChapterDto;
import com.join.mobi.dto.CourseDetailDto;
import com.join.mobi.fragment.*;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午10:25
 * 在线课程
 */
@EActivity(R.layout.livecourse_detail_activity_layout)
public class LiveCourseDetailActivity extends FragmentActivity  {

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
    View main;
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
    /**
     * 课程海报
     */
    @Extra
    String url;
    @Extra
    String name;
    @Extra
    long seekTo;

    private String playUrl;
    LiveCourseDetailFragment_ liveCourseDetailFragment;
    LiveCourseChapterFragment_ liveCourseChapterFragment;
    LiveCourseExamFragment_ liveCourseExamFragment;
    LiveCourseReferenceFragment_ liveCourseReferenceFragment;
    VideoFragment_ videoFragment;
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

    public void play(String url){
        videoFragment=null;
        System.gc();
        playUrl = url;
        if(StringUtils.isEmpty(playUrl))return;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        videoFragment = new VideoFragment_();
        transaction.replace(R.id.fragmentVideo,videoFragment);
        transaction.commit();
    }

    @Background
    void retrieveDataFromServer() {
        courseDetail = rpcService.getCourseDetail(myPref.rpcUserId().get(),courseId);
        afterRetrieveDataFromServer();
        if(courseDetail.getChapter()!=null&&courseDetail.getChapter().size()>0){
            ChapterDto chapter = courseDetail.getChapter().get(0);
            if(chapter.getChapter()!=null&&chapter.getChapter().size()>0&&StringUtils.isEmpty(chapter.getDownloadUrl()))
                play(chapter.getChapter().get(0).getDownloadUrl());
            else
            play(chapter.getDownloadUrl());
        }
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
    void backClicked() {
        finish();
    }

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

    public String getUrl() {
        return url;
    }

    @Override
    protected void onStop() {
        updateLearningTime();
        super.onStop();
    }

    void updateLearningTime() {
        CourseDetailDto courseDetailDto = liveCourseChapterFragment.getCourseDetailDto();
        if (courseDetailDto != null) {
            //更新到数据库中
            for (ChapterDto chapterDto : courseDetailDto.getChapter()) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        commitLearningLog();
        Intent i = new Intent("org.androidannotations.updateLearningTimeAfterCommitLog");
        sendBroadcast(i);
        super.onDestroy();
    }

    @Background
    void commitLearningLog(){
        try{
            //提交学习记录
            rpcService.commitLearningLog(myPref.rpcUserId().get(), DateUtils.ConvertDateToNormalString(new Date()),liveCourseDetailFragment.getDuration()+"",courseId,liveCourseChapterFragment.getLastChapterId()+"","0");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        replay();
        super.onResume();
    }

    public void replay(){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentVideo,new BlankFragment_());
        transaction.commit();

        if(StringUtils.isNotEmpty(playUrl))
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(1000);
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragmentVideo,new VideoFragment_());
                        transaction.commit();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    /**
     * 当播放进行时，更新进度和学习时间
     */
    @Receiver(actions = "org.androidannotations.play", registerAt = Receiver.RegisterAt.OnCreateOnDestroy)
    public void play(Intent intent){
        if(intent==null)return;
        playUrl = intent.getExtras().getString("playUrl");
        if(StringUtils.isEmpty(playUrl))return;
        replay();
    }

    /**
     * 接收全屏返回回来时的当前播放时间
     */
    @Receiver(actions = "org.androidannotations.seekTo", registerAt = Receiver.RegisterAt.OnCreateOnDestroy)
    public void seekTo(Intent intent){

        seekTo = intent.getExtras().getLong("seekTo",0);
    }

    @Receiver(actions = "org.androidannotations.downloadCompelte", registerAt = Receiver.RegisterAt.OnResumeOnPause)
    public void downLoadComplete(Intent i) {
        showDownLoadHint(main,i.getExtras().getString("name"));
    }

    private PopupWindow downLoadCompleteHint;

    public void showDownLoadHint(View anchor,String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_download_complete, null);
        ((TextView) view.findViewById(R.id.title)).setText(title);

        downLoadCompleteHint = new PopupWindow(view, getWindow().getWindowManager().getDefaultDisplay().getWidth(), 150, false);
        downLoadCompleteHint.setOutsideTouchable(true);
        downLoadCompleteHint.showAtLocation(anchor, Gravity.NO_GRAVITY,0,0);
        dismissPopUp();
    }

    @UiThread(delay = 2000)
    void dismissPopUp(){
        if (downLoadCompleteHint != null && downLoadCompleteHint.isShowing()) {
            try{
                downLoadCompleteHint.dismiss();
            }catch (Exception e){

            }


        }
    }

    public long getSeekTo() {
        return seekTo;
    }

    public void setSeekTo(long seekTo) {
        this.seekTo = seekTo;
    }
}
