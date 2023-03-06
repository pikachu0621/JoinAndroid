package com.mayunfeng.adminuser

import android.app.Application
import android.content.Context
import com.mayunfeng.adminuser.ui.widget.LoadFooter
import com.mayunfeng.adminuser.ui.widget.LoadHeader
import com.pikachu.utils.utils.LogsUtils
import com.pikachu.utils.utils.SharedPreferencesUtils
import com.pikachu.utils.utils.ToastUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator


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