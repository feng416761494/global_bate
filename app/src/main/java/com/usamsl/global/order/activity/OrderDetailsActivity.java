package com.usamsl.global.order.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.activity.EvUsActivity;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step2.adapter.DatumAdapter;
import com.usamsl.global.index.step.step5.activity.DSActivity;
import com.usamsl.global.index.step.step5.activity.DocumentScanningActivity;
import com.usamsl.global.index.step.step6.activity.PaymentActivity;
import com.usamsl.global.index.step.step7.activity.PhotoSubmissionActivity;
import com.usamsl.global.index.step.step7.entity.OrderDatum;
import com.usamsl.global.index.step.step8.InterviewTimeActivity;
import com.usamsl.global.my.entity.AllOrder;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.order.adapter.OrderMaterialRecyclerViewAdapter;
import com.usamsl.global.order.entity.BespeakTime;
import com.usamsl.global.order.entity.FormSchedule;
import com.usamsl.global.order.entity.OrderMaterialData;
import com.usamsl.global.order.util.MyLayoutManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 时间：2016/12/21
 * 描述：订单详情界面
 */
public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    //材料
    private List<OrderMaterialData> mData;
    private RecyclerView recyclerView;
    private OrderMaterialRecyclerViewAdapter adapter;
    //返回按钮
    private ImageView img_back;
    //订单详情
    private AllOrder.ResultBean allOrder;
    //产品名称
    private TextView tv_orderNumber;
    //订单编号
    private TextView tv_orderCode;
    //姓名
    private TextView tv_name;
    //身份证号
    private TextView tv_idCard;
    //类型
    private TextView tv_type;
    //证件照
    private ImageView img_photo;
    //继续
    private TextView tvContinue;
    private TextView tvForm;
    private TextView tvHint;
    private LinearLayout linearRecycle;
    private boolean connection = false;
    private AddOrder addOrder;
    private FormSchedule form;
    private OrderStatusChangedReceiver receiver;
    //是否是已完成订单(0:未完成，1是已完成）
    private int finish = 1;
    //预约面试时间
    private RelativeLayout rl_time;
    private TextView tv_time1, tv_time2, tv_time3;
    private OrderDatum lastDatum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ActivityManager.getInstance().addActivity(OrderDetailsActivity.this);
        initView();
        initDetailsData();
        getData();
        toListener();
        receiver = new OrderStatusChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ORDER_STATUS_CHANGED");
        filter.addAction(Constants.PHOTO_UPLOAD_SUCCESS);
        this.registerReceiver(receiver, filter);
    }

    /**
     * 界面初始化
     */
    private void initView() {
        rl_time = (RelativeLayout) findViewById(R.id.relTime);
        tv_time1 = (TextView) findViewById(R.id.tv_time1);
        tv_time2 = (TextView) findViewById(R.id.tv_time2);
        tv_time3 = (TextView) findViewById(R.id.tv_time3);
        tv_orderCode = (TextView) findViewById(R.id.tv_orderCode);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_idCard = (TextView) findViewById(R.id.tv_idCard);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_orderNumber = (TextView) findViewById(R.id.tv_orderNumber);
        tv_type = (TextView) findViewById(R.id.tv_type);
        img_photo = (ImageView) findViewById(R.id.img_photo);
        tvContinue = (TextView) findViewById(R.id.tvContinue);
        tvForm = (TextView) findViewById(R.id.tvForm);
        tvHint = (TextView) findViewById(R.id.tvHint);
        linearRecycle = (LinearLayout) findViewById(R.id.linearRecycle);
        tvContinue.setOnClickListener(this);
        tvForm.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //该两个方法放在onResume中是为了当页面于用户交互时可以获取最新的订单信息
        connectWork();
        if (connection) {
            getFormSchedule();
            getBespeakTime();
            getContinue();
        }
        PgyCrashManager.register(this);
    }

    //预约面试时间
    private void getBespeakTime() {
        String url = UrlSet.orderBespeakTime + allOrder.getOrder_id();
        OkHttpClient client = OkHttpManager.myClient();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstantsMethod.showTip(OrderDetailsActivity.this, "网络异常！");
                        rl_time.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final BespeakTime bespeakTime = gson.fromJson(result, BespeakTime.class);
                if(ObjectIsNullUtils.objectIsNull(bespeakTime)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (bespeakTime.getError_code()) {
                                case 0:
                                    BespeakTime.ResultBean bean = bespeakTime.getResult();
                                    if (bean.getBespeak_time() != null && !bean.getBespeak_time().equals("")) {
                                        rl_time.setVisibility(View.VISIBLE);
                                        tv_time1.setText(bean.getBespeak_time());
                                        tv_time2.setText(bean.getBespeak_time1());
                                        tv_time3.setText(bean.getBespeak_time2());
                                    } else {
                                        rl_time.setVisibility(View.GONE);
                                    }
                                    break;
                                case 1:
                                    rl_time.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 材料数据初始化
     */
    private void initDetailsData() {
        allOrder = getIntent().getParcelableExtra("order");
        addOrder = getIntent().getParcelableExtra("addOrder");
        finish = getIntent().getIntExtra("finish", 1);
        if (addOrder == null) {
            addOrder = new AddOrder();
            addOrder.setContact_id(allOrder.getContact_id());
            addOrder.setContact_name(allOrder.getContact_name());
            addOrder.setVisa_id(allOrder.getVisa_id());
        }
        //设置国家签证和来自EVUS的控件显示
        if (allOrder.getVisa_area_name() != null &&
                !allOrder.getVisa_area_name().equals("") &&
                !allOrder.getVisa_area_name().equals(" ")) {
            tv_orderNumber.setText("产品名称：" + allOrder.getVisa_name() + "-" + allOrder.getVisa_area_name());
            tvForm.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.VISIBLE);
            linearRecycle.setVisibility(View.VISIBLE);
        } else {
            if (allOrder.getIs_pay() == 1) {
                tvContinue.setEnabled(false);
                tvContinue.setBackgroundResource(R.drawable.before_visa_start_bg1);
            }
            tv_orderNumber.setText("产品名称：" + allOrder.getVisa_name());
            tvHint.setVisibility(View.GONE);
            linearRecycle.setVisibility(View.GONE);
        }
        /*//继续是否可以点击,在onResume中有写
        //如果订单处于领取签证这一步，则按钮不可点击
        if (allOrder.getOrder_status() >= 4) {
            tvContinue.setEnabled(false);
            tvContinue.setBackgroundResource(R.drawable.before_visa_start_bg1);
        } else {
            if (allOrder.getVisa_id() > 1) {
                tvContinue.setEnabled(true);
                tvContinue.setBackgroundResource(R.drawable.before_visa_start_bg2);
            } else {
                if (allOrder.getIs_pay() == 1) {
                    tvContinue.setEnabled(false);
                    tvContinue.setBackgroundResource(R.drawable.before_visa_start_bg1);
                }
            }

        }*/

        //设置订单详情的联系人姓名
        if (allOrder.getContact_name() != null && !allOrder.getContact_name().equals("")) {
            tv_name.setText("姓名：" + allOrder.getContact_name());
        }
        //订单的网店电话和网店地址如果是空的话不显示
        if (allOrder.getFixed_line() == null || allOrder.getFixed_line().equals("")) {
            tv_type.setVisibility(View.GONE);
        } else {
            tv_type.setText("网点电话：" + allOrder.getFixed_line());
        }

        if (allOrder.getAddress() == null || allOrder.getAddress().equals("")) {
            tv_idCard.setVisibility(View.GONE);
        } else {
            tv_idCard.setText("网点地址：" + allOrder.getAddress());
        }
        if (allOrder.getOrder_code() == null || allOrder.getOrder_code().equals("")) {
            tv_orderCode.setVisibility(View.GONE);
        } else {
            tv_orderCode.setText("订单编号：" + allOrder.getOrder_code());
        }

        if (allOrder.getHead_url() != null) {
            Picasso.with(this)
                    .load(allOrder.getHead_url())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.user_off)
                    .into(img_photo);
        }
        mData = new ArrayList<>();
        //设置布局管理器
        MyLayoutManager layoutManager = new MyLayoutManager();
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        //设置适配器
        adapter = new OrderMaterialRecyclerViewAdapter(this, mData);
        Constants.STATUS = allOrder.getOrder_status();
        if(Constants.STATUS >= 2 || Constants.IS_PAY == 1){
            tvForm.setBackgroundResource(R.drawable.order_details_flow_bg2);
            tvForm.setTextColor(Color.parseColor("#aaaaaa"));
            tvForm.setEnabled(false);
        }else{
            tvForm.setBackgroundResource(R.drawable.order_details_flow_bg1);
            tvForm.setTextColor(Color.parseColor("#bcd78e"));
            tvForm.setEnabled(true);
        }
        adapter.setDetailsItemClickListener(new OrderMaterialRecyclerViewAdapter.OrderDetailsItemClickListener() {
            @Override
            public void detailsItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.tv_data:
                        if (mData.get(position).isFilled() && finish == 0 && Constants.connect){
                            if (Constants.STATUS >= 2 || Constants.IS_PAY == 1){
                                //线上付款或者线下付款的订单，不做任何处理
                            }else{
                                int datumType = Integer.parseInt(mData.get(position).getDatumType());
                                //根据材料类型跳转页面
                                switch (datumType) {
                                    case 1://身份证跳转至证件扫描
                                    case 2:
                                        Intent intent1 = new Intent(OrderDetailsActivity.this, DocumentScanningActivity.class);
                                        intent1.putExtra("addOrder", addOrder);
                                        intent1.putExtra("order_id", allOrder.getOrder_id());
                                        startActivity(intent1);
                                        break;
                                    case 3:
                                    case 5:
                                        //继续订单
                                        continueOrder(Constants.STATUS);
                                        break;
                                }
                            }
                        }else if (mData.get(position).isFilled() && finish == 0 && !Constants.connect) {
                            ConstantsMethod.showTip(OrderDetailsActivity.this, "网络异常");
                        }
                        break;
                }
            }
        });


        /*//adapter中的控件点击监听
        adapter.setDetailsItemClickListener(new OrderMaterialRecyclerViewAdapter.OrderDetailsItemClickListener() {
            @Override
            public void detailsItemClick(View view, int position) {
                if (mData.get(position).isFilled() && finish == 0 && Constants.connect) {
                    if (Constants.STATUS >= 2 || Constants.IS_PAY == 1) {
                        int datumType = Integer.parseInt(mData.get(position).getDatumType());
                        //根据材料类型跳转页面
                        switch (datumType) {
                            case 1://身份证跳转至证件扫描
                            case 2://护照跳转至证件扫描
//                                Intent intent2 = new Intent(OrderDetailsActivity.this, DocumentScanningActivity.class);
//                                intent2.putExtra("addOrder", addOrder);
//                                intent2.putExtra("order_id", allOrder.getOrder_id());
//                                startActivity(intent2);
                                break;
                            case 4:
                                break;
                            case 3://跳转至材料提交
                            case 5://跳转至材料提交
//                                Intent intent5 = new Intent(OrderDetailsActivity.this, PhotoSubmissionActivity.class);
//                                intent5.putExtra("addOrder", addOrder);
//                                intent5.putExtra("order_id", allOrder.getOrder_id());
//                                startActivity(intent5);
                                break;
                        }
                    } else {
                        int datumType = Integer.parseInt(mData.get(position).getDatumType());
                        //根据材料类型跳转页面
                        switch (datumType) {
                            case 1://身份证跳转至证件扫描
                            case 2:
                                Intent intent1 = new Intent(OrderDetailsActivity.this, DocumentScanningActivity.class);
                                intent1.putExtra("addOrder", addOrder);
                                intent1.putExtra("order_id", allOrder.getOrder_id());
                                startActivity(intent1);
                                break;
                            case 3:
                            case 5:
                                //继续订单
                                continueOrder(Constants.STATUS);
                                break;
                        }
                    }

                } else if (mData.get(position).isFilled() && finish == 0 && !Constants.connect) {
                    ConstantsMethod.showTip(OrderDetailsActivity.this, "网络异常");
                }
            }
        });*/
    }

    //继续是否可以点击
    private void getContinue() {
        //如果订单处于领取签证这一步，则按钮不可点击
        if (Constants.STATUS >= 4) {
            tvContinue.setVisibility(View.GONE);
            tvContinue.setEnabled(false);
            tvContinue.setBackgroundResource(R.drawable.before_visa_start_bg1);
        } else {
            if (allOrder.getVisa_id() > 1) {
                tvContinue.setEnabled(true);
                tvContinue.setBackgroundResource(R.drawable.before_visa_start_bg2);
            } else {
                if (Constants.STATUS >= 2) {
                    tvContinue.setVisibility(View.GONE);
                    tvContinue.setEnabled(false);
                    tvContinue.setBackgroundResource(R.drawable.before_visa_start_bg1);
                }
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContinue://继续
                //继续订单
                if (Constants.connect) {
                    continueOrder(Constants.STATUS);
                } else {
                    ConstantsMethod.showTip(OrderDetailsActivity.this, "网络异常");
                }
                break;

            case R.id.tvForm://进入表单
                //进入表单以及，查看表单进度
                goToForm();
                break;
        }
    }


    private void goToForm() {
        switch (form.getError_code()) {
            case 0://成功获取
                //该订单来自国家签证
                if (addOrder.getVisa_id() > 1) {
                    if (form != null) {
                        //身份证和护照都提交完了
                        boolean cardFill = false;
                        boolean passportFill = false;
                        for(OrderDatum.ResultBean resultBean : lastDatum.getResult()){
                            if(resultBean.getVisa_datum_type().equals("1")){
                                if(resultBean.getAttachment_id() > 0){
                                    cardFill = true;
                                }
                            }else if(resultBean.getVisa_datum_type().equals("2")){
                                if(resultBean.getAttachment_id() > 0){
                                    passportFill = true;
                                }
                            }
                        }
                        //表示用户还没有提交过身份证和护照，跳转至证件扫描页面(根据材料判断)
                        if (!cardFill || !passportFill) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ConstantsMethod.showTip(OrderDetailsActivity.this, "请完善您的资料！");
                                    Intent intent = new Intent(OrderDetailsActivity.this, DocumentScanningActivity.class);
                                    intent.putExtra("addOrder", addOrder);
                                    intent.putExtra("order_id", allOrder.getOrder_id());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //表单的非空判断
                            if (form != null && finish == 0) {
                                String formUrl = form.getResult().getForm_url();
                                Intent intent = new Intent(OrderDetailsActivity.this, DSActivity.class);
                                intent.putExtra("addOrder", addOrder);
                                intent.putExtra("order_id", allOrder.getOrder_id());
                                intent.putExtra("formUrl", formUrl);
                                startActivity(intent);
                            }
                        }
                    }
                    //该订单来自EVUS
                } else {
                    if (form.getResult().getIs_show() != null) {
                        //如果不是1，则代表之前订单进入了Evus表单中
                        if (!form.getResult().getIs_show().equals("1")) {
                            String formUrl = form.getResult().getForm_url();
                            Intent intent = new Intent(OrderDetailsActivity.this, EvUsActivity.class);
                            intent.putExtra("addOrder", addOrder);
                            intent.putExtra("order_id", allOrder.getOrder_id());
                            intent.putExtra("formUrl", formUrl);
                            intent.putExtra("evus", "1");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(OrderDetailsActivity.this, EvUsActivity.class);
                            intent.putExtra("addOrder", addOrder);
                            intent.putExtra("order_id", allOrder.getOrder_id());
                            intent.putExtra("evus", "1");
                            startActivity(intent);
                        }
                    }

                }
                break;
        }
    }


    /**
     * 进入表单以及，查看表单进度
     */
    private void getFormSchedule() {

        String url = UrlSet.formSchedule + allOrder.getOrder_id();
        OkHttpClient client = OkHttpManager.myClient();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstantsMethod.showTip(OrderDetailsActivity.this, "网络异常！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                FormSchedule form = gson.fromJson(result, FormSchedule.class);
                OrderDetailsActivity.this.form = form;
            }
        });
    }


    /**
     * 继续订单创建
     *
     * @param orderStatus
     */
    private void continueOrder(int orderStatus) {
        AddOrder addOrder = new AddOrder();
        addOrder.setCust_type(allOrder.getCust_type());
        addOrder.setVisa_id(allOrder.getVisa_id());
        addOrder.setContact_name(allOrder.getContact_name());
        addOrder.setContact_id(allOrder.getContact_id());
        Constants.SPEC_KEY = allOrder.getPhoto_format();
        Constants.COUNTRY_ID = allOrder.getCountry_id();
        try {
            Constants.travelDate = ConstantsMethod.longToDate(allOrder.getDepart_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        UrlSet.countrySurface = allOrder.getForm_url();
        Constants.COUNTRY = allOrder.getCountry_name();
        if (allOrder.getVisa_id() > 1) { //国家签证(非EVUS)
            switch (orderStatus) {
                case 0:
                    if (form != null) {
                        //身份证和护照都提交完了
                        boolean cardFill = false;
                        boolean passportFill = false;
                        for(OrderDatum.ResultBean resultBean : lastDatum.getResult()){
                            if(resultBean.getVisa_datum_type().equals("1")){
                                if(resultBean.getAttachment_id() > 0){
                                    cardFill = true;
                                }
                            }else if(resultBean.getVisa_datum_type().equals("2")){
                                if(resultBean.getAttachment_id() > 0){
                                    passportFill = true;
                                }
                            }
                        }
                        //表示用户还没有提交过身份证和护照，跳转至证件扫描页面
                        if (!cardFill || !passportFill) {
//                            String formUrl = form.getResult().getRequest_url();
                            String formUrl = form.getResult().getForm_url();
                            Intent intent = new Intent(this, DocumentScanningActivity.class);
                            intent.putExtra("addOrder", addOrder);
                            intent.putExtra("order_id", allOrder.getOrder_id());
                            intent.putExtra("formUrl", formUrl);
                            startActivity(intent);
                            //已经填写了表单，进入表单的进度
                        } else {
                            String formUrl = form.getResult().getRequest_url();
                            Intent intent = new Intent(OrderDetailsActivity.this, DSActivity.class);
                            intent.putExtra("addOrder", addOrder);
                            intent.putExtra("order_id", allOrder.getOrder_id());
                            intent.putExtra("formUrl", formUrl);
                            startActivity(intent);
                        }
                    }

                    break;
                case 1:
                    if (allOrder.getIs_pay() == 1) {
                        Intent intentSubmission = new Intent(this, PhotoSubmissionActivity.class);
                        intentSubmission.putExtra("addOrder", addOrder);
                        intentSubmission.putExtra("order_id", allOrder.getOrder_id());
                        startActivity(intentSubmission);
                    } else if (allOrder.getIs_pay() == 0) {
                        Intent intent = new Intent(this, PaymentActivity.class);
                        //跳转支付界面
                        intent.putExtra("addOrder", addOrder);
                        intent.putExtra("order_id", allOrder.getOrder_id());
                        startActivity(intent);
                    }
                    break;
                case 2:
                    //跳转提交照片材料界面
                    Intent intent1 = new Intent(this, PhotoSubmissionActivity.class);
                    intent1.putExtra("addOrder", addOrder);
                    intent1.putExtra("order_id", allOrder.getOrder_id());
                    startActivity(intent1);
                    break;
                case 3:
                    //跳转预约时间界面
                    Intent intent2 = new Intent(this, InterviewTimeActivity.class);
                    intent2.putExtra("order_id", allOrder.getOrder_id());
                    startActivity(intent2);
                    break;
            }
        } else {//此处为EVUS签证

            switch (orderStatus) {
                case 0:
                    if (form != null) {
                        //如果不是1，则代表之前订单进入了Evus表单中
                        if (form.getResult().getIs_show().equals("1")) {
                            String formUrl = form.getResult().getForm_url();
                            Intent intent = new Intent(OrderDetailsActivity.this, EvUsActivity.class);
                            intent.putExtra("addOrder", addOrder);
                            intent.putExtra("order_id", allOrder.getOrder_id());
                            intent.putExtra("formUrl", formUrl);
                            intent.putExtra("evus", "1");
                            startActivity(intent);
                        } else {
//                            Intent intent  = new Intent(OrderDetailsActivity.this, EvUsActivity.class);
//                            intent.putExtra("addOrder", addOrder);
//                            intent.putExtra("order_id", allOrder.getOrder_id());
//                            intent.putExtra("evus","1");
//                            startActivity(intent);
                        }
                    }
                    break;
                case 1:

                    /*if(allOrder.getIs_pay() == 1){
                        Intent intentSubmission = new Intent(this, PhotoSubmissionActivity.class);
                        intentSubmission.putExtra("addOrder", addOrder);
                        intentSubmission.putExtra("order_id", allOrder.getOrder_id());
                        startActivity(intentSubmission);
                    }else*/
                    if (allOrder.getIs_pay() == 0) {
                        //跳转支付界面
                        Intent intent = new Intent(this, PaymentActivity.class);
                        intent.putExtra("addOrder", addOrder);
                        intent.putExtra("order_id", allOrder.getOrder_id());
                        intent.putExtra("evus", "1");
                        startActivity(intent);
                    }
                    break;
            }

        }


    }


    /**
     * 加载材料
     */
    private void getData() {

        new ProgressBarLoadUtils(this).startProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get请求上传
                String url = UrlSet.orderDatum + allOrder.getOrder_id();
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(url);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                                 @Override
                                 public void onFailure(Call call, IOException e) {

                                     runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             ConstantsMethod.showTip(OrderDetailsActivity.this, "网络异常！");
                                             new ProgressBarLoadUtils(OrderDetailsActivity.this).stopProgressDialog();
                                         }
                                     });
                                 }

                                 @Override
                                 public void onResponse(final Call call, Response response) throws IOException {
                                     String str = response.body().string();
                                     Gson gson = new Gson();
                                     final OrderDatum orderDatum = gson.fromJson(str, OrderDatum.class);
                                     OrderDetailsActivity.this.lastDatum = orderDatum;
                                     runOnUiThread(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           new ProgressBarLoadUtils(OrderDetailsActivity.this).stopProgressDialog();
                                                           switch (orderDatum.getError_code()) {
                                                               case 0:
                                                                   if (mData != null || mData.size() != 0) {
                                                                       mData.clear();
                                                                   }
                                                                   int num = -1, num1 = -1;
                                                                   List<OrderDatum.ResultBean> bean = orderDatum.getResult();
                                                                   for (int i = 0; i < bean.size(); i++) {
                                                                       OrderMaterialData orderMaterialData = new OrderMaterialData();
                                                                       orderMaterialData.setMaterialName(bean.get(i).getVisa_datum_name());
                                                                       orderMaterialData.setDatumType(bean.get(i).getVisa_datum_type());
                                                                       if (num < 0 && bean.get(i).getVisa_datum_type().equals("1")) {
                                                                           if (bean.get(i).getAttachment_id() > 0) {
                                                                               orderMaterialData.setFilled(true);
                                                                           } else {
                                                                               orderMaterialData.setFilled(false);
                                                                           }
                                                                           mData.add(orderMaterialData);
                                                                       }
                                                                       if (num1 < 0 && bean.get(i).getVisa_datum_type().equals("5")) {
                                                                           if(Constants.STATUS >= 2 || Constants.IS_PAY == 1){
                                                                               if (bean.get(i).getAttachment_id() > 0) {
                                                                                   orderMaterialData.setFilled(true);
                                                                               } else {
                                                                                   orderMaterialData.setFilled(false);
                                                                               }
                                                                           }
                                                                           mData.add(orderMaterialData);
                                                                       }
                                                                       if (bean.get(i).getVisa_datum_type().equals("1")) {
                                                                           num = i;
                                                                       } else if (bean.get(i).getVisa_datum_type().equals("5")) {
                                                                           num1 = i;
                                                                       } else if (bean.get(i).getVisa_datum_type().equals("2")) {
                                                                           if (bean.get(i).getAttachment_id() > 0) {
                                                                               orderMaterialData.setFilled(true);
                                                                           } else {
                                                                               orderMaterialData.setFilled(false);
                                                                           }
                                                                           mData.add(orderMaterialData);
                                                                       } else {
                                                                           if(Constants.STATUS >= 2 || Constants.IS_PAY == 1){
                                                                               if (bean.get(i).getAttachment_id() > 0) {
                                                                                   orderMaterialData.setFilled(true);
                                                                               } else {
                                                                                   orderMaterialData.setFilled(false);
                                                                               }
                                                                           }
                                                                           mData.add(orderMaterialData);
                                                                       }

                                                                   }
//                                                                   adapter.setData(mData);
//                                                                   mData = mData.subList(0,3);
//                                                                   adapter = new OrderMaterialRecyclerViewAdapter(OrderDetailsActivity.this,mData);
                                                                   recyclerView.setAdapter(adapter);
//                                                                   adapter.notifyDataSetChanged();
                                                                   break;
                                                           }
                                                       }
                                                   }
                                     );
                                 }
                             }
                );
            }
        }).start();

    }

    class OrderStatusChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getExtras().getString("ORDER_STATUS");
            String VISA_DATUM_TYPE = intent.getExtras().getString("Visa_datum_type");
            if (result != null && !result.equals("")) {
                if (result.equals("IdCard_passPort")) {
                    tvForm.setBackgroundResource(R.drawable.order_details_flow_bg1);
                    tvForm.setTextColor(Color.parseColor("#bcd78e"));
                    tvForm.setEnabled(true);
                } else if (result.equals("H5Form")) {
                    allOrder.setOrder_status(1);

                } else if (result.equals("Account_Photo")) {
                    allOrder.setOrder_status(3);

                } else if (result.equals("orderPay")) {
                    allOrder.setOrder_status(2);

                } else if (result.equals("InterviewTime")) {
                    allOrder.setOrder_status(4);
                    tvContinue.setEnabled(false);
                    tvContinue.setBackgroundResource(R.drawable.before_visa_start_bg1);
                }
            }
            if(ObjectIsNullUtils.TextIsNull(VISA_DATUM_TYPE)){
                for(int i=0;i<mData.size();i++){
                    if(mData.get(i).getDatumType().equals(VISA_DATUM_TYPE)){
                        mData.get(i).setFilled(true);
                    }
                }
                adapter.setData(mData);
                adapter.notifyDataSetChanged();
//                if(VISA_DATUM_TYPE.equals("5")){//户口本
//                    for(int i=0;i<mData.size();i++){
//                        if(mData.get(i).getDatumType().equals(VISA_DATUM_TYPE)){
//                            mData.get(i).setFilled(true);
//                        }
//                    }
//                    adapter.setData(mData);
//                    adapter.notifyDataSetChanged();
//                }else if(VISA_DATUM_TYPE.equals("1")){
//                    for(int i=0;i<mData.size();i++){
//                        if(mData.get(i).getDatumType().equals(VISA_DATUM_TYPE)){
//                            mData.get(i).setFilled(true);
//                        }
//                    }
//                    adapter.setData(mData);
//                    adapter.notifyDataSetChanged();
//                }
            }
            Constants.STATUS = allOrder.getOrder_status();
        }
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
    }



    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connection = true;
        } else {
            connection = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new ProgressBarLoadUtils(OrderDetailsActivity.this).stopProgressDialog();
        this.unregisterReceiver(receiver);
        PgyCrashManager.unregister();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PgyCrashManager.unregister();
    }
}
