package com.usamsl.global.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.socialize.UMShareAPI;
import com.usamsl.global.R;
import com.usamsl.global.ava.activity.AvaActivity;
import com.usamsl.global.ava.util.ThreadPoolManager;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.PermissionRequest;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.custom.AvaGuidePopupWindow;
import com.usamsl.global.index.custom.AvaOutPopupWindow;
import com.usamsl.global.index.fragment.IndexFragment;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.login.activity.RemoteLoginActivity;
import com.usamsl.global.main.entity.Version;
import com.usamsl.global.my.activity.MySettingActivity;
import com.usamsl.global.my.fragment.MyFragment;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.order.fragment.OrderFragment;
import com.usamsl.global.service.fragment.ServiceFragment;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.util.update.UpdateAppUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * 描述：主Activity
 * 文件名：MainActivity
 * 时间：2016/12/14
 */
public class MainActivity extends FragmentActivity {

    //标签栏菜单RadioGroup
    private RadioGroup rg;
    //首页对应的fragment
    private IndexFragment indexFragment;
    //订单对应的fragment
    private OrderFragment orderFragment;
    //服务对应的fragment
    private ServiceFragment serviceFragment;
    //我的对应的fragment
    private MyFragment myFragment;
    //ava
    private ImageView img_avaGuide,imgAva;
    //ava弹出的关闭 叉号
    private ImageView imgGuideClose;
    private TextView tvConfirm;
    private RadioButton rb_index,rb_my,rb_order;
    private RelativeLayout relBackGround;
    //网络获取的版本号
    private static String version;
    //双击退出
    private boolean isExit = false;
    private AvaGuidePopupWindow popupWindowBg;
    private AvaOutPopupWindow popupWindowAva;
    private int avaPlay = 1;
    private int avaPlay1 = 0;
    private int message = 1;
    private boolean flag = false;
    private View popupViewAva;
    //该变量是oncreat的控件确定加载完之后的一个判断
    private static boolean IS_INIT_FLOATVIEW_LOCATION = true;
    private MainActivityReceiver receiver;
    public static boolean isForeground = false;



    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    IS_INIT_FLOATVIEW_LOCATION = false;
                    if(Constants.AVA_HAS_FIRST_LOAD){
                        popupWindowAva.showAtLocation(popupViewAva, Gravity.CENTER, 0, 0);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//接收来自网页的启动消息
//        Intent intent = getIntent();
//        String action = intent.getAction();
//        if(Intent.ACTION_VIEW.equals(action)){
//            Uri uri = intent.getData();
//            if(uri != null){
//                String host = uri.getHost();
//                Toast.makeText(MainActivity.this, "收到网页的host:"+uri.getHost(), Toast.LENGTH_SHORT).show();
//            }
//        }
        initData();
        initView();
        initFragment();
        toListener();
        receiver = new MainActivityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("AVA_POPUPWINDOW_CLOSE");
        filter.addAction("USER_LOGIN_FALSE");
        filter.addAction(Constants.USER_REMOTELOGIN_CONTENT);
        this.registerReceiver(receiver,filter);
    }

    private void initData() {
//        judgmentVersion();
        //提示更新只出现一次
        if(!Constants.UPDATE_SHOW_ONLY_ONCE){
            update();
            Constants.UPDATE_SHOW_ONLY_ONCE = true;
        }
        changeToken();
    }


    /**
     * 判断版本是否有更新
     */
    private void update() {
        getSDKVersion();
        getVersion();
    }

