package com.usamsl.global.constants;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.usamsl.global.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;

/**
 * Created by Administrator on 2016/12/16.
 * 描述：常量类
 */
public class Constants {
    //区分百变蜥蜴和中信智能签的变量   百变2   中信3
    public static String APP_COMPANY_ID = "2";
    //登录状态
    public static boolean USER_LOGIN = false;
    //用户名
    public static String USER = "";
    //识别用户登录的taken
    public static String TOKEN = "";
    //json编码语言，防止乱码
    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //讯飞语音appId
    public static String APP_ID = "586dc433";
    //照片key
    public static String KEY = "d5e9b21ad4064641a530305efeaa61a5";
    //照片secret规格
    public static String SECRET = "e83fbf29f5e9478c8d2b23d8ac7607ba";
    //免费客服电话
    public static String SERVICE_PHONE = "400006518";
    //网络判断
    public static boolean connect = false;
    //出行时间
    public static Date travelDate;
    //图片文件类型
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    //照片规格
    public static String SPEC_KEY;
    //哪个国家
    public static String COUNTRY = "美国";
    //国家id
    public static int COUNTRY_ID;
    //是否支付,0:未支付，1：支付
    public static int IS_PAY = 0;
    //材料递交设置照片
    public static String setPhoto;
    //版本号
    public static String version = "1.0.1";
    //签证所需天数
    public static int plan_weekday;
    //户口本照片数
    public static List<Integer> show_order = new ArrayList<>();
    //是否出现删除按钮户口本
    public static boolean UPDATE;
    //订单状态
    public static int STATUS = 0;
    //用户上传完资料(户口本和一寸照片都可以使用该变量)
    public static String PHOTO_UPLOAD_SUCCESS = "PHOTO_SUBMISSION_UPLOAD_SUCCESS";
    //首页的Ava是否出现过
    public static boolean AVA_HAS_FIRST_LOAD = false;
    //用户异地登录，跳转至登录页面或者修改密码页面的值
    public static boolean USER_REMOTELOGIN = false;
    //异地登录的广播Action
    public static String USER_REMOTELOGIN_CONTENT = "USER_REMOTELOGIN_CONTENT";
    //异地登录的时候，登录页面和修改密码页面的判别码
    public static boolean USER_REMOTELOGIN_DOSOMING = false;
    //首页的更新提示，在打开App之后只出现一次
    public static boolean UPDATE_SHOW_ONLY_ONCE = false;
    //我的页面中 用户头像的Url
    public static String USER_PHOTOURL = "";
    //我的页面中 用户的职业
    public static String USER_CUSTTYPE = "";
    //分享网页
    public static String mslShareUrl = "http://usamsl.com/APP/index.html";
    //首页的banner图片地址//首页bannner图片对应的链接
    public static String bannerImageUrl_hotle = "";
    public static String bannerImageLink_hotle = "";
    public static String bannerImageUrl_fast = "";
    public static String bannerImageLink_fast = "";
    //用户没有登录的情况下，与ava对接时，传递的token用于区分用户唯一标识
    // 产生的手机IMEI码 + 当前毫秒数 作为唯一识别码
    public static String USER_LOGIN_FOR_AVA = "";
    //与AVA对接时，用于区分来自中信和百变蜥蜴....
    public static String TALK_TO_AVA_FROM = "global_visa";
    //当前所使用的第三方登录
    public static SHARE_MEDIA platform = null;
}
