package com.pikachu.utils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 文件读取工具类
 */
public final class FileUtils {

    /**
     * 读取文件内容，作为字符串返回
     */
    public static String readFileAsString(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath());
        }
        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File is too large");
        }
        StringBuilder sb = new StringBuilder((int) (file.length()));
        FileInputStream fis = new FileInputStream(file);
        byte[] bbuf = new byte[1024];
        int hasRead = 0;
        while ((hasRead = fis.read(bbuf)) > 0) {
            sb.append(new String(bbuf, 0, hasRead));
        }
        fis.close();
        return sb.toString();
    }

    public static String readFileString(String filePath){
        try {
          return readFileAsString(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readFileString(File file){
        try {
            return readFileAsString(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath());
        } else {

            try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {
                BufferedInputStream in = null;
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }
                return bos.toByteArray();
            }
        }
    }



    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return deleteFile(file);
    }

    public static boolean deleteFile(File file) {
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }


    // 获取文件扩展名
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }


    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param dir 被删除目录的文件路径
     * @return 目录删除成功返回true, 否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory())
            return false;
        boolean flag = true;
        File[] files = dirFile.listFiles();
        if (files == null)
            return false;
        for (File file : files) {
            if (file.isFile())
                flag = deleteFile(file.getAbsolutePath());
            else
                flag = deleteDirectory(file.getAbsolutePath());
            if (!flag) break;
        }
        return flag;/* dirFile.delete()*/
    }


    // 保存图片
    public static File saveImage(@NonNull File file, @NonNull Bitmap bmp) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File saveImage(@NonNull String path, @NonNull Bitmap bmp) {
        return saveImage(new File(path), bmp);
    }


    /**
     * @param bitmap 保存的图片
     * @param ratio  缩放值
     * @return 返回
     */
    public static File saveImage(@NonNull String path, Bitmap bitmap, float ratio) {

        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        File dirFile = new File(path);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dirFile);
            newBitMap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return dirFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    // 通知相册更新
    public static void notifySystemGallery(@NonNull Context context, @NonNull String path) {
        notifySystemGallery(context, new File(path));
    }

    // 通知相册更新
    public static void notifySystemGallery(@NonNull Context context, @NonNull File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("bmp should not be null");
        }

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(),
                    file.getName(), null);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File couldn't be found");
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }


    /**
     * @param oldPathName 原文件路径+文件名
     * @param newPathName 复制后路径+文件名
     * @return
     */
    public static boolean copyFile(String oldPathName, String newPathName) {
        return copyFile(new File(oldPathName), new File(newPathName));
    }

    /**
     * 复制文件
     *
     * @return
     */
    public static boolean copyFile(File oldFile, File newFile) {
        try {
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(oldFile);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /*将字符串写入到文本文件中*/
    public static void writeTxtToFile(String strcontent, String dir) {
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(dir);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + dir);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }

    }

    /*获取文件修改时间*/
    public static String getFileLastModifiedTime(File file) {
        Calendar cal = Calendar.getInstance();
        long time = file.lastModified();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        cal.setTimeInMillis(time);
        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.getTime());
    }

    /*获取文件大小*/
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /*获取文件路径*/
    public static String getPath(String dir) {
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        return dir;
    }


}
