package com.pikachu.utils.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.viewbinding.ViewBinding;
import com.pikachu.utils.R;
import com.pikachu.utils.utils.ViewBindingUtils;

/**
 * author : pikachu
 * date   : 2021/7/30 14:58
 * version: 1.0
 *
 * 普通对话框
 */
public abstract class BaseDialog<T extends ViewBinding> extends Dialog {

    private final Context context;
    public final T binding;


    private float widthProportion = 0.8f;
    private float amount = 0.5f;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        this.context = context;
        binding = ViewBindingUtils.create(getClass(), LayoutInflater.from(context));
    }

    /**
     * 视图创建时
     * 里面写视图事件
     *
     * @param binding 布局
     */
    protected abstract void onViewCreate(T binding);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true); // 设置按返回键是否可关闭
        setCanceledOnTouchOutside(true); // 设置外部触摸可关闭
        setContentView(binding.getRoot());
        onViewCreate(binding);
    }


    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setBackgroundTransparent(amount);
        setWidthProportion(widthProportion);
    }

    /**
     * 设置 位置
     * @param x x偏移
     * @param y y偏移
     */
    public void setXY(int x, int y){
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.CENTER;
        wlp.x = x;
        wlp.y = y;
    }

    // 设置dialog的大背景背景色为透明
    public void setBackgroundTransparent(@FloatRange(from = 0, to = 1) float amount){
        getWindow().setDimAmount(amount);
        this.amount = amount;
    }

    public void setWidthProportion(@FloatRange(from = 0, to = 1) float widthProportion) {
        WindowManager manager =getWindow().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = (int)(outMetrics.widthPixels * widthProportion);
        getWindow().setLayout(width == 0 ? WindowManager.LayoutParams.WRAP_CONTENT: width, WindowManager.LayoutParams.WRAP_CONTENT);
        this.widthProportion = widthProportion;
    }



    public void setAnimations(@StyleRes int id){
        getWindow().setWindowAnimations(id);
    }

}
