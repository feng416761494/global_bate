package com.usamsl.global.order.entity;

/**
 * Created by Administrator on 2016/12/21.
 * 描述：未完成订单
 */
public class UnFinishOrder {
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
    //进行到哪一步
    private int step;
    //金额
    private String money;
    public UnFinishOrder() {
    }

    public UnFinishOrder(String name, String orderNumber,
                         String visaType, String visaZone,
                         boolean safe, int step,
                         String money) {
        this.name = name;
        this.orderNumber = orderNumber;
        this.visaType = visaType;
        this.visaZone = visaZone;
        this.safe = safe;
        this.step = step;
        this.money = money;
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

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
