package com.pikachu.utils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.pikachu.utils.R;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author Pikachu
 * @Project 流量专家
 * @Package com.stkj.charmflowstool.utils
 * @Date 2021/8/10 ( 下午 5:17 )
 * @description 正则 和 随机数
 */
public final class OtherUtils {


    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    public static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static int random(int max) {
        return random(1, max);
    }

    public static int random() {
        return random(1, 100);
    }


    // 判断手机号码是否规则
    public static boolean isPhoneNumber(String input) {
        String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
        //Pattern p = Pattern.compile(regex);
        return Pattern.matches(regex, input);
    }



    public static double keepDecimal(double var, int keep) {
        BigDecimal bg = new BigDecimal(var);
        return bg.setScale(keep, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String keepDecimal(float var, int keep) {
        return String.format("%."+keep+"f", var);
    }










    public static boolean isAuthority(Context context, String authority) {
        return context.checkCallingOrSelfPermission(authority) == PackageManager.PERMISSION_GRANTED;
        //return ContextCompat.checkSelfPermission(context, authority) == PackageManager.PERMISSION_DENIED;
       /* PackageManager pm = context.getPackageManager();
        return  (PackageManager.PERMISSION_GRANTED == pm.checkPermission(authority, context.getApplicationInfo().packageName));*/
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ObsoleteSdkInt")
    public static boolean hasPermission(Context context) {
        AppOpsManager appOpsM = (AppOpsManager)
                context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mode = appOpsM.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }


    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }





    /**
     * 隐藏软件盘
     */
    //view为接受软键盘输入的视图，SHOW_FORCED表示强制显示
    public static void showOrHide(@NonNull Context context, @NonNull View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //  imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);//SHOW_FORCED表示强制显示
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(@NonNull EditText editText) {
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    //如果输入法在窗口上已经显示，则隐藏，反之则显示
    public static void showOrHide(@NonNull Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //获取输入法打开的状态
    //isOpen若返回true，则表示输入法打开
    public static boolean isShowing(@NonNull Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }


    public interface OnSoftInputHeight {
        void softInputHeight(int realKeyboardHeight);

        void onKeyboardShown(int realKeyboardHeight);

        void onKeyboardHidden();
    }


    /**
     * 监听软盘高度
     *
     * @param activity          activity
     * @param minHeight         最小键盘高度的阈值
     * @param onSoftInputHeight 监听回调
     */
    public static void setOnGlobalLayout(Activity activity, int minHeight, OnSoftInputHeight onSoftInputHeight) {
        View decorView = activity.getWindow().getDecorView();
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;
            int i = 0;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();
                int navigationBarHeight = 0;
                if (UiUtils.isNavigationBarShowing(activity)) {
                    navigationBarHeight = UiUtils.getNavigationBarHeight(activity);
                }

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + minHeight) {
                        int currentKeyboardHeight = decorView.getHeight() - windowVisibleDisplayFrame.bottom - navigationBarHeight;
                        onSoftInputHeight.onKeyboardShown(currentKeyboardHeight);
                    } else if (lastVisibleDecorViewHeight + minHeight < visibleDecorViewHeight /*&&*/) {
                        onSoftInputHeight.onKeyboardHidden();
                        //LogsUtils.showLog("---" + lastVisibleDecorViewHeight + minHeight +"   ---    "+visibleDecorViewHeight);
                    }
                    //LogsUtils.showLog(lastVisibleDecorViewHeight + minHeight +"   ---    "+visibleDecorViewHeight);
                }
                int i = decorView.getHeight() - windowVisibleDisplayFrame.bottom - navigationBarHeight;
                if (this.i != i && i > 10) {
                    this.i = i;
                    if (i < minHeight) {
                        i = minHeight;
                    }
                    onSoftInputHeight.softInputHeight(i);
                }
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }


    /**
     * 取消监听
     *
     * @param activity
     */
    public static void removeOnGlobalLayout (Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        if (onGlobalLayoutListener != null && decorView.getViewTreeObserver().isAlive()) {
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }



    // view 转 Bitmap
    public static Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * 自定义view转图片
     *
     * @param view
     * @return
     */
    public static Bitmap viewToBitmap(View view){
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();

    }

    /**
     * 取反色
     *
     * @param bitmap
     * @return
     */
    public static Bitmap reverseColor(Bitmap bitmap) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int originColor = bitmap.getPixel(i, j);
                int alpha = Color.alpha(originColor);
                int red = Color.red(originColor);
                int green = Color.green(originColor);
                int blue = Color.blue(originColor);
                bitmap.setPixel(i, j, Color.argb(alpha, 255 - red, 255 - green, 255 - blue));
            }
        }
        return bitmap;
    }




    /**
     * 类似ImageView的fitCenter算法
     *
     * @param x 画布宽
     * @param y 画布高
     * @param w 填充矩形宽
     * @param h 填充矩形高
     * @return 缩放后的矩形
     */
    public static RectF fitCenter(int x, int y, int w, int h) {
        Matrix matrix = new Matrix();
        float sx = (float) x / w;
        float sy = (float) y / h;
        float minScale = Math.min(sx, sy);
        matrix.postScale(minScale, minScale);
        if (sx == minScale) {
            float translate = (h * minScale - y) / 2f;
            matrix.postTranslate(0, -translate);
        } else {
            float translate = (w * minScale - x) / 2f;
            matrix.postTranslate(-translate, 0);
        }
        RectF rectF = new RectF();
        matrix.mapRect(rectF);
        LogsUtils.showLog(String.format("x: %s, y: %s, w: %s, h: %s, rectF.width(): %s, rectF.height(): %s", x, y, w, h, rectF.width(), rectF.height()));
        LogsUtils.showLog(rectF.toString());
        return rectF;
    }

    /**
     * 转数组
     *
     * @param w 画布宽
     * @param h 画布高
     * @param rectF 矩形
     * @return  int[0] = 宽   int[1] = 高
     *          int[2] = 相对与画布 居中位置左上角X坐标
     *          int[3] = 相对与画布 居中位置左上角Y坐标
     */
    public static int[] asRectFArrayInt(int w, int h, RectF rectF){
        int[] wh = new int[4];
        wh[0] = (int) (w - rectF.right - rectF.left);
        wh[1] = (int) (h - rectF.top - rectF.bottom);
        wh[2] = (int) rectF.left;
        wh[3] = (int) rectF.top;
        LogsUtils.showLog(String.format("wh[0]: %s, wh[1]: %s, wh[2]: %s, wh[3]: %s", wh[0], wh[1], wh[2], wh[3]));
        return wh;
    }


}
