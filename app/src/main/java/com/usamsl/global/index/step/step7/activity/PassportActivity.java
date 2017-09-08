package com.usamsl.global.index.step.step7.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hp.hpl.sparta.Text;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.entity.IndexVisa;
import com.usamsl.global.index.entity.VisaCity;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step1.entity.District;
import com.usamsl.global.index.step.step4.entity.AllContacts;
import com.usamsl.global.index.step.step5.util.MyAlertDialog;
import com.usamsl.global.index.step.step5.util.ScreenInfo;
import com.usamsl.global.index.step.step5.util.WheelMain;
import com.usamsl.global.index.step.step7.entity.GetContact;
import com.usamsl.global.index.step.step7.entity.IdCard;
import com.usamsl.global.index.step.step7.entity.Passport;
import com.usamsl.global.index.step.step7.entity.PhotoSubmission;
import com.usamsl.global.index.step.step7.util.FileUtil;
import com.usamsl.global.index.step.step7.util.HttpUtil;
import com.usamsl.global.index.step.step7.util.PhotoBitmapManager;
import com.usamsl.global.index.util.XCRoundRectImageView1;
import com.usamsl.global.login.entity.Login;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.RecyclerImageView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.Url;

/**
 * 时间：2017/1/16
 * 描述：返回护照信息
 */
