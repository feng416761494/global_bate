package com.usamsl.global.constants;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */
public class AppRunUtils {
    private Context context;
    String MY_PKG_NAME = "com.usamsl.global";

    public AppRunUtils(Context mContext){
        this.context = mContext;
    }

    /**
     * App处于前台显示
     * @return
     */
    public boolean isFrontAppRuning(){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * App是否还在运行
     * @return
     */
    public boolean isBgAppRuning(){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
                    && info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                return true;
            }
        }
        return false;
    }
}
