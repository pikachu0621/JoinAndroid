package com.pikachu.utils.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

/**
 * author : pikachu
 * date   : 2021/8/3 13:39
 * version: 1.0
 * <p>
 * 设备工具
 */
public final class EquipmentUtils {


    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取工业设备名称
     *
     * @return 工业设备名
     */
    public static String getSystemDevice() {
        return Build.DEVICE;
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机主板名
     *
     * @return
     */
    public static String getDeviceBoard() {
        return Build.BOARD;
    }


    /**
     * 获取手机厂商名
     *
     * @return 手机厂商名
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 官方解释这个ID是app安装的时候生成的，本人测试每一次卸载安装生成的都是固定的，所有可以用于绑定账号
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId == null ? "0000000000000000" : androidId;
    }

    /**
     * 获取手机的IEMI
     *
     * @param context
     * @return
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMEI(Context context) {
        try {
            TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (isAuthority(context, Manifest.permission.READ_PRECISE_PHONE_STATE)) {
                return mTm.getDeviceId();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the unique subscriber ID, for example, the IMSI for a GSM phone.
     * 返回唯一的用户ID，例如GSM电话的IMSI。
     * *如果不可用，返回null。需要权限READ_PHONE_STATE
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getGSM_ID(Context context) {
        try {
            TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (isAuthority(context, Manifest.permission.READ_PRECISE_PHONE_STATE)) {
                return mTm.getSubscriberId();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取手机当前手机号  有些手机就获取不到，比如华为
     * 一键登录功能就需要接入第三方sdk
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getPhone(Context context) {
        try {
            @SuppressLint("ServiceCast") TelephonyManager systemService = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
            if (isAuthority(context, Manifest.permission.READ_PHONE_STATE)) {
                return systemService.getLine1Number();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取SIM卡序号
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getISMNumber(Context context) {
        try {
            @SuppressLint("ServiceCast") TelephonyManager systemService = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
            if (isAuthority(context, Manifest.permission.READ_PHONE_STATE)) {
                return systemService.getSimSerialNumber();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取MAC地址    02:00:00:00:00:00
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getMAC(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (isAuthority(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                return wifiInfo.getMacAddress();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取设备拨号运营商
     *
     * @return ["中国电信CTCC":3]["中国联通CUCC:2]["中国移动CMCC":1]["other":0]["无sim卡":-1][中国铁通 5]
     */
    public static int getSubscriptionOperatorType(Context context) {
        try {
            int opeType = -1;
            // No sim
            if (!hasSim(context)) {
                return opeType;
            }
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String operator = tm.getNetworkOperator();
            // 中国联通
            if ("46001".equals(operator) || "46006".equals(operator) || "46009".equals(operator)) {
                opeType = 2;
                // 中国移动
            } else if ("46000".equals(operator) || "46002".equals(operator) || "46004".equals(operator) || "46007".equals(operator)) {
                opeType = 1;
                // 中国电信
            } else if ("46003".equals(operator) || "46005".equals(operator) || "46011".equals(operator)) {
                opeType = 3;
            } else if ("46020".equals(operator)) {
                opeType = 5;
            } else {
                opeType = 0;
            }
            return opeType;
        } catch (Exception e) {
            return -1;
        }

    }

    /**
     * 检查手机是否有sim卡
     */
    private static boolean hasSim(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String operator = tm.getSimOperator();
            return operator != null;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception ignored) {
        }
        return versionName;
    }

