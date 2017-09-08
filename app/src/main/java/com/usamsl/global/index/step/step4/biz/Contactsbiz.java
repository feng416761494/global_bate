package com.usamsl.global.index.step.step4.biz;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.activity.EvUsActivity;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step4.activity.ContactsActivity;
import com.usamsl.global.index.step.step4.entity.AllContacts;
import com.usamsl.global.index.step.step5.activity.DocumentScanningActivity;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by FengJingQi on 2017/6/21.代码重构
 */
public class Contactsbiz {
    private final OkHttpClient client;
    private Context context;
    private ContactsActivity activity;

    public Contactsbiz(Context context) {
        this.context = context;
        this.activity = (ContactsActivity) context;
        client = OkHttpManager.myClient();
    }

    /**
     * 用户获取所有联系人
     */
    public void getUserAllContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.selectContactAll + Constants.TOKEN);
                final Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        final String str = response.body().string();
                        Gson gson = new Gson();
                        AllContacts allContacts = gson.fromJson(str, AllContacts.class);
                        if(ObjectIsNullUtils.objectIsNull(allContacts)){
                            activity.updateContactsList(allContacts);
                        }
//
                    }
                });
            }
        }).start();
    }

    /**
     * 用户操作新增订单
     * @param addOrder      订单信息
     * @param contact_id    联系人Id
     * @param travelTime    出行时间
     * @param returnTime    返回时间
     */
    public void AddOrder(AddOrder addOrder, int contact_id, String travelTime, String returnTime) {
        String url = UrlSet.orderAdd;
        OkHttpClient client = OkHttpManager.myClient();
        RequestBody body = new FormBody.Builder()
                .add("visa_id", addOrder.getVisa_id() + "")
                .add("cust_type", addOrder.getCust_type() + "")
                .add("contact_id", contact_id + "")
                .add("depart_time", travelTime)
                .add("return_time", returnTime)
                .add("bank_outlets_id", addOrder.getBank_outlets_id() + "")
                .add("token", Constants.TOKEN)
                //2017/7/1 by FJQ   新增订单中需要给Web传递Visa_area_id
                .add("visa_area_id",addOrder.getVisa_area_id()+"")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
                         @Override
                         public void onFailure(Call call, IOException e) {
                             activity.runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     new ProgressBarLoadUtils(context).stopProgressDialog();
                                 }
                             });
                         }

                         @Override
                         public void onResponse(final Call call, Response response)
                                 throws IOException {
                             String str = response.body().string();
                             Gson gson = new Gson();
                             final ResultId result = gson.fromJson(str, ResultId.class);
                             if(ObjectIsNullUtils.objectIsNull(result)){
                                 activity.getAddOrderResult(result);
                             }
                         }
                     }

        );
    }

    /**
     * 添加联系人
     * @param name
     * @param phoneNum
     * @param email
     */
    public void addContacts(String name, String phoneNum, String email) {
        String url = UrlSet.addContact;
        OkHttpClient client = OkHttpManager.myClient();
        RequestBody body = new FormBody.Builder()
                .add("contact_name", name)
                .add("contact_phone", phoneNum)
                .add("e_mail", email)
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
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstantsMethod.showTip(context, "网络异常！");
                        new ProgressBarLoadUtils(context).stopProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response)
                    throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final ResultId result = gson.fromJson(str, ResultId.class);
                if(ObjectIsNullUtils.objectIsNull(result)){
                    activity.getAddContactResult(result);
                }
            }
        });
    }

    /**
     * 更新联系人
     * @param name
     * @param phoneNum
     * @param email
     * @param contactId
     */
    public void updateContacts(String name, String phoneNum, String email, int contactId) {
        String url = UrlSet.updateContact;
        OkHttpClient client = OkHttpManager.myClient();
        RequestBody body = new FormBody.Builder()
                .add("contact_name", name)
                .add("contact_phone", phoneNum)
                .add("e_mail", email)
                .add("contact_id", contactId + "")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
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
            public void onResponse(final Call call, Response response)
                    throws IOException {
                String str = response.body().string();
                if (str.substring(0, 1).equals("{")) {
                    Gson gson = new Gson();
                    final Result result = gson.fromJson(str, Result.class);
                    if(ObjectIsNullUtils.objectIsNull(result)){
                        activity.getUpdateContactsResult(result);
                    }
                }
            }
        });
    }

    /**
     * 用户更改出行时间
     * @param travelTime
     * @param returnTime
     * @param creatOrderId
     */
    public void updateOrderTimes(String travelTime, String returnTime, int creatOrderId) {
        String url = UrlSet.updateOrder;
        OkHttpClient client = OkHttpManager.myClient();
        RequestBody body = new FormBody.Builder()
                .add("depart_time", travelTime)
                .add("retuen_tiem", returnTime)
                .add("order_id", creatOrderId + "")
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
                try {
                    final String str = response.body().string();
                    final JSONObject json = new JSONObject(str);
                    activity.getUpdateOrderTimeResult(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
