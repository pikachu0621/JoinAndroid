package com.pkpk.join.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.pkpk.join.R
import com.pkpk.join.api.GroupApi
import com.pkpk.join.api.JoinGroupApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.GroupBean
import com.pkpk.join.databinding.ActivityGroupInfoBinding
import com.pkpk.join.ui.adapter.GroupInfoUserAdapter
import com.pkpk.join.ui.dialog.MsgDialog
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.UserUtils
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.GlideUtils
import java.io.Serializable

const val GroupInfoActivityEvt = 888
class GroupInfoActivity : AppBaseActivity<ActivityGroupInfoBinding, Serializable>(),
    QuickRtObserverListener<BaseBean<GroupBean>> {

    private val groupApi: GroupApi = RetrofitManager.getInstance().create(GroupApi::class.java)
    private val joinGroupApi: JoinGroupApi = RetrofitManager.getInstance().create(JoinGroupApi::class.java)

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



    @SuppressLint("SetTextI18n")
    private  fun initUi(group: GroupBean){
        groupBean = group
        GlideUtils.with(context).loadHeaderToken(group.groupImg).into(binding.groupImage)
        groupInfoUserAdapter.`data` = group.groupTopFourPeople
        binding.groupName.text = group.groupName
        binding.groupName2.text = group.groupName
        binding.groupId.text = UserUtils.encryptGroupId(group.id)
        binding.groupPeople.text = "${ group.groupPeople }${getString(R.string.group_info_people)}"
        binding.groupType.text = group.groupType
        binding.groupIntroduce.text = group.groupIntroduce

        binding.join.text = when(group.groupAndUser){
            0 -> {
                binding.join.setTextColor(resources.getColor(R.color.color_principal))
                binding.join.setOnClickListener {
                    joinGroupApi.sendJoinGroup(group.id)
                        .mySubscribeMainThread(this@GroupInfoActivity,
                            object : QuickRtObserverListener<BaseBean<GroupBean>> {

                                override fun onSendError(
                                    t: BaseBean<GroupBean>?,
                                    e: Throwable) = showToast(t?.reason ?: e.message)

                                override fun onSendComplete(t: BaseBean<GroupBean>) {
                                    postEventBus(null)
                                    showToast(getString(R.string.group_info_join_ok))
                                    initUi(t.result!!)
                                    // finish()
                                }
                            })
                }
                 getString(R.string.group_info_join_group)
            }
            1 -> {
                binding.join.setTextColor(resources.getColor(R.color.color_main_top6))
                binding.join.setOnClickListener {
                    MsgDialog(
                        context,
                        getString(R.string.my_create_group_dialog_dissolve_out_msg),
                        {
                            joinGroupApi.sendOutGroup(group.id)
                                .mySubscribeMainThread(this@GroupInfoActivity,
                                    object : QuickRtObserverListener<BaseBean<String>> {
                                        override fun onSendError(
                                            t: BaseBean<String>?,
                                            e: Throwable) = showToast(t?.reason ?: e.message)

                                        override fun onSendComplete(t: BaseBean<String>) {
                                            postEventBus(null)
                                            showToast(R.string.group_info_out_ok)
                                            finish()
                                        }
                                    })
                            true
                        }).show()
                }
                getString(R.string.group_info_out_group)
            }
            2 -> {
                binding.join.setTextColor(resources.getColor(R.color.color_main_top6))
                binding.join.setOnClickListener {
                    MsgDialog(
                        context,
                        getString(R.string.my_create_group_dialog_dissolve_msg),
                        {
                            groupApi.sendDeleteGroup(group.id)
                                .mySubscribeMainThread(this@GroupInfoActivity,
                                    object : QuickRtObserverListener<BaseBean<ArrayList<GroupBean>>> {
                                        override fun onSendComplete(t: BaseBean<ArrayList<GroupBean>>) {
                                            postEventBus(null)
                                            showToast(R.string.group_info_dissolve_ok)
                                            finish()
                                        }
                                    })
                            true
                        }).show()

                }
                getString(R.string.group_info_dissolve_group)
            }
            else -> getString(R.string.app_err)
        }

        binding.groupIdCopy.setOnClickListener {
            UserUtils.copyStr(context, binding.groupId.text.toString())
            showToast(R.string.group_info_copy_group_id)
        }
        binding.groupNameCopy.setOnClickListener {
            UserUtils.copyStr(context, binding.groupName.text.toString())
            showToast(R.string.group_info_copy_group_name)
        }
    }


    override fun onSendError(t: BaseBean<GroupBean>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        showToast(t?.reason ?: e.message)
        finish()
    }

    override fun onSendComplete(t: BaseBean<GroupBean>) {
        binding.smartRefreshLayout.finishRefresh()
        initUi(t.result!!)
    }

    override fun onEventBus(event: Serializable?, key: Int?, msg: String?) {
        if (key == GroupInfoActivityEvt) {
            loadData()
        }
    }




}
