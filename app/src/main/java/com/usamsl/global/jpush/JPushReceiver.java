package com.usamsl.global.jpush;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.usamsl.global.constants.AppRunUtils;
import com.usamsl.global.constants.Constants;
import com.usamsl.global.constants.ConstantsMethod;
import com.usamsl.global.login.activity.RemoteLoginActivity;
import com.usamsl.global.main.MainActivity;
import com.usamsl.global.my.activity.MySettingActivity;
import com.usamsl.global.util.update.ObjectIsNullUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/7/5.
 */
public class JPushReceiver extends BroadcastReceiver{

    //注销弹窗
    private PopupWindow cancelPop;
    //用户异地登录判别码
    private int USER_REMOTELOGIN = 200;
    String MY_PKG_NAME = "com.usamsl.global";
    /** 前台是否运行 */
    boolean isFrontAppRuning = false;
    /** 后台是否运行 */
    boolean isBgAppRuning = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            Log.i("FENG","广播收到信息"+intent.getAction());
            if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){//用户点开极光推送通知后的处理
                //对推送通知的内容做判断

            }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {//收到自定义消息
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                parserJson(context,message,json);

            }else if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){//收到通知的时候
            }
        }
    }

    private void parserJson(final Context context, String message, String json) {
        AppRunUtils appRunUtils = new AppRunUtils(context);
        try {
            JSONObject obj = new JSONObject(json);
            if(obj.optString("code").equals("1")){
                if (Constants.USER_LOGIN){
                    //App处于前台显示
                    if(appRunUtils.isFrontAppRuning()){
                        // 获得广播发送的数据
                        Intent intent = new Intent(context,RemoteLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        try{
                            //App没有结束进程，没有运行在前台，只在后台运行中
                            Intent intent = new Intent("android.intent.action.MAIN");
                            Bundle bundle = new Bundle();
                            //由桌面拉起进入异地登录页面USER_REMOTELOGIN = 200,    在App中提示异地登录USER_REMOTELOGIN=100
                            bundle.putInt("remotelogin",USER_REMOTELOGIN);
                            intent.setComponent(new ComponentName(context.getApplicationContext().getPackageName(), MainActivity.class.getName()));
                            intent.putExtras(bundle);
                            //由外部进入App，需要创建新的栈，并且将之前的栈清理掉
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.getApplicationContext().startActivity(intent);
                        }catch(Exception e){
                        }

                    }
                    //注销登录
                    ConstantsMethod.saveCancellationState(context);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private void toActivity(Context context,String content) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        String currentPackageName = cn.getPackageName();
//        if (!TextUtils.isEmpty(currentPackageName)
//                && currentPackageName.equals(context.getPackageName())) {
//            isFrontAppRuning = true;
//        }
//        //判断应用是否在运行
//        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
//        for (ActivityManager.RunningTaskInfo info : list) {
//            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
//                    && info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
//                isBgAppRuning = true;
//                break;
//            }
//        }
        AppRunUtils appRunUtils = new AppRunUtils(context);

        // App不在前台
        if (!appRunUtils.isFrontAppRuning()) {
            //App没有被结束任务
            if (appRunUtils.isBgAppRuning()) {
                Intent mainIntent = new Intent(context, MainActivity.class);
                Intent setIntent = new Intent(context, MySettingActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent[] intents = {mainIntent, setIntent};
                context.startActivities(intents);
            }else{
                //App被结束了任务
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(MY_PKG_NAME);
                //由于App是由通知栏点开启动的，所以要创建一个新的栈来存放打开的Activity
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("content",content);
                launchIntent.putExtras(bundle);
                context.startActivity(launchIntent);
            }
        }else{
            //App处于后台(将来此处要进行页面跳转)
            Intent intent = new Intent(context, MySettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }





    public class MessageEntity {
        private String message;

        private String title;

        private String ad_id;

        private Extras extras;

        private int show_type;

        public void setMessage(String message){
            this.message = message;
        }
        public String getMessage(){
            return this.message;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setAd_id(String ad_id){
            this.ad_id = ad_id;
        }
        public String getAd_id(){
            return this.ad_id;
        }
        public void setExtras(Extras extras){
            this.extras = extras;
        }
        public Extras getExtras(){
            return this.extras;
        }
        public void setShow_type(int show_type){
            this.show_type = show_type;
        }
        public int getShow_type(){
            return this.show_type;
        }

    }


    public class Extras {
        private String code;

        public void setCode(String code){
            this.code = code;
        }
        public String getCode(){
            return this.code;
        }

    }
}
