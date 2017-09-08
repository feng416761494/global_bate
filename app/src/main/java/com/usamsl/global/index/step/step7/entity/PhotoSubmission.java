package com.usamsl.global.index.step.step7.entity;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/13.
 * 描述：对应的签证人员类型所需要的材料
 */
public class PhotoSubmission implements Parcelable{
    //所需材料
    private String typeMaterial;
    //材料要求
    private List<String> typeMaterialDemand;
    //材料id
    private int order_datum_id;
    //附件id
    private int attachment_id;
    //材料类型
    private String visa_datum_type;
    //照片地址
    private String photoUrl;
    //是否为必要材料
    private boolean necessary = false;
    //订单ID
    private int order_id;
    //联系人id
    private int contact_id;
    //联系人姓名
    private String contact_name;
    public PhotoSubmission() {
    }

    protected PhotoSubmission(Parcel in) {
        typeMaterial = in.readString();
        typeMaterialDemand = in.createStringArrayList();
        order_datum_id = in.readInt();
        attachment_id = in.readInt();
        visa_datum_type = in.readString();
        photoUrl = in.readString();
        necessary = in.readByte() != 0;
        order_id = in.readInt();
        contact_id = in.readInt();
        contact_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeMaterial);
        dest.writeStringList(typeMaterialDemand);
        dest.writeInt(order_datum_id);
        dest.writeInt(attachment_id);
        dest.writeString(visa_datum_type);
        dest.writeString(photoUrl);
        dest.writeByte((byte) (necessary ? 1 : 0));
        dest.writeInt(order_id);
        dest.writeInt(contact_id);
        dest.writeString(contact_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoSubmission> CREATOR = new Creator<PhotoSubmission>() {
        @Override
        public PhotoSubmission createFromParcel(Parcel in) {
            return new PhotoSubmission(in);
        }

        @Override
        public PhotoSubmission[] newArray(int size) {
            return new PhotoSubmission[size];
        }
    };

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_datum_id() {
        return order_datum_id;
    }

    public void setOrder_datum_id(int order_datum_id) {
        this.order_datum_id = order_datum_id;
    }

    public int getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(int attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getVisa_datum_type() {
        return visa_datum_type;
    }

    public void setVisa_datum_type(String visa_datum_type) {
        this.visa_datum_type = visa_datum_type;
    }

    public String getTypeMaterial() {
        return typeMaterial;
    }

    public void setTypeMaterial(String typeMaterial) {
        this.typeMaterial = typeMaterial;
    }

    public List<String> getTypeMaterialDemand() {
        return typeMaterialDemand;
    }

    public void setTypeMaterialDemand(List<String> typeMaterialDemand) {
        this.typeMaterialDemand = typeMaterialDemand;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isNecessary() {
        return necessary;
    }

    public void setNecessary(boolean necessary) {
        this.necessary = necessary;
    }
}
