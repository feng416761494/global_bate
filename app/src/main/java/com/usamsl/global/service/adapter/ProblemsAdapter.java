package com.usamsl.global.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.service.entity.Problems;

import java.util.List;

/**
 * Created by MSL Vacation Inc on 2017/6/5.
 * 描述：服务模块：问题库展示
 */
public class ProblemsAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    private List<Problems.ResultBean> mData;

    public ProblemsAdapter(Context mContext, List<Problems.ResultBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    //上拉加载更多的时候，获取到新的数据添加到原有的list数据中
    public void loadMoreData(List<Problems.ResultBean> moreData){
        this.mData = moreData;
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
        Problems.ResultBean problems = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.problem_item, null);
            viewHolder = new ViewHolder();
            viewHolder.problem = (TextView) view.findViewById(R.id.tv_problem);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.problem.setText("Q " + (i + 1) + ":" + problems.getProblem());
        return view;
    }

    class ViewHolder {
        TextView problem;
    }

}

