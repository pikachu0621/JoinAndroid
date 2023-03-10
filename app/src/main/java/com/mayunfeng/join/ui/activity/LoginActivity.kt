package com.mayunfeng.join.ui.activity

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.mayunfeng.join.HTTP_OK
import com.mayunfeng.join.R
import com.mayunfeng.join.api.UserApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityLoginBinding
import com.mayunfeng.join.dialog.LoadingDialog
import com.mayunfeng.join.utils.MyRetrofitObserver
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.mayunfeng.join.utils.retrofit.RetrofitObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginActivity : AppBaseActivity<ActivityLoginBinding>() {
    private lateinit var loadingDialog: LoadingDialog

    override fun onAppCreate(savedInstanceState: Bundle?) {
        loadingDialog = LoadingDialog(context, getString(R.string.dialog_load_title_login))
        click()
        val readUserAccount = MainActivity.readUserAccount()
        if (!readUserAccount.isNullOrEmpty()) {
            binding.etUserName.setText(readUserAccount)
        }
    }

    private fun click() {
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

    private fun loginUser() {

        RetrofitManager.getInstance()
            .create(UserApi::class.java)
            .login(binding.etUserName.text.toString(), binding.etUserPassword.text.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MyRetrofitObserver<BaseBean<UserLoginBean>>() {
                override fun onRetrofitSubscribe(d: Disposable) {
                    loadingDialog.show()
                }

                override fun onRetrofitComplete(t: BaseBean<UserLoginBean>) {
                    loadingDialog.dismiss()
                    startActivity(MainActivity::class.java)
                    finish()
                }

                override fun onRetrofitError(t: BaseBean<UserLoginBean>?, e: Throwable) {
                    loadingDialog.dismiss()
                    showToast(t?.reason ?: e.message)
                }

                override fun onRetrofitNext(t: BaseBean<UserLoginBean>) {
                    val result = t.result!!
                    MainActivity.writeLoginToken(result.loginToken)
                    MainActivity.writeUserAccount(result.userAccount)
                }
            })
    }


}