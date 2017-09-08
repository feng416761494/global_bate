package com.usamsl.global.index.step.step2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step4.activity.ContactsActivity;
import com.usamsl.global.index.step.step4.adapter.PeopleTypeMaterialDemandAdapter;
import com.usamsl.global.index.step.step2.entity.PeopleTypeMaterial;
import com.usamsl.global.view.MyListView;

import net.println.itemanimatable.AnimatableExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 * 签证之前的人员选择
 */
public class BeforeVisaAdapter extends AnimatableExpandableListAdapter {
    private Context mContext;
    private List<String> groups;
    private Map<String, List<PeopleTypeMaterial>> mData;
    private int clickPostion = -1;


    public BeforeVisaAdapter(Context mContext, List<String> groups,
                       Map<String, List<PeopleTypeMaterial>> mData) {
        this.mContext = mContext;
        this.groups = groups;
        this.mData = mData;
    }

    //记录点击的位置
    public void setPointRotate(int groupPosition) {
        this.clickPostion = groupPosition;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        String groupName = groups.get(i);
        int childCount = mData.get(groupName).size();
        return childCount;
    }

    @Override
    public Object getGroup(int i) {
        return mData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        String groupName = groups.get(i);
        PeopleTypeMaterial typeMaterial = mData.get(groupName).get(i1);
        return typeMaterial;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String type = groups.get(i);
        BeforeVisaAdapter.ParentViewHolder parentViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.people_type_adapter, null);
            parentViewHolder = new BeforeVisaAdapter.ParentViewHolder();
            parentViewHolder.type = (TextView) view.findViewById(R.id.tv_type);
            parentViewHolder.check = (ImageView) view.findViewById(R.id.img_check);
            parentViewHolder.upOrDown = (ImageView) view.findViewById(R.id.img_upOrDown);
            parentViewHolder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            view.setTag(parentViewHolder);
        } else {
            parentViewHolder = (BeforeVisaAdapter.ParentViewHolder) view.getTag();
        }

        if (b) {
            parentViewHolder.check.setImageResource(R.drawable.round_selection);
            parentViewHolder.upOrDown.setImageResource(R.drawable.up);
            parentViewHolder.rl.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            parentViewHolder.check.setImageResource(R.drawable.round);
            parentViewHolder.upOrDown.setImageResource(R.drawable.down);
            parentViewHolder.rl.setBackgroundResource(R.drawable.before_visa_body_item_bg);
        }
        parentViewHolder.type.setText(type);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String groupName = groups.get(i);
        PeopleTypeMaterial typeMaterial = mData.get(groupName).get(i1);
        int bg = mData.get(groupName).size();
        BeforeVisaAdapter.ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.people_type_material, null);
            childViewHolder = new BeforeVisaAdapter.ChildViewHolder();
            childViewHolder.material = (TextView) view.findViewById(R.id.tv_material);
            childViewHolder.lv = (MyListView) view.findViewById(R.id.lv);
            childViewHolder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (BeforeVisaAdapter.ChildViewHolder) view.getTag();
        }
        childViewHolder.material.setText((i1 + 1) + "." + typeMaterial.getTypeMaterial());
        childViewHolder.materialDemands = new ArrayList<>();
        childViewHolder.materialDemands.addAll(typeMaterial.getTypeMaterialDemand());
        childViewHolder.adapter = new PeopleTypeMaterialDemandAdapter(mContext, childViewHolder.materialDemands);
        childViewHolder.lv.setAdapter(childViewHolder.adapter);
        switch (bg) {
            case 1:
                childViewHolder.rl.setBackgroundResource(R.drawable.people_type_bg2);
                childViewHolder.material.setPadding(0, 18, 0, 10);
                childViewHolder.lv.setPadding(0, 0, 0, 18);
                break;
            case 2:
                switch (i1) {
                    case 0:
                        childViewHolder.rl.setBackgroundResource(R.drawable.people_type_bg);
                        childViewHolder.material.setPadding(0, 18, 0, 0);
                        childViewHolder.lv.setPadding(0, 10, 0, 10);
                        break;
                    case 1:
                        childViewHolder.rl.setBackgroundResource(R.drawable.people_type_bg1);
                        childViewHolder.lv.setPadding(0, 10, 0, 18);
                        childViewHolder.material.setPadding(0, 0, 0, 0);
                        break;
                }
                break;
            default:
                if (i1 == 0) {
                    childViewHolder.rl.setBackgroundResource(R.drawable.people_type_bg);
                    childViewHolder.material.setPadding(0, 18, 0, 0);
                    childViewHolder.lv.setPadding(0, 10, 0, 10);
                } else if (i1 == bg - 1) {
                    childViewHolder.rl.setBackgroundResource(R.drawable.people_type_bg1);
                    childViewHolder.lv.setPadding(0, 10, 0, 18);
                    childViewHolder.material.setPadding(0, 0, 0, 0);
                } else {
                    childViewHolder.rl.setBackgroundColor(Color.parseColor("#f7f7f7"));
                    childViewHolder.lv.setPadding(0, 10, 0, 10);
                    childViewHolder.material.setPadding(0, 0, 0, 0);
                }
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class ParentViewHolder {
        //人员类型
        TextView type;
        //是否选中图片按钮
        ImageView check;
        //上下箭头
        ImageView upOrDown;
        //最外层容器
        RelativeLayout rl;
    }

    public class ChildViewHolder {
        //所需材料
        TextView material;
        //要求列表
        MyListView lv;
        List<String> materialDemands;
        PeopleTypeMaterialDemandAdapter adapter;
        //一个孩子
        RelativeLayout rl;
    }
}
