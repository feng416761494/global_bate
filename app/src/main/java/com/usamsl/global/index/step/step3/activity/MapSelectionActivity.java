package com.usamsl.global.index.step.step3.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.google.gson.Gson;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step3.biz.MapSelectionBiz;
import com.usamsl.global.index.step.step3.entity.Map;
import com.usamsl.global.index.step.step6.activity.PaymentActivity;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;

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
 * 时间：2017/1/6
 * 描述：网点选择
 */
public class MapSelectionActivity extends AppCompatActivity implements LocationSource, AMapLocationListener,
        AMap.OnMarkerClickListener, AMap.OnMapClickListener,View.OnClickListener {

    //搜索
    // private TextView tv_search;
    //输入内容
    // private EditText et_search;
    //返回按钮
    private ImageView img_back;
    //地图
    private MapView mapView;
    private AMap aMap;
    //所在地区
    private String city;
    private List<Map.ResultBean> bean;
    /**
     * 定位
     */
    //private LocationManagerProxy mAMapLocationManager;
    /**
     * 声明mlocationClient对象
     */
    private AMapLocationClient mlocationClient = null;

    /**
     * 声明mLocationOption对象
     */
    private AMapLocationClientOption mLocationOption = null;
    /**
     * 定位监听
     */
    private OnLocationChangedListener mListener;

    /**
     * 添加的覆盖物标志
     */
    private Marker currentMarker;
    PoiSearch poiSearch;
    //判断有无网络连接
    private boolean connection = false;
    //标记物详细信息
    private RelativeLayout rl;
    private TextView tv_name;
    private TextView tv_tel;
    private TextView tv_address;
    private TextView tv_up;
    private TextView tv_down;
    private Map.ResultBean map = null;
    //popupwindow滑出动画
    private Animation animation1;
    //划入动画
    private Animation animation2;
    private AddOrder addOrder;
    private int order_id;
    private ImageView imgHint;
    private RelativeLayout relTop;
    private int mapBottom;
    private MapSelectionBiz selectionBiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);
        ActivityManager.getInstance().addActivity(MapSelectionActivity.this);
        selectionBiz = new MapSelectionBiz(MapSelectionActivity.this);
        initView();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initData();
        toListener();
    }

    /**
     * 界面初始化
     */
    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.rl);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_up = (TextView) findViewById(R.id.tv_up);
        tv_down = (TextView) findViewById(R.id.tv_down);
        img_back = (ImageView) findViewById(R.id.img_back);
        mapView = (MapView) findViewById(R.id.map);
        imgHint = (ImageView) findViewById(R.id.imgHint);
        relTop = (RelativeLayout) findViewById(R.id.rl_top);
        img_back.setOnClickListener(this);
        imgHint.setOnClickListener(this);
        tv_up.setOnClickListener(this);
        tv_down.setOnClickListener(this);
        //首次安装App的用户，会有操作使用提示，此提示只出现一次
        SharedPreferences preferences = getSharedPreferences("ImageHint", Context.MODE_PRIVATE);
        boolean isFirstLoad = preferences.getBoolean("firstLoad",true);
        if(isFirstLoad){

            mapView.post(new Runnable() {
                @Override
                public void run() {
                    imgHint.setVisibility(View.VISIBLE);
                    mapBottom = mapView.getBottom();
                    animateViewDirection(imgHint,0 - imgHint.getHeight() - 50,(mapBottom + relTop.getBottom() - imgHint.getHeight()),60,3);
                }
            });
        }else{
            imgHint.setVisibility(View.GONE);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstLoad",false);
        editor.commit();

    }



    /**
     *
     * @param v
     * @param from
     * @param to
     * @param tension 拉力
     * @param friction 摩擦力
     */
    private void animateViewDirection(final View v, float from, float to, double tension, double friction) {
        Spring spring = SpringSystem.create().createSpring();
        spring.setCurrentValue(from);
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(tension, friction));
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                v.setTranslationY((float) spring.getCurrentValue());
            }
        });
        spring.setEndValue(to);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back://关闭
                this.finish();
                break;

            case R.id.imgHint://首次安装的用户，操作提示动画
                if(clickCount == 1){
                    imgHint.setBackgroundResource(R.drawable.guide2);
                    clickCount = clickCount + 1;
                }else{
                    imgHint.setVisibility(View.GONE);
                    AnimationSet animationSet = new AnimationSet(false);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,
                            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    //300毫秒完成动画
                    scaleAnimation.setDuration(300);
                    //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
                    animationSet.addAnimation(scaleAnimation);
                    animationSet.setRepeatCount(-1);
                    //启动动画
                    imgHint.startAnimation(animationSet);
                }
                break;

            case R.id.tv_up://线上
                Intent intent = new Intent(MapSelectionActivity.this, PaymentActivity.class);
                if(map == null){
                    map = new Map.ResultBean();
                    map.setBank_outlets_id(bean.get(2).getBank_outlets_id());
                    map.setName(bean.get(2).getName());
                    map.setAddress(bean.get(2).getAddress());
                    map.setFixed_line(bean.get(2).getFixed_line());
                }
                intent.putExtra("bankName",map.getName());
                intent.putExtra("bankPhone",map.getFixed_line());
                intent.putExtra("bankAddress",map.getAddress());
                intent.putExtra("bankId",map.getBank_outlets_id()+"");
                //点击线上的时候，将银行网点和订单关联起来
                selectionBiz.saveOrderBankDetails(order_id,map);
                setResult(200,intent);
                MapSelectionActivity.this.finish();
                break;

            case R.id.tv_down://线下
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        selectionBiz.doOffLine(map,bean,order_id);
                    }
                }).start();
                break;
        }
    }



    int clickCount = 1;
    /**
     * 监听
     */
    private void toListener() {
    }


    /**
     * 数据加载
     */
    private void initData() {
        addOrder = getIntent().getParcelableExtra("addOrder");
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            order_id = bundle.getInt("order_id");
        }
        animation1 = AnimationUtils.loadAnimation(this, R.anim.close);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.open);
        connectWork();
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connection = true;
        } else {
            connection = false;
            ConstantsMethod.showTip(MapSelectionActivity.this, "无网络连接");
        }
    }


    /**
     * 设置地图样式
     */
    private void setUpMap() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.BLUE);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(2);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置地图可视缩放大小
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        aMap.getUiSettings().setCompassEnabled(true);// 设置指南针
        aMap.getUiSettings().setScaleControlsEnabled(true);// 设置比例尺
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        selectionBiz.getBankList();
    }



    public void setBankListOnMap(final Map map) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (map.getError_code() == 0) {
                    bean = map.getResult();
                    for (int i = 0; i < bean.size(); i++) {
                        //根据经纬度进行标记
                        LatLng latLng = new LatLng(Double.parseDouble(bean.get(i).getLatitude()),
                                Double.parseDouble(bean.get(i).getLongitude()));
                        MarkerOptions otMarkerOptions = new MarkerOptions();
                        otMarkerOptions.position(latLng);
                        otMarkerOptions.visible(true);//设置可见
                        otMarkerOptions.draggable(true);
                        //下面这个是自定义的标记图标使用方法
                        otMarkerOptions.icon(ImagePress());
                        Marker marker = aMap.addMarker(otMarkerOptions);
                        marker.setObject(bean.get(i).getBank_outlets_id());
                    }
                    //定位到上海
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //默认选中一个网点，这里显示第三个
                            LatLng marker1 = new LatLng(Double.parseDouble(bean.get(2).getLatitude()),
                                    Double.parseDouble(bean.get(2).getLongitude()));
                            tv_name.setText(bean.get(2).getName());
                            tv_tel.setText("电话：" + bean.get(2).getFixed_line());
                            tv_address.setText("地址：" + bean.get(2).getAddress());
                            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
                        }
                    }, 500);

                }
            }
        });
    }




    /**
     * 自定义标记物的图片（建筑物）
     *
     * @return
     */
    private BitmapDescriptor ImageNormal() {
        View view = null;
        view = getLayoutInflater().inflate(R.layout.map_dot, null);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
        return bitmap;
    }

    /**
     * 自定义标记物图片（网点）
     *
     * @return
     */
    private BitmapDescriptor ImagePress() {
        //使用方法同上
        View view = null;
        view = getLayoutInflater().inflate(R.layout.map_dot1, null);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
        return bitmap;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        if(mlocationClient != null){
            mlocationClient.unRegisterLocationListener(this);
            mlocationClient = null;
        }
        ActivityManager.getInstance().removeActivity(MapSelectionActivity.this);
        super.onDestroy();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
//       initLocation();
    }

    /**
     * 获取定位坐标
     */
    public void initLocation() {
        mlocationClient = new AMapLocationClient(MapSelectionActivity.this);
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
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                city = amapLocation.getCity();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int bank_outlets_id = (int) marker.getObject();
        if (bank_outlets_id != -1) {
            Marker marker1 = currentMarker;
            currentMarker = marker;
            View view = getLayoutInflater().inflate(R.layout.map_dot2, null);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
            currentMarker.setIcon(bitmap);
            for (int i = 0; i < bean.size(); i++) {
                if (bank_outlets_id == bean.get(i).getBank_outlets_id()) {
                    map = bean.get(i);
                    break;
                }
            }
            if (map != null) {
                if (rl.getVisibility() != rl.VISIBLE) {
                    rl.setVisibility(View.VISIBLE);
                    rl.startAnimation(animation2);
                }
                view = getLayoutInflater().inflate(R.layout.map_dot2, null);
                bitmap = BitmapDescriptorFactory.fromView(view);
                currentMarker.setIcon(bitmap);
                tv_name.setText(map.getName());
                tv_tel.setText("电话：" + map.getFixed_line());
                tv_address.setText("地址：" + map.getAddress());
            }
            view = getLayoutInflater().inflate(R.layout.map_dot1, null);
            bitmap = BitmapDescriptorFactory.fromView(view);
            marker1.setIcon(bitmap);
        }
        return false;
    }





    /**```
     * 点击地图其他地方时，隐藏详细信息
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();//隐藏InfoWindow框
            View view = getLayoutInflater().inflate(R.layout.map_dot1, null);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
            currentMarker.setIcon(bitmap);
            currentMarker = null;
        }
    }


}
