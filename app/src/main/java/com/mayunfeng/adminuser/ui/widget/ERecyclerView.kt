package com.mayunfeng.adminuser.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.R
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.ui.widget
 * @Author:         pkpk.run
 * @Description:    null
 */
class ERecyclerView : RecyclerView {


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.recyclerViewStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        


    }

}