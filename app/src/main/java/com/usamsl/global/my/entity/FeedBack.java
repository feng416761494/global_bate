package com.usamsl.global.my.entity;

/**
 * Created by Administrator on 2017/8/4.
 */
public class FeedBack {
    private String photoUrl;
    int num;

    public FeedBack() {
    }

    public FeedBack(String photoUrl, int num) {
        this.photoUrl = photoUrl;
        this.num = num;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
