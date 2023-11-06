package com.pkpk.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.pkpk.join.R
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.databinding.ActivityCropBinding
import com.nanchen.compresshelper.CompressHelper
import com.pikachu.utils.utils.FileUtils
import com.pikachu.utils.utils.UiUtils
import java.io.File
import java.io.Serializable

private const val PHOTO_PATH = "PHOTO_PATH"

class CropActivity : AppBaseActivity<ActivityCropBinding, Serializable>() {


    private var photoPath: String? = null


    companion object {

        private var cropPhotoListener: CropPhotoListener? = null

        fun startCropActivity(
            activity: Activity,
            photoPath: String,
            cropPhotoListener: CropPhotoListener,
        ) {
            Companion.cropPhotoListener = cropPhotoListener
            activity.startActivity(Intent(activity, CropActivity::class.java).apply {
                putExtra(PHOTO_PATH, photoPath)
            })
            activity.overridePendingTransition(R.anim.aty_in, R.anim.aty_ont)
        }
    }

    interface CropPhotoListener {
        /**
         * 裁剪回调
         *
         * @param file 文件
         */
        fun onCropClick(file: File?)
    }


    override fun onAppCreate(savedInstanceState: Bundle?) {
        photoPath = getStringExtra(PHOTO_PATH)
        photoPath ?: finishTs()

        binding.likeView.setBitmapForWidth(
            photoPath,
            (UiUtils.getScreenWidth(context) / 1.3F).toInt()
        )

        binding.appBack.setOnClickListener {
            finishTs()
        }

        // 裁剪
        binding.top5.setOnClickListener {
            cropPhotoListener?:return@setOnClickListener
            val imageName = "${System.currentTimeMillis()}.png"
            val newFile = CompressHelper.Builder(this)
                .setMaxWidth(300F) // 默认最大宽度为720
                .setMaxHeight(300F) //
                .setQuality(60) // 默认压缩质量为80
                .setFileName(imageName) // 设置你需要修改的文件名
                .setCompressFormat(Bitmap.CompressFormat.PNG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(cacheDir.absolutePath)
                .build()
                .compressToFile(FileUtils.saveImage("$cacheDir${File.separator}$imageName", binding.likeView.clip(), 100, Bitmap.CompressFormat.PNG))
            cropPhotoListener!!.onCropClick(newFile)
            finishTs()
        }
    }

    private fun finishTs() {
        finish()
        overridePendingTransition(R.anim.aty_ont, R.anim.aty_out)
    }

}