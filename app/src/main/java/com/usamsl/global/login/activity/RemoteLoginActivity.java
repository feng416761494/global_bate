package com.usamsl.global.login.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.index.step.step5.custom.CustomIsFormSaveDialog;
import com.usamsl.global.util.manager.ActivityManager;

/**
 * Created by Administrator on 2017/7/7.
 */
public class RemoteLoginActivity extends Activity implements View.OnClickListener{

    //注销弹窗
    private PopupWindow cancelPop;
    private TextView tvPsdRecovery,tvLoginAgin;
    private int USER_REMOTELOGIN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            USER_REMOTELOGIN = bundle.getInt("fromDesk");
        }
        setContentView(R.layout.activity_remote_login);
        tvPsdRecovery = (TextView) findViewById(R.id.tvPsdRecovery);
        tvLoginAgin = (TextView) findViewById(R.id.tvLoginAgin);
        tvPsdRecovery.setOnClickListener(this);
        tvLoginAgin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tvPsdRecovery://跳转至修改密码
                Constants.USER_REMOTELOGIN = true;
                Intent intent = new Intent(RemoteLoginActivity.this, PasswordRecoveryActivity.class);
                if(USER_REMOTELOGIN != 0 && USER_REMOTELOGIN == 200){
                    Bundle bundle = new Bundle();
                    bundle.putInt("fromDesk",USER_REMOTELOGIN);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
                this.finish();
                break;

            case R.id.tvLoginAgin://重新登录
                Constants.USER_REMOTELOGIN = true;
                Intent intentLogin = new Intent(RemoteLoginActivity.this, LoginActivity.class);
                if(USER_REMOTELOGIN != 0 && USER_REMOTELOGIN == 200){
                    Bundle bundle = new Bundle();
                    bundle.putInt("fromDesk",USER_REMOTELOGIN);
                    intentLogin.putExtras(bundle);
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intentLogin);
                this.finish();
                break;
        }
    }


    /**
     * 当弹出异地登录的时候，返回键禁止使用
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }
}
