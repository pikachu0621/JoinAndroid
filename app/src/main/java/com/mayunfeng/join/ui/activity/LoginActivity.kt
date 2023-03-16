package com.mayunfeng.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.mayunfeng.join.R
import com.mayunfeng.join.api.UserApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityLoginBinding
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.UserUtils
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.NetUtils

class LoginActivity : AppBaseActivity<ActivityLoginBinding, UserLoginBean>(), QuickRtObserverListener<BaseBean<UserLoginBean>> {



    companion object{
        fun startLoginActivity(activity: Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
            activity.overridePendingTransition(R.anim.aty_in, R.anim.aty_ont)
        }
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        click()
        val readUserAccount = UserUtils.readUserAccount()
        if (!readUserAccount.isNullOrEmpty()) {
            binding.etUserName.setText(readUserAccount)
        }
    }

    private fun click() {

        binding.appBack.setOnClickListener {
            finishTs()
        }

        binding.etUserName.addTextChangedListener {
            binding.ctvPws.isChecked =
                (!binding.etUserName.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
            binding.ctvPws.isClickable = binding.ctvPws.isChecked
            binding.imgDel1.visibility = if (!binding.etUserName.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding.etUserPassword.addTextChangedListener {
            binding.ctvPws.isChecked =
                (!binding.etUserName.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
            binding.ctvPws.isClickable = binding.ctvPws.isChecked
            binding.imgDel2.visibility = if (!binding.etUserPassword.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        binding.imgDel1.setOnClickListener {
            binding.etUserName.setText("")
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


    private fun finishTs(){
        finish()
        overridePendingTransition(R.anim.aty_ont, R.anim.aty_out)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishTs()
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
            .sendLogin(binding.etUserName.text.toString(), binding.etUserPassword.text.toString())
            .mySubscribeMainThread(this, this, R.string.dialog_load_title_login)
    }


    override fun onError(t: BaseBean<UserLoginBean>?, e: Throwable) {
        showToast(t?.reason ?: e.message)
    }

    override fun onComplete(t: BaseBean<UserLoginBean>) {
        val result = t.result!!
        UserUtils.writeUserAccount(result.userAccount)
        // todo 全局添加 Header
        UserUtils.loginTokenInit(result.loginToken!!)

        startActivity(MainActivity::class.java)
        finishTs()
    }





}