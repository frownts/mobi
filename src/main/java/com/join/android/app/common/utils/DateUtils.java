package com.join.android.app.common.utils;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-17
 * Time: 上午11:15
 */
public class DateUtils {

    /**
     * 下载日期是几分钟前、几天前
     * @return
     */
    public static String FormatForDownloadTime(long begin){
        long now = System.currentTimeMillis();
        long gap = (now-begin)/1000;
        if(gap<60)return "刚刚";
        else if(gap>60&&gap<60*60){
            return gap/60+"分钟前";
        }else if(gap>60&&gap<60*60*60){
            return gap/60/60+"小时前";
        }

        return gap/60/60/24+"天前";
    }
}
