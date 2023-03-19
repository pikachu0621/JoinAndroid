package com.mayunfeng.join.ui.activity

import android.os.Bundle
import com.mayunfeng.join.R
import com.mayunfeng.join.api.GroupApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.databinding.ActivityGroupInfoBinding
import com.mayunfeng.join.ui.adapter.GroupInfoUserAdapter
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.GlideUtils

class GroupInfoActivity : AppBaseActivity<ActivityGroupInfoBinding, GroupBean>(),
    QuickRtObserverListener<BaseBean<GroupBean>> {

    private val groupApi: GroupApi = RetrofitManager.getInstance().create(GroupApi::class.java)
    private var groupBean: GroupBean? = null
    private val groupInfoUserAdapter: GroupInfoUserAdapter = GroupInfoUserAdapter({
        // 点击我的
        UserInfoActivity.startUserInfoActivity(this, it)
    }, {
        // 点击更多
        startActivity(AllUserActivity::class.java, groupBean?.id ?: 0)
    })

    override fun onAppCreate(savedInstanceState: Bundle?) {
        binding.userMsg.post { groupInfoUserAdapter.setItemWide(binding.userMsg.width) }
        binding.userMsg.adapter = groupInfoUserAdapter
        binding.smartRefreshLayout.setOnRefreshListener { loadData() }
        loadData()
    }

    private fun loadData(){
        binding.smartRefreshLayout.autoRefresh()
        groupApi.sendQueryGroupInfo(longExtra).mySubscribeMainThread(this, this, -1)
    }



    private  fun initUi(group: GroupBean){
        groupBean = group
        GlideUtils.with(context).loadHeaderToken(group.groupImg).into(binding.groupImage)
        groupInfoUserAdapter.`data` = group.groupTopFourPeople
        binding.groupName.text = group.groupName
        binding.groupName2.text = group.groupName
        binding.groupId.text = "${ group.id + 100000 }"
        binding.groupPeople.text = "${ group.groupPeople }人"
        binding.groupType.text = group.groupType
        binding.groupIntroduce.text = group.groupIntroduce
        binding.join.text = when(group.groupAndUser){
            0 -> {
                binding.join.setTextColor(resources.getColor(R.color.color_principal))
                 "加入该组"
            }
            1 -> {
                binding.join.setTextColor(resources.getColor(R.color.color_main_top6))
                "退出该组"
            }
            2 -> {
                binding.join.setTextColor(resources.getColor(R.color.color_main_top6))
                "解散该组"
            }
            else ->{ "出错" }
        }
    }


    override fun onError(t: BaseBean<GroupBean>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        showToast(t?.reason ?: e.message)
    }

    override fun onComplete(t: BaseBean<GroupBean>) {
        binding.smartRefreshLayout.finishRefresh()
        initUi(t.result!!)
    }


}