    /**
     * 判断手机是否ROOT
     */
    public static boolean isRoot() {
        try {
            return (!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists());
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 获取手机序列号
     *
     * @return 手机序列号
     */
    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getSerialNumber(Context context) {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0+
                if (isAuthority(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    serial = Build.getSerial();
                }
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                @SuppressLint("PrivateApi") Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    public static String getIPAddress(Context context) {
        try {
            if (!isAuthority(context, Manifest.permission.ACCESS_NETWORK_STATE)) return null;
            @SuppressLint("MissingPermission") NetworkInfo info = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                    WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    return intIP2StringIP(wifiInfo.getIpAddress());
                }
            } else {
                //当前无网络连接,请在设置中打开网络
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getLocalMacAddressFromIp() {
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
        }
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else ip = null;
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取手机内存，已用和未用
     */
    public static long[] queryStorage() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());

        /*//存储块总数量
        long blockCount = statFs.getBlockCountLong();
        //块大小
        long blockSize = statFs.getBlockSizeLong();
        //可用块数量
        long availableCount = statFs.getAvailableBlocksLong();
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        long freeBlocks = statFs.getFreeBlocksLong();*/
        //这两个方法是直接输出总内存和可用空间，也有getFreeBytes
        //API level 18（JELLY_BEAN_MR2）引入
        @SuppressLint({"NewApi", "LocalSuppress"})
        long totalSize = statFs.getTotalBytes();
        @SuppressLint({"NewApi", "LocalSuppress"})
        long availableSize = statFs.getAvailableBytes();

        return new long[]{totalSize, availableSize};
    }


    static class EquipmentInfo {
        public String 语言 = null;
        public String 安卓版本 = null;
        public String 型号 = null;
        public String 工业名 = null;
        public String 品牌 = null;
        public String 主板 = null;
        public String 厂商 = null;
        public String androidId = null;
        public String imei = null;
        public String imsi = null;
        public String 手机号 = null;
        public String sim = null;
        public String mac = null;
        public String 运营商 = null;
        public String isSim = null;
        public String app版本 = null;
        public String isRoot = null;
        public String 序列号 = null;
        public String ip = null;
        public String ipMac = null;
        public String 存储 = null;

        @Override
        public String toString() {
            return "语言 = '" + 语言 + "'\n" +
                    "安卓版本 = '" + 安卓版本 + "'\n" +
                    "型号 = '" + 型号 + "'\n" +
                    "工业名 = '" + 工业名 + "'\n" +
                    "品牌 = '" + 品牌 + "'\n" +
                    "主板 = '" + 主板 + "'\n" +
                    "厂商 = '" + 厂商 + "'\n" +
                    "androidId = '" + androidId + "'\n" +
                    "imei = '" + imei + "'\n" +
                    "imsi = '" + imsi + "'\n" +
                    "手机号 = '" + 手机号 + "'\n" +
                    "sim = '" + sim + "'\n" +
                    "mac = '" + mac + "'\n" +
                    "运营商 = '" + 运营商 + "'\n" +
                    "isSim = '" + isSim + "'\n" +
                    "app版本 = '" + app版本 + "'\n" +
                    "isRoot = '" + isRoot + "'\n" +
                    "序列号 = '" + 序列号 + "'\n" +
                    "ip = '" + ip + "'\n" +
                    "ipMac = '" + ipMac + "'\n" +
                    "存储 = '" + 存储;
        }

        public String toJson(){
            return new Gson().toJson(this);
        }
    }

    public static EquipmentInfo getAllData(Context context) {
        int subscriptionOperatorType = getSubscriptionOperatorType(context);
        String subscriptionOperatorTypeStr = "";
        if (subscriptionOperatorType == -1)
            subscriptionOperatorTypeStr = "无sim卡";
        else if (subscriptionOperatorType == 0)
            subscriptionOperatorTypeStr = "其他";
        else if (subscriptionOperatorType == 1)
            subscriptionOperatorTypeStr = "中国移动";
        else if (subscriptionOperatorType == 2)
            subscriptionOperatorTypeStr = "中国联通";
        else if (subscriptionOperatorType == 3)
            subscriptionOperatorTypeStr = "中国电信";
        else if (subscriptionOperatorType == 5)
            subscriptionOperatorTypeStr = "中国铁通";

        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.语言 = getSystemLanguage();
        equipmentInfo.安卓版本 = getSystemVersion();
        equipmentInfo.型号 = getSystemModel();
        equipmentInfo.工业名 = getSystemDevice();
        equipmentInfo.品牌 = getDeviceBrand();
        equipmentInfo.主板 = getDeviceBoard();
        equipmentInfo.厂商 = getDeviceManufacturer();
        equipmentInfo.androidId =  getAndroidId(context);
        equipmentInfo.imei = getDeviceManufacturer();
        equipmentInfo.imsi = getIMEI(context);
        equipmentInfo.手机号 = getPhone(context);
        equipmentInfo.sim = getISMNumber(context);
        equipmentInfo.mac = getMAC(context);
        equipmentInfo.运营商 = subscriptionOperatorTypeStr;
        equipmentInfo.isSim = hasSim(context) + "";
        equipmentInfo.app版本 = getAppVersionName(context);
        equipmentInfo.isRoot =  isRoot() + "";
        equipmentInfo.序列号 =  getSerialNumber(context);
        equipmentInfo.ip =  getIPAddress(context);
        equipmentInfo.ipMac =  getLocalMacAddressFromIp();
        equipmentInfo.存储 =  "可用的大小[" + (queryStorage()[1] / 8388608) * 8 + "MB], 总容量[" + (queryStorage()[0] / 8388608) * 8 + "MB]";
        return equipmentInfo;
    }

    public static String getAll(Context context) {
        return getAllData(context).toString();
    }
    public static String getAllJson(Context context) {
        return getAllData(context).toJson();
    }

    public static boolean isAuthority(Context context, String authority) {
        return context.checkCallingOrSelfPermission(authority) == PackageManager.PERMISSION_GRANTED;
    }

}
