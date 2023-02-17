package com.pikachu.utils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.net.URL;

/**
 * @ProjectName: 一带一路
 * @Package: com.pikachu.utils.utils
 * @Author: pkpk.run
 * @Description: null
 */
public class GlideUtils {


    public static Load with(Context context){
        return new Load(Glide.with(context));
    }

    public static class Load {
        private final RequestManager requestManager;

        private Load(RequestManager requestManager) {
            this.requestManager = requestManager;
        }

        public Into load(@Nullable Bitmap bitmap){
            return new Into(requestManager.load(bitmap));
        }

        public Into load(@Nullable Drawable drawable){
            return new Into(requestManager.load(drawable));
        }

        public Into load( @Nullable String string){
            return new Into(requestManager.load(string));
        }

        public Into load(@Nullable Uri uri){
            return new Into(requestManager.load(uri));
        }

        public Into load(@Nullable URL url){
            return new Into(requestManager.load(url));
        }

        public Into load(@RawRes @DrawableRes @Nullable Integer resourceId){
            return new Into(requestManager.load(resourceId));
        }

        public Into load(@Nullable File file){
            return new Into(requestManager.load(file));
        }
    }


    public static class Into{

        private RequestBuilder<Drawable> load;

        private Into(RequestBuilder<Drawable> load) {
            this.load = load;
        }


        /**
         * 圆角
         * @param roundingRadius 圆角半径
         * @return
         */
        public Into rounded(int roundingRadius){
            load = load.apply(RequestOptions.bitmapTransform(new RoundedCorners(roundingRadius)));
            return this;
        }

        /**
         * 渐显动画
         * @param transitionTime ms
         * @return
         */
        public Into transition(int transitionTime){
            load = load.transition(DrawableTransitionOptions.withCrossFade(transitionTime));
            return this;
        }

        public void into(ImageView imageView){
            load.into(imageView);
        }
    }



}
