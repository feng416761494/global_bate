package com.usamsl.global.index.step.step7.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.leqi.safelight.Safelight;
import com.leqi.safelight.SafelightCallback;
import com.leqi.safelight.bean.Backdrop;
import com.leqi.safelight.bean.EntranceParam;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step7.entity.PhotoSubmission;
import com.usamsl.global.index.step.step7.util.FileUtil;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2017/1/16
 * 描述：选择照片
 */
public class PhotoActivity extends AppCompatActivity {
    //照片路径
    private String photoUrl;
    //底片颜色
    private Backdrop backdrop;
    //证件照param
    private EntranceParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        initData();
    }

    private void initData() {
        initPhoto();
    }

    /*
     * 相机配置
     */
    private void initPhoto() {
        //白色底
        backdrop = new Backdrop(16777215, 16777215);
        param = new EntranceParam(Constants.KEY, Constants.SECRET, Constants.SPEC_KEY, backdrop);

        //利用证件照第三方来拍照
        //跳转SDK进行制作，传入activity，param，SafelightCallback
        Safelight.intentSDK(this, param, new SafelightCallback() {
            @Override
            public void onComplete(Bitmap bitmap, HashMap<String, Integer> score) {
                // 成功时的回调
                // bitmap 最终获得的照片
                // score 用户照片的各项分数
                photoUrl = FileUtil.saveImage(PhotoActivity.this, bitmap);
                //上传数据库照片以及信息
                Intent data = new Intent();
                Constants.setPhoto = photoUrl;
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                // 发生错误时的回调
                // errorMessage 错误信息
                ConstantsMethod.showTip(PhotoActivity.this, errorMessage);
                finish();
            }

            @Override
            public void onUserCancelled() {
                // 用户取消时的回调
                ConstantsMethod.showTip(PhotoActivity.this, "已取消");
                finish();
            }
        });
    }
}
