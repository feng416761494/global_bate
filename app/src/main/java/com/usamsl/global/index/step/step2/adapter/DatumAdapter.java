package com.usamsl.global.index.step.step2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.index.step.step2.entity.DatumList;
import com.usamsl.global.index.step.step2.entity.VisaDatumEntity;
import com.usamsl.global.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */
public class DatumAdapter extends BaseAdapter{

    private Context context;
    private List<VisaDatumEntity.Result> list ;
    private LayoutInflater inflater;
    public DatumAdapter(Context context, List<VisaDatumEntity.Result> list2) {
        this.context = context;
        this.list = list2;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.get(0).getDatumList().size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(0).getDatumList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DatumViewHolder holder = null;
        if(convertView == null){
            holder = new DatumViewHolder();
            convertView = inflater.inflate(R.layout.datumadapter_item,null);
            holder.tvDatumName = (TextView) convertView.findViewById(R.id.tvDatumName);
            holder.lsvDatum = (MyListView) convertView.findViewById(R.id.lsvDatum);
            convertView.setTag(holder);
        }else{
            holder = (DatumViewHolder) convertView.getTag();
        }
        DatumList resultEntity = list.get(0).getDatumList().get(position);
        holder.tvDatumName.setText(resultEntity.getVisa_datum_name());
        holder.lsvDatum.setAdapter(new DatumExplainAdapter(getTypeMaterialDemand(resultEntity.getExplain_desc().split("#"))));
        return convertView;
    }


    class DatumViewHolder{
        TextView tvDatumName;
        MyListView lsvDatum;
    }

    public List<String> getTypeMaterialDemand(String[] typeMater) {
        List<String> list = new ArrayList<>();
        if (typeMater.length > 1) {
            for (String s : typeMater) {
                list.add(s);
            }
        } else if (typeMater.length == 1) {
            list.add(typeMater[0]);
        }
        return list;
    }

    class DatumExplainAdapter extends BaseAdapter{

        List<String> material;
        public DatumExplainAdapter(List<String> material){
            this.material = material;
        }
        @Override
        public int getCount() {
            return material.size();
        }

        @Override
        public Object getItem(int position) {
            return material.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DatumLsvViewHolder holder = null;
            if(convertView == null){
                holder = new DatumLsvViewHolder();
                convertView = inflater.inflate(R.layout.lsv_datum_explain_item,null);
                holder.tvDatumExplain = (TextView) convertView.findViewById(R.id.tvDatumExplain);
                convertView.setTag(holder);
            }else{
                holder = (DatumLsvViewHolder) convertView.getTag();
            }
            String datumExplain = material.get(position);
            holder.tvDatumExplain.setText(datumExplain);
            return convertView;
        }


    }

    class DatumLsvViewHolder{
        TextView tvDatumExplain;
    }

}
