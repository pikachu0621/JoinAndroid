package com.mayunfeng.join.dialog

import android.content.Context
import android.view.View
import com.mayunfeng.join.databinding.DialogLoadBinding
import com.pikachu.utils.base.BaseDialog

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.dialog
 * @Author:         pkpk.run
 * @Description:    null
 */
class LoadingDialog(
    context: Context,
    private val title: String = ""
) : BaseDialog<DialogLoadBinding>(context) {
    override fun onViewCreate(binding: DialogLoadBinding) {
        setWidthProportion(0F)
        setCancelable(false) // 设置按返回键是否可关闭
        setCanceledOnTouchOutside(false) // 设置外部触摸可关闭
        if (title.isEmpty()) binding.loadTitle.visibility = View.GONE
        else binding.loadTitle.text = title
    }
}
