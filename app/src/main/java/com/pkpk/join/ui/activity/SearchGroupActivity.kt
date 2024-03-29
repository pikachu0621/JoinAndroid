package com.pkpk.join.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.pkpk.join.R
import com.pkpk.join.api.GroupApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.GroupBean
import com.pkpk.join.databinding.ActivitySearchGroupBinding
import com.pkpk.join.ui.adapter.SearchGroupAdapter
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.UserUtils
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.OtherUtils
import com.pikachu.utils.utils.TimeUtils
import java.io.Serializable

class SearchGroupActivity : AppBaseActivity<ActivitySearchGroupBinding, Serializable>(),
    QuickRtObserverListener<BaseBean<ArrayList<GroupBean>>> {

    private val groupApi: GroupApi = RetrofitManager.getInstance().create(GroupApi::class.java)
    private var searchGroupAdapter: SearchGroupAdapter? = null

    override fun onAppCreate(savedInstanceState: Bundle?) {
        binding.imgDel1.setOnClickListener {
            QRCodeActivity.startActivity(this)
            finish()
        }

        binding.appSearch.setOnClickListener { gotoSearch() }

        initUi()
        TimeUtils.timing(300) {
            OtherUtils.showSoftInputFromWindow(binding.etUserNickname)
        }

        binding.etUserNickname.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                gotoSearch()
            }
            false
        })
    }

    private fun gotoSearch(loadDialogTitle: Int = R.string.dialog_load_title) {
        binding.smartRefreshLayout.finishRefresh()
        val group = binding.etUserNickname.text.toString()
        if (group.isEmpty()) {
            showToast(R.string.activity_search_nul)
            return
        }
        val decryptGroupId = UserUtils.decryptGroupId(group)
        val groupNameOrId = if ( decryptGroupId == -1L) {
            group
        } else {
            "$decryptGroupId"
        }
        groupApi.sendLikeGroupList(groupNameOrId).mySubscribeMainThread(this, this, loadDialogTitle)
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
        gotoSearch(-1)
    }

    override fun onSendError(t: BaseBean<ArrayList<GroupBean>>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        binding.appNul.root.visibility = View.VISIBLE
        showToast(t?.reason ?: e.message)
    }


    override fun onSendComplete(t: BaseBean<ArrayList<GroupBean>>) {
        binding.smartRefreshLayout.finishRefresh()
        if (t.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            return
        }
        binding.appNul.root.visibility = View.GONE
        searchGroupAdapter!!.refreshData(t.result)
    }

    override fun onEventBus(event: Serializable?, key: Int?, msg: String?) {
        loadUserGroup()
    }

}