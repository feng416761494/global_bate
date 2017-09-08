package com.usamsl.global.okhttp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usamsl.global.index.adapter.VisaCountryAdapter;
import com.usamsl.global.index.entity.IndexShownCountry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/17.
 * 只建一次OkHttpClient
 */
public class OkHttpManager {
    private static OkHttpClient client;

    public static OkHttpClient myClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }
}
