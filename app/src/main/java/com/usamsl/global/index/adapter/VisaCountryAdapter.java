package com.usamsl.global.index.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.index.custom.RoundedImageView;
import com.usamsl.global.index.entity.IndexShownCountry;
import com.usamsl.global.view.RecyclerImageView;

import java.util.List;

/**
 * Created by MSL Vacation Inc on 2016/12/14.
 * 描述：首页下方显示国家中文、英文名称以及图片时的GridView用
 */
public class VisaCountryAdapter extends BaseAdapter {
    //上下文
    private Context mContext;
    //显示的Country集合
    private List<IndexShownCountry.ResultBean> result;

    public VisaCountryAdapter(Context mContext, List<IndexShownCountry.ResultBean> result) {
        this.mContext = mContext;
        this.result = result;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        IndexShownCountry.ResultBean bean = result.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.visa_country_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_chineseName = (TextView) view.findViewById(R.id.tv_chineseName);
            viewHolder.tv_englishName = (TextView) view.findViewById(R.id.tv_englishName);
            viewHolder.img = (RoundedImageView) view.findViewById(R.id.img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_chineseName.setText(bean.getCountry_name());
        viewHolder.tv_englishName.setText(bean.getCountry_en_name());
        Picasso.with(mContext)
                .load(bean.getLogo_url())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.fail)
                .error(R.drawable.fail)
                .into(viewHolder.img);

        return view;
    }

    class ViewHolder {
        //国家的中文名称
        TextView tv_chineseName;
        //英文名称
        TextView tv_englishName;
        //图片
        RoundedImageView img;
    }
}

