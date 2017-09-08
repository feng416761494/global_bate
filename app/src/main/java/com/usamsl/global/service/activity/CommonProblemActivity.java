package com.usamsl.global.service.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.sunflower.FlowerCollector;
import com.usamsl.global.R;
import com.usamsl.global.ava.entity.AvaReceived;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.service.entity.Problems;
import com.usamsl.global.util.entity.Result;
import com.usamsl.global.util.manager.AvaManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 时间：2017/1/25
 * 描述：常见问题
 */
public class CommonProblemActivity extends AppCompatActivity {

    private TextView tv_problem;
    private TextView tv_answer;
    private ImageView img_back;
    private RelativeLayout rl;
    //答案
    private String answer;
    //是否继续回答
    private boolean goOn = true;
    //语音播放助手
    private static SpeechSynthesizer mTts;
    private static String TAG = CommonProblemActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);
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
        rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!goOn) {
                    ConstantsMethod.callDirectly(CommonProblemActivity.this);
                }
                return false;
            }
        });
    }

    /**
     * 点击语音助手播放答案
     */
    public void playVoice(View view) {
        AvaManager.initTts(CommonProblemActivity.this, mTts, answer);
    }

    private void initData() {
        tv_problem.setText(getIntent().getStringExtra("problem"));
        boolean email = getIntent().getBooleanExtra("email", false);
        if (!email) {
            connectWork();
        }else {
            answer="jicj@sh-citic.com";
            tv_answer.setText("jicj@sh-citic.com");
        }
        //初始化语音助手
        mTts = SpeechSynthesizer.createSynthesizer(CommonProblemActivity.this, null);
    }

    private void initView() {
        tv_answer = (TextView) findViewById(R.id.tv_answer);
        tv_problem = (TextView) findViewById(R.id.tv_problem);
        img_back = (ImageView) findViewById(R.id.img_back);
        rl = (RelativeLayout) findViewById(R.id.rl);
    }

    /**
     * 判断是否联网
     */
    private void connectWork() {
        if (Constants.connect) {
            initConnectData();
        } else {
            ConstantsMethod.showTip(CommonProblemActivity.this, "无网络连接");
        }
    }

    /**
     * 联网之后数据加载
     */
    private void initConnectData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // post请求上传
                String url = UrlSet.problemList;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("problem", tv_problem.getText().toString())
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
                                ConstantsMethod.showTip(CommonProblemActivity.this, "网络异常！");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String str = response.body().string();
                        if (str.substring(0, 1).equals("{")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    final Problems problems = gson.fromJson(str, Problems.class);
                                    Problems.ResultBean bean=problems.getResult().get(0);
                                    if (bean.getAnswer()==null){
                                        goOn = false;
                                        answer = "您的问题太难了，我还小，请您长按联系客服吧！4006606033";
                                        tv_answer.setText(answer);
                                    }else {
                                        answer = bean.getAnswer();
                                        tv_answer.setText(answer);
                                        goOn = true;
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
    protected void onDestroy() {
        super.onDestroy();
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();

    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(CommonProblemActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(CommonProblemActivity.this);
        super.onPause();
    }
}
