package com.mayunfeng.join.ui.dialog

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
import com.mayunfeng.join.R
import com.mayunfeng.join.databinding.DialogEditUserAgeBinding
import com.mayunfeng.join.databinding.DialogEditUserInfoBinding
import com.mayunfeng.join.databinding.DialogEditUserSexBinding
import com.pikachu.utils.base.BaseBottomSheetDialog
import com.pikachu.utils.utils.NetUtils
import com.pikachu.utils.utils.OtherUtils
import com.pikachu.utils.utils.ToastUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.dialog
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
        binding.etUserName.filters = arrayOf(InputFilter.LengthFilter(maxLength))
        binding.title.text = title
        binding.etUserName.hint = hint


        binding.etUserName.addTextChangedListener {
            binding.ctvPws.isChecked = if (EditUserInfoDialogType.PASSWORD == eType) {
                (!binding.etUserName.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
            } else {
                !binding.etUserName.text.isNullOrEmpty() && defaultData != binding.etUserName.text.toString()
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
            binding.etUserName.setLines(4)
            binding.etUserName.gravity = Gravity.TOP

        } else if (eType == EditUserInfoDialogType.PASSWORD) {
            binding.etUserName.setText("")
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
            if (!NetUtils.isNetworkConnected(context)) {
                ToastUtils.showToast(context.getString(R.string.dialog_load_title_net_error))
                return@setOnClickListener
            }
            if (clickOk(
                    this,
                    binding.etUserName.text.toString(),
                    binding.etUserPassword.text.toString()
                )
            ) {
                dismiss()
            }
        }
        binding.ctvPws.isChecked = false
        binding.ctvPws.isClickable = false
        binding.etUserName.setText(defaultData)
    }


    override fun show() {
        super.show()
        OtherUtils.showSoftInputFromWindow(binding.etUserName)
        binding.etUserName.setSelection(binding.etUserName.length())
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
                ToastUtils.showToast(context.getString(R.string.dialog_load_title_net_error))
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
        binding.dateWheelLayout.setDateLabel("年", "月", "日")
        selectedDate = setSelectedDate(birth)
        binding.dateWheelLayout.setOnDateSelectedListener { year, month, day ->
            binding.ok.isChecked = selectedDate.day != day || selectedDate.month != month || selectedDate.year != year
            binding.ok.isClickable = binding.ok.isChecked
        }

        binding.ok.setOnClickListener {
            val year = binding.dateWheelLayout.selectedYear
            val month = binding.dateWheelLayout.selectedMonth
            val day = binding.dateWheelLayout.selectedDay
            if (!NetUtils.isNetworkConnected(context)) {
                ToastUtils.showToast(context.getString(R.string.dialog_load_title_net_error))
                return@setOnClickListener
            }
            if (clickOk(this,
                    "$year-${
                        if(month<= 9) "0" else ""
                    }$month-${
                        if(day<= 9) "0" else ""
                    }$day")) {
                dismiss()
            }
        }
    }

   private fun setSelectedDate(date: String, pattern: String = "yyyy-MM-dd"): DateEntity {
        val simpleDateFormat = SimpleDateFormat(pattern,  Locale.SIMPLIFIED_CHINESE)
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