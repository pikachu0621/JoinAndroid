package com.mayunfeng.adminuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.adminuser.base.AppBaseFragment
import com.mayunfeng.adminuser.databinding.FragmentAppBinding



class AppFragment : AppBaseFragment<FragmentAppBinding>() {



    companion object {
        @JvmStatic
        fun newInstance() = AppFragment()
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentAppBinding,
        activity: FragmentActivity
    ) {

    }

    override fun lazyLoad() {

    }
}