package com.usamsl.global.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.usamsl.global.R;
import com.usamsl.global.my.entity.AllOrder;
import com.usamsl.global.order.activity.OrderDetailsActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 * 描述：已完成订单显示
 */
public class FinishOrderAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    private List<AllOrder.ResultBean> mData;

    public FinishOrderAdapter(Context mContext, List<AllOrder.ResultBean> mData) {
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
        final AllOrder.ResultBean order = mData.get(i);
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
        viewHolder.tv_name.setText(order.getContact_name());
        viewHolder.tv_detail.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        sb.append(order.getVisa_name());
        if (order.getVisa_area_name() != null &&
                !order.getVisa_area_name().equals("") &&
                !order.getVisa_area_name().equals(" ")) {
            sb.append("-");
            sb.append(order.getVisa_area_name());
        }
        viewHolder.tv_type.setText(sb.toString());
        viewHolder.tv_orderNumber.setText("订单编号：" + order.getOrder_code());
        if (order.getVisa_id() > 1) {
            viewHolder.tv_detail.setVisibility(View.VISIBLE);
            if (order.getOrder_status() == 8) {
                viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                viewHolder.tv_received.setText("已领取");
            } else if (order.getOrder_status() == 7) {
                viewHolder.tv_received.setTextColor(Color.parseColor("#78b200"));
                viewHolder.tv_received.setText("已发送至指定网点，请速取");
            }else if(order.getOrder_status() == 99){
                viewHolder.tv_received.setTextColor(Color.parseColor("#333333"));
                viewHolder.tv_received.setText("已退款");
            }else if (order.getOrder_status() == 9) {
                viewHolder.tv_received.setTextColor(Color.parseColor("#78b200"));
                viewHolder.tv_received.setText("审核通过");
            } else if (order.getOrder_status() == 10) {
                viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                viewHolder.tv_received.setText("审核未通过");
            } else if (order.getOrder_status() == 11) {
                viewHolder.tv_received.setTextColor(Color.parseColor("#d78e8e"));
                viewHolder.tv_received.setText("撤销签证");
            }
            viewHolder.tv_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转至详情界面
                    Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                    intent.putExtra("finish", 1);
                    intent.putExtra("order", order);
                    mContext.startActivity(intent);
                }
            });
        } else if (order.getVisa_id() == 1) {
            viewHolder.tv_detail.setVisibility(View.GONE);
            if (order.getOrder_status() == 9) {
                viewHolder.tv_received.setTextColor(Color.parseColor("#78b200"));
                viewHolder.tv_received.setText("审核通过");
            } else if (order.getOrder_status() == 10) {
                viewHolder.tv_received.setTextColor(Color.parseColor("#8d8d8d"));
                viewHolder.tv_received.setText("审核未通过");
            } else if (order.getOrder_status() == 11) {
                viewHolder.tv_received.setTextColor(Color.parseColor("#d78e8e"));
                viewHolder.tv_received.setText("撤销签证");
            }else if(order.getOrder_status() == 99){
                viewHolder.tv_received.setTextColor(Color.parseColor("#333333"));
                viewHolder.tv_received.setText("已退款");
            }
            viewHolder.tv_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转至详情界面
                    Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                    intent.putExtra("finish", 1);
                    intent.putExtra("order", order);
                    mContext.startActivity(intent);
                }
            });
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
        //详情文本
        TextView tv_detail;
    }
}
