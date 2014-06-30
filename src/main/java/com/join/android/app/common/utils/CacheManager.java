package com.join.android.app.common.utils;

import android.content.Context;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-25
 * Time: 下午5:03
 */
public class CacheManager {
    public static void clearAll(Context context) {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
    }


    public static void clear(String url) {
        MemoryCacheUtil.removeFromCache(url, ImageLoader.getInstance().getMemoryCache());
        DiscCacheUtil.removeFromCache(url, ImageLoader.getInstance().getDiscCache());


    }
}
