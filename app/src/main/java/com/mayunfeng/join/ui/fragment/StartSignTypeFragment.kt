package com.mayunfeng.join.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.join.base.AppBaseFragment
import com.mayunfeng.join.databinding.FragmentStartSignInfoBinding
import com.mayunfeng.join.databinding.FragmentStartSignTypeBinding
import com.mayunfeng.join.ui.activity.PwsGestureActivity
import com.mayunfeng.join.ui.activity.PwsNumActivity
import com.mayunfeng.join.ui.adapter.SignStartTypeAdapter
import com.mayunfeng.join.ui.adapter.SingStartType


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