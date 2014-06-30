package com.join.mobi.activity;

import android.webkit.WebView;
import android.widget.Toast;
import com.BaseActivity;
import com.join.android.app.common.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-6-30
 * Time: 下午10:16
 */
@EActivity(R.layout.portal_activity_layout)
public class PortalActivity extends BaseActivity {

    long waitTime = 2000;
    long touchTime = 0;

    @ViewById
    WebView webview;

    @AfterViews
    void afterViews() {
        webview.loadUrl("http://mobi.365ulife.com/Default.aspx");
    }

    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if((currentTime-touchTime)>=waitTime) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        }else {
            finish();
        }
    }

}
