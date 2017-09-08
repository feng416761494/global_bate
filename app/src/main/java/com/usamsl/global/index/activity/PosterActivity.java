package com.usamsl.global.index.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.usamsl.global.R;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.util.manager.ActivityManager;

/**
 * 时间：2017/3/7
 * 海报3
 */
public class PosterActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poster);
        ActivityManager.getInstance().addActivity(PosterActivity.this);
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
        String url = UrlSet.poster1;
        webView.loadUrl(url);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web);
        img_back=(ImageView)findViewById(R.id.img_back);
    }
}
