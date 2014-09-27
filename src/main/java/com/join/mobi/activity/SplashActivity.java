package com.join.mobi.activity;

import android.util.Log;
import android.widget.ImageView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.CacheManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import io.vov.vitamio.LibsChecker;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import java.io.File;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-26
 * Time: 上午11:32
 */

@EActivity(R.layout.splash_activity_layout)
public class SplashActivity extends BaseActivity {

    String adUrl = "http://mobi.365ulife.com/Images/goods/wxtg70011.jpg";

    @ViewById
    ImageView img;

    @AfterViews
    void afterViews() {

//        loadAd();
        start();
        if (!LibsChecker.checkVitamioLibs(this))
            return;

    }

    @UiThread(delay = 1000)
    void loadAd() {


        AsyncHttpClient client = new AsyncHttpClient();

        client.get(adUrl, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                ImageLoader.getInstance().displayImage(adUrl, img);
            }

            @Override
            public void onSuccess(int i, Header[] headers, File file) {
                for (Header header : headers)
                    Log.d("SplashActivity", header.getName() + ";" + header.getValue());
                long b = file.length();
                long a = ImageLoader.getInstance().getDiscCache().get(adUrl).length();
                if (a != b) {
                    CacheManager.clear(adUrl);
                }
                ImageLoader.getInstance().displayImage(adUrl, img);

            }
        });


    }

    @UiThread(delay = 3000)
    void start() {
        PortalActivity_.intent(this).start();
        finish();
    }

}
