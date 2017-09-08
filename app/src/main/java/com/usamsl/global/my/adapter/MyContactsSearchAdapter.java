package com.usamsl.global.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.my.entity.MyContacts;
import com.usamsl.global.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 * 描述：我的：我的联系人查询
 */
public class MyContactsSearchAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    private List<MyContacts> mData;
    public MyContactsSearchAdapter(Context mContext, List<MyContacts> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyContacts contacts = mData.get(i);
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
                    .error(R.drawable.usa)
                    .into(childViewHolder.photo);
        }else {
            childViewHolder.photo.setImageResource(R.drawable.usa);
        }
        return view;
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
