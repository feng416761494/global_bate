package com.usamsl.global.index.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;

/**
 * 海报1
 * 2017/3/8
 */
public class Banner2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner2);
        TextView textTitle = (TextView)findViewById(R.id.tvTitlt);
        if(ObjectIsNullUtils.TextIsNull(getIntent().getExtras().getString("BANNER_TIELE"))){
            textTitle.setText(getIntent().getExtras().getString("BANNER_TIELE"));
        }
        ActivityManager.getInstance().addActivity(Banner2Activity.this);
        ImageView img_back=(ImageView)findViewById(R.id.img_back);
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
        String url = Constants.bannerImageLink_hotle;
        webView.loadUrl(url);
    }


}
