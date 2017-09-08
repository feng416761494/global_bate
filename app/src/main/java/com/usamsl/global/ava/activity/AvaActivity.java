package com.usamsl.global.ava.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.usamsl.global.R;
import com.usamsl.global.ava.entity.AvaReceived;
import com.usamsl.global.ava.setting.TtsSettings;
import com.usamsl.global.ava.util.ApkInstaller;
import com.usamsl.global.ava.util.ThreadPoolManager;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.entity.IndexVisa;
import com.usamsl.global.index.entity.VisaCountry;
import com.usamsl.global.index.step.step1.activity.CountryVisaDetailsActivity;
import com.usamsl.global.main.MainActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.manager.ActivityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AvaActivity extends AppCompatActivity {
    //城市,国家
    private String country;
    //所有国家
    private List<VisaCountry.ResultBean> countries;
    //开始语音
    private ImageView img_mic;
    private static String TAG = AvaActivity.class.getSimpleName();
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
    //返回
    private ImageView img_back;
    //用来显示ava说的话
    private TextView tv_avaSpeak;
    //ava
    private ImageView img_ava;
    private int avaPlay = 1;
    private int avaPlay1 = 0;
    private int message = 1;
    private boolean flag = false;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ava);
        ActivityManager.getInstance().addActivity(AvaActivity.this);
        initView();
        initData();
        toListener();
    }

    /**
     * 数据加载
     */
    private void initData() {
        countries = new ArrayList<>();
        //讯飞语音APPID
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "= " + Constants.APP_ID);
        //默认在线
        mEngineType = SpeechConstant.TYPE_CLOUD;
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(AvaActivity.this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);
        mInstaller = new ApkInstaller(AvaActivity.this);
        reply = "您好，我是ava，有什么可以帮助您的吗？";
        playAva();
    }

    /**
     * 监听
     */
    private void toListener() {
        img_mic.setOnClickListener(new startListener());
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 播放语音
     */
    private void playAva() {
        if (Constants.connect) {
            if (reply.equals("null") || reply.equals("")) {
                reply = "对不起，查询不到你想要找的内容";
            }
            avaStart(reply);
            sb.delete(0, sb.length());
        } else {
            ConstantsMethod.showTip(AvaActivity.this, "无网络连接");
        }
    }

    /**
     * 界面初始化
     */
    private void initView() {
        img_ava = (ImageView) findViewById(R.id.img_ava);
        img_mic = (ImageView) findViewById(R.id.img_mic);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_avaSpeak = (TextView) findViewById(R.id.tv_avaSpeak);
        ThreadPoolManager.getInstance().getSchedExecutorService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        }, 0, 80, TimeUnit.MILLISECONDS);

    }

    //开始语音
    class startListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            btnVoice();
        }
    }

    private void sendVoice(String text) {
        connectWork(text);
    }

    /**
     * 判断是否联网
     */
    private void connectWork(final String text) {
        if (Constants.connect) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String json = "";
                    // post请求上传
                    String url = UrlSet.ask_ava;
                    if(Constants.USER_LOGIN){
                        json = "{\"input_text\":{\"text\": \"" + text + "\"},\"user_id\":\""+Constants.TOKEN+"\",\"app_id\":\""+Constants.TALK_TO_AVA_FROM+"\"}";
                    }else{
                        json = "{\"input_text\":{\"text\": \"" + text + "\"},\"user_id\":\""+Constants.USER_LOGIN_FOR_AVA+"\",\"app_id\":\""+Constants.TALK_TO_AVA_FROM+"\"}";
                    }
                    post(url, json);
                }
            }).start();
        } else {
            ConstantsMethod.showTip(AvaActivity.this, "无网络连接");
        }
    }

    //开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(AvaActivity.this, null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //dialog消失时的监听
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendVoice(sb.toString());
//                        AlertDialog.Builder builder = new AlertDialog.Builder(AvaActivity.this);
//                        builder.setMessage(sb.toString())
//                                .setCancelable(false)
//                                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                })
//                                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                }).show();
//                        AlertDialog alert = builder.create();
                    }
                });
            }
        });
        //dialog打开时的监听
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
    }

    private StringBuilder sb = new StringBuilder();

    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
        sb.append(text);
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    /**
     * 与后台对接接口
     */
    private OkHttpClient client = OkHttpManager.myClient();

    private void post(String url, String json) {
        RequestBody body = RequestBody.create(Constants.JSON, json);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                reply = null;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                if (str.toString().substring(0, 1).equals("{")) {
                    final AvaReceived result = gson.fromJson(str, AvaReceived.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.getOutput_text() != null) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(AvaActivity.this);
//                                builder.setMessage(result.getOutput_text().toString())
//                                        .setCancelable(false)
//                                        .setPositiveButton("关闭2", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        })
//                                        .setNegativeButton("关闭2", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        }).show();
//                                AlertDialog alert = builder.create();
                                if (result.getOutput_text().getControl() != null) {
                                    tv_avaSpeak.setText(result.getOutput_text().getText());
                                    if (!getIntent().getBooleanExtra("help", true)) {
                                        nameToId(result.getOutput_text().getControl().getValue());
                                    }else {
                                        reply = result.getOutput_text().getText() + "";
                                        playAva();
                                    }
                                } else {
                                    reply = result.getOutput_text().getText() + "";
                                    playAva();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 根据国家名字查找国家id
     */
    private int nameToId(final String name) {
        String url = UrlSet.loadCountry;
        OkHttpClient client = OkHttpManager.myClient();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstantsMethod.showTip(AvaActivity.this, "网络异常！");
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String str = response.body().string();
                if (str.substring(0, 1).equals("{")) {
                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    final VisaCountry visaCountry = gson.fromJson(str, VisaCountry.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (visaCountry.getError_code()) {
                                case 0:
                                    VisaCountry.ResultBean countrys = null;
                                    countries.addAll(visaCountry.getResult());
                                    for (VisaCountry.ResultBean bean : countries) {
                                        if (bean.getCountry_name().equals(name)) {
                                            country = bean.getCountry_id() + "";
                                            countrys = bean;
                                            break;
                                        }
                                    }
                                    Constants.COUNTRY = countrys.getCountry_name();
                                    Intent intent = new Intent(AvaActivity.this, CountryVisaDetailsActivity.class);
                                    intent.putExtra("city", getIntent().getStringExtra("city"));
                                    intent.putExtra("country", country);
                                    intent.putExtra("ava", "ava");
                                    intent.putExtra("countrys", countrys);
                                    startActivity(intent);
                                    finish();
                                    break;
                            }
                        }
                    });
                }
            }
        });
        return 0;
    }

    /**
     * 开始合成
     */
    public void avaStart(String test) {
        //设置下面那句话
        tv_avaSpeak.setText(test);
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(AvaActivity.this, "tts_play");
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(test, mTtsListener);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ThreadPoolManager.getInstance().getSchedExecutorService().shutdownNow();
        mTts.pauseSpeaking();
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
        avaPlay1 = 0;
    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(AvaActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(AvaActivity.this);
        mTts.pauseSpeaking();
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
        super.onPause();
    }
}
