package com.usamsl.global.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.auth.AlipaySDK;
import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.login.entity.User;
import com.usamsl.global.main.MainActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.SharedPreferencesManager;
import com.usamsl.global.util.Verification;
import com.usamsl.global.util.manager.ActivityManager;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 描述：登录界面
 * 时间：2016/12/15
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener{
    //TextView:注册
    private TextView tv_register;
    //TextView:忘记密码
    private TextView tv_forget_pwd;
    //返回图片,密码显示，第三方登录 微信 QQ 支付宝
    private ImageView img_back,imgPSDVisiably,imgWeiChart,imgQQ,imgAlipay;
    //手机号
    private EditText et_tel;
    //密码
    private EditText et_pwd;
    //登录按钮
    private Button btn_login;
    private RelativeLayout rl;
    //罩层
    private RelativeLayout rl_show;
    private boolean passWordVisiably = true;
    private UMShareAPI umShareAPI = null;
    private SHARE_MEDIA platform = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            FROM_DESK = bundle.getInt("fromDesk");
        }
        setContentView(R.layout.activity_login_new);
        umShareAPI = UMShareAPI.get(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        toListener();
    }

    /**
     * 界面数据初始化
     */
    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.rl);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
        img_back = (ImageView) findViewById(R.id.img_back);
        imgWeiChart = (ImageView) findViewById(R.id.imgWeiChat);
        imgQQ = (ImageView) findViewById(R.id.imgQQ);
        imgAlipay = (ImageView) findViewById(R.id.imgALiPay);
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        rl_show = (RelativeLayout) findViewById(R.id.rl_show);
        imgPSDVisiably = (ImageView) findViewById(R.id.imgPSDVisiably);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        imgWeiChart.setOnClickListener(this);
        imgQQ.setOnClickListener(this);
        imgAlipay.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //第三方登录
            case R.id.imgWeiChat:
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                break;

            case R.id.imgQQ:
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
                break;

            case R.id.imgALiPay:
                String url = "";
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, authListener);
                umShareAPI.deleteOauth(LoginActivity.this, SHARE_MEDIA.SINA, authListener);
                break;
        }
    }


    UMAuthListener authListener = new UMAuthListener() {

        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Toast.makeText(LoginActivity.this, "开始", Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, final Map<String, String> data) {
            if(data != null){
                Log.i("FENG","uid===="+data.get("uid"));
                Log.i("FENG","name===="+data.get("name"));
                Log.i("FENG","gender===="+data.get("gender"));
                Log.i("FENG","iconurl===="+data.get("iconurl"));
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        loginByThird(data.get("uid"),data.get("name"),data.get("gender"),data.get("iconurl"));
                    }
                }.start();
                //记录使用的是哪一个第三方登录的
                Constants.platform = platform;
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            data.getExtras();
        }
    }

    /**
     * 监听事件
     */
    private void toListener() {
        rl_show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //注册文本监听事件（跳转到注册界面）
        tv_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //忘记密码文本监听事件（跳转到找回密码界面）
        tv_forget_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
                startActivity(intent);
            }
        });
        //返回图片（返回前一个界面）
        img_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //登录按钮
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Verification.isMobileNO(et_tel.getText().toString()) && !et_pwd.getText().toString().equals("")) {
                    rl_show.setVisibility(View.VISIBLE);
                    connectWork();
                } else {
                    if (et_tel.getText().toString().equals("")) {
                        ConstantsMethod.showTip(LoginActivity.this, "请输入手机号");
                    } else {
                        if (!Verification.isMobileNO(et_tel.getText().toString())) {
                            ConstantsMethod.showTip(LoginActivity.this, "请核对手机号");
                        } else {
                            if (et_pwd.getText().toString().equals("")) {
                                ConstantsMethod.showTip(LoginActivity.this, "密码不能为空");
                            }
                        }
                    }
                }
            }
        });
        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantsMethod.cancelKeyboard(LoginActivity.this, view);
            }
        });

        imgPSDVisiably.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passWordVisiably){
                    et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imgPSDVisiably.setImageDrawable(getResources().getDrawable(R.drawable.open));
                    passWordVisiably = !passWordVisiably;
                }else{
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imgPSDVisiably.setImageDrawable(getResources().getDrawable(R.drawable.close));
                    passWordVisiably = !passWordVisiably;
                }
                et_pwd.setSelection(et_pwd.getText().toString().length());
            }
        });
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            initConnectData();
        } else {
            ConstantsMethod.showTip(LoginActivity.this, "无网络连接");
        }
    }

    /**
     * 联网之后数据加载
     */
    private void initConnectData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // post请求上传
                String url = UrlSet.login + "cust_phone=" + et_tel.getText().toString() +
                        "&cust_password=" + et_pwd.getText().toString() + "&remark="+1 + "&app_company_id="+Constants.APP_COMPANY_ID;
                post(url);
            }
        }).start();
    }

    OkHttpClient client = OkHttpManager.myClient();

    private void post(final String url) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ConstantsMethod.showTip(LoginActivity.this, "网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final User user = gson.fromJson(str, User.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (user.getError_code()) {
                            case 0:
                                //保存登录状态
                                Constants.USER_LOGIN = true;
                                Constants.USER_REMOTELOGIN = false;
                                Constants.TOKEN = user.getResult().getToken();
                                Constants.USER = et_tel.getText().toString();
                                SharedPreferences.Editor editor = SharedPreferencesManager.startWrite(LoginActivity.this, "msl");
                                editor.putBoolean("login", true);
                                editor.putString("user", et_tel.getText().toString());
                                editor.putString("token", user.getResult().getToken());
                                editor.commit();
                                dialog("登录成功", R.drawable.yes);

                                JPushInterface.setAliasAndTags(LoginActivity.this, user.getResult().getToken(), null, new TagAliasCallback() {
                                    @Override
                                    public void gotResult(int responseCode, String alias, Set<String> set) {

                                        switch (responseCode){
                                            case 0:
//                                                Toast.makeText(LoginActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                                                break;
                                            case 6002:
//                                                Toast.makeText(LoginActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                });
                                break;
                            default:
                                dialog("手机号或密码错误", R.drawable.no);
                        }
                    }
                });
            }
        });
    }


    private void loginByThird(final String thirdUid, String thirdName, String thirdGender, String thirdPhotoUrl) {
        String url = UrlSet.loginByThird;
        RequestBody body = new FormBody.Builder()
                .add("cust_phone", thirdUid)
                .add("cust_password","")
                .add("cust_name",thirdName)
                .add("third_photoUrl",thirdPhotoUrl)
                .add("remark","1")
                .add("app_company_id",Constants.APP_COMPANY_ID)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "错误日志="+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final User user = gson.fromJson(str, User.class);
                switch (user.getError_code()){
                    case 0:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //保存登录状态
                                Constants.USER_LOGIN = true;
                                Constants.USER_REMOTELOGIN = false;
                                Constants.TOKEN = user.getResult().getToken();
                                Constants.USER = user.getResult().getCust_name();
                                SharedPreferences.Editor editor = SharedPreferencesManager.startWrite(LoginActivity.this, "msl");
                                editor.putBoolean("login", true);
                                editor.putString("user", thirdUid);
                                editor.putString("token", user.getResult().getToken());
                                editor.commit();
                                dialog("登录成功", R.drawable.yes);
                            }
                        });
                        break;

                    case 1:

                        break;
                }
            }
        });
    }


    /**
     * 弹出登录成功弹窗
     */
    private void dialog(String text, int img) {
        final PopupWindow pop;
        View v = getLayoutInflater().inflate(R.layout.dialog, null);
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
        TextView tv = (TextView) v.findViewById(R.id.tv);
        ImageView imgView = (ImageView) v.findViewById(R.id.img);
        tv.setText(text);
        imgView.setImageResource(img);
        pop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pop.showAtLocation(v, Gravity.CENTER, 0, 0);
        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        //如果不点击，过几秒消失
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pop.dismiss();
            }
        }, 1500);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_show.setVisibility(View.GONE);
                setResult(Activity.RESULT_OK);
                if (Constants.USER_LOGIN) {
                    finish();
                }
            }
        });
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        setResult(Activity.RESULT_OK, data);
        super.finish();
    }
    private static int USER_REMOTELOGIN_NUM = 100;
    private static int FROM_DESK = 0;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Constants.USER_REMOTELOGIN){
            if(MainActivity.isForeground){
                if(FROM_DESK == 200){
                //桌面拉起的App  暂时不做任何事件
                }else{
                    Intent intent = new Intent(Constants.USER_REMOTELOGIN_CONTENT);
                    intent.putExtra("remotelogin",USER_REMOTELOGIN_NUM);
                    this.sendBroadcast(intent);
                }

            }else{
                //用户什么都没有做，直接关闭了登录页面(跳转至MainActivity首页index)
                if(!Constants.USER_REMOTELOGIN_DOSOMING){
                    Intent intent = new Intent(this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("remotelogin",USER_REMOTELOGIN_NUM);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    this.startActivity(intent);
                    ActivityManager.getInstance().exit();
                }else{
                    ActivityManager.getInstance().exit();
                }
            }
        }
    }

}
