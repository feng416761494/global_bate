package com.usamsl.global.order.entity;

/**
 * Created by Administrator on 2017/6/7.
 * 预约面试时间
 */
public class BespeakTime {

    /**
     * result : {"bespeak_time":"2017-06-29","bespeak_time2":"2017-06-28","bespeak_time1":"2017-06-21"}
     * reason : 获取数据成功
     * error_code : 0
     */

    private ResultBean result;
    private String reason;
    private int error_code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

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

    public static class ResultBean {
        /**
         * bespeak_time : 2017-06-29
         * bespeak_time2 : 2017-06-28
         * bespeak_time1 : 2017-06-21
         */

        private String bespeak_time;
        private String bespeak_time2;
        private String bespeak_time1;

        public String getBespeak_time() {
            return bespeak_time;
        }

        public void setBespeak_time(String bespeak_time) {
            this.bespeak_time = bespeak_time;
        }

        public String getBespeak_time2() {
            return bespeak_time2;
        }

        public void setBespeak_time2(String bespeak_time2) {
            this.bespeak_time2 = bespeak_time2;
        }

        public String getBespeak_time1() {
            return bespeak_time1;
        }

        public void setBespeak_time1(String bespeak_time1) {
            this.bespeak_time1 = bespeak_time1;
        }
    }
}
