package com.usamsl.global.service.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.ava.entity.AvaReceived;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.service.adapter.ChatAdapter;
import com.usamsl.global.service.data.ServiceVisaCountryData;
import com.usamsl.global.service.entity.ChatModel;
import com.usamsl.global.service.entity.ItemModel;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 客服聊天对话框
 * 时间：2017/2/6
 */
public class ChatActivity_old extends AppCompatActivity {
    //展示聊天列表
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    //书写内容
    private EditText et;
    //发送
    private TextView tvSend;
    //发送的内容
    private String content;
    //收到的内容
    private String content1;
    private ImageView img_back;
    //哪个国家的签证
    private TextView tv_visaHint;
    //是否可长按呼叫客服
    private boolean press = false;
    //聊天界面最外层
    private RelativeLayout rl;
    private boolean send = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_chat);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
        toListener();
    }

    private void toListener() {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                content = s.toString();
                if (!content.trim().equals("")) {
                    send = true;
                    tvSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_send));
                    tvSend.setEnabled(true);
                } else {
                    send = false;
                    tvSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_send_false));
                    tvSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content != null && !content.equals("") && !content.equals(" ") && send) {
                    send = false;
                    connectWork(content);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
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
                ConstantsMethod.cancelKeyboard(ChatActivity_old.this, et);
            }
        });
       /* recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                    Rect rect = new Rect();
                    recyclerView.getWindowVisibleDisplayFrame(rect);
                    //计算出可见屏幕的高度
                    int displayHight = rect.bottom - rect.top;
                    //获得屏幕整体的高度
                    int hight = rl.getHeight();
                    //获得键盘高度
                    int keyboardHeight = hight - displayHight;
                    int heightDiff = recyclerView.getRootView().getHeight() - recyclerView.getHeight();
                    if (heightDiff > 100) { // 如果高度差超过100像素，就很有可能是有软键盘...
                        ViewGroup.LayoutParams para =  recyclerView.getLayoutParams();//获取按钮的布局
                        para.width = rl.getWidth();//修改宽度
                        para.height = 100;//修改高度
                        recyclerView.setLayoutParams(para); //设置修改后的布局。
                    } else {
                        ViewGroup.LayoutParams para = recyclerView.getLayoutParams();//获取按钮的布局
                        para.width = rl.getWidth();//修改宽度
                        para.height = rl.getHeight()-rl_send.getHeight();//修改高度
                        recyclerView.setLayoutParams(para); //设置修改后的布局。
                    }
            }
        });*/
    }

    private void initData() {
        tv_visaHint.setText(getIntent().getStringExtra("visaCountry"));
//        getAvaChatLog();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter = new ChatAdapter());
        adapter.replaceAll(ServiceVisaCountryData.initChatData());
        adapter.setOnItemLongClickListener(new ChatAdapter.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(View view, int position,
                                                   String c) {

                if (press && c.indexOf("我还太小了") != -1) {
                    ConstantsMethod.callDirectly(ChatActivity_old.this);
                }
                return false;
            }
        });
    }

    /**
     * 获取与Ava的聊天记录
     */
    private static int CURRENT_CHAT_PAGE = 0;
    private void getAvaChatLog() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String url = UrlSet.getAvaChatLog;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("token", Constants.TOKEN)
                        .add("page",CURRENT_CHAT_PAGE+"")//当前页
                        .add("log_type","1")//记录类型1：与ava的聊天     2：：意见反馈
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();

                    }
                });
            }
        }.start();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et = (EditText) findViewById(R.id.et);
        tvSend = (TextView) findViewById(R.id.tvSend);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_visaHint = (TextView) findViewById(R.id.tv_visaHint);
        rl = (RelativeLayout) findViewById(R.id.rl);
        if(et.getText().toString().equals("")){
            tvSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_send_false));
            tvSend.setEnabled(false);
        }else{
            tvSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_send));
            tvSend.setEnabled(false);
        }
    }

    /**
     * 判断是否联网
     */
    private void connectWork(final String text) {
        if (Constants.connect) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // post请求上传
                    String url = UrlSet.ask_ava;
                    String json = "{\"input_text\":{\"text\": \"" + text +
                            "\"},\"user_id\":\"" + Constants.TOKEN + "\"}";
                    post(url, json);
                }
            }).start();
        } else {
            ConstantsMethod.showTip(ChatActivity_old.this, "无网络连接");
        }
    }

    /**
     * 与后台对接接口
     */
    private OkHttpClient client = OkHttpManager.myClient();

    private void post(String url, String json) {
        RequestBody body = RequestBody.create(Constants.JSON, json);
        final Request request = new Request.Builder()
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
                        ConstantsMethod.showTip(ChatActivity_old.this, "网络异常");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws
                    IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                if (str.toString().substring(0, 1).equals("{")) {
                    final AvaReceived result = gson.fromJson(str,
                            AvaReceived.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.getOutput_text() != null) {
                                if (result.getOutput_text().getText() != null
                                        &&
                                        !result.getOutput_text().getText
                                                ().equals("")) {
                                    press = false;
                                    ArrayList<ItemModel> data = new
                                            ArrayList<>();
                                    ChatModel model = new ChatModel();
                                    model.setContent(content);
                                    data.add(data.size(),new ItemModel(ItemModel.CHAT_B,
                                            model));
                                    model = new ChatModel();
                                    model.setContent(result.getOutput_text
                                            ().getText());
                                    data.add(data.size(),new ItemModel(ItemModel.CHAT_A,
                                            model));

                                    adapter.addAll(data);
//                                    adapter.notifyItemInserted(data.size());
//                                    recyclerView.scrollToPosition(data.size());
//                                    recyclerView.setAdapter(adapter);
                                } else {
                                    onPress();
                                }
                                et.setText("");
                                content = "";
                            } else {
                                onPress();
                            }
                            send = false;
                            adapter.notifyDataSetChanged();
                        }
                    });
                    //聊天记录保存

                }
            }
        });
    }

    //联系客服
    private void onPress() {
        press = true;
        ArrayList<ItemModel> data = new ArrayList<>();
        ChatModel model = new ChatModel();
        model.setContent(content);
        data.add(new ItemModel(ItemModel.CHAT_B, model));
        model = new ChatModel();
        content1 = "我还太小了，不知道答案，请您长按文本联系客服吧！客服电话："
                + Constants.SERVICE_PHONE;
        model.setContent(content1);
        data.add(new ItemModel(ItemModel.CHAT_A, model));
        adapter.addAll(data);
    }
}