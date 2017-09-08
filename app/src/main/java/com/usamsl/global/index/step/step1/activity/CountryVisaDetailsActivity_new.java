package com.usamsl.global.index.step.step1.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.usamsl.global.index.step.step1.biz.CountryVisaDetailBiz;
import com.usamsl.global.index.step.step1.entity.District;
import com.usamsl.global.index.step.step1.entity.VisaDetails;
import com.usamsl.global.index.step.step1.util.AbstractWheel;
import com.usamsl.global.index.step.step1.util.OnWheelChangedListener;
import com.usamsl.global.index.step.step1.util.adapters.ArrayWheelAdapter;
import com.usamsl.global.index.step.step2.activity.BeforeVisaActivity;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.manager.AvaManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.MyListView;
import com.usamsl.global.view.RecyclerImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间：2016/12/23
 * 描述：国家签证详情
 */
public class CountryVisaDetailsActivity_new extends Activity implements View.OnClickListener{
    //返回按钮
    private ImageView img_back;
    //签证显示
    private MyListView lv;
    //签证显示的adapter
    private CountryVisaDetailsAdapter adapter;
    //签证数据
    private List<VisaDetails.ResultBean> mData;
    //城市，国家,领区
    private String city;
    private District.ResultBean area;
    private int country;
    private String ava;
    //ava获取到的国家
    private VisaCountry.ResultBean countrys;
    private ArrayWheelAdapter<String> wheelAdapter;
    private RecyclerImageView img_bg;
    private String TAG = CountryVisaDetailsActivity_new.class.getSimpleName();
    //哪国签证
    private TextView tv_country;
    //语音播放助手
    private SpeechSynthesizer mTts;
    //只播放一次
    private boolean one = true;
    private CountryVisaDetailBiz visaDetailBiz;
    private static int VISA_AREA_ID = 0;
    private ScrollView scrollView;
    //Gallery  为选择领取的控件
    private Gallery galleryVisaArea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_visa_details);
        ActivityManager.getInstance().addActivity(CountryVisaDetailsActivity_new.this);
        //业务层的了biz类，用于请求数据
        visaDetailBiz = new CountryVisaDetailBiz(CountryVisaDetailsActivity_new.this);
        initView();
        initData();
    }

    /**
     * 界面初始化
     */
    private void initView() {
        img_bg = (RecyclerImageView) findViewById(R.id.img_bg);
        img_back = (ImageView) findViewById(R.id.img_back);
        lv = (MyListView) findViewById(R.id.lv);
        tv_country = (TextView) findViewById(R.id.tv_country);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        tv_country.setText(Constants.COUNTRY + "签证");
        city = getIntent().getStringExtra("city");
        country = getIntent().getIntExtra("country", 11);
        ava = getIntent().getStringExtra("ava");
        countrys = getIntent().getParcelableExtra("countrys");
        if(!ObjectIsNullUtils.TextIsNull(city)){
            city = "";
        }
        visaDetailBiz.loadVisaAll(country,city);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onDestroy() {
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
        System.gc();
        ActivityManager.getInstance().removeActivity(CountryVisaDetailsActivity_new.this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(CountryVisaDetailsActivity_new.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(CountryVisaDetailsActivity_new.this);
        super.onPause();
    }

}
