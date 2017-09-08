package com.usamsl.global.index.step.step4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.usamsl.global.R;
import java.util.List;

/**
 * Created by Administrator on 2016/12/24.
 * 描述：签证国家的签证类型选择后，签证之前的人员所需材料
 */
public class PeopleTypeMaterialDemandAdapter extends BaseAdapter  implements ListAdapter {
    private Context mContext;
    private List<String> mData;

    public PeopleTypeMaterialDemandAdapter(Context mContext, List<String> mData) {
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
        String demand = mData.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.people_material_demand, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_demand = (TextView) view.findViewById(R.id.tv_demand);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_demand.setText("·"+demand);
        return view;
    }

    class ViewHolder {
        //人员类型
        TextView tv_demand;
    }
}
