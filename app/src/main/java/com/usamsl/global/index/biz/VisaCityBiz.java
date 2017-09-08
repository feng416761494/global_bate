package com.usamsl.global.index.biz;

import android.content.Context;

import com.google.gson.Gson;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.activity.VisaCityActivity;
import com.usamsl.global.index.entity.VisaCity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/19.
 */
public class VisaCityBiz {
    private Context context;
    private VisaCityActivity activity;

    public VisaCityBiz(Context context2){
        this.context = context2;
        activity = (VisaCityActivity) context2;
    }


    public void getData() {
        // get请求上传
        String url = UrlSet.loadVisa;
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
                        ConstantsMethod.showTip(context, "网络异常！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if (str.substring(0, 1).equals("{")) {
                    Gson gson = new Gson();
                    final VisaCity visaCity = gson.fromJson(str, VisaCity.class);
                    if(ObjectIsNullUtils.objectIsNull(visaCity)){
                        activity.updateListView(visaCity);
                    }
                }
            }
        });
    }
}
