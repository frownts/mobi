package com.join.mobi.dto;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午12:17
 */
public class ChapterDto {


    /**
     * 书签
     */
    private String bookMark;

    private List<ChapterDetailDto> chapter;

    /**
     * 章节时长
     */
    private long chapterDuration;
    /**
     * 章节ID
     */
    private long chapterId;

    /**
     * 章节资源下载链接
     */
    private String downloadUrl;
    /**
     * 章节关联资源的文件大小
     */
    private long fileSize;
    /**
     * 章节学习时间
     */
    private long learnedTime;

    /**
     * 章节资源播放链接
     */
    private String playUrl;

    private long resId;
    /**
     * 章节标题
     */
    private String title;
//
//    /**
//     * 有效期
//     */
//    private String validUntil;
//
    /**
     * 开始播放学习的时间
     */
    private String startTime;

    private boolean playing;




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

    public String getBookMark() {
        return bookMark;
    }

    public void setBookMark(String bookMark) {
        this.bookMark = bookMark;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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

//    public String getValidUntil() {
//        return validUntil;
//    }
//
//    public void setValidUntil(String validUntil) {
//        this.validUntil = validUntil;
//    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public long getResId() {
        return resId;
    }

    public void setResId(long resId) {
        this.resId = resId;
    }

    public List<ChapterDetailDto> getChapter() {
        return chapter;
    }

    public void setChapter(List<ChapterDetailDto> chapter) {
        this.chapter = chapter;
    }
}
