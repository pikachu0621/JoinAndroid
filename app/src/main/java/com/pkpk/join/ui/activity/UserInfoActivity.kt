package com.pkpk.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pkpk.join.R
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.UserLoginBean
import com.pkpk.join.databinding.ActivityUserInfoBinding
import com.pkpk.join.ui.dialog.*
import com.pkpk.join.utils.UserUtils
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils

/**
 * 用户数据
 */
class UserInfoActivity : AppBaseActivity<ActivityUserInfoBinding, UserLoginBean>(){

    companion object {
        fun startUserInfoActivity(activity: Activity, userLoginBean: UserLoginBean) {
            activity.startActivity(Intent(activity, UserInfoActivity::class.java).apply {
                putExtra(JumpType.J0, userLoginBean)
            })
        }
    }

    private lateinit var userLoginBean: UserLoginBean

    override fun onAppCreate(savedInstanceState: Bundle?) {
        userLoginBean = getSerializableExtra(JumpType.J0, UserLoginBean::class.java)
        initUserInfoUi(userLoginBean)
    }

    private fun initUserInfoUi(userLoginBean: UserLoginBean) {
        this.userLoginBean = userLoginBean
        GlideUtils.with(this)
            .loadHeaderToken(userLoginBean.userImg)
            .into(binding.userImage)
        binding.userAccount.text = userLoginBean.userAccount
        binding.userNickname.text = userLoginBean.userNickname
        binding.userOpenVsd.visibility = View.VISIBLE
        binding.userSexClick.visibility = View.GONE
        binding.userAgeClick.visibility = View.GONE
        binding.userUnitClick.visibility = View.GONE
        binding.userIntroduceLayout.visibility = View.GONE
        if (userLoginBean.userAccount == UserUtils.readUserAccount()
            || !userLoginBean.userOpenProfile){
            binding.userSex.text = if (userLoginBean.userSex) getString(R.string.drawer_sex_boy) else getString(R.string.drawer_sex_girl)
            binding.userAge.text = "${userLoginBean.userAge}"
            binding.userSchool.text = userLoginBean.userUnit
            binding.userIntroduce.text = userLoginBean.userIntroduce

            binding.userOpenVsd.visibility = View.GONE
            binding.userSexClick.visibility = View.VISIBLE
            binding.userAgeClick.visibility = View.VISIBLE
            binding.userUnitClick.visibility = View.VISIBLE
            binding.userIntroduceLayout.visibility = View.VISIBLE
        }
    }

}
