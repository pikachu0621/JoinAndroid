package com.pikachu.utils.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.pikachu.utils.utils.UiUtils;
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



    public BasePopupWindow(@NonNull Context context) {
        super(context);
        this.context = context;
        setIsWhole(false);
        setOutsideTouchable(true);
        setFocusable(true);
        setTransparent(0);
        binding = ViewBindingUtils.create(getClass(), LayoutInflater.from(context));
        root = binding.getRoot();
    }
    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }

    private interface ComputeShow {
        class ComputeData {
            int byViewHeight;
            int byViewWidth;
            int rootHeight;
            int rootWidth;
            int byViewLeft;
            int byViewTop;

            public ComputeData(int byViewHeight, int byViewWidth, int rootHeight, int rootWidth, int byViewLeft, int byViewTop) {
                this.byViewHeight = byViewHeight;
                this.byViewWidth = byViewWidth;
                this.rootHeight = rootHeight;
                this.rootWidth = rootWidth;
                this.byViewLeft = byViewLeft;
                this.byViewTop = byViewTop;
            }
        }
        void compute(ComputeData computeData);
    }

    private void computeShow(View view, ComputeShow computeShow){
        onViewCreate(binding);
        setContentView(root);
        ViewTreeObserver viewTreeObserver = root.getViewTreeObserver();
        super.showAsDropDown(view,   0 , 0);
        viewTreeObserver.addOnPreDrawListener(() -> {
            dismiss();
            int byViewHeight = view.getMeasuredHeight();
            int byViewWidth = view.getMeasuredWidth();
            int rootHeight = root.getMeasuredHeight();
            int rootWidth = root.getMeasuredWidth();
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int byViewLeft = location[0];
            int byViewTop = location[1];
            computeShow.compute(new ComputeShow.ComputeData(byViewHeight, byViewWidth, rootHeight, rootWidth, byViewLeft, byViewTop));
            return true;
        });
    }

    /**
     * 首选显示上部 上部空间不够显示下部
     *
     * @param view
     * @param xoff
     * @param yoff
     */
    public void showTop(View view, int xoff, int yoff) {
        computeShow(view, computeData -> {
            if (computeData.byViewTop >= computeData.rootHeight) {
                if (computeData.byViewLeft + computeData.byViewWidth / 2 > computeData.rootWidth / 2){
                    showAsDropDown(view,   - (computeData.rootWidth / 2  - computeData.byViewWidth / 2)  + xoff, -(computeData.byViewHeight + computeData.rootHeight) + yoff);
                } else {
                    showAsDropDown(view,   xoff , -(computeData.byViewHeight + computeData.rootHeight) + yoff);
                }
            } else {
                if (computeData.byViewLeft + computeData.byViewWidth / 2 > computeData.rootWidth / 2){
                    showAsDropDown(view,   - (computeData.rootWidth / 2  - computeData.byViewWidth / 2)  + xoff ,  yoff);
                } else {
                    showAsDropDown(view,   xoff , yoff);
                }
            }
        });
    }

    public void showBottom(View view, int xoff, int yoff) {
        computeShow(view, computeData -> {
            if (computeData.byViewTop < computeData.rootHeight) {
                if (computeData.byViewLeft + computeData.byViewWidth / 2 > computeData.rootWidth / 2){
                    super.showAsDropDown(view,   - (computeData.rootWidth / 2  - computeData.byViewWidth / 2)  + xoff, yoff);
                } else {
                    super.showAsDropDown(view,   xoff ,  yoff);
                }
            } else {
                if (computeData.byViewLeft + computeData.byViewWidth / 2 > computeData.rootWidth / 2){
                    super.showAsDropDown(view,   - (computeData.rootWidth / 2  - computeData.byViewWidth / 2)  + xoff ,  -(computeData.byViewHeight + computeData.rootHeight) + yoff);
                } else {
                    super.showAsDropDown(view,   xoff , yoff);
                }
            }
        });
    }

    public void showLeft(View view, int xoff, int yoff) {
        computeShow(view, computeData -> {
            if (computeData.byViewLeft  >= computeData.rootWidth){
                // ok
                if (computeData.byViewTop >= computeData.rootHeight / 2 - computeData.byViewHeight / 2) {
                    super.showAsDropDown(view,   - (computeData.rootWidth)  + xoff, -(computeData.byViewHeight / 2 + computeData.rootHeight / 2) + yoff);
                } else {
                    super.showAsDropDown(view,   - (computeData.rootWidth)  + xoff,  yoff);
                }
            } else {

                if (computeData.byViewTop >= computeData.rootHeight / 2 - computeData.byViewHeight / 2) {
                    super.showAsDropDown(view,    computeData.byViewWidth  + xoff, -(computeData.byViewHeight / 2 + computeData.rootHeight / 2) + yoff);
                } else {
                    super.showAsDropDown(view,    computeData.byViewWidth  + xoff,  yoff);
                }
            }
        });
    }

    public void showRight(View view, int xoff, int yoff) {
        computeShow(view, computeData -> {
            if (computeData.byViewLeft < computeData.rootWidth){
                // ok
                if (computeData.byViewTop >= computeData.rootHeight / 2 - computeData.byViewHeight / 2) {
                    super.showAsDropDown(view,    computeData.byViewWidth  + xoff, -(computeData.byViewHeight / 2 + computeData.rootHeight / 2) + yoff);
                } else {
                    super.showAsDropDown(view,    computeData.byViewWidth  + xoff,  yoff);
                }
            } else {
                if (computeData.byViewTop >= computeData.rootHeight / 2 - computeData.byViewHeight / 2) {
                    super.showAsDropDown(view,   - (computeData.rootWidth)  + xoff, -(computeData.byViewHeight / 2 + computeData.rootHeight / 2) + yoff);
                } else {
                    super.showAsDropDown(view,   - (computeData.rootWidth)  + xoff,  yoff);
                }
            }
        });
    }


    /**
     * 设置 可触摸取消
     */
    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
    }

    /**
     * 设置 可聚焦
     */
    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
    }

    /**
     * 设置透明
     * 0 透明
     */
    public void setTransparent(@IntRange(from = 0, to = 255) int alpha) {
        Drawable drawable = new ColorDrawable(context.getResources().getColor(android.R.color.black));
        drawable.setAlpha(alpha);
        setBackgroundDrawable(drawable);
    }


    // 全屏
    public void setIsWhole(boolean isWhole) {
        setHeight(isWhole ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(isWhole ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
