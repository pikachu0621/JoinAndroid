package com.pikachu.utils.utils;


import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.IntDef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * author : pikachu
 * date   : 2021/7/2816:50
 * version: 1.0
 */
public class LoadUrlUtils {


    private String url;
    private int type;
    private OnCall onCall;
    private int connectTime = 20000;
    private int readTime = 20000;
    private String postStr;
    private String charset = "UTF-8";


    @IntDef(value = {Type.GET, Type.POST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int GET = 1;
        int POST = 2;
    }

    public interface OnCall {
        void error(Exception e);

        void finish(String str);
    }



    //get utf-8
    public LoadUrlUtils(String url, OnCall onCall) {
        this.url = url;
        type = Type.GET;
        this.onCall = onCall;
        start();
    }

    //get utf-8
    public LoadUrlUtils( String url, String charset, OnCall onCall) {
        this.url = url;
        this.charset = charset == null || charset.equals("") ? "UTF-8" : charset;
        type = Type.GET;
        this.onCall = onCall;
        start();
    }


    //post charset
    public LoadUrlUtils( String url, String postStr, String charset, OnCall onCall) {
        this.url = url;
        this.postStr = postStr;
        this.charset = charset == null? "UTF-8" : charset;
        this.onCall = onCall;
        type = Type.POST;
        start();
    }


    @SuppressLint("NewApi")
    private void start() {
        new Thread(() -> {
            try {
                HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
                http.setRequestMethod(type == Type.GET ? "GET" : "POST");
                http.setConnectTimeout(connectTime);
                http.setReadTimeout(readTime);
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                http.setRequestProperty("user-agent", " Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3858.400 QQBrowser/10.7.4317.400");

                if (Type.POST == type) {
                    http.setDoOutput(true);
                    OutputStream outputStream = http.getOutputStream();
                    outputStream.write(postStr.getBytes());
                    outputStream.flush();
                    outputStream.close();
                }

                int responseCode = http.getResponseCode();
                if (responseCode == 200) {

                    try{
                        Pattern pattern = Pattern.compile("charset=\\S*");
                        Matcher matcher = pattern.matcher(http.getContentType());
                        if (matcher.find()) {
                            charset = matcher.group().replace("charset=", "");
                        }
                    }catch (Exception ignored){
                    }

                    InputStream inputStream = http.getInputStream();
                    BufferedReader bufferedReader;

                    String contentEncoding = http.getContentEncoding();
                    if (contentEncoding != null && contentEncoding.equals("gzip"))
                        bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream), charset));
                    else
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));


                    StringBuffer stringBuffer = new StringBuffer();
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        stringBuffer.append(str);
                    }
                    UiUtils.runUi(() -> onCall.finish(stringBuffer.toString()));
                } else {
                    UiUtils.runUi(() -> onCall.error(new Exception("error->" + responseCode)));
                }


            } catch (IOException e) {
                e.printStackTrace();
                UiUtils.runUi(() -> onCall.error(e));
            }


        }).start();


    }


}
