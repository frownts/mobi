package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午10:21
 */
public class NoticeDto {
    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 标题
     */
    private String title;

    /**
     * ID
     */
    private long noticeId;

    /**
     * 通告有效截止日期
     */
    private long validUntil;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }
}
