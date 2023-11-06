package com.pkpk.join.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.pkpk.join.R
import com.pkpk.join.api.GroupApi
import com.pkpk.join.base.AppBaseFragment
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.GroupBean
import com.pkpk.join.databinding.FragmentStartSignMyCreateGroupBinding
import com.pkpk.join.ui.activity.CreateGroupActivity
import com.pkpk.join.ui.adapter.SignMyCreateGroupAdapter
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager


class StartSignMyCreateGroupFragment : AppBaseFragment<FragmentStartSignMyCreateGroupBinding, String>() ,
    QuickRtObserverListener<BaseBean<ArrayList<GroupBean>>> {



    private val groupApi: GroupApi = RetrofitManager.getInstance().create(GroupApi::class.java)
    private var signMyCreateGroupAdapter: SignMyCreateGroupAdapter = SignMyCreateGroupAdapter({
        postEventBus(it.groupName, 10)
    })

    companion object {
        @JvmStatic
        fun newInstance() = StartSignMyCreateGroupFragment()
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentStartSignMyCreateGroupBinding,
        activity: FragmentActivity
    ) {
        binding.recycler.adapter = signMyCreateGroupAdapter
    }

    override fun lazyLoad() {
        groupApi.sendUserCreateGroup().mySubscribeMainThread(requireActivity(), this, -1)
    }




    override fun onSendError(t: BaseBean<ArrayList<GroupBean>>?, e: Throwable) {
        binding.appNul.root.visibility = View.VISIBLE
        showToast(t?.reason ?: e.message)
    }


    override fun onSendComplete(t: BaseBean<ArrayList<GroupBean>>) {
        if (t.result.isNullOrEmpty()) {
            binding.appNul.root.visibility = View.VISIBLE
            requireActivity().finish()
            showToast(getString(R.string.start_sign_create))
            startActivity(CreateGroupActivity::class.java)
            return
        }
        binding.appNul.root.visibility = View.GONE
        signMyCreateGroupAdapter.refreshData(t.result)
    }

    fun getGroupId(): Long = signMyCreateGroupAdapter.getChooseGroupId()
}