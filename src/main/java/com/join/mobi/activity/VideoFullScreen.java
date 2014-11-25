package com.join.mobi.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import com.join.android.app.common.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Created by mawj on 2014/11/24.
 */
@EActivity(R.layout.video)
public class VideoFullScreen extends Activity implements MediaPlayer.OnInfoListener{

    @Extra
    String path  = "http://192.168.85.82:8080/b.mp4";

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
    private int postion = 0;
//    private String playUrl;
    private MediaPlayer mediaPlayer;
    private upDateSeekBar update; // 更新进度条用
    private boolean isplayingFlag = true; // 用于判断视频是否在播放中
    Thread threadUpdateGrogress;
    Thread checkProgress;



    @AfterViews
    void afterViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            initPlayer();
            play();
//            startUpdateLearningTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initPlayer() {
        if(update==null)
            update = new upDateSeekBar(); // 创建更新进度条对象
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mSurfaceViewWidth = dm.widthPixels;
        int mSurfaceViewHeight = dm.heightPixels;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.width = mSurfaceViewWidth;
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
            public void onPrepared(final MediaPlayer mediaPlayer) {
                mediaPlayer.start();

                checkProgress = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        while(true){
                            try {
                                Thread.sleep(1000);
                                if(mediaPlayer.isPlaying()){
                                    //todo xxx  progressBar.setVisibility(View.GONE);
                                    mHandler.sendEmptyMessage(0);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                checkProgress.start();
            }

        });
        mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());


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

    /**
     * 更新进度条
     */
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (mediaPlayer == null) {
                isplayingFlag = false;
            } else if (mediaPlayer.isPlaying()) {
                issrt.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                progressBar.setVisibility(View.GONE);
                isplayingFlag = true;

                int position = mediaPlayer.getCurrentPosition();
                int mMax = mediaPlayer.getDuration();
                int sMax = seekbar.getMax();
                if(mMax==0)return;
                seekbar.setProgress(position * sMax / mMax);
            } else {
                issrt.setImageDrawable(getResources().getDrawable(R.drawable.player));
                isplayingFlag = false;
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

    class upDateSeekBar implements Runnable {

        @Override
        public void run() {
            mHandler.sendMessage(Message.obtain());
            if (mediaPlayer.isPlaying()) {
                mHandler.postDelayed(update, 1000);
            }
        }
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

//        private void startUpdateLearningTime(){
//        //开始播放了,更新学习时间
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//
//                    while(mediaPlayer!=null){
//                        while(mediaPlayer!=null&&mediaPlayer.isPlaying()){
//                            //更新学习时间
//                            Intent intent = new Intent("org.androidannotations.updateLearningTime");
//                            VideoFullScreen.this.sendBroadcast(intent);
//                            Thread.sleep(1000);
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }


}
