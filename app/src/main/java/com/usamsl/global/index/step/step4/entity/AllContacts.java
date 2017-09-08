package com.usamsl.global.index.step.step4.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/2/4.
 * 全部联系人
 */
public class AllContacts {

    /**
     * result : [{"contact_phone":"13210917673","id_place_issuse":" ","use_status":"1",
     * "remark":" ","contact_id":12,"passport_validity_time":" ","passport_nationality":" ",
     * "passport_place_birth":" ","passport_place_issuse":" ","head_url":" ","e_mail":"bsuwuh@qq.com",
     * "customert_id":10,"contact_name":"sjhsbbsj","address":" ","e_passport_place_birth":" ",
     * "contact_birth":" ","passport_no":" ","e_passport_place_issuse":" ","e_contact_name":" ",
     * "show_order":"1","passport_birth_time":" ","contact_number":" ","contact_sex":" ","nationality":" ",
     * "group_id":1,"id_validity":" ","e_contact_sex":" "}]
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
         * contact_phone : 13210917673
         * id_place_issuse :
         * use_status : 1
         * remark :
         * contact_id : 12
         * passport_validity_time :
         * passport_nationality :
         * passport_place_birth :
         * passport_place_issuse :
         * head_url :
         * e_mail : bsuwuh@qq.com
         * customert_id : 10
         * contact_name : sjhsbbsj
         * address :
         * e_passport_place_birth :
         * contact_birth :
         * passport_no :
         * e_passport_place_issuse :
         * e_contact_name :
         * show_order : 1
         * passport_birth_time :
         * contact_number :
         * contact_sex :
         * nationality :
         * group_id : 1
         * id_validity :
         * e_contact_sex :
         */

        private String contact_phone;
        private String id_place_issuse;
        private String use_status;
        private String remark;
        private int contact_id;
        private String passport_validity_time;
        private String passport_nationality;
        private String passport_place_birth;
        private String passport_place_issuse;
        private String head_url;
        private String e_mail;
        private int customert_id;
        private String contact_name;
        private String address;
        private String e_passport_place_birth;
        private String contact_birth;
        private String passport_no;
        private String e_passport_place_issuse;
        private String e_contact_name;
        private String show_order;
        private String passport_birth_time;
        private String contact_number;
        private String contact_sex;
        private String nationality;
        private int group_id;
        private String id_validity;
        private String e_contact_sex;

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public String getId_place_issuse() {
            return id_place_issuse;
        }

        public void setId_place_issuse(String id_place_issuse) {
            this.id_place_issuse = id_place_issuse;
        }

        public String getUse_status() {
            return use_status;
        }

        public void setUse_status(String use_status) {
            this.use_status = use_status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getContact_id() {
            return contact_id;
        }

        public void setContact_id(int contact_id) {
            this.contact_id = contact_id;
        }

        public String getPassport_validity_time() {
            return passport_validity_time;
        }

        public void setPassport_validity_time(String passport_validity_time) {
            this.passport_validity_time = passport_validity_time;
        }

        public String getPassport_nationality() {
            return passport_nationality;
        }

        public void setPassport_nationality(String passport_nationality) {
            this.passport_nationality = passport_nationality;
        }

        public String getPassport_place_birth() {
            return passport_place_birth;
        }

        public void setPassport_place_birth(String passport_place_birth) {
            this.passport_place_birth = passport_place_birth;
        }

        public String getPassport_place_issuse() {
            return passport_place_issuse;
        }

        public void setPassport_place_issuse(String passport_place_issuse) {
            this.passport_place_issuse = passport_place_issuse;
        }

        public String getHead_url() {
            return head_url;
        }

        public void setHead_url(String head_url) {
            this.head_url = head_url;
        }

        public String getE_mail() {
            return e_mail;
        }

        public void setE_mail(String e_mail) {
            this.e_mail = e_mail;
        }

        public int getCustomert_id() {
            return customert_id;
        }

        public void setCustomert_id(int customert_id) {
            this.customert_id = customert_id;
        }

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getE_passport_place_birth() {
            return e_passport_place_birth;
        }

        public void setE_passport_place_birth(String e_passport_place_birth) {
            this.e_passport_place_birth = e_passport_place_birth;
        }

        public String getContact_birth() {
            return contact_birth;
        }

        public void setContact_birth(String contact_birth) {
            this.contact_birth = contact_birth;
        }

        public String getPassport_no() {
            return passport_no;
        }

        public void setPassport_no(String passport_no) {
            this.passport_no = passport_no;
        }

        public String getE_passport_place_issuse() {
            return e_passport_place_issuse;
        }

        public void setE_passport_place_issuse(String e_passport_place_issuse) {
            this.e_passport_place_issuse = e_passport_place_issuse;
        }

        public String getE_contact_name() {
            return e_contact_name;
        }

        public void setE_contact_name(String e_contact_name) {
            this.e_contact_name = e_contact_name;
        }

        public String getShow_order() {
            return show_order;
        }

        public void setShow_order(String show_order) {
            this.show_order = show_order;
        }

        public String getPassport_birth_time() {
            return passport_birth_time;
        }

        public void setPassport_birth_time(String passport_birth_time) {
            this.passport_birth_time = passport_birth_time;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getContact_sex() {
            return contact_sex;
        }

        public void setContact_sex(String contact_sex) {
            this.contact_sex = contact_sex;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public String getId_validity() {
            return id_validity;
        }

        public void setId_validity(String id_validity) {
            this.id_validity = id_validity;
        }

        public String getE_contact_sex() {
            return e_contact_sex;
        }

        public void setE_contact_sex(String e_contact_sex) {
            this.e_contact_sex = e_contact_sex;
        }
    }
}
