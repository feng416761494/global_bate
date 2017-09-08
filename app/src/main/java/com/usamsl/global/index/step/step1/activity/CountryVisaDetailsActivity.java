package com.usamsl.global.index.step.step1.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.sunflower.FlowerCollector;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.entity.VisaCountry;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step1.adapter.CountryVisaDetailsAdapter;
import com.usamsl.global.index.step.step1.adapter.VisaAreaAdapter;
import com.usamsl.global.index.step.step1.adapter.VisaProductAdapter;
import com.usamsl.global.index.step.step1.biz.CountryVisaDetailBiz;
import com.usamsl.global.index.step.step1.entity.District;
import com.usamsl.global.index.step.step1.entity.ListVisa;
import com.usamsl.global.index.step.step1.entity.VisaAllEntity;
import com.usamsl.global.index.step.step1.entity.VisaAreaEntity;
import com.usamsl.global.index.step.step1.entity.VisaDetails;
import com.usamsl.global.index.step.step1.util.AbstractWheel;
import com.usamsl.global.index.step.step1.util.OnWheelChangedListener;
import com.usamsl.global.index.step.step1.util.adapters.ArrayWheelAdapter;
import com.usamsl.global.index.step.step2.activity.BeforeVisaActivity;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.StatusBarUtils;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.manager.AvaManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.MyListView;
import com.usamsl.global.view.RecyclerImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2016/12/23
 * 描述：国家签证详情
 */
public class CountryVisaDetailsActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    //返回按钮
    private ImageView img_back;
    //签证显示
    private MyListView lv;
    //城市，国家,领区
    private String city;
    private int country;
    private String ava;
    //ava获取到的国家
    private VisaCountry.ResultBean countrys;
    private String TAG = CountryVisaDetailsActivity.class.getSimpleName();
    //哪国签证
    private TextView tv_country;
    //语音播放助手
    private SpeechSynthesizer mTts;
    //只播放一次
    private boolean one = true;
    private CountryVisaDetailBiz visaDetailBiz;
    //Gallery  为选择领取的控件
    private Gallery galleryVisaArea;
    //Gallery 滑动的list
    private List<VisaAreaEntity> areaList;
    //领取图片的adapter
    private VisaAreaAdapter areaAdapter;
    //获取到的该国家的所有的产品
    private VisaAllEntity visaEntity;
    //领取产品的Adapter
    private VisaProductAdapter productAdapter;
    //对应领取的产品列表
    private List<ListVisa> listVisa;
    //传递到下一个页面的 VISA_AREA_ID
    private int VISA_AREA_ID = -1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && countrys != null && ava != null && one) {
                AvaManager.initTts(CountryVisaDetailsActivity.this, mTts, "开始" + Constants.COUNTRY + "签证");
                one = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_visa_details);
        ActivityManager.getInstance().addActivity(CountryVisaDetailsActivity.this);
        //业务层的了biz类，用于请求数据
        visaDetailBiz = new CountryVisaDetailBiz(CountryVisaDetailsActivity.this);
        initView();
        initData();

    }

    /**
     * 界面初始化
     */
    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        lv = (MyListView) findViewById(R.id.lv);
        tv_country = (TextView) findViewById(R.id.tv_country);
        galleryVisaArea = (Gallery) findViewById(R.id.galleryVisaArea);
        galleryVisaArea.setSpacing(3);
        lv.setFocusable(false);
        galleryVisaArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaAdapter.selectPosition(position);
                VISA_AREA_ID = areaList.get(position).getVisaAreaID();
                //获取对应的领取产品
                getVisaAreaProduct(areaList.get(position).getVisaAreaID());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        lv.setOnItemClickListener(this);
        img_back.setOnClickListener(this);
    }



    /**
     * 筛选出来领取对应的产品
     * @param visaAreaID
     */
    private void getVisaAreaProduct(int visaAreaID) {
        for(VisaAllEntity.Result result : visaEntity.getResult()){
            if(ObjectIsNullUtils.objectIsNull(result)){
                if(result.getVisa_area_id() == visaAreaID){
                    listVisa = result.getListVisa();
                    if(listVisa != null && listVisa.size() > 0){
                        productAdapter = new VisaProductAdapter(this,listVisa);
                        lv.setAdapter(productAdapter);
                    }
                }
            }
        }
    }

    /**
     * 数据初始化
     */
    private void initData() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(CountryVisaDetailsActivity.this, null);
        tv_country.setText(Constants.COUNTRY + "签证");
        city = getIntent().getStringExtra("city");
        country = getIntent().getIntExtra("country", 11);
        ava = getIntent().getStringExtra("ava");
        countrys = getIntent().getParcelableExtra("countrys");
        //首页定位获取到的省份名称
        if(!ObjectIsNullUtils.TextIsNull(city)){
            city = "";
        }
        visaDetailBiz.loadVisaAll(country,city);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.img_back){
            this.finish();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constants.plan_weekday = listVisa.get(position).getPlan_weekday();
        Intent intent = new Intent(CountryVisaDetailsActivity.this, BeforeVisaActivity.class);
        AddOrder addOrder = new AddOrder();
        addOrder.setVisa_id(listVisa.get(position).getVisa_id());
        if(VISA_AREA_ID != -1){
            addOrder.setVisa_area_id(VISA_AREA_ID);
        }
        intent.putExtra("addOrder", addOrder);
        //传选择类型的签证数据
        intent.putExtra("detail", listVisa.get(position));
        startActivity(intent);
    }


    /**
     * 获取到所有的领取及其产品
     * @param visaEntity
     */
    public void getAllVisaArea(VisaAllEntity visaEntity) {
        this.visaEntity = visaEntity;
        areaList = new ArrayList<>();
        //将领取单独取出
        for(int i=0 ;i<visaEntity.getResult().size();i++){
            VisaAllEntity.Result result = visaEntity.getResult().get(i);
            VisaAreaEntity areaEntity = new VisaAreaEntity();
            areaEntity.setVisaAreaID(result.getVisa_area_id());
            areaEntity.setVisaAreaCity(result.getVisa_area_name());
            if(result.getVisa_area_name().contains("北京")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.bjl));
            }else if(result.getVisa_area_name().contains("上海")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.shl));
            }else if(result.getVisa_area_name().contains("广州")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.gzl));
            }else if(result.getVisa_area_name().contains("成都")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.cdl));
            }else if(result.getVisa_area_name().contains("沈阳")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.syl));
            }else if(result.getVisa_area_name().contains("重庆")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.cqr));
            }else if(result.getVisa_area_name().contains("西部")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.xbr));
            }else if(result.getVisa_area_name().contains("青岛")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.qdr));
            }else if(result.getVisa_area_name().contains("武汉")){
                areaEntity.setAreaImage(this.getResources().getDrawable(R.drawable.whr));
            }
            areaList.add(areaEntity);
        }
        //领区交给Gallery显示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(areaList != null && areaList.size() > 0){
                    areaAdapter = new VisaAreaAdapter(CountryVisaDetailsActivity.this,areaList);
                    galleryVisaArea.setAdapter(areaAdapter);
                }
            }
        });
        handler.sendEmptyMessageDelayed(1, 1000);
    }


    @Override
    protected void onDestroy() {
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
        System.gc();
        ActivityManager.getInstance().removeActivity(CountryVisaDetailsActivity.this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(CountryVisaDetailsActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(CountryVisaDetailsActivity.this);
        super.onPause();
    }


}
