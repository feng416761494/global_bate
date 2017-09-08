package com.usamsl.global.util.manager;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.usamsl.global.ava.setting.TtsSettings;
import com.usamsl.global.ava.util.ApkInstaller;
import com.usamsl.global.constants.Constants;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Administrator on 2017/1/26.
 * 语音管理类
 */
public class AvaManager {
    private static Context mContext;
    // 语音合成对象
    private static SpeechSynthesizer mTts;
    // 默认发音人
    private static String voicer = "xiaoyan";
    // 引擎类型
    private static String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语记安装助手类
    private static ApkInstaller mInstaller;
    private static SharedPreferences mSharedPreferences;

    public static void initTts(Context context, SpeechSynthesizer mTt,String test) {
        mContext = context;
        //讯飞语音APPID
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "= " + Constants.APP_ID);
        //默认在线
        mEngineType = SpeechConstant.TYPE_CLOUD;
        // 初始化合成对象
        mTts = mTt;
        mSharedPreferences = context.getSharedPreferences(TtsSettings.PREFER_NAME, context.MODE_PRIVATE);
        mInstaller = new ApkInstaller((Activity) context);
        avaStart(test);
    }

    /**
     * 开始合成
     */
    public static void avaStart(String test) {
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(mContext, "tts_play");
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(test, null);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                //未安装则跳转到提示安装页面
                mInstaller.install();
            }
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    private static void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

}
