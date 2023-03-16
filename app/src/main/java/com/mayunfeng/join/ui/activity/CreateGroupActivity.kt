package com.mayunfeng.join.ui.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.mayunfeng.join.R
import com.mayunfeng.join.api.GroupApi
import com.mayunfeng.join.api.PucApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.databinding.ActivityCreateGroupBinding
import com.mayunfeng.join.ui.adapter.CreateGroupTypeAdapter
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import io.reactivex.rxjava3.disposables.Disposable
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


    override fun onAppCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initUi()
        initEdit()
        initTypeList()
        sendGroup()
    }

    // 发送数据
    private fun sendGroup() {
        // 上传图片 及其参数
        binding.ok.setOnClickListener {
            if (!binding.groupName.text.isNullOrEmpty() && !binding.groupIntroduce.text.isNullOrEmpty() && binding.groupImageMask.visibility != View.VISIBLE){
                // 上传
                val addFormDataPart = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name", binding.groupName.text.toString())
                    .addFormDataPart("type", createGroupTypeAdapter!!.getType())
                    .addFormDataPart("ird", binding.groupIntroduce.text.toString())
                    .addFormDataPart(
                        "img",
                        imageFile!!.name,
                        RequestBody.create(MediaType.parse("image/png"), imageFile!!)
                    )
                    .build()
                groupApi.sendCreateGroup(addFormDataPart).mySubscribeMainThread(this, this)
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
            override fun onError(t: BaseBean<Array<String>>?, e: Throwable) {
                showToast(R.string.load_footer_err)
                finish()
            }

            override fun onComplete(t: BaseBean<Array<String>>) {
                val result = mutableListOf<String>().apply { addAll(t.result!!) }
                createGroupTypeAdapter = CreateGroupTypeAdapter(result) {
                    showToast(it)
                }
                binding.classList.adapter = createGroupTypeAdapter
            }
        })
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
    override fun onError(t: BaseBean<GroupBean>?, e: Throwable) {
        showToast(t?.reason ?: e.message)
    }

    // 创建成功
    override fun onComplete(t: BaseBean<GroupBean>) {
        showToast(R.string.dialog_msg_complete)
        finish()
    }
}
