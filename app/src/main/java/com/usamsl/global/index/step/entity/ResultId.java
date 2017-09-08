package com.usamsl.global.index.step.entity;

/**
 * Created by Administrator on 2017/2/8.
 * 返回的结果加id
 */
public class ResultId {

    /**
     * reason : 添加联系人成功
     * error_code : 0
     * reason_id : 42
     */

    private String reason;
    private int error_code;
    private int reason_id;

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

    public int getReason_id() {
        return reason_id;
    }

    public void setReason_id(int reason_id) {
        this.reason_id = reason_id;
    }
}
