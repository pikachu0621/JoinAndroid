package com.pikachu.utils.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * author : pikachu
 * date   : 2021/8/2 16:02
 * version: 1.0
 * 单位转换
 */
public final class UiUtils {

    private Thread thread;

    /**
     * 将px值转换成为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换成为px值，保证尺寸大小不变
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将px值转换成为sp值，保证尺寸大小不变
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2sp(Context context, float px) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    /**
     * 将sp值转换成为px值，保证尺寸大小不变
     *
     * @param context
     * @param sp
     * @return
     */
    public static int sp2px(Context context, float sp) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }


    /**
     * 切换 到ui线程
     * @param runnable
     */
    public static void runUi(Runnable runnable){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }







    /**
     * 获取屏幕宽
     * Resources.getSystem().getDisplayMetrics().widthPixels 系统提供可能错误
     *
     * @return
     */
    public static int getScreenWidth(Context context){
        if (! (context instanceof Activity)){
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高 全
     * @return
     */
    public static int getScreenHeight(Context context){
        if (! (context instanceof Activity)){
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        return dm.heightPixels;
    }



    /**
     * 获取屏幕可显示高度  减去状态栏加导航栏
     * @return
     */
    public static int getScreenBarHeight(Context context){
        if (! (context instanceof Activity)){
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        return dm.heightPixels - getStatusBarHeight(context) - getNavigationBarHeight(context);
    }



    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = context.getResources().getDimensionPixelSize(resourceId);
        return result;
    }







    /**
     * 获取虚拟按键的高度
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }



    /**
     * 检查是否存在虚拟按键栏
     *
     *
     * 机型适配
     * https://www.cnblogs.com/zhujiulunjian/p/14075623.html
     *
     *
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }


    /**
     * 判断虚拟按键栏是否重写
     */
    public static String getNavBarOverride() {
        String sNavBarOverride = null;
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method m = c.getDeclaredMethod("get", String.class);
            m.setAccessible(true);
            sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
        } catch (Exception ignored) {
        }
        return sNavBarOverride;
    }




    /**
     * 获取导航栏高度
     * @return
     */
    public static int getNavigationBarHeight1(Context context) {
        if (!(context instanceof Activity)) {
            return 0;
        }
        int height;
        Display display = ((Activity) context).getWindow().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        View decorView = ((Activity) context).getWindow().getDecorView();
        Configuration conf = context.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            height = Math.abs(point.x - contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            height = Math.abs(rect.bottom - point.y);
        }
        return height;
    }


    /**
     * 判断导航栏是否为显示状态
     *
     * @param context
     * @return
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean isNavigationBarShowing(Context context) {
        //判断手机底部是否支持导航栏显示
        boolean haveNavigationBar = checkDeviceHasNavigationBar(context);
        if (haveNavigationBar) {
            if (Build.VERSION.SDK_INT >= 17) {
                String brand = Build.BRAND;
                String mDeviceInfo;
                if (brand.equalsIgnoreCase("HUAWEI")) {
                    mDeviceInfo = "navigationbar_is_min";
                } else if (brand.equalsIgnoreCase("XIAOMI")) {
                    mDeviceInfo = "force_fsg_nav_bar";
                } else if (brand.equalsIgnoreCase("VIVO")) {
                    mDeviceInfo = "navigation_gesture_on";
                } else if (brand.equalsIgnoreCase("OPPO")) {
                    mDeviceInfo = "navigation_gesture_on";
                } else {
                    mDeviceInfo = "navigationbar_is_min";
                }

                return Settings.Global.getInt(context.getContentResolver(), mDeviceInfo, 0) == 0;
            }
        }
        return false;
    }


    /**
     *  检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }





}
