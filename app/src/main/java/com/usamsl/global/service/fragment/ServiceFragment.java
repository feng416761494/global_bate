package com.usamsl.global.service.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.service.activity.CommonProblemActivity;
import com.usamsl.global.service.activity.ProblemsActivity;
import com.usamsl.global.service.adapter.ProblemsAdapter;
import com.usamsl.global.service.entity.Problems;
import com.usamsl.global.util.ProgressBarLoadUtils;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2016/12/14
 * 描述：标签栏服务菜单界面
 * 文件名ServiceFragment
 */
public class ServiceFragment extends Fragment {

    //视图界面View
    private View v;
    //最外层容器：用于让键盘消失
    private RelativeLayout rl;
    //搜索问题
    private EditText et_search;

    public ServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_service, container, false);
            initView();
            toListener();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        et_search.clearFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 界面初始化
     */
    private void initView() {
        rl = (RelativeLayout) v.findViewById(R.id.rl);
        et_search = (EditText) v.findViewById(R.id.et_search);
    }

    /**
     * 监听事件
     */
    private void toListener() {
        //点击除EditText外地方使键盘消失
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (et_search.getText().toString().length() != 0
                        && (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && KeyEvent.ACTION_DOWN == event.getAction()))) {
                    new ProgressBarLoadUtils(getActivity()).startProgressDialog();
                    getProblems();
                }
                return false;
            }
        });
    }

    private void getProblems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // post请求上传
                String url = UrlSet.problemList;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("problem", et_search.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new ProgressBarLoadUtils(getActivity()).stopProgressDialog();
                                ConstantsMethod.showTip(getActivity(), "网络异常！");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new ProgressBarLoadUtils(getActivity()).stopProgressDialog();
                                    Gson gson = new Gson();
                                    final Problems problems = gson.fromJson(str, Problems.class);
                                    if(ObjectIsNullUtils.objectIsNull(problems)){
                                        //在数据库中可以查到该类问题
                                        if(problems.getError_code() == 0 && problems.getResult().size() != 0){
                                            Intent intent = new Intent(getActivity(), ProblemsActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("problemList", (Serializable) problems.getResult());
                                            bundle.putString("problem",et_search.getText().toString());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }else if(problems.getError_code() == 1){
                                            Toast.makeText(getActivity(), problems.getReason(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }
}
