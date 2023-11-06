package com.pkpk.join.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.pkpk.join.R
import com.pkpk.join.api.GroupApi
import com.pkpk.join.api.PucApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.GroupBean
import com.pkpk.join.databinding.ActivityCreateGroupBinding
import com.pkpk.join.ui.adapter.CreateGroupTypeAdapter
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.Serializable

/**
 * 创建组
 */
class CreateGroupActivity : AppBaseActivity<ActivityCreateGroupBinding, Serializable>(),
    PhotoActivity.PhotoChooseListener, CropActivity.CropPhotoListener,
    QuickRtObserverListener<BaseBean<GroupBean>> {

    private val pucApi: PucApi = RetrofitManager.getInstance().create(PucApi::class.java)
    private val groupApi: GroupApi = RetrofitManager.getInstance().create(GroupApi::class.java)
    private var imageFile: File? = null
    private var createGroupTypeAdapter: CreateGroupTypeAdapter? = null
    private var groupBean: GroupBean? = null


    companion object{
        fun startCreateGroupActivity(context: Context, groupBean: GroupBean) {
            context.startActivity(Intent(context, CreateGroupActivity::class.java).apply {
                putExtra(JumpType.J0, groupBean)
            })
        }
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        groupBean = getSerializableExtra(JumpType.J0, GroupBean::class.java)
        initUi()
        initEdit()
        initTypeList()
        sendGroup()
        if (groupBean != null) initDataUi(groupBean!!)
    }

    private fun initDataUi(groupBean: GroupBean) {
        // 修改数据
        binding.title.setText(R.string.activity_title_create_group_edit)
        binding.groupImageMask.visibility = View.GONE
        GlideUtils.with(context).loadHeaderToken(groupBean.groupImg).into(binding.groupImage)
        binding.groupName.setText(groupBean.groupName)
        binding.groupIntroduce.setText(groupBean.groupIntroduce)
        binding.ok.isChecked = true
        binding.ok.isClickable = true
    }

    // 发送数据
    private fun sendGroup() {
        // 上传图片 及其参数
        binding.ok.setOnClickListener {
            if (!binding.groupName.text.isNullOrEmpty() && !binding.groupIntroduce.text.isNullOrEmpty() && binding.groupImageMask.visibility != View.VISIBLE){
                // 上传
                if (groupBean == null){
                    val addFormDataPart = MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("name", binding.groupName.text.toString())
                        .addFormDataPart("type", createGroupTypeAdapter!!.getType())
                        .addFormDataPart("ird", binding.groupIntroduce.text.toString())
                        .addFormDataPart("img", imageFile!!.name, RequestBody.create(MediaType.parse("image/png"), imageFile!!))
                        .build()
                    groupApi.sendCreateGroup(addFormDataPart).mySubscribeMainThread(this, this)
                    return@setOnClickListener
                }
                val addFormDataPart = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
                    addFormDataPart("id", "${ groupBean!!.id }")
                    if (!binding.groupName.text.isNullOrEmpty()) addFormDataPart("name", binding.groupName.text.toString())
                    addFormDataPart("type", createGroupTypeAdapter!!.getType())
                    if (!binding.groupName.text.isNullOrEmpty()) addFormDataPart("ird", binding.groupIntroduce.text.toString())
                    if (imageFile != null && imageFile!!.exists()) addFormDataPart("img", imageFile!!.name, RequestBody.create(MediaType.parse("image/png"), imageFile!!))
                }.build()
                groupApi.sendEditUserGroup(addFormDataPart).mySubscribeMainThread(this, this)
            }
        }
    }

    private fun initUi() {
        binding.groupImage.setOnClickListener {
            PhotoActivity.goPhotoImage(context, 1, 4, 1, this )
        }
    }

    private fun initEdit() {

        binding.groupName.addTextChangedListener {
            binding.ok.isChecked = !binding.groupName.text.isNullOrEmpty() && !binding.groupIntroduce.text.isNullOrEmpty() && binding.groupImageMask.visibility != View.VISIBLE
            binding.ok.isClickable = binding.ok.isChecked
            binding.imgDel1.visibility = if (!binding.groupName.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding.groupIntroduce.addTextChangedListener {
            binding.ok.isChecked = !binding.groupName.text.isNullOrEmpty() && !binding.groupIntroduce.text.isNullOrEmpty() && binding.groupImageMask.visibility != View.VISIBLE
            binding.ok.isClickable = binding.ok.isChecked
            binding.imgDel2.visibility = if (!binding.groupIntroduce.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding.imgDel1.setOnClickListener {
            binding.groupName.setText("")
        }
        binding.imgDel2.setOnClickListener {
            binding.groupIntroduce.setText("")
        }

    }

    private fun initTypeList() {
        pucApi.sendGroupType().mySubscribeMainThread(this, object :QuickRtObserverListener<BaseBean<Array<String>>>{
            override fun onSendError(t: BaseBean<Array<String>>?, e: Throwable) {
                showToast(R.string.load_footer_err)
                finish()
            }

            override fun onSendComplete(t: BaseBean<Array<String>>) {
                val result = mutableListOf<String>().apply { addAll(t.result!!) }
                createGroupTypeAdapter = CreateGroupTypeAdapter(result) {}
                binding.classList.adapter = createGroupTypeAdapter
                if (groupBean != null){
                    createGroupTypeAdapter!!.setType(groupBean!!.groupType)
                }
            }
        }, -1)
    }


    // 选择的图片
    override fun onChooseClick(files: MutableList<String>?, num: Int) {
        if (files.isNullOrEmpty()){
            showToast(R.string.photo_choose_nul)
            return
        }
        CropActivity.startCropActivity(this, files[0], this)
    }

    // 裁剪后的图片
    override fun onCropClick(file: File?) {
        imageFile = file
        file ?:let {
            showToast(R.string.photo_crop_error)
            return
        }
        binding.groupImageMask.visibility = View.GONE
        binding.groupImage.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
        binding.ok.isChecked = !binding.groupName.text.isNullOrEmpty() && !binding.groupIntroduce.text.isNullOrEmpty() && binding.groupImageMask.visibility != View.VISIBLE
        binding.ok.isClickable = binding.ok.isChecked
    }


    // 创建失败
    override fun onSendError(t: BaseBean<GroupBean>?, e: Throwable) {
        showToast(t?.reason ?: e.message)
    }

    // 创建成功
    override fun onSendComplete(t: BaseBean<GroupBean>) {
        showToast(R.string.dialog_msg_complete)
        postEventBus(t)
        finish()
    }
}
