package com.usamsl.global.index.step.step5.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.iflytek.cloud.SpeechSynthesizer;
import com.lidroid.xutils.HttpUtils;
import com.usamsl.global.R;
import com.usamsl.global.ava.util.ThreadPoolManager;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.index.step.step5.util.VoiceUtils;
import com.usamsl.global.index.step.step6.activity.PaymentActivity;
import com.usamsl.global.index.step.step7.activity.PhotoSubmissionActivity;
import com.usamsl.global.index.step.step7.util.HttpUtil;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.manager.AvaManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import org.apache.commons.httpclient.HttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.usamsl.global.constants.Constants.TOKEN;

/**
 * 填表
 * 时间：2017/2/7
 */
public class DSActivity extends AppCompatActivity implements View.OnClickListener {
    private BridgeWebView webView;
    private ImageView img_back;
    private TextView tvCountry, tvVoice;
    private AddOrder addOrder;
    //语音播放助手
    private SpeechSynthesizer mTts;
    private String url;
    private String formUrl;
    //是否调用webview中isInputed方法(调用则改变=0）如果加载失败时按返回按钮则返回
    private int webStatus = -1;
    //接收翻译结果
    private StringBuilder voiceText = new StringBuilder();
    //当前pageId
    private String currentPageId = "";
    private static final int VOICE_FROM_CODE = 100;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_ds_new);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ActivityManager.getInstance().addActivity(this);
        setViews();
    }

    /**
     * 控件初始化
     */
    private void setViews() {
        //开启线程池，判断是否加载完成webview
        ThreadPoolManager.getInstance().getSchedExecutorService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(can);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        webView = (BridgeWebView) findViewById(R.id.web);
        img_back = (ImageView) findViewById(R.id.img_back);
        tvCountry = (TextView) findViewById(R.id.tv_country);
        tvVoice = (TextView) findViewById(R.id.tv);
        //webView设置初始设置
        webView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(data);
            }
        });

      //  webView.setWebViewClient(new MessageWebViewClient());
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

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        img_back.setOnClickListener(this);
        tvVoice.setOnClickListener(this);
        //如果当前pageId是空(也就是H5表单没有展开某一个子项)将语音输入改为不可点击
        if (currentPageId == null || currentPageId.equals("")) {
            tvVoice.setEnabled(false);
            tvVoice.setTextColor(getResources().getColor(R.color.Text_3));
        }
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        addOrder = getIntent().getParcelableExtra("addOrder");
        tvCountry.setText(Constants.COUNTRY + "申请表");
        formUrl = getIntent().getStringExtra("formUrl");
        // 语音初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(DSActivity.this, null);
        //加载H5签证表单
        if (formUrl == null || formUrl.equals("")) {
            formUrl = UrlSet.countrySurface + "?order_id=" + getIntent().getIntExtra("order_id", 1) + "&" + "token=" + Constants.TOKEN;
        } else {
            formUrl = formUrl + "?order_id=" + getIntent().getIntExtra("order_id", 1) + "&" + "token=" + Constants.TOKEN;
        }
