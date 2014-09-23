/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.join.mobi.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.join.android.app.common.R;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * 全屏
 */
@EActivity(R.layout.videobuffer)
public class MyVideoViewBufferFullScreen extends Activity implements OnInfoListener, OnBufferingUpdateListener {

    /**
     * TODO: Set the path variable to a streaming video URL or a local media file
     * path.
     */
//    private String path = "http://192.168.1.104/apple.mp4";
    @Extra
    String path = "http://192.168.1.104/apple.mp4";
    private Uri uri;
    @ViewById(resName = "buffer")
     VideoView mVideoView;
    @ViewById(resName = "probar")
     ProgressBar pb;
    @ViewById(resName = "download_rate")
     TextView downloadRateView;
    @ViewById(resName = "load_rate")
     TextView loadRateView;

    @ViewById(resName = "progressBar")
    ProgressBar progressBar;

    @Extra
     long seekTo;

    @AfterViews
    void afterViews() {

        if (!LibsChecker.checkVitamioLibs(this))
            return;

        if (path == "") {
            // Tell the user to provide a media file URL/path.
            Toast.makeText(
                    MyVideoViewBufferFullScreen.this,
                    "Please edit VideoBuffer Activity, and set path"
                            + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
            return;
        } else {
      /*
       * Alternatively,for streaming media you can use
       * mVideoView.setVideoURI(Uri.parse(URLstring));
       */
            uri = Uri.parse(path);

//      mVideoView.setVideoURI(uri);

            mVideoView.setVideoPath(path);
            MediaController mediaController = new MediaController(this);
            mVideoView.setMediaController(mediaController);
            mediaController.setMediaPlayerControlFullScreen(new MediaController.MediaPlayerControlFullScreen() {
                @Override
                public void onFullScreen() {
//                    LiveCourseDetailActivity_.intent(MyVideoViewBufferFullScreen.this).seekTo(mVideoView.getCurrentPosition()).start();
                    Intent intent = new Intent("org.androidannotations.seekTo");
                    sendBroadcast(intent);
                    mVideoView.stopPlayback();
                    System.gc();
//                    Intent data = new Intent(MyVideoViewBufferFullScreen.this,LiveCourseDetailActivity_.class);
//                    data.putExtra("seekTo",mVideoView.getCurrentPosition());
//                    setResult(1002, data);
//                    startActivity(data);
                    finish();
                }
            });
            mVideoView.requestFocus();
            mVideoView.setOnInfoListener(this);
            mVideoView.setOnBufferingUpdateListener(this);
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // optional need Vitamio 4.0
                    mediaPlayer.setPlaybackSpeed(1.0f);
                    mVideoView.seekTo(seekTo);
                }
            });
        }
        startUpdateLearningTime();
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    pb.setVisibility(View.VISIBLE);
                    downloadRateView.setText("");
                    loadRateView.setText("");
                    downloadRateView.setVisibility(View.VISIBLE);
                    loadRateView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mVideoView.start();
                progressBar.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                downloadRateView.setVisibility(View.GONE);
                loadRateView.setVisibility(View.GONE);

                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                downloadRateView.setText("" + extra + "kb/s" + "  ");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        loadRateView.setText(percent + "%");
    }

    @Override
    protected void onDestroy() {
        mVideoView.stopPlayback();
        super.onDestroy();
    }

    private void startUpdateLearningTime(){
        //开始播放了,更新学习时间
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {

                    while(mVideoView!=null){
                        while(mVideoView!=null&&mVideoView.isPlaying()){
                            //更新学习时间
                            Intent intent = new Intent("org.androidannotations.updateLearningTime");
                            MyVideoViewBufferFullScreen.this.sendBroadcast(intent);
                            Thread.sleep(1000);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
