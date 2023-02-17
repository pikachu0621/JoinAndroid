package com.pikachu.utils.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.pikachu.utils.utils.ViewBindingUtils;


/**
 * author : pikachu
 * date   : 2021/7/28 9:59
 * version: 1.0
 * PopupWindow对话框
 */
public abstract class BasePopupWindow<T extends ViewBinding> extends PopupWindow {

    private final View root;
    private final Context context;
    public T binding;

    /**
     * 控件创建
     */
    public abstract void onViewCreate(T binding);




    @SuppressLint("UseCompatLoadingForDrawables")
    public BasePopupWindow(@NonNull Context context) {
        super(context);
        this.context = context;
        setIsWhole(false);
        setOutsideTouchable(true);
        setFocusable(true);
        setTransparent(0);

        binding = ViewBindingUtils.create(getClass(), LayoutInflater.from(context));
        root = binding.getRoot();
        //透明背景
        onViewCreate(binding);
        setContentView(root);
    }






    /**
     * 依附控件  上方显示
     *
     * @param view   依附控件
     * @param minTop 顶部空间最小值 （如果顶部空间不够则向下偏移）
     * @param yBelow 向下偏移的量
     * @param yTop   向上偏移的量
     */
    public void showAsTop(View view, int minTop, int yBelow, int yTop) {
        int height = root.getHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (location[1] > minTop) {
            super.showAsDropDown(view, (location[0] + view.getWidth() / 2) - height / 2, yBelow);
        } else {
            super.showAsDropDown(view, 0, yTop);
        }
    }

    public void showAsTop(View view, int y) {
        showAsTop(view, 400, y, y);
    }

    public void showAsTop(View view) {
        showAsTop(view, 400, 100, 100);
    }


/*    public void show(View view){

        int height = contentView.getHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (location[1] > 500 ) {
            super.showAsDropDown(view,(location[0] + view.getWidth() / 2) - height / 2 , -380);
        }else {
            super.showAsDropDown(view, 0, 100);
        }

    }*/



    /**
     * 依附控件  上下显示
     *
     * @param view   依附控件
     * @param maxTop 底部空间最小值 （如果底部空间不够则向上偏移）
     * @param yBelow 向下偏移的量
     * @param yTop   向上偏移的量
     */
    public void showAsBottom(View view, int maxTop, int yBelow, int yTop) {
        int height = root.getHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (location[1] <= maxTop) {
            super.showAsDropDown(view, (location[0] + view.getWidth() / 2) - height / 2, yBelow);
        } else {
            super.showAsDropDown(view, 0, yTop);
        }
    }


    public void showAsBottom(View view, int maxTop, int y) {
        showAsTop(view, maxTop, y, y);
    }

    public void showAsBottom(View view, int maxTop) {
        showAsTop(view, maxTop, 100, 100);
    }


    public void showAsDropDown(View view, int y) {
        showAsDropDown(view, 0, y);
    }




    /** 设置 可触摸取消 */
    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
    }

    /** 设置 可聚焦*/
    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
    }

    /**
     * 设置透明
     * 0 透明
     * */
    public void setTransparent(@IntRange(from=0,to=255) int alpha) {
        Drawable drawable = new ColorDrawable(context.getResources().getColor(android.R.color.black));
        drawable.setAlpha(alpha);
        setBackgroundDrawable(drawable);
    }


    // 全屏
    public void setIsWhole(boolean isWhole){
        setHeight(isWhole ? ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(isWhole ? ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
