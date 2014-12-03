package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14/12/3
 * Time: 22:40
 */
public class VersionDto {
    private String appUrl;
    private String versionNoAndroid;
    private String androidUrl;
    private float versionNo;

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getVersionNoAndroid() {
        return versionNoAndroid;
    }

    public void setVersionNoAndroid(String versionNoAndroid) {
        this.versionNoAndroid = versionNoAndroid;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public float getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(float versionNo) {
        this.versionNo = versionNo;
    }
}
