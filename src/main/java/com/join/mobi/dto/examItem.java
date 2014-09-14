package com.join.mobi.dto;

import java.io.Serializable;
import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午12:54
 */
public class ExamItem implements Serializable{
    private long itemId;
    private String title;
    private int type;
    private String createTime;


    private List<ItemOption> itemOptions;

    private boolean isCorrect;
    /**
     * 用户选择的答案
     */
    private List<String> userSelected;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ItemOption> getItemOptions() {
        return itemOptions;
    }

    public void setItemOptions(List<ItemOption> itemOptions) {
        this.itemOptions = itemOptions;
    }

    public List<String> getUserSelected() {
        return userSelected;
    }

    public void setUserSelected(List<String> userSelected) {
        this.userSelected = userSelected;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}


