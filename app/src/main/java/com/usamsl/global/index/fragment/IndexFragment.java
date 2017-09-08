package com.usamsl.global.index.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.activity.AdviertisementActivity;
import com.usamsl.global.index.activity.Banner2Activity;
import com.usamsl.global.index.activity.PosterActivity;
import com.usamsl.global.index.activity.VisaCityActivity;
import com.usamsl.global.index.activity.VisaCountryActivity;
import com.usamsl.global.index.adapter.VisaCountryAdapter;
import com.usamsl.global.index.biz.IndexCountryListBiz;
import com.usamsl.global.index.custom.AvaGuidePopupWindow;
import com.usamsl.global.index.entity.IndexBannerEntity;
import com.usamsl.global.index.entity.IndexShownCountry;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step1.activity.CountryVisaDetailsActivity;
import com.usamsl.global.index.step.step3.activity.MapSelectionActivity;
import com.usamsl.global.index.step.step4.activity.ContactsActivity;
import com.usamsl.global.index.util.HeadZoomScrollView;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.MyGridView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件名：IndexFragment
 * 描述：主界面
 * 时间：2016/12/14
 */
public class IndexFragment extends Fragment implements AMapLocationListener,View.OnClickListener {

    //展示国家部分MyGridView
    private MyGridView gv;
    //总界面View
    private View v;
    //展示国家部分GridViewAdapter
    private VisaCountryAdapter visaCountryAdapter;
    //展示国家部分列表数据
    private List<IndexShownCountry.ResultBean> result = new ArrayList<>();;
    //整体滑动的ScrollView
    private HeadZoomScrollView myScrollView;
    //查找签证国家文本
    private TextView tv_searchCountry;
    //查找城市文本
    private RelativeLayout rl_searchCity;
    //显示城市
    private TextView tv_city;
    //是否联网
    private boolean connect = false;
    //城市
    private String city;
    //首页banner
    private IndexBannerEntity bannerEntity;


    //定位
    /**
     * 声明mlocationClient对象
     */
    private AMapLocationClient mlocationClient = null;

    /**
     * 声明mLocationOption对象
     */
    private AMapLocationClientOption mLocationOption = null;
    private AvaGuidePopupWindow popupWindow;

    public IndexFragment() {
        // Required empty public constructor
    }

    private Handler mHandler = new Handler();

