package com.usamsl.global.service.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.usamsl.global.R;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.service.adapter.ProblemsAdapter;
import com.usamsl.global.service.entity.Problems;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 问题库
 */
public class ProblemsActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private ProblemsAdapter adapter;
    private List<Problems.ResultBean> mData = new ArrayList<>();;
    private String problem = "";
    private ProgressBar progressBar;
    private PullToRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mData = (List<Problems.ResultBean>) bundle.getSerializable("problemList");
            //上一页传递过来的 搜索问题
            problem = bundle.getString("problem");
        }
        initView();
    }


    private void initView() {
        progressBar=(ProgressBar)findViewById(R.id.progress);
        findViewById(R.id.img_back).setOnClickListener(this);
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new ProblemsAdapter(ProblemsActivity.this, mData);
        lv.setAdapter(adapter);
        toListener();
    }

    private void toListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProblemsActivity.this, CommonProblemActivity.class);
                intent.putExtra("problem", mData.get(i).getProblem());
                startActivity(intent);
            }
        });
        //上拉刷新和下拉刷新
        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            //下拉
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束刷新
                        refreshLayout.finishRefresh();
                    }
                }, 1500);
            }

            //上拉
            @Override
            public void loadMore() {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        //获取更多的数据
                        getMoreData();
                    }
                }.start();
            }
        });
    }

    private int page = 1;//默认页是1
    private void getMoreData() {
        // post请求上传
        String url = UrlSet.problemList;
        OkHttpClient client = OkHttpManager.myClient();
        RequestBody body = new FormBody.Builder()
                .add("problem", problem)
                .add("page",page + "")
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
                        Toast.makeText(ProblemsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final Problems problems = gson.fromJson(result, Problems.class);
                if(ObjectIsNullUtils.objectIsNull(problems)){
                    if(problems.getError_code() == 0 && problems.getResult().size() > 0){
                        //把获取到的下一页的数据加入到原有的list中
                        mData.addAll(mData.size(),problems.getResult());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.finishLoadMore();
                                adapter.loadMoreData(mData);
                                adapter.notifyDataSetChanged();
                                page = page + 1;
                            }
                        });
                    }else if(problems.getError_code() == 1 && problems.getResult().size() == 0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.finishLoadMore();
                                Toast.makeText(ProblemsActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

}
