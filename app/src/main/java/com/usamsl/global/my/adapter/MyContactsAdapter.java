package com.usamsl.global.my.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.my.entity.MyContacts;
import com.usamsl.global.my.entity.MyGroup;
import com.usamsl.global.view.CircleImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/29.
 * 我的联系人显示列表
 */
public class MyContactsAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<MyGroup.ResultBean> groups;
    private Map<String,List<MyContacts>> mData;
    private int clickPostion = -1;


    public MyContactsAdapter(Context mContext, List<MyGroup.ResultBean> groups,
                             Map<String,List<MyContacts>> mData) {
        this.mContext = mContext;
        this.groups = groups;
        this.mData = mData;
    }


    public void setPointRotate(int groupPosition) {
        this.clickPostion = groupPosition;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        String groupName = groups.get(i).getGroup_name();
        int childCount = 0;
        if(mData != null && mData.size() > 0){
            if(mData.get(groupName) != null){
                childCount = mData.get(groupName).size();
            }
        }
        return childCount;
    }

    @Override
    public Object getGroup(int i) {
        return mData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        String groupName = groups.get(i).getGroup_name();
        MyContacts contacts=mData.get(groupName).get(i1);
        return contacts;
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
        String group = groups.get(i).getGroup_name();
        ParentViewHolder parentViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.my_contacts_parent, null);
            parentViewHolder = new ParentViewHolder();
            parentViewHolder.groups = (TextView) view.findViewById(R.id.tv_parent);
            parentViewHolder.img = (ImageView) view.findViewById(R.id.img);
            view.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) view.getTag();
        }
        parentViewHolder.groups.setText(group);
        Animation animation;
//        boolean CLICKED = true;
//        if(clickPostion == i){
//            if(isExpanded){
//                animation = AnimationUtils.loadAnimation(mContext, R.anim.point_rotate_0_90);//point_rotate_0_90
//                parentViewHolder.img.startAnimation(animation);
//                animation.setFillAfter(!animation.getFillAfter());
//            }else{
//                animation = AnimationUtils.loadAnimation(mContext, R.anim.point_rotate_90_0);
//                parentViewHolder.img.startAnimation(animation);
//                animation.setFillAfter(!animation.getFillAfter());
//            }
//
//        }else{
//            parentViewHolder.img.setImageResource(R.drawable.right);
//        }


        if (isExpanded) {
            parentViewHolder.img.setImageResource(R.drawable.down);
        } else {
            parentViewHolder.img.setImageResource(R.drawable.right);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String groupName = groups.get(i).getGroup_name();
        MyContacts contacts=mData.get(groupName).get(i1);
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.my_contacts_child, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.name = (TextView) view.findViewById(R.id.tv_name);
            childViewHolder.photo = (CircleImageView) view.findViewById(R.id.img_photo);
            childViewHolder.realName = (TextView) view.findViewById(R.id.tv_realName);
            childViewHolder.tel = (TextView) view.findViewById(R.id.tv_tel);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        childViewHolder.name.setText(contacts.getName());
        if (contacts.isRealName()) {
            childViewHolder.realName.setText("已实名认证");
        } else {
            childViewHolder.realName.setText("未实名认证");
        }
        if (!contacts.getTel().equals("")) {
            childViewHolder.tel.setText(contacts.getTel());
        } else {
            childViewHolder.tel.setText("暂无");
        }
        if (contacts.getPhoto().substring(0,1).equals("h")){
            Picasso.with(mContext)
                    .load(contacts.getPhoto())
                    .error(R.drawable.user)
                    .into(childViewHolder.photo);
        }else {
            childViewHolder.photo.setImageResource(R.drawable.user);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class ParentViewHolder {
        //分组
        TextView groups;
        //是否展开图标
        ImageView img;
    }

    public class ChildViewHolder {
        //姓名
        TextView name;
        //头像
        CircleImageView photo;
        //电话
        TextView tel;
        //是否实名认证
        TextView realName;
    }
}
