package com.usamsl.global.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.my.entity.AllOrder;
import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 * 描述：我的：我的订单显示
 */
public class MyOrderAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    private List<AllOrder.ResultBean> mData;

    public MyOrderAdapter(Context mContext, List<AllOrder.ResultBean> mData) {
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
        AllOrder.ResultBean order = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.finish_order_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.tv_orderNumber = (TextView) view.findViewById(R.id.tv_orderNumber);
            viewHolder.tv_received = (TextView) view.findViewById(R.id.tv_received);
            viewHolder.tv_detail = (TextView) view.findViewById(R.id.tv_detail);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_detail.setVisibility(View.GONE);
        viewHolder.tv_name.setText(order.getContact_name());
        StringBuilder sb = new StringBuilder();
        sb.append(order.getVisa_name());
        if (order.getVisa_area_name() != null && !order.getVisa_area_name().equals("") &&
                !order.getVisa_area_name().equals(" ")) {
            sb.append("-");
            sb.append(order.getVisa_area_name());
        }
        viewHolder.tv_type.setText(sb.toString());
        viewHolder.tv_orderNumber.setText("订单编号：" + order.getOrder_code());
        if (order.getVisa_id() == 1) {
            if (order.getIs_pay() == 1) {
                switch (order.getOrder_status()) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 7:
                    case 8:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("￥" + order.getPayment() + "（已付款）");
                        break;
                    case 9:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_received.setText("审核通过");
                        break;
                    case 10:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("审核不通过");
                        break;
                    case 11:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#d78e8e"));
                        viewHolder.tv_received.setText("撤销签证");
                        break;
                }
            } else {
                switch (order.getOrder_status()) {
                    case 0:
                    case 1:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("￥" + order.getPayment() + "（未付款）");
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("￥" + order.getPayment() + "（已付款）");
                        break;
                    case 9:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_received.setText("审核通过");
                        break;
                    case 10:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("审核不通过");
                        break;
                    case 11:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#d78e8e"));
                        viewHolder.tv_received.setText("撤销签证");
                        break;
                }
            }
        } else {
            if (order.getIs_pay() == 1) {
                switch (order.getOrder_status()) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("￥" + order.getPayment() + "（已付款）");
                        break;
                    case 7:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_received.setText("已发送至指定网点，请速取");
                        break;
                    default:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("已领取");
                }
            } else {
                switch (order.getOrder_status()) {
                    case 0:
                    case 1:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("￥" + order.getPayment() + "（未付款）");
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("￥" + order.getPayment() + "（已付款）");
                        break;
                    case 7:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#78b200"));
                        viewHolder.tv_received.setText("已发送至指定网点，请速取");
                        break;
                    default:
                        viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                        viewHolder.tv_received.setText("已领取");
                }
            }
        }
        return view;
    }

    class ViewHolder {
        //姓名
        TextView tv_name;
        //类型
        TextView tv_type;
        //订单编号
        TextView tv_orderNumber;
        //是否领取
        TextView tv_received;
        TextView tv_detail;
    }
}
