package com.pikachu.utils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;

public final class ToastUtils {


    private static Toast toast;
    public static final int L = Toast.LENGTH_LONG, S = Toast.LENGTH_SHORT, MR = 3;
    private static int type = S;
    @SuppressLint("StaticFieldLeak")
    public static Context context;


    @IntDef({L, S, MR})
    public @interface ToastType {
    }

    public static void init(@ToastType int type){
        ToastUtils.type = type;
    }


    public static void init(@ToastType int type, Context context){
        ToastUtils.type = type;
        ToastUtils.context = context;
    }

    public static void init(Context context){
        ToastUtils.context = context;
    }


    /**
     * 使用传入 类型
     * @param context 上下文
     * @param msg 信息
     * @param t  L = 长 ， S = 短
     */
    public static void showToast(Context context, @ToastType int t, String msg) {
        if (context == null && ToastUtils.context == null){
            LogsUtils.showLog(LogsUtils.E, ToastUtils.class, "未初始化 Context \n 请使用 info(Context)初始化 或使用 showToast(Content ...) ");
            return;
        }
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context == null ? ToastUtils.context : context, msg, t == MR ? type : t);
        toast.show();
    }
    public static void showToast(Context context, @ToastType int t, Object msg) {
        showToast(context, t, msg.toString());
    }


    /**
     * 使用全局 类型
     * @param context 上下文
     * @param msg 信息
     */
    public static void showToast(Context context, String msg) {
        showToast(context, MR, msg);
    }
    public static void showToast(Context context, Object msg) {
        showToast(context, MR, msg.toString());
    }

    public static void showToast(@ToastType int t, String msg) {
        showToast(null, t, msg);
    }
    public static void showToast(@ToastType int t, Object msg) {
        showToast(null, t, msg.toString());
    }

    public static void showToast(String msg) {
        showToast(null, MR, msg);
    }
    public static void showToast(Object msg) {
        showToast(null, MR, msg.toString());
    }


}
