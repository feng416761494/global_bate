package com.usamsl.global.login.entity;

/**
 * Created by Administrator on 2017/1/11.
 * 登录、注册、找回密码等返回的信息
 */
public class Login {

    /**
     * reason : 该手机号已注册
     * error_code : 0
     */

    private String reason;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }




}
