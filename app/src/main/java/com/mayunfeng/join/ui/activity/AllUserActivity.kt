package com.mayunfeng.join.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mayunfeng.join.api.JoinGroupApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityAllUserBinding
import com.mayunfeng.join.ui.adapter.AllUserAdapter
import com.mayunfeng.join.ui.adapter.MyJoinGroupAdapter
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.TimeUtils
import java.io.Serializable

class AllUserActivity : AppBaseActivity<ActivityAllUserBinding, Serializable>(),
    QuickRtObserverListener<BaseBean<ArrayList<UserLoginBean>>> {


    private var groupId: Long? = 0
    private val joinGroupApi: JoinGroupApi = RetrofitManager.getInstance().create(JoinGroupApi::class.java)
    private val allUserAdapter: AllUserAdapter = AllUserAdapter({
        UserInfoActivity.startUserInfoActivity(this, it)
    }, {
        // todo 加移除用户
    })
    override fun onAppCreate(savedInstanceState: Bundle?) {
        groupId = longExtra
        if (groupId == 0L) finish()
        initUi()
        load()
    }

    private fun load() {
        binding.smartRefreshLayout.autoRefresh()
        joinGroupApi.queryJoinGroupAllUser(groupId!!).mySubscribeMainThread(this, this, -1)
    }


    private fun initUi() {
        binding.recycler.adapter = allUserAdapter
        binding.smartRefreshLayout.setOnRefreshListener {
            load()
        }

        // 加载
        binding.smartRefreshLayout.setOnLoadMoreListener {
            TimeUtils.timing(200) {
                // 完成刷新并标记没有更多数据
                binding.smartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }
    }

    override fun onError(t: BaseBean<ArrayList<UserLoginBean>>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        showToast(t?.reason ?: e.message)
    }


    override fun onComplete(t: BaseBean<ArrayList<UserLoginBean>>) {
        binding.smartRefreshLayout.finishRefresh()
        if (t.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            return
        }
        binding.appNul.root.visibility = View.GONE
        allUserAdapter.refreshData(t.result)
    }



}