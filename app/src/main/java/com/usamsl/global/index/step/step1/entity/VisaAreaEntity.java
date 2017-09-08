package com.usamsl.global.index.step.step1.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/3.
 */
public class VisaAreaEntity {
    private int visaAreaID;
    private String visaAreaCity;
    private Drawable areaImage;

    public int getVisaAreaID() {
        return visaAreaID;
    }

    public void setVisaAreaID(int visaAreaID) {
        this.visaAreaID = visaAreaID;
    }

    public String getVisaAreaCity() {
        return visaAreaCity;
    }

    public void setVisaAreaCity(String visaAreaCity) {
        this.visaAreaCity = visaAreaCity;
    }

    public Drawable getAreaImage() {
        return areaImage;
    }

    public void setAreaImage(Drawable areaImage) {
        this.areaImage = areaImage;
    }
}
