package com.pikachu.utils.photo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import com.pikachu.utils.utils.LogsUtils;
import com.pikachu.utils.utils.UiUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * @author pkpk.run
 * @package com.pikachu.utils.photo
 * @date 2021/10/24
 * @description 保存到公共文件里 并更新相册
 */
public class SavePhotoUtils {


    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String systemPathSecondary = null;
    private static String systemImagePath, systemAudioPath, systemVideoPath;


    public interface SaveBackgroundTask {
        void onOk();
        void oError(Exception e);
    }


    @IntDef({MediaType.AUDIO, MediaType.VIDEO, MediaType.IMAGE})
    public @interface MediaType {
        int AUDIO = 1;
        int VIDEO = 2;
        int IMAGE = 3;
    }


    /**
     * 初始
     *
     * @param context   上下文
     * @param secondary 二级目录   例： DCIM/xxx(secondary)
     */
    public static void init(Context context, String secondary) {
        init(context, Environment.DIRECTORY_DCIM, Environment.DIRECTORY_MUSIC, Environment.DIRECTORY_MOVIES, secondary);
    }


    /**
     * 初始
     *
     * @param context   上下文
     * @param imagePath 图片公共目录
     * @param audioPath 音频公共目录
     * @param videoPath 视频公共目录
     * @param secondary 二级目录  例： DCIM/xxx(secondary)
     */
    public static void init(Context context, String imagePath, String audioPath, String videoPath, String secondary) {
        SavePhotoUtils.context = context;
        SavePhotoUtils.systemImagePath = imagePath;
        SavePhotoUtils.systemAudioPath = audioPath;
        SavePhotoUtils.systemVideoPath = videoPath;
        SavePhotoUtils.systemPathSecondary = secondary;
    }


    /**
     * 同步处理
     *
     * @param type 文件类型
     * @param file 文件
     * @param isDelete 是否删除源文件
     * @return 是否成功
     */
    public static boolean to(@MediaType int type, File file, boolean isDelete) {
        if (context == null || systemPathSecondary == null ||
                systemImagePath == null || systemAudioPath == null || systemVideoPath == null){
            LogsUtils.showLog(LogsUtils.E, "SavePhotoUtils 未初始化");
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                toVersionUQ(type, file, isDelete);
                return true;
            }
            toVersionDQ(type, file, isDelete);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean to(@MediaType int type, String filePath, boolean isDelete) {
        return to(type, new File(filePath), isDelete);
    }


    /**
     * 异步处理
     *
     * @param type 文件类型
     * @param file 文件
     * @param isDelete 是否删除源文件
     * @param saveBackgroundTask 回调
     */
    public static void to(@MediaType int type, File file, boolean isDelete, SaveBackgroundTask saveBackgroundTask) {
        if (context == null || systemPathSecondary == null ||
                systemImagePath == null || systemAudioPath == null || systemVideoPath == null){
            saveBackgroundTask.oError(new Exception("SavePhotoUtils 未初始化"));
            return;
        }
        Thread thread = new Thread(() -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    toVersionUQ(type, file, isDelete);
                else
                    toVersionDQ(type, file, isDelete);
                UiUtils.runUi(saveBackgroundTask::onOk);
            }catch (Exception e){
                UiUtils.runUi(() -> saveBackgroundTask.oError(e));
            }
        });
        thread.start();
    }

    public static void to(@MediaType int type, String filePath, boolean isDelete, SaveBackgroundTask saveBackgroundTask) {
        to(type, new File(filePath), isDelete, saveBackgroundTask);
    }


    // android Q 及其以上更新
    @SuppressLint("SwitchIntDef")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void toVersionUQ(@MediaType int type, File file, boolean isDelete) throws Exception {
        ContentResolver contentResolver = SavePhotoUtils.context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, URLConnection.getFileNameMap().getContentTypeFor(file.getName()));
        Uri uri;
        switch (type) {
            case MediaType.AUDIO:
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, systemAudioPath + File.separator + systemPathSecondary + File.separator);
                uri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
                break;
            case MediaType.VIDEO:
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, systemVideoPath + File.separator + systemPathSecondary + File.separator);
                uri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                break;
            default:
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, systemImagePath + File.separator + systemPathSecondary + File.separator);
                uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        if (uri == null) throw new Exception("Uri 为空");
        OutputStream out = contentResolver.openOutputStream(uri);
        FileInputStream fis = new FileInputStream(file);
        FileUtils.copy(fis, out);
        fis.close();
        out.close();
        if (isDelete) {
            // 删除原有的
            com.pikachu.utils.utils.FileUtils.deleteFile(file);
        }
        //更新
        contentResolver.update(uri, values, null, null);
    }


    // android Q 以下更新
    private static void toVersionDQ(@MediaType int type, File file, boolean isDelete) throws Exception {
        File destDir;
        switch (type) {
            case MediaType.AUDIO:
                destDir = new File(Environment.getExternalStoragePublicDirectory(systemAudioPath), systemPathSecondary);
                break;
            case MediaType.VIDEO:
                destDir = new File(Environment.getExternalStoragePublicDirectory(systemVideoPath), systemPathSecondary);
                break;
            default:
                destDir = new File(Environment.getExternalStoragePublicDirectory(systemImagePath), systemPathSecondary);
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File newFile = new File(destDir.getPath() + File.separator + file.getName());
        if (!com.pikachu.utils.utils.FileUtils.copyFile(file, newFile))
            throw new Exception("复制文件失败");
        if (isDelete)
            com.pikachu.utils.utils.FileUtils.deleteFile(file);
        ContentResolver contentResolver = SavePhotoUtils.context.getContentResolver();
        String path = newFile.getPath();
        if (type == MediaType.IMAGE)
            MediaStore.Images.Media.insertImage(contentResolver, path, newFile.getName(), null);
        else
            SavePhotoUtils.context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }


}
