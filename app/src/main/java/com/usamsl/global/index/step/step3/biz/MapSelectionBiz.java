package com.usamsl.global.index.step.step3.biz;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.google.gson.Gson;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.step3.activity.MapSelectionActivity;
import com.usamsl.global.index.step.step3.entity.Map;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by FengJingQi on 2017/6/20.代码重构
 */
public class MapSelectionBiz {
    private Context context;
    private MapSelectionActivity activity;
    public MapSelectionBiz(Context context){
        this.context = context;
        activity = (MapSelectionActivity) context;
    }


    public void doOffLine(Map.ResultBean map, List<Map.ResultBean> bean, int order_id) {
        String url = UrlSet.orderOffPayAdd;
        if(map == null){
            map = new Map.ResultBean();
            map.setBank_outlets_id(bean.get(2).getBank_outlets_id());
        }
        OkHttpClient client = OkHttpManager.myClient();
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id + "")
                .add("bank_outlets_id",map.getBank_outlets_id()+"")
                .add("token", Constants.TOKEN)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final Result r = gson.fromJson(str, Result.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (r.getError_code()) {
                            case 0:
                                ActivityManager.getInstance().exit();
                                Toast.makeText(context, "线下付款", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                        }
                    }
                });
            }
        });
    }


    /**
     *
     * @param order_id
     * @param map
     */
    public void saveOrderBankDetails(final int order_id, final Map.ResultBean map) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateOrder;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_id", order_id+"")
                        .add("bank_outlets_id",map.getBank_outlets_id()+"")
                        .add("token", Constants.TOKEN)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {

                    }
                });
            }
        }).start();
    }

    public void getBankList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.bankList);
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
                        Gson gson = new Gson();
                        final Map map = gson.fromJson(str, Map.class);
                        if(ObjectIsNullUtils.objectIsNull(map)){
                            activity.setBankListOnMap(map);
                        }
                    }
                });
            }
        }).start();
    }
}
