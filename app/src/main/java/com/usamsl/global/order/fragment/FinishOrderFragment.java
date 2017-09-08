package com.usamsl.global.order.fragment;


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
import com.usamsl.global.my.entity.AllOrder;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.order.adapter.FinishOrderAdapter;
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
 * 描述：订单菜单栏：已完成订单
 */
public class FinishOrderFragment extends Fragment {

    //视图View
    private View v;
    //订单列表显示
    private ListView lv;
    //订单列表显示的adapter
    private FinishOrderAdapter finishOrderAdapter;
    //订单列表
    private List<AllOrder.ResultBean> mData;
    //判断是否联网
    private boolean connection = false;
    private Handler mHandler = new Handler();
    private ImageView img;

    public FinishOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_finish_order, container, false);
            initView();
            initData();
        }
        return v;
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
        finishOrderAdapter = new FinishOrderAdapter(getActivity(), mData);
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
        new ProgressBarLoadUtils(getActivity()).startProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.AppOrderFinish + Constants.TOKEN
                        + "&finish=1");
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(getActivity(), "网络异常！");
                                new ProgressBarLoadUtils(getActivity()).stopProgressDialog();
                            }
                        });
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
                                if(ObjectIsNullUtils.objectIsNull(allOrder)){
                                    switch (allOrder.getError_code()) {
                                        case 0:
                                            mData.addAll(allOrder.getResult());
                                            lv.setAdapter(finishOrderAdapter);
                                            img.setVisibility(View.GONE);
                                            break;
                                        case 1:
                                            img.setVisibility(View.VISIBLE);
                                            break;
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

}
