package com.pikachu.utils.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Xml;

import androidx.annotation.IntDef;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Arrays;


public final class LogsUtils {


    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int MR = 0; // 默认

    private static String LOG_TAG = "TEST_TT";
    private static int LOG_TYPE = D;

    @IntDef({V, D, I, W, E, MR})
    public @interface LogType {
    }




    /**
     * 设置全局标签, 优先级底
     * @param tag 标签 TAG
     */
    public static void init(String tag) {
        LOG_TAG = tag;
    }
    public static void init(Class<?> tag) {
        LOG_TAG = tag.getSimpleName();
    }
    public static void init(Context tag) {
        LOG_TAG = tag.getClass().getSimpleName();
    }
    public static void init(@LogType int type) {
        LOG_TYPE = type;
    }


    /**
     * 设置全局标签和全局类型 , 优先级底
     * @param tag 标签 TAG
     * @param type 类型
     */
    public static void info(String tag, @LogType int type) {
        LOG_TAG = tag;
        LOG_TYPE = type;
    }
    public static void info(Class<?> tag, @LogType int type) {
        LOG_TAG = tag.getSimpleName();
        LOG_TYPE = type;
    }
    public static void info(Context tag, @LogType int type) {
        LOG_TAG = tag.getClass().getSimpleName();
        LOG_TYPE = type;
    }





    /**
     * 使用全局类型的Log
     *
     * @param tag 标签  TAG
     * @param msg 信息
     */
    public static void showLog(Class<?> tag,  String msg) {
        showLog( MR, tag.getSimpleName() ,  null, msg);
    }
    public static void showLog(Class<?> tag,  Object msg) {
        showLog( MR, tag.getSimpleName() ,  null, msg.toString());
    }

    public static void showLog(Context tag, String msg) {
        showLog(MR, tag.getClass().getSimpleName(),  null, msg);
    }
    public static void showLog(Context tag, Object msg) {
        showLog(MR, tag.getClass().getSimpleName(),  null, msg.toString());
    }

    public static void showLog(String tag, String msg) {
        showLog(MR, tag, null, msg);
    }
    public static void showLog(String tag, Object msg) {
        showLog(MR, tag, null, msg.toString());
    }








    /**
     * 使用传入的 类型, TAG, 的Log
     *
     * @param type 类型
     * @param tag 标签  TAG
     * @param msg 信息
     */
    public static void showLog(@LogType int type, Class<?> tag,  String msg) {
        showLog( type, tag.getSimpleName() , null, msg);
    }
    public static void showLog(@LogType int type, Class<?> tag,  Object msg) {
        showLog( type, tag.getSimpleName() ,  null, msg.toString());
    }

    public static void showLog(@LogType int type, Context tag, String msg) {
        showLog(type, tag.getClass().getSimpleName(), null, msg);
    }
    public static void showLog(@LogType int type, Context tag, Object msg) {
        showLog( type, tag.getClass().getSimpleName(),  null, msg.toString());
    }

    public static void showLog(@LogType int type, String tag, String msg) {
        showLog(type, tag,  null, msg);
    }
    public static void showLog(@LogType int type, String tag, Object msg) {
        showLog( type, tag,  null, msg.toString());
    }







    /**
     * 使用传入的 类型, TAG, 的Log 包含记录异常
     *
     * @param type 类型
     * @param tag 标签 TAG
     * @param tr 记录异常
     * @param msg 信息
     */
    public static void showLog(@LogType int type, String tag, Throwable tr, String msg) {
        if (type == 0)
            type = LOG_TYPE;
        if (tag == null)
            tag = LOG_TAG;
        if (type == V) {
            if (tr == null) Log.v(tag, msg);
            else Log.v(tag, msg, tr);
        } else if (type == D) {
            if (tr == null) Log.d(tag, msg);
            else Log.d(tag, msg, tr);
        } else if (type == I) {
            if (tr == null) Log.i(tag, msg);
            else Log.i(tag, msg, tr);
        } else if (type == W) {
            if (tr == null) Log.w(tag, msg);
            else Log.w(tag, msg, tr);
        } else if (type == E) {
            if (tr == null) Log.e(tag, msg);
            else Log.e(tag, msg, tr);
        }
    }
    public static void showLog(@LogType int type, String tag, Throwable tr, Object msg) {
        showLog(type, tag, tr, msg.toString());
    }

    public static void showLog(@LogType int type, Class<?> tag, Throwable tr, String msg) {
        showLog(type, tag.getSimpleName(), tr, msg);
    }
    public static void showLog(@LogType int type, Class<?> tag, Throwable tr, Object msg) {
        showLog(type, tag.getSimpleName(), tr, msg.toString());
    }

    public static void showLog(@LogType int type, Context tag, Throwable tr, String msg) {
        showLog(type, tag.getClass().getSimpleName(), tr, msg);
    }
    public static void showLog(@LogType int type, Context tag, Throwable tr, Object msg) {
         showLog(type, tag.getClass().getSimpleName(), tr, msg.toString());
    }


    public static void showLog(@LogType int type, String msg) {
        showLog(type, (String) null, null, msg);
    }
    public static void showLog(@LogType int type,  Object msg) {
        showLog(type, (String) null, null, msg.toString());
    }






    /**
     * 使用全局类型，和TAG的Log
     *
     * @param msg 消息
     */
    public static void showLog(String msg) {
        showLog(MR, (String) null ,  null,  msg);
    }
    public static void showLog(Object msg) {
        showLog(MR, (String) null ,  null,  msg.toString());
    }








    /** 数据连接 */
    public static String forStr(String... strings){
        StringBuilder stringBuffer = new StringBuilder();
        for (String i : strings) {
            stringBuffer.append(i);
            if (strings.length > 1){
                stringBuffer.append("    ");
            }
        }
        return stringBuffer.toString();
    }


}
