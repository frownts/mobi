package com.join.mobi.utils;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-20
 * Time: 下午2:38
 */
public class ContentTypeUtils {

    public static String convertReferenceType(int _type) {
        String type = "未知";
        switch (_type) {
            case 1:
                type = "视频";
                break;
            case 2:
                type = "文档";
                break;
            case 3:
                type = "图片";
                break;
            case 4:
                type = "flash";
                break;
            case 5:
                type = "直播";
                break;
            case 6:
                type = "未知";
                break;
        }
        return type;
    }
}
