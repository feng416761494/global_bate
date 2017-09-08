package com.usamsl.global.index.data;

import com.usamsl.global.index.entity.City;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSL Vacation Inc on 2016/12/14.
 * 描述：首页数据
 */
public class IndexVisaData {
    /**
     * 首页跳转到城市定位输入：热门城市的数据
     */
    public static List<City> cityList = null;
    public static List<City> initHotCityData() {
        cityList = new ArrayList<>();
        cityList.add(new City("北京"));
        cityList.add(new City("上海"));
        cityList.add(new City("广州"));
        cityList.add(new City("深圳"));
        cityList.add(new City("南京"));
        cityList.add(new City("杭州"));
        cityList.add(new City("苏州"));
        cityList.add(new City("天津"));
        cityList.add(new City("大连"));
        cityList.add(new City("重庆"));
        cityList.add(new City("武汉"));
        cityList.add(new City("成都"));
        cityList.add(new City("西安"));
        cityList.add(new City("厦门"));
        return cityList;
    }
}
