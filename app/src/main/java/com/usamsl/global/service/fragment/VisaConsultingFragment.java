package com.usamsl.global.service.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.my.activity.MyOrderActivity;
import com.usamsl.global.service.activity.ChatActivity;
import com.usamsl.global.service.activity.ChatActivity_old;
import com.usamsl.global.service.adapter.ServiceGridViewAdapter;
import com.usamsl.global.service.data.ServiceVisaCountryData;
import com.usamsl.global.service.entity.VisaConsultationCountry;
import com.usamsl.global.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：服务菜单中签证咨询部分
 * 时间：2016/12/14
 */
public class VisaConsultingFragment extends Fragment {

    //视图界面View
    private View v;
    //签证咨询模块：国家签证的显示
    private MyGridView gv;
    //签证咨询模块：国家签证的显示的adapter
    private ServiceGridViewAdapter adapter;
    //签证国家列表
    private List<VisaConsultationCountry> mData;

    public VisaConsultingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_visa_consulting, container, false);
            initView();
            initData();
            toListener();
        }
        return v;
    }

    private void toListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mData.get(i).getVisaCountry().equals("联系电话") ||
                        mData.get(i).getVisaCountry().equals("售后服务")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("是否拨打电话")
                            .setMessage(Constants.SERVICE_PHONE)
                            .setPositiveButton("拨打",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ConstantsMethod.callDirectly(getActivity());
                                }
                            });

                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    if (Constants.USER_LOGIN) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("visaCountry", mData.get(i).getVisaCountry());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /*
     *界面数据的初始化
     */
    private void initView() {
        gv = (MyGridView) v.findViewById(R.id.gv);
    }

    /**
     * 数据加载
     */
    private void initData() {
        mData = new ArrayList<>();
        mData.addAll(ServiceVisaCountryData.initData());
        adapter = new ServiceGridViewAdapter(getActivity(), mData);
        gv.setAdapter(adapter);
        //去掉GridView选中或点击时的阴影
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
}
