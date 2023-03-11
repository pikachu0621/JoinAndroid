package com.mayunfeng.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.mayunfeng.join.Application
import com.mayunfeng.join.BASE_URL
import com.mayunfeng.join.R
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityEditUserInfoBinding
import com.mayunfeng.join.utils.UserUtils
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils


class EditUserInfoActivity : AppBaseActivity<ActivityEditUserInfoBinding>() {

    companion object{
        fun startEditUserInfoActivity(activity: Activity, userLoginBean: UserLoginBean) {
            activity.startActivity(Intent(activity, EditUserInfoActivity::class.java).apply {
                putExtra(JumpType.J0, userLoginBean)
            })
        }
    }
    private lateinit var userLoginBean : UserLoginBean

    override fun onAppCreate(savedInstanceState: Bundle?) {
        userLoginBean = getSerializableExtra(JumpType.J0, UserLoginBean::class.java)
        initUserInfoUi()
        initClick()
    }

    private fun initClick() {

        binding.userSexClick.setOnClickListener {

            showToast("----------")
        }
    }

    private fun initUserInfoUi() {
        GlideUtils.with(this)
            .loadBaseUrl(userLoginBean.userImg)
            .into(binding.userImage)
        binding.userAccount.text = userLoginBean.userAccount
        binding.userName.text = userLoginBean.userName
        binding.userSex.text = if (userLoginBean.userSex) getString(R.string.drawer_sex_boy) else getString(R.string.drawer_sex_girl)
        binding.userAge.text = "${ userLoginBean.userAge }"
        binding.userAccount.text = userLoginBean.userAccount
        binding.userSchool.text = userLoginBean.userUnit
        binding.userCreateTime.text = userLoginBean.createTime
    }


}