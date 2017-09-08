package com.usamsl.global.index.step.step1.biz;

import android.content.Context;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.step1.activity.CountryVisaDetailsActivity;
import com.usamsl.global.index.step.step1.entity.District;
import com.usamsl.global.index.step.step1.entity.VisaAllEntity;
import com.usamsl.global.index.step.step1.entity.VisaDetails;
import com.usamsl.global.index.step.step1.util.adapters.ArrayWheelAdapter;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by FengJingQi on 2017/6/20. 代码重构
 */
public class CountryVisaDetailBiz {
    private Context context;
    private CountryVisaDetailsActivity activity;

    public CountryVisaDetailBiz(Context mContext){
        this.context = mContext;
        activity = (CountryVisaDetailsActivity) context;
    }

    public void loadVisaAll(int countryId,String locationCity){
        OkHttpClient client = OkHttpManager.myClient();
        RequestBody body = new FormBody.Builder()
                .add("country_id", countryId + "")
                .add("area_name", locationCity)
                .add("app_company_id", Constants.APP_COMPANY_ID)
                .build();
        Request request = new Request.Builder()
                .url(UrlSet.loadVisaAll)//            /GlobalVisa/app/visa/loadVisaAll
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
            public void onResponse(final Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                VisaAllEntity visaEntity = gson.fromJson(str,VisaAllEntity.class);
                if(ObjectIsNullUtils.objectIsNull(visaEntity)){
                    switch (visaEntity.getError_code()){
                        case 0:
                            activity.getAllVisaArea(visaEntity);
                            break;
                    }
                }
            }
        });
    }

}
