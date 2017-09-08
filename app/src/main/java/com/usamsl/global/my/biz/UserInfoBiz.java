package com.usamsl.global.my.biz;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.step2.entity.VisaDatumEntity;
import com.usamsl.global.my.activity.UserInfoActivity;
import com.usamsl.global.my.entity.UserInfoEntity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/1.
 * 获取用户信息数据的业务类
 */
public class UserInfoBiz {

    private Context context;
    private UserInfoActivity activity;

    public UserInfoBiz(Context context) {
        this.activity = (UserInfoActivity) context;
    }

    public void getUserInfoData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.userInfo + Constants.TOKEN;
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(url);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                            final UserInfoEntity infoEntity = gson.fromJson(str, UserInfoEntity.class);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(ObjectIsNullUtils.objectIsNull(infoEntity)){
                                    if(infoEntity.getError_code() == 1){
                                        activity.setUserInfoData(infoEntity);
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
