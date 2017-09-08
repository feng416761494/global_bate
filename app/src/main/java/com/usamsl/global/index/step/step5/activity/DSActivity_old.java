package com.usamsl.global.index.step.step5.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.index.step.step6.activity.PaymentActivity;
import com.usamsl.global.index.step.step7.activity.PhotoSubmissionActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.manager.AvaManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

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
public class DSActivity_old extends AppCompatActivity {
    private ImageView img_back;
    private WebView webView;
    private WebSettings mWebSettings;
    //语音输入
    private TextView tv;
    //接收翻译结果
    private StringBuilder sb = new StringBuilder();
    //是否有上一页
    private String msg = "msg";
    private String TAG = DSActivity_old.class.getSimpleName();
    //语音播放助手
    private SpeechSynthesizer mTts;
    //国家
    private TextView tv_country;
    //ava语音接口
    private String page_id;
    private AddOrder addOrder;
    //有订单详情页面传递过来的表单正在进行的页面
    private String formUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_ds);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ActivityManager.getInstance().addActivity(DSActivity_old.this);
        initView();
        initData();
        toListener();
    }

    private void initData() {
        addOrder=getIntent().getParcelableExtra("addOrder");
        formUrl = getIntent().getStringExtra("formUrl");
        tv_country.setText(Constants.COUNTRY + "申请表");
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(DSActivity_old.this, null);
        initWebView();
    }

    /**
     * webView初始化
     */
    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        // 设置支持JavaScript等
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setLightTouchEnabled(true);
        //后台调用自身方法，触发事件
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setSupportZoom(true);
        webView.setHapticFeedbackEnabled(false);
        /* 设置为使用webview推荐的窗口 */
        mWebSettings.setUseWideViewPort(true);
    /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
        mWebSettings.setLoadWithOverviewMode(true);
        //继续在此界面加载
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
            }
        });
        // mWebView.setInitialScale(0); // 改变这个值可以设定初始大小
        //重要,用于与页面交互!
        webView.addJavascriptInterface(DSActivity_old.this, "js");//此名称在页面中被调用,方法如下:
        String url = "";
//        if(formUrl != null && !formUrl.equals("")){
//            url = formUrl;
//        }else{
//        url = UrlSet.countrySurface + "?ORDERID=" + getIntent().getIntExtra("order_id", 1);
//        }
        url = "http://192.168.0.115:8080/GlobalVisa/application/japan/japan.html";
        webView.loadUrl(url);
    }

    private void toListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  finish();
