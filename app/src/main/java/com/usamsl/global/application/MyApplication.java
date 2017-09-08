package com.usamsl.global.application;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.iflytek.cloud.SpeechUtility;
import com.leqi.safelight.Safelight;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.okhttp.MyBroadcastReceiver;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.SharedPreferencesManager;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/12/16.DDDDD
 *
 * 描述：重写的Application
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        //语音合成
        SpeechUtility.createUtility(MyApplication.this, "appid=" + getString(R.string.app_id));
        super.onCreate();
        //蒲公英集成catch异常
        PgyCrashManager.register(this);
        //友盟分享
        UMShareAPI.get(this);
        loginStatus();
        userStatus();
        loginEffective();
        //Client初始化
        OkHttpManager.myClient();
        //证件照
        Safelight.init(this);
        receiver();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

//        int builderId =1;SVN
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
//        builder.statusBarDrawable = R.drawable.ic_launcher;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
//        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;  // 设置为铃声与震动都要
//        JPushInterface.setPushNotificationBuilder(builderId , builder);

        // 指定定制的 Notification Layout
        CustomPushNotificationBuilder builder3 = new
                CustomPushNotificationBuilder(this,
                R.layout.view_notification,
                R.id.notifi_icon,
                R.id.notifi_title,
                R.id.notifi_message);
// 指定最顶层状态栏小图标
        builder3.statusBarDrawable = R.drawable.logo;
// 指定下拉状态栏时显示的通知图标
        builder3.layoutIconDrawable = R.drawable.logo;
        JPushInterface.setDefaultPushNotificationBuilder(builder3);
        PlatformConfig.setWeixin("wx624ee0202313e892","d530a71d445825cfdf67beaf302fe5cd");
        PlatformConfig.setQQZone("101418106", "52390c06e83a2aa4486a7646dedf2025");
        //此处最后一个参数，回调地址。必须要与新浪开放平台中配置的是一个地址
        PlatformConfig.setSinaWeibo("1939121652", "7c09bb6832ce4978655cbcdbb5370376", "http://sns.whalecloud.com/sina2/callback");
//        Config.DEBUG = true;

        //这里是用户在未登录的情况下与AVA进行交谈时，产生的手机IMEI码 + 当前毫秒数 作为唯一识别码
    }

    /**
     * 用户名
     */
    private void userStatus() {
        SharedPreferences sharedPreferences = SharedPreferencesManager.startRead(this, "msl");
        Constants.USER = sharedPreferences.getString("user", "登录/注册");
    }

    /**
     * 判断登录状态
     */
    private void loginStatus() {
        SharedPreferences sharedPreferences = SharedPreferencesManager.startRead(this, "msl");
        Constants.USER_LOGIN = sharedPreferences.getBoolean("login", false);
    }
    /**
     * 识别用户登录是否有效
     */
    private void loginEffective(){
        SharedPreferences sharedPreferences = SharedPreferencesManager.startRead(this, "msl");
        Constants.TOKEN = sharedPreferences.getString("token", "356a192b7913b04c54574d18c28d46e6395428ab");
    }

    /**
     * 网络连接
     */
    private void receiver(){
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);//注册
        receiver.onReceive(this, null);//接收
    }

}