    /**
     * 判断版本
     */
    private void judgmentVersion() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermission();
        }
    }

    /**
     * 动态请求权限
     */
    public void requestPermission() {
        RxPermissions.getInstance(this)
                .request(PermissionRequest.str)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (!granted) {
                            //不同意
                            ConstantsMethod.showTip(MainActivity.this, "您必须允许权限才能使用！");
                            finish();
                        }
                        TelephonyManager tm = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
                        String imei = tm.getSimSerialNumber();
                        Constants.USER_LOGIN_FOR_AVA = imei + System.currentTimeMillis();
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {


    }

    /**
     * 监听
     */
    private void toListener() {
        rgOnCheckedChange();
        imgAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AvaActivity.class);
                intent.putExtra("help", false);
                String city = indexFragment.findCity();
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });
    }

    //该方法在onCreat的控件加载完之后会进入此方法
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(IS_INIT_FLOATVIEW_LOCATION){
            if(hasFocus){
                mHandler.sendEmptyMessageAtTime(1,500);
            }
        }
    }

    /**
     * 界面数据初始化
     */
    private void initView() {
        rb_index = (RadioButton) findViewById(R.id.rb_index);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
        rb_order = (RadioButton) findViewById(R.id.rb_order);
        imgAva = (ImageView) findViewById(R.id.img_ava);
        rg = (RadioGroup) findViewById(R.id.rg);
        popupViewAva = LayoutInflater.from(this).inflate(R.layout.va_guide_layout,null);
        imgGuideClose = (ImageView) popupViewAva.findViewById(R.id.imgClose);
        tvConfirm = (TextView) popupViewAva.findViewById(R.id.tvOk);
        img_avaGuide = (ImageView) popupViewAva.findViewById(R.id.imgAvaGuide);

        //首次安装App的用户，会有操作使用提示，此提示只出现一次
        SharedPreferences preferences = getSharedPreferences("AVA_POPUPWINDOW", Context.MODE_PRIVATE);
        boolean isFirstLoad = preferences.getBoolean("avaOut",true);
        if(isFirstLoad){
            Constants.AVA_HAS_FIRST_LOAD = true;
            popupWindowAva = new AvaOutPopupWindow(this,popupViewAva,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindowAva.setAvaGuideClickListener(new AvaOutPopupWindow.AvaGuideClickListener() {
                @Override
                public void close() {
                    popupWindowAva.dismiss();
                }

                @Override
                public void confirm() {
                    popupWindowAva.dismiss();
                }
            });
            popupViewAva.setFocusableInTouchMode(true);
            popupViewAva.setFocusable(true);
        }else{
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("avaOut",false);
        editor.commit();
    }





    /**
     * fragment数据初始化
     */
    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (null == indexFragment) {
            indexFragment = new IndexFragment();
        }
        if (null == orderFragment) {
            orderFragment = new OrderFragment();
        }
        if (null == serviceFragment) {
            serviceFragment = new ServiceFragment();
        }
        if (null == myFragment) {
            myFragment = new MyFragment();
        }
        transaction
                .add(R.id.container, indexFragment)
                .add(R.id.container, orderFragment)
                .add(R.id.container, serviceFragment)
                .add(R.id.container, myFragment);
        transaction.show(indexFragment).hide(orderFragment).hide(serviceFragment).hide(myFragment).commit();

        //MyFragment的接口回调监听，有一个fragment跳转至另一个fragment
        myFragment.setListener(new MyFragment.GoToOrderFragmentListener() {
            @Override
            public void goToOrder(View view) {
                if (Constants.USER_LOGIN) {
                    rb_order.setChecked(true);
                    //注意！！！此处不可以用之前创建的transaction，因为已经commit过了，所以要创建新的transaction
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    transaction2.hide(myFragment).show(orderFragment).hide(serviceFragment).hide(indexFragment).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, ConstantsCode.LOGIN);
                }
            }
        });
    }

    /*
    标签栏菜单修改监听事件
     */
    private void rgOnCheckedChange() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case R.id.rb_index:
                        transaction.show(indexFragment).hide(orderFragment).hide(serviceFragment).hide(myFragment).commitAllowingStateLoss();
                        break;
                    case R.id.rb_order:
                        if (Constants.USER_LOGIN) {
                            transaction.hide(indexFragment).show(orderFragment).hide(serviceFragment).hide(myFragment).commit();
                        } else {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, ConstantsCode.LOGIN);
                        }
                        break;
                    case R.id.rb_service:
                        transaction.hide(indexFragment).hide(orderFragment).show(serviceFragment).hide(myFragment).commit();
                        break;
                    case R.id.rb_my:
                        transaction.hide(indexFragment).hide(orderFragment).hide(serviceFragment).show(myFragment).commitAllowingStateLoss();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (requestCode) {
                case ConstantsCode.LOGIN:
                    if (Constants.USER_LOGIN) {
                        rb_order.setChecked(true);
                        transaction.hide(indexFragment).show(orderFragment).hide(serviceFragment).hide(myFragment).commit();
                    } else {
                        transaction.show(indexFragment).hide(orderFragment).hide(serviceFragment).hide(myFragment).commit();
                        rb_index.setChecked(true);
                    }
                    break;
            }
        } else {
            switch (requestCode) {
                case ConstantsCode.LOGIN:
                    rb_index.setChecked(true);
                    break;
            }
        }
    }


    /*
     * get the sdk version. 获取当前版本号
     */
    public void getSDKVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            Constants.version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取版本号
    private void getVersion() {
        OkHttpClient client = OkHttpManager.myClient();
        String url = UrlSet.clientVersion;
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final Version v = gson.fromJson(str, Version.class);
                if(v != null){
                    final List<Version.ResultBean> list = v.getResult();
                    final Version.ResultBean[] bean = {new Version.ResultBean()};
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (v.getError_code()) {
                                case 0:
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).getClient_name().equals("安卓")) {
                                            version = list.get(i).getClient_version();
                                            bean[0] = list.get(i);
                                            break;
                                        }
                                    }
                                    UpdateAppUtil.updateApp(MainActivity.this, bean[0].getApp_http());
                                    break;
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 判断token是否有效
     */
    private void changeToken() {
        OkHttpClient client = OkHttpManager.myClient();
        String url = UrlSet.token + Constants.TOKEN;
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final Result result = gson.fromJson(str, Result.class);
                if(result != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (result.getError_code()) {
                                case 2:
                                    //保存注销状态到本地
                                    ConstantsMethod.saveCancellationState(MainActivity.this);
                                    break;
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean radioBtn_myCheck = false;
    /**
     * 广播接收
     */
    class MainActivityReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //code 是对popupwindow的关闭监听，这里防止首次安装的时候弹窗和权限获取冲突的问题
            int code = intent.getIntExtra("ava_popupwindow_close",0);
            //用户是否登录
            boolean userLogin = intent.getBooleanExtra("login",false);
            //用户是否被异地登录
            int user_remotelogin_num = intent.getIntExtra("remotelogin",1);

            if(code == 100){
                judgmentVersion();
            }
            if(userLogin == false){
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.show(indexFragment).hide(orderFragment).hide(serviceFragment).hide(myFragment).commitAllowingStateLoss();
//                rb_index.setChecked(true);
            }
            //App处于前台时被告知异地登录
            if(user_remotelogin_num == 100){
                if(rb_order.isChecked()){
                    getSupportFragmentManager().beginTransaction().hide(orderFragment).hide(orderFragment).hide(serviceFragment).show(indexFragment).commitAllowingStateLoss();
                    rb_index.setChecked(true);
                }
            }else if(user_remotelogin_num == 200){//App处于后台被拉起告知异地登录
            }
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByDoubleClick();
        }
        return false;
    }

    /**
     * 双击退出程序
     */
    private void exitByDoubleClick() {
        Timer tExit;
        if (!isExit) {
            isExit = true;
            ConstantsMethod.showTip(MainActivity.this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {
        exitByDoubleClick();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            this.unregisterReceiver(receiver);
        }
    }



    /**
     * 获取服务器的版本号
     *
     * @param context  上下文
     * @param callBack 为了控制线程，需要获取服务器版本号成功的回掉
     */
    public static void getAPPServerVersion(Context context, final VersionCallBack callBack) {
        //todo 自己的网络请求获取 服务器上apk的版本号（需要与后台协商好）
        callBack.callBack(version);
    }


    public interface VersionCallBack {
        void callBack(String versionCode);
    }

    boolean isFinish = false;
    @Override
    protected void onResume() {
        isForeground = true;
        int USER_REMOTELOGIN = 0;
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            bundle.putString("content",bundle.getString("content"));
            //用户被异地登录的判别码
            if(!isFinish){
                USER_REMOTELOGIN = bundle.getInt("remotelogin");
            }
            switch (USER_REMOTELOGIN){
                case 100://在App中进入异地登录
                    getSupportFragmentManager().beginTransaction().hide(orderFragment).hide(myFragment).hide(serviceFragment).show(indexFragment).commitAllowingStateLoss();
                    rb_index.setChecked(true);
                    isFinish = true;
                    break;

                case 200://在桌面中拉起进入App，然后进入异地登录
                    Intent intent1 = new Intent(this,RemoteLoginActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle.putInt("fromDesk",200);
                    intent1.putExtras(bundle1);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(intent1);
                    isFinish = true;
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
}
