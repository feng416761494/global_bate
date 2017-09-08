package com.usamsl.global.index.step.step7.entity;

/**
 * Created by Administrator on 2017/1/13.
 * <p/>
 * 身份证信息
 */
public class IdCard {
    //姓名
    private String name;
    //性别
    private String sex;
    //民族
    private String folk;
    //出生年月
    private String birthday;
    //住址
    private String address;
    //身份证号码
    private String cardno;
    //签发机关
    private String issue_authority;
    //有效期限
    private String valid_period;
    //正面链接
    private String positive;
    //反面链接
    private String back;

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFolk() {
        return folk;
    }

    public void setFolk(String folk) {
        this.folk = folk;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getIssue_authority() {
        return issue_authority;
    }

    public void setIssue_authority(String issue_authority) {
        this.issue_authority = issue_authority;
    }

    public String getValid_period() {
        return valid_period;
    }

    public void setValid_period(String valid_period) {
        this.valid_period = valid_period;
    }
}