//                webView.loadUrl("javascript:backStep()");
              final CustomIsFormSaveDialog dialog = new CustomIsFormSaveDialog(DSActivity_old.this,
                      "申请表的内容已经更新，是否提交最新内容？","提示");
                dialog.show();
                dialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doConfirm() {
                        Toast.makeText(DSActivity_old.this, "确定", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用js方法
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        sb.delete(0, sb.length());
                        btnVoice();
                    }
                });
            }
        });
    }


    /**
     * 当页面加载错误时，js调用Android方法。页面关闭
     */
    @JavascriptInterface
    public void loadFormError(){
        this.finish();
    }


    /**
     *  向JavaScript提供token和order_id的接口,由js调用
     * @return json格式
     */
    @JavascriptInterface
    public String getUserToken(){
        String json = "{\"token\":\"" + Constants.TOKEN + "\",\"order_id\":\"" + getIntent().getIntExtra("order_id", 1) + "\"}";
        return json;
    }


    //跳转界面
    @JavascriptInterface
    public void toNext() {
        switch (addOrder.getCust_type()) {
            //必填（1：在职人员、2：自由职业者、3：学龄前儿童、4：学生、5：退休人员)
            case 1:
                Intent intent = new Intent(DSActivity_old.this, JobProveActivity.class);
                intent.putExtra("addOrder", addOrder);
                intent.putExtra("order_id", getIntent().getIntExtra("order_id",1));
                startActivity(intent);
                break;
            case 3:
                Intent intent2 = new Intent(DSActivity_old.this, JobProve2Activity.class);
                intent2.putExtra("addOrder", addOrder);
                intent2.putExtra("order_id", getIntent().getIntExtra("order_id",1));
                startActivity(intent2);
                break;

            case 4:
                Intent intent1 = new Intent(DSActivity_old.this, StuProveActivity.class);
                intent1.putExtra("addOrder", addOrder);
                intent1.putExtra("order_id", getIntent().getIntExtra("order_id",1));
                startActivity(intent1);
                break;
            default:
                if (Constants.IS_PAY == 1) {
                    updateStatus("2");
                    intent = new Intent(DSActivity_old.this, PhotoSubmissionActivity.class);
                    intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                    intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                    startActivity(intent);
                } else {
                    updateStatus("1");
                    intent = new Intent(DSActivity_old.this, PaymentActivity.class);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateOrder;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_status", order_status)
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

                    }
                });
            }
        }).start();

    }

    //js返回信息，是否有上一页:"":表示为空
    @JavascriptInterface
    public void getValue(String msg) {
        this.msg = msg;
        if (!msg.equals("")) {
            webView.loadUrl(UrlSet.IP + msg);//msg
        } else {
            finish();
        }
    }

    @JavascriptInterface
    public void getPageId(String page_id) {
        this.page_id = page_id;
    }
    private void initView() {
        tv_country = (TextView) findViewById(R.id.tv_country);
        img_back = (ImageView) findViewById(R.id.img_back);
        webView = (WebView) findViewById(R.id.web);
        tv = (TextView) findViewById(R.id.tv);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            webView.loadUrl("javascript:backPage()");
//            if (!msg.equals("")) {
//                webView.loadUrl(UrlSet.IP + msg);//msg
//                return true;
//            } else {
//                finish();
//            }
            final String json = "{\"input_text\":{\"text\": \"" + "重填" +
                    "\"},\"user_id\":\"" + TOKEN + "\",\"page_id\":\"" + page_id + "\"}";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    newPost(UrlSet.ask_ava, json);
                }
            }).start();
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
        webView.loadUrl("javascript:backPage()");
        if (!msg.equals("")) {
            webView.loadUrl(UrlSet.IP + msg);
        } else {
//            super.onBackPressed();
        }
        final String json = "{\"input_text\":{\"text\": \"" + "重填" +
                "\"},\"user_id\":\"" + TOKEN + "\",\"page_id\":\"" + page_id + "\"}";
        new Thread(new Runnable() {
            @Override
            public void run() {
                newPost(UrlSet.ask_ava, json);
            }
        }).start();
    }

    @Override
    public void finish() {
        final String json = "{\"input_text\":{\"text\": \"" + "重填" +
                "\"},\"user_id\":\"" + TOKEN + "\",\"page_id\":\"" + page_id + "\"}";
        new Thread(new Runnable() {
            @Override
            public void run() {
                newPost(UrlSet.ask_ava, json);
            }
        }).start();
        super.finish();
    }

    //开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(DSActivity_old.this, null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sb.length() > 0) {
                            // post请求上传(将语音翻译后的字符串已json的格式向ava接口发送)
                            String json = "{\"input_text\":{\"text\": \"" + sb.toString() +
                                    "\"},\"user_id\":\"" + TOKEN + "\",\"page_id\":\"" + page_id + "\"}";
                            post(UrlSet.ask_ava, json);
                        }
                    }
                });
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
                Log.i("INFO","错误==="+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                Gson gson = new Gson();
                if (str.substring(0, 1).equals("{")) {
                    final AvaReceived result = gson.fromJson(str, AvaReceived.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.getOutput_text() != null) {
                                if (result.getOutput_text().getControl() != null) {

                                    //output_text : {"control":{"input":"NAME","value":"杜贤贤"},"text":"您叫杜贤贤。您的生日？","video":null}
                                    //'input'H5页面上要对应填写的项，'value'指填写的内容
                                    String str = "javascript:setVal('" + result.getOutput_text().getControl().getInput() + "','"
                                            + result.getOutput_text().getControl().getValue() + "')";
                                    webView.loadUrl(str);
                                    //语音播放
                                    AvaManager.initTts(DSActivity_old.this, mTts, result.getOutput_text().getText());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    //重填
    private void newPost(String url, String json) {
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

            }
        });
    }

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

    @Override
    protected void onDestroy() {
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
        ActivityManager.getInstance().removeActivity(DSActivity_old.this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(DSActivity_old.this);
        super.onPause();
        webView.resumeTimers();
    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(DSActivity_old.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
        webView.resumeTimers();
    }
}
