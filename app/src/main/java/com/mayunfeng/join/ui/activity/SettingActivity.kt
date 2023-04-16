package com.mayunfeng.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.databinding.ActivitySettingBinding
import com.mayunfeng.join.ui.dialog.MsgDialog
import com.pikachu.utils.utils.DarkModeUtils
import com.pikachu.utils.utils.SharedPreferencesUtils
import java.io.Serializable

class SettingActivity : AppBaseActivity<ActivitySettingBinding, Serializable>() {

    override fun onAppCreate(savedInstanceState: Bundle?) {

        binding.fragmentSettingLin1.setOnClickListener {
            val msgDialog = MsgDialog(this, "免责声明！\n\n本项目使用到的所有组件均为<Apache License 2.0>" +
                    "\n\n以下为开源项目名" +
                    "\nretrofit2" +
                    "\nokhttp3" +
                    "\nrxjava3" +
                    "\nrxAndroid" +
                    "\ngson" +
                    "\neventbus" +
                    "\nrefresh" +
                    "\nlottie" +
                    "\nzxing" +
                    "\nCompressHelper" +
                    "\nAddressPicker" +
                    "\nkotlin-stdlib-jdk8" +
                    "\n\n项目可商用但请遵循<Apache License 2.0>开源协议" +
                    "\nby：Pikachu_WeChat", { true }, cancelStr = ""
            )
            msgDialog.show()
            msgDialog.setWidthProportion(0.75F)
        }

        binding.fragmentSettingLin2.setOnClickListener {
            jumpURl(this, "https://github.com/pikachu0621/MyfJoinAndroid")
        }




        binding.t1.setOnClickListener {
            DarkModeUtils.applySystemMode(this)
            SharedPreferencesUtils.write("SystemMode",  0)
            binding.tt1.isChecked = true
            binding.tt2.isChecked = false
            binding.tt3.isChecked = false
        }
        binding.t2.setOnClickListener {
            DarkModeUtils.applyNightMode(this)
            SharedPreferencesUtils.write("SystemMode",  1)
            binding.tt1.isChecked = false
            binding.tt2.isChecked = true
            binding.tt3.isChecked = false
        }
        binding.t3.setOnClickListener {
            DarkModeUtils.applyDayMode(this)
            SharedPreferencesUtils.write("SystemMode",  2)
            binding.tt1.isChecked = false
            binding.tt2.isChecked = false
            binding.tt3.isChecked = true
        }

    }

    override fun onResume() {
        super.onResume()
        when(SharedPreferencesUtils.readInt("SystemMode") ){
            0 ->  binding.tt1.isChecked = true
            1 ->  binding.tt2.isChecked = true
            2 ->  binding.tt3.isChecked = true
        }
    }



    private fun jumpURl(activity: Activity, url: String) {
        activity.startActivity(Intent("android.intent.action.VIEW").apply {
            `data` = Uri.parse(url)
        })
    }

}