package com.mayunfeng.adminuser.ui.activity

import android.content.Intent
import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.mayunfeng.adminuser.R
import com.mayunfeng.adminuser.adapter.MainDrawerAdapter
import com.mayunfeng.adminuser.adapter.MainMsgAdapter
import com.mayunfeng.adminuser.base.AppBaseActivity
import com.mayunfeng.adminuser.databinding.ActivityMainBinding
import com.mayunfeng.adminuser.mode.MainMsgData
import com.mayunfeng.adminuser.ui.widget.MRecyclerView
import com.pikachu.utils.adapter.BaseAdapter.OnItemClickListener
import com.pikachu.utils.utils.TimeUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

class MainActivity : AppBaseActivity<ActivityMainBinding>() {


    private var mainMsgAdapter: MainMsgAdapter? = null


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


        // 刷新
        binding.mainContent.smartRefreshLayout.setOnRefreshListener {
            TimeUtils.timing(200) {
                binding.mainContent.smartRefreshLayout.finishRefresh()
            }
        }

        // 加载
        binding.mainContent.smartRefreshLayout.setOnLoadMoreListener {
            TimeUtils.timing(1000) {
                // 完成刷新并标记没有更多数据
                binding.mainContent.smartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }


        // 加载msg数据
        infoMsgListUi(MainMsgData.getTestData())
    }


    // ui msg 初始化数据 or 更新数据
    private fun infoMsgListUi(`data`: MutableList<MainMsgData>) {
        if (mainMsgAdapter == null) {
            mainMsgAdapter = MainMsgAdapter(`data`)
            binding.mainContent.recycler.adapter = mainMsgAdapter
            return
        }
        mainMsgAdapter!!.refreshData(`data`)
    }

    // ui msg 初始化数据 or 更新数据
    private fun addMsgListUi(`data`: MutableList<MainMsgData>) {
        mainMsgAdapter?.addDataAll(`data`)
    }


    private fun initNavigationFragment() {
        val mainDrawerAdapter = MainDrawerAdapter(true)
        binding.mainDrawer.drawerRecycler.adapter = mainDrawerAdapter
        MRecyclerView.setAutoLayoutManager(context, binding.mainDrawer.drawerRecycler)
        binding.mainDrawer.outDr.setOnClickListener {
            binding.root.closeDrawers()
        }
    }
}