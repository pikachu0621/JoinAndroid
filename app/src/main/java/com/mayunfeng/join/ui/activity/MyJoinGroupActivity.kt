package com.mayunfeng.join.ui.activity

import android.os.Bundle
import android.view.View
import com.mayunfeng.join.R
import com.mayunfeng.join.api.GroupApi
import com.mayunfeng.join.api.JoinGroupApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.databinding.ActivityMyCreateGroupBinding
import com.mayunfeng.join.databinding.ActivityMyJoinGroupBinding
import com.mayunfeng.join.databinding.DialogCreateGroupEditBinding
import com.mayunfeng.join.ui.adapter.MyCreateGroupAdapter
import com.mayunfeng.join.ui.adapter.MyJoinGroupAdapter
import com.mayunfeng.join.ui.dialog.MsgDialog
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.base.BaseBottomSheetDialog
import com.pikachu.utils.utils.TimeUtils
import com.pikachu.utils.utils.UiUtils
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