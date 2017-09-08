package com.usamsl.global.constants;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.usamsl.global.util.SharedPreferencesManager;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2016/12/16.
 * 描述：常量方法
 */
public class ConstantsMethod {
    /**
     * 点击EditText以外地方键盘消失
     */
    public static void cancelKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Toast输出
     */
    public static void showTip(Context context, final String str) {
        Toast mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 静态handler
     */
    public static class MyHandler extends Handler {

    }

    /**
     * 保存注销状态
     */
    public static void saveCancellationState(Context context) {
        Constants.USER_LOGIN = false;
        Constants.USER = "登录/注册";
        SharedPreferences.Editor editor = SharedPreferencesManager.startWrite(context, "msl");
        editor.putBoolean("login", false);
        editor.putString("user", "登录/注册");
        editor.commit();
        JPushInterface.setAliasAndTags(context, "", null, new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> set) {
            }
        });
    }

    /**
     * 拨打客服电话
     */
    public static void callDirectly(Context mContext) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.SERVICE_PHONE));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mContext.startActivity(intent);
    }

    /**
     * long 型的转成 date型的
     */
    public static Date longToDate(long currentTime) throws ParseException {
        Date date = new Date(currentTime);
        return date;
    }
}
