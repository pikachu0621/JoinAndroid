package com.mayunfeng.join.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.king.zxing.util.CodeUtils
import com.mayunfeng.join.R
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.databinding.ActivityPwsPreviewBinding
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.LogsUtils

class ActivityPwsPreview : AppBaseActivity<ActivityPwsPreviewBinding, String>() {

    private var cloudPws: String? = null
    private var type: Int = 0

    companion object {
        /**
         * @param cloudPws 密码
         */
        fun startActivity(activity: Activity, cloudPws: String, type: Int) {
            activity.startActivity(Intent(activity, ActivityPwsPreview::class.java).apply {
                putExtra(JumpType.J0, cloudPws)
                putExtra(JumpType.J1, type)
            })
            activity.overridePendingTransition(R.anim.aty_in, R.anim.aty_ont)
        }
    }

    override fun setActivityWindowsInfo(isStatusBar: Boolean) {
        ImmersionBar.with(this)
            .statusBarDarkFont(false)
            .fitsSystemWindows(false)
            .navigationBarDarkIcon(isStatusBar)
            .navigationBarColor(R.color.black)
            .init()
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        binding.appBack.setOnClickListener {
            LoginActivity.finishTs(this)
        }
        cloudPws = getStringExtra(JumpType.J0)
        type = getIntExtra(JumpType.J1, 0)
        initPatternLockerView()
    }


    private fun initPatternLockerView() {

        // 0 无密码打卡   1 签到码打卡    2 二维码打卡     3 手势打卡
        when (type) {
            1 -> {
                var st = ""
                cloudPws!!.forEach { st += "$it " }
                binding.appCompatTextView9.text = st.substring(0, st.length - 1)
                binding.appCompatTextView9.visibility = View.VISIBLE
            }
            2 -> {
                binding.qrcImg.post {
                    binding.qrcImg.setImageBitmap(
                        CodeUtils.createQRCode(
                            QRCodeDisplayActivity.createQrStr(
                                cloudPws!!
                            ), binding.qrcImg.width
                        )
                    )
                }
                binding.qrcImgRe.visibility = View.VISIBLE
            }
            3 -> {
                val hitIndexList = arrayListOf<Int>()
                binding.patternIndicatorView.visibility = View.VISIBLE
                cloudPws!!.forEach {
                    hitIndexList.add(it.toString().toInt())
                }
                binding.patternIndicatorView.updateState(hitIndexList, false)
            }
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoginActivity.finishTs(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}