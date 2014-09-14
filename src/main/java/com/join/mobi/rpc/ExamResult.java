package com.join.mobi.rpc;

import java.io.Serializable;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-13
 * Time: 上午11:27
 */
public class ExamResult implements Serializable {
    String correctPercent;
    String finishPersenct;
    String startTime;
    String duration;
    String correctNum;
    String incorrectNum;


    public String getCorrectPercent() {
        return correctPercent;
    }

    public void setCorrectPercent(String correctPercent) {
        this.correctPercent = correctPercent;
    }

    public String getFinishPersenct() {
        return finishPersenct;
    }

    public void setFinishPersenct(String finishPersenct) {
        this.finishPersenct = finishPersenct;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(String correctNum) {
        this.correctNum = correctNum;
    }

    public String getIncorrectNum() {
        return incorrectNum;
    }

    public void setIncorrectNum(String incorrectNum) {
        this.incorrectNum = incorrectNum;
    }
}
