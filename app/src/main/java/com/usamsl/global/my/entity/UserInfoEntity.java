package com.usamsl.global.my.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1.
 */
public class UserInfoEntity implements Serializable{
    private Result result;

    private String reason;

    private int error_code;

    public void setResult(Result result){
        this.result = result;
    }
    public Result getResult(){
        return this.result;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    public String getReason(){
        return this.reason;
    }
    public void setError_code(int error_code){
        this.error_code = error_code;
    }
    public int getError_code(){
        return this.error_code;
    }

    public static class Result implements Serializable{
        private String cust_tel;

        private String cust_type;

        private String cust_email;

        private String sex;

        private String cust_photo;

        private String cust_name;

        private String path_name;

        public void setCust_tel(String cust_tel){
            this.cust_tel = cust_tel;
        }
        public String getCust_tel(){
            return this.cust_tel;
        }
        public void setCust_type(String cust_type){
            this.cust_type = cust_type;
        }
        public String getCust_type(){
            return this.cust_type;
        }
        public void setCust_email(String cust_email){
            this.cust_email = cust_email;
        }
        public String getCust_email(){
            return this.cust_email;
        }
        public void setSex(String sex){
            this.sex = sex;
        }
        public String getSex(){
            return this.sex;
        }
        public void setCust_photo(String cust_photo){
            this.cust_photo = cust_photo;
        }
        public String getCust_photo(){
            return this.cust_photo;
        }
        public void setCust_name(String cust_name){
            this.cust_name = cust_name;
        }
        public String getCust_name(){
            return this.cust_name;
        }
        public void setPath_name(String path_name){
            this.path_name = path_name;
        }
        public String getPath_name(){
            return this.path_name;
        }
    }

}
