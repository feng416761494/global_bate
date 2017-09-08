package com.usamsl.global.my.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 * 描述：我的分组
 */
public class MyGroup {

    /**
     * result : [{"group_name":"我的好友","id":1,"show_order":1}]
     * reason : 获取分组成功
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

    public static class ResultBean implements Parcelable {
        /**
         * group_name : 我的好友
         * id : 1
         * show_order : 1
         */

        private String group_name;
        private int id;
        private int show_order;

        public ResultBean(Parcel in) {
            group_name = in.readString();
            id = in.readInt();
            show_order = in.readInt();
        }

        public ResultBean() {
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

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getShow_order() {
            return show_order;
        }

        public void setShow_order(int show_order) {
            this.show_order = show_order;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(group_name);
            parcel.writeInt(id);
            parcel.writeInt(show_order);
        }
    }
}
