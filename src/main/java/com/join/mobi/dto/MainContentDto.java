package com.join.mobi.dto;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午10:24
 */
public class MainContentDto {
    private List<LiveDto> lives;
    private List<LiveCourseDto> courses;
    private List<ResourceShareDto> resourceShares;
    private List<NoticeDto> notices;

    public List<LiveDto> getLives() {
        return lives;
    }

    public void setLives(List<LiveDto> lives) {
        this.lives = lives;
    }

    public List<LiveCourseDto> getCourses() {
        return courses;
    }

    public void setCourses(List<LiveCourseDto> courses) {
        this.courses = courses;
    }

    public List<ResourceShareDto> getResourceShares() {
        return resourceShares;
    }

    public void setResourceShares(List<ResourceShareDto> resourceShares) {
        this.resourceShares = resourceShares;
    }

    public List<NoticeDto> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticeDto> notices) {
        this.notices = notices;
    }
}
