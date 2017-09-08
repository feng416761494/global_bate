package com.usamsl.global.index.step.step2.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.usamsl.global.R;
import com.usamsl.global.ava.activity.AvaActivity;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step1.entity.ListVisa;
import com.usamsl.global.index.step.step1.entity.VisaDetails;
import com.usamsl.global.index.step.step2.adapter.BeforeVisaAdapter;
import com.usamsl.global.index.step.step2.adapter.CustTypePeopleAdapter;
import com.usamsl.global.index.step.step2.adapter.CustTypeRecycleViewAdapter;
import com.usamsl.global.index.step.step2.adapter.DatumAdapter;
import com.usamsl.global.index.step.step2.custom.HorizontalListView;
import com.usamsl.global.index.step.step2.custom.MyScrollView;
import com.usamsl.global.index.step.step2.entity.CustTypeEntity;
import com.usamsl.global.index.step.step2.entity.DatumList;
import com.usamsl.global.index.step.step2.entity.PeopleTypeMaterial;
import com.usamsl.global.index.step.step2.entity.TypeDemand;
import com.usamsl.global.index.step.step2.entity.VisaDatumEntity;
import com.usamsl.global.index.step.step4.activity.ContactsActivity;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.FullyLinearLayoutManager;
import com.usamsl.global.view.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 时间：2016/12/23
 * 描述：签证国家的签证类型选择后，签证之前的人员选择
 */
