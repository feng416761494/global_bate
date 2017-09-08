package com.usamsl.global.service.entity;

/**
 * Created by Administrator on 2016/12/15.
 * 服务菜单中签证咨询模块的显示
 */
public class VisaConsultationCountry {
    //国家签证图标
    private int img;
    //国家名称
    private  String visaCountry;

    public VisaConsultationCountry() {
    }

    public VisaConsultationCountry(int img, String visaCountry) {
        this.img = img;
        this.visaCountry = visaCountry;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getVisaCountry() {
        return visaCountry;
    }

    public void setVisaCountry(String visaCountry) {
        this.visaCountry = visaCountry;
    }
}
