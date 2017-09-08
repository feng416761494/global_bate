package com.usamsl.global.index.step.step7.entity;

/**
 * Created by Administrator on 2017/3/9.
 * 户口本
 */
public class AccountBook {
    private String photoUrl;
    private int id;

    public AccountBook() {
    }

    public AccountBook(String photoUrl, int id) {
        this.photoUrl = photoUrl;
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
