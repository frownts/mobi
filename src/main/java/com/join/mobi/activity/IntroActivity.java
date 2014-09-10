package com.join.mobi.activity;

import android.webkit.WebView;
import com.BaseActivity;
import com.join.android.app.common.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-11
 * Time: 上午12:32
 */
@EActivity(R.layout.intro_activity_layout)
public class IntroActivity extends BaseActivity {
    @ViewById
    WebView webView;

    @AfterViews
    void afterViews() {
        webView.loadUrl("file:///android_asset/intro.html");
    }
}
