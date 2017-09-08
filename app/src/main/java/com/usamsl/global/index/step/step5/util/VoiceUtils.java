package com.usamsl.global.index.step.step5.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.usamsl.global.ava.entity.AvaReceived;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.activity.EvUsActivity;
import com.usamsl.global.index.step.step5.activity.DSActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.manager.AvaManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.usamsl.global.constants.Constants.TOKEN;

/**
 * 语音弹窗的工具类
 * Created by FengJingQi on 2017/5/23.
 */

public class VoiceUtils {

    private Context context;
    private DSActivity activity;
    private EvUsActivity evActivity;
    private StringBuilder voiceText;
    private String currentPageId;
    private OkHttpClient client = OkHttpManager.myClient();
    private static int COUNTRY_EVUS = 0;

    public VoiceUtils(Context context2, StringBuilder voiceText2, String currentPageId2,int COUNTRY_EVUS2){
        this.context = context2;
        this.voiceText = voiceText2;
        this.currentPageId = currentPageId2;
        this.COUNTRY_EVUS = COUNTRY_EVUS2;
        if(COUNTRY_EVUS == 100){
            this.activity = (DSActivity) context2;
        }else{
            this.evActivity = (EvUsActivity) context2;
        }
    }

    public void voiceDialog() {
        RecognizerDialog dialog = new RecognizerDialog(context, null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                switch (COUNTRY_EVUS){
                    case 100:
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (voiceText.length() > 0) {
//                                    Toast.makeText(context, voiceText, Toast.LENGTH_LONG).show();
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                    builder.setMessage(voiceText)
//                                            .setCancelable(false)
//                                            .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                }
//                                            })
//                                            .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                }
//                                            }).show();
//                                    AlertDialog alert = builder.create();
                                    // post请求上传(将语音翻译后的字符串已json的格式向ava接口发送)
                                    String json = "{\"input_text\":{\"text\": \"" + voiceText.toString().replace(",","") +
                                            "\"},\"user_id\":\"" + TOKEN + "\",\"page_id\":\"" + currentPageId + "\",\"app_id\":\"" + Constants.TALK_TO_AVA_FROM + "\"}";
                                    post(UrlSet.ask_ava, json);
                                }
                            }
                        });
                        break;

                    case 200:
                        evActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (voiceText.length() > 0) {
//                                    Toast.makeText(context, voiceText, Toast.LENGTH_LONG).show();
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                    builder.setMessage(voiceText)
//                                            .setCancelable(false)
//                                            .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                }
//                                            })
//                                            .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                }
//                                            }).show();
//                                    AlertDialog alert = builder.create();
                                    // post请求上传(将语音翻译后的字符串已json的格式向ava接口发送)
                                    String json = "{\"input_text\":{\"text\": \"" + voiceText.toString().replace(",","") +
                                            "\"},\"user_id\":\"" + TOKEN + "\",\"page_id\":\"" + currentPageId + "\",\"app_id\":\"" + Constants.TALK_TO_AVA_FROM + "\"}";
                                    post(UrlSet.ask_ava, json);
                                }
                            }
                        });
                        break;
                }

            }
        });
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


    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
        voiceText.append(text);
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                Gson gson = new Gson();
                if (str.substring(0, 1).equals("{")) {
                    final AvaReceived result = gson.fromJson(str, AvaReceived.class);

                    switch (COUNTRY_EVUS){
                        case 100:
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.getOutput_text() != null) {
                                        if (result.getOutput_text().getControl() != null) {
                                            /**
                                             * output_text : {"control":{"input":"NAME","value":"杜贤贤"},"text":"您叫杜贤贤。您的生日？","video":null}
                                             *'input'H5页面上要对应填写的项，'value'指填写的内容
                                             */
                                            //H5页面上要对应填写的项
                                            String inputKey = result.getOutput_text().getControl().getInput();
                                            //填写的内容
                                            String inputValue = result.getOutput_text().getControl().getValue();
                                            //机器人提问
                                            String avaQuestion = result.getOutput_text().getText();
                                            activity.fillInH5Form(inputKey,inputValue,avaQuestion);
                                        }else{
                                            activity.avaSayNothing();
                                        }
                                    }else{
                                        activity.avaSayNothing();
                                    }
                                }
                            });
                            break;

                        case 200:
                            evActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.getOutput_text() != null) {
                                        if (result.getOutput_text().getControl() != null) {
                                            /**
                                             * output_text : {"control":{"input":"NAME","value":"杜贤贤"},"text":"您叫杜贤贤。您的生日？","video":null}
                                             *'input'H5页面上要对应填写的项，'value'指填写的内容
                                             */
                                            //H5页面上要对应填写的项
                                            String inputKey = result.getOutput_text().getControl().getInput();
                                            //填写的内容
                                            String inputValue = result.getOutput_text().getControl().getValue();
                                            //机器人提问
                                            String avaQuestion = result.getOutput_text().getText();
                                            evActivity.fillInH5Form(inputKey,inputValue,avaQuestion);
                                        }else{
                                            evActivity.avaSayNothing();
                                        }
                                    }else{
                                        evActivity.avaSayNothing();
                                    }
                                }
                            });
                            break;
                    }

                }
            }
        });
    }

}
