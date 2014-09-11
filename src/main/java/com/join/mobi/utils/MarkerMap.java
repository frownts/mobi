package com.join.mobi.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-11
 * Time: 下午4:35
 */
public class MarkerMap {
    static Map<String, String> markers = new HashMap<String, String>(0);

    public static boolean isMarked(String examId) {
        return markers.containsKey(examId);
    }

    public static void mark(String examId) {
        markers.put(examId, examId);
    }

    public static void unMark(String examId) {
        markers.remove(markers.get(examId));
    }

    public static void clear() {
        markers.clear();
    }
}
