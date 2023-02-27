package com.mayunfeng.adminuser.ui.activity

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.mayunfeng.adminuser.R
import com.mayunfeng.adminuser.adapter.MainDrawerAdapter
import com.mayunfeng.adminuser.adapter.TestAdapter
import com.mayunfeng.adminuser.base.AppBaseActivity
import com.mayunfeng.adminuser.databinding.ActivityMainBinding
import com.mayunfeng.adminuser.ui.widget.MRecyclerView

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
        initUi()
        initNavigationFragment()
    }

    private fun initUi() {
       //  binding.mainContent.recycler.setAdapter(TestAdapter(), 1)
        binding.mainContent.view2.setOnClickListener {
            binding.root.open()
        }
    }

    private fun initNavigationFragment() {

        binding.mainDrawer.drawerRecycler.adapter = MainDrawerAdapter(true)
        MRecyclerView.setAutoLayoutManager(context, binding.mainDrawer.drawerRecycler)
        binding.mainDrawer.outDr.setOnClickListener {
            binding.root.closeDrawers()
        }

    }
}