package com.mayunfeng.join.dialog

import android.content.Context
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.databinding.DialogLoadBinding
import com.mayunfeng.join.databinding.DialogMsgBinding
import com.pikachu.utils.base.BaseDialog

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.dialog
 * @Author:         pkpk.run
 * @Description:    null
 */
class MsgDialog(
    context: Context,
    private val title: String = "",
    private val clickOk: (dialog: MsgDialog) -> Boolean,
    private val okStr: String = "确认",
    private val cancelStr: String = "取消",
    private val clickCancel: (dialog: MsgDialog) -> Boolean = { true }
): BaseDialog<DialogMsgBinding>(context)  {
    override fun onViewCreate(binding: DialogMsgBinding) {
        setWidthProportion(0.65F)
        binding.msg.text = title
        binding.ok.text = okStr
        binding.cancel.text = cancelStr
        binding.cancel.setOnClickListener {
            if (clickCancel(this)) {
                dismiss()
            }
        }
        binding.ok.setOnClickListener {
            if (clickOk(this)) {
                dismiss()
            }
        }
    }

}