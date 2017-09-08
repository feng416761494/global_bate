package com.usamsl.global.my.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.SpeechSynthesizer;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.UrlSet;

/**
 * 时间：2017/2/23
 * 隐私政策
 */
public class AuthorizeActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        initView();
        initData();
        toListener();
    }

    private void toListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        String url = UrlSet.authorize;
        webView.loadUrl(url);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web);
        img_back = (ImageView) findViewById(R.id.img_back);
    }
}
