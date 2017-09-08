package com.usamsl.global.index.step.step7.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.squareup.timessquare.CalendarPickerView;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step5.activity.DocumentScanningActivity;
import com.usamsl.global.index.step.step6.activity.PaymentActivity;
import com.usamsl.global.index.step.step6.entity.PayOrder;
import com.usamsl.global.index.step.step8.InterviewTimeActivity;
import com.usamsl.global.index.step.step7.adapter.PhotoSubmissionAdapter;
import com.usamsl.global.index.step.step7.entity.OrderDatum;
import com.usamsl.global.index.step.step7.entity.PhotoSubmission;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.view.FullyLinearLayoutManager;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间2016/12/28
 * 描述：材料提交：照片，户口本，在职证明
 */
public class PhotoSubmissionActivity extends AppCompatActivity {

    //返回按钮
    private ImageView img_back;
    //提交按钮
    private TextView tv_submit;
    private RecyclerView lv;
    private List<PhotoSubmission> mData;
    private PhotoSubmissionAdapter adapter;
    //订单的id
    private int order_id;
    //联系人id
    private int contact_id;
    //联系人
    private String contact_name;
    //判断是否联网
    private boolean connect = false;
    //其他类型返回后
    private PhotoSubmission p;
    private int attachment_id;
    private RelativeLayout progressBar;
    private int which = -1;
    //提交按钮是否可以点击
    private int success = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_submission);
        ActivityManager.getInstance().addActivity(PhotoSubmissionActivity.this);
        initView();
        initData();
        toListener();
    }

    @Override
    protected void onDestroy() {
        System.gc();
        ActivityManager.getInstance().removeActivity(PhotoSubmissionActivity.this);
        super.onDestroy();
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
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (success) {
                    case 1:
                        int i = 0;
                        for (; i < mData.size(); i++) {
                            if (mData.get(i).getAttachment_id() <= 0) {
                                break;
                            }
                        }
                        if (i == mData.size()) {
                            updateStatus();
                        } else {
                            ConstantsMethod.showTip(PhotoSubmissionActivity.this, "信息不完整");
                        }
                        break;
                }
//                success = 0;
            }
        });
    }

    /**
     * 更改订单状态
     */
    private void updateStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateDatumOrder;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_id", order_id + "")
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
                        String error = e.toString();
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final Result r = gson.fromJson(str, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                success = 1;
                                switch (r.getError_code()) {
                                    case 0:
                                        if (Constants.STATUS > 3) {
                                            ActivityManager.getInstance().exit();
                                        } else {
                                            dialog();
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
     * 弹出提交成功弹窗
     */
    private void dialog() {
        final PopupWindow pop;
        View v = getLayoutInflater().inflate(R.layout.pay_success, null);
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
        TextView tv = (TextView) v.findViewById(R.id.tv);
        TextView tv_hint = (TextView) v.findViewById(R.id.tv_hint);
        tv.setText("提交成功");
        tv_hint.setVisibility(View.VISIBLE);
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
                //修改订单状态
                Constants.STATUS = 3;
                //这里的广播是在告知订单详情页面，改变详情页面的订单状态
                Intent intentReceiver = new Intent("ORDER_STATUS_CHANGED");
                intentReceiver.putExtra("ORDER_STATUS", "Account_Photo");
                PhotoSubmissionActivity.this.sendBroadcast(intentReceiver);
                Intent intent = new Intent(PhotoSubmissionActivity.this, InterviewTimeActivity.class);
                intent.putExtra("order_id", order_id);
                startActivity(intent);
            }
        });
    }

    /**
     * 数据加载
     */
    private void initData() {
        AddOrder addOrder = getIntent().getParcelableExtra("addOrder");
        order_id = getIntent().getIntExtra("order_id", 101);
        contact_id = addOrder.getContact_id();
        contact_name = addOrder.getContact_name();
        connectWork();
        mData = new ArrayList<>();
        //创建默认的线性LayoutManager
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv.setLayoutManager(linearLayoutManager);
        adapter = new PhotoSubmissionAdapter(this, mData);
        if (connect) {
            new ProgressBarLoadUtils(this).startProgressDialog();
            getDatum();
        }
    }

    /**
     * 材料请求
     */
    private void getDatum() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get请求上传
                String url = UrlSet.orderDatum + order_id;
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(url);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        new ProgressBarLoadUtils(PhotoSubmissionActivity.this).stopProgressDialog();
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        new ProgressBarLoadUtils(PhotoSubmissionActivity.this).stopProgressDialog();
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final OrderDatum orderDatum = gson.fromJson(str, OrderDatum.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (orderDatum.getError_code()) {
                                    case 0:
                                        List<Integer> show = new ArrayList<>();
                                        int num = -1;
                                        mData.clear();
                                        PhotoSubmission photoSubmission;
                                        List<OrderDatum.ResultBean> beanList = orderDatum.getResult();
                                        for (int i = 0; i < beanList.size(); i++) {
                                            photoSubmission = new PhotoSubmission();
                                            if (!beanList.get(i).getVisa_datum_type().equals("11") &&
                                                    !beanList.get(i).getVisa_datum_type().equals("1") &&
                                                    !beanList.get(i).getVisa_datum_type().equals("2")
                                                    && !beanList.get(i).getVisa_datum_type().equals("5")) {
                                                photoSubmission.setOrder_id(order_id);
                                                photoSubmission.setContact_id(contact_id);
                                                photoSubmission.setContact_name(contact_name);
                                                photoSubmission.setAttachment_id(beanList.get(i).getAttachment_id());
                                                photoSubmission.setOrder_datum_id(beanList.get(i).getOrder_datum_id());
                                                if (beanList.get(i).getPath_name().equals("")) {
                                                    photoSubmission.setPhotoUrl(null);
                                                } else {
                                                    photoSubmission.setPhotoUrl(beanList.get(i).getPath_name());
                                                }

                                                photoSubmission.setVisa_datum_type(beanList.get(i).getVisa_datum_type());
                                                photoSubmission.setTypeMaterial(beanList.get(i).getVisa_datum_name());
                                                switch (beanList.get(i).getIs_must()) {
                                                    case 0:
                                                        photoSubmission.setNecessary(false);
                                                        break;
                                                    case 1:
                                                        photoSubmission.setNecessary(true);
                                                        break;
                                                }
                                                List<String> list = new ArrayList<>();
                                                if (beanList.get(i).getExplain_desc() != null) {
                                                    String[] str = beanList.get(i).getExplain_desc().split("#");
                                                    if (str.length > 0) {
                                                        for (int j = 0; j < str.length; j++) {
                                                            list.add(str[j]);
                                                        }
                                                        photoSubmission.setTypeMaterialDemand(list);
                                                    }
                                                } else {
                                                    list.add("");
                                                    photoSubmission.setTypeMaterialDemand(list);
                                                }
                                                mData.add(photoSubmission);
                                            } else if (beanList.get(i).getVisa_datum_type().equals("5")) {
                                                if (beanList.get(i).getPath_name().equals("")) {
                                                    photoSubmission.setPhotoUrl(null);
                                                } else {
                                                    if (num != -1) {
                                                        photoSubmission.setPhotoUrl(mData.get(num).getPhotoUrl() + "#" +
                                                                beanList.get(i).getPath_name());
                                                    } else {
                                                        photoSubmission.setPhotoUrl(beanList.get(i).getPath_name());
                                                    }
                                                }
                                                show.add(beanList.get(i).getShow_order());
                                                Constants.show_order = show;
                                                if (num == -1) {
                                                    photoSubmission.setContact_id(contact_id);
                                                    photoSubmission.setOrder_id(order_id);
                                                    photoSubmission.setContact_name(contact_name);
                                                    photoSubmission.setAttachment_id(beanList.get(i).getAttachment_id());
                                                    photoSubmission.setOrder_datum_id(beanList.get(i).getOrder_datum_id());
                                                    photoSubmission.setVisa_datum_type(beanList.get(i).getVisa_datum_type());
                                                    photoSubmission.setTypeMaterial(beanList.get(i).getVisa_datum_name());
                                                    switch (beanList.get(i).getIs_must()) {
                                                        case 0:
                                                            photoSubmission.setNecessary(false);
                                                            break;
                                                        case 1:
                                                            photoSubmission.setNecessary(true);
                                                            break;
                                                    }
                                                    List<String> list = new ArrayList<>();
                                                    if (beanList.get(i).getExplain_desc() != null) {
                                                        String[] str = beanList.get(i).getExplain_desc().split("#");
                                                        if (str.length > 0) {
                                                            for (int j = 0; j < str.length; j++) {
                                                                list.add(str[j]);
                                                            }
                                                            photoSubmission.setTypeMaterialDemand(list);
                                                        }
                                                    } else {
                                                        list.add("");
                                                        photoSubmission.setTypeMaterialDemand(list);
                                                    }
                                                    mData.add(photoSubmission);
                                                    num = mData.size() - 1;
                                                } else {
                                                    mData.get(num).setPhotoUrl(photoSubmission.getPhotoUrl());
                                                }
                                            }
                                        }
                                        lv.setAdapter(adapter);
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
     * 初始化界面
     */
    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        lv = (RecyclerView) findViewById(R.id.lv);
        progressBar = (RelativeLayout) findViewById(R.id.ll_progress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ConstantsCode.OTHER_PHOTO:
                    int order_datum_id = data.getIntExtra("order_datum_id", 1);
                    connectWork();
                    if (connect && Constants.setPhoto != null) {
                        int i = 0;
                        for (; i < mData.size(); i++) {
                            if (order_datum_id == mData.get(i).getOrder_datum_id()) {
                                mData.get(i).setPhotoUrl(Constants.setPhoto);
                                p = mData.get(i);
                                adapter.notifyDataSetChanged();
                                which = i;
                                postPhoto(i);
                                break;
                            }
                        }
                    }
                    break;
                case ConstantsCode.PHOTO:
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getVisa_datum_type().equals("3")) {
                            mData.get(i).setPhotoUrl(Constants.setPhoto);
                            adapter.notifyDataSetChanged();
                            p = mData.get(i);
                            which = i;
                            postPhoto(i);
                            break;
                        }
                    }
                    Intent intentReceiver = new Intent(Constants.PHOTO_UPLOAD_SUCCESS);
                    intentReceiver.putExtra("Visa_datum_type","3");
                    PhotoSubmissionActivity.this.sendBroadcast(intentReceiver);
                    break;
                case ConstantsCode.ACCOUNT_BOOK:
                    attachment_id = data.getIntExtra("attachment_id", 1);
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getVisa_datum_type().equals("5")) {
                            mData.get(i).setPhotoUrl(Constants.setPhoto);
                            mData.get(i).setAttachment_id(attachment_id);
                            adapter.notifyDataSetChanged();
                            which = i;
                            break;
                        }
                    }
                    break;
            }
            if (which == 0) {
                getDatum();
            }
        }
    }

    //上传图片至服务器
    private void postPhoto(final int i) {
        new ProgressBarLoadUtils(this).startProgressDialog();
        OkHttpClient client = OkHttpManager.myClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(p.getPhotoUrl());
        if (file != null) {
            builder.addFormDataPart("img", "1.jpg", RequestBody.create(Constants.MEDIA_TYPE_PNG, file))
                    .addFormDataPart("attachment_id", p.getAttachment_id() + "")
                    .addFormDataPart("visa_datum_type", p.getVisa_datum_type())
                    .addFormDataPart("visa_datum_name", p.getTypeMaterial())
                    .addFormDataPart("contact_id", p.getContact_id() + "")
                    .addFormDataPart("show_order", "1")
                    .addFormDataPart("is_del", "0");
        }
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(UrlSet.upload)//地址
                .post(requestBody)//添加请求体
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new ProgressBarLoadUtils(PhotoSubmissionActivity.this).stopProgressDialog();
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final ResultId resultId = gson.fromJson(str, ResultId.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new ProgressBarLoadUtils(PhotoSubmissionActivity.this).stopProgressDialog();
                        switch (resultId.getError_code()) {
                            case 0:
                                attachment_id = resultId.getReason_id();
                                mData.get(i).setAttachment_id(attachment_id);
                                updateDatum();
                                break;
                        }
                    }
                });
            }
        });
    }

    /**
     * 更新数据
     */
    private void updateDatum() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.orderDatumUpdate;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_datum_id", p.getOrder_datum_id() + "")
                        .add("attachment_id", attachment_id + "")
                        .add("order_id", p.getOrder_id() + "")
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                if (which == 0) {
                                    getDatum();
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
            ConstantsMethod.showTip(PhotoSubmissionActivity.this, "网络异常，上传失败");
        }
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
