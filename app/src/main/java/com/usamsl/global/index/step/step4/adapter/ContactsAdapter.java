package com.usamsl.global.index.step.step4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step4.entity.AllContacts;
import com.usamsl.global.index.step.step4.entity.Contacts;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 * 描述：材料提交：已有联系人显示
 */
public class ContactsAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    //显示的已有联系人集合
    private List<AllContacts.ResultBean> mData;

    public ContactsAdapter(Context mContext, List<AllContacts.ResultBean> mData) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final AllContacts.ResultBean contacts = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.contacts_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(contacts.getContact_name());
        return view;
    }

    class ViewHolder {
        //名字
        TextView name;
    }
}
