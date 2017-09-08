package com.usamsl.global.index.activity;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.hp.hpl.sparta.Text;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

/**
 * Created by Administrator on 2017/8/7.
 */
public class AdviertisementActivity extends Activity{

    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner3);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        TextView textTitle = (TextView)findViewById(R.id.tvTitlt);
        ImageView img_back=(ImageView)findViewById(R.id.img_back);
        if(ObjectIsNullUtils.TextIsNull(getIntent().getExtras().getString("BANNER3_TIELE"))){
            textTitle.setText(getIntent().getExtras().getString("BANNER3_TIELE"));
        }
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdviertisementActivity.this.finish();
            }
        });
        BridgeWebView webView = (BridgeWebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        //webView加载的网页自适应webView
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient() {


            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String url = Constants.bannerImageLink_fast;
        webView.loadUrl(url);
    }
}
