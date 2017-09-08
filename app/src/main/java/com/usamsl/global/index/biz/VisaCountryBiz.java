package com.usamsl.global.index.biz;

import android.content.Context;

import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.activity.VisaCountryActivity;
import com.usamsl.global.index.entity.IndexVisa;
import com.usamsl.global.index.entity.VisaCountry;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/19.
 */
public class VisaCountryBiz {
    private Context context;
    private VisaCountryActivity activity;
    public VisaCountryBiz(Context context2){
        this.context = context2;
        activity = (VisaCountryActivity) context2;
    }

    public void getData() {

        String url = UrlSet.loadCountry;
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
                com.google.gson.Gson gson = new com.google.gson.Gson();
                final VisaCountry visaCountry = gson.fromJson(str, VisaCountry.class);
                if(ObjectIsNullUtils.objectIsNull(visaCountry)){
                    activity.updataListView(visaCountry);
                }
            }
        });
    }
}
