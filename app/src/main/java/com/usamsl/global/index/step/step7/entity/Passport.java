package com.usamsl.global.index.step.step7.entity;

/**
 * Created by Administrator on 2017/1/16.
 * 护照
 */
public class Passport {
    /**
     * name : 姓名
     * nameCh : 中文姓名
     * cardno : 护照号
     * sex : 性别
     * sexCH : 中文性别
     * birthday : 出生日期
     * address : 地址
     * addressCH : 中文地址
     * issueAuthority : 签发地址
     * issueAddressCH : 签发地址中文
     * issueDate : 发证日期
     * validPeriod : 有效期
     * nation : 国籍
     * personalNo :
     */

    private String name;
    private String nameCh;
    private String cardno;
    private String sex;
    private String sexCH;
    private String birthday;
    private String address;
    private String addressCH;
    private String issueAuthority;
    private String issueAddressCH;
    private String issueDate;
    private String validPeriod;
    private String nation;
    private String personalNo;

    //照片路径
    private String photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Passport() {
    }

    public Passport(String name, String nameCh,
                    String cardno, String sex,
                    String sexCH, String birthday,
                    String address, String addressCH,
                    String issueAuthority, String issueAddressCH,
                    String issueDate, String validPeriod,
                    String nation, String personalNo) {
        this.name = name;
        this.nameCh = nameCh;
        this.cardno = cardno;
        this.sex = sex;
        this.sexCH = sexCH;
        this.birthday = birthday;
        this.address = address;
        this.addressCH = addressCH;
        this.issueAuthority = issueAuthority;
        this.issueAddressCH = issueAddressCH;
        this.issueDate = issueDate;
        this.validPeriod = validPeriod;
        this.nation = nation;
        this.personalNo = personalNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexCH() {
        return sexCH;
    }

    public void setSexCH(String sexCH) {
        this.sexCH = sexCH;
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

    public String getAddressCH() {
        return addressCH;
    }

    public void setAddressCH(String addressCH) {
        this.addressCH = addressCH;
    }

    public String getIssueAuthority() {
        return issueAuthority;
    }

    public void setIssueAuthority(String issueAuthority) {
        this.issueAuthority = issueAuthority;
    }

    public String getIssueAddressCH() {
        return issueAddressCH;
    }

    public void setIssueAddressCH(String issueAddressCH) {
        this.issueAddressCH = issueAddressCH;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(String validPeriod) {
        this.validPeriod = validPeriod;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPersonalNo() {
        return personalNo;
    }

    public void setPersonalNo(String personalNo) {
        this.personalNo = personalNo;
    }
}
