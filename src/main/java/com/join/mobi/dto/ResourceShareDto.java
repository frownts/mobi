package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 上午10:18
 */
public class ResourceShareDto {


    /**
     * 共享资源文件大小
     */
    private long fileSize;

    /**
     * 共享资源ID
     */
    private long rsId;

    /**
     * 上传资料-保险001,(共享资源标题)
     */
    private String name;

    /**
     * 共享资源ID (共享资源类型：1—视频，2—音乐，3—PDF，4—照片)
     */
    private int type;

    /**
     * 链接地址
     */
    private String url;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getRsId() {
        return rsId;
    }

    public void setRsId(long rsId) {
        this.rsId = rsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
