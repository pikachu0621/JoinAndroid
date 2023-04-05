package com.mayunfeng.join.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.join.R
import com.mayunfeng.join.api.JoinGroupApi
import com.mayunfeng.join.api.SignApi
import com.mayunfeng.join.base.AppBaseFragment
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.bean.StartSignBean
import com.mayunfeng.join.bean.UserSignBean
import com.mayunfeng.join.databinding.FragmentMyStartSignStatisticsBinding
import com.mayunfeng.join.service.WebSocketType
import com.mayunfeng.join.ui.activity.ActivityPwsPreview
import com.mayunfeng.join.ui.activity.GroupInfoActivity
import com.mayunfeng.join.ui.activity.UserInfoActivity
import com.mayunfeng.join.ui.dialog.MsgDialog
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.LogsUtils
import com.pikachu.utils.utils.OtherUtils
import com.pikachu.utils.utils.TimeUtils
import org.greenrobot.eventbus.EventBus


class MyStartSignStatisticsFragment :
    AppBaseFragment<FragmentMyStartSignStatisticsBinding, String>(),
    QuickRtObserverListener<BaseBean<UserSignBean>> {

    private var signId: Long? = null
    private var signExpire: Boolean = false
    private var isShowLoadAmd = true

    private val signApi: SignApi = RetrofitManager.getInstance().create(SignApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            signId = it.getLong(JumpType.J1)
            signExpire = it.getBoolean(JumpType.J2)
        }
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentMyStartSignStatisticsBinding,
        activity: FragmentActivity
    ) {
        binding.smartRefreshLayout.setOnRefreshListener { load(true) }
        binding.smartRefreshLayout.setOnLoadMoreListener {
            TimeUtils.timing(200) {
                binding.smartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }
        binding.del.setOnClickListener {
            MsgDialog(
                context,
                getString(R.string.start_sign_group_del),
                {
                    signApi.sendDelSign(signId!!)
                        .mySubscribeMainThread(requireActivity(),
                            object : QuickRtObserverListener<BaseBean<ArrayList<StartSignBean>>> {
                                override fun onSendError(
                                    t: BaseBean<ArrayList<StartSignBean>>?,
                                    e: Throwable
                                ) {
                                    showToast(t?.reason ?: e.message)
                                }
                                override fun onSendComplete(t: BaseBean<ArrayList<StartSignBean>>) {
                                    showToast("删除成功")
                                    requireActivity().finish()
                                }
                            })
                    true
                }).show()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initUi(userSignBean: UserSignBean) {

        val startSignTable = userSignBean.startSignTable
        val userTable = userSignBean.startSignTable.userTable
        val signGroupInfo = userSignBean.startSignTable.signGroupInfo


        binding.signName.text = startSignTable.signTitle
        binding.time.text = "发起时间：${
            TimeUtils.strToStr(
                startSignTable.createTime,
                "yyyy-MM-dd HH:mm:ss",
                "MM-dd HH:mm"
            )
        }"
        binding.content.text = startSignTable.signContent


        GlideUtils.with(context).loadHeaderToken(userTable.userImg).into(binding.sendUserImg)
        binding.userName.text = "发起者：${userTable.userName}"
        if (signGroupInfo == null) {
            binding.sendGroupImg.visibility = View.GONE
            binding.groupName.text = getString(R.string.start_sign_group_nul)
            binding.groupName.setTextColor(getColor(R.color.color_main_top6))
            binding.groupName.setOnClickListener {}
        } else {
            binding.sendGroupImg.visibility = View.VISIBLE
            GlideUtils.with(context).loadHeaderToken(signGroupInfo.groupImg)
                .into(binding.sendGroupImg)
            binding.groupName.text = "发起组：${signGroupInfo.groupName}"
            binding.groupName.setTextColor(getColor(R.color.color_grey1))
            binding.groupName.setOnClickListener {
                startActivity(GroupInfoActivity::class.java, signGroupInfo.id)
            }
        }
        // 0 无密码打卡   1 签到码打卡    2 二维码打卡     3 手势打卡
        binding.userSchool.visibility = View.VISIBLE
        binding.appCompatImageView6.visibility = View.VISIBLE
        binding.typeStart.setOnClickListener {
            ActivityPwsPreview.startActivity(
                requireActivity(),
                startSignTable.signKey,
                startSignTable.signType
            )
        }
        when (startSignTable.signType) {
            0 -> {
                binding.typeStart.setOnClickListener {}
                binding.userSchool.visibility = View.GONE
                binding.appCompatImageView6.visibility = View.GONE
                binding.appCompatImageView4.setImageResource(R.drawable.ic_start_sign_nul_psw)
            }
            1 -> binding.appCompatImageView4.setImageResource(R.drawable.ic_start_sign_num_psw)
            2 -> binding.appCompatImageView4.setImageResource(R.drawable.ic_start_sign_qrc)
            3 -> binding.appCompatImageView4.setImageResource(R.drawable.ic_start_sign_psw_gesture)
        }
        binding.timeAll.text = MyStartSignUserFragment.formatTime(startSignTable.signTime)
        binding.isEx.text = if (startSignTable.signExpire) {
            binding.isEx.setTextColor(getColor(R.color.color_main_top6))
            "已结束"
        } else {
            binding.isEx.setTextColor(getColor(R.color.color_main_top1))
            "进行中"
        }
        binding.appCompatTextView21.text = "${startSignTable.signNotCompletedPeople}"
        binding.appCompatTextView24.text = "${startSignTable.signHaveCompletedPeople}"
        val keepDecimal = OtherUtils.keepDecimal(
            (startSignTable.signHaveCompletedPeople.toFloat() / startSignTable.signAllPeople.toFloat() * 100F),
            2
        )
        binding.ratio.text = "${keepDecimal}%"

        binding.userName.setOnClickListener {
            UserInfoActivity.startUserInfoActivity(requireActivity(), userTable)
        }


    }

    private fun load(isShowLoadAmd: Boolean) {
        this.isShowLoadAmd = isShowLoadAmd
        if (isShowLoadAmd) {
            binding.smartRefreshLayout.autoRefresh()
        }
        signApi.sendAllUserInfo(signId!!).mySubscribeMainThread(requireActivity(), this, -1)
    }

    override fun onSendError(t: BaseBean<UserSignBean>?, e: Throwable) {
        binding.smartRefreshLayout.finishRefresh()
        showToast(t?.reason ?: e.message)
    }

    override fun onSendComplete(t: BaseBean<UserSignBean>) {
        binding.smartRefreshLayout.finishRefresh()
        initUi(t.result!!)
    }

    override fun onEventBus(event: String?, key: Int?, msg: String?) {
        if (key == 6 || key == WebSocketType.WE_MESSAGE.type){
            load(false)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(signId: Long, signExpire: Boolean) = MyStartSignStatisticsFragment().apply {
            arguments = Bundle().apply {
                putLong(JumpType.J1, signId)
                putBoolean(JumpType.J2, signExpire)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        load(false)
    }
}