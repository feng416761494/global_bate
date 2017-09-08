package com.usamsl.global.my.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step7.activity.TakePhotoActivity;
import com.usamsl.global.index.step.step7.entity.AccountBook;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.index.util.XCRoundRectImageView;
import com.usamsl.global.my.entity.FeedBack;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ImageCompress;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.MyGridView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_wordNum;//输入的字数
    private TextView tv_pictureNum;//图片数量
    private EditText et_word;//输入的文本
    private TextView tv_send;//保存
    private EditText et_tel;
    private List<FeedBack> mData;
    private int which;//点击的哪个
    private int sum = 0;//图片个数
    private FrameLayout frameLayout1,frameLayout2,frameLayout3;
    private ImageView delete1,delete2,delete3,imgBack;
    private XCRoundRectImageView photo1,photo2,photo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_feed_back);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        toListener();
    }


    private void initView() {
        tv_pictureNum = (TextView) findViewById(R.id.tv_pictureNum);
        tv_wordNum = (TextView) findViewById(R.id.tv_wordNum);
        tv_send = (TextView) findViewById(R.id.tv_send);
        et_word = (EditText) findViewById(R.id.et_word);
        et_tel = (EditText) findViewById(R.id.et_tel);
        frameLayout1=(FrameLayout)findViewById(R.id.frameLayout1);
        frameLayout2=(FrameLayout)findViewById(R.id.frameLayout2);
        frameLayout3=(FrameLayout)findViewById(R.id.frameLayout3);
        delete1=(ImageView)findViewById(R.id.img_delete1);
        delete2=(ImageView)findViewById(R.id.img_delete2);
        delete3=(ImageView)findViewById(R.id.img_delete3);
        imgBack=(ImageView)findViewById(R.id.img_back);
        photo1=(XCRoundRectImageView)findViewById(R.id.img_photo1);
        photo2=(XCRoundRectImageView)findViewById(R.id.img_photo2);
        photo3=(XCRoundRectImageView)findViewById(R.id.img_photo3);
        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        delete3.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        frameLayout1.setOnClickListener(this);
        frameLayout2.setOnClickListener(this);
        frameLayout3.setOnClickListener(this);
        mData = new ArrayList<>();
        mData.add(new FeedBack(null, 1));
        mData.add(new FeedBack(null, 2));
        mData.add(new FeedBack(null, 2));
        //设置用户注册的手机号码
        if(Constants.USER_LOGIN){
            et_tel.setText(Constants.USER);
        }else{
            et_tel.setText("");
        }
        imgBack.setOnClickListener(this);
    }

    private void toListener() {

        et_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_wordNum.setText(charSequence.toString().length() + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                this.finish();
                break;

            case R.id.tv_send:
                if (et_word.getText().toString().length() > 0 && et_tel.getText().toString().trim().length() == 11) {
                    pull();
                } else if (et_word.getText().toString().length() == 0) {
                    ConstantsMethod.showTip(FeedBackActivity.this, "请输入反馈意见或建议");
                } else if (et_tel.getText().toString().trim().length() != 11) {
                    ConstantsMethod.showTip(FeedBackActivity.this, "请输入正确的手机号");
                }
                break;

            case R.id.frameLayout1:
                which = 0;
                Intent intent = new Intent(FeedBackActivity.this, TakePhotoActivity.class);
                startActivityForResult(intent, ConstantsCode.FEED_BACK);
                break;

            case R.id.frameLayout2:
                which = 1;
                Intent intent2 = new Intent(FeedBackActivity.this, TakePhotoActivity.class);
                startActivityForResult(intent2, ConstantsCode.FEED_BACK);
                break;

            case R.id.frameLayout3:
                which = 2;
                Intent intent3 = new Intent(FeedBackActivity.this, TakePhotoActivity.class);
                startActivityForResult(intent3, ConstantsCode.FEED_BACK);
                break;
            case R.id.img_delete1:
                sum--;
                mData.get(0).setPhotoUrl(null);
                photo1.setImageResource(R.drawable.add1);
                delete1.setVisibility(View.GONE);
                tv_pictureNum.setText(sum + "");
                break;

            case R.id.img_delete2:
                sum--;
                mData.get(1).setPhotoUrl(null);
                photo2.setImageResource(R.drawable.add1);
                delete2.setVisibility(View.GONE);
                tv_pictureNum.setText(sum + "");
                break;

            case R.id.img_delete3:
                sum--;
                mData.get(2).setPhotoUrl(null);
                photo3.setImageResource(R.drawable.add1);
                delete3.setVisibility(View.GONE);
                tv_pictureNum.setText(sum + "");
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        super.onActivityResult(arg0, arg1, data);
        if (data == null) {
            return;
        }
        if (arg1 == Activity.RESULT_OK) {
            switch (arg0) {
                case ConstantsCode.FEED_BACK:
                    String photoUrl = data.getStringExtra("photoUrl");
                    mData.get(which).setPhotoUrl(photoUrl);
                    switch (which) {
                        case 0:
                            if(ObjectIsNullUtils.TextIsNull(photoUrl)){
                                photo1.setImageBitmap(compressImage(photoUrl));
                                findViewById(R.id.img_delete1).setVisibility(View.VISIBLE);
                            }
                            break;
                        case 1:
                            if(ObjectIsNullUtils.TextIsNull(photoUrl)){
                                photo2.setImageBitmap(compressImage(photoUrl));
                                findViewById(R.id.img_delete2).setVisibility(View.VISIBLE);
                            }
                            break;
                        case 2:
                            if(ObjectIsNullUtils.TextIsNull(photoUrl)){
                                photo3.setImageBitmap(compressImage(photoUrl));
                                findViewById(R.id.img_delete3).setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                    sum++;
                    tv_pictureNum.setText(sum + "");
                    break;
            }
        }
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (!Constants.connect) {
            ConstantsMethod.showTip(FeedBackActivity.this, "网络异常");
        }
    }


    /**
     * 压缩拍照的图片
     */
    private Bitmap compressImage(String path){
        ImageCompress compress = new ImageCompress();
        ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
        options.uri = Uri.fromFile(new File(path));
        options.maxWidth = 150;
        options.maxHeight = 150;
        Bitmap bitmap = compress.compressFromUri(FeedBackActivity.this, options);
        return bitmap;
    }


    /**
     * 上传信息
     */
    private void pull() {
        //上传图片
        connectWork();
        new ProgressBarLoadUtils(this).startProgressDialog();
        if (sum != 0) {
            postPhoto();
        } else {
            addDatum(0);
        }
    }

    //上传图片至服务器
    private void postPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = OkHttpManager.myClient();
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for (int i = 0; i < 3; i++) {
                    if (mData.get(i).getPhotoUrl() != null) {
                        File file = new File(mData.get(i).getPhotoUrl());
                        builder.addFormDataPart("img",mData.get(i).getNum() + ".jpg", RequestBody.create(Constants.MEDIA_TYPE_PNG, file));
                    }
                }
                builder.addFormDataPart("attachment_id", "0")
                        .addFormDataPart("visa_datum_type", "4")
                        .addFormDataPart("show_order", "0")
                        .addFormDataPart("is_del", "0");

                MultipartBody requestBody = builder.build();
                //构建请求
                final Request request = new Request.Builder()
                        .url(UrlSet.upload)//地址
                        .post(requestBody)//添加请求体
                        .build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(FeedBackActivity.this, "网络异常！");
                                new ProgressBarLoadUtils(FeedBackActivity.this).stopProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final ResultId resultId = gson.fromJson(str, ResultId.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new ProgressBarLoadUtils(FeedBackActivity.this).stopProgressDialog();
                                switch (resultId.getError_code()) {
                                    case 0:
                                        addDatum(resultId.getReason_id());
                                        break;
                                    case 1:
                                        ConstantsMethod.showTip(FeedBackActivity.this, resultId.getReason());
                                        break;
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    /**
     * 新增数据
     */

    private void addDatum(final int attachment_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.AvaChatLogSave;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("token", Constants.TOKEN)
                        .add("attachment_id", attachment_id + "")
                        .add("project", et_word.getText().toString())
                        .add("log_type", "2")
                        .add("phone", et_tel.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(FeedBackActivity.this, "网络异常！");
                                new ProgressBarLoadUtils(FeedBackActivity.this).stopProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final Result result = gson.fromJson(str, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new ProgressBarLoadUtils(FeedBackActivity.this).stopProgressDialog();
                                switch (result.getError_code()) {
                                    case 0:
                                        Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                        break;
                                    case 1:
                                        ConstantsMethod.showTip(FeedBackActivity.this, result.getReason());
                                        break;
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

}
