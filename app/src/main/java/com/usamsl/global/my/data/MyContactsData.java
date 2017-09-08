package com.usamsl.global.my.data;

import com.usamsl.global.my.entity.MyContacts;
import com.usamsl.global.my.entity.MyGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30.
 * 描述：我的联系人数据
 */
public class MyContactsData {
    /**
     * 所有的联系人
     */
    public static List<MyContacts> myContacts;

    public static void initMyContacts(List<MyContacts> list) {
        myContacts = list;
    }

    /**
     * 删除联系人
     */
    public static void deleteMyContacts(MyContacts contacts) {
        myContacts.remove(contacts);
    }


    /***
     * 所有的分组
     */
    public static List<MyGroup.ResultBean> myGroups;

    public static List<MyGroup.ResultBean> initMyGroups(List<MyGroup.ResultBean> been) {
        myGroups = new ArrayList<>();
        myGroups.addAll(been);
        return myGroups;
    }

    /**
     * 删除分组
     */
    public static void deleteMyGroup(String group) {
        for (MyGroup.ResultBean bean : myGroups) {
            if (bean.getGroup_name().equals(group)) {
                myGroups.remove(bean);
                break;
            }
        }
    }

    /**
     * 添加分组
     */
    public static void addMyGroup(String group) {
        MyGroup.ResultBean bean = new MyGroup.ResultBean();
        bean.setGroup_name(group);
        myGroups.add(bean);
    }

}
