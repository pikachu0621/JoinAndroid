package com.mayunfeng.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.mayunfeng.join.R
import com.mayunfeng.join.api.UserApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityEditUserInfoBinding
import com.mayunfeng.join.dialog.EditInfoDialog
import com.mayunfeng.join.dialog.EditSexDialog
import com.mayunfeng.join.dialog.EditUserInfoDialogType
import com.mayunfeng.join.dialog.LoadingDialog
import com.mayunfeng.join.utils.MyRetrofitObserver
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.UserUtils
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.mayunfeng.join.utils.retrofit.RetrofitObserver
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.NetUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File


class EditUserInfoActivity : AppBaseActivity<ActivityEditUserInfoBinding, UserLoginBean>(), QuickRtObserverListener<BaseBean<UserLoginBean>>,
    PhotoActivity.PhotoChooseListener {

    companion object {
        fun startEditUserInfoActivity(activity: Activity, userLoginBean: UserLoginBean) {
            activity.startActivity(Intent(activity, EditUserInfoActivity::class.java).apply {
                putExtra(JumpType.J0, userLoginBean)
            })
        }
    }

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var userLoginBean: UserLoginBean
    private val readLoginToken = UserUtils.readLoginToken()
    private val userApi = RetrofitManager.getInstance().create(UserApi::class.java)


    override fun onAppCreate(savedInstanceState: Bundle?) {
        userLoginBean = getSerializableExtra(JumpType.J0, UserLoginBean::class.java)
        loadingDialog = LoadingDialog(context, getString(R.string.dialog_load_title_modify))
        initClick()
        initUserInfoUi(userLoginBean)
    }


    private fun initClick() {

        binding.userImage.setOnClickListener {
            PhotoActivity.goPhotoImage(context, 1, 4, 1, this )
        }


        binding.userNameClick.setOnClickListener {
            EditInfoDialog(
                context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_name)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_name)}",
                userLoginBean.userName,
                10
            ) { _, v1, _ ->
                userApi.sendEditName(v1).mySubscribeMainThread(this, this)
                true
            }.show()
        }

        binding.userSexClick.setOnClickListener {
            EditSexDialog(context, userLoginBean.userSex) { _, isBoy ->
                userApi.sendEditSex(isBoy).mySubscribeMainThread(this, this)
                true
            }.show()
        }

        binding.userAgeClick.setOnClickListener {
            EditInfoDialog(
                context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_age)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_age)}",
                "${userLoginBean.userAge}",
                6
            ).show()
        }


        binding.userIntroduceClick.setOnClickListener {
            EditInfoDialog(
                context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_introduce)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_introduce)}",
                userLoginBean.userIntroduce,
                100,
                EditUserInfoDialogType.HIGH
            ) { _, v1, _ ->
                userApi.sendEditIrd(v1).mySubscribeMainThread(this, this)
                true
            }.show()
        }


        binding.userUnitClick.setOnClickListener {
            EditInfoDialog(
                context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_unit)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_unit)}",
                userLoginBean.userUnit,
                30
            ) { _, v1, _ ->
                userApi.sendEditUnit(v1).mySubscribeMainThread(this, this)
                true
            }.show()
        }


        binding.userPasswordClick.setOnClickListener {
            EditInfoDialog(
                context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_password)}",
                getString(R.string.dialog_edit_old_password),
                "",
                12,
                EditUserInfoDialogType.PASSWORD
            ) { _, v1, v2 ->
                userApi.sendEditPassword(v1, v2).mySubscribeMainThread(this, this)
                true
            }.show()
        }

    }


    private fun initUserInfoUi(userLoginBean: UserLoginBean) {
        this.userLoginBean = userLoginBean
        GlideUtils.with(this)
            .loadHeaderToken(userLoginBean.userImg)
            .into(binding.userImage)
        binding.userAccount.text = userLoginBean.userAccount
        binding.userName.text = userLoginBean.userName
        binding.userSex.text =
            if (userLoginBean.userSex) getString(R.string.drawer_sex_boy) else getString(R.string.drawer_sex_girl)
        binding.userAge.text = "${userLoginBean.userAge}"
        binding.userAccount.text = userLoginBean.userAccount
        binding.userSchool.text = userLoginBean.userUnit
        binding.userCreateTime.text = userLoginBean.createTime
    }



    override fun onStart(d: Disposable) {
        loadingDialog.show()
    }

    override fun onError(t: BaseBean<UserLoginBean>?, isHandled: Boolean, e: Throwable) {
        loadingDialog.dismiss()
        if (isHandled) return
        showToast(t?.reason ?: e.message)
    }

    override fun onComplete(t: BaseBean<UserLoginBean>) {
        postEventBus(t.result!!)
        loadingDialog.dismiss()
    }

    override fun onEventBus(event: UserLoginBean, key: Int?, msg: String?) {
        initUserInfoUi(event)
    }

    // 图片选择
    override fun onChooseClick(files: MutableList<String>?, num: Int) {


    }
}
