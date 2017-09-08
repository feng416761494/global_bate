package com.usamsl.global.main.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 * 版本
 */
public class Version {

    /**
     * result : [{"client_version":"1.0","client_name":"安卓","client_id":1},{"client_version":"1.0","client_name":"苹果","client_id":2}]
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
         * client_version : 1.0
         * client_name : 安卓
         * client_id : 1
         * 版本链接
         * app_http
         */

        private String client_version;
        private String client_name;
        private int client_id;
        private String app_http;

        public String getClient_version() {
            return client_version;
        }

        public void setClient_version(String client_version) {
            this.client_version = client_version;
        }

        public String getClient_name() {
            return client_name;
        }

        public void setClient_name(String client_name) {
            this.client_name = client_name;
        }

        public int getClient_id() {
            return client_id;
        }

        public void setClient_id(int client_id) {
            this.client_id = client_id;
        }

        public String getApp_http() {
            return app_http;
        }

        public void setApp_http(String app_http) {
            this.app_http = app_http;
        }
    }
}
