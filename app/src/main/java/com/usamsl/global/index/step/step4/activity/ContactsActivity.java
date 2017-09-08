package com.usamsl.global.index.step.step4.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsCode;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.activity.EvUsActivity;
import com.usamsl.global.index.activity.EvUsActivity_old;
import com.usamsl.global.index.step.entity.AddOrder;
import com.usamsl.global.index.step.step4.adapter.ContactsAdapter;
import com.usamsl.global.index.step.step4.biz.Contactsbiz;
import com.usamsl.global.index.step.step4.entity.AllContacts;
import com.usamsl.global.index.step.entity.ResultId;
import com.usamsl.global.index.step.step5.activity.DocumentScanningActivity;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.Verification;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.view.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2016/12/26
 * 描述：材料提交中：选择联系人
 */
public class ContactsActivity extends AppCompatActivity implements View.OnClickListener{
    //返回按钮
    private ImageView img_back;
    //使用已有联系人
    private RelativeLayout rl_old;
    //已有联系人列表
    private MyListView lv;
    //新建联系人
    private RelativeLayout rl_new;
    //新建联系人的最外容器
    private RelativeLayout rl_newContacts;
    //新建联系人显示和不显示
    private List<AllContacts.ResultBean> mData;
    private ContactsAdapter contactsAdapter;
    //已有联系人显示和不显示
    boolean contacts = true;
    //新建联系人显示和不显示
    boolean newContacts = false;
    //界面最外层，用于键盘消失
    private LinearLayout ll;
    //中文姓名
    private EditText et_name;
    //电话号码
    private EditText et_tel;
    //提交
    private TextView tv_commit;
    //出行时间
    private TextView tv_time;
    //出行时间
    private RelativeLayout rl_time;
    //返回时间
    private RelativeLayout rl_returnTime;
    private TextView tv_returnTime;
    //常用邮箱
    private EditText et_email;
    //是否联网
    private boolean connect = false;
    //新建联系人状态:0:不改变，1：修改，2：添加
    private int state = -1;
    //联系人的id
    private int contact_id;
    private AddOrder addOrder;
    private RelativeLayout progressBar;
    //返回时间
    private String returnTime;
    //出行时间
    private String travelTime;
    private Animation animation;
    private LayoutAnimationController controller;
    private ImageView imgPoint, imgPoint1;
    //是否来自evus     （1是来自evus    null则是国家签证）.......
    private String isEvus;
    private int creatOrderId = -1;
    //记录是修改的联系人或者是出行时间
    private static int CONTACT_OR_TIME = 0;
    //上一次填写的出行时间和返回时间
    private static String LAST_RETURN_TIME = "";
    private static String LAST_TRAVEL_TIME = "";
    private static int UPDATE_TIMES = 0;
    //用于判断来自Evus的订单，对于联系人的状态管理
    private static int FROM_EVUS_CONTACT_STATUS = 200;//100.选中联系人    200.没有选中的已有联系人
    private static int FROM_EVUS_ICONTACT_CHANGE = -1;//100未改变      200.联系人信息修改
    private Contactsbiz contactsBiz;