public class BeforeVisaActivity extends AppCompatActivity implements OnClickListener,MyScrollView.OnScrollListener{
    //类型
    private TextView type;
    //金额
    private TextView money;
    //原价
    private TextView oldMoney;
    //左侧的颜色标注
    private TextView left;
    //是否拒签退全款
    private TextView advantage1;
    //材料易递交
    private TextView advantage2;
    //工作日
    private TextView tv1;
    //几年内有效
    private TextView tv2;
    // 是否可停留海关定
    private TextView tv3;
    //返回按钮
    private ImageView img_back;
    //签证助手
    private TextView tv_help;
    //产品左侧标签
    private TextView tvPresent;
    //人员类型列表
    private MyListView elv;
    //人员类型
    private List<String> typeData;
    //开始签证文本
    private TextView tv_start;
    //签证类型
    private ListVisa details;
    //是否联网
    private boolean connect = false;
    //加载到第几个
    private int cust_type = 1;
    //选择的哪一个
    private int which_cust_type = 1;
    //人员类型的recycleView
    private RecyclerView rclCustType;
    private CustTypeRecycleViewAdapter rclAdapter;
    //人员类型的集合
    private List<CustTypeEntity> custList;
    private HorizontalListView lsvContact;
    private CustTypePeopleAdapter peopleAdapter;
    private DatumAdapter datumAdapter;
    private RelativeLayout relative01,relative02;
    private MyScrollView myScrollView;
    private int topHeight;
    private RelativeLayout before_top;
    private ImageView imgShadowLeft,imgShadowRight;
    private List<VisaDatumEntity.Result> resultList = new ArrayList<>();
    //基本信息   文本和内容
    private TextView tvService,tvServiceInfo,tvRange,tvRangeInfo;
    //底部拨打电话和收藏
    private RelativeLayout relativeCallPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_visa);
        ActivityManager.getInstance().addActivity(BeforeVisaActivity.this);
        initView();
        initData();
        listener();
    }

    private void listener() {
        rclCustType.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();//完全显示的最后一条
                    int firstVisible = manager.findFirstCompletelyVisibleItemPosition();//完全显示的第一条
                    int totalItemCount = manager.getItemCount();//总条数
                    int firstVisibleItem = manager.findFirstVisibleItemPosition();//第一条
                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        imgShadowRight.setVisibility(View.INVISIBLE);
                        imgShadowLeft.bringToFront();
                        imgShadowLeft.setVisibility(View.VISIBLE);
                    }else if(firstVisible == 0 && !isSlidingToLast){
                        imgShadowLeft.setVisibility(View.INVISIBLE);
                        imgShadowRight.setVisibility(View.VISIBLE);
                        imgShadowRight.bringToFront();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dx > 0) {
                    //大于0表示正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0表示停止或向左滚动
                    isSlidingToLast = false;
                }
            }
        });
    }


    /**
     * 界面初始化
     */
    private void initView() {
        type = (TextView) findViewById(R.id.tv_type);
        money = (TextView) findViewById(R.id.tv_money);
//        advantage1 = (TextView) findViewById(R.id.tv_advantage1);
//        advantage2 = (TextView) findViewById(R.id.tv_advantage2);
        left = (TextView) findViewById(R.id.tv_left);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        oldMoney = (TextView) findViewById(R.id.tv_oldMoney);
        tvPresent = (TextView) findViewById(R.id.tvPresent);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_help = (TextView) findViewById(R.id.tv_help);
        elv = (MyListView) findViewById(R.id.elv);
        tv_start = (TextView) findViewById(R.id.tv_start);
//        lsvContact = (HorizontalListView) findViewById(R.id.lsvContact);
        relative01 = (RelativeLayout) findViewById(R.id.relative01);
        relative02 = (RelativeLayout) findViewById(R.id.relative02);
        relativeCallPhone = (RelativeLayout) findViewById(R.id.relativeCallPhone);
        myScrollView = (MyScrollView) findViewById(R.id.myScrollView);
        before_top = (RelativeLayout) findViewById(R.id.rl_body);
        imgShadowLeft = (ImageView) findViewById(R.id.imgShadow_left);
        imgShadowRight = (ImageView) findViewById(R.id.imgShadow_right);
        rclCustType = (RecyclerView) findViewById(R.id.lsvContact);
        tvService = (TextView) findViewById(R.id.tvService);
        tvServiceInfo = (TextView) findViewById(R.id.tvServiceInfo);
        tvRange = (TextView) findViewById(R.id.tvRange);
        tvRangeInfo = (TextView) findViewById(R.id.tvRangeInfo);
        //recycleView设置横向linearLayout
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rclCustType.setLayoutManager(linearLayoutManager);
        imgShadowRight.bringToFront();
        //监听设置
        myScrollView.setOnScrollListener(this);
        img_back.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_help.setOnClickListener(this);
        relativeCallPhone.setOnClickListener(this);
        elv.setFocusable(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            topHeight = before_top.getBottom();//获取before_top的顶部位置
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                this.finish();
                break;

            case R.id.tv_help://签证助手
                Intent intent = new Intent(BeforeVisaActivity.this, AvaActivity.class);
                intent.putExtra("help", true);
                startActivity(intent);
                break;

            case R.id.tv_start://开始签证
                if(Constants.USER_LOGIN){
                    Intent intentStart = new Intent(BeforeVisaActivity.this, ContactsActivity.class);
                    AddOrder addOrder = getIntent().getParcelableExtra("addOrder");
                    addOrder.setCust_type(which_cust_type);
                    intentStart.putExtra("addOrder", addOrder);
                    startActivity(intentStart);
                }else{
                    this.startActivity(new Intent(this, LoginActivity.class));
                }

                break;

            case R.id.relativeCallPhone://拨打电话
                final CustomIsFormSaveDialog dialog = new CustomIsFormSaveDialog(BeforeVisaActivity.this, "400-006518\n09:30-18:30", "人工客服","取消","确定");
                dialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doConfirm() {
                        dialog.dismiss();
                        ConstantsMethod.callDirectly(BeforeVisaActivity.this);
                    }
                });
                dialog.show();
                break;

        }
    }



    private void setAdapterClick() {
        if(rclAdapter != null){
            rclAdapter.setOnclickListener(new CustTypeRecycleViewAdapter.CustTypeRecycleViewClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(view.getId() == R.id.tvCustType){
                        rclAdapter.clickItem(position);
                        rclAdapter.notifyDataSetChanged();
                        which_cust_type = Integer.parseInt(custList.get(position).getTypeNumber());
                        getFilterData(custList.get(position).getTypeNumber());
                    }
                }
            });
        }
    }



    @Override
    public void onScroll(int scrollY) {
        if(scrollY >= topHeight){
            if (rclCustType.getParent()!=relative02) {
                relative01.removeView(rclCustType);
                relative02.addView(rclCustType);
            }
        }else{
            if (rclCustType.getParent()!=relative01) {
                relative02.removeView(rclCustType);
                relative01.addView(rclCustType);
            }
        }
    }



    private void getFilterData(String typeNumber) {
        List<VisaDatumEntity.Result> list = new ArrayList<>();
        if(ObjectIsNullUtils.TextIsNull(typeNumber)){
            for(int i=0;i<resultList.size();i++){
                //横向listView选中的人员类型
                if(resultList.get(i).getCust_type() == Integer.parseInt(typeNumber)){
                    list.add(resultList.get(i));
                }
            }
        }
        //交给adapter显示材料的需求内容
        if(list != null && list.size() > 0){
            datumAdapter = new DatumAdapter(this,list);
            elv.setAdapter(datumAdapter);
        }
    }


    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connect = true;
        } else {
            ConstantsMethod.showTip(BeforeVisaActivity.this, "无网络连接");
        }
    }


    /**
     * 数据初始化
     */
    private void initData() {
        connectWork();
        details = (ListVisa) getIntent().getSerializableExtra("detail");
        initDetailType();
        //人员类型数据
        typeData = new ArrayList<>();
//        beforeVisaAdapter = new BeforeVisaAdapter(this, typeData, mData);
        if (connect) {
            initConnectData();
        }
    }

    /**
     * 签证类型数据初始化
     */
    private void initDetailType() {
        money.setText("￥" + details.getPreferential_price());
        if (details.getPrice() >= 0) {
            oldMoney.setVisibility(View.VISIBLE);
            oldMoney.setText("￥" + details.getPrice());
        }else {
            oldMoney.setVisibility(View.INVISIBLE);
        }
        tvPresent.setText(details.getVisa_label());
        //中间加条横线
        oldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        type.setText(details.getVisa_name() + "-" + details.getVisa_area_name());
        tv1.setText("预计" + details.getPlan_weekday() + "个工作日");
        if (!details.getValidity_time().equals("")) {
            tv2.setText(details.getValidity_time());
        } else {
            tv2.setVisibility(View.INVISIBLE);
        }
        if (!details.getIs_stop_set().equals("")) {
            tv3.setText(details.getIs_stop_set());
        } else {
            tv3.setVisibility(View.INVISIBLE);
        }
    }




    /**
     * 联网之后数据加载
     */
    private void initConnectData() {
//        new ProgressBarLoadUtils(this).startProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }


    OkHttpClient client = OkHttpManager.myClient();
    public void getData() {
        String url = UrlSet.loadVisaDatumAll2 + "&visa_id=" + details.getVisa_id();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final VisaDatumEntity typeDemand = gson.fromJson(str, VisaDatumEntity.class);
                if(ObjectIsNullUtils.objectIsNull(typeDemand)){
                    switch (typeDemand.getError_code()){
                        case 0:
                            resultList = typeDemand.getResult();
                            custList = new ArrayList<CustTypeEntity>();
                            for(int i=0;i<typeDemand.getResult().size();i++){
                                CustTypeEntity entity = new CustTypeEntity();
                                if(typeDemand.getResult().get(i).getCust_type() == 1){
                                    entity.setTypeName("在职人员");
                                    entity.setTypeNumber(String.valueOf(typeDemand.getResult().get(i).getCust_type()));
                                }else if(typeDemand.getResult().get(i).getCust_type() == 2){
                                    entity.setTypeName("自由职业者");
                                    entity.setTypeNumber(String.valueOf(typeDemand.getResult().get(i).getCust_type()));
                                }else if(typeDemand.getResult().get(i).getCust_type() == 3){
                                    entity.setTypeName("学龄前儿童");
                                    entity.setTypeNumber(String.valueOf(typeDemand.getResult().get(i).getCust_type()));
                                }else if(typeDemand.getResult().get(i).getCust_type() == 4){
                                    entity.setTypeName("学生");
                                    entity.setTypeNumber(String.valueOf(typeDemand.getResult().get(i).getCust_type()));
                                }else if(typeDemand.getResult().get(i).getCust_type() == 5){
                                    entity.setTypeName("退休人员");
                                    entity.setTypeNumber(String.valueOf(typeDemand.getResult().get(i).getCust_type()));
                                }
                                custList.add(entity);
                            }
                            //横向listView用于显示人员类型
                            if(custList != null && custList.size()>0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(custList != null && custList.size()>0){
                                            rclAdapter = new CustTypeRecycleViewAdapter(custList,BeforeVisaActivity.this);
                                            rclCustType.setAdapter(rclAdapter);
                                            getFilterData(custList.get(0).getTypeNumber());
                                            setBasicInfo(typeDemand.getVisa());
                                            setAdapterClick();
                                        }
                                    }
                                });
                            }
                            break;

                        case 1:

                            break;
                    }
                }
            }
        });
    }

    /**
     * 设置基本信息
     * @param visaBean
     */
    private void setBasicInfo(VisaDatumEntity.Visa visaBean) {
        if(ObjectIsNullUtils.objectIsNull(visaBean)){
            tvServiceInfo.setText(visaBean.getService_desc());
            //范围信息换行显示
            tvRangeInfo.setText(visaBean.getRange_desc().replace("#","\n"));
        }
    }


    public boolean isListViewReachTopEdge(final HorizontalListView listView) {
        boolean result=false;
        if(listView.getFirstVisiblePosition()==0){
            final View topChildView = listView.getChildAt(0);
            result=topChildView.getTop()==0;
        }
        return result ;
    }

    private List<String> getTypeMaterialDemand(String[] typeMater) {
        List<String> list = new ArrayList<>();
        if (typeMater.length > 1) {
            for (String s : typeMater) {
                list.add(s);
            }
        } else if (typeMater.length == 1) {
            list.add(typeMater[0]);
        }
        return list;
    }


    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(BeforeVisaActivity.this);
        super.onDestroy();
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
