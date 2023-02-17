package com.mayunfeng.adminuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.adminuser.ui.activity.LoginActivity
import com.mayunfeng.adminuser.base.AppBaseFragment
import com.mayunfeng.adminuser.databinding.FragmentMeBinding


class MeFragment : AppBaseFragment<FragmentMeBinding>() {



    companion object {
        @JvmStatic
        fun newInstance() = MeFragment()
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentMeBinding,
        activity: FragmentActivity
    ) {

        binding.root.setOnClickListener {
            startActivity(LoginActivity::class.java)
        }

    }

    override fun lazyLoad() {

    }
}