package com.pkpk.join.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity
import com.pkpk.join.R
import com.pkpk.join.base.AppBaseFragment
import com.pkpk.join.databinding.FragmentStartSignInfoBinding


class StartSignInfoFragment : AppBaseFragment<FragmentStartSignInfoBinding, String>() {

    companion object {
        @JvmStatic
        fun newInstance() = StartSignInfoFragment()
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentStartSignInfoBinding,
        activity: FragmentActivity
    ) {
        val typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        val color = context.resources.getColor(R.color.color_grey1)
        binding.dateWheelLayout.hourLabelView.setTextColor(color)
        binding.dateWheelLayout.hourLabelView.typeface = typeface
        binding.dateWheelLayout.minuteLabelView.setTextColor(color)
        binding.dateWheelLayout.minuteLabelView.typeface = typeface
        binding.dateWheelLayout.setTimeLabel(getString(R.string.app_hour), getString(R.string.app_minutes) , "")
        binding.dateWheelLayout.setRange(
            TimeEntity.target(0, 2, 0),
            TimeEntity.target(24, 59, 0),
            TimeEntity.target(0,10,0))
    }

    override fun onEventBus(event: String?, key: Int?, msg: String?) {
        if (key == 10) {
            binding.groupName.setText(event)
        }
    }


    fun getTitle(): String = binding.groupName.text.toString()
    fun getContent(): String = binding.groupIntroduce.text.toString()
    fun getTime(): Long =  binding.dateWheelLayout.selectedHour * 60L * 60L + binding.dateWheelLayout.selectedMinute * 60L
}