package com.usamsl.global.index.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 国家加载
 */
public class VisaCountry {


    /**
     * result : [{"ensign_url":"http://139.196.88.32/oss/upload/20170210/be1f607e968d4d02b2b834b51331b138.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/005019f7db1147beaf354160362838c2.jpg","country_name":"美国","photo_format":"774fc5348ae0419ca5328c4a0446c46b","country_id":11},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/2d9b5c82756540c7811cdbb60f06ca3f.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/e9af36e3fd21436aaff18833a3b845ed.jpg","country_name":"加拿大","photo_format":"be6b28f22a194c5ab38f5c28816bbe08","country_id":12},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/6aab46da622f4d99b85a075bf1a7629d.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/b0bd1142d9af40e7a30f984ebc5f7ae1.jpg","country_name":"日本","photo_format":"e972c319e4c24deaa297aa992f327a81","country_id":10},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/199363b85400428cb3964c662848bc93.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/0c9146a81dcf4c15a161e5c8b007978e.jpg","country_name":"澳大利亚","photo_format":"6c39c16787514071a4e87fd1c046dda6","country_id":32},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/e950a94a01f949fea99f833fbf3b869f.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/71a79a04466d4c5e8040b2e46b0897e3.jpg","country_name":"法国","photo_format":"449146bd88eb4aa18c7ae42623610b62","country_id":31},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/f49c6a30b1624a8eb4f397c065b1d2de.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/a048ff22a4114cb79494ca3bcd396092.jpg","country_name":"意大利","photo_format":"1cc70b94efb1439a859fe1a5f581aa32","country_id":38},{"ensign_url":"http://139.196.88.32/oss/upload/20170210/a4a4708b999a4562b22227a8b41106da.png","bg_img_url":"http://139.196.88.32/oss/upload/20170210/8cc924a788034623a856f6e5af9f37e4.jpg","country_name":"英国","photo_format":"5689f70bc0964c908c7770d780c5634c","country_id":37}]
     * reason : 获取数据成功
     * error_code : 0
     */

    private String reason;
    private int error_code;
    private List<ResultBean> result;

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

    public static class ResultBean implements Parcelable{
        /**
         * ensign_url : http://139.196.88.32/oss/upload/20170210/be1f607e968d4d02b2b834b51331b138.png
         * bg_img_url : http://139.196.88.32/oss/upload/20170210/005019f7db1147beaf354160362838c2.jpg
         * country_name : 美国
         * photo_format : 774fc5348ae0419ca5328c4a0446c46b
         * country_id : 11
         */

        private String ensign_url;
        private String bg_img_url;
        private String country_name;
        private String photo_format;
        private int country_id;
        public String form_url;

        protected ResultBean(Parcel in) {
            ensign_url = in.readString();
            bg_img_url = in.readString();
            country_name = in.readString();
            photo_format = in.readString();
            country_id = in.readInt();
            form_url = in.readString();
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel in) {
                return new ResultBean(in);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };

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

        public String getBg_img_url() {
            return bg_img_url;
        }

        public void setBg_img_url(String bg_img_url) {
            this.bg_img_url = bg_img_url;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(ensign_url);
            parcel.writeString(bg_img_url);
            parcel.writeString(country_name);
            parcel.writeString(photo_format);
            parcel.writeInt(country_id);
            parcel.writeString(form_url);
        }
    }
}
