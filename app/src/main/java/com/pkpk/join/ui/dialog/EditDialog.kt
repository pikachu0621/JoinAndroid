package com.pkpk.join.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.pikachu.utils.base.BaseBottomSheetDialog
import com.pikachu.utils.base.BaseDialog
import com.pikachu.utils.base.BasePopupWindow
import com.pikachu.utils.utils.NetUtils
import com.pikachu.utils.utils.OtherUtils
import com.pikachu.utils.utils.ToastUtils
import com.pkpk.join.R
import com.pkpk.join.databinding.DialogEditUserAgeBinding
import com.pkpk.join.databinding.DialogEditUserInfoBinding
import com.pkpk.join.databinding.DialogEditUserSexBinding
import com.pkpk.join.databinding.DialogLoadBinding
import com.pkpk.join.databinding.DialogMsgBinding
import com.pkpk.join.databinding.DialogQueryMsgBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.dialog
 * @Author:         pkpk.run
 * @Description:    null
 */
enum class EditUserInfoDialogType(
    val type: Int,
) {
    DEF(1), HIGH(2), PASSWORD(3)
}


class EditInfoDialog(
    context: Context,
    private val title: String,
    private val hint: String,
    private val defaultData: String,
    private val maxLength: Int,
    private val eType: EditUserInfoDialogType = EditUserInfoDialogType.DEF,
    private val clickOk: (dialog: EditInfoDialog, value1: String, value2: String) -> Boolean = { _, _, _ -> false },
) : BaseBottomSheetDialog<DialogEditUserInfoBinding>(context) {

    override fun onViewCreate(binding: DialogEditUserInfoBinding) {
        binding.etUserNickname.filters = arrayOf(InputFilter.LengthFilter(maxLength))
        binding.title.text = title
        binding.etUserNickname.hint = hint


        binding.etUserNickname.addTextChangedListener {
            binding.ctvPws.isChecked = if (EditUserInfoDialogType.PASSWORD == eType) {
                (!binding.etUserNickname.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
            } else {
                !binding.etUserNickname.text.isNullOrEmpty() && defaultData != binding.etUserNickname.text.toString()
            }
            binding.ctvPws.isClickable = binding.ctvPws.isChecked
            binding.imgDel1.visibility = if (!binding.etUserNickname.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        if (eType == EditUserInfoDialogType.HIGH) {

            binding.etUserNickname.maxLines = 4
            binding.etUserNickname.setLines(4)
            binding.etUserNickname.gravity = Gravity.TOP

        } else if (eType == EditUserInfoDialogType.PASSWORD) {
            binding.etUserNickname.setText("")
            binding.edVal2.visibility = View.VISIBLE
            binding.etUserPassword.addTextChangedListener {
                binding.ctvPws.isChecked =
                    (!binding.etUserNickname.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
                binding.ctvPws.isClickable = binding.ctvPws.isChecked
                binding.imgDel2.visibility = if (!binding.etUserPassword.text.isNullOrEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

        }

        binding.imgDel1.setOnClickListener {
            binding.etUserNickname.setText("")
        }
        binding.imgDel2.setOnClickListener {
            binding.etUserPassword.setText("")
        }

        binding.ctvPws.setOnClickListener {
            if (!NetUtils.isNetworkConnected(context)) {
                ToastUtils.showToast(context.resources.getString(R.string.dialog_load_title_net_error))
                return@setOnClickListener
            }
            if (clickOk(
                    this,
                    binding.etUserNickname.text.toString(),
                    binding.etUserPassword.text.toString()
                )
            ) {
                dismiss()
            }
        }
        binding.ctvPws.isChecked = false
        binding.ctvPws.isClickable = false
        binding.etUserNickname.setText(defaultData)
    }


    override fun show() {
        super.show()
        OtherUtils.showSoftInputFromWindow(binding.etUserNickname)
        binding.etUserNickname.setSelection(binding.etUserNickname.length())
    }
}


class EditSexDialog(
    context: Context,
    private var isBoy: Boolean,
    private val clickOk: (dialog: EditSexDialog, isBoy: Boolean) -> Boolean = { _, _ -> false },
) : BaseBottomSheetDialog<DialogEditUserSexBinding>(context) {

    override fun onViewCreate(binding: DialogEditUserSexBinding) {
        val dfIsBoy = isBoy

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
            binding.ok.isChecked = isBoy != dfIsBoy
            binding.ok.isClickable = isBoy != dfIsBoy
        }

        binding.girl.setOnClickListener {
            binding.boy.setImageDrawable(boy0)
            binding.girl.setImageDrawable(girl1)
            isBoy = false
            binding.ok.isChecked = isBoy != dfIsBoy
            binding.ok.isClickable = isBoy != dfIsBoy
        }

        binding.ok.setOnClickListener {
            if (!NetUtils.isNetworkConnected(context)) {
                ToastUtils.showToast(context.resources.getString(R.string.dialog_load_title_net_error))
                return@setOnClickListener
            }
            if (clickOk(this, isBoy)) {
                dismiss()
            }
        }
        binding.ok.isChecked = false
        binding.ok.isClickable = false
    }


    //0-灰度，1-原色
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getGreyImg(context: Context, id: Int, sat: Float): Drawable {
        val dr = context.resources.getDrawable(id, null)
        val matrix = ColorMatrix().apply {
            setSaturation(sat)
        }
        dr.colorFilter = ColorMatrixColorFilter(matrix)
        return dr
    }


}


class EditAgeDialog(
    context: Context,
    private var birth: String,
    private val clickOk: (dialog: EditAgeDialog, isBoy: String) -> Boolean = { _, _ -> false },
) : BaseBottomSheetDialog<DialogEditUserAgeBinding>(context) {


    private val startData = DateEntity.target(1901, 2, 1)
    private val endData = DateEntity.today()
    private var selectedDate = DateEntity.today()

    override fun onViewCreate(binding: DialogEditUserAgeBinding) {
        val typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        val color = context.resources.getColor(R.color.color_grey1)
        binding.dateWheelLayout.yearLabelView.setTextColor(color)
        binding.dateWheelLayout.yearLabelView.typeface = typeface
        binding.dateWheelLayout.monthLabelView.setTextColor(color)
        binding.dateWheelLayout.monthLabelView.typeface = typeface
        binding.dateWheelLayout.dayLabelView.setTextColor(color)
        binding.dateWheelLayout.dayLabelView.typeface = typeface
        binding.dateWheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY)
        binding.dateWheelLayout.setDateLabel(
            context.resources.getString(R.string.app_years),
            context.resources.getString(R.string.app_month),
            context.resources.getString(R.string.app_days)
        )
        selectedDate = setSelectedDate(birth)
        binding.dateWheelLayout.setOnDateSelectedListener { year, month, day ->
            binding.ok.isChecked =
                selectedDate.day != day || selectedDate.month != month || selectedDate.year != year
            binding.ok.isClickable = binding.ok.isChecked
        }

        binding.ok.setOnClickListener {
            val year = binding.dateWheelLayout.selectedYear
            val month = binding.dateWheelLayout.selectedMonth
            val day = binding.dateWheelLayout.selectedDay
            if (!NetUtils.isNetworkConnected(context)) {
                ToastUtils.showToast(context.resources.getString(R.string.dialog_load_title_net_error))
                return@setOnClickListener
            }
            if (clickOk(
                    this,
                    "$year-${
                        if (month <= 9) "0" else ""
                    }$month-${
                        if (day <= 9) "0" else ""
                    }$day"
                )
            ) {
                dismiss()
            }
        }
    }

    private fun setSelectedDate(date: String, pattern: String = "yyyy-MM-dd"): DateEntity {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE)
        var parse: Date?
        try {
            parse = simpleDateFormat.parse(date)
            if (parse == null) parse = Date()
        } catch (e: Exception) {
            parse = Date()
            e.printStackTrace()
        }
        return DateEntity.target(parse)
    }


    override fun show() {
        super.show()
        binding.dateWheelLayout.setRange(this.startData, this.endData, this.selectedDate)
    }

}


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


class MsgDialog(
    context: Context,
    private val title: String = "",
    private val clickOk: (dialog: MsgDialog) -> Boolean,
    private val okStr: String = context.resources.getString(R.string.dialog_msg_confirm),
    private val cancelStr: String? = context.resources.getString(R.string.dialog_msg_cancel),
    private val clickCancel: (dialog: MsgDialog) -> Boolean = { true }
) : BaseDialog<DialogMsgBinding>(context) {
    override fun onViewCreate(binding: DialogMsgBinding) {
        setWidthProportion(0.65F)
        binding.msg.text = title
        binding.ok.text = okStr
        if (cancelStr.isNullOrEmpty()) {
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

class MsgQueryDialog(
    private val msg: String? = null,
    private val title: String? = null,
    context: Context) :
    BasePopupWindow<DialogQueryMsgBinding>(context) {
    override fun onViewCreate(binding: DialogQueryMsgBinding) {
        title?.let {
            binding.title.text = it
        } ?: run {
            binding.title.visibility = View.GONE
        }

        msg?.let {
            binding.msg.text = it
        } ?: run {
            binding.msg.visibility = View.GONE
        }
    }


    companion object {
        fun View.showTop(msg: String? = null, title: String? = null) {
            MsgQueryDialog(msg, title, this.context).showTop(this, 0, 0)
        }

        fun View.showBottom(msg: String? = null, title: String? = null) {
            MsgQueryDialog(title, msg, this.context).showBottom(this, 0, 0)
        }

        fun View.showLeft(msg: String? = null, title: String? = null) {
            MsgQueryDialog(title, msg, this.context).showLeft(this, 0, 0)
        }

        fun View.showRight( msg: String? = null, title: String? = null) {
            MsgQueryDialog(title, msg, this.context).showRight(this, 0, 0)
        }
    }
}



