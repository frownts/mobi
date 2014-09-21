package com.join.mobi.activity;

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
import com.join.mobi.dto.ChapterDto;
import com.join.mobi.dto.CourseDetailDto;
import com.join.mobi.fragment.*;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import com.join.mobi.rpc.RPCTestData;
import io.vov.vitamio.LibsChecker;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

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
    @AfterViews
    void afterViews() {
        title.setText(name);

        //加载数据
        retrieveDataFromServer();
        loading = new CommonDialogLoading(this);
        loading.show();

    }

    private void play() {
        //todo 播放的代码

        if (!LibsChecker.checkVitamioLibs(this))
            return;

//        initVideo();
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
        videoFragment = new VideoFragment_();




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
        transaction.replace(R.id.fragmentVideo,videoFragment);
        transaction.commit();
        loading.dismiss();
        //开始播放
        play();
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
            for (ChapterDto chapterDto : courseDetailDto.getChapters()) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        //todo 提交
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentVideo,new BlankFragment_());
        transaction.commit();

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


        super.onResume();


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
