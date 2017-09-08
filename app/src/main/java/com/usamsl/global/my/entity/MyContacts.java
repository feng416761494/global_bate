package com.usamsl.global.my.entity;

/**
 * Created by Administrator on 2016/12/29.
 * 描述：我的联系人
 */
public class MyContacts {
    //资料是否完整
    private boolean complete;
    //姓名
    private String name;
    //头像
    private String photo;
    //电话
    private String tel;
    //分组
    private String group;
    //是否实名认证
    private boolean realName;
    //分组id
    private int groupId;
    //email
    private String email;
    //联系人id
    private int contactId;

    public MyContacts() {
    }

    public MyContacts(boolean complete, String name,
                      String photo, String tel,
                      String group, boolean realName,
                      int groupId,String email,int contactId) {
        this.complete = complete;
        this.name = name;
        this.photo = photo;
        this.tel = tel;
        this.group = group;
        this.realName = realName;
        this.groupId = groupId;
        this.email=email;
        this.contactId=contactId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isRealName() {
        return realName;
    }

    public void setRealName(boolean realName) {
        this.realName = realName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
