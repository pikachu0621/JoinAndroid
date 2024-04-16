package com.pkpk.join.ui.activity

import android.os.Bundle
import android.view.View
import com.pkpk.join.api.SignApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.UserSignTable
import com.pkpk.join.databinding.ActivityAdminMsgBinding
import com.pkpk.join.ui.adapter.MainMsgAdapter
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.TimeUtils

class AdminMsgActivity : AppBaseActivity<ActivityAdminMsgBinding, String>(), QuickRtObserverListener<BaseBean<ArrayList<UserSignTable>>> {


    private val signApi = RetrofitManager.getInstance().create(SignApi::class.java)
    private val mainMsgAdapter = MainMsgAdapter{
        startActivity(AdminUserStartActivity::class.java)
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        binding.recycler.adapter = mainMsgAdapter
        binding.smartRefreshLayout.setOnRefreshListener {
            startLoad()
        }
        binding.smartRefreshLayout.setOnLoadMoreListener {
            TimeUtils.timing(200) {
                binding.smartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }
        startLoad()
    }

    private fun startLoad() {
        // binding.appNul.root.visibility = View.GONE
        signApi.sendMySignAllInfo().mySubscribeMainThread(this, this, -1)
    }

    override fun onSendError(t: BaseBean<ArrayList<UserSignTable>>?, e: Throwable) {
        binding.appNul.root.visibility = View.VISIBLE
        binding.smartRefreshLayout.finishRefresh()
        showToast(t?.reason ?: e.message)
    }

    override fun onSendComplete(t: BaseBean<ArrayList<UserSignTable>>) {
        binding.smartRefreshLayout.finishRefresh()
        if (t.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            return
        }
        binding.appNul.root.visibility = View.GONE
        mainMsgAdapter.refreshData(t.result)
    }

    override fun onResume() {
        super.onResume()
        startLoad()
    }
}