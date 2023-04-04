package com.mayunfeng.join.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.join.R
import com.mayunfeng.join.api.SignApi
import com.mayunfeng.join.base.AppBaseFragment
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.StartSignBean
import com.mayunfeng.join.bean.UserSignBean
import com.mayunfeng.join.databinding.FragmentMyStartSignUserBinding
import com.mayunfeng.join.ui.activity.UserInfoActivity
import com.mayunfeng.join.ui.adapter.MyStartSignUserAdapter
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.LogsUtils
import com.pikachu.utils.utils.TimeUtils
import com.pikachu.utils.utils.UiUtils


class MyStartSignUserFragment : AppBaseFragment<FragmentMyStartSignUserBinding, String>(),
    QuickRtObserverListener<BaseBean<UserSignBean>> {

    private var signId: Long? = null
    private var signExpire: Boolean = false
    private var thread: Thread? = null
    private var isShowLoadAmd = true
    private val signApi: SignApi = RetrofitManager.getInstance().create(SignApi::class.java)
    private val myStartSignUserAdapter = MyStartSignUserAdapter {
        // 点击用户
        UserInfoActivity.startUserInfoActivity(requireActivity(), it.userTable)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            signId = it.getLong(JumpType.J1)
            signExpire = it.getBoolean(JumpType.J2)
        }
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentMyStartSignUserBinding,
        activity: FragmentActivity
    ) {
        initUi()
        // load(isShowLoadAmd)
    }

    private fun initTime(startSignTable: StartSignBean) {
        GlideUtils.with(context).loadHeaderToken(startSignTable.userTable.userImg)
            .into(binding.groupImage)

        if (startSignTable.signExpire) {
            binding.timeBar.setPrimaryColor(getColor(R.color.color_main_top6))
            binding.timeBar.setBackgroundColor(getColor(R.color.color_main_top6))
            binding.timeTxt.text = "已结束"
            return
        }

        if (startSignTable.signTime == -1L) {
            binding.timeBar.setPrimaryColor(getColor(R.color.color_principal))
            binding.timeBar.setBackgroundColor(getColor(R.color.color_grey4))
            binding.timeTxt.text = "不限时"
            return
        }

        binding.timeBar.setPrimaryColor(getColor(R.color.color_principal))
        binding.timeBar.setBackgroundColor(getColor(R.color.color_grey4))
        binding.timeBar.max = startSignTable.signTime.toInt()
        if (thread != null && thread!!.isAlive) thread!!.interrupt()
        thread = Thread {
            try {
                for (i in startSignTable.signTimeRemaining.toInt() downTo 0) {
                    UiUtils.runUi {
                        binding.timeBar.progress = i
                        binding.timeTxt.text = "剩余：${formatTime(i.toLong())}"
                    }
                    Thread.sleep(1000)
                }
                postEventBus(null, 6)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        thread!!.start()
    }


    private fun initUi() {
        binding.recycler.adapter = myStartSignUserAdapter
        binding.smartRefreshLayout.setOnRefreshListener { load(true) }
        binding.smartRefreshLayout.setOnLoadMoreListener {
            TimeUtils.timing(200) {
                binding.smartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }
    }


    private fun load(isShowLoadAmd: Boolean) {
        this.isShowLoadAmd = isShowLoadAmd
        if (isShowLoadAmd){
            binding.smartRefreshLayout.autoRefresh()
        }
        signApi.sendAllUserInfo(signId!!).mySubscribeMainThread(requireActivity(), this, -1)
    }


    companion object {
        @JvmStatic
        fun newInstance(signId: Long, signExpire: Boolean) = MyStartSignUserFragment().apply {
            arguments = Bundle().apply {
                putLong(JumpType.J1, signId)
                putBoolean(JumpType.J2, signExpire)
            }
        }

        /**
         *
         * @param time 秒
         * @return String
         */
        fun formatTime(seconds: Long): String {
            val h = seconds / 3600
            val min = seconds % 3600 / 60
            val second = seconds % 3600 % 60
            if (h <= 0L && min > 0) return "${min}分${second}秒"
            if (min <= 0L) return "${second}秒"
            return "${h}时${min}分${second}秒"
        }
    }


    override fun onSendError(t: BaseBean<UserSignBean>?, e: Throwable) {
        if (!isShowLoadAmd) return
        binding.smartRefreshLayout.finishRefresh()
        showToast(t?.reason ?: e.message)
    }

    override fun onSendComplete(t: BaseBean<UserSignBean>) {
        if (isShowLoadAmd) binding.smartRefreshLayout.finishRefresh()
        myStartSignUserAdapter.refreshData(t.result!!.userSignTable)
        myStartSignUserAdapter.setIsExpire(t.result!!.startSignTable.signExpire)
        initTime(t.result!!.startSignTable)
    }

    override fun onEventBus(event: String?, key: Int?, msg: String?) {
        if (key == 6){
            load(false)
        }
    }
    override fun onResume() {
        super.onResume()
        load(false)
    }

    override fun onDestroy() {
        thread?.interrupt()
        super.onDestroy()
    }

}