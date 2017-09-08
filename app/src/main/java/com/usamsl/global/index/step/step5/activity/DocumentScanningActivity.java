package com.usamsl.global.index.step.step5.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step2.activity.BeforeVisaActivity;
import com.usamsl.global.index.step.step5.adapter.DocumentAdapter;
import com.usamsl.global.index.step.step7.adapter.PhotoSubmissionAdapter;
import com.usamsl.global.index.step.step7.entity.OrderDatum;
import com.usamsl.global.index.step.step7.entity.PhotoSubmission;
import com.usamsl.global.index.step.step8.InterviewTimeActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ImageCompress;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.view.FullyLinearLayoutManager;

import java.io.File;
import java.io.IOException;
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
 * 时间2017/3/7
 * 描述：证件扫描
 */
public class DocumentScanningActivity extends AppCompatActivity {

    //返回按钮
    private ImageView img_back;
    //提交按钮
    private TextView tv_submit;
    private RecyclerView lv;
    private List<PhotoSubmission> mData;
    private DocumentAdapter adapter;
    //订单的id
    private int order_id;
    //联系人id
    private int contact_id;
    //联系人
    private String contact_name;
    //判断是否联网
    private boolean connect = false;
    private int attachment_id;
    private AddOrder addOrder;
    private int which = -1;
    //罩层
    private RelativeLayout progress;
    private String formUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_scanning);
        ActivityManager.getInstance().addActivity(DocumentScanningActivity.this);
        initView();
        initData();
        toListener();
    }



    @Override
    protected void onDestroy() {
        System.gc();
        ActivityManager.getInstance().removeActivity(DocumentScanningActivity.this);
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
                int i = 0;
                for (; i < mData.size(); i++) {
                    if (mData.get(i).getAttachment_id() <= 0) {
                        break;
                    }
                }
                if (i == mData.size()) {
                    progress.setVisibility(View.VISIBLE);
                    //dialog();
                    updadeOrderDatum();
                } else {
                    ConstantsMethod.showTip(DocumentScanningActivity.this, "信息不完整");
                }
            }
        });
    }
    /**
     * 把联系人和订单号关联到一起
     */
    private void updadeOrderDatum(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.updateOrderDatum;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_id", order_id + "")
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
        tv_hint.setVisibility(View.GONE);
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
                progress.setVisibility(View.GONE);
                //这里的广播是在告知订单详情页面，改变详情页面的订单状态
                Intent intentReceiver = new Intent("ORDER_STATUS_CHANGED");
                intentReceiver.putExtra("ORDER_STATUS","IdCard_passPort");
                DocumentScanningActivity.this.sendBroadcast(intentReceiver);
                Intent intent = new Intent(DocumentScanningActivity.this, DSActivity.class);
                intent.putExtra("addOrder", addOrder);
                intent.putExtra("order_id", order_id);
                intent.putExtra("formUrl", formUrl);
                startActivity(intent);
            }
        });
    }

    /**
     * 数据加载
     */
    private void initData() {
        formUrl = getIntent().getStringExtra("formUrl");
        addOrder = getIntent().getParcelableExtra("addOrder");
        order_id = getIntent().getIntExtra("order_id", 101);
        contact_id = addOrder.getContact_id();
        contact_name = addOrder.getContact_name();
        connectWork();
        mData = new ArrayList<>();
        //创建默认的线性LayoutManager
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv.setLayoutManager(linearLayoutManager);
        adapter = new DocumentAdapter(this, mData);
//        if (connect) {
//            getDatum();
//        }
    }

    /**
     * 材料请求
     */
    private void getDatum() {
        new ProgressBarLoadUtils(this).startProgressDialog();
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
                        new ProgressBarLoadUtils(DocumentScanningActivity.this).stopProgressDialog();
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        new ProgressBarLoadUtils(DocumentScanningActivity.this).stopProgressDialog();
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final OrderDatum orderDatum = gson.fromJson(str, OrderDatum.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (orderDatum.getError_code()) {
                                    case 0:
                                        mData.clear();
                                        PhotoSubmission photoSubmission;
                                        List<OrderDatum.ResultBean> beanList = orderDatum.getResult();
                                        //第几个是身份证正面
                                        int num = -1;
                                        List<Integer> show = new ArrayList<>();
                                        for (int i = 0; i < beanList.size(); i++) {
                                            photoSubmission = new PhotoSubmission();
                                            if (beanList.get(i).getVisa_datum_type().equals("1")) {
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
                                            if (beanList.get(i).getVisa_datum_type().equals("2")) {
                                                photoSubmission.setContact_id(contact_id);
                                                photoSubmission.setOrder_id(order_id);
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
        progress = (RelativeLayout) findViewById(R.id.ll_progress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ConstantsCode.ID_CARD_PHOTO:
                    attachment_id = data.getIntExtra("attachment_id", 1);
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getVisa_datum_type().equals("1")) {
                            String[] p = Constants.setPhoto.split("#");
                            mData.get(i).setPhotoUrl(p[0]);
                            mData.get(i).setAttachment_id(attachment_id);
                            adapter.notifyDataSetChanged();
                            which = i;
                            break;
                        }
                    }
                    break;
                case ConstantsCode.PASSPORT_PHOTO:
                    attachment_id = data.getIntExtra("attachment_id", 1);
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getVisa_datum_type().equals("2")) {
                            mData.get(i).setPhotoUrl(Constants.setPhoto);
                            mData.get(i).setAttachment_id(attachment_id);
                            adapter.notifyDataSetChanged();
                            which = i;
                            break;
                        }
                    }
                    break;
            }
            if (which == 0 && (mData.get(which).getVisa_datum_type().equals("1")
                    || mData.get(which).getVisa_datum_type().equals("2"))) {
                getDatum();
            }
        }
    }



    /**
     * 压缩拍照的图片
     */
    private Bitmap compressImage(String path){
        ImageCompress compress = new ImageCompress();
        ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
        options.uri = Uri.fromFile(new File(path));
        options.maxWidth = 150;
        options.maxHeight = 150;
        Bitmap bitmap = compress.compressFromUri(DocumentScanningActivity.this, options);
        return bitmap;
    }
    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connect = true;
        } else {
            ConstantsMethod.showTip(DocumentScanningActivity.this, "网络异常");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (connect) {
            getDatum();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



}
