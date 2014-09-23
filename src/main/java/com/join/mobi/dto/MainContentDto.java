package com.join.mobi.dto;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午10:24
 */
public class MainContentDto {
    private List<LiveDto> lives;
    private List<LiveCourseDto> course;
    private List<ResourceShareDto> resourceShare;
    private List<NoticeDto> notice;

    public List<LiveDto> getLives() {
        return lives;
    }

    public void setLives(List<LiveDto> lives) {
        this.lives = lives;
    }

    public List<LiveCourseDto> getCourse() {
        return course;
    }

    public void setCourse(List<LiveCourseDto> course) {
        this.course = course;
    }

    public List<ResourceShareDto> getResourceShare() {
        return resourceShare;
    }

    public void setResourceShare(List<ResourceShareDto> resourceShare) {
        this.resourceShare = resourceShare;
    }

    public List<NoticeDto> getNotice() {
        return notice;
    }

    public void setNotice(List<NoticeDto> notice) {
        this.notice = notice;
    }

}
