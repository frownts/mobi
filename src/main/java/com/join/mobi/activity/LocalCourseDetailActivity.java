package com.join.mobi.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.j256.ormlite.dao.CloseableIterable;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.LocalCourseManager;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.dialog.CommonDialogLoading;
import com.join.android.app.common.utils.DateUtils;
import com.join.mobi.fragment.*;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.*;

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

    @Extra
    long seekTo;

    private String playUrl;

    LocalCourse localCourse;
    LocalCourseDetailFragment_ localCourseDetailFragment;
    LocalCourseChapterFragment_ localCourseChapterFragment;
    LocalCourseReferenceFragment_ localCourseReferenceFragment;
    VideoFragment_ videoFragment;

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

        List<Chapter> chapters = new ArrayList<Chapter>(0);
        CloseableIterable<Chapter> closeableIterable = localCourse.getChapters().getWrappedIterable();
        Iterator<Chapter> chapterIterator = closeableIterable.iterator();

        while (chapterIterator.hasNext()) {
            Chapter c = chapterIterator.next();
            if (DownloadTool.isFinished((DownloadApplication) getApplicationContext(), c.getDownloadUrl())) {
                chapters.add(c);
                play(DownloadTool.getFileByUrl((DownloadApplication)this.getApplicationContext(),c.getDownloadUrl()));
//                play(c.getDownloadUrl());
                break;
            }
        }
        closeableIterable.closeableIterator();

    }

    public void play(String url){
        playUrl = url;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        videoFragment = new VideoFragment_();
        transaction.replace(R.id.fragmentVideo,videoFragment);
        transaction.commit();
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

    @Background
    void commitLearningLog(){
        try{
            //提交学习记录
            rpcService.commitLearningLog(myPref.rpcUserId().get(), DateUtils.ConvertDateToNormalString(new Date()),localCourseDetailFragment.getDuration()+"",courseId,localCourseChapterFragment.getLastChapterId()+"","0");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try{
            //更新本地数据库
            Map<String, Object> params = new HashMap<String, Object>(0);
            params.put("courseId", courseId);
            List<LocalCourse> localCourses = LocalCourseManager.getInstance().findForParams(params);
            if(localCourses!=null&&localCourses.size()>0){

                //更新学习总时长
                localCourses.get(0).setLearningTimes(localCourses.get(0).getLearningTimes()+(int)localCourseDetailFragment.getDuration());
                LocalCourseManager.getInstance().saveOrUpdate(localCourses.get(0));
                Intent i = new Intent("org.androidannotations.updateLearningTimeAfterCommitLog");
                sendBroadcast(i);
            }
        }catch (Exception e){

        }
        commitLearningLog();
        super.onDestroy();
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

        if(playUrl!=null)
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
        playUrl = intent.getExtras().getString("playUrl");
        replay();
    }

    /**
     * 接收全屏返回回来时的当前播放时间
     */
    @Receiver(actions = "org.androidannotations.seekTo", registerAt = Receiver.RegisterAt.OnCreateOnDestroy)
    public void seekTo(Intent intent){
        seekTo = intent.getExtras().getLong("seekTo");
    }

    public long getSeekTo() {
        return seekTo;
    }

    public void setSeekTo(long seekTo) {
        this.seekTo = seekTo;
    }
}

