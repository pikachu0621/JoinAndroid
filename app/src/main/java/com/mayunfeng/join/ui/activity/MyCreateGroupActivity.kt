package com.mayunfeng.join.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mayunfeng.join.R
import com.mayunfeng.join.api.GroupApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.databinding.ActivityMyCreateGroupBinding
import com.mayunfeng.join.databinding.DialogCreateGroupEditBinding
import com.mayunfeng.join.ui.adapter.MyCreateGroupAdapter
import com.mayunfeng.join.ui.dialog.MsgDialog
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.UserUtils
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.base.BaseBottomSheetDialog
import com.pikachu.utils.base.BasePopupWindow
import com.pikachu.utils.utils.TimeUtils
import com.pikachu.utils.utils.UiUtils
import java.io.Serializable

/**
 * 我创建的组
 */
class MyCreateGroupActivity : AppBaseActivity<ActivityMyCreateGroupBinding, Serializable>(),
    QuickRtObserverListener<BaseBean<ArrayList<GroupBean>>> {


    private val groupApi: GroupApi = RetrofitManager.getInstance().create(GroupApi::class.java)
    private var myCreateGroupAdapter: MyCreateGroupAdapter? = null

    override fun onAppCreate(savedInstanceState: Bundle?) {
        initUi()
        loadUserGroup()
    }

    private fun loadUserGroup() {
        binding.smartRefreshLayout.autoRefresh()
        groupApi.sendUserCreateGroup().mySubscribeMainThread(this, this, -1)
    }

    private fun initUi() {
        myCreateGroupAdapter = MyCreateGroupAdapter({
            // 跳转group 详情页
            startActivity(GroupInfoActivity::class.java, it.id)
        }, { group ->
            object : BaseBottomSheetDialog<DialogCreateGroupEditBinding>(context) {
                override fun onViewCreate(binding: DialogCreateGroupEditBinding) {
                    binding.edit.setOnClickListener {
                        CreateGroupActivity.startCreateGroupActivity(context, group)
                        dismiss()
                    }
                    binding.dissolve.setOnClickListener {
                        MsgDialog(
                            context,
                            getString(R.string.my_create_group_dialog_dissolve_msg),
                            {
                                groupApi.sendDeleteGroup(group.id).mySubscribeMainThread(
                                    this@MyCreateGroupActivity,
                                    this@MyCreateGroupActivity
                                )
                                dismiss()
                                true
                            }).show()
                    }
                    // binding.send
                }
            }.show()
        })
        binding.recycler.adapter = myCreateGroupAdapter

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


    override fun onError(t: BaseBean<ArrayList<GroupBean>>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        showToast(t?.reason ?: e.message)
    }


    override fun onComplete(t: BaseBean<ArrayList<GroupBean>>) {
        binding.smartRefreshLayout.finishRefresh()
        if (t.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            return
        }
        binding.appNul.root.visibility = View.GONE
        myCreateGroupAdapter!!.refreshData(t.result)
    }


    override fun onEventBus(event: Serializable, key: Int?, msg: String?) {
        loadUserGroup()
    }

}