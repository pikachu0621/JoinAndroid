package com.pikachu.utils.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

/**
 * bitmap 工具类
 */
public final class BitmapUtils {





    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin 原图
     * @param newWidth 新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public static  Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio 比例
     * @return 新的bitmap
     */
    public static  Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }



    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha 旋转角度，可正可负
     * @return 旋转后的图片
     */
    public static  Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 偏移效果
     * @param origin 原图
     * @return 偏移后的bitmap
     */
    public static  Bitmap skewBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.postSkew(-0.6f, -0.3f);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }



    // 设置图片宽高
    public static Bitmap bitmapZoom(Bitmap bm, int newWidth, int newHeight) {
        Bitmap newbm = null;
        if (bm != null) {
            // 获得图片的宽高
            int width = bm.getWidth();
            int height = bm.getHeight();
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        }
        return newbm;
    }


    /**
     * 压缩图片
     *
     * @param bitmap 位图
     * @param radius 压缩半径 0 - 1
     * @return
     */
    public static Bitmap bitmapRgb565(Bitmap bitmap,@FloatRange(from = 0, to = 1F) float radius){
        Bitmap copy = bitmap.copy(Bitmap.Config.RGB_565, true);
        Matrix matrix = new Matrix();
        matrix.setScale( 1 - radius, 1 - radius);
        Bitmap bitmap1 = Bitmap.createBitmap(copy, 0, 0, copy.getWidth(), copy.getHeight(), matrix, false);
        Bitmap bitmap2 = bitmapZoom(bitmap1, bitmap.getWidth(), bitmap.getHeight());
        copy.recycle();
        bitmap1.recycle();
        return bitmap2;
    }




    /**
     * 裁剪图片
     *
     * @param bitmap 原图
     * @param rectF 剪裁矩形
     * @return bitmap
     */
    public static Bitmap cropBitmap(Bitmap bitmap, RectF rectF) {
        int width = (int) rectF.width();
        int height = (int) rectF.height();
        if (width <= 0 || height <= 0) return null;
        return Bitmap.createBitmap(bitmap, (int) rectF.left, (int) rectF.top, width, height, null, false);
    }


    /**
     * 居中裁剪
     * @param bm 位图
     * @return
     */
    public static Bitmap centerCrop(Bitmap bm, int newWidth, int newHeight) {
        int w = bm.getWidth(); // 得到图片的宽，高
        int h = bm.getHeight();
        int retX;
        int retY;
        double wh = (double) w / (double) h;
        double nwh = (double) newWidth / (double) newHeight;
        if (wh > nwh) {
            retX = h * newWidth / newHeight;
            retY = h;
        } else {
            retX = w;
            retY = w * newHeight / newWidth;
        }
        int startX = w > retX ? (w - retX) / 2 : 0;//基于原图，取正方形左上角x坐标
        int startY = h > retY ? (h - retY) / 2 : 0;
        Bitmap bit = Bitmap.createBitmap(bm, startX, startY, retX, retY, null, false);
        bm.recycle();
        return bit;
    }





    /**
     * 径向模糊
     * pkpk.run
     * 有点慢
     * @return
     */
    public static Bitmap radialBlur(@NonNull  Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int w1 = width - 1;
        int h1 = height - 1;
        Bitmap bTopToBottom = Bitmap.createBitmap(width, height, bitmap.getConfig());
        for (int i = 0; i < width; i++) {
            int pixel = bitmap.getPixel(i, 0);
            int alpha = Color.alpha(pixel);
            int blue = Color.blue(pixel);
            int red = Color.red(pixel);
            int green = Color.green(pixel);
            float alphaRad =  alpha / (float)height;
            for (int h = 0; h < height; h++) {
                bTopToBottom.setPixel(i, h, Color.argb( (int)(alpha - alphaRad * h), red, green, blue));
            }
        }
        Bitmap bBottomToTop = Bitmap.createBitmap(width, height, bitmap.getConfig());
        for (int i = 0; i < width; i++) {
            int pixel = bitmap.getPixel(i, h1);
            int alpha = Color.alpha(pixel);
            int blue = Color.blue(pixel);
            int red = Color.red(pixel);
            int green = Color.green(pixel);
            float alphaRad =  alpha / (float) height;
            for (int h = 0; h < height; h++) {
                bBottomToTop.setPixel(i, h1 - h, Color.argb( (int)(alpha - alphaRad * h), red, green, blue));
            }
        }
        Bitmap bLiftToRight = Bitmap.createBitmap(width, height, bitmap.getConfig());
        for (int i = 0; i < height; i++) {
            int pixel = bitmap.getPixel(0, i);
            int alpha = Color.alpha(pixel);
            int blue = Color.blue(pixel);
            int red = Color.red(pixel);
            int green = Color.green(pixel);
            float alphaRad =  alpha / (float) width;
            for (int w = 0; w < width; w++) {
                bLiftToRight.setPixel(w, i, Color.argb( (int)(alpha - alphaRad * w), red, green, blue));
            }
        }
        Bitmap bRightToLift = Bitmap.createBitmap(width, height, bitmap.getConfig());
        for (int i = 0; i < height; i++) {
            int pixel = bitmap.getPixel(w1, i);
            int alpha = Color.alpha(pixel);
            int blue = Color.blue(pixel);
            int red = Color.red(pixel);
            int green = Color.green(pixel);
            float alphaRad =  alpha / (float) width;
            for (int w = 0; w < width; w++) {
                bRightToLift.setPixel(w1 - w, i, Color.argb( (int)(alpha - alphaRad * w), red, green, blue));
            }
        }
        Bitmap bZ = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(bZ);
        canvas.drawBitmap(bTopToBottom,0, 0, null);
        canvas.drawBitmap(bBottomToTop,0, 0, null);
        canvas.drawBitmap(bLiftToRight,0, 0, null);
        canvas.drawBitmap(bRightToLift,0, 0, null);
        bitmap.recycle();
        bTopToBottom.recycle();
        bBottomToTop.recycle();
        bRightToLift.recycle();
        bLiftToRight.recycle();
        return bZ;

    }









    /**
     * 模糊处理 快速 效果不太好
     *
     * @param context 上下文
     * @param bitmap 位图
     * @param blurRadius 置渲染的模糊程度, 25f是最大模糊度
     * @return
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap,@FloatRange(from = 0, to = 25F) float blurRadius) {
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap);
        Allocation tmpIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        blurScript.setRadius(blurRadius);
        blurScript.setInput(tmpIn);
        blurScript.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        bitmap.recycle();
        rs.destroy();
        return outputBitmap;
    }

    /**
     * 模糊处理 快速 效果不太好
     *
     * @param context 上下文
     * @param bitmap 位图
     * @param blurRadius 置渲染的模糊程度, 25f是最大模糊度
     * @return
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float blurRadius, float sf) {
        float BITMAP_SCALE = sf;//图片缩放比例
        int width = Math.round(bitmap.getWidth() * BITMAP_SCALE);// 计算图片缩小后的长宽
        int height = Math.round(bitmap.getHeight() * BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);// 将缩小后的图片做为预渲染的图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);// 创建一张渲染后的输出图片
        RenderScript rs = RenderScript.create(context);// 创建RenderScript内核对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));// 创建一个模糊效果的RenderScript的工具对象
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);// 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);// 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        blurScript.setRadius(blurRadius); // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setInput(tmpIn);// 设置blurScript对象的输入内存
        blurScript.forEach(tmpOut);// 将输出数据保存到输出内存中
        tmpOut.copyTo(outputBitmap);// 将数据填充到Allocation中
        inputBitmap.recycle();
        rs.destroy();
        return outputBitmap;
    }






    /**
     * 高斯模糊
     * 慢速 效果好
     *
     * @param sentBitmap
     * @param radius 100
     * @return
     */
    public static Bitmap blurBitmap(Bitmap sentBitmap, int radius) {
        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;

    }







}
