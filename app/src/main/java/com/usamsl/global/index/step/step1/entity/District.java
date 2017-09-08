package com.usamsl.global.index.step.step1.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/23.
 * 领区
 */
public class District {

    /**
     * result : [{"visa_area_id":30,"visa_area_name":"上海领区"}]
     * reason : 获取数据成功
     * error_code : 0
     */

    private String reason;
    private int error_code;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * visa_area_id : 30
         * visa_area_name : 上海领区
         */

        private int visa_area_id;
        private String visa_area_name;

        public int getVisa_area_id() {
            return visa_area_id;
        }

        public void setVisa_area_id(int visa_area_id) {
            this.visa_area_id = visa_area_id;
        }

        public String getVisa_area_name() {
            return visa_area_name;
        }

        public void setVisa_area_name(String visa_area_name) {
            this.visa_area_name = visa_area_name;
        }
    }
}
