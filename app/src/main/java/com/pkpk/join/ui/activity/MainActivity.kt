package com.pkpk.join.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.pkpk.join.Application
import com.pkpk.join.R
import com.pkpk.join.api.SignApi
import com.pkpk.join.api.UserApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.UserLoginBean
import com.pkpk.join.bean.UserSignTable
import com.pkpk.join.databinding.ActivityMainBinding
import com.pkpk.join.service.WebSocketService
import com.pkpk.join.service.WebSocketType
import com.pkpk.join.ui.adapter.MainDrawerAdapter
import com.pkpk.join.ui.adapter.MainMsgAdapter
import com.pkpk.join.ui.dialog.MsgDialog
import com.pkpk.join.ui.widget.MRecyclerView
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.UserUtils
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.*
import com.pkpk.join.ui.dialog.MsgQueryDialog.Companion.showTop
import com.pkpk.join.ui.widget.LoadHeader
import java.io.Serializable


enum class UserGrade(
    val code: Int
) {
    OrdinaryUser(0), // 普通用户
    AdministratorUser(1), // 管理员
    RootUser(2) // ROOT管理员
}


@SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
class MainActivity : AppBaseActivity<ActivityMainBinding, Serializable>() {

    private lateinit var userInfo: UserLoginBean
    private var userApi = RetrofitManager.getInstance().create(UserApi::class.java)
    private var outTime = 0L
    private val mainMsgAdapter = MainMsgAdapter{
        startActivity(AdminUserStartActivity::class.java)
    }

    override fun setActivityWindowsInfo(isStatusBar: Boolean) {
        ImmersionBar.with(this)
            .statusBarDarkFont(isStatusBar)
            .fitsSystemWindows(false)
            .navigationBarDarkIcon(isStatusBar)
            .navigationBarColor(R.color.color_bg)
            .init()
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        initUi()
        initNavigationFragment()
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val loginToken = UserUtils.readLoginToken()
        if (loginToken.isNullOrEmpty()) {
            showToast(R.string.login_user_token_nul)
            UserUtils.loginTokenOut(this)
            return
        }
        UserUtils.loginTokenInit(null)
        if (!NetUtils.isNetworkConnected(context)) {
            showToast(R.string.dialog_load_title_net_error)
            UserUtils.loginTokenOut(this)
            return
        }
        userApi.sendUserInfo(loginToken)
            .mySubscribeMainThread(this, object : QuickRtObserverListener<BaseBean<UserLoginBean>> {
                override fun onSendError(t: BaseBean<UserLoginBean>?, e: Throwable) {
                    showToast(R.string.login_user_token_failure)
                    UserUtils.loginTokenOut(this@MainActivity)
                }

                override fun onSendComplete(t: BaseBean<UserLoginBean>) {
                    initUserInfoUi(t.result!!)
                }
            }, R.string.dialog_load_title)
    }

    override fun onEventBus(event: Serializable?, key: Int?, msg: String?) {
        LogsUtils.showLog(key)
        when(key){
            WebSocketType.WE_CLOSED.type or WebSocketType.WE_FINISH.type  -> {
                binding.mainContent.mainUserStateBg.background = getDrawable(R.drawable.dr_main_state_out)
                binding.mainContent.mainUserState.text = getString(R.string.main_top_root_user_offline)
            }
            WebSocketType.WE_OK.type -> {
                binding.mainContent.mainUserStateBg.background = getDrawable(R.drawable.dr_main_state_in)
                binding.mainContent.mainUserState.text = getString(R.string.main_top_root_user_online)
            }
            WebSocketType.WE_CLOSED.type -> {
                binding.mainContent.mainUserStateBg.background = getDrawable(R.drawable.dr_main_state_load)
                binding.mainContent.mainUserState.text = getString(R.string.main_top_root_user_connecting)
            }
        }

        if (key == WebSocketType.WE_OTHER_DEVICES.type || key == WebSocketType.WE_PWS_NUL.type) {
            val f =
                if (key == WebSocketType.WE_OTHER_DEVICES.type) getString(R.string.dialog_msg_out_login_q)
                else getString(R.string.dialog_msg_out_login_pws_nul)
            val msgDialog = MsgDialog(currentActivity(), f, {
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(false)
                UserUtils.loginTokenOut(currentActivity())
                true
            }, cancelStr = null)
            msgDialog.show()
            msgDialog.setCancelable(false)
            msgDialog.setCanceledOnTouchOutside(false)
            return
        }
        event ?: return
        if (key == WebSocketType.WE_MESSAGE_GOTO_SIGN.type) {
            if (event !is UserSignTable) return
            loadSignInfo()
            return
        }
        if (event !is UserLoginBean) return
        initUserInfoUi(event)
    }

