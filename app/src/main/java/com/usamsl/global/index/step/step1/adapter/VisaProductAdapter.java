package com.usamsl.global.index.step.step1.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step1.activity.CountryVisaDetailsActivity;
import com.usamsl.global.index.step.step1.entity.ListVisa;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */
public class VisaProductAdapter extends BaseAdapter{
    private Context context;
    private List<ListVisa> listVisa;
    private LayoutInflater inflater;
    public VisaProductAdapter(Context context, List<ListVisa> listVisa) {
        this.context = context;
        this.listVisa = listVisa;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listVisa.size();
    }

    @Override
    public Object getItem(int position) {
        return listVisa.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VisaProductViewHolder holder = null;
        if(convertView == null){
            holder = new VisaProductViewHolder();
            convertView = inflater.inflate(R.layout.visa_product_list_item,null);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tvPresent = (TextView) convertView.findViewById(R.id.tvPresent);
            holder.tv_oldMoney = (TextView) convertView.findViewById(R.id.tv_oldMoney);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            convertView.setTag(holder);
        }else{
            holder = (VisaProductViewHolder) convertView.getTag();
        }
        ListVisa entity = listVisa.get(position);
        holder.tv1.setText("预计"+entity.getPlan_weekday()+"个工作日");
        holder.tv2.setText(entity.getValidity_time());
        holder.tv3.setText(entity.getIs_stop_set());
        holder.tv_type.setText(entity.getVisa_name() + "-" + entity.getVisa_type_name());
        holder.tvPresent.setText(entity.getVisa_label());
        holder.tv_oldMoney.setText("￥"+entity.getPrice());
        holder.tv_oldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_money.setText("￥" + entity.getPreferential_price());
        return convertView;
    }


    class VisaProductViewHolder{
        TextView tv_type,tvPresent,tv_oldMoney,tv_money,tv1,tv2,tv3;
    }
}
