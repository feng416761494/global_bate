package com.usamsl.global.my.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;

/**
 * 关于我们
 * 2017/3/11
 */
public class AboutUsActivity extends AppCompatActivity {
    private ImageView img_back;
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
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
        tv_version.setText("版本号：" + Constants.version);
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_version = (TextView) findViewById(R.id.tv_version);
    }
}
