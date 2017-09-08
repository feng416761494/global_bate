package com.usamsl.global.my.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.usamsl.global.R;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.UrlSet;
import com.usamsl.global.login.activity.LoginActivity;
import com.usamsl.global.my.activity.FeedBackActivity;
import com.usamsl.global.my.activity.MyContactsActivity;
import com.usamsl.global.my.activity.MySettingActivity;
import com.usamsl.global.my.activity.UserInfoActivity;
import com.usamsl.global.my.adapter.MyFragmentOptionAdapter;
import com.usamsl.global.my.custom.CircleImageView;
import com.usamsl.global.my.entity.UserInfoEntity;
import com.usamsl.global.my.entity.UserOptionEntity;
import com.usamsl.global.okhttp.OkHttpManager;
import com.usamsl.global.util.ImageLoadUtils;
import com.usamsl.global.util.update.ObjectIsNullUtils;
import com.usamsl.global.view.MyGridView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 菜单栏我的界面
 * 时间：2016/12/15
 */
public class MyFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    //界面视图View
    private View view;
    //用户头像
    private CircleImageView img_user;
    //用户手机号码/人员类型
    private TextView tvUserPhoneNmb,tvUserCustType;
    //页面选项卡
    private MyGridView gvOption;
    //首页选项卡的内容
    private String[] tvOptions = {"个人信息","联系人管理","订单管理","意见反馈","设置","推荐好友"};
    private int[] imgOptions = {R.drawable.mine,R.drawable.contact,R.drawable.order_fragment_my,R.drawable.opinion,R.drawable.setup,R.drawable.commend};
    //选项卡的适配器
    private MyFragmentOptionAdapter adapter;
    private List<UserOptionEntity> optionList;
    //定义接口跳转至OrderFragment
    private GoToOrderFragmentListener listener;
    private UMWeb web;
    public ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();

    public GoToOrderFragmentListener getListener() {
        return listener;
    }

    public void setListener(GoToOrderFragmentListener listener) {
        this.listener = listener;
    }
    //跳转至OrderFragment需要的接口
    public interface GoToOrderFragmentListener{
        void goToOrder(View view);
    }

    public MyFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my, container, false);
            initView();
        }
        setData();
        return view;
    }


    /**
     * 界面数据初始化
     */
    private void initView() {
        img_user = (CircleImageView) view.findViewById(R.id.imgUserPhoto);
        tvUserPhoneNmb = (TextView) view.findViewById(R.id.tvUserPhoneNmb);
        tvUserCustType = (TextView) view.findViewById(R.id.tvUserCustType);
        gvOption = (MyGridView) view.findViewById(R.id.gvOption);
        img_user.setOnClickListener(this);
        gvOption.setOnItemClickListener(this);
        img_user.setBorderColor(getActivity().getResources().getColor(R.color.Transparent));
    }

    /**
     * 选项卡中的数据
     */
    private void setData() {
        optionList = new ArrayList<>();
        for(int i=0;i<tvOptions.length;i++){
            UserOptionEntity entity = new UserOptionEntity();
            entity.setTvOption(tvOptions[i]);
            entity.setImgOption(imgOptions[i]);
            optionList.add(entity);
        }
        if(optionList != null && optionList.size()>0){
            adapter = new MyFragmentOptionAdapter(getActivity(),optionList);
            gvOption.setAdapter(adapter);
        }
        platforms.add(SHARE_MEDIA.WEIXIN.toSnsPlatform());
        platforms.add(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform());
        platforms.add(SHARE_MEDIA.WEIXIN_FAVORITE.toSnsPlatform());
        platforms.add(SHARE_MEDIA.SINA.toSnsPlatform());
        platforms.add(SHARE_MEDIA.QQ.toSnsPlatform());
        platforms.add(SHARE_MEDIA.QZONE.toSnsPlatform());
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int selectOption = position + 1;
        switch (selectOption){
            case 1://个人信息
                if (Constants.USER_LOGIN) {
                    Intent intentUserInfo = new Intent(getActivity(),UserInfoActivity.class);
                    startActivity(intentUserInfo);
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case 2://联系人
                if (Constants.USER_LOGIN) {
                    Intent intent = new Intent(getActivity(), MyContactsActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case 3://订单管理
                //由接口进行回调，点击MyFragment中的控件跳转至OrderFragement
                if(listener!=null){
                    listener.goToOrder(gvOption);
                }
                break;
            case 4://意见反馈
                if(Constants.USER_LOGIN){
                    Intent intent_opinion = new Intent(getActivity(), FeedBackActivity.class);
                    startActivity(intent_opinion);
                }else{
                    Intent intentOpnion = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intentOpnion);
                }

                break;
            case 5://设置
                Intent intent = new Intent(getActivity(), MySettingActivity.class);
                startActivity(intent);
                break;
            case 6://推荐好友
                web = new UMWeb(Constants.mslShareUrl);
                web.setTitle("百变蜥蜴全球签证，一同畅享全球酒店和全球飞行吧!");
                web.setThumb(new UMImage(getActivity(), R.drawable.share_icon));
                web.setDescription("轻触开始签证按钮即可办理全球签证");
                new ShareAction(getActivity())
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(web)
                        .setCallback(shareListener)
                        .open();
                break;
        }
    }



    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(),"分享成功",Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(),"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(),"取消了",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //用户登录的情况下去请求数据
        if (Constants.USER_LOGIN) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    getUserInfo();
                }
            }.start();
        } else {
            //没有登录，则直接将头像、名称、职位等设置为未登录时的状态
            img_user.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.user_fragment_my));
            tvUserPhoneNmb.setText(Constants.USER);
            tvUserCustType.setVisibility(View.GONE);
        }
    }



    private void getUserInfo() {
        String url = UrlSet.userInfo + Constants.TOKEN;
        OkHttpClient client = OkHttpManager.myClient();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final UserInfoEntity infoEntity = gson.fromJson(str, UserInfoEntity.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(ObjectIsNullUtils.objectIsNull(infoEntity)){
                            if(infoEntity.getError_code() == 1){
                                if(ObjectIsNullUtils.TextIsNull(infoEntity.getResult().getPath_name())){
                                    ImageLoadUtils.loadUserPhoto(infoEntity.getResult().getPath_name(),img_user,getActivity());
                                }
                                if(ObjectIsNullUtils.TextIsNull(infoEntity.getResult().getCust_type())){
                                    tvUserCustType.setVisibility(View.VISIBLE);
                                    tvUserCustType.setText(infoEntity.getResult().getCust_type());
                                }
                                if(ObjectIsNullUtils.TextIsNull(infoEntity.getResult().getCust_name())){
                                    tvUserPhoneNmb.setText(infoEntity.getResult().getCust_name());
                                }else{
                                    tvUserPhoneNmb.setText(Constants.USER);
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
