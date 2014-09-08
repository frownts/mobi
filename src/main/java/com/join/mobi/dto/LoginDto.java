package com.join.mobi.dto;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午8:26
 */
public class LoginDto {
    private String loginResult;
    private String message;

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
