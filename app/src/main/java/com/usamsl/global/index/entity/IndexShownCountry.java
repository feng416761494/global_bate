package com.usamsl.global.index.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by MSL Vacation Inc on 2016/12/14.
 * 描述：主界面下方显示国家中文、英文名称以及图片时用
 */
public class IndexShownCountry implements Parcelable{

    /**
     * result : [{"ensign_url":"http://139.196.88.32/oss/upload/20170210/be1f607e968d4d02b2b834b51331b138.png","logo_url":"http://139.196.88.32/oss/upload/20170210/14ec47f96b0d48ef9cf06b68f07ed9f5.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/005019f7db1147beaf354160362838c2.jpg","logo_url_plus":"http://139.196.88.32/oss/upload/20170210/b714b33135c34eb688019cf2dfc664f0.png","country_name":"美国","country_en_name":"USA","photo_format":"774fc5348ae0419ca5328c4a0446c46b","country_id":11},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/2d9b5c82756540c7811cdbb60f06ca3f.png","logo_url":"http://139.196.88.32/oss/upload/20170210/44631165f5d2499a8b2bd8695f15fe60.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/e9af36e3fd21436aaff18833a3b845ed.jpg","logo_url_plus":"http://139.196.88.32/oss/upload/20170210/133da90b934e4eabbc62cbe110b9520d.png","country_name":"加拿大","country_en_name":"Canada","photo_format":"be6b28f22a194c5ab38f5c28816bbe08","country_id":12},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/6aab46da622f4d99b85a075bf1a7629d.png","logo_url":"http://139.196.88.32/oss/upload/20170210/3e7b3f380e2b4ac4ac45dda0286a0741.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/b0bd1142d9af40e7a30f984ebc5f7ae1.jpg","logo_url_plus":"http://139.196.88.32/oss/upload/20170210/590d6c0501194090889a61b814f4f2ac.png","country_name":"日本","country_en_name":"Japan","photo_format":"e972c319e4c24deaa297aa992f327a81","country_id":10},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/199363b85400428cb3964c662848bc93.png","logo_url":"http://139.196.88.32/oss/upload/20170210/07ff96de07e2423986c1f1cd58ba61d3.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/0c9146a81dcf4c15a161e5c8b007978e.jpg","logo_url_plus":"http://139.196.88.32/oss/upload/20170210/875d9dc854e2474fbdb4e100f9e82252.png","country_name":"澳大利亚","country_en_name":"Australia","photo_format":"6c39c16787514071a4e87fd1c046dda6","country_id":32},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/e950a94a01f949fea99f833fbf3b869f.png","logo_url":"http://139.196.88.32/oss/upload/20170210/0058f2a0c1804a9ea4f3f346529cae3b.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/71a79a04466d4c5e8040b2e46b0897e3.jpg","logo_url_plus":"http://139.196.88.32/oss/upload/20170210/822539dc3c4a4d9cb955d1fa7e27edf1.png","country_name":"法国","country_en_name":"French","photo_format":"449146bd88eb4aa18c7ae42623610b62","country_id":31},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/f49c6a30b1624a8eb4f397c065b1d2de.png","logo_url":"http://139.196.88.32/oss/upload/20170210/e570a7b906f24f1aa270b60567c3d979.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/a048ff22a4114cb79494ca3bcd396092.jpg","logo_url_plus":"http://139.196.88.32/oss/upload/20170210/367267b9f1ef4a23ac6d6d576373752d.png","country_name":"意大利","country_en_name":"Italy","photo_format":"1cc70b94efb1439a859fe1a5f581aa32","country_id":38}]
     * reason : 获取数据成功
     * error_code : 0
     */

    private String reason;
    private int error_code;
    private List<ResultBean> result;


    protected IndexShownCountry(Parcel in) {
        reason = in.readString();
        error_code = in.readInt();
    }

    public static final Creator<IndexShownCountry> CREATOR = new Creator<IndexShownCountry>() {
        @Override
        public IndexShownCountry createFromParcel(Parcel in) {
            return new IndexShownCountry(in);
        }

        @Override
        public IndexShownCountry[] newArray(int size) {
            return new IndexShownCountry[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reason);
        parcel.writeInt(error_code);
    }

    public static class ResultBean {
        /**
         * ensign_url : http://139.196.88.32/oss/upload/20170210/be1f607e968d4d02b2b834b51331b138.png
         * logo_url : http://139.196.88.32/oss/upload/20170210/14ec47f96b0d48ef9cf06b68f07ed9f5.png
         * bg_img_url : http://139.196.88.32/oss/upload/20170210/005019f7db1147beaf354160362838c2.jpg
         * logo_url_plus : http://139.196.88.32/oss/upload/20170210/b714b33135c34eb688019cf2dfc664f0.png
         * country_name : 美国
         * country_en_name : USA
         * photo_format : 774fc5348ae0419ca5328c4a0446c46b
         * country_id : 11
         */

        private String ensign_url;
        private String logo_url;
        private String bg_img_url;
        private String logo_url_plus;
        private String country_name;
        private String country_en_name;
        private String photo_format;
        private int country_id;
        public String form_url;

        public String getForm_url() {
            return form_url;
        }

        public void setForm_url(String form_url) {
            this.form_url = form_url;
        }

        public String getEnsign_url() {
            return ensign_url;
        }

        public void setEnsign_url(String ensign_url) {
            this.ensign_url = ensign_url;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getBg_img_url() {
            return bg_img_url;
        }

        public void setBg_img_url(String bg_img_url) {
            this.bg_img_url = bg_img_url;
        }

        public String getLogo_url_plus() {
            return logo_url_plus;
        }

        public void setLogo_url_plus(String logo_url_plus) {
            this.logo_url_plus = logo_url_plus;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public String getCountry_en_name() {
            return country_en_name;
        }

        public void setCountry_en_name(String country_en_name) {
            this.country_en_name = country_en_name;
        }

        public String getPhoto_format() {
            return photo_format;
        }

        public void setPhoto_format(String photo_format) {
            this.photo_format = photo_format;
        }

        public int getCountry_id() {
            return country_id;
        }

        public void setCountry_id(int country_id) {
            this.country_id = country_id;
        }
    }
}
