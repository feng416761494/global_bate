package com.usamsl.global.index.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.usamsl.global.index.util.CnSpell;

/**
 * Created by Administrator on 2016/12/19.
 * 描述：首页跳转到的签证国家或城市显示
 */
public class IndexVisa implements Comparable<IndexVisa>,Parcelable {
    private String name; // 国家或城市对应的名字
    private String pinyin; // 对应的拼音
    private String firstLetter; // 首字母
    //国家id
    private int country_id;
    //国家规格
    private String SPEC_KEY;
    //头像
    private String ensign_url;
    //背景
    private String bg_img_url;
    //国家对应表的连接
    public  String countrySurface;
    public IndexVisa() {
    }

    public IndexVisa(String name, int country_id) {
        this.name = name;
        this.country_id = country_id;
        pinyin = CnSpell.getPinYin(name); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }


    protected IndexVisa(Parcel in) {
        name = in.readString();
        pinyin = in.readString();
        firstLetter = in.readString();
        country_id = in.readInt();
        SPEC_KEY = in.readString();
        ensign_url = in.readString();
        bg_img_url = in.readString();
    }

    public static final Creator<IndexVisa> CREATOR = new Creator<IndexVisa>() {
        @Override
        public IndexVisa createFromParcel(Parcel in) {
            return new IndexVisa(in);
        }

        @Override
        public IndexVisa[] newArray(int size) {
            return new IndexVisa[size];
        }
    };

    public String getEnsign_url() {
        return ensign_url;
    }

    public void setEnsign_url(String ensign_url) {
        this.ensign_url = ensign_url;
    }

    public String getBg_img_url() {
        return bg_img_url;
    }

    public void setBg_img_url(String bg_img_url) {
        this.bg_img_url = bg_img_url;
    }

    public String getSPEC_KEY() {
        return SPEC_KEY;
    }

    public void setSPEC_KEY(String SPEC_KEY) {
        this.SPEC_KEY = SPEC_KEY;
    }

    public String getName() {
        return name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getCountrySurface() {
        return countrySurface;
    }

    public void setCountrySurface(String countrySurface) {
        this.countrySurface = countrySurface;
    }

    @Override
    public int compareTo(IndexVisa another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")) {
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(pinyin);
        parcel.writeString(firstLetter);
        parcel.writeInt(country_id);
        parcel.writeString(SPEC_KEY);
        parcel.writeString(ensign_url);
        parcel.writeString(bg_img_url);
    }
}
