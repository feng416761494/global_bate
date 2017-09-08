package com.usamsl.global.order.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.my.entity.AllOrder;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.order.adapter.UnFinishOrderAdapter;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 时间：2016/12/21
 * 描述：订单菜单栏：未完成订单
 */
public class UnFinishOrderFragment extends Fragment {

    //视图View
    private View v;
    //未完成订单列表
    private ListView lv;
    //未完成订单列表显示的adapter
    private UnFinishOrderAdapter unFinishOrderAdapter;
    //未完成订单列表数据
    private List<AllOrder.ResultBean> mData;
    //判断是否联网
    private boolean connection = false;
    private Handler mHandler = new Handler();
    private ImageView img;


    public UnFinishOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_unfinish_order, container, false);
            initView();
            initData();
            toListener();
        }
        return v;
    }

    private void toListener() {
        unFinishOrderAdapter.setCancelOrderListener(new UnFinishOrderAdapter.CancelOrderListener() {
            @Override
            public void cancelOrder(int position) {
                mData.remove(position);
                unFinishOrderAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 界面初始化
     */
    private void initView() {
        lv = (ListView) v.findViewById(R.id.lv);
        img = (ImageView) v.findViewById(R.id.img);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        mData = new ArrayList<>();
        unFinishOrderAdapter = new UnFinishOrderAdapter(getActivity(), mData);
    }

    @Override
    public void onResume() {
        super.onResume();
        connectWork();
        if (connection) {
            if (Constants.USER_LOGIN) {
                mData.clear();
                getData();
            }
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        //加载进度条
        new ProgressBarLoadUtils(getActivity()).startProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.AppOrderFinish + Constants.TOKEN
                        + "&finish=0");
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        new ProgressBarLoadUtils(getActivity()).stopProgressDialog();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String str = response.body().string();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                new ProgressBarLoadUtils(getActivity()).stopProgressDialog();
                                Gson gson = new Gson();
                                AllOrder allOrder = gson.fromJson(str, AllOrder.class);
                                if(allOrder.getResult() != null && allOrder.getResult().size() > 0){
                                    switch (allOrder.getError_code()) {
                                        case 0:
                                            mData.addAll(allOrder.getResult());
                                            lv.setAdapter(unFinishOrderAdapter);
                                            lv.setVisibility(View.VISIBLE);
                                            img.setVisibility(View.GONE);
                                            break;
                                        case 1:
                                            img.setVisibility(View.VISIBLE);
                                            break;
                                    }
                                }else{
                                    switch (allOrder.getError_code()) {
                                        case 1 :
                                            lv.setVisibility(View.GONE);
                                            img.setVisibility(View.VISIBLE);
                                            break;
                                        case 2:
                                            Constants.USER_LOGIN = false;
                                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                                            startActivity(intent);
                                            Intent intent2 = new Intent("USER_LOGIN_FALSE");
                                            intent2.putExtra("login",false);
                                            getActivity().sendBroadcast(intent2);
;                                            break;
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connection = true;
        } else {
            connection = false;
        }
    }
}
