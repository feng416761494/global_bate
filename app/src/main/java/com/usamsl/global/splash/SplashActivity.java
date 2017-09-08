package com.usamsl.global.splash;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.usamsl.global.R;
import com.usamsl.global.main.MainActivity;

/**
 * 时间：2017/3/28
 * 描述：引导页
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //接收来自网页的启动消息
        Intent intent = getIntent();
        String action = intent.getAction();
        if(Intent.ACTION_VIEW.equals(action)){
            Uri uri = intent.getData();
            if(uri != null){
                String host = uri.getHost();
            }
        }
        RelativeLayout rl=(RelativeLayout)findViewById(R.id.rl);
        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash);
        rl.startAnimation(alphaAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 1500);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            bundle.putString("content",bundle.getString("content"));
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }
}
