package com.pkpk.join.ui.activity

import android.os.Bundle
import android.view.View
import com.pkpk.join.api.SignApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.StartSignBean
import com.pkpk.join.databinding.ActivitySignInfoListBinding
import com.pkpk.join.ui.adapter.MyStartSignInfoAdapter
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.TimeUtils

class SignInfoListActivity : AppBaseActivity<ActivitySignInfoListBinding, String>(),
    QuickRtObserverListener<BaseBean<ArrayList<StartSignBean>>> {


    private val signApi: SignApi = RetrofitManager.getInstance().create(SignApi::class.java)
    private val myStartSignInfoAdapter = MyStartSignInfoAdapter({
        // 列表点击事件
        MyStartSignInfoActivity.startActivity(this@SignInfoListActivity, it.id, it.signExpire)
    })

    override fun onAppCreate(savedInstanceState: Bundle?) {
        binding.recycler.adapter = myStartSignInfoAdapter
        binding.smartRefreshLayout.setOnRefreshListener {
            startLoad()
        }
        // 加载
        binding.smartRefreshLayout.setOnLoadMoreListener {
            TimeUtils.timing(200) {
                // 完成刷新并标记没有更多数据
                binding.smartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }
    }


    private fun startLoad(){
        // binding.appNul.root.visibility = View.GONE
        signApi.sendAllInfo().mySubscribeMainThread(this, this, -1)
    }


    override fun onSendError(t: BaseBean<ArrayList<StartSignBean>>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        binding.appNul.root.visibility = View.VISIBLE
        showToast(t?.reason ?: e.message)
    }


    override fun onSendComplete(t: BaseBean<ArrayList<StartSignBean>>) {
        binding.smartRefreshLayout.finishRefresh()
        if (t.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            return
        }
        binding.appNul.root.visibility = View.GONE
        myStartSignInfoAdapter.refreshData(t.result)
    }

    override fun onResume() {
        super.onResume()
        startLoad()
    }

}