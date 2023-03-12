package com.mayunfeng.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.mayunfeng.join.R
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityEditUserInfoBinding
import com.mayunfeng.join.dialog.EditInfoDialog
import com.mayunfeng.join.dialog.EditSexDialog
import com.mayunfeng.join.dialog.EditUserInfoDialogType
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
        initUserInfoUi(userLoginBean)
        initClick()
    }

    private fun initClick() {

        binding.userNameClick.setOnClickListener {
            EditInfoDialog(
                context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_name)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_name)}",
                10
            ) { _, _, _ ->
                true
            }.show()
        }

        binding.userSexClick.setOnClickListener {
            EditSexDialog(context, userLoginBean.userSex) { _, _ ->
                true
            }.show()
        }

        binding.userAgeClick.setOnClickListener {
            EditInfoDialog(context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_name)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_name)}",
                6
            ).show()
        }


        binding.userIntroduceClick.setOnClickListener {
            EditInfoDialog(context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_introduce)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_introduce)}",
                100,
                EditUserInfoDialogType.HIGH
            ).show()
        }


        binding.userUnitClick.setOnClickListener {
            EditInfoDialog(context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_unit)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_unit)}",
                30
            ).show()
        }


        binding.userPasswordClick.setOnClickListener {
            EditInfoDialog(context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_password)}",
                getString(R.string.dialog_edit_old_password),
                12,
                EditUserInfoDialogType.PASSWORD
            ).show()
        }

    }

    private fun initUserInfoUi(userLoginBean : UserLoginBean) {
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