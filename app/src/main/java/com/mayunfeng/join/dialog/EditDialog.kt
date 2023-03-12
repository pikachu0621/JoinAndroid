package com.mayunfeng.join.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.mayunfeng.join.R
import com.mayunfeng.join.databinding.DialogEditUserInfoBinding
import com.mayunfeng.join.databinding.DialogEditUserSexBinding
import com.pikachu.utils.base.BaseBottomSheetDialog
import com.pikachu.utils.utils.OtherUtils
import com.pikachu.utils.utils.ToastUtils

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.dialog
 * @Author:         pkpk.run
 * @Description:    null
 */
enum class EditUserInfoDialogType(
    val type: Int
) {
    DEF(1), HIGH(2), PASSWORD(3)
}


class EditInfoDialog(
    context: Context,
    private val title: String,
    private val hint: String,
    private val maxLength: Int,
    private val eType: EditUserInfoDialogType = EditUserInfoDialogType.DEF,
    private val clickOk: (dialog: EditInfoDialog, value1: String, value2: String) -> Boolean = { _, _, _-> false }
) : BaseBottomSheetDialog<DialogEditUserInfoBinding>(context) {

    override fun onViewCreate(binding: DialogEditUserInfoBinding) {
        binding.title.text = title
        binding.etUserName.hint = hint
        binding.etUserName.filters = arrayOf(InputFilter.LengthFilter(maxLength))

        binding.etUserName.addTextChangedListener {
            binding.ctvPws.isChecked = if (EditUserInfoDialogType.PASSWORD == eType) {
                !binding.etUserName.text.isNullOrEmpty()
            } else {
                (!binding.etUserName.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
            }
            binding.ctvPws.isClickable = binding.ctvPws.isChecked
            binding.imgDel1.visibility = if (!binding.etUserName.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        if (eType == EditUserInfoDialogType.HIGH) {

            binding.etUserName.maxLines = 4
            binding.etUserName.gravity = Gravity.TOP

        } else if (eType == EditUserInfoDialogType.PASSWORD) {

            binding.edVal2.visibility = View.VISIBLE
            binding.etUserPassword.addTextChangedListener {
                binding.ctvPws.isChecked =
                    (!binding.etUserName.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
                binding.ctvPws.isClickable = binding.ctvPws.isChecked
                binding.imgDel2.visibility = if (!binding.etUserPassword.text.isNullOrEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

        }

        binding.imgDel1.setOnClickListener {
            binding.etUserName.setText("")
        }
        binding.imgDel2.setOnClickListener {
            binding.etUserPassword.setText("")
        }

        binding.ctvPws.setOnClickListener {
            if(binding.etUserName.text.toString().isEmpty()) {
                ToastUtils.showToast(context.getString(R.string.dialog_edit_nul))
                return@setOnClickListener
            }
            if (clickOk(this,
                    binding.etUserName.text.toString(),
                    binding.etUserPassword.text.toString())) {
                dismiss()
            }
        }

    }


    override fun show() {
        super.show()
        OtherUtils.showSoftInputFromWindow(binding.etUserName)
    }
}






class EditSexDialog(
    context: Context,
    private var isBoy: Boolean,
    private val clickOk: (dialog: EditSexDialog, isBoy: Boolean) -> Boolean = {_,_-> false }
) : BaseBottomSheetDialog<DialogEditUserSexBinding>(context) {

    override fun onViewCreate(binding: DialogEditUserSexBinding) {

        val boy0 = getGreyImg(context, R.drawable.ic_edit_dialog_sex_boy, 0F)
        val boy1 = getGreyImg(context, R.drawable.ic_edit_dialog_sex_boy, 1F)
        val girl0 = getGreyImg(context, R.drawable.ic_edit_dialog_sex_girl, 0F)
        val girl1 = getGreyImg(context, R.drawable.ic_edit_dialog_sex_girl, 1F)

        if (isBoy) {
            binding.girl.setImageDrawable(girl0)
        } else {
            binding.boy.setImageDrawable(boy0)
        }

        binding.boy.setOnClickListener {
            binding.girl.setImageDrawable(girl0)
            binding.boy.setImageDrawable(boy1)
            isBoy = true
        }

        binding.girl.setOnClickListener {
            binding.boy.setImageDrawable(boy0)
            binding.girl.setImageDrawable(girl1)
            isBoy = false
        }

        binding.ok.setOnClickListener {
            if (clickOk(this, isBoy)) {
                dismiss()
            }
        }
    }


    //0-灰度，1-原色
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getGreyImg(context: Context, id: Int, sat: Float): Drawable {
        val dr = context.resources.getDrawable(id, null)
        val matrix = ColorMatrix().apply {
            setSaturation(sat)
        }
        dr.colorFilter = ColorMatrixColorFilter(matrix)
        return dr;
    }


}