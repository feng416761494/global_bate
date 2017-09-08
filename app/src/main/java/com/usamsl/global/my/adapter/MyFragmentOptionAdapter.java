package com.usamsl.global.my.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.my.entity.UserOptionEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */
public class MyFragmentOptionAdapter extends BaseAdapter{
    private Context context;
    private List<UserOptionEntity> optionList;
    private LayoutInflater inflater;

    public MyFragmentOptionAdapter(Context mContext, List<UserOptionEntity> optionList) {
        this.context = mContext;
        this.optionList = optionList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return optionList.size();
    }

    @Override
    public Object getItem(int position) {
        return optionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OptionViewHolder holder = null;
        if(convertView == null){
            holder = new OptionViewHolder();
            convertView = inflater.inflate(R.layout.fragment_my_option_item,null);
            holder.tvOption = (TextView) convertView.findViewById(R.id.tvOption);
            holder.imgOption = (ImageView) convertView.findViewById(R.id.imgOPtion);
            convertView.setTag(holder);
        }else{
            holder = (OptionViewHolder) convertView.getTag();
        }
        UserOptionEntity entity = optionList.get(position);
        holder.tvOption.setText(entity.getTvOption());
        holder.imgOption.setImageDrawable(context.getResources().getDrawable(entity.getImgOption()));

        return convertView;
    }

    class OptionViewHolder{
        TextView tvOption;
        ImageView imgOption;
    }
}
