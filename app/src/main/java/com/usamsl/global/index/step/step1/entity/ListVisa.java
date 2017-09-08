package com.usamsl.global.index.step.step1.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/3.
 */
public class ListVisa implements Serializable{
    private String is_easy;

    private String remark;

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

    private String visa_label;

    private String is_refund;

    private int country_id;

    private String validity_time;

    public void setIs_easy(String is_easy){
        this.is_easy = is_easy;
    }
    public String getIs_easy(){
        return this.is_easy;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return this.remark;
    }
    public void setPlan_weekday(int plan_weekday){
        this.plan_weekday = plan_weekday;
    }
    public int getPlan_weekday(){
        return this.plan_weekday;
    }
    public void setVisa_name(String visa_name){
        this.visa_name = visa_name;
    }
    public String getVisa_name(){
        return this.visa_name;
    }
    public void setPreferential_price(double preferential_price){
        this.preferential_price = preferential_price;
    }
    public double getPreferential_price(){
        return this.preferential_price;
    }
    public void setDeal_type_name(String deal_type_name){
        this.deal_type_name = deal_type_name;
    }
    public String getDeal_type_name(){
        return this.deal_type_name;
    }
    public void setIs_stop_set(String is_stop_set){
        this.is_stop_set = is_stop_set;
    }
    public String getIs_stop_set(){
        return this.is_stop_set;
    }
    public void setVisa_type_name(String visa_type_name){
        this.visa_type_name = visa_type_name;
    }
    public String getVisa_type_name(){
        return this.visa_type_name;
    }
    public void setVisa_area_name(String visa_area_name){
        this.visa_area_name = visa_area_name;
    }
    public String getVisa_area_name(){
        return this.visa_area_name;
    }
    public void setVisa_type_id(int visa_type_id){
        this.visa_type_id = visa_type_id;
    }
    public int getVisa_type_id(){
        return this.visa_type_id;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public double getPrice(){
        return this.price;
    }
    public void setVisa_id(int visa_id){
        this.visa_id = visa_id;
    }
    public int getVisa_id(){
        return this.visa_id;
    }
    public void setCountry_name(String country_name){
        this.country_name = country_name;
    }
    public String getCountry_name(){
        return this.country_name;
    }
    public void setVisa_label(String visa_label){
        this.visa_label = visa_label;
    }
    public String getVisa_label(){
        return this.visa_label;
    }
    public void setIs_refund(String is_refund){
        this.is_refund = is_refund;
    }
    public String getIs_refund(){
        return this.is_refund;
    }
    public void setCountry_id(int country_id){
        this.country_id = country_id;
    }
    public int getCountry_id(){
        return this.country_id;
    }
    public void setValidity_time(String validity_time){
        this.validity_time = validity_time;
    }
    public String getValidity_time(){
        return this.validity_time;
    }
}
