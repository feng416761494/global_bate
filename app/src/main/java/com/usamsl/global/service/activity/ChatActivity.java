package com.usamsl.global.service.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.usamsl.global.R;
import com.usamsl.global.ava.entity.AvaReceived;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.my.entity.AllOrder;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.service.adapter.ChatAdapter;
import com.usamsl.global.service.customView.ScrollSpeedLinearLayoutManger;
import com.usamsl.global.service.data.ServiceVisaCountryData;
import com.usamsl.global.service.entity.ChatModel;
import com.usamsl.global.service.entity.ItemModel;
import com.usamsl.global.service.entity.TalkToAvaEntity;
import com.usamsl.global.util.update.ObjectIsNullUtils;

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
 * 客服聊天对话框
 * 时间：2017/2/6
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    //展示聊天列表
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private ImageView imgBack;
    //书写内容
    private EditText et;
    //发送
    private TextView tvSend;
    //发送的内容
    private String content;
    //收到的内容
    private String content1;
    //哪个国家的签证
    private TextView tv_visaHint;
    //是否可长按呼叫客服
    private boolean press = false;
    //聊天界面最外层
    private boolean send = false;
    //下拉刷新页面
    private PullRefreshLayout refreshLayout;
    //是否下拉刷新
    private static boolean isRefresh = false;
    //聊天记录一共多少页
    private int pageCount = 0;
    private ArrayList<ItemModel> chatContentList = new ArrayList<ItemModel>();
    private int mIndex;
    private LinearLayoutManager linearLayoutManager;
    private boolean move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_chat);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        initData();
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        imgBack = (ImageView) findViewById(R.id.img_back);
        et = (EditText) findViewById(R.id.et);
        tvSend = (TextView) findViewById(R.id.tvSend);
        tv_visaHint = (TextView) findViewById(R.id.tv_visaHint);
        //为recycleView设置layout
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        //设置下拉刷新
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(currentPage < pageCount){
                    //请求数据
                    isRefresh = true;
                    getToAvaChatLog();
                }else{
                    Toast.makeText(ChatActivity.this, "暂无更多聊天记录", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            }

        });

        //设置监听
        tvSend.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        //对于发送按钮(当聊天输入框没有文字内容时，变为灰色，并且不可点击)
        if(et.getText().toString().equals("")){
            tvSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_send_false));
            tvSend.setEnabled(false);
        }else{
            tvSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_send));
            tvSend.setEnabled(false);
        }
        //对于聊天输入框的有无内容的监听
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
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
            public void afterTextChanged(Editable s) {}
        });


    }

    private void getKeyboardHeight() {
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                WindowManager wm = (WindowManager) ChatActivity.this.getSystemService(Context.WINDOW_SERVICE);
                int screenHeight = wm.getDefaultDisplay().getHeight();
                //判断窗口可见区域大小
                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = screenHeight - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > screenHeight / 3;
                if (isKeyboardShowing) {
                    changeScrollView();
                    //移除布局变化监听
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
    }

    private void changeScrollView() {
        int chatCount = adapter.getItemCount();
        if(chatCount > 0){
            recyclerView.scrollToPosition(chatCount);
        }
    }


    private void initData() {
        //title来自哪一个国家的签证
        tv_visaHint.setText(getIntent().getStringExtra("visaCountry"));
        recyclerView.setAdapter(adapter = new ChatAdapter());
        recyclerView.addOnScrollListener(new RecyclerViewListener());
        adapter.setOnItemLongClickListener(new ChatAdapter.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(View view, int position,
                                                   String c) {

                if (press && c.indexOf("我还太小了") != -1) {
                    ConstantsMethod.callDirectly(ChatActivity.this);
                }
                return false;
            }
        });
        getToAvaChatLog();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                this.finish();
                break;

            case R.id.tvSend://发送聊天内容
                if(ObjectIsNullUtils.TextIsNull(content)){
                    connectWork(content);
                }
                break;
        }
    }

    /**
     * 判断是否联网
     */
    private static String userQuestion = "";
    private static String avaAnwser = "";
    private void connectWork(final String text) {
        if (Constants.connect) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // post请求上传
                    String url = UrlSet.ask_ava;
                    String json = "{\"input_text\":{\"text\": \"" + text + "\"},\"user_id\":\""+Constants.TOKEN+"\",\"app_id\":\""+Constants.TALK_TO_AVA_FROM+"\"}";
                    post(url, json);
                }
            }).start();
            //创建一个聊天记录的实体类对象,加入集合中
            List<ItemModel> newList = new ArrayList<>();
            ChatModel model = new ChatModel();
            model.setContent(et.getText().toString());
            userQuestion = model.getContent();
            newList.add(new ItemModel(ItemModel.CHAT_B,model));
            //该页面所有聊天记录的集合
            chatContentList.addAll(newList);
            adapter.addAll(newList);
            recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
            et.setText("");
        } else {
            ConstantsMethod.showTip(ChatActivity.this, "无网络连接");
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
                        ConstantsMethod.showTip(ChatActivity.this, "网络异常");
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
                                if (result.getOutput_text().getText() != null &&
                                        !result.getOutput_text().getText().equals("")) {
                                    press = false;
                                    List<ItemModel> newList = new ArrayList<>();
                                    ChatModel model = new ChatModel();
                                    model.setContent(result.getOutput_text().getText());
                                    avaAnwser = model.getContent();
                                    newList.add(new ItemModel(ItemModel.CHAT_A,model));
                                    chatContentList.addAll(newList);
                                    adapter.addAll(newList);
                                    recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                                } else {
                                    onPress();
                                }
                                et.setText("");
                                content = "";
                            } else {
                                onPress();
                            }
                            //聊天记录保存(如若后期用户想设置是否保存聊天记录，可以用一个静态变量来控制此方法调用与否)
                            saveChatToAva(userQuestion,avaAnwser);
                            send = false;
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    //联系客服
    private void onPress() {
        press = true;
        ArrayList<ItemModel> data = new ArrayList<>();
        ChatModel model = new ChatModel();
        content1 = "我还太小了，不知道答案，请您长按文本联系客服吧！客服电话："
                + Constants.SERVICE_PHONE;
        model.setContent(content1);
        avaAnwser = model.getContent();
        data.add(new ItemModel(ItemModel.CHAT_A, model));
        chatContentList.addAll(data);
        adapter.addAll(data);
        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
    }


    /**
     * 保存于Ava的聊天记录
     * @param userQuestion 用户的问题
     * @param avaAnwser     ava的回答
     */
    private void saveChatToAva(final String userQuestion, final String avaAnwser) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String url = UrlSet.AvaChatLogSave;
                OkHttpClient client = OkHttpManager.myClient();
                RequestBody body = new FormBody.Builder()
                        .add("token", Constants.TOKEN)
                        .add("project",userQuestion)//用户问题
                        .add("answer_ava",avaAnwser)//ava的回答
                        .add("log_type","1")//1：ava聊天记录2：意见反馈
                        .add("visa_name",getIntent().getStringExtra("visaCountry"))//签证国家名称
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


    private static int currentPage = 0;
    /**
     * 获取之前与ava聊天保存的记录
     */
    private void getToAvaChatLog() {
        final String visaCountryName = getIntent().getStringExtra("visaCountry");
        new Thread(){
            @Override
            public void run() {
                super.run();
                OkHttpClient client = OkHttpManager.myClient();
                Request.Builder requestBuilder = new Request.Builder().url(UrlSet.getAvaChatLog + "token=" + Constants.TOKEN + "&page=" + currentPage + "&log_type=1" + "&visa_name=" + visaCountryName);
                Request request = requestBuilder.build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        final TalkToAvaEntity entity = gson.fromJson(str, TalkToAvaEntity.class);
                        if(ObjectIsNullUtils.objectIsNull(entity)){
                            if(entity.getError_code() == 0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pageCount = entity.getPageSum();
                                        if(entity.getPageSum() <= 1){
                                            adapter.replaceAll(ServiceVisaCountryData.initChatData());
                                        }
                                        List<TalkToAvaEntity.ResultBean> talkList = entity.getResult();
                                        ArrayList<ItemModel> ItemModelList = new ArrayList<>();
                                        for(int i=0;i <talkList.size();i++){
                                            ChatModel modelAva = new ChatModel();
                                            ChatModel modelUser = new ChatModel();
                                            modelAva.setContent(talkList.get(i).getAnswer_ava().toString());
                                            modelUser.setContent(talkList.get(i).getProject().toString());
                                            ItemModelList.add(new ItemModel(ItemModel.CHAT_B, modelUser));
                                            ItemModelList.add(new ItemModel(ItemModel.CHAT_A, modelAva));
                                            chatContentList.addAll(ItemModelList);
                                        }

                                        if(ItemModelList != null && ItemModelList.size() != 0){
                                            //如果处于刷新，则添加到数据的最前端
                                            if(isRefresh){
                                                adapter.Refresh(ItemModelList);
                                                recyclerView.scrollToPosition(0);
//                                                moveToTop(0);
                                                currentPage = currentPage + 1;
                                                refreshLayout.setRefreshing(false);
                                            }else{
                                                adapter.addAll(ItemModelList);
                                                recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                                                currentPage = 1;
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }.start();
    }

    private void moveToTop(int position) {
        mIndex = position;
        recyclerView.stopScroll();
        smoothMoveToPosition(position);
    }

    private void smoothMoveToPosition(int n) {

        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            recyclerView.smoothScrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.smoothScrollBy(0, top);
        }else{
            recyclerView.smoothScrollToPosition(n);
            move = true;
        }

    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE ){
                move = false;
                int n = mIndex - linearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < recyclerView.getChildCount()){
                    int top = recyclerView.getChildAt(n).getTop();
                    recyclerView.smoothScrollBy(0, top);
                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move ){
                move = false;
                int n = mIndex - linearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < recyclerView.getChildCount()){
                    int top = recyclerView.getChildAt(n).getTop();
                    recyclerView.scrollBy(0, top);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatContentList.clear();
        currentPage = 0;
        isRefresh = false;
    }
}