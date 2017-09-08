package com.usamsl.global.my.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.my.adapter.MyOrderAdapter;
import com.usamsl.global.my.entity.AllOrder;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.view.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 时间：2016/12/28
 * 我的：我的订单
 */
public class MyOrderActivity extends AppCompatActivity {
    //返回按钮
    private ImageView img_back;
    //订单显示列表
    private MyListView lv;
    //订单数据
    private List<AllOrder.ResultBean> mData;
    //订单显示Adapter
    private MyOrderAdapter myOrderAdapter;
    //判断是否联网
    private boolean connection = false;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        toListener();
        initData();
    }

    /**
     * 监听
     */
    private void toListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img = (ImageView) findViewById(R.id.img);
    }

    /**
     * 数据加载
     */
    private void initData() {
        mData = new ArrayList<>();
        myOrderAdapter = new MyOrderAdapter(this, mData);
        connectWork();
        if (connection) {
            getData();
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        new ProgressBarLoadUtils(this).startProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.AppOrderAll + Constants.TOKEN);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(MyOrderActivity.this, "网络异常！");
                                new ProgressBarLoadUtils(MyOrderActivity.this).stopProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String str = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new ProgressBarLoadUtils(MyOrderActivity.this).stopProgressDialog();
                                Gson gson = new Gson();
                                AllOrder allOrder = gson.fromJson(str, AllOrder.class);
                                switch (allOrder.getError_code()) {
                                    case 0:
                                        mData.addAll(allOrder.getResult());
                                        lv.setAdapter(myOrderAdapter);
                                        img.setVisibility(View.GONE);
                                        break;
                                    case 1:
                                        img.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        });
                    }

                });
            }
        }).start();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        lv = (MyListView) findViewById(R.id.lv);
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connection = true;
        } else {
            connection = false;
            ConstantsMethod.showTip(MyOrderActivity.this, "无网络连接");
        }
    }
}
