package com.pkpk.join.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.pkpk.join.databinding.UiLoadHeaderBinding
import com.scwang.smart.refresh.footer.classics.R
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.ui.widget
 * @Author:         pkpk.run
 * @Description:    null
 */
@SuppressLint("RestrictedApi")
class LoadHeader: RelativeLayout, RefreshHeader {


    private var uiLoadHeaderBinding: UiLoadHeaderBinding

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        uiLoadHeaderBinding = UiLoadHeaderBinding.inflate(LayoutInflater.from(context), this, false)
        addView(uiLoadHeaderBinding.root)
    }

    fun setAnimationBirthday(isBirthday: Boolean){
        if (isBirthday){
            uiLoadHeaderBinding.h0.setAnimation("animation/animation_load_header_birthday.json")
        } else {
            uiLoadHeaderBinding.h0.setAnimation("animation/animation_load_header_1.json")
        }
    }


    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.None -> {
                uiLoadHeaderBinding.h0.cancelAnimation()
                uiLoadHeaderBinding.h1.visibility = GONE
                uiLoadHeaderBinding.h0.visibility = VISIBLE
            }
            RefreshState.PullDownToRefresh -> {

            }
            RefreshState.Refreshing -> {

            }
            RefreshState.ReleaseToRefresh -> {

            }
            else -> {

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
        if(isDragging && percent <= 1){
            uiLoadHeaderBinding.h0.progress = percent
            R.layout.srl_classics_footer
        }
    }


    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        uiLoadHeaderBinding.h0.playAnimation()
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        uiLoadHeaderBinding.h0.visibility = GONE
        uiLoadHeaderBinding.h1.visibility = VISIBLE
        return 1000
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean = false
}