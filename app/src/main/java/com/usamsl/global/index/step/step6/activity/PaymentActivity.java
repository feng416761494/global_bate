package com.usamsl.global.index.step.step6.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.auth.AlipaySDK;
import com.google.gson.Gson;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.step2.activity.BeforeVisaActivity;
import com.usamsl.global.index.step.step3.activity.MapSelectionActivity;
import com.usamsl.global.index.step.step6.entity.AliPayResult;
import com.usamsl.global.index.step.step6.entity.GetPay;
import com.usamsl.global.index.step.step6.entity.PayOrder;
import com.usamsl.global.index.step.step6.util.PayRadioGroup;
import com.usamsl.global.index.step.step6.util.PayRadioPurified;
import com.usamsl.global.index.step.step6.entity.PayResult;
import com.usamsl.global.index.step.step7.activity.PhotoSubmissionActivity;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2017/1/4
 * 描述：支付界面（支持微信和支付宝）
 */
public class PaymentActivity extends AppCompatActivity {
    //线下支付显示
    private TextView tv_map;
    private TextView tv_onLine;
    private TextView tv_address;
    //支付分组
    private PayRadioGroup group;
    //选择的支付方法
    private PayRadioPurified rp;
    //确认支付按钮
    private TextView tv_commit;
    //类型
    private TextView tv_type;
    //手机号
    private TextView tv_tel;
    //姓名
    private TextView tv_name;
    //金额
    private TextView tv_salary;
    private double sum = 0;
    //陪签金额
    private TextView tv_accompany_price;
    //选择支付方式的id
    private int radioButtonId;
    private ImageView img_back;
    //订单id
    private int order_id;
    //是否联网
    private boolean connect = false;
    //订单
    private PayOrder.ResultBean bean;
    //陪签一行
    private RelativeLayout rl_bodyTop;
    //陪签是否选中的checkBox
    private CheckBox cb;
    //是否陪签
    private int accompany = 0;
    //线下支付
    private RelativeLayout rl_bodyMiddle;
    private CheckBox cb_below;
    private LinearLayout linearLayout;
    //是否线下支付
    private boolean below = false;
    //private PayRadioPurified pay1;
    private PayRadioPurified pay2;
    private static final int SDK_PAY_FLAG = 1;
    private AliPayResult.AlipayTradeAppPayResponseBean payBean;
    //去地图选择网点的请求码
    private static final int SELECT_BANK_IN_MAP = 100;
    //地图返回的信息，名称，电话，地址,银行网点Id
    private String bankName = "";
    private String bankPhone = "";
    private String bankAddress = "";
    private int bankId = -1;
    //选择陪签，银行网点字样提示
    private TextView tv_chooseBankHint, tv_accompany;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        String resultResult = payResult.getResult();
                        Gson gson = new Gson();
                        AliPayResult alipay = gson.fromJson(resultResult, AliPayResult.class);
                        payBean = alipay.getAlipay_trade_app_pay_response();
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        postAliPay("1");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ActivityManager.getInstance().addActivity(PaymentActivity.this);
        initView();
        initData();
        toListener();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(PaymentActivity.this);
        super.onDestroy();
        PgyCrashManager.unregister();
    }

    /**
     * 数据加载
     */
    private void initData() {
        //evus陪签,网点不可选
        if (getIntent().getStringExtra("evus") != null) {
            rl_bodyTop.setEnabled(false);
            rl_bodyMiddle.setEnabled(false);
            cb.setEnabled(false);
            cb_below.setEnabled(false);
            tv_accompany.setTextColor(getResources().getColor(R.color.Text_3));
            tv_chooseBankHint.setTextColor(getResources().getColor(R.color.Text_3));
            below = true;
        } else {
            rl_bodyTop.setVisibility(View.VISIBLE);
            rl_bodyMiddle.setVisibility(View.VISIBLE);
            rl_bodyTop.setEnabled(true);
            rl_bodyMiddle.setEnabled(true);
            cb.setEnabled(true);
            cb_below.setEnabled(true);
        }
        pay2.setChecked(true);
        pay2.setChangeImg(R.id.pay2);
        rp = pay2;
        connectWork();
        order_id = getIntent().getIntExtra("order_id", 1);
        if (connect) {
            getOrder();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUpMsg();
        PgyCrashManager.register(this);
        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
//        PgyFeedbackShakeManager.setShakingThreshold(500);
        // 以对话框的形式弹出
//        PgyFeedbackShakeManager.register(this);
    }

    /**
     * 获取线下支付的信息
     */
    private void getUpMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get请求上传
                String url = UrlSet.selectBankId + order_id;
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(url);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final com.usamsl.global.index.step.step3.entity.Map m = gson.fromJson(str, com.usamsl.global.index.step.step3.entity.Map.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (m != null && m.getResult() != null && m.getResult().size() != 0) {
                                    com.usamsl.global.index.step.step3.entity.Map.ResultBean map = m.getResult().get(0);
                                    switch (m.getError_code()) {
                                        case 0:
                                            linearLayout.setVisibility(View.VISIBLE);
                                            below = true;
                                            cb_below.setChecked(true);
                                            cb_below.setClickable(false);
                                            tv_map.setText("网点：" + map.getName());
                                            tv_onLine.setText("电话：" + map.getFixed_line());
                                            tv_address.setText("地址：" + map.getAddress());
                                            break;
                                    }
                                } else {
                                    linearLayout.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    //检测网络
    private void connectWork() {
        if (Constants.connect) {
            connect = true;
        } else {
            ConstantsMethod.showTip(PaymentActivity.this, "无网络连接");
        }
    }

    /**
     * 发送信息改变订单状态
     */
    private void postAliPay(final String pay_type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.ordesrPay;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_id", order_id + "")
                        .add("pay_type", pay_type)
                        .add("payment", sum + "")
                        .add("is_accompany_visa", accompany + "")
                        .add("gmt_payment", payBean.getTimestamp())
                        .add("buyer_pay_amount", payBean.getTotal_amount())
                        .add("out_trade_no", payBean.getOut_trade_no())
                        .add("trade_no", payBean.getTrade_no())
                        .add("bank_outlets_id", bankId + "")
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
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final Result r = gson.fromJson(str, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (r.getError_code()) {
                                    case 0:
                                        dialog();
                                        break;
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }


    //加载订单信息
    private void getOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get请求上传
                String url = UrlSet.orderDetail + order_id;
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(url);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final PayOrder payOrder = gson.fromJson(str, PayOrder.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bean = payOrder.getResult().get(0);
                                switch (payOrder.getError_code()) {
                                    case 0:
                                        tv_name.setText("姓名：" + bean.getContact_name());
                                        tv_tel.setText("手机号码：" + bean.getContact_phone());
                                        if (bean.getVisa_area_id() != null
                                                && !bean.getVisa_area_id().equals("") &&
                                                !bean.getVisa_area_id().equals(" ")) {
                                            tv_type.setText(bean.getVisa_name() + "-" + bean.getVisa_area_id());
                                        } else {
                                            tv_type.setText(bean.getVisa_name());
                                        }
                                        tv_salary.setText("￥" + bean.getPreferential_price());
                                        tv_accompany_price.setText("￥" + bean.getAccompany_price());
                                        tv_commit.setText(bean.getPreferential_price() + " 确认支付");
                                        try {
                                            Constants.travelDate = ConstantsMethod.longToDate(bean.getDepart_time());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Constants.SPEC_KEY = bean.getPhoto_format();
                                        sum = bean.getPreferential_price();
                                        break;
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    /**
     * 监听
     */
    private void toListener() {
        group.setOnCheckedChangeListener(new PayRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(PayRadioGroup group, int checkedId) {
                radioButtonId = group.getCheckedRadioButtonId();
                rp = (PayRadioPurified) findViewById(radioButtonId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    ((PayRadioPurified) group.getChildAt(i)).setChangeImg(checkedId);
                }
            }
        });
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rp != null) {
                    if (below) {
                        //支付宝支付
                        aliPay();
                    } else {
                        ConstantsMethod.showTip(PaymentActivity.this, "您还未选择银行网点");
                    }
                } else {
                    ConstantsMethod.showTip(PaymentActivity.this, "请选择一种支付方式");
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rl_bodyTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accompany == 1) {
                    cb.setChecked(false);
                    accompany = 0;
                } else {
                    cb.setChecked(true);
                    accompany = 1;
                }
            }
        });
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //below用来判断网点了，在此不需要用below判断陪签
                if (b) {
                    sum = bean.getPreferential_price() + bean.getAccompany_price();
                    tv_commit.setText(sum + " 确认支付");
                    accompany = 1;
                } else {
                    accompany = 0;
                    sum = bean.getPreferential_price();
                    tv_commit.setText(sum + " 确认支付");
                }
            }
        });
//        cb_below.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    below = true;
//                    //pay1.setChecked(false);
//                    pay2.setChecked(false);
//                    //pay1.setEnabled(false);
//                    pay2.setEnabled(false);
//                    //pay1.setClickable(false);
//                    pay2.setClickable(false);
//                    if (rp != null) {
//                        rp.setChecked(false);
//                        rp.setChangeImg(-1);
//                        rp = null;
//                    }
//                    tv_commit.setText("确认");
//                    linearLayout.setVisibility(View.VISIBLE);
//                } else {
//                    below = false;
//                    // pay1.setEnabled(true);
//                    pay2.setClickable(true);
//                    pay2.setEnabled(true);
//                    // pay1.setClickable(true);
//                    if (accompany == 1) {
//                        sum = bean.getPreferential_price() + bean.getAccompany_price();
//                        tv_commit.setText(sum + " 确认支付");
//                    } else {
//                        sum = bean.getPreferential_price();
//                        tv_commit.setText(sum + " 确认支付");
//                    }
//                    linearLayout.setVisibility(View.GONE);
//                }
//            }
//        });
        rl_bodyMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (below) {
//                    below = false;
//                    cb_below.setChecked(false);
//                } else {
//                    below = true;
//                    cb_below.setChecked(true);
//                }
                Intent intent = new Intent(PaymentActivity.this, MapSelectionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("order_id", order_id);
                intent.putExtras(bundle);
                startActivityForResult(intent, SELECT_BANK_IN_MAP);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case SELECT_BANK_IN_MAP:

                switch (resultCode) {
                    case 200:
                        if (data != null) {
                            below = true;
                            cb_below.setChecked(true);
                            cb_below.setClickable(false);
                            bankName = data.getStringExtra("bankName");
                            bankPhone = data.getStringExtra("bankPhone");
                            bankAddress = data.getStringExtra("bankAddress");
                            bankId = Integer.parseInt(data.getStringExtra("bankId"));
                            tv_map.setText(bankName);
                            tv_onLine.setText(bankPhone);
                            tv_address.setText(bankAddress);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                break;
        }
    }

    /**
     * 发送通知给后台线下支付
     */
    private void postOrderOff() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.orderOffPayAdd;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_id", order_id + "")
                        .add("bank_outlets_id", bankId + "")
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
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final Result r = gson.fromJson(str, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (r.getError_code()) {
                                    case 0:
                                        ActivityManager.getInstance().exit();
                                        break;
                                    case 2:
                                        Intent intent = new Intent(PaymentActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    /**
     * 支付宝支付业务
     */
    public void aliPay() {
        //沙箱测试
        // EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.aliPayCode + "?totalPrice=" + sum + "&AppVersionCode=" + Constants.version + "&visaName=" + bean.getVisa_name() + "&app_company_id="+Constants.APP_COMPANY_ID;
                Request.Builder requestBuilder = new Request.Builder().url(url);
                Request request = requestBuilder.build();
                Call call = OkHttpManager.myClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final GetPay pay = gson.fromJson(str, GetPay.class);
                        switch (pay.getError_code()) {
                            case 0:
                                PayTask alipay = new PayTask(PaymentActivity.this);
                                Map<String, String> result = alipay.payV2(pay.getPayCode(), true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                        }
                    }
                });
            }
        }).start();
    }


    /**
     * 弹出支付成功弹窗
     */
    private void dialog() {
        final PopupWindow pop;
        View v = getLayoutInflater().inflate(R.layout.pay_success, null);
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
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
                //支付状态为1
                Constants.IS_PAY = 1;
                Constants.STATUS = 2;
                if (getIntent().getStringExtra("evus") == null) {
                    Intent intent = new Intent(PaymentActivity.this, PhotoSubmissionActivity.class);
                    intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                    intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                    startActivity(intent);
                    finish();
                } else {
                    ActivityManager.getInstance().exit();
                    finish();
                }
            }
        });
    }

    /**
     * 界面初始化
     */
    private void initView() {
        //pay1 = (PayRadioPurified) findViewById(R.id.pay1);
        pay2 = (PayRadioPurified) findViewById(R.id.pay2);
        rl_bodyMiddle = (RelativeLayout) findViewById(R.id.rl_bodyMiddle);
        cb_below = (CheckBox) findViewById(R.id.cb_below);
        group = (PayRadioGroup) findViewById(R.id.genderGroup);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_salary = (TextView) findViewById(R.id.tv_salary);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_accompany_price = (TextView) findViewById(R.id.tv_accompany_price);
        rl_bodyTop = (RelativeLayout) findViewById(R.id.rl_bodyTop);
        cb = (CheckBox) findViewById(R.id.cb);
        linearLayout = (LinearLayout) findViewById(R.id.rl_bodyMiddle1);
        tv_map = (TextView) findViewById(R.id.tv_map);
        tv_onLine = (TextView) findViewById(R.id.tv_onLine);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_chooseBankHint = (TextView) findViewById(R.id.chooseBankHint);
        tv_accompany = (TextView) findViewById(R.id.tv_accompany);
    }


    @Override
    protected void onPause() {
        super.onPause();
        PgyCrashManager.unregister();
    }


}