    //列表选择的哪一个
    private int positon = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_contacts);
        contactsBiz = new Contactsbiz(this);
        addOrder = getIntent().getParcelableExtra("addOrder");
        isEvus = getIntent().getStringExtra("evus");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ActivityManager.getInstance().addActivity(ContactsActivity.this);
        initView();
        initData();
        toListener();
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            connect = true;
        } else {
            ConstantsMethod.showTip(ContactsActivity.this, "无网络连接");
        }
    }

    /**
     * 界面初始化
     */
    private void initView() {
        progressBar = (RelativeLayout) findViewById(R.id.progress);
        img_back = (ImageView) findViewById(R.id.img_back);
        rl_old = (RelativeLayout) findViewById(R.id.rl_old);
        lv = (MyListView) findViewById(R.id.lv);
        rl_new = (RelativeLayout) findViewById(R.id.rl_new);
        rl_newContacts = (RelativeLayout) findViewById(R.id.rl_newContacts);
        ll = (LinearLayout) findViewById(R.id.ll);
        et_name = (EditText) findViewById(R.id.et_name);
        et_tel = (EditText) findViewById(R.id.et_tel);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        tv_time = (TextView) findViewById(R.id.tv_time);
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
        tv_returnTime = (TextView) findViewById(R.id.tv_returnTime);
        rl_returnTime = (RelativeLayout) findViewById(R.id.rl_returnTime);
        et_email = (EditText) findViewById(R.id.et_email);
        imgPoint = (ImageView) findViewById(R.id.imgPoint);
        imgPoint1 = (ImageView) findViewById(R.id.imgPoint1);
        if (isEvus != null && isEvus.equals("1")) {
            rl_time.setVisibility(View.GONE);
            rl_returnTime.setVisibility(View.GONE);
        }
        img_back.setOnClickListener(this);
        rl_old.setOnClickListener(this);
        rl_new.setOnClickListener(this);
        ll.setOnClickListener(this);
        rl_newContacts.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
        rl_returnTime.setOnClickListener(this);
        rl_time.setOnClickListener(this);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        mData = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(this, mData);
        connectWork();
        if (connect) {
            //有网络连接的情况下先查询所有的联系人
            contactsBiz.getUserAllContacts();
        } else {
            ConstantsMethod.showTip(ContactsActivity.this, "网络错误");
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.img_back:
                this.finish();
                break;

            case R.id.rl_old://已有联系人
                ConstantsMethod.cancelKeyboard(ContactsActivity.this, view);
                myContacts();
                break;

            case R.id.rl_new://新建联系人
                ConstantsMethod.cancelKeyboard(ContactsActivity.this, view);
                newContacts();
                break;

            case R.id.ll:
                ConstantsMethod.cancelKeyboard(ContactsActivity.this, view);
                break;

            case R.id.rl_newContacts://新建联系人的信息部分
                ConstantsMethod.cancelKeyboard(ContactsActivity.this, view);
                break;

            case R.id.tv_commit://提交
                if (isEvus == null || !isEvus.equals("1")) {
                    if (tv_returnTime.getText().length() > 0 && tv_time.getText().length() > 0 && et_name.getText
                            ().toString().length() > 0 && Verification.isMobileNO(et_tel.getText
                            ().toString()) && Verification.isEmail(et_email.getText().toString())) {
                        connectWork();
                        for (int i = 0; i < mData.size(); i++) {
                            if (et_name.getText().toString().equals(mData.get(i).getContact_name()) && i != positon) {
                                Toast.makeText(ContactsActivity.this, "联系人已存在", Toast.LENGTH_SHORT).show();
                                new ProgressBarLoadUtils(ContactsActivity.this).stopProgressDialog();
                                return;
                            }
                        }
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        String travelTime = tv_time.getText().toString();
                        String returnTime = tv_returnTime.getText().toString();
                        try {
                            Date travelData = sdf.parse(travelTime);
                            Date returnData = sdf.parse(returnTime);
                            long travelMillis = travelData.getTime()/1000;
                            long returnMillis = returnData.getTime()/1000;
                            if(travelMillis >= returnMillis){
                                Toast.makeText(ContactsActivity.this, "返回日期要在出行日期之后", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        new ProgressBarLoadUtils(ContactsActivity.this).startProgressDialog();
                        switch (state) {
                            case 0:
                                //该变量用于限制页面返回时重复创建订单
                                if (creatOrderId == -1) {
                                    toAddOrder();
                                } else {
                                    //修改
                                    updateContacts();
                                }
                                break;
                            case 1:
                                switch (UPDATE_TIMES) {
                                    case 100:
                                        //修改
                                        updateContacts();
                                        break;
                                    case 200:
                                        updateOrderTimes();
                                        break;
                                }

                                break;
                            case 2:
                                if (positon != -1) {
                                    updateContacts();
                                } else {
                                    //添加
                                    addContacts();
                                }
                                break;
                        }
                    } else if (et_name.getText().toString().length() == 0) {
                        ConstantsMethod.showTip(ContactsActivity.this, "姓名不能为空");
                    } else if (!Verification.isMobileNO(et_tel.getText().toString())) {
                        ConstantsMethod.showTip(ContactsActivity.this, "联系电话信息错误");
                    } else if (!Verification.isEmail(et_email.getText().toString())) {
                        ConstantsMethod.showTip(ContactsActivity.this, "常用邮箱信息错误");
                    } else if (tv_time.getText().length() == 0) {
                        ConstantsMethod.showTip(ContactsActivity.this, "请选择近期出行时间");
                    } else if (tv_returnTime.getText().length() == 0) {
                        ConstantsMethod.showTip(ContactsActivity.this, "请选择预计返回时间");
                    }
                } else {
                    if (et_name.getText().toString().length() > 0 && Verification.
                            isMobileNO(et_tel.getText().toString()) && Verification.isEmail(et_email.getText().toString())) {
                        new ProgressBarLoadUtils(ContactsActivity.this).startProgressDialog();
                        connectWork();
                        //修改
                        for (int i = 0; i < mData.size(); i++) {
                            if (et_name.getText().toString().equals(mData.get(i).getContact_name()) &&
                                    et_tel.getText().toString().equals(mData.get(i).getContact_phone())
                                    && i != positon) {
                                Toast.makeText(ContactsActivity.this, "联系人已存在", Toast.LENGTH_SHORT).show();
                                new ProgressBarLoadUtils(ContactsActivity.this).stopProgressDialog();
                                return;
                            }
                        }

                        switch (FROM_EVUS_CONTACT_STATUS){
                            case 100:
                                switch (FROM_EVUS_ICONTACT_CHANGE){
                                    case 100:
                                        Toast.makeText(ContactsActivity.this, "新增订单", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 200:
//                                        Toast.makeText(ContactsActivity.this, "新增订单+修改联系人", Toast.LENGTH_SHORT).show();
                                        updateContacts();
//                                        toAddOrder();
                                        break;
                                }
                                break;

                            case 200:
//                                Toast.makeText(ContactsActivity.this, "新增订单+新建联系人", Toast.LENGTH_SHORT).show();
                                addContacts();
                                break;
                        }
//                        switch (state) {
//                            case 0:
//                                if (creatOrderId == -1) {
//                                    toAddOrder();
//                                } else {
//                                    updateContacts();
//                                }
//                                break;
//                            case 1:
//                                //修改
//                                updateContacts();
//                                break;
//                            case 2:
//                                if (creatOrderId == -1) {
//                                    //添加
//                                    addContacts();
//                                }
//                                break;
//                        }
                    } else if (et_name.getText().toString().length() == 0) {
                        ConstantsMethod.showTip(ContactsActivity.this, "姓名不能为空");
                    } else if (!Verification.isMobileNO(et_tel.getText().toString())) {
                        ConstantsMethod.showTip(ContactsActivity.this, "联系电话信息错误");
                    } else if (!Verification.isEmail(et_email.getText().toString())) {
                        ConstantsMethod.showTip(ContactsActivity.this, "常用邮箱信息错误");
                    }
                }
                break;

            case R.id.rl_returnTime://返回时间
                if (!tv_time.getText().toString().equals("")) {
                    Intent intent = new Intent(ContactsActivity.this, ReturnTimeActivity.class);
                    startActivityForResult(intent, ConstantsCode.RETURN_TIME);
                } else {
                    ConstantsMethod.showTip(ContactsActivity.this, "请先选择出行时间");
                }
                break;

            case R.id.rl_time://出行时间
                Intent intent = new Intent(ContactsActivity.this, TravelTimeActivity.class);
                startActivityForResult(intent, ConstantsCode.TRAVEL_TIME);
                break;
        }
    }

    private static int CLICK_CURRENT_POSITION = -1;
    /**
     * 监听
     */
    private void toListener() {
        //罩层效果
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                positon = i;
                if(CLICK_CURRENT_POSITION == i){
                    state = 2;
                    lv.setItemChecked(i, false);
                    et_name.setText("");
                    et_tel.setText("");
                    et_email.setText("");
                    CLICK_CURRENT_POSITION = -1;
                    positon = CLICK_CURRENT_POSITION;
                    FROM_EVUS_CONTACT_STATUS = 200;
                }else{
                    state = 0;
                    lv.setItemChecked(i, true);
                    CLICK_CURRENT_POSITION = i;
                    contact_id = mData.get(i).getContact_id();
                    et_name.setText(mData.get(i).getContact_name());
                    et_tel.setText(mData.get(i).getContact_phone());
                    et_email.setText(mData.get(i).getE_mail());
                    FROM_EVUS_CONTACT_STATUS = 100;
                }
            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (positon != -1) {
                    state = 1;
                } else {
                    state = 2;
                    creatOrderId = -1;
                }
                FROM_EVUS_ICONTACT_CHANGE = 200;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (state != 2) {
                    state = 1;
                }
                UPDATE_TIMES = 100;
                FROM_EVUS_ICONTACT_CHANGE = 200;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (state != 2) {
                    state = 1;
                }
                UPDATE_TIMES = 100;
                FROM_EVUS_ICONTACT_CHANGE = 200;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    /**
     * 修改订单时间(出行时间和返回时间)
     */
    private void updateOrderTimes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsBiz.updateOrderTimes(travelTime,returnTime,creatOrderId);
            }
        }).start();
    }


    /**
     * 添加联系人
     */
    private void addContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsBiz.addContacts(et_name.getText().toString(),et_tel.getText().toString(),et_email.getText().toString());
            }
        }).start();
    }

    /**
     * 新增订单
     */
    private void toAddOrder() {
        //支付状态为0
        Constants.IS_PAY = 0;
        Constants.STATUS = 0;
        if (isEvus != null && isEvus.equals("1")) {
            travelTime = "";
            returnTime = "";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsBiz.AddOrder(addOrder,contact_id,travelTime,returnTime);
            }
        }).start();
    }


    /**
     * 修改联系人
     */
    private void updateContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsBiz.updateContacts(et_name.getText().toString(),et_tel.getText().toString(),et_email.getText().toString(),contact_id);
            }
        }).start();
    }

    /**
     * 已有联系人
     */
    private void myContacts() {
        if (contacts) {
            lv.setVisibility(View.VISIBLE);
            //listView动画演示
            animation = new TranslateAnimation(-300f, 0f, 0f, 0f);
            animation.setDuration(100);
            //1f为延时
            controller = new LayoutAnimationController(animation, 1f);
            controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lv.setLayoutAnimation(controller);
            contactsAdapter.notifyDataSetInvalidated();
            contacts = false;
            //设置上下箭头
            Animation animation = AnimationUtils.loadAnimation(ContactsActivity.this, R.anim.point_rotate_0_180);
            imgPoint.startAnimation(animation);
            animation.setFillAfter(!animation.getFillAfter());
        } else {
            lv.setVisibility(View.GONE);
            contacts = true;
            //设置上下箭头
            Animation animation = AnimationUtils.loadAnimation(ContactsActivity.this, R.anim.point_rotate_180_360);
            imgPoint.startAnimation(animation);
            animation.setFillAfter(!animation.getFillAfter());
        }
    }

    /**
     * 新建联系人
     */
    private void newContacts() {
        if (newContacts) {
            rl_newContacts.setVisibility(View.VISIBLE);
            newContacts = false;
            //设置上下箭头
            Animation animation = AnimationUtils.loadAnimation(ContactsActivity.this, R.anim.point_rotate_0_1801);
            imgPoint1.startAnimation(animation);
            animation.setFillAfter(!animation.getFillAfter());
        } else {
            rl_newContacts.setVisibility(View.GONE);
            newContacts = true;
            //设置上下箭头
            Animation animation = AnimationUtils.loadAnimation(ContactsActivity.this, R.anim.point_rotate_180_3601);
            imgPoint1.startAnimation(animation);
            animation.setFillAfter(!animation.getFillAfter());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case ConstantsCode.TRAVEL_TIME://出行时间
                        travelTime = data.getStringExtra("time");
                        tv_time.setText(travelTime);
                        if (LAST_TRAVEL_TIME.equals("")) {
                            LAST_TRAVEL_TIME = travelTime;
                        } else {
                            if (creatOrderId != -1) {
                                //出行时间没有改变
                                if (LAST_TRAVEL_TIME.equals(travelTime)) {
                                    UPDATE_TIMES = 100;
                                } else {
                                    //出行时间有改变
                                    UPDATE_TIMES = 200;
                                    state = 1;
                                }
                            }
                        }
                        break;
                    case ConstantsCode.RETURN_TIME://返回时间
                        returnTime = data.getStringExtra("time");
                        tv_returnTime.setText(returnTime);
                        if (LAST_RETURN_TIME.equals("")) {
                            LAST_RETURN_TIME = returnTime;
                        } else {
                            if (creatOrderId != -1) {
                                //返回时间没有改变
                                if (LAST_RETURN_TIME.equals(returnTime)) {
                                    UPDATE_TIMES = 100;
                                } else {
                                    //返回时间有改变
                                    UPDATE_TIMES = 200;
                                    state = 1;
                                }
                            }

                        }
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(ContactsActivity.this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取到业务层所有的联系人数据
     * @param allContacts
     */
    public void updateContactsList(final AllContacts allContacts) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (allContacts.getError_code()) {
                    case 0:
                        mData.addAll(allContacts.getResult());
                        lv.setAdapter(contactsAdapter);
                        break;
                    case 2:
                        Intent intent = new Intent(ContactsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /**
     * 用户获取新增订单的结果
     * @param result
     */
    public void getAddOrderResult(final ResultId result) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              new ProgressBarLoadUtils(ContactsActivity.this).stopProgressDialog();
                              switch (result.getError_code()) {
                                  case 0:
                                      creatOrderId = result.getReason_id();
                                      if (getIntent().getStringExtra("evus") == null) {
                                          addOrder.setContact_name(et_name.getText().toString());
                                          addOrder.setContact_id(contact_id);
                                          Intent intent = new Intent(ContactsActivity.this, DocumentScanningActivity.class);
                                          intent.putExtra("addOrder", addOrder);
                                          intent.putExtra("order_id", result.getReason_id());
                                          startActivity(intent);
                                      } else {
                                          addOrder.setContact_name(et_name.getText().toString());
                                          addOrder.setContact_id(contact_id);
                                          Intent intent = new Intent(ContactsActivity.this, EvUsActivity.class);
                                          intent.putExtra("addOrder", addOrder);
                                          intent.putExtra("order_id", result.getReason_id());
                                          intent.putExtra("evus", getIntent().getStringExtra("evus"));
                                          startActivity(intent);
                                      }
                                      break;
                                  case 2:
                                      Intent intent1 = new Intent(ContactsActivity.this, LoginActivity.class);
                                      startActivity(intent1);
                                      break;
                              }
                          }
                      }

        );
    }

    /**
     * 用户获取新增联系人的结果，处理
     * @param result
     */
    public void getAddContactResult(final ResultId result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ProgressBarLoadUtils(ContactsActivity.this).stopProgressDialog();
                switch (result.getError_code()) {
                    case 0:
                        contact_id = result.getReason_id();
                        AllContacts.ResultBean bean = new AllContacts.ResultBean();
                        bean.setContact_name(et_name.getText().toString());
                        bean.setContact_phone(et_tel.getText().toString());
                        bean.setE_mail(et_email.getText().toString());
                        mData.add(bean);
                        contactsAdapter.notifyDataSetChanged();
                        lv.setItemChecked(positon, true);
                        toAddOrder();
                        break;
                    case 2:
                        Intent intent = new Intent(ContactsActivity.this, LoginActivity.class);
                        startActivityForResult(intent, ConstantsCode.LOGIN);
                        break;
                }
            }
        });
    }

    /**
     * 用户获取更新联系人的结果
     * @param result
     */
    public void getUpdateContactsResult(final Result result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mData.get(positon).setContact_name(et_name.getText().toString());
                mData.get(positon).setContact_phone(et_tel.getText().toString());
                mData.get(positon).setE_mail(et_email.getText().toString());
                contactsAdapter.notifyDataSetChanged();
                lv.setItemChecked(positon, true);
                switch (result.getError_code()) {
                    case 0:
                        if (creatOrderId == -1) {
                            toAddOrder();
                        } else {
                            switch (result.getError_code()) {
                                case 0:
                                    if (getIntent().getStringExtra("evus") == null) {
                                        addOrder.setContact_name(et_name.getText().toString());
                                        addOrder.setContact_id(contact_id);
                                        Intent intent = new Intent(ContactsActivity.this, DocumentScanningActivity.class);
                                        intent.putExtra("addOrder", addOrder);
                                        intent.putExtra("order_id", creatOrderId);
                                        startActivity(intent);
                                    } else {
                                        addOrder.setContact_name(et_name.getText().toString());
                                        addOrder.setContact_id(contact_id);
                                        Intent intent = new Intent(ContactsActivity.this, EvUsActivity.class);
                                        intent.putExtra("addOrder", addOrder);
                                        intent.putExtra("order_id", creatOrderId);
                                        intent.putExtra("evus", getIntent().getStringExtra("evus"));
                                        startActivity(intent);
                                    }
                                    break;
                            }
                        }
                        break;
                    case 1:
                        ConstantsMethod.showTip(ContactsActivity.this, result.getReason());
                        break;
                }
            }
        });
    }

    /**
     * 获取更改出行或者预计返回时间的结果
     * @param json
     */
    public void getUpdateOrderTimeResult(final JSONObject json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (json.optInt("error_code") == 0) {
                    if (getIntent().getStringExtra("evus") == null) {
                        addOrder.setContact_name(et_name.getText().toString());
                        addOrder.setContact_id(contact_id);
                        Intent intent = new Intent(ContactsActivity.this, DocumentScanningActivity.class);
                        intent.putExtra("addOrder", addOrder);
                        intent.putExtra("order_id", creatOrderId);
                        startActivity(intent);
                    }
                } else if (json.optInt("error_code") == 2) {
                    Intent intent = new Intent(ContactsActivity.this, LoginActivity.class);
                    startActivityForResult(intent, ConstantsCode.LOGIN);
                }
            }
        });
    }
}
