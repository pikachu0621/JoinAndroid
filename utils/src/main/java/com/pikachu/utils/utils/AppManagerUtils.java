package com.pikachu.utils.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * author : pikachu
 * date   : 2021/7/15 11:13
 * version: 1.0
 */
public final class AppManagerUtils {
    private static Stack<Activity> activityStack;
    private static AppManagerUtils instance;

    private AppManagerUtils() {
    }

    /**
     * 单一实例
     */
    public static AppManagerUtils getAppManager() {
        if (instance == null) {
            instance = new AppManagerUtils();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
//		//应用即将全部关闭，清理缓存
//		if(activityStack.size()==1){
//			((AppContext)activity.getApplication()).clearWebViewCache();
//
//		}
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 0)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                    return;
                }
            }
    }

    //获取指定类名的Activity
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
        }
    }


}