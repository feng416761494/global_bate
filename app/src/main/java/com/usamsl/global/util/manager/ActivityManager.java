package com.usamsl.global.util.manager;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * 时间：2017/2/9
 * 描述：统一管理activity，签证流程中跳转到首页时全部关闭
 */
public class ActivityManager {
    public static List<Activity> list = new LinkedList<Activity>();
    private static ActivityManager instance = null;

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    //添加activity
    public void addActivity(Activity activity) {
        list.add(activity);
    }

    //移除activity
    public void removeActivity(Activity activity) {
        list.remove(activity);
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public static void finishSingleActivityByClass(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : list) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }
        finishSingleActivity(tempActivity);
    }



    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (list.contains(activity)) {
                list.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    //全部关闭activity
    public void exit() {
        for (Activity activity : list) {
            if (!activity.isFinishing() && activity != null) {
                activity.finish();
            }
        }
    }
}
