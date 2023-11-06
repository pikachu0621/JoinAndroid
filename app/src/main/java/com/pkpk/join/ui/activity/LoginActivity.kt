package com.pkpk.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.pkpk.join.R
import com.pkpk.join.api.UserApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.UserLoginBean
import com.pkpk.join.databinding.ActivityLoginBinding
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.UserUtils
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.NetUtils

class LoginActivity : AppBaseActivity<ActivityLoginBinding, UserLoginBean>(), QuickRtObserverListener<BaseBean<UserLoginBean>> {



    companion object{
        fun startLoginActivity(activity: Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
            activity.overridePendingTransition(R.anim.aty_in, R.anim.aty_ont)
        }


        fun finishTs(activity: Activity){
            activity.finish()
            activity.overridePendingTransition(R.anim.aty_ont, R.anim.aty_out)
        }
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        click()
        val readUserAccount = UserUtils.readUserAccount()
        if (!readUserAccount.isNullOrEmpty()) {
            binding.etUserNickname.setText(readUserAccount)
        }
    }

    private fun click() {

        binding.appBack.setOnClickListener {
            finishTs(this)
        }

        binding.etUserNickname.addTextChangedListener {
            binding.ctvPws.isChecked =
                (!binding.etUserNickname.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
            binding.ctvPws.isClickable = binding.ctvPws.isChecked
            binding.imgDel1.visibility = if (!binding.etUserNickname.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding.etUserPassword.addTextChangedListener {
            binding.ctvPws.isChecked =
                (!binding.etUserNickname.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
            binding.ctvPws.isClickable = binding.ctvPws.isChecked
            binding.imgDel2.visibility = if (!binding.etUserPassword.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        binding.imgDel1.setOnClickListener {
            binding.etUserNickname.setText("")
        }
        binding.imgDel2.setOnClickListener {
            binding.etUserPassword.setText("")
        }
        binding.imgShow.setOnClickListener {
            if (it.tag == "true") {
                it.tag = "false"
                binding.etUserPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.imgShow.setImageResource(R.drawable.ic_login_edit_hide)
            } else {
                it.tag = "true"
                binding.etUserPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                binding.imgShow.setImageResource(R.drawable.ic_login_edit_show)
            }
            binding.etUserPassword.setSelection(binding.etUserPassword.length())
        }
        // 登录
        binding.ctvPws.setOnClickListener {
            loginUser()
        }
        binding.ctvPws.isChecked = false
        binding.ctvPws.isClickable = false
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishTs(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }



    private fun loginUser() {
        if (!NetUtils.isNetworkConnected(context)) {
            showToast(R.string.dialog_load_title_net_error)
            return
        }
        RetrofitManager.getInstance()
            .create(UserApi::class.java)
            .sendLogin(binding.etUserNickname.text.toString(), binding.etUserPassword.text.toString())
            .mySubscribeMainThread(this, this, R.string.dialog_load_title_login)
    }


    override fun onSendError(t: BaseBean<UserLoginBean>?, e: Throwable) {
        showToast(t?.reason ?: e.message)
    }

    override fun onSendComplete(t: BaseBean<UserLoginBean>) {
        val result = t.result!!
        UserUtils.writeUserAccount(result.userAccount)
        // todo 全局添加 Header
        UserUtils.loginTokenInit(result.loginToken!!)

        startActivity(MainActivity::class.java)
        finishTs(this)
    }





}