package com.pkpk.join.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.pkpk.join.base.AppBaseFragment
import com.pkpk.join.databinding.FragmentStartSignTypeBinding
import com.pkpk.join.ui.activity.PwsGestureActivity
import com.pkpk.join.ui.activity.PwsNumActivity
import com.pkpk.join.ui.adapter.SignStartTypeAdapter
import com.pkpk.join.ui.adapter.SingStartType


class StartSignTypeFragment : AppBaseFragment<FragmentStartSignTypeBinding, String>() {


    private var type: SingStartType = SingStartType.PASSWORD_NOT
    private var pws: String? = ""

    private val adapter = SignStartTypeAdapter {
        type = it.type
        if (it.type == SingStartType.PASSWORD_GESTURE) {
            PwsGestureActivity.startActivity(requireActivity())
            return@SignStartTypeAdapter false
        }
        if (it.type == SingStartType.PASSWORD_NUM) {
            PwsNumActivity.startActivity(requireActivity())
            return@SignStartTypeAdapter false
        }
        return@SignStartTypeAdapter true
    }


    companion object {
        @JvmStatic
        fun newInstance() = StartSignTypeFragment()
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentStartSignTypeBinding,
        activity: FragmentActivity
    ) {
        binding.recycler.adapter = adapter

    }

    override fun onEventBus(event: String?, key: Int?, msg: String?) {
        if (event != null && key != 10) {
            pws = event
            adapter.setSingStartType(type)
        }
    }

    fun getSingStartType(): SingStartType  = type
    fun getPws(): String? = pws
}