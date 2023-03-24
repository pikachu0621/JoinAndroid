package com.mayunfeng.join.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mayunfeng.join.GROUP_ID_LENGTH
import com.mayunfeng.join.api.GroupApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.databinding.ActivitySearchGroupBinding
import com.mayunfeng.join.ui.adapter.MyJoinGroupAdapter
import com.mayunfeng.join.ui.adapter.SearchGroupAdapter
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.TimeUtils
import java.io.Serializable

class SearchGroupActivity : AppBaseActivity<ActivitySearchGroupBinding, Serializable>(),
    QuickRtObserverListener< BaseBean<ArrayList<GroupBean>>> {

    private val groupApi: GroupApi = RetrofitManager.getInstance().create(GroupApi::class.java)
    private var searchGroupAdapter: SearchGroupAdapter? = null

    override fun onAppCreate(savedInstanceState: Bundle?) {
        binding.imgDel1.setOnClickListener {
            startActivity(QRCodeActivity::class.java)
            finish()
        }

        binding.appSearch.setOnClickListener {
            val group = binding.etUserName.text.toString()
            if (group.isEmpty()) {
                showToast("不能为空哦！")
                return@setOnClickListener
            }
            val groupNameOrId = try {
                val toLong = group.toLong()
                if ((toLong - GROUP_ID_LENGTH <= 0)) "$toLong" else "${toLong - GROUP_ID_LENGTH}"
            } catch (e: Exception) {
                group
            }
            groupApi.sendLikeGroupList(groupNameOrId).mySubscribeMainThread(this, this)
        }
        initUi()
    }

    private fun initUi() {
        searchGroupAdapter = SearchGroupAdapter({
            // 跳转group 详情页
            startActivity(GroupInfoActivity::class.java, it.id)
        })
        binding.recycler.adapter = searchGroupAdapter

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

    private fun loadUserGroup() {
        binding.smartRefreshLayout.autoRefresh()
        val group = binding.etUserName.text.toString()
        if (group.isEmpty()) return
        val groupNameOrId = try {
            val toLong = group.toLong()
            if ((toLong - GROUP_ID_LENGTH <= 0)) "$toLong" else "${toLong - GROUP_ID_LENGTH}"
        } catch (e: Exception) {
            group
        }
        groupApi.sendLikeGroupList(groupNameOrId).mySubscribeMainThread(this, this)
    }

    override fun onError(t: BaseBean<ArrayList<GroupBean>>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        binding.appNul.root.visibility = View.VISIBLE
        showToast(t?.reason ?: e.message)
    }


    override fun onComplete(t: BaseBean<ArrayList<GroupBean>>) {
        binding.smartRefreshLayout.finishRefresh()
        if (t.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            return
        }
        binding.appNul.root.visibility = View.GONE
        searchGroupAdapter!!.refreshData(t.result)
    }

}