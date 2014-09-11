package com.join.mobi.dto;

import java.io.Serializable;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午12:55
 */
public class ItemOption implements Serializable {
    private long optionId;
    private String optionCode;
    private String title;

    private boolean selected;

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
