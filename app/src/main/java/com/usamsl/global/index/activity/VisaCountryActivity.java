package com.usamsl.global.index.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.biz.VisaCityBiz;
import com.usamsl.global.index.biz.VisaCountryBiz;
import com.usamsl.global.index.entity.VisaCountry;
import com.usamsl.global.index.step.step1.activity.CountryVisaDetailsActivity;
import com.usamsl.global.index.adapter.SortAdapter;
import com.usamsl.global.index.entity.IndexVisa;
import com.usamsl.global.index.util.CnSpell;
import com.usamsl.global.index.util.SideBar;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.manager.ActivityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 时间：2016/12/19
 * 描述：首页跳转：签证国家界面
 */
public class VisaCountryActivity extends AppCompatActivity implements View.OnClickListener{
    //国家显示列表
    private ListView listView;
    //右边的首字母
    private SideBar sideBar;
    //国家名称
    private List<IndexVisa> mData;
    private List<IndexVisa> mSortList;
    //按首字母显示国家的adapter
    private SortAdapter adapter;
    //返回按钮
    private ImageView img_back;
    //输入文本
    private EditText et_country;
    //最外层容器
    private RelativeLayout rl;
    //城市
    private String city;
    //voice
    private ImageView img_voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_visa_country);
        ActivityManager.getInstance().addActivity(VisaCountryActivity.this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
        toListener();
    }

    /**
     * 界面初始化
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.lv);
        sideBar = (SideBar) findViewById(R.id.side_bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        et_country = (EditText) findViewById(R.id.et_country);
        rl = (RelativeLayout) findViewById(R.id.rl);
        img_voice = (ImageView) findViewById(R.id.img_voice);
        img_back.setOnClickListener(this);
        rl.setOnClickListener(this);
        img_voice.setOnClickListener(this);
    }

    /**
     * 数据加载
     */
    private void initData() {
        city = getIntent().getStringExtra("city");
        mData = new ArrayList<>();
        mSortList = new ArrayList<>();
        adapter = new SortAdapter(this, mData);
        getVisaCountry();
    }

    /**
     * 获取国家列表
     */
    private void getVisaCountry() {
        if (Constants.connect) {
            initConnectData();
        } else {
            ConstantsMethod.showTip(VisaCountryActivity.this, "无网络连接");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_back:
                this.finish();
                break;

            case R.id.rl:
                ConstantsMethod.cancelKeyboard(VisaCountryActivity.this, et_country);
                break;

            case R.id.img_voice:
                sb.delete(0, sb.length());
                btnVoice();
                break;
        }
    }

    /**
     * 联网之后数据加载
     */
    private void initConnectData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                VisaCountryBiz countryBiz = new VisaCountryBiz(VisaCountryActivity.this);
                countryBiz.getData();
            }
        }).start();
    }

    /**
     * 获取到的业务层数据
     * @param visaCountry
     */
    public void updataListView(final VisaCountry visaCountry) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visaCountry.getError_code() == 0) {
                    for (VisaCountry.ResultBean bean : visaCountry.getResult()) {
                        IndexVisa indexVisa = new IndexVisa(bean.getCountry_name(), bean.getCountry_id());
                        indexVisa.setBg_img_url(bean.getBg_img_url());
                        indexVisa.setEnsign_url(bean.getEnsign_url());
                        indexVisa.setSPEC_KEY(bean.getPhoto_format());
                        mData.add(indexVisa);
                    }
                    Collections.sort(mData); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    /**
     * 监听事件
     */
    private void toListener() {
        //右边首字母显示
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < mData.size(); i++) {
                    if (selectStr.equalsIgnoreCase(mData.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });

        //签证国家输入
        //根据输入框输入值的改变来过滤搜索
        et_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //点击国家
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转到国家签证详情界面
                IndexVisa indexVisa;
                if (et_country.getText() == null || et_country.getText().toString().equals("")) {
                    indexVisa = mData.get(i);
                } else {
                    indexVisa = mSortList.get(i);
                }
                Constants.COUNTRY_ID = indexVisa.getCountry_id();
                Constants.COUNTRY = indexVisa.getName();
                Constants.SPEC_KEY = indexVisa.getSPEC_KEY();
                UrlSet.countrySurface = indexVisa.getCountrySurface();
                Intent intent = new Intent(VisaCountryActivity.this, CountryVisaDetailsActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("country", indexVisa.getCountry_id());
                intent.putExtra("ensign_url", indexVisa.getEnsign_url());
                intent.putExtra("bg_img_url", indexVisa.getBg_img_url());
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
       // mSortList = new ArrayList<>();
        mSortList.clear();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList.addAll(mData) ;
        } else {
            for (IndexVisa country : mData) {
                String name = country.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 ||
                        CnSpell.getPinYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(country);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList);
        adapter.updateListView(mSortList);
    }

    //开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(VisaCountryActivity.this, null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sb.length() > 0) {
                            sb.delete(sb.length() - 1, sb.length());
                        }
                        et_country.setText(sb.toString());
                        et_country.setSelection(sb.length());
                    }
                });
            }
        });
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
    }

    private StringBuilder sb = new StringBuilder();

    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
        sb.append(text);
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}
