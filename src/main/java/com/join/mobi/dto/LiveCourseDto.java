package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午10:08
 */
public class LiveCourseDto {
    /**
     * 最后一次学习时间
     */
    private String lastLearn;

    /**
     * 对应学员学习该课程次数
     */
    private int learningTimes;

    /**
     * 课程标题
     */
    private String title;

    /**
     * 对应学员学习该课程的总时长，单位：秒
     */
    private String totalDuration;

    /**
     * 课程ID
     */
    private long courseId;

    /**
     * 课程的总时长，单位：秒
     */
    private long courseHour;

    /**
     * 课程是否完成 0未完成  1完成
     */
    private int courseComplete;

    /**
     * 海报链接
     */
    private String url;




    public String getLastLearn() {
        return lastLearn;
    }

    public void setLastLearn(String lastLearn) {
        this.lastLearn = lastLearn;
    }

    public int getLearningTimes() {
        return learningTimes;
    }

    public void setLearningTimes(int learningTimes) {
        this.learningTimes = learningTimes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(long courseHour) {
        this.courseHour = courseHour;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCourseComplete() {
        return courseComplete;
    }

    public void setCourseComplete(int courseComplete) {
        this.courseComplete = courseComplete;
    }
}
