package com.mayunfeng.adminuser.base

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.pikachu.utils.base.BaseActivity
import com.mayunfeng.adminuser.R

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.base
 * @Author:         pkpk.run
 * @Description:    null
 */
abstract class AppBaseActivity<T : ViewBinding> : BaseActivity<T>() {
    abstract fun onAppCreate(savedInstanceState: Bundle?)
    override fun initActivity(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .fitsSystemWindows(true)
            .navigationBarDarkIcon(resources.getBoolean(R.bool.isStatusBar))
            .navigationBarColor(R.color.color_bg)
            .statusBarColor(R.color.color_bg)
            .init()
        try {
            findViewById<View>(R.id.app_back)?.setOnClickListener {
                finish()
            }
        } catch (_: Exception){
        }
        onAppCreate(savedInstanceState)
    }
}