package com.usamsl.global.index.biz;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.adapter.VisaCountryAdapter;
import com.usamsl.global.index.entity.IndexBannerEntity;
import com.usamsl.global.index.entity.IndexShownCountry;
import com.usamsl.global.index.fragment.IndexFragment;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/19.
 */
public class IndexCountryListBiz {
    private Context context;
    private Fragment fragment;
    private IndexFragment indexFragment;
    private OkHttpClient client = OkHttpManager.myClient();
    public IndexCountryListBiz(Fragment fragment2) {
        this.fragment = fragment2;
        indexFragment = (IndexFragment) fragment;
    }

    public void getData() {
        Request.Builder requestBuilder = new Request.Builder().url(UrlSet.index_countryList + "app_company_id=" + Constants.APP_COMPANY_ID);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                indexFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstantsMethod.showTip(fragment.getActivity(), "网络异常！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                indexFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        IndexShownCountry country = gson.fromJson(str, IndexShownCountry.class);
                        if(ObjectIsNullUtils.objectIsNull(country)){
                            indexFragment.updateViews(country);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取首页banner图片，以及连接
     * @return
     */
    public void getBannerImageResouces() {
        Request.Builder requestBuilder = new Request.Builder().url(UrlSet.index_banner + "&app_company_id=" + Constants.APP_COMPANY_ID);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                indexFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstantsMethod.showTip(fragment.getActivity(), "网络异常！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                indexFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        IndexBannerEntity entity = gson.fromJson(str,IndexBannerEntity.class);
                        if(entity.getError_code() == 0){
                            indexFragment.updateIndexBanner(entity);
                        }
                    }
                });
            }
        });
    }
}
