package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午12:21
 */
public class ReferenceDto {

    /**
     * 参考资料ID
     */
    private long referenceId;
    /**
     * 参考资料标题
     */
    private String title;
    /**
     * 参考资料文件大小，字节
     */
    private long fileSize;
    /**
     * 参考资料类型， 1—video,2—doc,3—image,4—flash,5—live,6--unknow
     */
    private int type;
    /**
     * 参考资料链接
     */
    private String url;

    public long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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