//        webView.loadUrl("http://139.196.88.32:8080/GlobalVisa/application/japan/japan222.html");
        webView.loadUrl(formUrl);

        //发送数据给web指定接收，方法名称就是getUserToken.由JavaScript调用
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

            case R.id.tv://语音键
                //每次说话前将先前的清空
                voiceText.delete(0, voiceText.length());
                VoiceUtils voiceUtils = new VoiceUtils(this, voiceText, currentPageId, VOICE_FROM_CODE);
                voiceUtils.voiceDialog();
                break;
        }
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
//                webView.loadUrl(formUrl);
            }
        });
        //语音播放
        AvaManager.initTts(DSActivity.this, mTts, avaQuestion);
    }

    /**
     * 供JS调用获取token
     *
     * @param json
     */
    private void getUserTokenToJS(final String json) {
        webView.registerHandler("getUserToken", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(json);
            }
        });
    }


    /**
     * 供JS调用，关闭页面的方法
     */
    private void popBackToJS() {
        webView.registerHandler("popBack", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                DSActivity.this.finish();
            }
        });
    }


    /**
     * 表单成功提交之后，H5回调Android的方法改变状态
     */
    private void changeOrderStatusToJS() {
        webView.registerHandler("submitAction", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null && !data.equals("")) {
                    if (data.equals("2")) {
                        if (addOrder != null) {
                            //改变状态的方法
                            changeOrderStatus(addOrder.getCust_type());
                        }
                    }
                }
            }
        });
    }


    /**
     * 此方法用于接收H5返回的pageId
     */
    public void getPageIdInForm() {
        webView.registerHandler("changePageId", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                can = 2;
                if (ObjectIsNullUtils.TextIsNull(data)) {
                    currentPageId = data.toString();
                }
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
     * 改变状态的方法
     *
     * @param cust_type
     */
    private void changeOrderStatus(int cust_type) {

        switch (cust_type) {
            //必填（1：在职人员、2：自由职业者、3：学龄前儿童、4：学生、5：退休人员)
            case 1:
                Intent intent = new Intent(DSActivity.this, JobProveActivity.class);
                intent.putExtra("addOrder", addOrder);
                intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                startActivity(intent);
                break;
            case 3:
                Intent intent2 = new Intent(DSActivity.this, JobProve2Activity.class);
                intent2.putExtra("addOrder", addOrder);
                intent2.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                startActivity(intent2);
                break;

            case 4:
                Intent intent1 = new Intent(DSActivity.this, StuProveActivity.class);
                intent1.putExtra("addOrder", addOrder);
                intent1.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                startActivity(intent1);
                break;
            default:
                if (Constants.IS_PAY == 1) {
                    updateStatus("2");
                    intent = new Intent(DSActivity.this, PhotoSubmissionActivity.class);
                    intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                    intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                    startActivity(intent);
                } else {
                    updateStatus("1");
                    intent = new Intent(DSActivity.this, PaymentActivity.class);
                    intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                    intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                    startActivity(intent);
                }
        }
    }

    /**
     * 更改订单状态
     */
    private void updateStatus(final String order_status) {
        Constants.STATUS = Integer.parseInt(order_status);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateInfo;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_id", getIntent().getIntExtra("order_id", 1) + "")
                        .add("token", TOKEN)
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Constants.STATUS = Integer.parseInt(order_status);
                            }
                        });
                    }
                });
            }
        }).start();

    }

    /**
     * 调用js方法.判断内容是否发生改变
     */
    private void formIsSave() {
        //"isInputed" 必须和JScript的函数名一样
        webView.callHandler("isInputed", "data for web", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                //data是H5返回的数据，根据data判定弹窗显示与否        0:未修改    ,1:有修改
                webStatus = 0;
                String status = data.toString();
                if (status != null && status.equals("1")) {
                    can = 2;
                    final CustomIsFormSaveDialog dialog = new CustomIsFormSaveDialog(DSActivity.this,
                            "申请表的内容已经更新，是否提交最新内容？", "提示");
                    dialog.show();
                    dialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
                        @Override
                        public void doCancel() {
                            dialog.dismiss();
                            DSActivity.this.finish();
                        }

                        @Override
                        public void doConfirm() {
                            //调用JScript的保存方法(子项保存)
                            saveChangeInForm("3");
                            dialog.dismiss();
                        }
                    });
                } else {
                    DSActivity.this.finish();
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
     * ava 在没有回答的情况下，语音提示用户
     */
    public void avaSayNothing() {
        AvaManager.initTts(DSActivity.this, mTts, "ava没有听清，请您再说一遍");
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
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
        ActivityManager.getInstance().removeActivity(DSActivity.this);
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

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            ConstantsMethod.showTip(DSActivity.this, "加载失败");
            DSActivity.this.finish();
        }
    }
}