    // 渲染 用户数据
    private fun initUserInfoUi(userLoginBean: UserLoginBean) {
        this.userInfo = userLoginBean
        Application.isLoginOk = true

        // 用户名
        binding.mainContent.mainUserNickname.text = userLoginBean.userNickname
        binding.mainDrawer.tvUserNickname.text = userLoginBean.userNickname

        // 用户头像
        GlideUtils.with(this)
            .loadHeaderToken(userLoginBean.userImg)
            .into(binding.mainDrawer.QMUIRadiusImageView)
        GlideUtils.with(this)
            .loadHeaderToken(userLoginBean.userImg)
            .into(binding.mainContent.ivBrandReturn)


        // 性别
        binding.mainDrawer.tvSex.text = if (userLoginBean.userSex)
            getString(R.string.drawer_sex_boy)
        else
            getString(R.string.drawer_sex_girl)

        // 性别图标
        binding.mainDrawer.tvSex.setCompoundDrawablesRelative(
            if (userLoginBean.userSex) {
                resources.getDrawable(R.drawable.ic_drawer_me_boy, null)
            } else {
                resources.getDrawable(R.drawable.ic_drawer_me_girl, null)
            }, null, null, null
        )

        // 用户等级
        binding.mainDrawer.tvAdmin.text = when (userLoginBean.userGrade) {
            UserGrade.RootUser.code -> getString(R.string.login_user_grade_2)
            UserGrade.AdministratorUser.code -> getString(R.string.login_user_grade_1)
            else -> getString(R.string.login_user_grade_0)
        }

        // 用户账号
        binding.mainDrawer.tvUserId.text =
            "${getString(R.string.login_user_id)}${userLoginBean.userAccount}"

        // 用户是否今天生日
        if (TimeUtils.isTimeBirthday(userLoginBean.userBirth,"yyyy-MM-dd")){
            (binding.mainContent.smartRefreshLayout.refreshHeader as LoadHeader).setAnimationBirthday(true)
            Application.setAnimationBirthday(true)
        } else {
            (binding.mainContent.smartRefreshLayout.refreshHeader as LoadHeader).setAnimationBirthday(false)
            Application.setAnimationBirthday(false)
        }

        //....
        // 启动 WebSocketService
        startService(Intent(this@MainActivity, WebSocketService::class.java))
        loadSignInfo()
    }


