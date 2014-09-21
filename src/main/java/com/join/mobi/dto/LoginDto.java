package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午8:26
 */
public class LoginDto {
    private String logined;
    private String userGender;
    private String userName;
    private String organName;

    public String getLogined() {
        return logined;
    }

    public void setLogined(String logined) {
        this.logined = logined;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }
}
