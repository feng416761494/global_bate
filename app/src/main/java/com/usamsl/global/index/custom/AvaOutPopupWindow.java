package com.usamsl.global.index.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.usamsl.global.R;
import com.usamsl.global.ava.setting.TtsSettings;
import com.usamsl.global.ava.util.ApkInstaller;
import com.usamsl.global.ava.util.ThreadPoolManager;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.index.step.step6.util.PayRadioGroup;
import com.usamsl.global.index.util.FadeInTextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/6/26.
 */
public class AvaOutPopupWindow extends PopupWindow implements View.OnClickListener,PopupWindow.OnDismissListener{
    public View menuView;
    private ImageView img_ava,imgClose;
    private FadeInTextView tvContent;
    private TextView tvConfirm;
    private Context context;
    private LinearLayout layoutGuiDe;

    private int avaPlay = 1;
    private int avaPlay1 = 0;
    private int message = 1;
    private boolean flag = false;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";
    // 引擎类型sss
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语记安装助手类
    ApkInstaller mInstaller;
    private SharedPreferences mSharedPreferences;
    //ava回复
    private String reply = "";
    private StringBuilder sb = new StringBuilder();
    private AvaGuideClickListener listener;
    private OnDismissListener dismissListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (avaPlay <= 23) {
                        avaOut(avaPlay);
                        avaPlay++;
                        if (avaPlay == 23) {
                            flag = true;
                            //开始进入界面时ava打招呼
//                            reply = "您好，我是ava，有什么可以帮助您的吗？";
//                            playAva();
                        }
                    }
                    if (avaPlay1 > 0 && flag) {
                        avaSpeak(avaPlay1 % 19);
                        avaPlay1++;
                        message = avaPlay1;
                    }
                    break;

            }
        }
    };

    @Override
    public void onDismiss() {
        mTts.pauseSpeaking();
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
        Intent intent = new Intent("AVA_POPUPWINDOW_CLOSE");
        intent.putExtra("ava_popupwindow_close",100);
        context.sendBroadcast(intent);
        avaPlay1 = 0;
    }



    public interface AvaGuideClickListener{
        public void close();
        public void confirm();
    }

    public void setAvaGuideClickListener(AvaGuideClickListener mListener){
        this.listener = mListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgClose:
                listener.close();
                break;
            case R.id.tvOk:
                listener.confirm();
                break;
        }
    }

    public AvaOutPopupWindow(Activity context, View contentView, int width, int height, boolean b) {
        // TODO Auto-generated constructor stub
        super(contentView, width, height, true);
//        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.va_guide_layout, null);
        img_ava = (ImageView) menuView.findViewById(R.id.imgAvaGuide);
        imgClose = (ImageView) menuView.findViewById(R.id.imgClose);
        tvConfirm = (TextView) menuView.findViewById(R.id.tvOk);
        layoutGuiDe = (LinearLayout) menuView.findViewById(R.id.relativeBackgroung);
        tvContent = (FadeInTextView) menuView.findViewById(R.id.tvInstruction);
        imgClose.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        this.setOnDismissListener(this);
        //设置MyPopuWindow的View
        this.setContentView(menuView);
        //设置弹窗的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置弹窗的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置弹窗客点击
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        //动画效果
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.ava_guide_anim);
//        layoutGuiDe.setAnimationStyle(R.style.popwin_anim_style);
        layoutGuiDe.setAnimation(animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable draw = new ColorDrawable(context.getResources().getColor(R.color.banTouMing));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(draw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        ThreadPoolManager.getInstance().getSchedExecutorService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        }, 1100, 75, TimeUnit.MILLISECONDS);
        //讯飞语音APPID
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "= " + Constants.APP_ID);
        //默认在线
        mEngineType = SpeechConstant.TYPE_CLOUD;
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        mSharedPreferences = context.getSharedPreferences(TtsSettings.PREFER_NAME, context.MODE_PRIVATE);
        mInstaller = new ApkInstaller(context);
        reply = tvContent.getText().toString();
        playAva();
    }



    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    /**
     * 播放语音
     */
    private void playAva() {
        if (Constants.connect) {
            if (reply.equals("null") || reply.equals("")) {
                reply = "对不起，查询不到你想要找的内容";
            }
            avaStart(reply);
        } else {
            ConstantsMethod.showTip(context, "无网络连接");
        }
    }


    /**
     * 开始合成
     */
    public void avaStart(String test) {
        //设置下面那句话
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(context, "tts_play");
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(test, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                //未安装则跳转到提示安装页面
                mInstaller.install();
            }
        }else{
            tvContent.setFadeInMaskColor(context.getResources().getColor(R.color.white));
            tvContent.setMoveUnitLength(7);
            tvContent.setFadeInDuration(300);
        }
    }



    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
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



    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            avaPlay1 = message;
        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }

        @Override
        public void onCompleted(SpeechError error) {
            avaPlay1 = 0;
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };



    private void avaOut(int i) {
        switch (i) {
            case 1:
                img_ava.setImageResource(R.drawable.ava_out1);
                break;
            case 2:
                img_ava.setImageResource(R.drawable.ava_out2);
                break;
            case 3:
                img_ava.setImageResource(R.drawable.ava_out3);
                break;
            case 4:
                img_ava.setImageResource(R.drawable.ava_out4);
                break;
            case 5:
                img_ava.setImageResource(R.drawable.ava_out5);
                break;
            case 6:
                img_ava.setImageResource(R.drawable.ava_out6);
                break;
            case 7:
                img_ava.setImageResource(R.drawable.ava_out7);
                break;
            case 8:
                img_ava.setImageResource(R.drawable.ava_out8);
                break;
            case 9:
                img_ava.setImageResource(R.drawable.ava_out9);
                break;
            case 10:
                img_ava.setImageResource(R.drawable.ava_out10);
                break;
            case 11:
                img_ava.setImageResource(R.drawable.ava_out11);
                break;
            case 12:
                img_ava.setImageResource(R.drawable.ava_out12);
                break;
            case 13:
                img_ava.setImageResource(R.drawable.ava_out13);
                break;
            case 14:
                img_ava.setImageResource(R.drawable.ava_out14);
                break;
            case 15:
                img_ava.setImageResource(R.drawable.ava_out15);
                break;
            case 16:
                img_ava.setImageResource(R.drawable.ava_out16);
                break;
            case 17:
                img_ava.setImageResource(R.drawable.ava_out17);
                break;
            case 18:
                img_ava.setImageResource(R.drawable.ava_out18);
                break;
            case 19:
                img_ava.setImageResource(R.drawable.ava_out19);
                break;
            case 20:
                img_ava.setImageResource(R.drawable.ava_out20);
                break;
            case 21:
                img_ava.setImageResource(R.drawable.ava_out21);
                break;
            case 22:
                img_ava.setImageResource(R.drawable.ava_out22);
                break;
            case 23:
                img_ava.setImageResource(R.drawable.ava_out23);
                break;
        }
    }



    private void avaSpeak(int i) {
        switch (i) {
            case 1:
                img_ava.setImageResource(R.drawable.ava_speak1);
                break;
            case 2:
                img_ava.setImageResource(R.drawable.ava_speak2);
                break;
            case 3:
                img_ava.setImageResource(R.drawable.ava_speak3);
                break;
            case 4:
                img_ava.setImageResource(R.drawable.ava_speak4);
                break;
            case 5:
                img_ava.setImageResource(R.drawable.ava_speak5);
                break;
            case 6:
                img_ava.setImageResource(R.drawable.ava_speak6);
                break;
            case 7:
                img_ava.setImageResource(R.drawable.ava_speak7);
                break;
            case 8:
                img_ava.setImageResource(R.drawable.ava_speak8);
                break;
            case 9:
                img_ava.setImageResource(R.drawable.ava_speak9);
                break;
            case 10:
                img_ava.setImageResource(R.drawable.ava_speak10);
                break;
            case 11:
                img_ava.setImageResource(R.drawable.ava_speak9);
                break;
            case 12:
                img_ava.setImageResource(R.drawable.ava_speak8);
                break;
            case 13:
                img_ava.setImageResource(R.drawable.ava_speak7);
                break;
            case 14:
                img_ava.setImageResource(R.drawable.ava_speak6);
                break;
            case 15:
                img_ava.setImageResource(R.drawable.ava_speak5);
                break;
            case 16:
                img_ava.setImageResource(R.drawable.ava_speak4);
                break;
            case 17:
                img_ava.setImageResource(R.drawable.ava_speak3);
                break;
            case 18:
                img_ava.setImageResource(R.drawable.ava_speak2);
                break;
            case 19:
                img_ava.setImageResource(R.drawable.ava_speak1);
                break;
        }
    }

}