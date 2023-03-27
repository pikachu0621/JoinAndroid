package com.mayunfeng.join.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mayunfeng.join.R
import com.mayunfeng.join.api.GroupApi
import com.mayunfeng.join.api.JoinGroupApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.LGroupBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityAllUserBinding
import com.mayunfeng.join.ui.adapter.AllUserAdapter
import com.mayunfeng.join.ui.dialog.MsgDialog
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.TimeUtils
import java.io.Serializable


/**
 * 需要传 group id
 */
class AllUserActivity : AppBaseActivity<ActivityAllUserBinding, Serializable>(),
    QuickRtObserverListener<BaseBean<LGroupBean<ArrayList<UserLoginBean>>>> {


    private var groupId: Long? = 0
    private val joinGroupApi: JoinGroupApi = RetrofitManager.getInstance().create(JoinGroupApi::class.java)
    private val groupApi: GroupApi = RetrofitManager.getInstance().create(GroupApi::class.java)
    private var msgDialog: MsgDialog? = null
    private var isPostEvt = false

    private val allUserAdapter: AllUserAdapter = AllUserAdapter({
        UserInfoActivity.startUserInfoActivity(this, it)
    }, { user ->
        //  移除用户
        msgDialog = MsgDialog(context, getString(R.string.all_out_user), {
                groupApi.removeUserToGroup(user.id, groupId)
                    .mySubscribeMainThread(this@AllUserActivity,
                    this@AllUserActivity)
            isPostEvt = true
                false
            })
        msgDialog!!.show()
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

    override fun onSendError(t: BaseBean<LGroupBean<ArrayList<UserLoginBean>>>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        msgDialog?.dismiss()
        showToast(t?.reason ?: e.message)
    }


    override fun onSendComplete(t: BaseBean<LGroupBean<ArrayList<UserLoginBean>>>) {
        binding.smartRefreshLayout.finishRefresh()
        msgDialog?.dismiss()
        if (isPostEvt){
            postEventBus(null, GroupInfoActivityEvt)
            isPostEvt = false
        }
        if (t.result!!.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            return
        }
        binding.appNul.root.visibility = View.GONE
        allUserAdapter.setOpenEdit(t.result!!.groupUserIsFounder)
        allUserAdapter.refreshData(t.result!!.result)
    }



}