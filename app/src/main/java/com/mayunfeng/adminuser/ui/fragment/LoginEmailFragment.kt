package com.mayunfeng.adminuser.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import com.mayunfeng.adminuser.base.AppBaseFragment
import com.mayunfeng.adminuser.databinding.FragmentLoginEmailBinding


class LoginEmailFragment : AppBaseFragment<FragmentLoginEmailBinding>() {


    companion object {
        @JvmStatic
        fun newInstance() = LoginEmailFragment()
    }

    override fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: FragmentLoginEmailBinding,
        activity: FragmentActivity
    ) {
        click()

    }


    private fun click() {
        binding.etUserName.addTextChangedListener {
            binding.ctvPws.isChecked =
                (!binding.etUserName.text.isNullOrEmpty() && !binding.etUserPassword.text.isNullOrEmpty())
            binding.ctvPws.isClickable = binding.ctvPws.isChecked
            binding.tvGetvcode.isClickable = binding.ctvPws.isChecked
            binding.imgDel1.visibility = if (!binding.etUserName.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.tvGetvcode.alpha = if (!binding.etUserName.text.isNullOrEmpty()) {
                1F
            } else {
                0.4F
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
        binding.imgDel1.setOnClickListener { binding.etUserName.setText("") }
        binding.imgDel2.setOnClickListener { binding.etUserPassword.setText("") }

        // 获取验证码
        binding.tvGetvcode.setOnClickListener {
            showToast("get v_code")
        }

        // 登录
        binding.ctvPws.setOnClickListener {
            showToast("goto user")
        }



        binding.ctvPws.isClickable = false
        binding.tvGetvcode.isClickable = false

    }


    override fun lazyLoad() {

    }
}