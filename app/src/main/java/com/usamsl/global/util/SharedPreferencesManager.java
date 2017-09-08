package com.usamsl.global.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 时间：2016/12/16
 * 描述：SharedPreferences帮助类
 */

public class SharedPreferencesManager {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    /**
     * 写入本地
     * @param context
     * @param name
     * @return
     */
    public static SharedPreferences.Editor startWrite(Context context,String name){
        sharedPreferences=context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        return editor;
    }

    /**
     * 从本地读取
     * @param context
     * @param name
     * @return
     */
    public static SharedPreferences startRead(Context context,String name){
        sharedPreferences=context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