    private fun initUi() {
        //  binding.mainContent.recycler.setAdapter(TestAdapter(), 1)
        // 打卡菜单
        binding.mainContent.recycler.adapter = mainMsgAdapter

        binding.mainContent.view2.setOnClickListener {
            binding.root.`open`()
        }



        // 用户消息
        binding.mainContent.userMsg.setOnClickListener {
           startActivity(AdminMsgActivity::class.java)
        }

        // 扫码
        binding.mainContent.addGroupCode.setOnClickListener {
            QRCodeActivity.startActivity(this)
        }

        // 搜索
        binding.mainContent.addGroup.setOnClickListener {
            startActivity(SearchGroupActivity::class.java)
        }

        // 刷新
        binding.mainContent.smartRefreshLayout.setOnRefreshListener {
            loadSignInfo()
        }

        // 加载
        binding.mainContent.smartRefreshLayout.setOnLoadMoreListener {
            TimeUtils.timing(1000) {
                binding.mainContent.smartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }

        // 退出登录
        binding.mainDrawer.appCompatTextView7.setOnClickListener {
            MsgDialog(context, getString(R.string.dialog_msg_out_login), {
                userApi.outLogin().mySubscribeMainThread(this@MainActivity, object : QuickRtObserverListener<BaseBean<Boolean>> {
                    override fun onSendComplete(t: BaseBean<Boolean>) {
                        if (t.result != null && t.result!!){
                            UserUtils.loginTokenOut(currentActivity())
                        } else showToast(getString(R.string.user_out_err))
                    }

                    override fun onRetrofitError(t: BaseBean<Boolean>?, e: Throwable) = showToast(getString(R.string.user_out_err))
                })
                true
            }).show()
        }


        // 用户信息
        binding.mainDrawer.QMUIRadiusImageView.setOnClickListener {
            EditUserInfoActivity.startEditUserInfoActivity(this, userInfo)
        }

        binding.mainContent.mySign.setOnClickListener {
            startActivity(AdminUserStartActivity::class.java)
        }

        // 发起签到
        binding.mainContent.myStartSing.setOnClickListener {
            startActivity(StartSignActivity::class.java)
        }

        binding.mainContent.userInfo.setOnClickListener {
            EditUserInfoActivity.startEditUserInfoActivity(this, userInfo)
        }

        // 批阅签到
        binding.mainContent.mySignReview.setOnClickListener {
            startActivity(SignInfoListActivity::class.java)
        }

        // 更多设置
        binding.mainDrawer.appCompatTextView5.setOnClickListener {
            startActivity(SettingActivity::class.java)
        }

        // 切换主题
        binding.mainDrawer.appCompatTextView6.setOnClickListener {
            if (DarkModeUtils.isDarkMode(context)) {
                DarkModeUtils.applyDayMode(this)
                SharedPreferencesUtils.write("SystemMode",  2)
            } else {
                DarkModeUtils.applyNightMode(this)
                SharedPreferencesUtils.write("SystemMode",  1)
            }
        }
    }


    // 加载消息
    private fun loadSignInfo() {
        // binding.mainContent.root.setBackgroundColor(resources.getColor(R.color.color_bg_secondary))

        RetrofitManager.getInstance().create(SignApi::class.java).sendMySignAllInfo().mySubscribeMainThread(
            this,
            object : QuickRtObserverListener<BaseBean<ArrayList<UserSignTable>>> {

                override fun onSendError(t: BaseBean<ArrayList<UserSignTable>>?, e: Throwable) {
                    binding.mainContent.appNul.root.visibility = View.VISIBLE
                    binding.mainContent.recycler.visibility = View.GONE
                    binding.mainContent.rootColorBg.setBackgroundColor(resources.getColor(R.color.color_bg))
                    binding.mainContent.smartRefreshLayout.finishRefresh()
                    showToast(t?.reason ?: e.message)
                }

                override fun onSendComplete(t: BaseBean<ArrayList<UserSignTable>>) {
                    binding.mainContent.smartRefreshLayout.finishRefresh()
                    if (t.result.isNullOrEmpty()) {
                        binding.mainContent.appNul.root.visibility = View.VISIBLE
                        binding.mainContent.recycler.visibility = View.GONE
                        binding.mainContent.rootColorBg.setBackgroundColor(resources.getColor(R.color.color_bg))
                        return
                    }
                    binding.mainContent.rootColorBg.setBackgroundColor(resources.getColor(R.color.color_bg_secondary))
                    binding.mainContent.appNul.root.visibility = View.GONE
                    binding.mainContent.recycler.visibility = View.VISIBLE
                    mainMsgAdapter.refreshData(t.result)
                }
            },
            -1
        )
    }

    override fun onResume() {
        super.onResume()
        loadSignInfo()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - outTime <= 5000) {
                UserUtils.appOut(this)
                return super.onKeyDown(keyCode, event)
            }
            outTime = System.currentTimeMillis()
            showToast(getString(R.string.app_again_out))
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun initNavigationFragment() {
        val mainDrawerAdapter = MainDrawerAdapter(true)
        binding.mainDrawer.drawerRecycler.adapter = mainDrawerAdapter
        MRecyclerView.setAutoLayoutManager(context, binding.mainDrawer.drawerRecycler)
        binding.mainDrawer.outDr.setOnClickListener {
            binding.root.closeDrawers()
        }
    }
}