package com.join.mobi.dto;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午12:54
 */
public class ExamItem {
    private long itemId;
    private String title;
    private int type;
    private String createTime;

    private List<ItemOption> itemOptions;

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
}


