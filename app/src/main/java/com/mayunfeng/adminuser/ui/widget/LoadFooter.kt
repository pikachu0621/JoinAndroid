package com.mayunfeng.adminuser.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.ViewAnimator
import com.mayunfeng.adminuser.R
import com.mayunfeng.adminuser.databinding.UiLoadFooterBinding
import com.pikachu.utils.utils.LogsUtils
import com.scwang.smart.refresh.classics.ClassicsAbstract
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.ui.widget
 * @Author:         pkpk.run
 * @Description:    null
 */
@SuppressLint("RestrictedApi")
class LoadFooter:  RelativeLayout, RefreshFooter {


    private var uiLoadLoadFooter: UiLoadFooterBinding


    private var mNoMoreData = false

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        uiLoadLoadFooter = UiLoadFooterBinding.inflate(LayoutInflater.from(context), this, false)
        addView(uiLoadLoadFooter.root)
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        if (!mNoMoreData) {
            when (newState) {
                RefreshState.None -> {
                    uiLoadLoadFooter.h0.cancelAnimation()
                    showText(R.string.load_footer_null)
                }
                RefreshState.PullUpToLoad -> {
                    uiLoadLoadFooter.h0.playAnimation()
                }
                else -> { }
            }
        }
    }

    override fun getView(): View = this

    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.Translate



    override fun setPrimaryColors(vararg colors: Int) {

    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {

    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }


    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int = 0

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean = false
    override fun setNoMoreData(noMoreData: Boolean): Boolean{
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            if (noMoreData) {
               // 没有更多数据
                showText(R.string.load_footer_null, false)
            }
        }
        return true
    }

    private fun showText(text: String, isAnimator: Boolean = true){
        if (isAnimator){
            uiLoadLoadFooter.h0.visibility = VISIBLE
            uiLoadLoadFooter.h1.visibility = GONE
            return
        }
        uiLoadLoadFooter.h0.visibility = GONE
        uiLoadLoadFooter.h1.visibility = VISIBLE
        uiLoadLoadFooter.h1.text = text
    }
    private fun showText(text: Int, isAnimator: Boolean = true){
        showText(resources.getString(text), isAnimator)
    }


}