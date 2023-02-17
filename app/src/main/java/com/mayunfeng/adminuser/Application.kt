package com.mayunfeng.adminuser

import android.app.Application
import com.pikachu.utils.utils.LogsUtils
import com.pikachu.utils.utils.SharedPreferencesUtils
import com.pikachu.utils.utils.ToastUtils

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser
 * @Author:         pkpk.run
 * @Description:    null
 */
class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
        LogsUtils.init(this)
        SharedPreferencesUtils.init(this)

        //你是小狗吧？哈哈哈哈哈哈哈
        //12345上山打老虎
    }
}