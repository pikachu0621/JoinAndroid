package com.pikachu.utils.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.pm.ShortcutManagerCompat;

import com.google.gson.Gson;

import java.util.Set;

import static com.pikachu.utils.type.XmlType.XName;

/**
 * author : pikachu
 * date   : 2021/7/28 13:47
 * version: 1.0
 * 本地 xml储存
 */

public final class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences;
    private static String xmlName = XName;

    public static SharedPreferences init(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences( xmlName, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static SharedPreferences init(Context context, String xmlName) {
        SharedPreferencesUtils.xmlName = xmlName;
        return init(context);
    }



    public static void write(String key, String var) {
        if (isNull(sharedPreferences)) return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, var);
        edit.apply();
    }

    public static void write(String key, int var) {
        if (isNull(sharedPreferences)) return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, var);
        edit.apply();
    }

    public static void write(String key, float var) {
        if (isNull(sharedPreferences)) return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putFloat(key, var);
        edit.apply();
    }

    public static void write(String key, long var) {
        if (isNull(sharedPreferences)) return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(key, var);
        edit.apply();
    }

    public static void write(String key, boolean var) {
        if (isNull(sharedPreferences)) return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, var);
        edit.apply();
    }

    public static void write(String key, Set<String> values) {
        if (isNull(sharedPreferences)) return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(key, values);
        edit.apply();
    }

    public static void write(String key, Object values) {
        if (isNull(sharedPreferences)) return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(values);
        edit.putString(key, s);
        edit.apply();
    }


    public static String readString(String key) {
        return readString(key, null);
    }
    public static int readInt(String key) {
        return readInt(key, -1);
    }
    public static float readFloat(String key) {
        return readFloat(key, -1);
    }
    public static long readLong(String key) {
        return readLong(key, -1);
    }
    public static boolean readBoolean(String key) {
        return readBoolean(key, false);
    }
    public static Set<String> readStrings(String key) {
        return readStrings(key, null);
    }
    public static <T> T readObject(String key, Class<T> cls) {
        String string = readString(key);
        if (string == null || string.equals(""))
            return null;
        Gson gson = new Gson();
        return gson.fromJson(string, cls);
    }


    public static String readString(String key, String v) {
        if (isNull(sharedPreferences)) return null;
        return sharedPreferences.getString(key, v);
    }
    public static int readInt(String key, int v) {
        if (isNull(sharedPreferences)) return 0;
        return sharedPreferences.getInt(key, v);
    }
    public static float readFloat(String key, float v) {
        if (isNull(sharedPreferences)) return 0;
        return sharedPreferences.getFloat(key, v);
    }
    public static long readLong(String key, long v) {
        if (isNull(sharedPreferences)) return 0;
        return sharedPreferences.getLong(key, v);
    }
    public static boolean readBoolean(String key, boolean v) {
        if (isNull(sharedPreferences)) return false;
        return sharedPreferences.getBoolean(key, v);
    }
    public static Set<String> readStrings(String key, Set<String> v) {
        if (isNull(sharedPreferences)) return null;
        return sharedPreferences.getStringSet(key, v);
    }


    private static boolean isNull(String msg, Object object){
        if (object == null){
            LogsUtils.showLog(LogsUtils.E, SharedPreferencesUtils.class, msg);
            return true;
        }
        return false;
    }
    private static boolean isNull(Object object){
        return isNull("SharedPreferencesUtils：未初始化    [ 请使用 info() 方法初始 ]", object);
    }



}
