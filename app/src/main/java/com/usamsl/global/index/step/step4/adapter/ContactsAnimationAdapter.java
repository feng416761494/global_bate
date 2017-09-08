package com.usamsl.global.index.step.step4.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import com.usamsl.global.index.step.step4.entity.AllContacts;

import net.println.itemanimatable.AnimatableExpandableListAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */
public class ContactsAnimationAdapter extends AnimatableExpandableListAdapter {

    private Context context;
    private List<AllContacts.ResultBean> mData;

    public ContactsAnimationAdapter(Context context2, List<AllContacts.ResultBean> mData2) {
        this.context = context2;
        this.mData = mData2;
    }

    @Override
    public void removeGroupAt(int groupPosition) {

    }

    @Override
    public void removeChildAt(int groupPosition, int childPosition) {

    }

    @Override
    public void removeGroup(Object group) {

    }

    @Override
    public void removeChild(Object child, int groupPosition) {

    }

    @Override
    public void addGroup(Object group) {

    }

    @Override
    public void addGroupAt(Object group, int groupPosition) {

    }

    @Override
    public void addChildToGroup(Object child, int groupPosition) {

    }

    @Override
    public void addChildToGroupAt(Object child, int groupPosition, int childPosition) {

    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
