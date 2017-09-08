package com.usamsl.global.index.step.step5.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step5.entity.JobProve;
import com.usamsl.global.index.step.step5.util.MyAlertDialog;
import com.usamsl.global.index.step.step5.util.ScreenInfo;
import com.usamsl.global.index.step.step5.util.WheelMain;
import com.usamsl.global.index.step.step6.activity.PaymentActivity;
import com.usamsl.global.index.step.step7.activity.PhotoSubmissionActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2016/12/28
 * 描述：本人在职证明
 */
public class JobProveActivity extends AppCompatActivity {

    //返回按钮
    private ImageView img_back;
    //保存并下载按钮
    private TextView tv_save;
    //姓
    private EditText et_fName;
    //名
    private EditText et_name;
    //月薪
    private EditText et_salary;

    //公司名称
    private EditText et_companyName;
    //公司职位
    private EditText et_companyPosition;
    //单位地址
    private EditText et_companyAddress;
    //单位电话
    private EditText et_companyTel;
    //出行天数
    private EditText et_days;
    //出行原因
    private EditText et_reason;
    //公司传真
    private EditText et_fax;
    //护照号
    private EditText et_passport;
    private AddOrder addOrder;
    //出资人：1：自费，2：公司
    private int pay = -1;
    //出资人
    private RadioGroup rg;
    private RadioButton rb_personal, rb_company;
    //是否联网
    private boolean connect = false;
    private ProgressBar progressBar;
    //跳过按钮
    private TextView tv_cancel;
    private TextView tv_outDate;
    private RelativeLayout rl_outDate;
    private TextView tv_returnDate;
    private RelativeLayout rl_returnDate;
    private TextView tv_birthDate;
    private RelativeLayout rl_birthDate;
    //任职日期
    private TextView tv_jobTime;
    private RelativeLayout rl_jobTime;
    private WheelMain wheelMain;
    private View timepickerview1;
    //性别
    private RadioGroup rg_sex;
    private RadioButton rb_man, rb_women;
    private String man = null;
    private MyAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_job_prove);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ActivityManager.getInstance().addActivity(JobProveActivity.this);
        initView();
        initData();
        toListener();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(JobProveActivity.this);
        super.onDestroy();
    }

    private void initData() {
        addOrder = getIntent().getParcelableExtra("addOrder");
        connectWork();
        if (connect) {
            get();
        }
        timePicker();
    }

    /**
     * 根据联系人id加载数据
     */
    private void get() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.selectProfession +
                        addOrder.getContact_id() + "&order_id=" + getIntent().getIntExtra("order_id", 1));
                final Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        final String str = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                JobProve jobProve = gson.fromJson(str, JobProve.class);
                                switch (jobProve.getError_code()) {
                                    case 0:
                                        JobProve.ResultBean bean = jobProve.getResult();
                                        if (bean.getName() != null)
                                            et_name.setText(bean.getName());
                                        if (bean.getSurname() != null)
                                            et_fName.setText(bean.getSurname());
                                        if (bean.getSex() != null) {
                                            if (bean.getSex().equals("男")) {
                                                rb_man.setChecked(true);
                                            } else {
                                                rb_women.setChecked(true);
                                            }
                                        }
                                        if (bean.getSalary() != null)
                                            et_salary.setText(bean.getSalary());
                                        if (bean.getCompany_name() != null)
                                            et_companyName.setText(bean.getCompany_name());
                                        if (bean.getPosition() != null)
                                            et_companyPosition.setText(bean.getPosition());
                                        if (bean.getAddress() != null)
                                            et_companyAddress.setText(bean.getAddress());
                                        if (bean.getTelephone() != null)
                                            et_companyTel.setText(bean.getTelephone());
                                        if (bean.getE_mail() != null)
                                            et_fax.setText(bean.getE_mail());
                                        if (bean.getEntry_date() != null)
                                            tv_jobTime.setText(bean.getEntry_date());
                                        if (bean.getBirth_date() != null)
                                            tv_birthDate.setText(bean.getBirth_date());
                                        if (bean.getPassport_no() != null)
                                            et_passport.setText(bean.getPassport_no());
                                        if (bean.getDate_number() != null)
                                            et_days.setText(bean.getDate_number());
                                        if (bean.getTravel_desc() != null)
                                            et_reason.setText(bean.getTravel_desc());
                                        if (bean.getEntry_date() != null)
                                            tv_jobTime.setText(bean.getEntry_date());
                                        if (bean.getReturn_date() != null)
                                            tv_returnDate.setText(bean.getReturn_date());
                                        if (bean.getTravel_date() != null)
                                            tv_outDate.setText(bean.getTravel_date());
                                        if (bean.getPay_type() != null) {
                                            switch (bean.getPay_type()) {
                                                case "1":
                                                    pay = 1;
                                                    rb_personal.setChecked(true);
                                                    break;
                                                case "2":
                                                    pay = 2;
                                                    rb_company.setChecked(true);
                                                    break;
                                            }
                                        }
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
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connect = true;
        } else {
            ConstantsMethod.showTip(JobProveActivity.this, "无网络连接");
        }
    }

    /**
     * 界面初始化
     */
    private void initView() {
        tv_birthDate = (TextView) findViewById(R.id.tv_birthDate);
        tv_jobTime = (TextView) findViewById(R.id.tv_jobTime);
        rl_birthDate = (RelativeLayout) findViewById(R.id.rl_birthDate);
        rl_jobTime = (RelativeLayout) findViewById(R.id.rl_jobTime);
        tv_outDate = (TextView) findViewById(R.id.tv_outDate);
        tv_returnDate = (TextView) findViewById(R.id.tv_returnDate);
        rl_outDate = (RelativeLayout) findViewById(R.id.rl_outDate);
        rl_returnDate = (RelativeLayout) findViewById(R.id.rl_returnDate);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        rb_personal = (RadioButton) findViewById(R.id.rb_personal);
        rb_company = (RadioButton) findViewById(R.id.rb_company);
        rg = (RadioGroup) findViewById(R.id.rg);
        et_fName = (EditText) findViewById(R.id.et_fName);
        et_passport = (EditText) findViewById(R.id.et_passport);
        et_fax = (EditText) findViewById(R.id.et_fax);
        et_days = (EditText) findViewById(R.id.et_days);
        et_reason = (EditText) findViewById(R.id.et_reason);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_companyAddress = (EditText) findViewById(R.id.et_companyAddress);
        et_companyName = (EditText) findViewById(R.id.et_companyName);
        et_companyTel = (EditText) findViewById(R.id.et_companyTel);
        et_companyPosition = (EditText) findViewById(R.id.et_companyPosition);
        et_name = (EditText) findViewById(R.id.et_name);
        et_salary = (EditText) findViewById(R.id.et_salary);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rb_women = (RadioButton) findViewById(R.id.rb_women);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
    }


    /**
     * 监听
     */
    private void toListener() {
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_man:
                        man = "男";
                        break;
                    case R.id.rb_women:
                        man = "女";
                        break;
                }
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_personal:
                        pay = 2;
                        break;
                    case R.id.rb_company:
                        pay = 1;
                        break;
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_fax.getText().toString().equals("") && pay > 0 && !tv_returnDate.getText().toString().equals("")
                        && !et_passport.getText().toString().equals("") && !tv_jobTime.getText().toString().equals("")
                        && !tv_birthDate.getText().toString().equals("") && !tv_outDate.getText().toString().equals("")
                        && !et_companyAddress.getText().toString().equals("") && !et_companyName.getText().toString().equals("")
                        && !et_companyPosition.getText().toString().equals("") && !et_companyTel.getText().toString().equals("")
                        && !et_days.getText().toString().equals("") && !et_name.getText().toString().equals("")
                        && !et_fName.getText().toString().equals("") && man != null
                        && !et_salary.getText().toString().equals("") && !et_reason.getText().toString().equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    post();
                } else {
                    ConstantsMethod.showTip(JobProveActivity.this, "信息不完整");
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.IS_PAY == 1) {
                    updateStatus("2");
                    Intent intent = new Intent(JobProveActivity.this, PhotoSubmissionActivity.class);
                    intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                    intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                    startActivity(intent);
                } else {
                    updateStatus("1");
                    //这里的广播是在告知订单详情页面，改变详情页面的订单状态
                    Intent intentReceiver = new Intent("ORDER_STATUS_CHANGED");
                    intentReceiver.putExtra("ORDER_STATUS","H5Form");
                    JobProveActivity.this.sendBroadcast(intentReceiver);
                    Intent intent = new Intent(JobProveActivity.this, PaymentActivity.class);
                    intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                    intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                    startActivity(intent);
                }
            }
        });
        rl_jobTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(JobProveActivity.this)
                        .builder()
                        .setView(timepickerview1)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tv_jobTime.setText(wheelMain.getTime());
                            }
                        });
                dialog.show();

            }
        });
        rl_birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(JobProveActivity.this)
                        .builder()
                        .setView(timepickerview1)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tv_birthDate.setText(wheelMain.getTime());
                            }
                        });
                dialog.show();
            }
        });
        rl_outDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(JobProveActivity.this)
                        .builder()
                        .setView(timepickerview1)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tv_outDate.setText(wheelMain.getTime());
                            }
                        });
                dialog.show();

            }
        });
        rl_returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(JobProveActivity.this)
                        .builder()
                        .setView(timepickerview1)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tv_returnDate.setText(wheelMain.getTime());
                            }
                        });
                dialog.show();
            }
        });
    }

    private void timePicker() {
        //日期选择
        LayoutInflater inflater1 = LayoutInflater.from(JobProveActivity.this);
        timepickerview1 = inflater1.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo1 = new ScreenInfo(JobProveActivity.this);
        wheelMain = new WheelMain(timepickerview1);
        wheelMain.screenheight = screenInfo1.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
    }

    /**
     * 发送信息
     */
    private void post() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.saveProfession;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("contact_id", addOrder.getContact_id() + "")
                        .add("is_oneself", 1 + "")
                        .add("name", et_name.getText().toString())
                        .add("sex", man)
                        .add("salary", et_salary.getText().toString())
                        .add("entry_date", tv_jobTime.getText().toString())
                        .add("company_name", et_companyName.getText().toString())
                        .add("position", et_companyPosition.getText().toString())
                        .add("address", et_companyAddress.getText().toString())
                        .add("telephone", et_companyTel.getText().toString())
                        .add("birth_date", tv_birthDate.getText().toString())
                        .add("travel_date", tv_outDate.getText().toString())
                        .add("return_date", tv_returnDate.getText().toString())
                        .add("date_number", et_days.getText().toString())
                        .add("pay_type", pay + "")
                        .add("surname", et_fName.getText().toString())
                        .add("passport_no", et_passport.getText().toString())
                        .add("e_mail", et_fax.getText().toString())
                        .add("travel_desc", et_reason.getText().toString())
                        .add("country_id", Constants.COUNTRY_ID + "")
                        .add("order_id", getIntent().getIntExtra("order_id", 1) + "")
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
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final Result r = gson.fromJson(str, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                switch (r.getError_code()) {
                                    case 0:
                                        ConstantsMethod.showTip(JobProveActivity.this, "已发送邮件到您的常用邮箱");
                                        if (Constants.IS_PAY == 1) {
                                            updateStatus("2");
                                            Intent intent = new Intent(JobProveActivity.this, PhotoSubmissionActivity.class);
                                            intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                                            intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                                            startActivity(intent);
                                        } else {
                                            updateStatus("1");
                                            //这里的广播是在告知订单详情页面，改变详情页面的订单状态
                                            Intent intentReceiver = new Intent("ORDER_STATUS_CHANGED");
                                            intentReceiver.putExtra("ORDER_STATUS","H5Form");
                                            JobProveActivity.this.sendBroadcast(intentReceiver);
                                            Intent intent = new Intent(JobProveActivity.this, PaymentActivity.class);
                                            intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                                            intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                                            startActivity(intent);
                                        }
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
     * 更改订单状态
     */
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
}
