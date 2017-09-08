package com.usamsl.global.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.service.entity.VisaConsultationCountry;

import java.util.List;

/**
 * Created by MSL Vacation Inc on 2017/1/25.
 * 描述：服务模块：热门问题展示
 */
public class ProblemAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    private List<String> mData;

    public ProblemAdapter(Context mContext, List<String> mData) {
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
        String str = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.problem_item, null);
            viewHolder = new ViewHolder();
            viewHolder.problem = (TextView) view.findViewById(R.id.tv_problem);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.problem.setText("Q " + (i + 1) + ":" + str);
        return view;
    }

    class ViewHolder {
        TextView problem;
    }

}

