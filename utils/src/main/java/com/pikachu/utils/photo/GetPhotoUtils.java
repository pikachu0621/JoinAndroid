package com.pikachu.utils.photo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import androidx.annotation.IntDef;


import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author pkpk.run
 * @project ps
 * @package com.pikachu.image
 * @date 2021/9/23
 * @description 略
 */
public class GetPhotoUtils {


    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static String data = MediaStore.Images.Media.DATA;
    private static String typeStr = "图片";

    @IntDef(value = {Type.Image, Type.Audio, Type.Video})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int Image = 0;
        int Audio = 1;
        int Video = 2;
    }


    @SuppressLint("SwitchIntDef")
    public static void init(Context context, @Type int type) {
        GetPhotoUtils.context = context;
        switch (type) {
            case Type.Audio:
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                data = MediaStore.Audio.Media.DATA;
                typeStr = "音频";
                break;
            case Type.Video:
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                data = MediaStore.Video.Media.DATA;
                typeStr = "视频";
                break;
            default:
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                data = MediaStore.Images.Media.DATA;
                typeStr = "图片";
        }
    }




    public static String getTypeStr(){
        return typeStr;
    }



    // 获取全部图片   数据库里的
    public static List<String> getSystemList() {
        List<String> result = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        @SuppressLint("Recycle")
        Cursor cursor = contentResolver.query(uri, null, null, null, ContactsContract.Contacts._ID + " DESC");
        if (cursor == null || cursor.getCount() <= 0) return result; // 没有图片
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndexOrThrow(data);
            String path = cursor.getString(index); // 文件地址
            result.add(path);
        }
        return result;
    }


    // 获取相册 （按文件夹分  不同文件夹算一个）
    public static List<PhotoModule> getSystemLibs() {
        String fileOne = null;
        List<String> files = new ArrayList<>();
        List<PhotoModule> photoModules = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        @SuppressLint("Recycle")
        Cursor cursor = contentResolver.query(uri, null, null, null, ContactsContract.Contacts._ID + " DESC");
        if (cursor == null || cursor.getCount() <= 0) return null;
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndexOrThrow(data);
            String path = cursor.getString(index);
            files.add(path);
            if (fileOne == null){
                fileOne = path;
            }
            compareAdd(photoModules, path);
        }
        if (photoModules.size() > 0) {
            Collections.sort(photoModules, (o1, o2) -> o2.getFiles().size() - o1.getFiles().size());
            photoModules.add(0, new PhotoModule("", "全部"+ typeStr , fileOne, files));
        }
        return photoModules;

    }





    private static void compareAdd(List<PhotoModule> photoModules, String file){

        getParent(file);
        String parent = getParent(file);

        for (PhotoModule photoModule : photoModules){
            String path = photoModule.getPath() == null ? "" : photoModule.getPath();
            if (parent.equals(path)){
                photoModule.getFiles().add(file);
                return;
            }
        }

        String parentFile = getParent(file);

        String name = "";
        name = getName(parentFile);
        List<String> files = new ArrayList<>();
        files.add(file);
        photoModules.add(new PhotoModule(parent, name, file, files));
    }


    private static String getParent(String filePath){
         return filePath.substring(0, filePath.lastIndexOf(File.separator));
    }

    private static String getName(String filePath){
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1 );
    }


}
