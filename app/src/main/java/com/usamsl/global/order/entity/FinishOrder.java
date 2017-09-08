package com.usamsl.global.order.entity;

/**
 * Created by Administrator on 2016/12/21.
 * 描述：已完成订单
 */
public class FinishOrder {
    //姓名
    private String name;
    //订单编号
    private String orderNumber;
    //签证种类
    private String visaType;
    //签证领区
    private String visaZone;
    //是否有保险
    private boolean safe = false;
    //是否领取(received:已领取,unReceived:未领取)
    private String received = "paid";

    public FinishOrder() {
    }

    public FinishOrder(String name, String visaType,
                       String visaZone, boolean safe,
                       String orderNumber, String received) {
        this.name = name;
        this.visaType = visaType;
        this.orderNumber = orderNumber;
        this.visaZone = visaZone;
        this.safe = safe;
        this.received = received;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getVisaZone() {
        return visaZone;
    }

    public void setVisaZone(String visaZone) {
        this.visaZone = visaZone;
    }

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }
}
