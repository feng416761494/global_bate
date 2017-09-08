package com.usamsl.global.order.entity;

/**
 * Created by Administrator on 2016/12/22.
 * 描述：签证需要的材料数据
 */
public class OrderMaterialData {
    //材料名字
    private String materialName;
    //材料是否填完
    private boolean filled = false;
    private String datumType;

    public OrderMaterialData() {
    }

    public String getDatumType() {
        return datumType;
    }

    public void setDatumType(String datumType) {
        this.datumType = datumType;
    }

    public OrderMaterialData(String materialName, boolean filled) {
        this.materialName = materialName;
        this.filled = filled;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}
