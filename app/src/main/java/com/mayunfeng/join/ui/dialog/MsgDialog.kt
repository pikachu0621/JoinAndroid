package com.mayunfeng.join.ui.dialog

import android.content.Context
import android.view.View
import com.mayunfeng.join.R
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
    private val okStr: String = context.resources.getString(R.string.dialog_msg_confirm),
    private val cancelStr: String? = context.resources.getString(R.string.dialog_msg_cancel),
    private val clickCancel: (dialog: MsgDialog) -> Boolean = { true }
): BaseDialog<DialogMsgBinding>(context)  {
    override fun onViewCreate(binding: DialogMsgBinding) {
        setWidthProportion(0.65F)
        binding.msg.text = title
        binding.ok.text = okStr
        if (cancelStr.isNullOrEmpty()){
            binding.cancel.visibility = View.GONE
            binding.fg.visibility = View.GONE
        } else {
            binding.cancel.visibility = View.VISIBLE
            binding.fg.visibility = View.VISIBLE
            binding.cancel.text = cancelStr
        }
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