package com.usamsl.global.login.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.login.entity.Login;
import com.usamsl.global.login.entity.GetCode;
import com.usamsl.global.login.utils.PatternUtils;
import com.usamsl.global.main.MainActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.Verification;
import com.usamsl.global.util.manager.ActivityManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2016/12/16
 * 描述：密码找回界面
 */
public class PasswordRecoveryActivity extends AppCompatActivity {
    //TextView:注册
    private TextView tv_register;
    //返回图片
    private ImageView img_back,imgPSDVisiably;
    //倒计时时间：60s
    private int time = 60;
    private boolean stop = true;
    //手机是否已注册
    private int error_code = -1;
    //获取验证码
    private TextView tv_getCode;
    //手机号
    private EditText et_account;
    //密码
    private EditText et_pwd;
    //再次输入密码
    private EditText et_pwd1;
    //验证码
    private EditText et_code;
    //确定按钮
    private Button btn_ok;
    //判断网络连接
    private boolean connection = false;
    //是否退出
    private boolean finish = false;

    private static int FROM_DESK = 0;

    private boolean passWordVisiably = true;
    //罩层
    private RelativeLayout rl_show;
    private Handler handler = new ConstantsMethod.MyHandler() {

        public void handleMessage(Message msg) {
            // handle message
            switch (msg.what) {
                case 1:
                    if (time > 0) {
                        if (!stop) {
                            time--;
                            tv_getCode.setText("" + time);
                            tv_getCode.setEnabled(false);
                            Message message = handler.obtainMessage(1);
                            handler.sendMessageDelayed(message, 1000);      // send message
                        } else {
                            tv_getCode.setText("发送验证码");
                            tv_getCode.setEnabled(true);
                            time = 60;
                        }
                    } else if (time == 0) {
                        Message message = handler.obtainMessage(2);
                        handler.sendMessage(message);
                    }
                    break;
                case 2:
                    tv_getCode.setText("发送验证码");
                    tv_getCode.setEnabled(true);
                    time = 60;
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            FROM_DESK = bundle.getInt("fromDesk");
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_password_recovery_new);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
        toListener();
    }

    private void initData() {
        connectWork();
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connection = true;
        } else {
            connection = false;
            ConstantsMethod.showTip(PasswordRecoveryActivity.this, "无网络连接");
        }
    }

