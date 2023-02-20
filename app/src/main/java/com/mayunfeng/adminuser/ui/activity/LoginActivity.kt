package com.mayunfeng.adminuser.ui.activity

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.mayunfeng.adminuser.R
import com.mayunfeng.adminuser.base.AppBaseActivity
import com.mayunfeng.adminuser.databinding.ActivityLoginBinding

class LoginActivity : AppBaseActivity<ActivityLoginBinding>() {


    override fun onAppCreate(savedInstanceState: Bundle?) {
        click()
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
            showToast("goto user")
        }

        binding.ctvPws.isChecked = false
        binding.ctvPws.isClickable = false
    }

}