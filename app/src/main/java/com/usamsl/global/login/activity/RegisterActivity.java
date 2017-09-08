package com.usamsl.global.login.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.Verification;

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
 * 描述：注册界面
 */
public class RegisterActivity extends AppCompatActivity {
    //返回按钮
    private ImageView img_back,imgPSDVisiably;
    //倒计时时间：60s
    private int time = 60;
    private boolean stop = true;
    //获取验证码
    private TextView tv_getCode;
    //手机号
    private EditText et_account;
    //密码
    private EditText et_pwd;
    //验证码
    private EditText et_code;
    //注册按钮
    private Button btn_register;
    //手机是否已注册
    private int error_code = -1;
    //登录
    private TextView tv_login;
    //判断是否联网
    private boolean connection = false;
    //罩层
    private RelativeLayout rl_show;


    private boolean passWordVisiably = true;

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
        setContentView(R.layout.activity_register_new);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
        initData();
        toListener();
    }

    private void initData() {
        connectWork();
    }

    /**
     * 界面初始化
     */
    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_getCode = (TextView) findViewById(R.id.tv_getCode);
        et_account = (EditText) findViewById(R.id.et_account);
        et_code = (EditText) findViewById(R.id.et_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_register = (Button) findViewById(R.id.btn_register);
        rl_show = (RelativeLayout) findViewById(R.id.rl_show);
        imgPSDVisiably = (ImageView) findViewById(R.id.imgPSDVisiably);
    }

    /**
     * 监听
     */
    private void toListener() {
        rl_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectWork();
                if (!et_account.getText().toString().equals("")) {
                    if (!Verification.isMobileNO(et_account.getText().toString())) {
                        ConstantsMethod.showTip(RegisterActivity.this, "手机号错误！");
                    } else if (time == 60 && connection) {
                        switch (error_code) {
                            case 0:
                                dialog("该手机号已注册", R.drawable.wow);
                                break;
                            case 1:
                                if (et_pwd.getText().toString().length() < 6) {
                                    ConstantsMethod.showTip(RegisterActivity.this, "密码长度至少六位！");
                                } else {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // post请求上传
                                            String url = UrlSet.sendSMS;
                                            postCode(url, et_account.getText().toString());
                                        }
                                    }).start();
                                }
                                break;
                        }
                    }
                } else {
                    ConstantsMethod.showTip(RegisterActivity.this, "请输入手机号！");
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
                Message message = handler.obtainMessage(1);
                handler.sendMessage(message);
                if (connection && charSequence.toString().length() == 11) {
                    if (Verification.isMobileNO(charSequence.toString())) {
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectWork();
                if (et_pwd.getText().toString().length() >= 6 && Verification.isMobileNO(et_account.getText().toString())
                        && et_code.getText().toString().length() == 6) {
                    rl_show.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // post请求上传
                            String url = UrlSet.registerCust;
                            postRegister(url);
                        }
                    }).start();
                } else {
                    if (et_account.getText().toString().equals("")) {//^[a-zA-Z0-9]{6,15}$
                        ConstantsMethod.showTip(RegisterActivity.this, "请输入手机号");
                    } else {
                        if (!Verification.isMobileNO(et_account.getText().toString())) {
                            ConstantsMethod.showTip(RegisterActivity.this, "手机号错误");
                        } else if (!PatternUtils.passwordIsMatcher(et_pwd.getText().toString())) {
                            ConstantsMethod.showTip(RegisterActivity.this, "密码需为6~15位字母或数字");
                        }else if (et_code.getText().toString().length() < 6) {
                            ConstantsMethod.showTip(RegisterActivity.this, "验证码错误");
                        }
                    }
                }
            }
        });
//        tv_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });

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
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connection = true;
        } else {
            connection = false;
            ConstantsMethod.showTip(RegisterActivity.this, "无网络连接");
        }
    }

    /**
     * 弹出注册成功弹窗
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
                rl_show.setVisibility(View.GONE);
                finish();
            }
        });
    }

    /**
     * 与后台对接接口
     */
    private OkHttpClient client = OkHttpManager.myClient();

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
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
                            if (code.getError_code() == 1) {
                                ConstantsMethod.showTip(RegisterActivity.this, "请核实手机号！");
                            } else {
                                tv_getCode.setText("" + time);
                                tv_getCode.setEnabled(false);
                                stop = false;
                                Message message = handler.obtainMessage(1);
                                handler.sendMessageDelayed(message, 1000);
                            }
                        }
                    });

                }
            }
        });
    }

    //注册
    private void postRegister(String url) {
        RequestBody body = new FormBody.Builder()
                .add("phone", et_account.getText().toString())
                .add("code", et_code.getText().toString())
                .add("cust_password", et_pwd.getText().toString())
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
                                                 dialog("注册成功", R.drawable.yes);
                                                 Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                 startActivity(intent);
                                                 finish();
                                                 break;
                                             case 1:
                                                 rl_show.setVisibility(View.GONE);
                                                 ConstantsMethod.showTip(RegisterActivity.this, login.getReason());
                                                 break;
                                         }

                                     }
                                 });
                             }
                         }
                     }

        );
    }
}
