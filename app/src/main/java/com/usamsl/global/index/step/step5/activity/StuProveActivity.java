package com.usamsl.global.index.step.step5.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.usamsl.global.index.step.step5.entity.StudentProve;
import com.usamsl.global.index.step.step5.util.MyAlertDialog;
import com.usamsl.global.index.step.step5.util.ScreenInfo;
import com.usamsl.global.index.step.step5.util.WheelMain;
import com.usamsl.global.index.step.step6.activity.PaymentActivity;
import com.usamsl.global.index.step.step7.activity.PhotoSubmissionActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

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
 * 时间：2017/2/10
 * 描述：在读证明
 */
public class StuProveActivity extends AppCompatActivity {

    //返回按钮
    private ImageView img_back;
    //保存并下载按钮
    private TextView tv_save;
    //姓
    private EditText et_fName;
    //名
    private EditText et_name;
    //性别
    private RadioGroup rg_sex;
    private RadioButton rb_man, rb_women;
    private String man = null;
    //学校名称
    private EditText et_schoolName;
    //年级班级
    private EditText et_age;
    private EditText et_class;
    //学校电话
    private EditText et_schoolTel;
    //出行天数
    private EditText et_days;
    private AddOrder addOrder;
    //父母是否有固定工作:1:有，0，没有
    private int have = -1;
    private RadioGroup rg;
    //学生类别：中小学：0，大学及其以上：1
    private int education = 1;
    private RadioGroup rg_eduction;
    private RadioButton rb_college, rb_primary;
    //中小学展现
    private RelativeLayout rl_age;
    private RelativeLayout rl_schollTel;
    private RelativeLayout rl_days;
    //大学及以上展现
    private RelativeLayout rl_student_no;
    private RelativeLayout rl_specialty;
    private RelativeLayout rl_system_year;
    private RelativeLayout rl_college;
    //返回日期
    private RelativeLayout rl_returnDate;
    private TextView tv_returnDate;
    //出资方
    private RelativeLayout rl_contribution;
    private EditText et_contribution;
    //学号
    private EditText et_student_no;
    //专业
    private EditText et_specialty;
    //学制
    private EditText et_system_year;
    //本科专科研究生
    private RadioGroup rg_college;
    private RadioButton rb_college1, rb_college2, rb_college3;
    //0：专科，1：本科，2：研究生及其以上
    private int college = -1;
    //是否联网
    private boolean connect = false;
    private ProgressBar progressBar;
    //跳过按钮
    private TextView tv_cancel;
    //出生日期
    private TextView tv_birthDate;
    private RelativeLayout rl_birthDate;
    //出发日期
    private TextView tv_outDate;
    private RelativeLayout rl_outDate;
    //就读日期
    private TextView tv_startDate;
    private RelativeLayout rl_startDate;
    private WheelMain wheelMain;
    private View timepickerview1;
    private MyAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_stu_prove);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ActivityManager.getInstance().addActivity(StuProveActivity.this);
        initView();
        initData();
        toListener();
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connect = true;
        } else {
            ConstantsMethod.showTip(StuProveActivity.this, "无网络连接");
        }
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(StuProveActivity.this);
        super.onDestroy();
    }

    /**
     * 界面初始化
     */
    private void initView() {
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rb_women = (RadioButton) findViewById(R.id.rb_women);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        tv_birthDate = (TextView) findViewById(R.id.tv_birthDate);
        rl_birthDate = (RelativeLayout) findViewById(R.id.rl_birthDate);
        tv_outDate = (TextView) findViewById(R.id.tv_outDate);
        rl_outDate = (RelativeLayout) findViewById(R.id.rl_outDate);
        tv_startDate = (TextView) findViewById(R.id.tv_startDate);
        rl_startDate = (RelativeLayout) findViewById(R.id.rl_startDate);
        rl_returnDate = (RelativeLayout) findViewById(R.id.rl_returnDate);
        tv_returnDate = (TextView) findViewById(R.id.tv_returnDate);
        rl_contribution = (RelativeLayout) findViewById(R.id.rl_contribution);
        et_contribution = (EditText) findViewById(R.id.et_contribution);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        rb_primary = (RadioButton) findViewById(R.id.rb_primary);
        rb_college = (RadioButton) findViewById(R.id.rb_college);
        rb_college3 = (RadioButton) findViewById(R.id.rb_college3);
        rb_college1 = (RadioButton) findViewById(R.id.rb_college1);
        rb_college2 = (RadioButton) findViewById(R.id.rb_college);
        rg_college = (RadioGroup) findViewById(R.id.rg_college);
        et_student_no = (EditText) findViewById(R.id.et_student_no);
        et_specialty = (EditText) findViewById(R.id.et_specialty);
        et_system_year = (EditText) findViewById(R.id.et_system_year);
        rl_age = (RelativeLayout) findViewById(R.id.rl_age);
        rl_days = (RelativeLayout) findViewById(R.id.rl_days);
        rl_schollTel = (RelativeLayout) findViewById(R.id.rl_schollTel);
        rl_system_year = (RelativeLayout) findViewById(R.id.rl_system_year);
        rl_student_no = (RelativeLayout) findViewById(R.id.rl_student_no);
        rl_college = (RelativeLayout) findViewById(R.id.rl_college);
        rl_specialty = (RelativeLayout) findViewById(R.id.rl_specialty);
        rg_eduction = (RadioGroup) findViewById(R.id.rg_education);
        rg = (RadioGroup) findViewById(R.id.rg);
        et_age = (EditText) findViewById(R.id.et_age);
        et_days = (EditText) findViewById(R.id.et_days);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_schoolName = (EditText) findViewById(R.id.et_schoolName);
        et_schoolTel = (EditText) findViewById(R.id.et_schoolTel);
        et_class = (EditText) findViewById(R.id.et_class);
        et_name = (EditText) findViewById(R.id.et_name);
        et_fName = (EditText) findViewById(R.id.et_fName);
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
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.selectStudent +
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
                                StudentProve prove = gson.fromJson(str, StudentProve.class);

                                switch (prove.getError_code()) {
                                    case 0:
                                        StudentProve.ResultBean bean = prove.getResult();
                                        if (prove.getResult().getIs_college() != null) {
                                            switch (bean.getIs_college()) {
                                                case "0":
                                                    rb_primary.setChecked(true);
                                                    if (bean.getSurname() != null)
                                                        et_fName.setText(bean.getSurname());
                                                    if (bean.getName() != null)
                                                        et_name.setText(bean.getName());
                                                    if (bean.getAge() != null)
                                                        et_age.setText(bean.getAge());
                                                    if (bean.getClassX() != null)
                                                        et_class.setText(bean.getClassX());
                                                    if (bean.getSex() != null) {
                                                        if (bean.getSex().equals("男")) {
                                                            rb_man.setChecked(true);
                                                        } else {
                                                            rb_women.setChecked(true);
                                                        }
                                                    }
                                                    if (bean.getTravel_date() != null)
                                                        tv_outDate.setText(bean.getTravel_date());
                                                    if (bean.getTelephone() != null)
                                                        et_schoolTel.setText(bean.getTelephone());
                                                    if (bean.getStudent_name() != null)
                                                        et_schoolName.setText(bean.getStudent_name());
                                                    if (bean.getDate_number() != null)
                                                        et_days.setText(bean.getDate_number());
                                                    if (bean.getBirth_date() != null)
                                                        tv_birthDate.setText(bean.getBirth_date());
                                                    if (bean.getStart_date() != null)
                                                        tv_startDate.setText(bean.getStart_date());
                                                    //新增的返回时间和出资方(注:此处的返回时间暂获取的是order_visa中的returnTime) 2017/06/29
                                                    if(ObjectIsNullUtils.TextIsNull(bean.getReturn_date())){
                                                        tv_returnDate.setText(bean.getReturn_date());
                                                    }
                                                    if(ObjectIsNullUtils.TextIsNull(bean.getPay_promoter())){
                                                        et_contribution.setText(bean.getPay_promoter());
                                                    }
                                                    break;
                                                default:
                                                    rb_college.setChecked(true);
                                                    if (bean.getSurname() != null)
                                                        et_fName.setText(bean.getSurname());
                                                    if (bean.getName() != null)
                                                        et_name.setText(bean.getName());
                                                    if (bean.getAge() != null)
                                                        et_age.setText(bean.getAge());
                                                    if (bean.getSex() != null) {
                                                        if (bean.getSex().equals("男")) {
                                                            rb_man.setChecked(true);
                                                        } else {
                                                            rb_women.setChecked(true);
                                                        }
                                                    }
                                                    if (bean.getSpecialty() != null)
                                                        et_specialty.setText(bean.getSpecialty());
                                                    if (bean.getStudent_no() != null)
                                                        et_student_no.setText(bean.getStudent_no());
                                                    if (bean.getBirth_date() != null)
                                                        tv_birthDate.setText(bean.getBirth_date());
                                                    if (bean.getTravel_date() != null)
                                                        tv_outDate.setText(bean.getTravel_date());
                                                    if (bean.getStudent_name() != null)
                                                        et_schoolName.setText(bean.getStudent_name());
                                                    if (bean.getSystem_year() != null)
                                                        et_system_year.setText(bean.getSystem_year());
                                                    if (bean.getCollege() != null) {
                                                        switch (bean.getCollege()) {
                                                            case "0":
                                                                rb_college1.setChecked(true);
                                                                break;
                                                            case "1":
                                                                rb_college2.setChecked(true);
                                                                break;
                                                            case "2":
                                                                rb_college3.setChecked(true);
                                                                break;
                                                        }
                                                    }
                                                    break;
                                            }
                                            break;
                                        }
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
        rg_college.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_college1:
                        college = 0;
                        break;
                    case R.id.rb_college2:
                        college = 1;
                        break;
                    case R.id.rb_college3:
                        college = 2;
                        break;
                }
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_yes:
                        have = 1;
                        break;
                    case R.id.rb_no:
                        have = 0;
                        break;
                }
            }
        });
        rg_eduction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_college:
                        education = 1;
                        rl_schollTel.setVisibility(View.GONE);
                        rl_outDate.setVisibility(View.GONE);
                        rl_days.setVisibility(View.GONE);
                        rl_age.setVisibility(View.GONE);
                        rl_returnDate.setVisibility(View.GONE);
                        rl_contribution.setVisibility(View.GONE);

                        rl_startDate.setVisibility(View.VISIBLE);
                        rl_specialty.setVisibility(View.VISIBLE);
                        rl_system_year.setVisibility(View.VISIBLE);
                        rl_college.setVisibility(View.VISIBLE);
                        rl_student_no.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_primary:
                        education = 0;
                        rl_schollTel.setVisibility(View.VISIBLE);
                        rl_outDate.setVisibility(View.VISIBLE);
                        rl_days.setVisibility(View.VISIBLE);
                        rl_age.setVisibility(View.VISIBLE);
                        rl_returnDate.setVisibility(View.VISIBLE);
                        rl_contribution.setVisibility(View.VISIBLE);

                        rl_startDate.setVisibility(View.GONE);
                        rl_specialty.setVisibility(View.GONE);
                        rl_system_year.setVisibility(View.GONE);
                        rl_college.setVisibility(View.GONE);
                        rl_student_no.setVisibility(View.GONE);
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
                switch (education) {
                    case 0:
                        if (have >= 0
                                && !tv_birthDate.getText().toString().equals("") && !et_schoolName.getText().toString().equals("")
                                && !et_schoolTel.getText().toString().equals("") && !et_name.getText().toString().equals("")
                                && !et_days.getText().toString().equals("") && !tv_outDate.getText().toString().equals("")
                                && !et_fName.getText().toString().equals("") && man != null
                                && !et_class.getText().toString().equals("") && !et_age.getText().toString().equals("")
                                && ObjectIsNullUtils.TextIsNull(tv_returnDate.getText().toString().trim())
                                && ObjectIsNullUtils.TextIsNull(et_contribution.getText().toString().trim())) {
                            progressBar.setVisibility(View.VISIBLE);
                            post();
                        } else {
                            ConstantsMethod.showTip(StuProveActivity.this, "信息不完整");
                        }
                        break;
                    case 1:
                        if (have >= 0 && college >= 0
                                && !tv_birthDate.getText().toString().equals("") && !et_schoolName.getText().toString().equals("")
                                && !et_specialty.getText().toString().equals("") && !et_name.getText().toString().equals("")
                                && !et_system_year.getText().toString().equals("") && !tv_startDate.getText().toString().equals("")
                                && !et_fName.getText().toString().equals("") && man != null
                                && !et_student_no.getText().toString().equals("") && !et_age.getText().toString().equals("")
                                ) {
                            progressBar.setVisibility(View.VISIBLE);
                            post1();
                        } else {
                            ConstantsMethod.showTip(StuProveActivity.this, "信息不完整");
                        }
                        break;
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StuProveActivity.this, JobProve2Activity.class);
                intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                intent.putExtra("addOrder", addOrder);
                startActivity(intent);
            }
        });
        rl_birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(StuProveActivity.this)
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
        //选择中小学时(返回时间)
        rl_returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(StuProveActivity.this)
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
        rl_outDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(StuProveActivity.this)
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
        rl_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(StuProveActivity.this)
                        .builder()
                        .setView(timepickerview1)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tv_startDate.setText(wheelMain.getTime());
                            }
                        });
                dialog.show();
            }
        });

    }

    private void timePicker() {
        //日期选择
        LayoutInflater inflater1 = LayoutInflater.from(StuProveActivity.this);
        timepickerview1 = inflater1.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo1 = new ScreenInfo(StuProveActivity.this);
        wheelMain = new WheelMain(timepickerview1);
        wheelMain.screenheight = screenInfo1.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
    }

    /**
     * 发送中小学信息
     */
    private void post() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.saveStudent;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("contact_id", addOrder.getContact_id() + "")
                        .add("country_id", Constants.COUNTRY_ID + "")
                        .add("name", et_name.getText().toString())
                        .add("surname", et_fName.getText().toString())
                        .add("birth_date", tv_birthDate.getText().toString())
                        .add("sex", man)
                        .add("age", et_age.getText().toString())
                        .add("class", et_class.getText().toString())
                        .add("student_name", et_schoolName.getText().toString())
                        .add("telephone", et_schoolTel.getText().toString())
                        .add("travel_date", tv_outDate.getText().toString())
                        .add("date_number", et_days.getText().toString())
                        .add("is_college", 0 + "")
                        .add("order_id", getIntent().getIntExtra("order_id", 1) + "")
                        .add("return_date",tv_returnDate.getText().toString())
                        .add("pay_promoter",et_contribution.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("FENG",e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final Result r = gson.fromJson(str, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                switch (r.getError_code()) {
                                    case 0:
                                        ConstantsMethod.showTip(StuProveActivity.this, "已发送邮件到您的常用邮箱");
                                        switch (have) {
                                            case 0:
                                                if (Constants.IS_PAY == 1) {
                                                    updateStatus("2");
                                                    Intent intent = new Intent(StuProveActivity.this, PhotoSubmissionActivity.class);
                                                    intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                                                    intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                                                    startActivity(intent);
                                                } else {
                                                    updateStatus("1");
                                                    //这里的广播是在告知订单详情页面，改变详情页面的订单状态
                                                    Intent intentReceiver = new Intent("ORDER_STATUS_CHANGED");
                                                    intentReceiver.putExtra("ORDER_STATUS", "H5Form");
                                                    StuProveActivity.this.sendBroadcast(intentReceiver);
                                                    Intent intent = new Intent(StuProveActivity.this, PaymentActivity.class);
                                                    intent.putExtra("addOrder", getIntent().getParcelableExtra("addOrder"));
                                                    intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                                                    startActivity(intent);
                                                }
                                                break;
                                            case 1:
                                                Intent intent1 = new Intent(StuProveActivity.this, JobProve1Activity.class);
                                                intent1.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                                                intent1.putExtra("addOrder", addOrder);
                                                startActivity(intent1);
                                                break;
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

    /**
     * 发送大学及以上信息
     */
    private void post1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.saveStudent;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("contact_id", addOrder.getContact_id() + "")
                        .add("country_id", Constants.COUNTRY_ID + "")
                        .add("name", et_name.getText().toString())
                        .add("surname", et_fName.getText().toString())
                        .add("birth_date", tv_birthDate.getText().toString())
                        .add("sex", man)
                        .add("age", et_age.getText().toString())
                        .add("student_no", et_student_no.getText().toString())
                        .add("student_name", et_schoolName.getText().toString())
                        .add("start_date", tv_startDate.getText().toString())
                        .add("specialty", et_specialty.getText().toString())
                        .add("college", college + "")
                        .add("is_college", 1 + "")
                        .add("system_year", et_system_year.getText().toString())
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
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final Result r = gson.fromJson(str, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                switch (r.getError_code()) {
                                    case 0:
                                        ConstantsMethod.showTip(StuProveActivity.this, "已发送邮件到您的常用邮箱");
                                        switch (have) {
                                            case 0:
                                                Intent intent = new Intent(StuProveActivity.this, PaymentActivity.class);
                                                intent.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                                                intent.putExtra("addOrder", addOrder);
                                                startActivity(intent);
                                                break;
                                            case 1:
                                                Intent intent1 = new Intent(StuProveActivity.this, JobProve1Activity.class);
                                                intent1.putExtra("order_id", getIntent().getIntExtra("order_id", 1));
                                                intent1.putExtra("addOrder", addOrder);
                                                startActivity(intent1);
                                                break;
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
}
