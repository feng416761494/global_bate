package com.usamsl.global.index.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.index.adapter.HotCityAdapter;
import com.usamsl.global.index.adapter.SortAdapter;
import com.usamsl.global.index.biz.VisaCityBiz;
import com.usamsl.global.index.data.IndexVisaData;
import com.usamsl.global.index.entity.City;
import com.usamsl.global.index.entity.IndexVisa;
import com.usamsl.global.index.entity.VisaCity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.manager.ActivityManager;
import com.usamsl.global.view.MyGridView;
import com.usamsl.global.index.util.CnSpell;
import com.usamsl.global.index.util.SideBar;

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

public class VisaCityActivity extends AppCompatActivity implements View.OnClickListener{
    //右边的首字母
    private SideBar sideBar;
    //国家名称
    private List<IndexVisa> indexVisas;
    private List<IndexVisa> mData;
    //按首字母显示城市的adapter
    private SortAdapter sortAdapter;
    //返回按钮
    private ImageView img_back;
    //输入文本
    private EditText et_city;
    //热门城市的显示
    private MyGridView gv;
    //定位城市的显示
    private TextView tv_history;
    //热门城市的显示的adapter
    private HotCityAdapter hotCityAdapter;
    //热门城市列表
    private List<City> cities;
    private ListView listView;
    //最外层容器
    private RelativeLayout rl;
    //voice
    private ImageView img_voice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_visa_city);
        ActivityManager.getInstance().addActivity(VisaCityActivity.this);
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
        sideBar = (SideBar) findViewById(R.id.side_bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        listView = (ListView) findViewById(R.id.lv);
        et_city = (EditText) findViewById(R.id.et_city);
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
        mData = new ArrayList<>();
        indexVisas = new ArrayList<>();
        sortAdapter = new SortAdapter(this, mData);
        //热门城市和定位城市
        View view = LayoutInflater.from(this).inflate(R.layout.visa_city_include, null);
        listView.addHeaderView(view);
        gv = (MyGridView) view.findViewById(R.id.gv);
        tv_history = (TextView) view.findViewById(R.id.tv_history);
        tv_history.setOnClickListener(this);
        tv_history.setText(getIntent().getStringExtra("city"));
        cities = new ArrayList<>();
        cities.addAll(IndexVisaData.initHotCityData());
        hotCityAdapter = new HotCityAdapter(this, cities);
        gv.setAdapter(hotCityAdapter);
        //去掉GridView选中或点击时的阴影
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        getVisaCity();
    }

    /**
     * get签证城市
     */
    private void getVisaCity() {
        if (Constants.connect) {
            initConnectData();
        } else {
            ConstantsMethod.showTip(VisaCityActivity.this, "无网络连接");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_back:
                this.finish();
                break;

            case R.id.rl:
                ConstantsMethod.cancelKeyboard(VisaCityActivity.this, et_city);
                break;

            case R.id.tv_history://定位 历史
                Intent intent = new Intent();
                intent.putExtra("city", tv_history.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;

            case R.id.img_voice://语音搜索城市
                sb.delete(0, sb.length());
                btnVoice();
                break;
        }
    }


    /**
     * 获取业务层的数据
     * @param visaCity
     */
    public void updateListView(final VisaCity visaCity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visaCity.getError_code() == 0) {
                    for (VisaCity.ResultBean bean : visaCity.getResult()) {
                        IndexVisa indexVisa = new IndexVisa(bean.getArea_name(), 0);
                        mData.add(indexVisa);
                    }
                    Collections.sort(mData); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
                    listView.setAdapter(sortAdapter);
                } else {
                    ConstantsMethod.showTip(VisaCityActivity.this, visaCity.getReason());
                }
            }
        });
    }


    /**
     * 联网之后的数据加载
     */
    private void initConnectData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                VisaCityBiz cityBiz = new VisaCityBiz(VisaCityActivity.this);
                cityBiz.getData();
            }
        }).start();
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
                        listView.setSelection(i + 1); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });

        //根据输入框输入值的改变来过滤搜索
        et_city.addTextChangedListener(new TextWatcher() {
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String city;
                if (i != 0) {
                    if (et_city.getText() == null || et_city.getText().toString().equals("")) {
                        city = mData.get(i - 1).getName();
                    } else {
                        city = indexVisas.get(i - 1).getName();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("city", city);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("city", cities.get(i).getCityName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        //indexVisas = new ArrayList<>();
        indexVisas.clear();
        if (TextUtils.isEmpty(filterStr)) {
            indexVisas.addAll(mData);
        } else {
            for (IndexVisa country : mData) {
                String name = country.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 ||
                        CnSpell.getPinYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    indexVisas.add(country);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(indexVisas);
        sortAdapter.updateListView(indexVisas);
    }

    //开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(VisaCityActivity.this, null);
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
                        et_city.setText(sb.toString());
                        et_city.setSelection(sb.length());
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
