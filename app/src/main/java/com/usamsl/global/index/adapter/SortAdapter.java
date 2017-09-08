package com.usamsl.global.index.adapter;

/**
 * Created by Administrator on 2016/12/19.
 * 描述：签证国家按首字母显示的adapter
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.entity.IndexVisa;

import java.util.List;

public class SortAdapter extends BaseAdapter{

    private List<IndexVisa> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<IndexVisa> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
        IndexVisa indexVisa = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_shown_visa_country_item, null);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(catalog)){
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(indexVisa.getFirstLetter().toUpperCase());
        }else{
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.name.setText(this.list.get(position).getName());

        return view;

    }

    final static class ViewHolder {
        TextView catalog;
        TextView name;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<IndexVisa> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
