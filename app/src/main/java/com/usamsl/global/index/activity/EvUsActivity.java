package com.usamsl.global.index.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.usamsl.global.R;
import com.usamsl.global.ava.entity.AvaReceived;
import com.usamsl.global.ava.util.ThreadPoolManager;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.index.step.step5.util.VoiceUtils;
import com.usamsl.global.index.step.step6.activity.PaymentActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.order.adapter.UnFinishOrderAdapter;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.manager.AvaManager;
import com.usamsl.global.util.update.ConfirmDialog;
import com.usamsl.global.util.update.DownloadAppUtils;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 2017/2/13
 * 描述：evus类型签证
 */
public class EvUsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;
    private TextView tvVoice;
    private BridgeWebView webView;
    //语音播放助手
    private SpeechSynthesizer mTts;
    //订单详情页面中传递过来的表单的进度的url
    private String formUrl = "";
    private AddOrder addOrder;
    //接收翻译结果
    private StringBuilder voiceText = new StringBuilder();
    //当前pageId
    private String currentPageId = "";
    private static final int VOICE_FROM_CODE = 200;
    //是否可以返回
    private boolean canBack = true;
    private int can = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    canBack = true;
                    break;
                case 2:
                    canBack = false;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_us);
        ActivityManager.getInstance().addActivity(this);
        //控件初始化
        setViews();
        //数据初始化
        initData();
    }


    private void setViews() {
        //开启线程池，判断是否加载完成webview
        ThreadPoolManager.getInstance().getSchedExecutorService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(can);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvVoice = (TextView) findViewById(R.id.tv);
        webView = (BridgeWebView) findViewById(R.id.web);
        imgBack.setOnClickListener(this);
        tvVoice.setOnClickListener(this);
        //webView设置初始设置
        webView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(data);
            }
        });

        //webView.setWebViewClient(new MessageWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {


            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            }
        });

        //如果当前pageId是空(也就是H5表单没有展开某一个子项)将语音输入改为不可点击
        if (currentPageId == null || currentPageId.equals("")) {
            tvVoice.setEnabled(false);
            tvVoice.setTextColor(getResources().getColor(R.color.Text_3));
        }
    }

    private void initData() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(EvUsActivity.this, null);
        formUrl = getIntent().getStringExtra("formUrl");
        addOrder = getIntent().getParcelableExtra("addOrder");
        if (formUrl == null || formUrl.equals("")) {
            formUrl = UrlSet.goEvusOne + "?order_id=" + getIntent().getIntExtra("order_id", 1) + "&" + "token=" + Constants.TOKEN;
        } else {
            formUrl = UrlSet.goEvusOne + "?order_id=" + getIntent().getIntExtra("order_id", 1) + "&" + "token=" + Constants.TOKEN;
        }
        webView.loadUrl(formUrl);
        //发送数据给web指定接收，方法名称就是getUserToken.由JavaScript调用
        int order_id = getIntent().getIntExtra("order_id", 1);
        final String json = "{\"token\":\"" + Constants.TOKEN + "\",\"order_id\":\"" + getIntent().getIntExtra("order_id", 1) + "\"}";
        //客户端注册，js调用
        getUserTokenToJS(json);
        //当加载H5表单失败时或者点back键返回时提示的保存弹窗，点击弹框js会回调Android端的popback方法。关闭页面
        popBackToJS();
        //改方法是H5表单成功提交之后回调的Android的方法，去改变状态
        changeOrderStatusToJS();
        //获取H5pageId
        getPageIdInForm();
    }


    /**
     * ava 在没有回答的情况下，语音提示用户
     */
    public void avaSayNothing() {
        AvaManager.initTts(EvUsActivity.this, mTts, "ava没有听清，请您再说一遍");
    }

    /**
     * 此方法用于接收H5返回的pageId
     */
    public void getPageIdInForm() {
        webView.registerHandler("changePageId", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                can = 2;
                currentPageId = data.toString();
                if (ObjectIsNullUtils.TextIsNull(currentPageId) && !currentPageId.equals("0")) {
                    tvVoice.setEnabled(true);
                    tvVoice.setTextColor(getResources().getColor(R.color.Text_1));
                } else if (currentPageId.equals("0")) {
                    tvVoice.setEnabled(false);
                    tvVoice.setTextColor(getResources().getColor(R.color.Text_3));
                }
            }
        });
    }


    /**
     * 将语音接口返回的key   value、传给h5，进行填表
     *
     * @param inputKey
     * @param inputValue
     * @param avaQuestion
     */
    public void fillInH5Form(String inputKey, String inputValue, String avaQuestion) {
        String json = "{\"inputKey\":\"" + inputKey + "\",\"inputValue\":\"" + inputValue + "\"}";
        webView.callHandler("avaAnswer", json, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
            }
        });
        //语音播放
        AvaManager.initTts(EvUsActivity.this, mTts, avaQuestion);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_back:
                //返回键的时候调用js方法去判断当前用户表单是否又更改并且没有保存，如果有就弹出提示
                if (canBack) {
                    finish();
                } else {
                    formIsSave();
                }
                break;

            case R.id.tv:
                //每次说话前将先前的清空
                voiceText.delete(0, voiceText.length());
                VoiceUtils voiceUtils = new VoiceUtils(this, voiceText, currentPageId, VOICE_FROM_CODE);
                voiceUtils.voiceDialog();
                break;
        }
    }


    private void getUserTokenToJS(final String json) {
        webView.registerHandler("getUserToken", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(json);
            }
        });
    }

    private void popBackToJS() {
        webView.registerHandler("popBack", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                EvUsActivity.this.finish();
            }
        });
    }

    private void changeOrderStatusToJS() {
        webView.registerHandler("submitAction", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null && !data.equals("")) {
                    if (data.equals("2")) {
                        if (addOrder != null) {
                            //改变状态的方法
                            changeOrderStatus();
                        }
                    }
                }
            }
        });
    }


    /**
     * 根据JS返回信息，进行下一步事件
     */
    private void changeOrderStatus() {
        switch (Constants.IS_PAY) {
            case 1:
                updateStatus("2");
                ActivityManager.getInstance().exit();
                finish();
                break;
            default:
                updateStatus("1");
                Intent intent = new Intent(EvUsActivity.this, PaymentActivity.class);
                intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                intent.putExtra("evus", getIntent().getStringExtra("evus"));
                startActivity(intent);
                break;
        }
    }

    private void updateStatus(final String order_status) {
        Constants.STATUS = Integer.parseInt(order_status);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateOrder;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_status", order_status)
                        .add("order_id", getIntent().getIntExtra("order_id", 1) + "")
                        .add("token", Constants.TOKEN)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                    }
                });
            }
        }).start();
    }


    private void formIsSave() {
        //"isInputed" 必须和JScript的函数名一样
        webView.callHandler("isInputed", "data for web", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                //data是H5返回的数据，根据data判定弹窗显示与否        0:未修改    ,1:有修改
                String status = data.toString();
                if (status != null && status.equals("1")) {
                    final CustomIsFormSaveDialog dialog = new CustomIsFormSaveDialog(EvUsActivity.this,
                            "申请表的内容已经更新，是否提交最新内容？", "提示");
                    dialog.show();
                    dialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
                        @Override
                        public void doCancel() {
                            dialog.dismiss();
                            EvUsActivity.this.finish();
                        }

                        @Override
                        public void doConfirm() {
                            //调用JScript的保存方法(子项保存)
                            saveChangeInForm("3");
                            dialog.dismiss();
                        }
                    });
                } else {
                    EvUsActivity.this.finish();
                }
            }
        });
    }


    /**
     * JS的保存方法
     */
    private void saveChangeInForm(String status) {
        //当调用保存方法时，JS会回调popBack方法。关闭页面(1回调时不关闭，3回调时关闭)
        webView.callHandler("submitForm", status, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
            }
        });
    }


    /**
     * onResume生命周期中调用了JS的isInputed。传递参数保存表单
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.callHandler("isInputed", "", new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    String status = data.toString();
                    if (status != null && !status.equals("") && status.equals("1")) {
                        saveChangeInForm("1");
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //返回键的时候调用js方法去判断当前用户表单是否又更改并且没有保存，如果有就弹出提示
        if (canBack) {
            finish();
        } else {
            formIsSave();
        }
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(EvUsActivity.this);
        super.onDestroy();
    }

    //webview加载失败
    private class MessageWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            canBack = false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            ConstantsMethod.showTip(EvUsActivity.this, "加载失败");
            EvUsActivity.this.finish();
        }
    }
}
