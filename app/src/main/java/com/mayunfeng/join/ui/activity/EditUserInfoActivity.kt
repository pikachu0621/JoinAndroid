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
import com.mayunfeng.join.ui.dialog.*
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.UserUtils
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * 编辑用户数据
 */
class EditUserInfoActivity : AppBaseActivity<ActivityEditUserInfoBinding, UserLoginBean>(),
    QuickRtObserverListener<BaseBean<UserLoginBean>>,
    PhotoActivity.PhotoChooseListener, CropActivity.CropPhotoListener {

    companion object {
        fun startEditUserInfoActivity(activity: Activity, userLoginBean: UserLoginBean) {
            activity.startActivity(Intent(activity, EditUserInfoActivity::class.java).apply {
                putExtra(JumpType.J0, userLoginBean)
            })
        }
    }

    private lateinit var userLoginBean: UserLoginBean
    private val readLoginToken = UserUtils.readLoginToken()
    private val userApi = RetrofitManager.getInstance().create(UserApi::class.java)


    override fun onAppCreate(savedInstanceState: Bundle?) {
        userLoginBean = getSerializableExtra(JumpType.J0, UserLoginBean::class.java)
        initClick()
        initUserInfoUi(userLoginBean)
    }


    private fun initClick() {

        binding.userImage.setOnClickListener {
            PhotoActivity.goPhotoImage(context, 1, 4, 1, this)
        }


        binding.userNameClick.setOnClickListener {
            EditInfoDialog(
                context,
                "${getString(R.string.dialog_edit_modify)}${getString(R.string.edit_user_info_name)}",
                "${getString(R.string.dialog_edit_input)}${getString(R.string.edit_user_info_name)}",
                userLoginBean.userName,
                10
            ) { _, v1, _ ->
                userApi.sendEditName(v1)
                    .mySubscribeMainThread(this, this, R.string.dialog_load_title_modify)
                true
            }.show()
        }

        binding.userSexClick.setOnClickListener {
            EditSexDialog(context, userLoginBean.userSex) { _, isBoy ->
                userApi.sendEditSex(isBoy)
                    .mySubscribeMainThread(this, this, R.string.dialog_load_title_modify)
                true
            }.show()
        }

        binding.userBirthClick.setOnClickListener {
            EditAgeDialog(
                context,
                userLoginBean.userBirth
            ) { _, birth ->
                userApi.sendEditBirth(birth)
                    .mySubscribeMainThread(this, this, R.string.dialog_load_title_modify)
                true
            }.show()
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
                userApi.sendEditIrd(v1)
                    .mySubscribeMainThread(this, this, R.string.dialog_load_title_modify)
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
                userApi.sendEditUnit(v1)
                    .mySubscribeMainThread(this, this, R.string.dialog_load_title_modify)
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
                userApi.sendEditPassword(v1, v2)
                    .mySubscribeMainThread(this, this, R.string.dialog_load_title_modify)
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
        binding.userBirth.text = userLoginBean.userBirth
        binding.userAccount.text = userLoginBean.userAccount
        binding.userSchool.text = userLoginBean.userUnit
        binding.userCreateTime.text = userLoginBean.createTime
    }


    override fun onSendError(t: BaseBean<UserLoginBean>?, e: Throwable) {
        showToast(t?.reason ?: e.message)
    }

    override fun onSendComplete(t: BaseBean<UserLoginBean>) {
        postEventBus(t.result!!)
    }

    override fun onEventBus(event: UserLoginBean?, key: Int?, msg: String?) {
        event?:return
        initUserInfoUi(event)
    }

    // 图片选择
    override fun onChooseClick(files: MutableList<String>?, num: Int) {
        if (files.isNullOrEmpty()) {
            showToast(R.string.photo_choose_nul)
            return
        }
        CropActivity.startCropActivity(this, files[0], this)
        /* Handler(Looper.myLooper()!!).postDelayed({
             CropActivity.startCropActivity(this, files[0])
         }, 300)*/
    }

    // 裁剪完成
    override fun onCropClick(file: File?) {
        file ?: let {
            showToast(R.string.photo_crop_error)
            return
        }
        // 上传
        val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
        val imageBodyPart = MultipartBody.Part.createFormData("img", file.name, imageBody)
        userApi.sendEditImage(imageBodyPart).mySubscribeMainThread(this, this)
    }
}
