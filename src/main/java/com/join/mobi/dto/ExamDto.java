package com.join.mobi.dto;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午10:29
 */
public class ExamDto {
    /**
     * 测试ID
     */
    private long examId;
    /**
     * 测试标题
     */
    private String name;
    /**
     * 测试考题数目
     */
    private int itemCount;
    /**
     * 测试状态，值为0时为未测
     */
    private int status;
    /**
     * 完成率为100时，状态为已完成，其余则显示百分比
     */
    private String finishPercent;

    /**
     * 测试限制时间
     */
    private long durationLimit;

    /*************以下是个人考试详情***************/
    /**
     * 测试创建时间
     */
    private String createTime;

    /**
     * 学员测试时间
     */
    private String examTime;

    /**
     * 正确率
     */
    private String correctPercent;

    /**
     * 学员完成时长
     */
    private long duration;

    private List<ExamItem> examItems;


    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFinishPercent() {
        return finishPercent;
    }

    public void setFinishPercent(String finishPercent) {
        this.finishPercent = finishPercent;
    }

    public long getDurationLimit() {
        return durationLimit;
    }

    public void setDurationLimit(long durationLimit) {
        this.durationLimit = durationLimit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getCorrectPercent() {
        return correctPercent;
    }

    public void setCorrectPercent(String correctPercent) {
        this.correctPercent = correctPercent;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<ExamItem> getExamItems() {
        return examItems;
    }

    public void setExamItems(List<ExamItem> examItems) {
        this.examItems = examItems;
    }
}
