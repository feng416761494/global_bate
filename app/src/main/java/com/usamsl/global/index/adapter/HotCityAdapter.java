package com.usamsl.global.index.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.entity.City;
import com.usamsl.global.index.entity.IndexVisa;

import java.util.List;

/**
 * Created by MSL Vacation Inc on 2016/12/20.
 * 描述：城市定位中的热门城市的GridView用
 */
public class HotCityAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    //显示的Country集合
    private List<City> mData;

    public HotCityAdapter(Context mContext, List<City> mData) {
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
        City city = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.visa_city_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.cityName = (TextView) view.findViewById(R.id.tv_cityName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.cityName.setText(city.getCityName());
        return view;
    }

    class ViewHolder {
        //热门城市的名称
        TextView cityName;
    }
}

