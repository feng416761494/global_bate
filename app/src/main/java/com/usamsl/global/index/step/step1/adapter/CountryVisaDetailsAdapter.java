package com.usamsl.global.index.step.step1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step1.entity.VisaDetails;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 * 描述：国家签证详情数据显示的adapter
 */
public class CountryVisaDetailsAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    //显示的Country集合
    private List<VisaDetails.ResultBean> mData;

    public CountryVisaDetailsAdapter(Context mContext, List<VisaDetails.ResultBean> mData) {
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
        ViewHolder viewHolder;
        VisaDetails.ResultBean details = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.country_visa_details_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.type = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.money = (TextView) view.findViewById(R.id.tv_money);
            viewHolder.advantage1 = (TextView) view.findViewById(R.id.tv_advantage1);
            viewHolder.advantage2 = (TextView) view.findViewById(R.id.tv_advantage2);
            viewHolder.left = (TextView) view.findViewById(R.id.tv_left);
            viewHolder.tv1 = (TextView) view.findViewById(R.id.tv1);
            viewHolder.tv2 = (TextView) view.findViewById(R.id.tv2);
            viewHolder.tv3 = (TextView) view.findViewById(R.id.tv3);
            viewHolder.oldMoney = (TextView) view.findViewById(R.id.tv_oldMoney);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.money.setText("￥" + details.getPreferential_price());
        if (details.getPrice() == 0) {
            viewHolder.oldMoney.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.oldMoney.setText("￥" + details.getPrice());
        }
        if (!details.getDeal_type_name().equals(" ")) {
            viewHolder.type.setText(details.getVisa_name() + "-" + details.getVisa_area_name()
                    + "-" + details.getDeal_type_name());
        } else {
            viewHolder.type.setText(details.getVisa_name() + "-" + details.getVisa_area_name());
        }
        if (details.getVisa_type_id() == 1) {
            viewHolder.left.setBackgroundColor(Color.parseColor("#bcd78e"));
        } else if (details.getVisa_type_id() == 2) {
            viewHolder.left.setBackgroundColor(Color.parseColor("#8ed7ca"));
        } else if (details.getVisa_type_id() == 3) {
            viewHolder.left.setBackgroundColor(Color.parseColor("#dfbca9"));
        } else if (details.getVisa_type_id() == 4) {
            viewHolder.left.setBackgroundColor(Color.parseColor("#d5a9df"));
        }
        viewHolder.tv1.setText("预计" + details.getPlan_weekday() + "个工作日");
        if (!details.getValidity_time().equals("")) {
            viewHolder.tv2.setText(details.getValidity_time());
        } else {
            viewHolder.tv2.setVisibility(View.GONE);
        }
        if (!details.getIs_stop_set().equals("")) {
            viewHolder.tv3.setText(details.getIs_stop_set());
        } else {
            viewHolder.tv3.setVisibility(View.GONE);
        }
        if (details.getIs_refund().equals("1")) {
            viewHolder.advantage1.setText("拒签退全款");
        } else {
            viewHolder.advantage1.setVisibility(View.GONE);
        }
        if (details.getIs_easy().equals("1")) {
            viewHolder.advantage2.setText("材料易递交");
        } else {
            viewHolder.advantage2.setVisibility(View.GONE);
        }
        //中间加条横线
        viewHolder.oldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        return view;
    }

    class ViewHolder {
        //类型
        TextView type;
        //价格
        TextView money;
        //原价
        TextView oldMoney;
        //是否拒签退全款
        TextView advantage1;
        //材料易递交
        TextView advantage2;
        //左侧区分签证类型的线
        TextView left;
        //工作日
        TextView tv1;
        //几年内有效
        TextView tv2;
        // 是否可停留海关定
        TextView tv3;
    }
}
