package com.usamsl.global.index.step.step8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.view.MyCalendarView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2017/1/17
 * 描述：预约面试三个时间
 */
public class InterviewTimeActivity extends AppCompatActivity {
    //返回按钮
    private ImageView img_back;
    //确定按钮
    private RelativeLayout tv_ok;
    private CalendarPickerView calendar;
    //选择的时间
    private List<Date> list;
    private ProgressBar progressBar;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_time);
        ActivityManager.getInstance().addActivity(this);
        initView();
        initData();
        toListener();
    }

    /**
     * 监听
     */
    private void toListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> str;
                if (list.size() == 3) {
                    str = new ArrayList<>();
                    for (Date d : list) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        str.add(format.format(d));
                    }
                    setDialog(str);
                } else {
                    ConstantsMethod.showTip(InterviewTimeActivity.this, "您必须预约三个时间");
                }
            }
        });
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                if (list.size() == 3) {
                    calendar.selectDate(date, false);
                    ConstantsMethod.showTip(InterviewTimeActivity.this, "您只能选择三个时间");
                } else {
                    list.add(date);
                }
            }

            @Override
            public void onDateUnselected(Date date) {
                list.remove(date);
            }
        });
    }

    /**
     * 确定弹窗
     */
    private void setDialog(final List str) {
        String date = str.get(0) + "\n" + str.get(1) + "\n" + str.get(2);
        final CustomIsFormSaveDialog dialog = new CustomIsFormSaveDialog(InterviewTimeActivity.this, date, "您的预约面试时间是");
        dialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void doConfirm() {
                Constants.STATUS = 4;
                progressBar.setVisibility(View.VISIBLE);
                postDate(str);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 数据加载
     */
    private void initData() {
        list = new ArrayList<>();
        calendar.setCustomDayView(new DefaultDayViewAdapter());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime();
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.init(date, Constants.travelDate)
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
    }

    /**
     * 界面初始化
     */
    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progress);
        tv_ok = (RelativeLayout) findViewById(R.id.tv_ok);
        img_back = (ImageView) findViewById(R.id.img_back);
        calendar = (MyCalendarView) findViewById(R.id.calendar_view);
    }

    /**
     * 发送预约时间到后台
     *
     * @param str
     */
    private void postDate(final List<String> str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.orderUpdateTime;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_id", getIntent().getIntExtra("order_id", 101) + "")
                        .add("bespeak_time1", str.get(0))
                        .add("bespeak_time2", str.get(1))
                        .add("bespeak_time3", str.get(2))
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
                                ConstantsMethod.showTip(InterviewTimeActivity.this, "网络异常！");
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
//                                progressBar.setVisibility(View.GONE);
                                switch (r.getError_code()) {
                                    case 0:
                                        Intent intentReceiver = new Intent("ORDER_STATUS_CHANGED");
                                        intentReceiver.putExtra("ORDER_STATUS", "InterviewTime");
                                        InterviewTimeActivity.this.sendBroadcast(intentReceiver);
                                        ActivityManager.getInstance().exit();
                                        InterviewTimeActivity.this.finish();
                                        break;
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
