package com.pkpk.join.ui.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import com.gyf.immersionbar.ImmersionBar
import com.pkpk.join.R
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.databinding.ActivityPwsGestureBinding
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.LogsUtils
import com.pikachu.utils.utils.TimeUtils

class PwsGestureActivity : AppBaseActivity<ActivityPwsGestureBinding, String>() {


    private var settingPws: String? = null
    private var cloudPws: String? = null

    companion object{
        /**
         * @param cloudPws 密码 == null 设置模式
         */
        fun startActivity(activity: Activity, cloudPws: String? = null) {
            activity.startActivity(Intent(activity, PwsGestureActivity::class.java).apply {
                putExtra(JumpType.J0, cloudPws)
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
        initPatternLockerView()
    }

    private fun initPatternLockerView() {



        binding.patternLockView.setOnPatternChangedListener(object : OnPatternChangeListener {

            private var isClear = false

            override fun onStart(view: PatternLockerView) { }

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {
                binding.patternIndicatorView.updateState(hitIndexList, false)
            }

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {

                if (cloudPws != null){
                    if (conversionStringPws(hitIndexList) != cloudPws){
                        jitterAnimator(getString(R.string.edit_pws_pattern_error), (0xFFFF0000).toInt())
                        view.updateStatus(true)
                        binding.patternIndicatorView.updateState(hitIndexList, true)
                        isClear = true
                        return
                    }
                    // 签到成功业务逻辑
                    postEventBus(conversionStringPws(hitIndexList))
                    LoginActivity.finishTs(this@PwsGestureActivity)
                    return
                }
                if ((hitIndexList.isEmpty() || hitIndexList.size < 4)) {
                    jitterAnimator(getString(R.string.edit_pws_pattern_for4), (0xFFFF0000).toInt())
                    view.updateStatus(true)
                    binding.patternIndicatorView.updateState(hitIndexList, true)
                    isClear = true
                    return
                }
                if (settingPws == null){
                    jitterAnimator(getString(R.string.edit_pws_pattern_again), ContextCompat.getColor(context, R.color.color_principal), false)
                    settingPws = conversionStringPws(hitIndexList)
                    return
                }
                val conversionStringPws = conversionStringPws(hitIndexList)
                if (settingPws != conversionStringPws){
                    jitterAnimator(getString(R.string.edit_pws_pattern_mismatch), (0xFFFF0000).toInt())
                    binding.patternIndicatorView.updateState(hitIndexList, true)
                    isClear = true
                    settingPws = null
                    return
                }
                // 返回 pws 数据
                LogsUtils.showLog(settingPws)
                postEventBus(settingPws)
                LoginActivity.finishTs(this@PwsGestureActivity)
            }

            override fun onClear(view: PatternLockerView) {
                if (isClear){
                    binding.patternIndicatorView.updateState(arrayListOf(), false)
                    isClear = false
                }
            }
        })
    }



   private  fun jitterAnimator(msg: String? = null, colorMsg: Int = Color.WHITE, isReset: Boolean = true){
        msg?.let {
            binding.toastMsg.text = msg
            binding.toastMsg.setTextColor(colorMsg)
        }
        ObjectAnimator.ofFloat(binding.toastMsg, "translationX",   0f, -100f, 0f, 100f, 0f).apply {
            duration = 40
            repeatCount = 4
            repeatMode = ValueAnimator.REVERSE
            if (isReset){
                addListener {
                    TimeUtils.timing(800){
                        binding.toastMsg.text = getString(R.string.start_sign_type_gesture)
                        binding.toastMsg.setTextColor(Color.WHITE)
                    }
                }
            }
        }.start()
    }


    private fun conversionStringPws(hitIndexList: List<Int>): String{
        var pws = ""
        hitIndexList.forEach { it ->
            pws += it
        }
        return pws
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoginActivity.finishTs(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}