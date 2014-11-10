package com.join.mobi.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
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
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午10:25
 * 本地课程
 */
@EActivity(R.layout.localcourse_detail_activity_layout)
public class LocalCourseDetailActivity extends FragmentActivity implements MediaPlayer.OnInfoListener{

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
    View frameContainer;

    @ViewById
    ImageView trash;
    @Extra
    String courseId;
    @Extra
    String name;
    @Extra
    long seekTo;

    //视频部分
    @ViewById
    RelativeLayout videoContainer;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    SeekBar seekbar;
    @ViewById
    ImageView issrt;
    @ViewById
    ImageView centerPlay;
    @ViewById
    RelativeLayout btm;
    @ViewById
    SurfaceView surface;
    @ViewById
    ImageView fullScreen;
    @ViewById
    View header;
    private int postion = 0;

    private String playUrl;

    LocalCourse localCourse;
    LocalCourseDetailFragment_ localCourseDetailFragment;
    LocalCourseChapterFragment_ localCourseChapterFragment;
    LocalCourseReferenceFragment_ localCourseReferenceFragment;
    VideoFragment_ videoFragment;

    FragmentManager fragmentManager;
    Fragment currentFragment;
    CommonDialogLoading loading;
    Thread threadUpdateGrogress;

    boolean trashShowing;

    @AfterViews
    void afterViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                play(DownloadTool.getFileByUrl((DownloadApplication) this.getApplicationContext(), c.getDownloadUrl()));
                break;
            }
        }
        closeableIterable.closeableIterator();
        initPlayer();
        startUpdateLearningTime();
    }

    private void initPlayer() {
        if(update==null)
            update = new upDateSeekBar(); // 创建更新进度条对象
        mediaPlayer = new MediaPlayer();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mSurfaceViewWidth = dm.widthPixels;
        int mSurfaceViewHeight = dm.heightPixels;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.width = mSurfaceViewWidth;
        lp.height = mSurfaceViewHeight * 1 / 3;
        surface.setLayoutParams(lp);
        surface.getHolder().setFixedSize(lp.width, lp.height);
        surface.getHolder().setKeepScreenOn(true);
        surface.getHolder().addCallback(new SurfaceViewLis());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });

        mediaPlayer.setOnInfoListener(this);
        seekbar.setOnSeekBarChangeListener(new surfaceSeekBar());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.start();
            }

        });
        mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
    }

    @Click
    void videoContainerClicked(){
        if (centerPlay.getVisibility() == View.VISIBLE) {
            try {
                centerPlay.setVisibility(View.GONE);
                play();
                issrt.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            if (btm.getVisibility() == View.VISIBLE)
                btm.setVisibility(View.GONE);
            else
                btm.setVisibility(View.VISIBLE);
        }
    }

    @Click
    void fullScreenClicked(){
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Click
    void issrtClicked(){

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            issrt.setImageDrawable(getResources().getDrawable(R.drawable.player));
        } else {
            mediaPlayer.start();
            issrt.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        }
    }

    @UiThread
    public void play(String url){

        playUrl = url;
        if(StringUtils.isEmpty(playUrl))return;
        try {
            mediaPlayer.setDataSource(playUrl);
            play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void play(String url){
//        playUrl = url;
//        fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        videoFragment = new VideoFragment_();
//        transaction.replace(R.id.fragmentVideo,videoFragment);
//        transaction.commit();
//    }


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


    private void startUpdateLearningTime() {
        //开始播放了,更新学习时间
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (mediaPlayer != null) {
                        try{
                            while (mediaPlayer.isPlaying()) {
                                //更新学习时间
                                Intent intent = new Intent("org.androidannotations.updateLearningTime");
                                sendBroadcast(intent);
                                Thread.sleep(1000);
                            }
                        }catch (java.lang.IllegalStateException e){

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
        super.onResume();
        mediaPlayer.reset();
        play(playUrl);

    }


//    public void replay(){
//        fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragmentVideo,new BlankFragment_());
//        transaction.commit();
//
//        if(playUrl!=null)
//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        Thread.sleep(1000);
//                        FragmentTransaction transaction = fragmentManager.beginTransaction();
//                        transaction.replace(R.id.fragmentVideo,new VideoFragment_());
//                        transaction.commit();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }.start();
//    }

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

    //视频总部代码
    private MediaPlayer mediaPlayer;
    private upDateSeekBar update; // 更新进度条用
    private boolean isplayingFlag = true; // 用于判断视频是否在播放中


    class upDateSeekBar implements Runnable {

        @Override
        public void run() {
            mHandler.sendMessage(Message.obtain());
            if (isplayingFlag) {
                mHandler.postDelayed(update, 1000);
            }
        }
    }

    /**
     * 更新进度条
     */
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (mediaPlayer == null) {
                isplayingFlag = false;
            } else if (mediaPlayer.isPlaying()) {
                isplayingFlag = true;

                int position = mediaPlayer.getCurrentPosition();
                int mMax = mediaPlayer.getDuration();
                int sMax = seekbar.getMax();
                seekbar.setProgress(position * sMax / mMax);
            } else {
                return;
            }
        }
    };

    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {
        if(threadUpdateGrogress==null)
            threadUpdateGrogress = new Thread(update);
        try{
            threadUpdateGrogress.start();
        }catch (java.lang.IllegalThreadStateException e){}

        progressBar.setVisibility(View.VISIBLE);
        mediaPlayer.prepareAsync();
    }

    private final class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            progressBar.setVisibility(View.GONE);
            mediaPlayer.seekTo(0);
            seekbar.setProgress(0);
            issrt.setImageDrawable(getResources().getDrawable(R.drawable.player));
        }
    }

    private class SurfaceViewLis implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (postion == 0) {
                try {
                    // 把视频输出到SurfaceView上
                    mediaPlayer.setDisplay(surface.getHolder());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 检测屏幕的方向：纵向或横向
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 当前为横屏， 在此处添加额外的处理代码
            btm.setVisibility(View.GONE);
            frameContainer.setVisibility(View.GONE);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int mSurfaceViewWidth = dm.widthPixels;
            int mSurfaceViewHeight = dm.heightPixels;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.width = mSurfaceViewWidth;
            lp.height = mSurfaceViewHeight;

            surface.setLayoutParams(lp);
            surface.getHolder().setFixedSize(mSurfaceViewWidth,
                    mSurfaceViewHeight);

            videoContainer.setLayoutParams(lp);

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 当前为竖屏， 在此处添加额外的处理代码
            header.setVisibility(View.VISIBLE);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int mSurfaceViewWidth = dm.widthPixels;
            int mSurfaceViewHeight = dm.heightPixels;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.FILL_PARENT);

            lp.width = mSurfaceViewWidth;
            lp.height = mSurfaceViewHeight * 1 / 3;
            lp.addRule(RelativeLayout.BELOW,R.id.header);
            videoContainer.setLayoutParams(lp);
            frameContainer.setVisibility(View.VISIBLE);

        }
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {

        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                issrt.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                progressBar.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                progressBar.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                progressBar.setVisibility(View.GONE);
                break;
            default:
                progressBar.setVisibility(View.VISIBLE);
                break;
        }
        if(mediaPlayer.isPlaying()){
            progressBar.setVisibility(View.GONE);
            issrt.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            return true;
        }
        return true;
    }

    private final class surfaceSeekBar implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            seekBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            int value = seekbar.getProgress() * mediaPlayer.getDuration() // 计算进度条需要前进的位置数据大小
                    / seekbar.getMax();
            mediaPlayer.seekTo(value);
            progressBar.setVisibility(View.VISIBLE);
        }

    }
}

