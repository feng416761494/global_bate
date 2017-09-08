package com.usamsl.global.index.step.step7.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step7.adapter.AccountBookAdapter;
import com.usamsl.global.index.step.step7.entity.AccountBook;
import com.usamsl.global.index.step.step7.entity.GetContact;
import com.usamsl.global.index.step.step7.entity.PhotoSubmission;
import com.usamsl.global.index.step.step7.util.FileUtil;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.MyGridView;
import com.usamsl.global.view.MyScrollView;

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

/**
 * 户口本
 * 时间：2017/3/9
 */
public class AccountBookActivity_new extends AppCompatActivity {
    //列表
    private MyGridView gv;
    private ImageView img_back;
    private AccountBookAdapter accountBookAdapter;
    private List<AccountBook> mData;
    private RelativeLayout progressBar;
    private PhotoSubmission photoSubmission;
    private String[] url;
    private int attachment_id;
    //照片路径
    private String photoUrl = null;
    //点击的第几个
    private int which = 0;
    //是否改变
    private boolean change = false;
    //确定
    private TextView tv_update;
    //一共多少数量
    private int sum = 0;
    //增加和删除，修改图片
    String show_order = null;
    //最外层
    private RelativeLayout rl_cancel1, rl_cancel2;
    private MyScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_book_activity_new);
        ActivityManager.getInstance().addActivity(this);
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
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Constants.UPDATE) {
                    which = i;
                    Intent intent = new Intent(AccountBookActivity_new.this, TakePhotoActivity.class);
                    startActivityForResult(intent, ConstantsCode.ACCOUNT_BOOK);
                }
            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constants.UPDATE = true;
                accountBookAdapter.notifyDataSetChanged();
                return true;
            }
        });
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*update = !update;
                Constants.UPDATE = update;
                if (update) {
                    tv_update.setText("确定");
                } else {
                    tv_update.setText("编辑");
                }
                accountBookAdapter.notifyDataSetChanged();*/
                pull();
            }
        });
        accountBookAdapter.setUpdatePhotoListener(new AccountBookAdapter.UpdatePhotoListener() {
            @Override
            public void update(int id, int i) {
                change = true;
                mData.remove(i);
                accountBookAdapter.notifyDataSetChanged();
                if (show_order != null) {
                    show_order = show_order + "," + id;
                } else {
                    show_order = id + "";
                }
            }
        });
        rl_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.UPDATE){
                    Constants.UPDATE = false;
                    accountBookAdapter.notifyDataSetChanged();
                }
            }
        });
        rl_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.UPDATE){
                    Constants.UPDATE = false;
                    accountBookAdapter.notifyDataSetChanged();
                }
            }
        });
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.UPDATE){
                    Constants.UPDATE = false;
                    accountBookAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData() {
        photoSubmission = getIntent().getParcelableExtra("photoSubmission");
        mData = new ArrayList<>();
        if (photoSubmission.getPhotoUrl() != null) {
            url = photoSubmission.getPhotoUrl().split("#");
            for (int i = 0; i < url.length; i++) {
                mData.add(new AccountBook(url[i], Constants.show_order.get(i)));
            }
            mData.add(new AccountBook("h", url.length + 1));
        } else {
            mData.add(new AccountBook("h", 1));
        }
        //找最大的show_order
        int t = 0;
        if (mData.size() > 1) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getId() > t) {
                    t = mData.get(i).getId();
                }
            }
        }
        sum = t;
        accountBookAdapter = new AccountBookAdapter(this, mData);
        gv.setAdapter(accountBookAdapter);
    }

    private void initView() {
        tv_update = (TextView) findViewById(R.id.tv_update);
        img_back = (ImageView) findViewById(R.id.img_back);
        gv = (MyGridView) findViewById(R.id.gv);
        progressBar = (RelativeLayout) findViewById(R.id.ll_progress);
        rl_cancel1 = (RelativeLayout) findViewById(R.id.rl_cancel1);
        rl_cancel2 = (RelativeLayout) findViewById(R.id.rl_cancel2);
        scrollView=(MyScrollView)findViewById(R.id.scrollView);
    }

    /**
     * 上传信息
     */
    private void pull() {
        //上传图片
        connectWork();
        progressBar.setVisibility(View.VISIBLE);
        if (change) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    postPhoto();
                }
            }).start();
        } else {
            finish();
        }
    }

    //上传图片至服务器
    private void postPhoto() {
        int num = 0;
        OkHttpClient client = OkHttpManager.myClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < mData.size() - 1; i++) {
            if (mData.get(i).getPhotoUrl() != null) {
                if (!mData.get(i).getPhotoUrl().substring(0, 2).equals("ht")) {
                    num++;
                    File file = new File(mData.get(i).getPhotoUrl());
                    builder.addFormDataPart("img" + mData.get(i).getId(), mData.get(i).getId() + ".jpg", RequestBody.create(Constants.MEDIA_TYPE_PNG, file));
                    if (show_order != null) {
                        show_order = show_order + "," + mData.get(i).getId();
                    } else {
                        show_order = mData.get(i).getId() + "";
                    }
                }
            }
        }
        builder.addFormDataPart("attachment_id", photoSubmission.getAttachment_id() + "")
                .addFormDataPart("visa_datum_type", photoSubmission.getVisa_datum_type())
                .addFormDataPart("visa_datum_name", photoSubmission.getTypeMaterial())
                .addFormDataPart("contact_id", photoSubmission.getContact_id() + "");
        if (show_order != null) {
            builder.addFormDataPart("show_order", show_order);
        }
        if (num == 0) {
            builder.addFormDataPart("is_del", "1");
        } else {
            builder.addFormDataPart("is_del", "0");
        }
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
                        ConstantsMethod.showTip(AccountBookActivity_new.this, "网络异常！");
                        progressBar.setVisibility(View.GONE);
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
                        progressBar.setVisibility(View.GONE);
                        switch (resultId.getError_code()) {
                            case 0:
                                attachment_id = resultId.getReason_id();
                                for (int i = 1; i < mData.size() - 1; i++) {
                                    Constants.setPhoto = Constants.setPhoto + "#" + mData.get(i).getPhotoUrl();
                                }
                                Intent intent = new Intent();
                                intent.putExtra("attachment_id", attachment_id);
                                setResult(Activity.RESULT_OK, intent);
                                updateDatum();
                                finish();
                                break;
                        }
                    }
                });
            }
        });
    }

    /**
     * 更新数据
     */

    private void updateDatum() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlSet.orderDatumUpdate;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("order_datum_id", photoSubmission.getOrder_datum_id() + "")
                        .add("attachment_id", attachment_id + "")
                        .add("order_id", photoSubmission.getOrder_id() + "")
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
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            Gson gson = new Gson();
                            final Result r = gson.fromJson(str, Result.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (r.getError_code()) {
                                        case 1:
                                            progressBar.setVisibility(View.GONE);
                                            break;
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        super.onActivityResult(arg0, arg1, data);
        if (data == null) {
            return;
        }
        if (arg1 == Activity.RESULT_OK) {
            switch (arg0) {
                case ConstantsCode.ACCOUNT_BOOK:
                    if (sum == 0) {
                        sum = 2;
                    } else {
                        sum++;
                    }
                    change = true;
                    photoUrl = data.getStringExtra("photoUrl");
                    if(ObjectIsNullUtils.TextIsNull(photoUrl)){
                        mData.get(which).setPhotoUrl(photoUrl);
                        if (which == mData.size() - 1) {
                            mData.add(new AccountBook("h", sum));
                        }
                        accountBookAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (!Constants.connect) {
            ConstantsMethod.showTip(AccountBookActivity_new.this, "网络异常");
        }
    }

    @Override
    protected void onDestroy() {
        Constants.UPDATE = false;
        System.gc();
        super.onDestroy();
    }
}
