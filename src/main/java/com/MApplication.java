package com;

import com.join.android.app.common.db.manager.DBManager;
import com.join.android.app.common.utils.ImageOptionFactory;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.php25.PDownload.DownloadApplication;
import org.androidannotations.annotations.EApplication;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-11
 * Time: 下午4:58
 */
@EApplication
public class MApplication extends DownloadApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initCache();

    }

    private void initCache() {
        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3)//default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .writeDebugLogs()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(ImageOptionFactory.getDefaultOptions())
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBManager.getInstance(this).onDestroy();
    }
}
