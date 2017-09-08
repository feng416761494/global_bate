package com.usamsl.global.okhttp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.usamsl.global.constants.Constants;

/**
 * Created by Administrator on 2017/1/22.
 * 监听网络
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Constants.connect = false;
        //获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
        if (NetworkInfo.State.CONNECTED == state) { // 判断是否正在使用WIFI网络
            Constants.connect = true;
        }
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
        if (NetworkInfo.State.CONNECTED == state) { // 判断是否正在使用GPRS网络
            Constants.connect = true;
        }
    }
}