public class PassportActivity extends AppCompatActivity {
    private static byte[] bytes;
    private static String extension;
    public static final String action = "passport.scan";
    private EditText et_name;
    private EditText et_namech;
    private EditText et_cardno;
    private RadioButton rdBTnWoman, rdBTnMan;
    private RadioGroup rg;
    private EditText et_sexCH;
    private TextView et_birthday;
    private EditText et_address;
    private EditText et_addressCH;
    private EditText et_issueauthority;
    private EditText et_issueAddressCH;
    private TextView et_issuedate;
    private TextView et_valid_period;
    private EditText et_nation;
    private TextView et_nationCHN;
    //照片路径
    private String photoUrl = null;
    //护照信息
    private Passport passport;
    private ImageView img_back;
    private RelativeLayout rl;
    private XCRoundRectImageView1 img_photo;
    private PhotoSubmission photoSubmission;
    private int attachment_id;
    private RelativeLayout progressBar;
    private LinearLayout showMessage;
    //是否作修改
    private int change = 0;
    private TextView tv_ok;
    //日历
    private WheelMain wheelMain;
    private View timepickerview;
    private MyAlertDialog dialog;
    //最外层
    private RelativeLayout rl1,rl2;
    private String contactSexEn = "";
    private RelativeLayout relativePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_passport_new);
        ActivityManager.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
        toListener();
    }


    private void initView() {
        rl2 = (RelativeLayout) findViewById(R.id.rl2);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rg=(RadioGroup)findViewById(R.id.radioGroupSex);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        showMessage = (LinearLayout) findViewById(R.id.linearLayout);
        progressBar = (RelativeLayout) findViewById(R.id.ll_progress);
        et_name = (EditText) findViewById(R.id.et_name);
        et_namech = (EditText) findViewById(R.id.et_namech);
        et_sexCH = (EditText) findViewById(R.id.et_sexCH);
        rdBTnWoman = (RadioButton) findViewById(R.id.radioButtonWoman);
        rdBTnMan = (RadioButton) findViewById(R.id.radioButtonMan);
        et_birthday = (TextView) findViewById(R.id.et_birthday);
        et_address = (EditText) findViewById(R.id.et_address);
        et_cardno = (EditText) findViewById(R.id.et_cardno);
        et_issueauthority = (EditText) findViewById(R.id.et_issueauthority);
        et_valid_period = (TextView) findViewById(R.id.et_valid_period);
        et_addressCH = (EditText) findViewById(R.id.et_addressCH);
        et_issueAddressCH = (EditText) findViewById(R.id.et_issueAddressCH);
        et_nation = (EditText) findViewById(R.id.et_nation);
        et_nationCHN = (TextView) findViewById(R.id.et_nationCHN);
        et_issuedate = (TextView) findViewById(R.id.et_issuedate);
        img_back = (ImageView) findViewById(R.id.img_back);
        rl = (RelativeLayout) findViewById(R.id.rl);
        img_photo = (XCRoundRectImageView1) findViewById(R.id.img_photo);
        relativePhoto = (RelativeLayout) findViewById(R.id.relativePhoto);
    }

    private void initData() {
        timePicker();
        connectWork();
        passport = new Passport();
        photoSubmission = getIntent().getParcelableExtra("photoSubmission");
        if (photoSubmission.getPhotoUrl() != null) {
            if (photoSubmission.getPhotoUrl().substring(0, 1).equals("h")) {
                //当有图片的时候将背景的圆角框去掉
                relativePhoto.setBackgroundResource(0);
                ImageLoadUtils.loadCameraImage(photoSubmission.getPhotoUrl(), img_photo, this);
                relativePhoto.setPadding(0,0,0,0);
//                Picasso.with(this)
//                        .load(photoSubmission.getPhotoUrl())
//                        .memoryPolicy(MemoryPolicy.NO_CACHE)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.camera)
//                        .into(img_photo);

            } else {
                img_photo.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap(photoSubmission.getPhotoUrl(),
                        img_photo.getWidth(), img_photo.getHeight()));
            }
            getData();
            passport.setPhotoUrl(photoSubmission.getPhotoUrl());
        } else {
            showMessage.setVisibility(View.GONE);
        }
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstantsMethod.showTip(PassportActivity.this, "网络异常！");
                                new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
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
                                        et_name.setText(bean.getE_contact_name());
                                        et_namech.setText(bean.getContact_name());
                                        et_issuedate.setText(bean.getPassport_birth_time());
                                        if (ObjectIsNullUtils.TextIsNull(bean.getContact_sex())) {
                                            if (bean.getContact_sex().equals("女")) {
                                                rdBTnWoman.setChecked(true);
                                                rdBTnMan.setChecked(false);
                                                passport.setSex("女");
                                            } else {
                                                rdBTnWoman.setChecked(false);
                                                rdBTnMan.setChecked(true);
                                                passport.setSex("男");
                                            }
                                        }
                                        et_sexCH.setText(bean.getE_contact_sex());
                                        et_nation.setText(bean.getPassport_nationality());
                                        et_address.setText(bean.getE_passport_place_birth());
                                        et_addressCH.setText(bean.getPassport_place_birth());
                                        et_issueAddressCH.setText(bean.getPassport_place_issuse());
                                        et_issueauthority.setText(bean.getE_passport_place_issuse());
                                        et_birthday.setText(bean.getContact_birth());
                                        et_cardno.setText(bean.getPassport_no());
                                        et_valid_period.setText(bean.getPassport_validity_time());
                                        passport.setBirthday(bean.getContact_birth());
                                        passport.setIssueDate(bean.getPassport_birth_time());
                                        passport.setValidPeriod(bean.getPassport_validity_time());
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

    private void timePicker() {
        //日期选择
        LayoutInflater inflater1 = LayoutInflater.from(PassportActivity.this);
        timepickerview = inflater1.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo1 = new ScreenInfo(PassportActivity.this);
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
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantsMethod.cancelKeyboard(PassportActivity.this, view);
            }
        });
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantsMethod.cancelKeyboard(PassportActivity.this, view);
            }
        });
        //罩层
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassportActivity.this, TakePhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FROM_WHERE", "Passport");
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantsCode.PASSPORT_PHOTO);
            }
        });
        et_valid_period.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setValidPeriod(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_valid_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDate();
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(PassportActivity.this)
                        .builder()
                        .setView(timepickerview)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                et_valid_period.setText(wheelMain.getTime());
                                passport.setValidPeriod(wheelMain.getTime());
                                change = 1;
                            }
                        });
                dialog.show();
            }
        });
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setAddress(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_addressCH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setAddressCH(charSequence.toString());
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
                passport.setBirthday(charSequence.toString());
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
                dialog = new MyAlertDialog(PassportActivity.this)
                        .builder()
                        .setView(timepickerview)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                et_birthday.setText(wheelMain.getTime());
                                passport.setBirthday(wheelMain.getTime());
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
                passport.setCardno(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_issueAddressCH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setIssueAddressCH(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_issueauthority.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setIssueAuthority(charSequence.toString());
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
                passport.setName(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_namech.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setNameCh(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_nation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setNation(charSequence.toString());
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
                        passport.setSex("男");
                        break;
                    case R.id.radioButtonWoman:
                        passport.setSex("女");
                        break;
                }
            }
        });
       /* if (rdBTnWoman.isChecked()) {
            passport.setSex("女");
        } else {
            passport.setSex("男");
        }
        rdBTnWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change = 1;
                passport.setSex("女");
            }
        });
        rdBTnMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change = 1;
                passport.setSex("男");
            }
        });*/
        et_sexCH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setSexCH(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_issuedate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passport.setIssueDate(charSequence.toString());
                change = 1;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_issuedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDate();
                if (dialog != null) {
                    dialog.removeAllView();
                }
                dialog = new MyAlertDialog(PassportActivity.this)
                        .builder()
                        .setView(timepickerview)
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                et_issuedate.setText(wheelMain.getTime());
                                passport.setIssueDate(wheelMain.getTime());
                                change = 1;
                            }
                        });
                dialog.show();
            }
        });
    }

    /**
     * 上传信息
     */
    private void pull() {
        //上传图片
        connectWork();
        if (passport.getSex() != null && passport.getName() != null && passport.getAddress() != null &&
                passport.getAddressCH() != null && passport.getBirthday() != null && passport.getCardno() != null &&
                passport.getIssueAddressCH() != null && passport.getIssueAuthority() != null && passport.getIssueDate() != null
                && passport.getNameCh() != null && passport.getNation() != null
                && passport.getPhotoUrl() != null) {
            switch (change) {
                case 0:
                    finish();
                    break;
                case 1:
                    int name, folk = -1;
                    if (!photoSubmission.getContact_name().equals(passport.getNameCh())) {
                        ConstantsMethod.showTip(PassportActivity.this, "姓名与联系人" + photoSubmission.getContact_name() + "不符");
                        name = -1;
                    } else {
                        name = 0;
                        if (passport.getNation().indexOf("回") == -1 && passport.getNation().indexOf("维吾尔") == -1) {
                            folk = 0;
                        } else {
                            ConstantsMethod.showTip(PassportActivity.this, "本系统暂不受理");
                            folk = -1;
                        }
                    }

                    if (folk == 0 && name == 0) {
                        progressBar.setVisibility(View.VISIBLE);
                        new ProgressBarLoadUtils(this).startProgressDialog();
                        if (photoSubmission.getPhotoUrl() != null && photoSubmission.getPhotoUrl().substring(0, 1).equals("h") && photoUrl == null) {
                            updateContacts();
                        } else {
                            postPhoto();
                        }
                    }
                    break;
            }
        } else if (passport.getSex() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请选择性别");
        } else if (passport.getName() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请输入英文名字");
        } else if (passport.getAddress() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请输入英文地址");
        } else if (passport.getAddressCH() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请输入中文地址");
        } else if (passport.getBirthday() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请选择出生日期");
        } else if (passport.getCardno() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请输入护照号");
        } else if (passport.getIssueAddressCH() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请输入英文签发地址");
        } else if (passport.getIssueAuthority() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请输入中文签发地址");
        } else if (passport.getIssueDate() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请选择签发日期");
        } else if (passport.getNameCh() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请输入中文名字");
        } else if (passport.getNation() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请输入国际");
        } else if (passport.getPhotoUrl() == null) {
            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
            ConstantsMethod.showTip(PassportActivity.this, "请拍摄护照照片");
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
                String url = UrlSet.updateContact;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("contact_id", photoSubmission.getContact_id() + "")
                        .add("e_contact_name", et_name.getText().toString())
                        .add("contact_sex", contactSex)
                        .add("e_contact_sex", et_sexCH.getText().toString())
                        .add("e_passport_place_issuse", et_issueauthority.getText().toString())
                        .add("passport_place_issuse", et_issueAddressCH.getText().toString())
                        .add("passport_place_birth", et_addressCH.getText().toString())
                        .add("e_passport_place_birth", et_address.getText().toString())
                        .add("contact_birth", et_birthday.getText().toString())
                        .add("passport_nationality", et_nation.getText().toString())
                        .add("passport_validity_time", et_valid_period.getText().toString())
                        .add("passport_birth_time", et_issuedate.getText().toString())
                        .add("passport_no", et_cardno.getText().toString())
                        .add("now_nationality",et_nationCHN.getText().toString())
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
                                new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            Gson gson = new Gson();
                            final Result r = gson.fromJson(str, Result.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
                                    progressBar.setVisibility(View.GONE);
                                    switch (r.getError_code()) {
                                        case 0:
                                            PassportActivity.this.finish();
                                            break;
                                        case 1:
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

    //上传图片至服务器
    private void postPhoto() {
        OkHttpClient client = OkHttpManager.myClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (photoUrl == null) {
            photoUrl = photoSubmission.getPhotoUrl();
            Toast.makeText(this, "图片无变化", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(photoUrl);
        if (file != null) {
            builder.addFormDataPart("img", "1.jpg", RequestBody.create(Constants.MEDIA_TYPE_PNG, file))
                    .addFormDataPart("attachment_id", photoSubmission.getAttachment_id() + "")
                    .addFormDataPart("visa_datum_type", photoSubmission.getVisa_datum_type())
                    .addFormDataPart("visa_datum_name", photoSubmission.getTypeMaterial())
                    .addFormDataPart("contact_id", photoSubmission.getContact_id() + "")
                    .addFormDataPart("show_order", "1")
                    .addFormDataPart("is_del", "0");
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
                        ConstantsMethod.showTip(PassportActivity.this, "网络异常！");
                        progressBar.setVisibility(View.GONE);
                        new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
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
                                Constants.setPhoto = photoUrl;
                                Intent intentReceiver = new Intent(Constants.PHOTO_UPLOAD_SUCCESS);
                                intentReceiver.putExtra("Visa_datum_type",photoSubmission.getVisa_datum_type());
                                PassportActivity.this.sendBroadcast(intentReceiver);
                                Intent intent = new Intent();
                                intent.putExtra("attachment_id", attachment_id);
                                intent.putExtra("photoUrl",photoUrl);
                                setResult(Activity.RESULT_OK, intent);
                                updateDatum();
                                updateContacts();
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
                                new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
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
                                            new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
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
            progressBar.setVisibility(View.VISIBLE);
            new ProgressBarLoadUtils(this).startProgressDialog();
            switch (arg0) {
                case ConstantsCode.PASSPORT_PHOTO:
                    photoUrl = data.getStringExtra("photoUrl");
                    passport.setPhotoUrl(photoUrl);
                    relativePhoto.setBackgroundResource(0);
                    img_photo.setImageBitmap(PhotoBitmapManager.getFitSampleBitmap
                            (photoUrl, img_photo.getWidth(), img_photo.getHeight()));
                    relativePhoto.setPadding(0,0,0,0);
                    extension = getExtensionByPath(photoUrl);
                    bytes = FileUtil.getByteFromPath(photoUrl);
                    if (bytes != null) {
                        new MyAsynTask().execute();
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
            ConstantsMethod.showTip(PassportActivity.this, "网络异常");
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
                new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
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
        new ProgressBarLoadUtils(PassportActivity.this).stopProgressDialog();
        progressBar.setVisibility(View.GONE);
        showMessage.setVisibility(View.VISIBLE);
        String name, namech, sex, sexCH, birthday, address, addressCH, cardno, issueauthority, issueAddressCH, validperiod, issuedate, nation;
        if (result.indexOf("name") != -1) {
            name = result.substring(result.indexOf("<name>") + 6, result.indexOf("</name>"));
            if (!name.equals("")) {
                et_name.setText(name);
            }
            namech = result.substring(result.indexOf("<nameCh>") + 8, result.indexOf("</nameCh>"));
            if (!name.equals("")) {
                et_namech.setText(namech);
            }
            sex = result.substring(result.indexOf("<sex>") + 5, result.indexOf("</sex>"));
            if (!sex.equals("")) {
                et_sexCH.setText(sex);
            }
            sexCH = result.substring(result.indexOf("<sexCH>") + 7, result.indexOf("</sexCH>"));
            passport.setSex(sexCH);
            if (sexCH.equals("女")) {
                rdBTnWoman.setChecked(true);
                rdBTnMan.setChecked(false);
                et_sexCH.setText("F");
            } else {
                rdBTnWoman.setChecked(false);
                rdBTnMan.setChecked(true);
                et_sexCH.setText("M");
            }
            birthday = result.substring(result.indexOf("<birthday>") + 10, result.indexOf("</birthday>"));
            if (!birthday.equals("")) {
                et_birthday.setText(formatToBeMillis(birthday));
            }

            address = result.substring(result.indexOf("<address>") + 9, result.indexOf("</address>"));
            if (!address.equals("")) {
                et_address.setText(address);
            }
            addressCH = result.substring(result.indexOf("<addressCH>") + 11, result.indexOf("</addressCH>"));
            if (!address.equals("")) {
                et_addressCH.setText(addressCH);
            }
            cardno = result.substring(result.indexOf("<cardno>") + 8, result.indexOf("</cardno>"));
            if (!cardno.equals("")) {
                et_cardno.setText(cardno);
            }
            issueauthority = result.substring(result.indexOf("<issueAuthority>") + 16, result.indexOf("</issueAuthority>"));
            if (!issueauthority.equals("")) {
                et_issueauthority.setText(issueauthority);
            }
            issueAddressCH = result.substring(result.indexOf("<issueAddressCH>") + 16, result.indexOf("</issueAddressCH>"));
            if (!issueAddressCH.equals("")) {
                et_issueAddressCH.setText(issueAddressCH);
            }
            issuedate = result.substring(result.indexOf("<issueDate>") + 11, result.indexOf("</issueDate>"));
            if (!issuedate.equals("")) {
                et_issuedate.setText(formatToBeMillis(issuedate));
            }
            validperiod = result.substring(result.indexOf("<validPeriod>") + 13, result.indexOf("</validPeriod>"));
            if (!validperiod.equals("")) {
                et_valid_period.setText(formatToBeMillis(validperiod));
            }
            nation = result.substring(result.indexOf("<nation>") + 8, result.indexOf("</nation>"));
            if (!validperiod.equals("")) {
                et_nation.setText(nation);
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
            calendar.setTime(new SimpleDateFormat("yyyyMMdd").parse(birthday));
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
        img_photo.setImageResource(0);
        img_photo = null;
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
