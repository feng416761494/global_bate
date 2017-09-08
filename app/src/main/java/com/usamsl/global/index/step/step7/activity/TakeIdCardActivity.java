package com.usamsl.global.index.step.step7.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step5.util.MyAlertDialog;
import com.usamsl.global.index.step.step5.util.ScreenInfo;
import com.usamsl.global.index.step.step5.util.WheelMain;
import com.usamsl.global.index.step.step7.adapter.TakeIdAdapter;
import com.usamsl.global.index.step.step7.entity.AccountBook;
import com.usamsl.global.index.step.step7.entity.GetContact;
import com.usamsl.global.index.step.step7.entity.IdCard;
import com.usamsl.global.index.step.step7.entity.PhotoSubmission;
import com.usamsl.global.index.step.step7.util.FileUtil;
import com.usamsl.global.index.step.step7.util.HttpUtil;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.MyGridView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * 身份证照片信息
 * 时间：2017/1/13
 */
public class TakeIdCardActivity extends AppCompatActivity {
    private ImageView img_back;
    private EditText et_name;
    private EditText et_folk;
    private TextView et_birthday;
    private EditText et_address;
    private EditText et_cardno;
    private EditText et_issue_authority;
    private EditText et_valid_period;
    private String photoUrl;
    //正反面
    private boolean back = false;
    //身份证信息
    private IdCard idCard;
    private static byte[] bytes;
    private static String extension;
    private static final String action = "idcard.scan";
    //判断是否联网
    private boolean connect = false;
    private PhotoSubmission photoSubmission;
    private int attachment_id;
    private RelativeLayout progressBar;
    //扫描的信息
    private LinearLayout showMessage;
    //是否修改
    private int change = 0;
    private TextView tv_ok;
    private MyGridView gv;
    private List<AccountBook> mData;
    private TakeIdAdapter adapter;
    private String[] photo;
    private RadioButton rdBTnWoman, rdBTnMan;
    private RadioGroup rg;
    //日历
    private WheelMain wheelMain;
    private View timepickerview;
    private MyAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_take_id_card_new);
        ActivityManager.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
        toListener();
    }

    private void timePicker() {
        //日期选择
        LayoutInflater inflater1 = LayoutInflater.from(TakeIdCardActivity.this);
        timepickerview = inflater1.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo1 = new ScreenInfo(TakeIdCardActivity.this);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo1.getHeight();
    }

    private void initDate() {
        //日期控件数据初始化
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
    }

    private void toListener() {
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pull();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    back = true;
                } else {
                    back = false;
                }
                Intent intent = new Intent(TakeIdCardActivity.this, TakePhotoActivity.class);
                Bundle bundle = new Bundle();
                //区分正反面
                if (back) {
                    bundle.putString("FROM_WHERE", "IdCard_front");
                } else {
                    bundle.putString("FROM_WHERE", "IdCard_behind");
                }
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantsCode.ID_CARD_PHOTO);
            }
        });
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idCard.setAddress(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idCard.setBirthday(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDate();
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(TakeIdCardActivity.this)
                        .builder()
                        .setView(timepickerview)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                et_birthday.setText(wheelMain.getTime());
                                idCard.setBirthday(wheelMain.getTime());
                                change = 1;
                            }
                        });
                dialog.show();
            }
        });

        et_cardno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idCard.setCardno(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_folk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idCard.setFolk(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_issue_authority.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idCard.setIssue_authority(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idCard.setName(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_valid_period.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idCard.setValid_period(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                change=1;
                switch (i){
                    case R.id.radioButtonMan:
                        idCard.setSex("男");
                      break;
                    case R.id.radioButtonWoman:
                        idCard.setSex("女");
                        break;
                }
            }
        });
        /*et_valid_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDate();
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(TakeIdCardActivity.this)
                        .builder()
                        .setView(timepickerview)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                et_valid_period.setText(wheelMain.getTime());
                                idCard.setValid_period(wheelMain.getTime());
                                change = 1;
                            }
                        });
                dialog.show();
            }
        });*/
    }

    private void initData() {
        timePicker();
        mData = new ArrayList<>();
        connectWork();
        idCard = new IdCard();
        photoSubmission = getIntent().getParcelableExtra("photoSubmission");
        if (photoSubmission.getPhotoUrl() != null) {
            photo = photoSubmission.getPhotoUrl().split("#");
            if (Constants.show_order.get(0) == 1) {
                mData.add(new AccountBook(photo[0], Constants.show_order.get(0)));
                mData.add(new AccountBook(photo[1], Constants.show_order.get(1)));
                idCard.setPositive(photo[0]);
                idCard.setBack(photo[1]);
            } else {
                mData.add(new AccountBook(photo[1], Constants.show_order.get(1)));
                mData.add(new AccountBook(photo[0], Constants.show_order.get(0)));
                idCard.setPositive(photo[1]);
                idCard.setBack(photo[0]);
            }
            getData();
        } else {
            mData.add(new AccountBook(null, 1));
            mData.add(new AccountBook(null, 2));
            showMessage.setVisibility(View.GONE);
        }
        adapter = new TakeIdAdapter(this, mData);
        gv.setAdapter(adapter);
    }

    /**
     * 使用已有联系人时加载信息
     */
    private void getData() {
        new ProgressBarLoadUtils(this).startProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get请求上传
                String url = UrlSet.getContact + photoSubmission.getContact_id();
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(url);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final GetContact contacts = gson.fromJson(str, GetContact.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (contacts.getError_code()) {
                                    case 0:
                                        showMessage.setVisibility(View.VISIBLE);
                                        GetContact.ResultBean bean = contacts.getResult();
                                        et_name.setText(bean.getContact_name());
                                        et_folk.setText(bean.getNationality());
//                                        et_sex.setText(bean.getContact_sex());
                                        if (ObjectIsNullUtils.TextIsNull(bean.getContact_sex())) {
                                            if (bean.getContact_sex().equals("女")) {
                                                rdBTnWoman.setChecked(true);
                                                rdBTnMan.setChecked(false);
                                                idCard.setSex("女");
                                            } else {
                                                rdBTnWoman.setChecked(false);
                                                rdBTnMan.setChecked(true);
                                                idCard.setSex("男");
                                            }
                                        }
                                        et_address.setText(bean.getAddress());
                                        et_birthday.setText(bean.getContact_birth());
                                        et_cardno.setText(bean.getContact_number());
                                        et_valid_period.setText(bean.getId_validity());
                                        et_issue_authority.setText(bean.getId_place_issuse());
                                        idCard.setBirthday(bean.getContact_birth());
                                        idCard.setValid_period(bean.getId_validity());
                                        idCard.setSex(bean.getContact_sex());
                                        change = 0;
                                        break;
                                }

                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void initView() {
        rg = (RadioGroup) findViewById(R.id.radioGroupSex);
        gv = (MyGridView) findViewById(R.id.gv);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        showMessage = (LinearLayout) findViewById(R.id.linearLayout);
        progressBar = (RelativeLayout) findViewById(R.id.ll_progress);
        et_name = (EditText) findViewById(R.id.et_name);
        rdBTnWoman = (RadioButton) findViewById(R.id.radioButtonWoman);
        rdBTnMan = (RadioButton) findViewById(R.id.radioButtonMan);
        et_folk = (EditText) findViewById(R.id.et_folk);
        et_birthday = (TextView) findViewById(R.id.et_birthday);
        et_address = (EditText) findViewById(R.id.et_address);
        et_cardno = (EditText) findViewById(R.id.et_cardno);
        et_issue_authority = (EditText) findViewById(R.id.et_issue_authority);
        et_valid_period = (EditText) findViewById(R.id.et_valid_period);
        img_back = (ImageView) findViewById(R.id.img_back);

    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connect = true;
        } else {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "网络异常");
        }
    }

    /**
     * 上传资料
     */
    private void pull() {
        //上传图片
        connectWork();
        if (connect && idCard.getBack() != null && idCard.getPositive() != null &&
                idCard.getAddress() != null && idCard.getBirthday() != null &&
                idCard.getCardno() != null && idCard.getFolk() != null
                && idCard.getName() != null && idCard.getIssue_authority() != null && idCard.getSex() != null &&
                idCard.getValid_period() != null) {
            switch (change) {
                case 0:
                    finish();
                    break;
                case 1:
                    int name, folk = -1;
                    if (!photoSubmission.getContact_name().equals(idCard.getName())) {
                        ConstantsMethod.showTip(TakeIdCardActivity.this, "姓名与联系人" + photoSubmission.getContact_name() + "不符");
                        name = -1;
                    } else {
                        name = 0;

                        if (Constants.COUNTRY_ID == 11) {
                            if (idCard.getFolk().indexOf("回") != -1 || idCard.getFolk().indexOf("维吾尔") != -1) {
                                ConstantsMethod.showTip(TakeIdCardActivity.this, "本系统暂不受理");
                                folk = -1;
                            } else {
                                folk = 0;
                            }
                        } else {
                            folk = 0;
                        }
                    }

                    if (folk == 0 && name == 0) {
                        //用户的照片没有发生变化，只是更改了文字信息
                        if (idCard.getPositive().substring(0, 1).equals("h") && idCard.getBack().substring(0, 1).equals("h")) {
                            updateContacts();
//                            updateDatum();
                        } else {
                            postPhoto();
                        }
                    }
                    break;
            }
        } else if (idCard.getBack() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请拍摄身份证反面照片");
        } else if (idCard.getPositive() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请拍摄身份证正面照片");
        } else if (idCard.getAddress() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请输入住址");
        } else if (idCard.getBirthday() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请选择出生日期");
        } else if (idCard.getCardno() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请输入身份证号");
        } else if (idCard.getFolk() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请输入民族");
        } else if (idCard.getName() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请输入名字");
        } else if (idCard.getIssue_authority() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请输入签发机关");
        } else if (idCard.getSex() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请选择性别");
        } else if (idCard.getValid_period() == null) {
            ConstantsMethod.showTip(TakeIdCardActivity.this, "请选择有效日期");
        }
    }

    /**
     * 修改联系人
     */
    private void updateContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String contactSex = "";
                if (rdBTnWoman.isChecked()) {
                    contactSex = "女";
                } else {
                    contactSex = "男";
                }
                String url = UrlSet.updateContact;      //      /GlobalVisa/app/contact/updateContact?
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("contact_name", et_name.getText().toString())
                        .add("contact_id", photoSubmission.getContact_id() + "")
                        .add("contact_number", et_cardno.getText().toString())
                        .add("nationality", et_folk.getText().toString())
                        .add("contact_birth", et_birthday.getText().toString())
                        .add("address", et_address.getText().toString())
                        .add("id_place_issuse", et_issue_authority.getText().toString())
                        .add("id_validity", et_valid_period.getText().toString())
                        .add("contact_sex", contactSex)
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
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final Result r = gson.fromJson(str, Result.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (r.getError_code()) {

                                    case 0:
                                        TakeIdCardActivity.this.finish();
                                        Toast.makeText(TakeIdCardActivity.this, "信息修改成功", Toast.LENGTH_SHORT).show();
                                        break;

                                    case 1:
                                        Toast.makeText(TakeIdCardActivity.this, r.getReason(), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    //上传图片至服务器
    private void postPhoto() {
        String show_order = null;
        OkHttpClient client = OkHttpManager.myClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(idCard.getPositive());
        File file1 = new File(idCard.getBack());
        if (file != null && !idCard.getPositive().substring(0, 1).equals("h")) {
            builder.addFormDataPart("img1", "1.jpg", RequestBody.create(Constants.MEDIA_TYPE_PNG, file));
            show_order = "1";
        }


        if (file1 != null && !idCard.getBack().substring(0, 1).equals("h")) {
            builder.addFormDataPart("img2", "2.jpg", RequestBody.create(Constants.MEDIA_TYPE_PNG, file1));
            if (show_order != null) {
                show_order = show_order + ",2";
            } else {
                show_order = "2";
            }
        }
        builder.addFormDataPart("attachment_id", photoSubmission.getAttachment_id() + "")
                .addFormDataPart("visa_datum_type", photoSubmission.getVisa_datum_type())
                .addFormDataPart("visa_datum_name", photoSubmission.getTypeMaterial())
                .addFormDataPart("contact_id", photoSubmission.getContact_id() + "")
                .addFormDataPart("is_del", "0");
        if (show_order != null) {
            builder.addFormDataPart("show_order", show_order);
        }
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(UrlSet.upload)//地址             /GlobalVisa/appfileupload/upload?
                .post(requestBody)//添加请求体
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConstantsMethod.showTip(TakeIdCardActivity.this, "网络异常");
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
                        switch (resultId.getError_code()) {
                            case 0:
                                attachment_id = resultId.getReason_id();
                                Constants.setPhoto = idCard.getPositive() + "#" + idCard.getBack();
                                updateDatum();
                                updateContacts();
                                Intent intent = new Intent();
                                intent.putExtra("attachment_id", attachment_id);
                                setResult(Activity.RESULT_OK, intent);
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
                String url = UrlSet.orderDatumUpdate;           //      /GlobalVisa/app/order/orderDatumUpdate
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
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response)
                            throws IOException {
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            new ProgressBarLoadUtils(this).startProgressDialog();
            switch (requestCode) {
                case ConstantsCode.ID_CARD_PHOTO:
                    //本地文件存储路径
                    photoUrl = data.getStringExtra("photoUrl");
                    if (photoUrl != null) {
                        if (back) {
                            idCard.setPositive(photoUrl);
                            mData.get(0).setPhotoUrl(photoUrl);
                            adapter.notifyDataSetChanged();
                        } else {
                            idCard.setBack(photoUrl);
                            mData.get(1).setPhotoUrl(photoUrl);
                            adapter.notifyDataSetChanged();
                        }
                        extension = getExtensionByPath(photoUrl);
                        bytes = FileUtil.getByteFromPath(photoUrl);
                        if (bytes != null) {
                            new MyAsynTask().execute();
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 根据路径获取文件扩展名
     *
     * @param path
     */
    private String getExtensionByPath(String path) {
        if (path != null) {
            return path.substring(path.lastIndexOf(".") + 1);
        }
        return null;
    }

    class MyAsynTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            return startScan();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                handleResult(result);
            }
        }

    }

    /**
     * 处理服务器返回的结果
     *
     * @param result
     */
    private void handleResult(String result) {
        new ProgressBarLoadUtils(TakeIdCardActivity.this).stopProgressDialog();
        showMessage.setVisibility(View.VISIBLE);
        String name, sex, folk, birthday, address, cardno, issue_authority, valid_period;
        int nameStart, nameEnd, sexStart, sexEnd, folkStart, folkEnd, birthdayStart, birthdayEnd, addressStart,
                addressEnd, cardnoStart, cardnoEnd, issue_authorityStart, issue_authorityEnd, valid_periodStart, valid_periodEnd;

        if (result.indexOf("name") != -1) {
            if (back) {
                nameStart = result.indexOf("<name>");
                nameEnd = result.indexOf("</name>");
                name = result.substring(nameStart + 6, nameEnd);
                if (!name.equals("")) {
                    et_name.setText(name);
                }
                sexStart = result.indexOf("<sex>");
                sexEnd = result.indexOf("</sex>");
                sex = result.substring(sexStart + 5, sexEnd);
                if (!sex.equals("")) {
//                    et_sex.setText(sex);
                    if (sex.equals("女")) {
                        rdBTnWoman.setChecked(true);
                    } else {
                        rdBTnMan.setChecked(true);
                    }
                }
                folkStart = result.indexOf("<folk>");
                folkEnd = result.indexOf("</folk>");
                folk = result.substring(folkStart + 6, folkEnd);
                if (!folk.equals("")) {
                    et_folk.setText(folk);
                }
                birthdayStart = result.indexOf("<birthday>");
                birthdayEnd = result.indexOf("</birthday>");
                birthday = result.substring(birthdayStart + 10, birthdayEnd);
                if (!birthday.equals("")) {
                    et_birthday.setText(formatToBeMillis(birthday));
                }
                addressStart = result.indexOf("<address>");
                addressEnd = result.indexOf("</address>");
                address = result.substring(addressStart + 9, addressEnd);
                if (!address.equals("")) {
                    et_address.setText(address);
                }
                cardnoStart = result.indexOf("<cardno>");
                cardnoEnd = result.indexOf("</cardno>");
                cardno = result.substring(cardnoStart + 8, cardnoEnd);
                if (!cardno.equals("")) {
                    et_cardno.setText(cardno);
                }

            } else {
                issue_authorityStart = result.indexOf("<issue_authority>");
                issue_authorityEnd = result.indexOf("</issue_authority>");
                issue_authority = result.substring(issue_authorityStart + 17, issue_authorityEnd);
                if (!issue_authority.equals("")) {
                    et_issue_authority.setText(issue_authority);
                }
                valid_periodStart = result.indexOf("<valid_period>");
                valid_periodEnd = result.indexOf("</valid_period>");
                valid_period = result.substring(valid_periodStart + 14, valid_periodEnd);
                if (!valid_period.equals("")) {
                    et_valid_period.setText(valid_period);
                }
            }
        }
    }

    /**
     * 时间格式转换
     *
     * @param birthday
     */
    private String formatToBeMillis(String birthday) {
        String data = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy年MM月dd日").parse(birthday));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            data = format.format(new Date(calendar.getTimeInMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String startScan() {
        String xml = HttpUtil.getSendXML(action, extension);
        return HttpUtil.send(xml, bytes);
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
