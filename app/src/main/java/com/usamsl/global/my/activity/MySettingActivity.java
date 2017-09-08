package com.usamsl.global.my.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.my.util.IOSSwitchView;
import com.usamsl.global.util.MyDataCleanManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.util.Map;

/**
 * 时间：2016/12/16
 * 描述：个人设置界面
 */
public class MySettingActivity extends AppCompatActivity {

    //勿扰模式开关控件
    private IOSSwitchView coolSwitch;
    //返回图片
    private ImageView img_back;
    //注销文本,缓存大小
    private TextView tv_cancel,tvCache;
    //勿扰弹窗
    private PopupWindow mPopupWindow;
    //注销弹窗
    private PopupWindow cancelPop;
    //关于我们/隐私政策
    private RelativeLayout rlAboutUs,rlSecret,rlCache;
    private UMShareAPI umShareAPI = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
//            Toast.makeText(MySettingActivity.this, "MySettingActivity=="+bundle.getString("content"), Toast.LENGTH_SHORT).show();
        }
        initView();
        toListener();
        umShareAPI = UMShareAPI.get(this);
    }

    /**
     * 界面数据初始化
     */
    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        coolSwitch = (IOSSwitchView) findViewById(R.id.coolSwitch);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tvCache = (TextView) findViewById(R.id.tvCache);
        rlAboutUs = (RelativeLayout) findViewById(R.id.rl_point);
        rlSecret = (RelativeLayout) findViewById(R.id.rl_secret);
        rlCache = (RelativeLayout) findViewById(R.id.rl_cache);
        initData();
        if(Constants.USER_LOGIN){
            tv_cancel.setVisibility(View.VISIBLE);
        }else{
            tv_cancel.setVisibility(View.GONE);
        }
        //获取App的缓存大小
        getCacheSize();
        coolSwitch.setOn(true);
    }

    /**
     * 获取缓存
     */
    private void getCacheSize() {
        try {
            String dataCache = MyDataCleanManager.getTotalCacheSize(getApplicationContext());
            if(ObjectIsNullUtils.TextIsNull(dataCache)){
                if(!dataCache.equals("0.0Byte")){
                    tvCache.setText(dataCache);
                }else{
                    tvCache.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 界面数据加载
     */
    private void initData() {
    }

    /**
     * 监听事件
     */
    private void toListener() {
        //返回图片监听
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //关于我们
        rlAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
        //隐私政策
        rlSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySettingActivity.this, AuthorizeActivity.class);
                startActivity(intent);
            }
        });
        //清除缓存
        rlCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDataCleanManager.clearAllCache(getApplicationContext());
                //重新获取缓存大小，并且显示
                getCacheSize();
            }
        });

        //注销
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.USER_LOGIN) {
                    final CustomIsFormSaveDialog dialog = new CustomIsFormSaveDialog(MySettingActivity.this, "是否注销账号?", "提示");
                    dialog.setDialogClickListener(new CustomIsFormSaveDialog.DialogClickListener() {
                        @Override
                        public void doCancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void doConfirm() {
                            dialog.dismiss();
                            //保存注销状态到本地
                            ConstantsMethod.saveCancellationState(MySettingActivity.this);
                            if(Constants.platform != null){
                                umShareAPI.deleteOauth(MySettingActivity.this, Constants.platform, authListener);
                            }
                            View v = getLayoutInflater().inflate(R.layout.dialog, null);
                            RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
                            TextView tv = (TextView) v.findViewById(R.id.tv);
                            tv.setText("注销成功");
                            cancelPop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT, true);
                            cancelPop.showAtLocation(v, Gravity.CENTER, 0, 0);
                            rl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cancelPop.dismiss();
                                }
                            });
                            //如果不点击，过几秒消失
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cancelPop.dismiss();
                                }
                            }, 1500);
                            cancelPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    finish();
                                }
                            });
                        }
                    });
                    dialog.show();

                }
            }
        });



        coolSwitch.setOnSwitchStateChangeListener(new IOSSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onStateSwitched(boolean isOn) {
                if (isOn) {
                    View popupView = getLayoutInflater().inflate(R.layout.setting_pop, null);
                    RelativeLayout rl = (RelativeLayout) popupView.findViewById(R.id.rl);
                    mPopupWindow =
                            new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT, true);
                    mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                    rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPopupWindow.dismiss();
                        }
                    });
                    //如果不点击，过几秒消失
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                        }
                    }, 1500);
                } else {
                }
            }
        });
//        coolSwitch.setCheckedListener(new CoolSwitch.CheckedListener() {
//            @Override
//            public void onChecked() {
//                View popupView = getLayoutInflater().inflate(R.layout.setting_pop, null);
//                RelativeLayout rl = (RelativeLayout) popupView.findViewById(R.id.rl);
//                mPopupWindow =
//                        new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT, true);
//                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//                rl.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mPopupWindow.dismiss();
//                    }
//                });
//                //如果不点击，过几秒消失
//                Handler mHandler = new Handler();
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPopupWindow.dismiss();
//                    }
//                }, 1500);
//            }
//
//            @Override
//            public void onUnchecked() {
//
//            }
//        });
    }

    UMAuthListener authListener = new UMAuthListener() {

        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Toast.makeText(MySettingActivity.this, "开始", Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, final Map<String, String> data) {
            Toast.makeText(MySettingActivity.this, "完成", Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(MySettingActivity.this, "失败：" + t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(MySettingActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

}
