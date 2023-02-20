package com.mayunfeng.adminuser.ui.activity

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.mayunfeng.adminuser.R
import com.mayunfeng.adminuser.base.AppBaseActivity
import com.mayunfeng.adminuser.databinding.ActivityMainBinding

class MainActivity : AppBaseActivity<ActivityMainBinding>() {


    override fun setActivityWindowsInfo(isStatusBar: Boolean) {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .fitsSystemWindows(false)
            .navigationBarDarkIcon(isStatusBar)
            .navigationBarColor(R.color.color_bg)
            .init()
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        initNavigationFragment()
    }

    private fun initNavigationFragment() {
        // binding.navView
    }
}