    //海报1
    private ImageView img_banner3;
    //haibao2
    private ImageView img1;
    //evus海报2
    private ImageView img_evus;
    //bannner接口中获取的title
    private String GET_BANNNER_TITLE_1 = "";
    private String GET_BANNNER_TITLE_3 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_index, container, false);
            initView(v);
            initLocation();
            initData();
            toListener();
        }
        return v;
    }

    /**
     * 界面初始化
     */
    private void initView(View view) {
        img1 = (ImageView) v.findViewById(R.id.img1);
        img_banner3 = (ImageView) v.findViewById(R.id.img_banner3);
        myScrollView = (HeadZoomScrollView) v.findViewById(R.id.myScrollView);
        gv = (MyGridView) v.findViewById(R.id.gv);
        tv_searchCountry = (TextView) v.findViewById(R.id.tv_searchCountry);
        rl_searchCity = (RelativeLayout) v.findViewById(R.id.rl_searchCity);
        tv_city = (TextView) v.findViewById(R.id.tv_city);
        img_evus = (ImageView) v.findViewById(R.id.img_evus);
        ImageView imgBanner = (ImageView) v.findViewById(R.id.imgBanner);
        Animation animation = AnimationUtils.loadAnimation(
                getActivity(), R.anim.index_banner_animation);
        imgBanner.startAnimation(animation);
        img1.setOnClickListener(this);
        img_banner3.setOnClickListener(this);
        tv_searchCountry.setOnClickListener(this);
        rl_searchCity.setOnClickListener(this);
        img_evus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_searchCountry:
                Intent intent = new Intent(getActivity(), VisaCountryActivity.class);
                intent.putExtra("city", tv_city.getText().toString());
                startActivity(intent);
                break;

            case R.id.rl_searchCity:
                Intent intentCity = new Intent(getActivity(), VisaCityActivity.class);
                intentCity.putExtra("city", tv_city.getText().toString());
                startActivityForResult(intentCity, ConstantsCode.SELECT_CITY);
                break;

            case R.id.img_banner3:
                Intent intentBanner3 = new Intent(getActivity(),AdviertisementActivity.class);
                intentBanner3.putExtra("BANNER3_TIELE",GET_BANNNER_TITLE_3);
                startActivity(intentBanner3);
                break;

            case R.id.img1:
                Intent intentBanner2 = new Intent(getActivity(), Banner2Activity.class);
                intentBanner2.putExtra("BANNER_TIELE",GET_BANNNER_TITLE_1);
                startActivity(intentBanner2);
                break;

            case R.id.img_evus:
                if (Constants.USER_LOGIN) {
                    Intent intentContacts = new Intent(getActivity(), ContactsActivity.class);
                    AddOrder addOrder = new AddOrder();
                    addOrder.setCust_type(0);
                    addOrder.setVisa_id(1);
                    intentContacts.putExtra("evus", "1");
                    intentContacts.putExtra("addOrder", addOrder);
                    startActivity(intentContacts);
                } else {
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intentLogin, ConstantsCode.LOGIN);
                }
                break;
        }
    }

    /**
     * 数据加载
     */
    OkHttpClient client = OkHttpManager.myClient();

    private void initData() {
        myScrollView.smoothScrollTo(0, 20);
        //去掉GridView选中或点击时的阴影
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        city = tv_city.getText().toString();
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connect = true;
        } else {
            ConstantsMethod.showTip(getActivity(), "无网络连接");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        connectWork();
        if (connect) {
            if (result.size() == 0) {
                initConnectData();
            }
        }
    }


    public void updateViews(IndexShownCountry country) {
        if (country.getError_code() == 0) {
            result = country.getResult();
            if(result != null && result.size() > 0){
                visaCountryAdapter = new VisaCountryAdapter(getActivity(), result);
                gv.setAdapter(visaCountryAdapter);
            }
        }
    }
    /**
     * 联网之后加载数据
     */
    private void initConnectData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IndexCountryListBiz biz = new IndexCountryListBiz(IndexFragment.this);
                biz.getData();
                biz.getBannerImageResouces();
            }
        }).start();
    }

    /**
     * 在主界面返回城市
     */
    public String findCity() {
        return city;
    }

    /**
     * 监听
     */
    private void toListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constants.COUNTRY = result.get(i).getCountry_name();
                Constants.COUNTRY_ID = result.get(i).getCountry_id();
                Constants.SPEC_KEY = result.get(i).getPhoto_format();
                UrlSet.countrySurface = result.get(i).getForm_url();
                //跳转到国家签证详情界面
                Intent intent = new Intent(getActivity(), CountryVisaDetailsActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("country", result.get(i).getCountry_id());
                intent.putExtra("ensign_url", result.get(i).getEnsign_url());
                intent.putExtra("bg_img_url", result.get(i).getBg_img_url());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case ConstantsCode.SELECT_CITY:
                    city = data.getStringExtra("city");
                    tv_city.setText(city);
                    break;
            }
        }
    }

    /**
     * 获取定位坐标
     */
    public void initLocation() {
        mlocationClient = new AMapLocationClient(getActivity());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    /**
     * 高德定位回调
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (!amapLocation.getCity().equals("") && amapLocation.getCity() != null) {
                tv_city.setText(amapLocation.getCity());
            } else {
                tv_city.setText("上海");
            }
            //省
            city = amapLocation.getProvince();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mlocationClient.unRegisterLocationListener(this);
        mlocationClient = null;
    }

    /**
     * 获取到首页banner图片
     * @param entity
     */
    public void updateIndexBanner(IndexBannerEntity entity) {
        this.bannerEntity = entity;
        if(entity.getResult() != null && entity.getResult().size() > 0){
            //  1对应酒店机票入口   2极速美签
            for(int i=0;i<entity.getResult().size();i++){
                if(entity.getResult().get(i).getBan_no().equals("1")){
                    Constants.bannerImageUrl_hotle = entity.getResult().get(i).getImg_url();//banner图片url
                    Constants.bannerImageLink_hotle = entity.getResult().get(i).getHyperlinks();//点击banner的link
                    GET_BANNNER_TITLE_1 = entity.getResult().get(i).getTitle();//该banner的title
                }else if(entity.getResult().get(i).getBan_no().equals("2")){
                    Constants.bannerImageUrl_fast = entity.getResult().get(i).getImg_url();
                    Constants.bannerImageLink_fast = entity.getResult().get(i).getHyperlinks();
                    GET_BANNNER_TITLE_3 = entity.getResult().get(i).getTitle();
                }
            }
            ImageLoadUtils.loadBanner1Image(Constants.bannerImageUrl_hotle,img1,getActivity());
            ImageLoadUtils.loadBanner2Image(Constants.bannerImageUrl_fast,img_banner3,getActivity());
        }
    }
}
