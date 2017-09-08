package com.usamsl.global.index.step.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/2/8.
 * 新增订单时所需要的变量
 */
public class AddOrder implements Parcelable {
    //签证id
    private int visa_id;
    //人员类型
    private int cust_type;
    //联系人id
    private int contact_id;
    //联系人的名字
    private String contact_name;
    //银行网点id
    private int bank_outlets_id;
    //签证领取Id
    private int visa_area_id;

    public AddOrder() {
    }

    protected AddOrder(Parcel in) {
        visa_id = in.readInt();
        cust_type = in.readInt();
        contact_id = in.readInt();
        contact_name = in.readString();
        bank_outlets_id = in.readInt();
        visa_area_id = in.readInt();
    }

    public static final Creator<AddOrder> CREATOR = new Creator<AddOrder>() {
        @Override
        public AddOrder createFromParcel(Parcel in) {
            return new AddOrder(in);
        }

        @Override
        public AddOrder[] newArray(int size) {
            return new AddOrder[size];
        }
    };

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public int getVisa_id() {
        return visa_id;
    }

    public void setVisa_id(int visa_id) {
        this.visa_id = visa_id;
    }

    public int getCust_type() {
        return cust_type;
    }

    public void setCust_type(int cust_type) {
        this.cust_type = cust_type;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public int getBank_outlets_id() {
        return bank_outlets_id;
    }

    public void setBank_outlets_id(int bank_outlets_id) {
        this.bank_outlets_id = bank_outlets_id;
    }

    public int getVisa_area_id() {
        return visa_area_id;
    }

    public void setVisa_area_id(int visa_area_id) {
        this.visa_area_id = visa_area_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(visa_id);
        parcel.writeInt(cust_type);
        parcel.writeInt(contact_id);
        parcel.writeString(contact_name);
        parcel.writeInt(bank_outlets_id);
        parcel.writeInt(visa_area_id);
    }
}
