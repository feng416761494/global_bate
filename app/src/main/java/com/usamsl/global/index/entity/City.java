package com.usamsl.global.index.entity;

/**
 * Created by Administrator on 2016/12/20.
 * 描述：热门城市
 */
public class City {

    //热门城市名称
    private String cityName;

    public City() {
    }

    public City(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
