package com.usamsl.global.index.step.step2.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/20.
 */
public class CustTypeEntity implements Serializable{
    private String typeName;
    private String typeNumber;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }
}
