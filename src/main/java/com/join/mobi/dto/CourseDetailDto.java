package com.join.mobi.dto;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午10:26
 */
public class CourseDetailDto {
    /**
     * 课程ID
     */
    private long courseId;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 课程描述
     */
    private String description;
    /**
     * 课程老师
     */
    private String instructor;
    /**
     * 开课机构名称
     */
    private String branch;
    /**
     * 课程创建时间
     */
    private String createTime;
    /**
     * 学习该课程的学员数
     */
    private int totalStudent;
    /**
     * 对应学员最后学习该课程的时间 2013-06-24
     */
    private String lastLearn;
    /**
     * 对象学员学习改课程的总时长
     */
    private long totalHour;
    /**
     * 课程时长
     */
    private long courseHour;

    /**
     * 上将的课程编号
     */
    private long lastLearnChptId;

    /**
     * 课程有效期
     */
    private long validUntil;

    private String bookmark;

    private List<ExamDto> exam;

    private List<ChapterDto> chapter;

    private List<ReferenceDto> reference;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(int totalStudent) {
        this.totalStudent = totalStudent;
    }

    public String getLastLearn() {
        return lastLearn;
    }

    public void setLastLearn(String lastLearn) {
        this.lastLearn = lastLearn;
    }

    public long getTotalHour() {
        return totalHour;
    }

    public void setTotalHour(long totalHour) {
        this.totalHour = totalHour;
    }

    public long getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(long courseHour) {
        this.courseHour = courseHour;
    }

    public List<ExamDto> getExam() {
        return exam;
    }

    public void setExam(List<ExamDto> exam) {
        this.exam = exam;
    }

    public List<ChapterDto> getChapter() {
        return chapter;
    }

    public void setChapter(List<ChapterDto> chapter) {
        this.chapter = chapter;
    }

    public List<ReferenceDto> getReference() {
        return reference;
    }

    public void setReference(List<ReferenceDto> reference) {
        this.reference = reference;
    }

    public long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }

    public long getLastLearnChptId() {
        return lastLearnChptId;
    }

    public void setLastLearnChptId(long lastLearnChptId) {
        this.lastLearnChptId = lastLearnChptId;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }
}

