package com.usamsl.global.login.entity;

/**
 * Created by Administrator on 2017/1/11.
 * 描述：登录用户的信息
 */
public class User {

    /**
     * result : {"update_time":1482890475000,"cust_email":"132@qq.com","cust_password":"356a192b7913b04c54574d18c28d46e6395428ab","use_status":"1","cust_name":"15563788080","remark":"备注","id":1,"create_date":1482890472000,"cust_phone":"15563788080","token":"3d10e917f8004bd1f5b382dfe76359da95ae051b"}
     * reason : 用户登录成功
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
         * update_time : 1482890475000
         * cust_email : 132@qq.com
         * cust_password : 356a192b7913b04c54574d18c28d46e6395428ab
         * use_status : 1
         * cust_name : 15563788080
         * remark : 备注
         * id : 1
         * create_date : 1482890472000
         * cust_phone : 15563788080
         * token : 3d10e917f8004bd1f5b382dfe76359da95ae051b
         */

        private long update_time;
        private String cust_email;
        private String cust_password;
        private String use_status;
        private String cust_name;
        private String remark;
        private int id;
        private long create_date;
        private String cust_phone;
        private String token;

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public String getCust_email() {
            return cust_email;
        }

        public void setCust_email(String cust_email) {
            this.cust_email = cust_email;
        }

        public String getCust_password() {
            return cust_password;
        }

        public void setCust_password(String cust_password) {
            this.cust_password = cust_password;
        }

        public String getUse_status() {
            return use_status;
        }

        public void setUse_status(String use_status) {
            this.use_status = use_status;
        }

        public String getCust_name() {
            return cust_name;
        }

        public void setCust_name(String cust_name) {
            this.cust_name = cust_name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreate_date() {
            return create_date;
        }

        public void setCreate_date(long create_date) {
            this.create_date = create_date;
        }

        public String getCust_phone() {
            return cust_phone;
        }

        public void setCust_phone(String cust_phone) {
            this.cust_phone = cust_phone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
