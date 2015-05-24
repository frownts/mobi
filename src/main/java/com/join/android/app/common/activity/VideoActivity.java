package com.join.android.app.common.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.join.android.app.common.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Created by wanjinma on 15/5/24.
 */
@EActivity(R.layout.video_activity)
public class VideoActivity extends Activity{
    @ViewById
    SurfaceView surface;
    private MediaPlayer mediaPlayer;

    @AfterViews
    void afterViews(){

        surface.getHolder().setFixedSize(176, 144);//设置分辨率
        surface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surface.getHolder().addCallback(new SurceCallBack());
        mediaPlayer = new MediaPlayer();


    }

    private void play() {

        mediaPlayer.reset();//重置为初始状态
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音乐流的类型
        mediaPlayer.setDisplay(surface.getHolder());//设置video影片以surfaceviewholder播放
        try {
            mediaPlayer.setDataSource("http://192.168.1.101:8080/test.mp4");//设置路径
            mediaPlayer.prepare();//缓冲
            mediaPlayer.start();//播放
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final class SurceCallBack implements SurfaceHolder.Callback{
        /**
         * 画面修改
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub

        }

        /**
         * 画面创建
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            play();

        }

        /**
         * 画面销毁
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }
    }
}
