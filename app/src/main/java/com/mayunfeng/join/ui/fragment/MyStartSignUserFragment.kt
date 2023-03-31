package com.mayunfeng.join.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.join.R
import com.mayunfeng.join.base.AppBaseFragment
import com.mayunfeng.join.databinding.FragmentMyStartSignStatisticsBinding
import com.mayunfeng.join.databinding.FragmentMyStartSignUserBinding
import com.pikachu.utils.type.JumpType


class MyStartSignUserFragment : AppBaseFragment<FragmentMyStartSignUserBinding, String>() {

    private var signId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            signId = it.getLong(JumpType.J1)
        }
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentMyStartSignUserBinding,
        activity: FragmentActivity
    ) {




    }



    companion object {
        @JvmStatic
        fun newInstance(signId: Long) = MyStartSignUserFragment().apply {
            arguments = Bundle().apply {
                putLong(JumpType.J1, signId)
            }
        }
    }
}