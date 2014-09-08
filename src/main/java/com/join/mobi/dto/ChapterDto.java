package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午12:17
 */
public class ChapterDto {
    /**
     * 章节ID
     */
    private long chapterId;

    /**
     * 章节标题
     */
    private String title;

    /**
     * 章节时长
     */
    private long chapterDuration;

    /**
     * 章节学习时间
     */
    private long learnedTime;

    /**
     * 书签
     */
    private String bookmark;

    /**
     * 章节关联资源的文件大小
     */
    private long filesize;

    /**
     * 章节资源播放链接
     */
    private String playUrl;

    /**
     * 章节资源下载链接
     */
    private String downloadUrl;


    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getChapterDuration() {
        return chapterDuration;
    }

    public void setChapterDuration(long chapterDuration) {
        this.chapterDuration = chapterDuration;
    }

    public long getLearnedTime() {
        return learnedTime;
    }

    public void setLearnedTime(long learnedTime) {
        this.learnedTime = learnedTime;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
