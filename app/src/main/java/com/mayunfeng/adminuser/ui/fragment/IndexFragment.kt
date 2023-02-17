package com.mayunfeng.adminuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.adminuser.base.AppBaseFragment
import com.mayunfeng.adminuser.databinding.FragmentIndexBinding

class IndexFragment : AppBaseFragment<FragmentIndexBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = IndexFragment()
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentIndexBinding,
        activity: FragmentActivity
    ) {

    }

    override fun lazyLoad() {

    }
}