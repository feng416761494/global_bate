package com.usamsl.global.index.step.step6.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */
public class PayOrder {

    /**
     * result : [{"contact_name":"qq","visa_area_id":"上海领区","depart_time":1493308800000,"contact_phone":"13569496330","create_time":1487315911000,"use_status":"1","photo_format":"774fc5348ae0419ca5328c4a0446c46b","contact_id":66,"visa_name":"美国个人旅游签证","preferential_price":8888,"order_code":"NO2017021715183142127296","order_status":"1","update_time":1487315911000,"cust_type":"1","visa_id":1,"accompany_price":200,"order_id":389,"is_pay":"0","cust_id":10,"deal_status":0}]
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
         * contact_name : qq
         * visa_area_id : 上海领区
         * depart_time : 1493308800000
         * contact_phone : 13569496330
         * create_time : 1487315911000
         * use_status : 1
         * photo_format : 774fc5348ae0419ca5328c4a0446c46b
         * contact_id : 66
         * visa_name : 美国个人旅游签证
         * preferential_price : 8888.0
         * order_code : NO2017021715183142127296
         * order_status : 1
         * update_time : 1487315911000
         * cust_type : 1
         * visa_id : 1
         * accompany_price : 200.0
         * order_id : 389
         * is_pay : 0
         * cust_id : 10
         * deal_status : 0
         */

        private String contact_name;
        private String visa_area_id;
        private long depart_time;
        private String contact_phone;
        private long create_time;
        private String use_status;
        private String photo_format;
        private int contact_id;
        private String visa_name;
        private double preferential_price;
        private String order_code;
        private String order_status;
        private long update_time;
        private String cust_type;
        private int visa_id;
        private double accompany_price;
        private int order_id;
        private String is_pay;
        private int cust_id;
        private int deal_status;

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getVisa_area_id() {
            return visa_area_id;
        }

        public void setVisa_area_id(String visa_area_id) {
            this.visa_area_id = visa_area_id;
        }

        public long getDepart_time() {
            return depart_time;
        }

        public void setDepart_time(long depart_time) {
            this.depart_time = depart_time;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getUse_status() {
            return use_status;
        }

        public void setUse_status(String use_status) {
            this.use_status = use_status;
        }

        public String getPhoto_format() {
            return photo_format;
        }

        public void setPhoto_format(String photo_format) {
            this.photo_format = photo_format;
        }

        public int getContact_id() {
            return contact_id;
        }

        public void setContact_id(int contact_id) {
            this.contact_id = contact_id;
        }

        public String getVisa_name() {
            return visa_name;
        }

        public void setVisa_name(String visa_name) {
            this.visa_name = visa_name;
        }

        public double getPreferential_price() {
            return preferential_price;
        }

        public void setPreferential_price(double preferential_price) {
            this.preferential_price = preferential_price;
        }

        public String getOrder_code() {
            return order_code;
        }

        public void setOrder_code(String order_code) {
            this.order_code = order_code;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public String getCust_type() {
            return cust_type;
        }

        public void setCust_type(String cust_type) {
            this.cust_type = cust_type;
        }

        public int getVisa_id() {
            return visa_id;
        }

        public void setVisa_id(int visa_id) {
            this.visa_id = visa_id;
        }

        public double getAccompany_price() {
            return accompany_price;
        }

        public void setAccompany_price(double accompany_price) {
            this.accompany_price = accompany_price;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public int getCust_id() {
            return cust_id;
        }

        public void setCust_id(int cust_id) {
            this.cust_id = cust_id;
        }

        public int getDeal_status() {
            return deal_status;
        }

        public void setDeal_status(int deal_status) {
            this.deal_status = deal_status;
        }
    }
}
