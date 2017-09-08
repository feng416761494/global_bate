package com.usamsl.global.index.step.step2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step2.entity.CustTypeEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */
public class CustTypeRecycleViewAdapter extends RecyclerView.Adapter<CustTypeRecycleViewAdapter.CustTypePeopleViewHolder>{
    private List<CustTypeEntity> custList;
    private Context context;
    private int selectPosition;
    private CustTypeRecycleViewClickListener listener;

    public CustTypeRecycleViewAdapter(List<CustTypeEntity> custList, Context context) {
        this.custList = custList;
        this.context = context;
    }

    public void clickItem(int mSelectPosition){
        this.selectPosition = mSelectPosition;
    }

    public interface CustTypeRecycleViewClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnclickListener(CustTypeRecycleViewClickListener mListener){
        this.listener = mListener;
    }

    @Override
    public CustTypePeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.before_visa_cust_type_listview_item, null);
        }
        CustTypePeopleViewHolder viewHolder = new CustTypePeopleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustTypePeopleViewHolder holder, final int position) {
        CustTypeEntity entity = custList.get(position);
        holder.tvCustType.setText(entity.getTypeName());
        if(position == selectPosition){
            holder.imgLine.setVisibility(View.VISIBLE);
        }else{
            holder.imgLine.setVisibility(View.GONE);
        }
        if(listener != null){
            holder.tvCustType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.tvCustType, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return custList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class CustTypePeopleViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustType;
        ImageView imgLine;

        public CustTypePeopleViewHolder(View itemView) {
            super(itemView);
            tvCustType = (TextView) itemView.findViewById(R.id.tvCustType);
            imgLine = (ImageView) itemView.findViewById(R.id.imgBottomLine);
        }
    }
}
