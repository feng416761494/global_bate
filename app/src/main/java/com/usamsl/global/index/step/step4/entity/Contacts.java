package com.usamsl.global.index.step.step4.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/26.
 * 描述：已有联系人
 */
public class Contacts implements Parcelable{
    //名字
    private String name;
    //联系电话
    private String tel;
    //出行时间
    private String goTime;
    //常用邮箱
    private String email;
    //是否选择
    private boolean checked = false;

    public Contacts() {
    }

    public Contacts(String name, String tel, String email, boolean checked) {
        this.name = name;
        this.tel = tel;
        this.checked = checked;
        this.email = email;
    }

    protected Contacts(Parcel in) {
        name = in.readString();
        tel = in.readString();
        goTime = in.readString();
        email = in.readString();
        checked = in.readByte() != 0;
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGoTime() {
        return goTime;
    }

    public void setGoTime(String goTime) {
        this.goTime = goTime;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(tel);
        parcel.writeString(goTime);
        parcel.writeString(email);
        parcel.writeByte((byte) (checked ? 1 : 0));
    }
}
