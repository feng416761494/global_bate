package com.usamsl.global.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.order.entity.OrderMaterialData;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 * 描述：材料填写显示
 */
public class OrderMaterialRecyclerViewAdapter extends
        RecyclerView.Adapter<OrderMaterialRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<OrderMaterialData> mData;
    private OrderDetailsItemClickListener listener;
    private Context context;


    public OrderMaterialRecyclerViewAdapter(Context context2, List<OrderMaterialData> mData) {
        mInflater = LayoutInflater.from(context2);
        this.mData = mData;
        this.context = context2;
    }

    public void setDetailsItemClickListener(OrderDetailsItemClickListener listener2){
        this.listener = listener2;
    }

    public void setData(List<OrderMaterialData> mData) {
        this.mData = mData;
    }


    public interface OrderDetailsItemClickListener{
        void detailsItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.order_details_flow, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.img_check = (ImageView) view.findViewById(R.id.img_check);
        viewHolder.tv_data = (TextView) view.findViewById(R.id.tv_data);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        OrderMaterialData data = mData.get(i);
        viewHolder.tv_data.setText(data.getMaterialName());
        if (data.isFilled()) {
            viewHolder.img_check.setVisibility(View.VISIBLE);
            viewHolder.tv_data.setTextColor(Color.parseColor("#bcd78e"));
            viewHolder.tv_data.setBackgroundResource(R.drawable.order_details_flow_bg1);
        } else {
            viewHolder.img_check.setVisibility(View.INVISIBLE);
            viewHolder.tv_data.setTextColor(Color.parseColor("#ababab"));
            viewHolder.tv_data.setBackgroundResource(R.drawable.order_details_flow_bg2);
        }

//        if(listener != null){
//            viewHolder.tv_data.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int clickPosition = viewHolder.getLayoutPosition();
//                    listener.detailsItemClick(viewHolder.tv_data,clickPosition);
//                }
//            });
//        }
        if(listener != null){
            viewHolder.tv_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.detailsItemClick(viewHolder.tv_data,i);
                }
            });
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
        //check图片
        ImageView img_check;
        //材料
        TextView tv_data;
    }
}
