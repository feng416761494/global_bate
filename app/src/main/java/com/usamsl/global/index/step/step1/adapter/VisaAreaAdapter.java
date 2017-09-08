package com.usamsl.global.index.step.step1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step1.activity.CountryVisaDetailsActivity;
import com.usamsl.global.index.step.step1.entity.VisaAreaEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */
public class VisaAreaAdapter extends BaseAdapter{
    private Context context;
    private List<VisaAreaEntity> areaList;
    private LayoutInflater inflater;
    private int selectPosition = -1;
    public VisaAreaAdapter(Context context, List<VisaAreaEntity> areaList) {
        this.context = context;
        this.areaList = areaList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return areaList.size();
    }

    @Override
    public Object getItem(int position) {
        return areaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //用户选中了某一个item  传过来的position
    public void selectPosition(int postion){
        if(selectPosition != postion){
            this.selectPosition = postion;
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VisaAreaViewHolder holder = null;
        if(convertView == null){
            holder = new VisaAreaViewHolder();
            convertView = inflater.inflate(R.layout.visa_area_font_item,null);
            holder.imgAreaFont = (ImageView) convertView.findViewById(R.id.imgAreaFont);
            holder.relativeAreaFont = (RelativeLayout) convertView.findViewById(R.id.relativeAreaFont);
            convertView.setTag(holder);
        }else{
            holder = (VisaAreaViewHolder) convertView.getTag();
        }
        VisaAreaEntity areaEntity = areaList.get(position);
        if(selectPosition != -1 && selectPosition == position){
//            holder.imgAreaFont.setLayoutParams(new RelativeLayout.LayoutParams(100,50));
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.visa_area_select);
            holder.relativeAreaFont.startAnimation(animation);
            animation.setFillAfter(!animation.getFillAfter());
        }else{

        }
        holder.imgAreaFont.setImageDrawable(areaEntity.getAreaImage());

//        if(areaEntity.getVisaAreaCity().contains("北京")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.bjl));
//        }else if(areaEntity.getVisaAreaCity().contains("上海")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.shl));
//        }else if(areaEntity.getVisaAreaCity().contains("广州")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.gzl));
//        }else if(areaEntity.getVisaAreaCity().contains("成都")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.cdl));
//        }else if(areaEntity.getVisaAreaCity().contains("沈阳")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.syl));
//        }else if(areaEntity.getVisaAreaCity().contains("重庆")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.cqr));
//        }else if(areaEntity.getVisaAreaCity().contains("西部")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.xbr));
//        }else if(areaEntity.getVisaAreaCity().contains("青岛")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.qdr));
//        }else if(areaEntity.getVisaAreaCity().contains("武汉")){
//            holder.imgAreaFont.setImageDrawable(context.getResources().getDrawable(R.drawable.whr));
//        }
        return convertView;
    }

    class VisaAreaViewHolder{
        ImageView imgAreaFont;
        RelativeLayout relativeAreaFont;
    }
}
