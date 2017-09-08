package com.usamsl.global.my.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 * 全部订单显示
 */
public class AllOrder {

    /**
     * result : [{"contact_number":"11","contact_id":30,
     * "contact_phone":"13210917673","contact_name":"小红",
     * "visa_area_name":"上海领区","visa_name":"美国个人旅游签证",
     * "order_id":103,"order_status":"1","order_code":"NO2017021015130305237501"},
     * {"contact_number":"11","contact_id":30,"contact_phone":"13210917673","contact_name":"小红",
     * "visa_area_name":"上海领区","visa_name":"美国个人旅游签证","order_id":102,"order_status":"1","order_code":"NO2017021015082015275838"},{"contact_number":"1","payment":9999,"pay_type":"1","contact_id":13,"contact_phone":"13210917673","contact_name":"小明","visa_area_name":"上海领区","visa_name":"美国个人旅游签证","order_id":101,"order_status":"9","order_code":"NO2017020918241979958761"}]
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

    public static class ResultBean implements Parcelable {
        /**
         * contact_number : 11
         * contact_id : 30
         * contact_phone : 13210917673
         * contact_name : 小红
         * visa_area_name : 上海领区
         * visa_name : 美国个人旅游签证
         * order_id : 103
         * order_status : 1
         * order_code : NO2017021015130305237501
         * payment : 9999.0
         * pay_type : 1
         */

        private String contact_number;
        private int contact_id;
        private String contact_phone;
        private String contact_name;
        private String visa_area_name;
        private String visa_name;
        private int order_id;
        private int order_status;
        private String order_code;
        private double payment;
        private String pay_type;
        private int cust_type;
        private String photo_format;
        private long depart_time;
        private int is_pay;
        private String form_url;
        private String country_name;
        private String head_url;
        private int country_id;
        private String fixed_line;
        private String address;
        private  int visa_id;

        protected ResultBean(Parcel in) {
            contact_number = in.readString();
            contact_id = in.readInt();
            contact_phone = in.readString();
            contact_name = in.readString();
            visa_area_name = in.readString();
            visa_name = in.readString();
            order_id = in.readInt();
            order_status = in.readInt();
            order_code = in.readString();
            payment = in.readDouble();
            pay_type = in.readString();
            cust_type = in.readInt();
            photo_format = in.readString();
            depart_time = in.readLong();
            is_pay = in.readInt();
            form_url = in.readString();
            country_name = in.readString();
            head_url = in.readString();
            country_id = in.readInt();
            fixed_line = in.readString();
            address = in.readString();
            visa_id = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(contact_number);
            dest.writeInt(contact_id);
            dest.writeString(contact_phone);
            dest.writeString(contact_name);
            dest.writeString(visa_area_name);
            dest.writeString(visa_name);
            dest.writeInt(order_id);
            dest.writeInt(order_status);
            dest.writeString(order_code);
            dest.writeDouble(payment);
            dest.writeString(pay_type);
            dest.writeInt(cust_type);
            dest.writeString(photo_format);
            dest.writeLong(depart_time);
            dest.writeInt(is_pay);
            dest.writeString(form_url);
            dest.writeString(country_name);
            dest.writeString(head_url);
            dest.writeInt(country_id);
            dest.writeString(fixed_line);
            dest.writeString(address);
            dest.writeInt(visa_id);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel in) {
                return new ResultBean(in);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };

        public int getVisa_id() {
            return visa_id;
        }

        public void setVisa_id(int visa_id) {
            this.visa_id = visa_id;
        }

        public String getFixed_line() {
            return fixed_line;
        }

        public void setFixed_line(String fixed_line) {
            this.fixed_line = fixed_line;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCountry_id() {
            return country_id;
        }

        public void setCountry_id(int country_id) {
            this.country_id = country_id;
        }

        public String getHead_url() {
            return head_url;
        }

        public void setHead_url(String head_url) {
            this.head_url = head_url;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public int getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(int is_pay) {
            this.is_pay = is_pay;
        }

        public long getDepart_time() {
            return depart_time;
        }

        public void setDepart_time(long depart_time) {
            this.depart_time = depart_time;
        }

        public String getContact_number() {
            return contact_number;
        }

        public String getForm_url() {
            return form_url;
        }

        public void setForm_url(String form_url) {
            this.form_url = form_url;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public int getContact_id() {
            return contact_id;
        }

        public void setContact_id(int contact_id) {
            this.contact_id = contact_id;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getVisa_area_name() {
            return visa_area_name;
        }

        public void setVisa_area_name(String visa_area_name) {
            this.visa_area_name = visa_area_name;
        }

        public String getVisa_name() {
            return visa_name;
        }

        public void setVisa_name(String visa_name) {
            this.visa_name = visa_name;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public int getOrder_status() {
            return order_status;
        }

        public void setOrder_status(int order_status) {
            this.order_status = order_status;
        }

        public String getOrder_code() {
            return order_code;
        }

        public void setOrder_code(String order_code) {
            this.order_code = order_code;
        }

        public double getPayment() {
            return payment;
        }

        public void setPayment(double payment) {
            this.payment = payment;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public int getCust_type() {
            return cust_type;
        }

        public void setCust_type(int cust_type) {
            this.cust_type = cust_type;
        }

        public String getPhoto_format() {
            return photo_format;
        }

        public void setPhoto_format(String photo_format) {
            this.photo_format = photo_format;
        }

    }
}