    /**
     * 界面数据初始化
     */
    private void initView() {
        tv_register = (TextView) findViewById(R.id.tv_register);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_getCode = (TextView) findViewById(R.id.tv_getCode);
        et_account = (EditText) findViewById(R.id.et_account);
        et_code = (EditText) findViewById(R.id.et_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
//        et_pwd1 = (EditText) findViewById(R.id.et_pwd1);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        rl_show = (RelativeLayout) findViewById(R.id.rl_show);
        imgPSDVisiably = (ImageView) findViewById(R.id.imgPSDVisiably);
    }

    /**
     * 监听事件
     */
    private void toListener() {
        rl_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //注册文本监听事件（跳转到注册界面）
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PasswordRecoveryActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //返回图片（返回前一个界面）
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_account.getText().toString().equals("")) {
                    if (!Verification.isMobileNO(et_account.getText().toString())) {
                        ConstantsMethod.showTip(PasswordRecoveryActivity.this, "手机号错误");
                    } else if (time == 60 && connection) {
                        switch (error_code) {
                            case 1:
                                finish = false;
                                dialog("该手机号没有注册", R.drawable.wow);
                                break;
                            case 0:
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // post请求上传
                                        String url = UrlSet.sendSMS;
                                        postCode(url, et_account.getText().toString());
                                    }
                                }).start();
                                break;
                        }
                    }
                } else {
                    ConstantsMethod.showTip(PasswordRecoveryActivity.this, "请输入手机号");
                }
            }
        });
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                connectWork();
                stop = true;
                Message message = handler.obtainMessage(2);
                handler.sendMessage(message);
                if (connection && Verification.isMobileNO(charSequence.toString())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // post请求上传
                            String url = UrlSet.validation_phone;
                            postAccount(url, et_account.getText().toString());
                        }
                    }).start();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_pwd.getText().toString().length() > 5 && Verification.isMobileNO(et_account.getText().toString())) {
                    connectWork();
                    if (connection) {
                        rl_show.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // post请求上传
                                String url = UrlSet.forgotPwd;
                                postOk(url);
                            }
                        }).start();
                    }
                } else {
                    if (et_account.getText().toString().equals("")) {
                        finish = false;
                        ConstantsMethod.showTip(PasswordRecoveryActivity.this, "请输入手机号");
                    } else {
                        if (!Verification.isMobileNO(et_account.getText().toString())) {
                            finish = false;
                            ConstantsMethod.showTip(PasswordRecoveryActivity.this, "手机号错误");
                        } else if (et_code.getText().toString().length() < 6) {
                            ConstantsMethod.showTip(PasswordRecoveryActivity.this, "验证码错误");
                        } else if (!PatternUtils.passwordIsMatcher(et_pwd.getText().toString())) {
                            finish = false;
                            ConstantsMethod.showTip(PasswordRecoveryActivity.this, "密码需为6~15位字母或数字");
                        }
//                        else if (!et_pwd1.getText().toString().equals(et_pwd.getText().toString())) {
//                            finish = false;
//                            ConstantsMethod.showTip(PasswordRecoveryActivity.this, "两次密码不统一");
//                        }
                    }
                }
            }
        });


        imgPSDVisiably.setOnClickListener(new View.OnClickListener() {
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
     * 与后台对接接口
     */
    private OkHttpClient client = OkHttpManager.myClient();

    //请求验证码
    private void postCode(String url, String account) {
        RequestBody body = new FormBody.Builder()
                .add("phone", account)
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
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if (str.substring(0, 1).equals("{")) {
                    Gson gson = new Gson();
                    final GetCode code = gson.fromJson(str, GetCode.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (code.getError_code()) {
                                case 1:
                                    ConstantsMethod.showTip(PasswordRecoveryActivity.this, "请核实手机号！");
                                    break;
                                case 0:
                                    stop = false;
                                    tv_getCode.setText("" + time);
                                    tv_getCode.setEnabled(false);
                                    Message message = handler.obtainMessage(1);
                                    handler.sendMessageDelayed(message, 1000);
                                    break;
                            }

                        }
                    });
                }
            }
        });
    }

    //修改密码
    private void postOk(String url) {
        RequestBody body = new FormBody.Builder()
                .add("phone", et_account.getText().toString())
                .add("cust_password", et_pwd.getText().toString())
                .add("code", et_code.getText().toString())
                .add("app_company_id",Constants.APP_COMPANY_ID)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ConstantsMethod.showTip(PasswordRecoveryActivity.this, "网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if (str.substring(0, 1).equals("{")) {
                    Gson gson = new Gson();
                    final Login login = gson.fromJson(str, Login.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (login.getError_code()) {
                                case 0:
                                    finish = true;
                                    dialog("修改成功", R.drawable.yes);
                                    Intent intent = new Intent(PasswordRecoveryActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    break;
                                default:
                                    rl_show.setVisibility(View.GONE);
                                    ConstantsMethod.showTip(PasswordRecoveryActivity.this, login.getReason());
                            }
                        }
                    });

                }
            }
        });
    }

    //验证手机号是否已被注册
    private void postAccount(String url, String account) {
        RequestBody body = new FormBody.Builder()
                .add("phone", account)
                .add("app_company_id",Constants.APP_COMPANY_ID)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ConstantsMethod.showTip(PasswordRecoveryActivity.this, "网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if (str.substring(0, 1).equals("{")) {
                    Gson gson = new Gson();
                    Login a = gson.fromJson(str, Login.class);
                    error_code = a.getError_code();
                }
            }
        });
    }

    /**
     * 弹出修改成功弹窗
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
        rl.setOnClickListener(new View.OnClickListener() {
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
                if (finish) {
                    rl_show.setVisibility(View.GONE);
                    finish();
                }
            }
        });
    }

    private static int USER_REMOTELOGIN_NUM = 100;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用户被异地登录
        if(Constants.USER_REMOTELOGIN){
            //App处于MainActivity，直接发送广播切
            if(MainActivity.isForeground){
                if(FROM_DESK == 200){

                }else{
                    Intent intent = new Intent(Constants.USER_REMOTELOGIN_CONTENT);
                    intent.putExtra("remotelogin",USER_REMOTELOGIN_NUM);
                    this.sendBroadcast(intent);
                }
            }else{
                //App没有处于MainActivity..用户什么都没有做，直接关闭了密码修改页面(跳转至MainActivity首页index)
                if(!Constants.USER_REMOTELOGIN_DOSOMING){
                    Intent intent = new Intent(this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("remotelogin",USER_REMOTELOGIN_NUM);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    this.startActivity(intent);
                    ActivityManager.getInstance().exit();
                }else{
//                    ActivityManager.getInstance().exit();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            goToUserCenter();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goToUserCenter(){
        if(Constants.USER_REMOTELOGIN) {
            //App处于MainActivity，直接发送广播切
            if (MainActivity.isForeground) {
                Intent intent = new Intent(Constants.USER_REMOTELOGIN_CONTENT);
                intent.putExtra("remotelogin", USER_REMOTELOGIN_NUM);
                this.sendBroadcast(intent);
            }
        }
    }
}
