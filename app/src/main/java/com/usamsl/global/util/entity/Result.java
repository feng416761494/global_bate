package com.usamsl.global.util.entity;

/**
 * Created by Administrator on 2017/1/19.
 *返回信息和编码
 */
public class Result {

    /**
     * reason : 添加分组成功
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
