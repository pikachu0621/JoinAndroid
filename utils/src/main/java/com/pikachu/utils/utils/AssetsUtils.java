package com.pikachu.utils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AssetsUtils {


    /*获取Assets图片资源*/
    public static Bitmap readAssetsBitmap(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /* 读取Assets文件夹当中的内容，存放到字符串当中 */
    public static String readAssetsString(Context context, String filename) {
        /* 1、获取Assets文件管理器*/
        AssetManager am = context.getResources().getAssets();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /*2、获取文件输入流*/
        try {
            InputStream is = am.open(filename);
            /* 读取内容存放到内存流当中*/
            int hasRead = 0;
            byte[] buf = new byte[1024];
            while (true) {
                hasRead = is.read(buf);
                if (hasRead == -1) {
                    break;
                }
                baos.write(buf, 0, hasRead);
            }
            String msg = baos.toString();
            is.close();
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }






    /**
     * 从项目assets文件夹拷贝到手机sd卡
     * @param context
     * @param isNoMedia 是否禁止媒体扫描
     */
    public static boolean copyAssets(Context context, String inPath, String outPath, boolean isNoMedia) {

        String substring = outPath.substring(outPath.length() - 1);
        if (substring.equals("/")) {
            outPath = outPath.substring(0, outPath.length() - 1);
        }
        String[] fileNames = null;
        try {// 获得Assets一共有几多文件
            fileNames = context.getAssets().list(inPath);
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        if (fileNames.length > 0) {//如果是目录
            File fileOutDir = new File(outPath);
            if (fileOutDir.isFile()) {
                fileOutDir.delete();
            }
            if (!fileOutDir.exists()) { // 如果文件路径不存在
                if (!fileOutDir.mkdirs()) { // 创建文件夹
                    return false;
                }
                if (isNoMedia){
                    try {
                        new File(fileOutDir, ".nomedia").createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (String fileName : fileNames) { //递归调用复制文件夹
                String inDir = inPath;
                String outDir = outPath + File.separator;
                if (!inPath.equals("")) { //空目录特殊处理下
                    inDir = inDir + File.separator;
                }
                copyAssets(context, inDir + fileName, outDir + fileName, isNoMedia);
            }
            return true;
        } else {//如果是文件
            try {
                File fileOut = new File(outPath);
                if (fileOut.exists()) {
                    fileOut.delete();
                }
                fileOut.createNewFile();
                FileOutputStream fos = new FileOutputStream(fileOut);
                InputStream is = context.getAssets().open(inPath);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }



    // ini 文件解析
    public static String readIniNoSection(String iniPath, String key){
        try{
            File file = new File(iniPath);
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            if(!properties.contains(key)){
                return null;
            }
            return String.valueOf(properties.get(key));
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }







}
