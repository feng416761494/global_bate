package com.usamsl.global.index.step.step1.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/1/23.
 * 签证详情
 */
public class VisaDetails {

    /**
     * result : [{"is_easy":"1","visa_area_id":30,"plan_weekday":20,"visa_name":"美国商务签证",
     * "preferential_price":1920,"deal_type_name":"加急","is_stop_set":"可停留3个月",
     * "visa_type_name":"商务签证","visa_area_name":"上海领区","visa_type_id":2,"price":0,
     * "visa_id":2,"country_name":"美国","is_refund":"0","country_id":11,
     * "validity_time":"10年有效"}]
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
         * is_easy : 1
         * visa_area_id : 30
         * plan_weekday : 20
         * visa_name : 美国商务签证
         * preferential_price : 1920.0
         * deal_type_name : 加急
         * is_stop_set : 可停留3个月
         * visa_type_name : 商务签证
         * visa_area_name : 上海领区
         * visa_type_id : 2
         * price : 0.0
         * visa_id : 2
         * country_name : 美国
         * is_refund : 0
         * country_id : 11
         * validity_time : 10年有效
         */

        private String is_easy;
        private int visa_area_id;
        private int plan_weekday;
        private String visa_name;
        private double preferential_price;
        private String deal_type_name;
        private String is_stop_set;
        private String visa_type_name;
        private String visa_area_name;
        private int visa_type_id;
        private double price;
        private int visa_id;
        private String country_name;
        private String is_refund;
        private int country_id;
        private String validity_time;

        protected ResultBean(Parcel in) {
            is_easy = in.readString();
            visa_area_id = in.readInt();
            plan_weekday = in.readInt();
            visa_name = in.readString();
            preferential_price = in.readDouble();
            deal_type_name = in.readString();
            is_stop_set = in.readString();
            visa_type_name = in.readString();
            visa_area_name = in.readString();
            visa_type_id = in.readInt();
            price = in.readDouble();
            visa_id = in.readInt();
            country_name = in.readString();
            is_refund = in.readString();
            country_id = in.readInt();
            validity_time = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(is_easy);
            dest.writeInt(visa_area_id);
            dest.writeInt(plan_weekday);
            dest.writeString(visa_name);
            dest.writeDouble(preferential_price);
            dest.writeString(deal_type_name);
            dest.writeString(is_stop_set);
            dest.writeString(visa_type_name);
            dest.writeString(visa_area_name);
            dest.writeInt(visa_type_id);
            dest.writeDouble(price);
            dest.writeInt(visa_id);
            dest.writeString(country_name);
            dest.writeString(is_refund);
            dest.writeInt(country_id);
            dest.writeString(validity_time);
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

        public String getIs_easy() {
            return is_easy;
        }

        public void setIs_easy(String is_easy) {
            this.is_easy = is_easy;
        }

        public int getVisa_area_id() {
            return visa_area_id;
        }

        public void setVisa_area_id(int visa_area_id) {
            this.visa_area_id = visa_area_id;
        }

        public int getPlan_weekday() {
            return plan_weekday;
        }

        public void setPlan_weekday(int plan_weekday) {
            this.plan_weekday = plan_weekday;
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

        public String getDeal_type_name() {
            return deal_type_name;
        }

        public void setDeal_type_name(String deal_type_name) {
            this.deal_type_name = deal_type_name;
        }

        public String getIs_stop_set() {
            return is_stop_set;
        }

        public void setIs_stop_set(String is_stop_set) {
            this.is_stop_set = is_stop_set;
        }

        public String getVisa_type_name() {
            return visa_type_name;
        }

        public void setVisa_type_name(String visa_type_name) {
            this.visa_type_name = visa_type_name;
        }

        public String getVisa_area_name() {
            return visa_area_name;
        }

        public void setVisa_area_name(String visa_area_name) {
            this.visa_area_name = visa_area_name;
        }

        public int getVisa_type_id() {
            return visa_type_id;
        }

        public void setVisa_type_id(int visa_type_id) {
            this.visa_type_id = visa_type_id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getVisa_id() {
            return visa_id;
        }

        public void setVisa_id(int visa_id) {
            this.visa_id = visa_id;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public String getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(String is_refund) {
            this.is_refund = is_refund;
        }

        public int getCountry_id() {
            return country_id;
        }

        public void setCountry_id(int country_id) {
            this.country_id = country_id;
        }

        public String getValidity_time() {
            return validity_time;
        }

        public void setValidity_time(String validity_time) {
            this.validity_time = validity_time;
        }
    }
}
