package com.mayunfeng.join

import android.app.Application
import com.mayunfeng.adminuser.R
import com.mayunfeng.join.ui.widget.LoadFooter
import com.mayunfeng.join.ui.widget.LoadHeader
import com.pikachu.utils.utils.LogsUtils
import com.pikachu.utils.utils.SharedPreferencesUtils
import com.pikachu.utils.utils.ToastUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join
 * @Author:         pkpk.run
 * @Description:    null
 */
class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
        LogsUtils.init(this)
        SharedPreferencesUtils.init(this)
    }
    companion object{
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                //全局设置主题颜色
                layout.setPrimaryColorsId(
                    R.color.color_principal,
                    R.color.color_grey1
                )
                LoadHeader(context)
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                //全局设置主题颜色
                layout.setPrimaryColorsId(
                    R.color.color_principal,
                    R.color.color_grey1
                )
                // ClassicsFooter(context)
                LoadFooter(context)
            }
        }
    }
}