package com.usamsl.global.index.step.step2.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/24.
 * 描述：签证之前的人员类型对应的所需要的材料
 */
public class PeopleTypeMaterial {
    private int id;
    //人员类型
    private String type;
    //人员类型的ID
    private String typeID;
    //所需材料
    private String typeMaterial;
    //材料要求
    private List<String> typeMaterialDemand;

    public PeopleTypeMaterial() {
    }

    public PeopleTypeMaterial(String type, String typeMaterial, List<String> typeMaterialDemand) {
        this.type = type;
        this.typeMaterial = typeMaterial;
        this.typeMaterialDemand = typeMaterialDemand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
