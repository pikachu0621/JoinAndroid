package com.pkpk.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.pkpk.join.R
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.databinding.ActivitySettingBinding
import com.pkpk.join.ui.dialog.MsgDialog
import com.pikachu.utils.utils.DarkModeUtils
import com.pikachu.utils.utils.SharedPreferencesUtils
import java.io.Serializable


class SettingActivity : AppBaseActivity<ActivitySettingBinding, Serializable>() {

    override fun onAppCreate(savedInstanceState: Bundle?) {

        binding.fragmentSettingLin1.setOnClickListener {
            val msgDialog = MsgDialog(this, getString(R.string.settings_disclaimer), { true }, cancelStr = "")
            msgDialog.show()
            msgDialog.setWidthProportion(0.75F)
        }

        binding.fragmentSettingLin2.setOnClickListener {
            jumpURl(this, "https://github.com/pikachu0621/JoinAndroid")
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



/*
        binding.l1.setOnClickListener {
            LanguageUtils.toggle(context, LanguageUtils.Language.SYS)
            binding.ll1.isChecked = true
            binding.ll2.isChecked = false
            binding.ll3.isChecked = false
        }
        binding.l2.setOnClickListener {
            LanguageUtils.toggle(context, LanguageUtils.Language.CN)
            binding.ll1.isChecked = false
            binding.ll2.isChecked = true
            binding.ll3.isChecked = false
        }
        binding.l3.setOnClickListener {
            LanguageUtils.toggle(context, LanguageUtils.Language.EN)
            binding.ll1.isChecked = false
            binding.ll2.isChecked = false
            binding.ll3.isChecked = true
        }*/

    }

    override fun onResume() {
        super.onResume()
        when(SharedPreferencesUtils.readInt("SystemMode", 0) ){
            0 ->  binding.tt1.isChecked = true
            1 ->  binding.tt2.isChecked = true
            2 ->  binding.tt3.isChecked = true
        }

       /* when(LanguageUtils.getLanguageCode()){
            LanguageUtils.Language.SYS ->  binding.ll1.isChecked = true
            LanguageUtils.Language.CN -> binding.ll2.isChecked = true
            LanguageUtils.Language.EN -> binding.ll3.isChecked = true
        }*/
    }



    private fun jumpURl(activity: Activity, url: String) {
        activity.startActivity(Intent("android.intent.action.VIEW").apply {
            `data` = Uri.parse(url)
        })
    }
}