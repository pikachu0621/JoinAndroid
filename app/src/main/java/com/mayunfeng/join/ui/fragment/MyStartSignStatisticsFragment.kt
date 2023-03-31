package com.mayunfeng.join.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.join.R
import com.mayunfeng.join.base.AppBaseFragment
import com.mayunfeng.join.databinding.FragmentMyStartSignStatisticsBinding
import com.pikachu.utils.type.JumpType


class MyStartSignStatisticsFragment : AppBaseFragment<FragmentMyStartSignStatisticsBinding, String>() {

    private var signId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            signId = it.getLong(JumpType.J0)
        }
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentMyStartSignStatisticsBinding,
        activity: FragmentActivity
    ) {

    }



    companion object {
        @JvmStatic
        fun newInstance(signId: Long) = MyStartSignStatisticsFragment().apply {
                arguments = Bundle().apply {
                    putLong(JumpType.J0, signId)
                }
            }
    }
}