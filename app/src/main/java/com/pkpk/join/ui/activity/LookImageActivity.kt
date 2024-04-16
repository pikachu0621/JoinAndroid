package com.pkpk.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils
import com.pkpk.join.R
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.databinding.ActivityLookImageBinding
import java.io.Serializable

class LookImageActivity : AppBaseActivity<ActivityLookImageBinding, Serializable>(){

    private var imageUrl: String? = null
    private var isConvert: Boolean = true
    companion object {
        fun startActivity(activity: Activity, imageUrl: String) {
            activity.startActivity(Intent(activity, LookImageActivity::class.java).apply {
                putExtra(JumpType.J0, imageUrl)
            })
            activity.overridePendingTransition(R.anim.aty_in, R.anim.aty_ont)
        }
    }

    override fun setActivityWindowsInfo(isStatusBar: Boolean) {
        ImmersionBar.with(this)
            .hideBar(BarHide.FLAG_HIDE_BAR)
            .init()
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        imageUrl = getStringExtra(JumpType.J0)
        if (imageUrl.isNullOrEmpty()) {
            LoginActivity.finishTs(this)
            showToast("图片错误！")
        }
        binding.appBack.setOnClickListener {
            LoginActivity.finishTs(this)
        }

        binding.imgConvert.setOnClickListener {
            isConvert = !isConvert

            val colorBg: Int = if (isConvert) Color.BLACK else Color.WHITE
            val color: Int = if (isConvert) Color.WHITE else Color.BLACK
            binding.imgConvert.setColorFilter(color)
            binding.appBack.setColorFilter(color)
            binding.root.setBackgroundColor(colorBg)


        }

        GlideUtils.with(this)
            .loadHeaderToken(imageUrl!!)
            .into(binding.lookImage)
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoginActivity.finishTs(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}