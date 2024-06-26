package com.pkpk.join.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.pkpk.join.R
import com.pkpk.join.ui.adapter.PhotoRecycler1Adapter
import com.pkpk.join.ui.adapter.PhotoRecycler2Adapter
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.databinding.ActivityPhotoBinding
import com.pkpk.join.databinding.UiItemPhoto2Binding
import com.pikachu.utils.photo.GetPhotoUtils
import com.pikachu.utils.photo.PhotoModule
import java.io.Serializable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.ui.activity
 * @Author:         pkpk.run
 * @Description:    null
 */
class PhotoActivity : AppBaseActivity<ActivityPhotoBinding, Serializable>(),
    PhotoRecycler1Adapter.OnItemClickListener, PhotoRecycler2Adapter.OnItemClickListener {
    private var photoRecycler1Adapter: PhotoRecycler1Adapter? = null
    private var photoRecycler2Adapter: PhotoRecycler2Adapter? = null
    private var thread: Thread? = null
    private var isShow = false
    private var has: MutableList<String>? = null
    private var num = 0
    private var clickItemData: PhotoModule? = null

    @SuppressLint("SetTextI18n")
    override fun onAppCreate(savedInstanceState: Bundle?) {
        GetPhotoUtils.init(context, type)
        binding.r1.setOnClickListener { showAndStop(!isShow) }
        binding.p5.setOnClickListener { showAndStop(!isShow) }
        binding.appBack.setOnClickListener {
            if (photoChooseListener != null) {
                if (has != null) {
                    has!!.clear()
                }
                photoChooseListener!!.onChooseClick(has, 0)
                finishTs()
            }
        }
        binding.top5.setOnClickListener {
            if (photoChooseListener != null) {
                photoChooseListener!!.onChooseClick(has, num)
                finishTs()
            }
        }
        photoRecycler1Adapter = PhotoRecycler1Adapter(null, type, this, this)
        photoRecycler2Adapter = PhotoRecycler2Adapter(null, type, context, this)
        binding.p4.adapter = photoRecycler1Adapter
        binding.p6.adapter = photoRecycler2Adapter
        binding.p4.layoutManager = GridLayoutManager(context, maxW)
        binding.p6.layoutManager = LinearLayoutManager(context)
        photoRecycler1Adapter!!.setMaxW(maxW)
        photoRecycler1Adapter!!.setBorderW(borderW)
        photoRecycler1Adapter!!.setMaxNum(maxNum)
        binding.top3.text = getString(R.string.photo_all) + GetPhotoUtils.getTypeStr()
        binding.p61.text = getString(R.string.photo_on) + GetPhotoUtils.getTypeStr()
        permissions()
        // readPhoto()
    }

    private fun readPhoto() {
        if (thread != null && thread!!.isInterrupted) {
            thread!!.interrupt()
        }
        thread = Thread {
            val systemPhotoLibs = GetPhotoUtils.getSystemLibs()
            if (clickItemData != null) {
                val systemPathLibs = GetPhotoUtils.getSystemPathLibs(clickItemData)
                runOnUiThread {
                    photoRecycler1Adapter!!.refreshData(systemPathLibs.files)
                    photoRecycler2Adapter!!.refreshData(systemPhotoLibs)
                }
                return@Thread
            }
            val systemList = GetPhotoUtils.getSystemList()
            runOnUiThread {
                if (systemList.size <= 0) {
                    binding.p61.visibility = View.VISIBLE
                    binding.r1.isClickable = false
                    return@runOnUiThread
                }
                binding.p61.visibility = View.GONE
                binding.r1.isClickable = true
                photoRecycler1Adapter!!.refreshData(systemList)
                photoRecycler2Adapter!!.refreshData(systemPhotoLibs)
            }
        }
        thread!!.start()
    }

    @SuppressLint("Recycle")
    private fun showAndStop(isShow: Boolean) = if (isShow) {
        this.isShow = true
        //显示相册
        binding.p5.visibility = View.VISIBLE
        val alphaProper = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        val scaleYProper = PropertyValuesHolder.ofFloat("translationY", -800f, 0f)
        val rotation = PropertyValuesHolder.ofFloat("rotation", 180f)
        val animator1 = ObjectAnimator.ofPropertyValuesHolder(binding.p5, alphaProper) // 遮罩
        val animator2 =
            ObjectAnimator.ofPropertyValuesHolder(binding.p6, alphaProper, scaleYProper) // 列表
        val animator3 = ObjectAnimator.ofPropertyValuesHolder(binding.top4, rotation) // 旋转
        animator1.interpolator = DecelerateInterpolator()
        animator2.interpolator = DecelerateInterpolator()
        animator3.interpolator = DecelerateInterpolator()
        animator1.duration = 200
        animator2.duration = 200
        animator3.duration = 200
        animator1.start()
        animator2.start()
        animator3.start()
    } else {
        this.isShow = false
        //隐藏相册
        val alphaProper = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val scaleYProper = PropertyValuesHolder.ofFloat("translationY", 0f, -800f)
        val rotation = PropertyValuesHolder.ofFloat("rotation", 0f)
        val animator1 = ObjectAnimator.ofPropertyValuesHolder(binding.p5, alphaProper) // 遮罩
        val animator2 =
            ObjectAnimator.ofPropertyValuesHolder(binding.p6, alphaProper, scaleYProper) // 列表
        val animator3 = ObjectAnimator.ofPropertyValuesHolder(binding.top4, rotation) // 旋转
        animator1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator, isReverse: Boolean) {}
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                binding.p5.visibility = View.GONE
            }
        })
        animator1.interpolator = DecelerateInterpolator()
        animator2.interpolator = DecelerateInterpolator()
        animator3.interpolator = DecelerateInterpolator()
        animator1.duration = 200
        animator2.duration = 200
        animator3.duration = 200
        animator1.start()
        animator2.start()
        animator3.start()
    }

    override fun onItemClick(has: MutableList<String>, position: Int, num: Int) {
        binding.top5.text =
            if (num <= 0) getString(R.string.dialog_msg_confirm) else "${getString(R.string.dialog_msg_confirm)}($num)"
        this.has = has
        this.num = num
    }

    override fun onItemClick(
        binding: UiItemPhoto2Binding,
        itemData: PhotoModule,
        position: Int,
        data: MutableList<PhotoModule?>,
    ) {
        clickItemData = if (itemData.isAll) {
            null
        } else {
            itemData
        }
        photoRecycler1Adapter!!.refreshData(itemData.files)
        showAndStop(false.also { isShow = it })
        this.binding.top3.text = itemData.name
    }

    interface PhotoChooseListener {
        /**
         * 选择回调
         * @param files 文件
         * @param num 已选
         */
        fun onChooseClick(files: MutableList<String>?, num: Int)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (photoChooseListener != null) {
                if (has != null) {
                    has!!.clear()
                }
                photoChooseListener!!.onChooseClick(has, 0)
                finishTs()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun finishTs() {
        finish()
        overridePendingTransition(R.anim.aty_ont, R.anim.aty_out)
    }

    override fun onRestart() {
        super.onRestart()
        if (XXPermissions.isGranted(context, Permission.READ_MEDIA_IMAGES)) {
            readPhoto()
        }
    }


    private fun permissions() {
        XXPermissions.with(this)
            .permission(Permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    readPhoto()
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        XXPermissions.startPermissionActivity(context, permissions)
                        showToast(R.string.app_permissions_turn_down)
                        return
                    }
                    showToast(R.string.app_permissions_err)
                }
            })
    }


    companion object {
        private var photoChooseListener: PhotoChooseListener? = null
        private var maxNum = 0
        private var maxW = 4
        private var borderW = 0 //一行几个
        private var type = GetPhotoUtils.Type.Image

        /**
         *
         * look down
         */
        fun goPhotoImage(
            context: Context,
            maxNum: Int,
            maxW: Int,
            borderWDp: Int,
            photoChooseListener: PhotoChooseListener?,
        ) {
            goPhoto(context, GetPhotoUtils.Type.Image, maxNum, maxW, borderWDp, photoChooseListener)
        }

        fun goPhotoVideo(
            context: Context,
            maxNum: Int,
            maxW: Int,
            borderWDp: Int,
            photoChooseListener: PhotoChooseListener?,
        ) {
            goPhoto(context, GetPhotoUtils.Type.Video, maxNum, maxW, borderWDp, photoChooseListener)
        }

        fun goPhotoAudio(
            context: Context,
            maxNum: Int,
            maxW: Int,
            borderWDp: Int,
            photoChooseListener: PhotoChooseListener?,
        ) {
            goPhoto(context, GetPhotoUtils.Type.Audio, maxNum, maxW, borderWDp, photoChooseListener)
        }

        fun goPhotoVideo(context: Context, maxNum: Int, photoChooseListener: PhotoChooseListener?) {
            goPhoto(context, GetPhotoUtils.Type.Video, maxNum, 3, 1, photoChooseListener)
        }

        fun goPhotoAudio(context: Context, maxNum: Int, photoChooseListener: PhotoChooseListener?) {
            goPhoto(context, GetPhotoUtils.Type.Audio, maxNum, 3, 1, photoChooseListener)
        }

        fun goPhotoImage(context: Context, maxNum: Int, photoChooseListener: PhotoChooseListener?) {
            goPhoto(context, GetPhotoUtils.Type.Image, maxNum, 3, 1, photoChooseListener)
        }

        /**
         * 媒体选择
         * @param context context
         * @param type 类型 图片，视频， 音频
         * @param maxNum 最大选择几个
         * @param maxW 一行几个
         * @param borderWDp 边框
         * @param photoChooseListener 回调
         */
        fun goPhoto(
            context: Context,
            @GetPhotoUtils.Type type: Int,
            maxNum: Int,
            maxW: Int,
            borderWDp: Int,
            photoChooseListener: PhotoChooseListener?,
        ) {
            Companion.photoChooseListener = photoChooseListener
            Companion.type = type
            Companion.maxNum = maxNum
            Companion.maxW = maxW
            borderW = borderWDp
            context.startActivity(Intent(context, PhotoActivity::class.java))
            if (context is Activity) {
                context.overridePendingTransition(R.anim.aty_in, R.anim.aty_ont)
            }
        }
    }
}