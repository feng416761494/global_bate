package com.usamsl.global.service.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.PermissionRequest;
import com.usamsl.global.service.entity.VisaConsultationCountry;

import java.util.List;

/**
 * Created by MSL Vacation Inc on 2016/12/15.
 * 描述：服务界面签证咨询模块的显示
 */
public class ServiceGridViewAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    //显示的签证国家集合
    private List<VisaConsultationCountry> mData;

    public ServiceGridViewAdapter(Context mContext, List<VisaConsultationCountry> mData) {
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
        final VisaConsultationCountry country = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_visa_consulting_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.visaCountry = (TextView) view.findViewById(R.id.tv_visaCountry);
            viewHolder.img = (ImageView) view.findViewById(R.id.img);
            viewHolder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.visaCountry.setText(country.getVisaCountry());
        viewHolder.img.setImageResource(country.getImg());
        return view;
    }

    class ViewHolder {
        //签证国家
        TextView visaCountry;
        //图标
        ImageView img;
        RelativeLayout rl;
    }

}

