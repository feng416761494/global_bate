package com.usamsl.global.my.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.adapter.VisaCountryAdapter;
import com.usamsl.global.index.entity.IndexShownCountry;
import com.usamsl.global.login.entity.Login;
import com.usamsl.global.my.adapter.MyContactsGroupAdapter;
import com.usamsl.global.my.data.MyContactsData;
import com.usamsl.global.my.entity.MyGroup;
import com.usamsl.global.okhttp.OkHttpManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 时间：2016/12/29
 * 描述：我的联系人里面的移动分组
 */
public class GroupActivity extends AppCompatActivity {

    private ListView lv;
    private List<MyGroup.ResultBean> mData;
    private ImageView img_back;
    private MyContactsGroupAdapter adapter;
    private MyGroup.ResultBean group;
    //判断是否联网
    private boolean connection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        initView();
        initData();
        toListener();
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                group = mData.get(i);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("group", group);
        setResult(1, data);
        super.finish();
    }

    /**
     * 数据加载
     */
    private void initData() {
        mData = new ArrayList<>();
        connectWork();
        if (connection) {
            getGroup();
        }
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connection = true;
        } else {
            connection = false;
            ConstantsMethod.showTip(GroupActivity.this, "无网络连接");
        }
    }

    /**
     * get请求好友分组
     */
    OkHttpClient client = OkHttpManager.myClient();

    private void getGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.groupList + Constants.TOKEN);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(GroupActivity.this, "网络异常！");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    MyGroup g = gson.fromJson(str, MyGroup.class);
                                    if (g.getError_code() == 0) {
                                        mData.addAll(g.getResult());
                                        adapter = new MyContactsGroupAdapter(GroupActivity.this, mData);
                                        lv.setAdapter(adapter);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 界面初始化
     */
    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        img_back = (ImageView) findViewById(R.id.img_back);
    }
}
