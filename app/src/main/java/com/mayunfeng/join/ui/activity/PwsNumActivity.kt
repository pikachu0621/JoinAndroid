package com.mayunfeng.join.ui.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.View
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import com.mayunfeng.join.R
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.databinding.ActivityPwsNumBinding
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.TimeUtils

class PwsNumActivity : AppBaseActivity<ActivityPwsNumBinding, String>() {

    private var cloudPws: String? = null
    private var settingPws: String? = null

    companion object {
        /**
         * @param pws 密码 == null 设置模式
         */
        fun startActivity(activity: Activity, cloudPws: String? = null) {
            activity.startActivity(Intent(activity, PwsNumActivity::class.java).apply {
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
        initNum()
    }

    private fun initNum() {
        binding.inputNum0.setOnClickListener {
            vi(it)
            inputNum("0")
        }
        binding.inputNum1.setOnClickListener { inputNum("1", it) }
        binding.inputNum2.setOnClickListener { inputNum("2", it) }
        binding.inputNum3.setOnClickListener { inputNum("3", it) }
        binding.inputNum4.setOnClickListener { inputNum("4", it) }
        binding.inputNum5.setOnClickListener { inputNum("5", it) }
        binding.inputNum6.setOnClickListener { inputNum("6", it) }
        binding.inputNum7.setOnClickListener { inputNum("7", it) }
        binding.inputNum8.setOnClickListener { inputNum("8", it) }
        binding.inputNum9.setOnClickListener { inputNum("9", it) }
        binding.inputNumDel.setOnClickListener { delNum(it) }
    }



    private fun getPws(): String?{
        return if (binding.num1.text != "-" && binding.num2.text != "-" && binding.num3.text != "-" && binding.num4.text != "-" ) {
           "${binding.num1.text}${binding.num2.text}${binding.num3.text}${binding.num4.text}"
        } else {
            null
        }
    }

    private fun clear(){
        binding.num1.text = "-"
        binding.num2.text = "-"
        binding.num3.text = "-"
        binding.num4.text = "-"
    }

    private fun delNum(view: View? = null) {

        if (view != null) vi(view)

        if (binding.num4.text != "-") {
            binding.num4.text = "-"
            return
        }
        if (binding.num3.text != "-") {
            binding.num3.text = "-"
            return
        }
        if (binding.num2.text != "-") {
            binding.num2.text = "-"
            return
        }
        if (binding.num1.text != "-") {
            binding.num1.text = "-"
            return
        }
    }

    private fun inputNum(num: CharSequence, view: View? = null) {
        if (view != null) vi(view)

        if (binding.num1.text == "-") {
            binding.num1.text = "$num"
            return
        }
        if (binding.num2.text == "-") {
            binding.num2.text = "$num"
            return
        }
        if (binding.num3.text == "-") {
            binding.num3.text = "$num"
            return
        }
        if (binding.num4.text == "-") {
            binding.num4.text = "$num"
        }
        if( cloudPws != null){
            if (cloudPws != getPws()){
                jitterAnimator("密码错误", (0xFFFF0000).toInt())
                clear()
                return
            }
            // todo 签到成功业务逻辑

            return
        }
        if (settingPws == null) {
            settingPws = getPws()
            jitterAnimator("再次输入", ContextCompat.getColor(context, R.color.color_principal), false)
            clear()
            return
        }

        if (settingPws != getPws()){
            jitterAnimator("不匹配，请重新输入", (0xFFFF0000).toInt())
            settingPws = null
            clear()
            return
        }

        // 返回 pws 数据
        postEventBus(getPws())
        LoginActivity.finishTs(this@PwsNumActivity)
    }

    private fun vi(view: View) {
        view.performHapticFeedback(
            HapticFeedbackConstants.VIRTUAL_KEY,
            HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                    or HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoginActivity.finishTs(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
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
                        binding.toastMsg.text = getString(R.string.start_sign_type_pws)
                        binding.toastMsg.setTextColor(Color.WHITE)
                    }
                }
            }
        }.start()
    }
}