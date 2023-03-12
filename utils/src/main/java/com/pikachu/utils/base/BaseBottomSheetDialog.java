package com.pikachu.utils.base;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pikachu.utils.R;
import com.pikachu.utils.utils.ViewBindingUtils;

/**
 * 底部对话框
 * by ： pikachu
 *
 * @param <T>
 */

public abstract class BaseBottomSheetDialog<T extends ViewBinding> extends BottomSheetDialog {

    private final View root;
    public T binding;
    private boolean cancelable = false, // 是否可取消
            cancel = true, // 是否返回键可取消
            canSlide = true; // 是否可滑动

    /**
     * 视图创建时
     * 里面写视图事件
     *
     * @param binding 布局
     */
    protected abstract void onViewCreate(@NonNull T binding);


    public BaseBottomSheetDialog(@NonNull Context context) {
        super(context, R.style.BottomSheetDialog);
        binding = ViewBindingUtils.create(getClass(), LayoutInflater.from(context));
        root = binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onViewCreate(binding);
    }

    public void show() {
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancel);
        setContentView(root);
        View root = getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        assert root != null;
        BottomSheetBehavior.from(root).setHideable(canSlide);
        super.show();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (root == null) return;
        View parent = (View) root.getParent();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
        root.measure(0, 0);
        behavior.setPeekHeight(root.getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);
    }


    public boolean isCancelable() {
        return cancelable;
    }


    public void setCancelableS(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCanSlide() {
        return canSlide;
    }

    public void setCanSlide(boolean canSlide) {
        this.canSlide = canSlide;
    }

}

