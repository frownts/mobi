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

package com.join.android.app.common.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.join.android.app.common.R;
//import io.vov.vitamio.LibsChecker;
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
//import io.vov.vitamio.MediaPlayer.OnInfoListener;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
import org.androidannotations.annotations.*;

//@EActivity(R.layout.videobuffer)
//public class VideoViewBuffer extends Activity implements OnInfoListener, OnBufferingUpdateListener {
//
//
//    /**
//     * TODO: Set the path variable to a streaming video URL or a local media file
//     * path.
//     */
//    private String path = "http://aia.fortune-net.cn/video/2014-5-20/1326994717.mp4";
//    private Uri uri;
//    private VideoView mVideoView;
//    private ProgressBar pb;
//    private TextView downloadRateView, loadRateView;
//    @ViewById
//    View frameContainer;
//    @ViewById
//    ImageView mediacontroller_screen;
//    @ViewById
//    LinearLayout header;
//
//    @AfterViews
//    public void afterViews() {
//
//        if (!LibsChecker.checkVitamioLibs(this))
//            return;
//
//        mVideoView = (VideoView) findViewById(R.id.buffer);
//        pb = (ProgressBar) findViewById(R.id.probar);
//
//        downloadRateView = (TextView) findViewById(R.id.download_rate);
//        loadRateView = (TextView) findViewById(R.id.load_rate);
//        if (path == "") {
//            // Tell the user to provide a media file URL/path.
//            Toast.makeText(
//                    VideoViewBuffer.this,
//                    "Please edit VideoBuffer Activity, and set path"
//                            + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
//            return;
//        } else {
//      /*
//       * Alternatively,for streaming media you can use
//       * mVideoView.setVideoURI(Uri.parse(URLstring));
//       */
//            uri = Uri.parse(path);
//            mVideoView.setVideoURI(uri);
//            mVideoView.setMediaController(new MediaController(this));
//            mVideoView.requestFocus();
//            mVideoView.setOnInfoListener(this);
//            mVideoView.setOnBufferingUpdateListener(this);
//            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    // optional need Vitamio 4.0
//                    mediaPlayer.setPlaybackSpeed(1.0f);
//                }
//            });
//        }
//
//    }
//
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
//
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
//        // 检测屏幕的方向：纵向或横向
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            // 当前为横屏， 在此处添加额外的处理代码
////            btm.setVisibility(View.GONE);
//            header.setVisibility(View.GONE);
//            frameContainer.setVisibility(View.GONE);
//
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int mSurfaceViewWidth = dm.widthPixels;
//            int mSurfaceViewHeight = dm.heightPixels;
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.MATCH_PARENT);
//            lp.width = mSurfaceViewWidth;
//            lp.height = mSurfaceViewHeight;
//            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FULL_SCREEN,0);
//
////            surface.setLayoutParams(lp);
////            surHolder.setFixedSize(mSurfaceViewWidth,
////                    mSurfaceViewHeight);
////
////            videoContainer.setLayoutParams(lp);
//
//
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // 当前为竖屏， 在此处添加额外的处理代码
//            header.setVisibility(View.VISIBLE);
//
////            DisplayMetrics dm = new DisplayMetrics();
////            getWindowManager().getDefaultDisplay().getMetrics(dm);
////            int mSurfaceViewWidth = dm.widthPixels;
////            int mSurfaceViewHeight = dm.heightPixels;
////            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
////                    RelativeLayout.LayoutParams.MATCH_PARENT,
////                    RelativeLayout.LayoutParams.FILL_PARENT);
////
////            lp.width = mSurfaceViewWidth;
////            lp.height = mSurfaceViewHeight * 1 / 3;
//////            lp.addRule(RelativeLayout.BELOW, R.id.header);
//////            videoContainer.setLayoutParams(lp);
////            frameContainer.setVisibility(View.VISIBLE);
//
//        }
//        super.onConfigurationChanged(newConfig);
//    }
//
//    @Receiver(actions = MediaController.BROADCAST_ACTION_FULLSCREEN)
//    void mediacontroller_screen(){
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//    }
//
//}
