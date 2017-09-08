package com.usamsl.global.index.step.step2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step2.entity.CustTypeEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */
public class CustTypePeopleAdapter extends BaseAdapter {
    private List<CustTypeEntity> custList;
    private Context context;
    private LayoutInflater inflater;
    private int selectPostion;
    public CustTypePeopleAdapter(List<CustTypeEntity> custList2,Context context2) {
        this.custList = custList2;
        this.inflater = LayoutInflater.from(context2);
    }

    public void selectPosition(int position){
        this.selectPostion = position;
    }

    @Override
    public int getCount() {
        return custList.size();
    }

    @Override
    public Object getItem(int position) {
        return custList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustTypePeopleViewHolder viewHolder = null;
        CustTypeEntity entity = custList.get(position);
        if(convertView == null){
            viewHolder = new CustTypePeopleViewHolder();
            convertView = inflater.inflate(R.layout.before_visa_cust_type_listview_item,null);
            viewHolder.tvCustType = (TextView) convertView.findViewById(R.id.tvCustType);
            viewHolder.imgLine = (ImageView) convertView.findViewById(R.id.imgBottomLine);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (CustTypePeopleViewHolder) convertView.getTag();
        }
        if(position == selectPostion){
            viewHolder.imgLine.setVisibility(View.VISIBLE);
        }else{
            viewHolder.imgLine.setVisibility(View.GONE);
        }
        viewHolder.tvCustType.setText(entity.getTypeName());
        return convertView;
    }

    class CustTypePeopleViewHolder{
        TextView tvCustType;
        ImageView imgLine;
    }
}
