package com.mayunfeng.adminuser.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.ui.widget
 * @Author:         pkpk.run
 * @Description:    null
 */
class MRecyclerView : SmartRefreshLayout {

    private var recyclerView: RecyclerView

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        setEnableOverScrollDrag(true)
        setEnableOverScrollBounce(true)
        setEnableRefresh(false)
        setEnableLoadMore(false)
        recyclerView = RecyclerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            overScrollMode = 2 /*never*/
        }
        addView(recyclerView)
    }

    fun setAdapter(adapter: Adapter<out ViewHolder>) {
        recyclerView.adapter = adapter
    }

    fun setAdapter(
        adapter: Adapter<out ViewHolder>,
        item: Int = 1,
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
    ) {
        recyclerView.adapter = adapter
        setAutoLayoutManager(context, recyclerView, item, orientation)
    }

    fun setLayoutManager(layout: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layout
    }

    /**
     * @param item          一行有几个item
     * @param orientation   布局方向    [RecyclerView.VERTICAL 竖向]    [RecyclerView.HORIZONTAL 横向]  只在 item = 1 时生效
     */
    companion object {
        fun setAutoLayoutManager(
            context: Context,
            recyclerView: RecyclerView,
            item: Int = 1,
            @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
        ) {
            if (item == 1) {
                recyclerView.layoutManager = LinearLayoutManager(context).apply {
                    setOrientation(orientation)
                }
            } else {
                recyclerView.layoutManager = GridLayoutManager(context, item)
            }
        }
    }


}