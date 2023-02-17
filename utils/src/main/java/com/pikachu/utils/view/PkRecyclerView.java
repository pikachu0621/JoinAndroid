package com.pikachu.utils.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author pkpk.run
 * @project 通用工程
 * @package com.pikachu.utils.view
 * @date 2021/9/9
 * @description 
 * 
 * RecyclerView
 */
public class PkRecyclerView extends RecyclerView {
    public PkRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public PkRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PkRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);

    }







}
