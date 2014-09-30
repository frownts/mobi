package com.join.mobi.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;
import com.BaseActivity;
import com.join.android.app.common.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-21
 * Time: 下午2:12
 * html & jquery  against video using by webview
 */
@EActivity(R.layout.video_webview_activity_layout)
public class VideoWebViewActivity extends BaseActivity {

    @ViewById
    WebView webView;


    @AfterViews
    void afterViews() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/video/index.html");
    }


}
