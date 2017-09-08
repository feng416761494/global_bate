package com.usamsl.global.util;

import android.content.Context;

import com.usamsl.global.view.CustomProgressDialog;
import com.usamsl.global.view.CustomProgressDialog_takePhoto;

/**
 * Created by FengJingQi on 2017/4/18.
 * 加载Loading
 */

public class ProgressBarLoadUtils {

    private static Context context;
    private static CustomProgressDialog dialog;
    private static CustomProgressDialog_takePhoto dialogTake;

    public  ProgressBarLoadUtils(Context context2){
        this.context = context2;
    }

    //开始加载进度条
    public static void startProgressDialog(){
        if (dialog == null){
            dialog = new CustomProgressDialog(context);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    //停止加载
    public static void stopProgressDialog(){
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }


    //开始加载进度条
    public static void startProgressDialog_takePhoto(){
        if (dialogTake == null){
            dialogTake = new CustomProgressDialog_takePhoto(context);
        }
        dialogTake.setCanceledOnTouchOutside(false);
        dialogTake.show();
    }

    //停止加载
    public static void stopProgressDialog_takePhoto(){
        if (dialogTake != null){
            dialogTake.dismiss();
            dialogTake = null;
        }
    }
}
