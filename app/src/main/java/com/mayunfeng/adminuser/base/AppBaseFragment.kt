package com.mayunfeng.adminuser.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.pikachu.utils.base.BaseFragment

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.base
 * @Author:         pkpk.run
 * @Description:    null
 */
abstract class AppBaseFragment<T : ViewBinding> : BaseFragment<T>() {
    abstract fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: T,
        activity: FragmentActivity
    )

    override fun onInitView(savedInstanceState: Bundle?, binding: T, activity: FragmentActivity) {
        onAppCreateView(savedInstanceState, binding, activity)
    }

}