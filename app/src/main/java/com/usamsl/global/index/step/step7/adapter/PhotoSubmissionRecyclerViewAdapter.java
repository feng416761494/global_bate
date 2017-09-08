package com.usamsl.global.index.step.step7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 描述：材料提交中照片提交要求显示
 */
public class PhotoSubmissionRecyclerViewAdapter extends BaseAdapter {
    private List<String> mData;
    private Context mContext;

    public PhotoSubmissionRecyclerViewAdapter(List<String> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
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
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.photo_submission_adapter_reclerview, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) view.findViewById(R.id.text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTextView.setText("·" + mData.get(i));
        return view;
    }

    class ViewHolder {
        //要求
        TextView mTextView;
    }
}
