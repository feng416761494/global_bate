package com.usamsl.global.constants;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.usamsl.global.util.SharedPreferencesManager;

import okhttp3.MediaType;

/**
 * Created by Administrator on 2016/12/16.
 * 描述：请求码
 */
public class ConstantsCode {
    //身份证信息请求码
    public static final int ID_CARD_PHOTO = 1;
    //护照信息请求码
    public static final int PASSPORT_PHOTO = 2;
    //其他不需要识别的照片请求码
    public static final int OTHER_PHOTO = 3;
    //头像请求码
    public static final int PHOTO = 4;
    //出行时间请求码
    public static final int TRAVEL_TIME = 5;
    //选择城市请求码
    public static final int SELECT_CITY = 6;
    //导入照片请求码
    public static final int IMPORT_PHOTO = 7;
    //登录请求码
    public static final int LOGIN = 8;
    //户口本请求码
    public static final int ACCOUNT_BOOK = 9;
    //返回时间请求码
    public static final int RETURN_TIME = 10;
    //打开相机请求码
    public static final int OPEN_CAMERA = 11;
    //意见反馈打开相机
    public static final int FEED_BACK = 12;


}
