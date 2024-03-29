package com.pkpk.join.ui.activity

import android.os.Bundle
import android.view.View
import com.pkpk.join.api.JoinGroupApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.GroupBean
import com.pkpk.join.databinding.ActivityMyJoinGroupBinding
import com.pkpk.join.ui.adapter.MyJoinGroupAdapter
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.TimeUtils
import java.io.Serializable

/**
 * 我加入的组
 */
class MyJoinGroupActivity : AppBaseActivity<ActivityMyJoinGroupBinding, Serializable>(),
    QuickRtObserverListener<BaseBean<ArrayList<GroupBean>>> {


    private val joinGroupApi: JoinGroupApi = RetrofitManager.getInstance().create(JoinGroupApi::class.java)
    private var myJoinGroupAdapter: MyJoinGroupAdapter? = null

    override fun onAppCreate(savedInstanceState: Bundle?) {
        initUi()
        loadUserGroup()
    }

    private fun loadUserGroup() {
        binding.smartRefreshLayout.autoRefresh()
        joinGroupApi.sendMyJoinGroup().mySubscribeMainThread(this, this, -1)
    }

    private fun initUi() {
        myJoinGroupAdapter = MyJoinGroupAdapter({
            // 跳转group 详情页
            startActivity(GroupInfoActivity::class.java, it.id)
        })
        binding.recycler.adapter = myJoinGroupAdapter

        binding.smartRefreshLayout.setOnRefreshListener {
            loadUserGroup()
        }

        // 加载
        binding.smartRefreshLayout.setOnLoadMoreListener {
            TimeUtils.timing(200) {
                // 完成刷新并标记没有更多数据
                binding.smartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }

    }


    override fun onSendError(t: BaseBean<ArrayList<GroupBean>>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        showToast(t?.reason ?: e.message)
    }


    override fun onSendComplete(t: BaseBean<ArrayList<GroupBean>>) {
        binding.smartRefreshLayout.finishRefresh()
        if (t.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            return
        }
        binding.appNul.root.visibility = View.GONE
        myJoinGroupAdapter!!.refreshData(t.result)
    }


    override fun onEventBus(event: Serializable?, key: Int?, msg: String?) {
        loadUserGroup()
    }

}