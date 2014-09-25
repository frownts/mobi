package com.join.mobi.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
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

    LiveCourseDetailFragment_ liveCourseDetailFragment;
    LiveCourseChapterFragment_ liveCourseChapterFragment;
    LiveCourseExamFragment_ liveCourseExamFragment;
    LiveCourseReferenceFragment_ liveCourseReferenceFragment;
    VideoFragment_ videoFragment;

    FragmentManager fragmentManager;
    Fragment currentFragment;

    CourseDetailDto courseDetail;
    CommonDialogLoading loading;
    //从服务器端取到的原总学习时间
    long origLearningTime;

//    private String path = "http://192.168.1.104/apple.mp4";
//    @ViewById(resName = "buffer")
//     VideoView mVideoView;
//    @ViewById(resName = "probar")
//     ProgressBar pb;
//    @ViewById(resName = "download_rate")
//     TextView downloadRateView;
//    @ViewById(resName = "load_rate")
//     TextView loadRateView;
    @Extra
    long seekTo;
    private Uri uri;

    private String playUrl;
    @AfterViews
    void afterViews() {
        title.setText(name);

        //加载数据
        retrieveDataFromServer();
        loading = new CommonDialogLoading(this);
        loading.show();

    }

    public void play(String url){
        playUrl = url;
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

        if(courseDetail.getChapter()!=null&&courseDetail.getChapter().size()>0)
        play(courseDetail.getChapter().get(0).getDownloadUrl());
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

    //    void initVideo() {
////        uri = Uri.parse(path);
//
////      mVideoView.setVideoURI(uri);
//
//        mVideoView.setVideoPath(path);
//        MediaController mediaController = new MediaController(this){
//
//        };
//
//        mediaController.setMediaPlayerControlFullScreen(new MediaController.MediaPlayerControlFullScreen() {
//            @Override
//            public void onFullScreen() {
//                MyVideoViewBufferFullScreen_.intent(LiveCourseDetailActivity.this).seekTo(mVideoView.getCurrentPosition()).start();
//                mVideoView.stopPlayback();
//                finish();
//            }
//
//
//        });
//
//        mVideoView.setMediaController(mediaController);
//        mVideoView.requestFocus();
//        mVideoView.setOnInfoListener(this);
//        mVideoView.setOnBufferingUpdateListener(this);
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                // optional need Vitamio 4.0
//                mediaPlayer.setPlaybackSpeed(1.0f);
//                mediaPlayer.seekTo(seekTo);
//            }
//        });
//    }

//    @Override
//    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//        switch (what) {
//            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                if (mVideoView.isPlaying()) {
//                    mVideoView.pause();
//                    pb.setVisibility(View.VISIBLE);
//                    downloadRateView.setText("");
//                    loadRateView.setText("");
//                    downloadRateView.setVisibility(View.VISIBLE);
//                    loadRateView.setVisibility(View.VISIBLE);
//
//                }
//                break;
//            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                mVideoView.start();
//                pb.setVisibility(View.GONE);
//                downloadRateView.setVisibility(View.GONE);
//                loadRateView.setVisibility(View.GONE);
//                startUpdateLearningTime();
//                break;
//            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//                downloadRateView.setText("" + extra + "kb/s" + "  ");
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        loadRateView.setText(percent + "%");
//    }

//    private void startUpdateLearningTime(){
//        //开始播放了,更新学习时间
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    while (mVideoView!=null){
//                        while(mVideoView.isPlaying()){
//                            //更新学习时间
//                            Intent intent = new Intent("org.androidannotations.updateLearningTime");
//                            LiveCourseDetailActivity.this.sendBroadcast(intent);
//                            Thread.sleep(1000);
//                        }
//                    }
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

}
