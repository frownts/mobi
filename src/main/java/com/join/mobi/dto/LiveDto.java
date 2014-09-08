package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午10:15
 * 直播
 */
public class LiveDto {
    /**
     * 直播ID
     */
    private long liveId;

    /**
     * 直播标题
     */
    private String title;

    /**
     * 创建时间 2014-04-18
     */
    private String createTime;

    /**
     * 直播主讲人
     */
    private String author;

    /**
     * 直播路径
     */
    private String url;

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
