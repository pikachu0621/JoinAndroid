package com.pikachu.utils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.RequiresPermission;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Pikachu
 * @Project 流量专家
 * @Package com.stkj.charmflowstool.utils
 * @Date 2021/8/6 ( 下午 9:54 )
 * @description 网络测试工具
 *
 *
 */

public final class NetUtils {

    // 网络可用？
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }



    /*
     *判断WIFI是否可用
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWiFiActive(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getTypeName().equals("WIFI")
                            && networkInfo.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }




    // 流量可用？
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    // 当前网络类型
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }


    /**
     * ping 网络延迟丢包   检查
     *
     * @param ip   ip
     * @param time 超时时间 s
     * @param bout 次数
     * @return 0-> 延迟   1->丢包
     * @throws Exception 错误
     */
    public static int[] pingNet(String ip, int time, int bout) throws Exception {

        String lost = null;
        String delay = null;
        Process p = Runtime.getRuntime().exec("ping -c " + bout + " -w " + time + " " + ip);
        BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String str = "";
        while ((str = buf.readLine()) != null) {
            if (str.contains("packet loss")) {
                int i = str.indexOf("received");
                int j = str.indexOf("%");
                LogsUtils.showLog("丢包率:" + str.substring(i + 10, j + 1));
                lost = str.substring(i + 10, j + 1);
            }
            if (str.contains("avg")) {
                int i = str.indexOf("/", 20);
                int j = str.indexOf(".", i);
                LogsUtils.showLog("延迟:" + str.substring(i + 1, j));
                delay = str.substring(i + 1, j);
                delay = delay + "ms";
            }

        }
        String ms = delay.replace("ms", "");
        String replace = lost.replace("%", "");
        int iMs = Integer.parseInt(ms);
        int iReplace = Integer.parseInt(replace);

        return new int[]{iMs, iReplace};
    }


}
