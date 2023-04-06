package com.mayunfeng.join

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mayunfeng.join.service.CLOSE_ALL_NOTIFY_KEY
import com.mayunfeng.join.service.WebSocketService
import com.mayunfeng.join.ui.widget.LoadFooter
import com.mayunfeng.join.ui.widget.LoadHeader
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.*
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join
 * @Author:         pkpk.run
 * @Description:    null
 */

// 192.168.0.105
// 42.192.221.73
// 192.168.0.112

// 服务端 ip 地址
const val BASE_ADDRESS = "192.168.0.112"
// 端口
const val BASE_PORT = "8012"
const val BASE_HTTP_URL = "http://${BASE_ADDRESS}:${BASE_PORT}"
const val BASE_WS_URL = "ws://${BASE_ADDRESS}:${BASE_PORT}"
const val HTTP_OK = 200
const val TOKEN_ERROR_CODE = -3
const val TOKEN_ERROR_KEY = "token"
const val AES_PASSWORD = "myf"

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
        LogsUtils.init("TEST_TT")
        SharedPreferencesUtils.init(this)
        DarkModeUtils.init(this)
        RetrofitManager.init(BASE_HTTP_URL)
        GlideUtils.init(BASE_HTTP_URL)
        AESBCBUtils.init(AES_PASSWORD)
        myApplicationContext = applicationContext



        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) { }

            override fun onActivityStarted(p0: Activity) { }

            override fun onActivityResumed(p0: Activity) {
                LogsUtils.showLog(p0.javaClass.name)
                if (isLoginOk){
                    if (!WebSocketService.isServiceRunning(applicationContext, WebSocketService::class.java)){
                        startService(Intent(myApplicationContext, WebSocketService::class.java))
                    } else {
                        WebSocketService.getWebSocketService()?.startWebSocketNul()
                    }
                }
            }

            override fun onActivityPaused(p0: Activity) { }

            override fun onActivityStopped(p0: Activity) { }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) { }

            override fun onActivityDestroyed(p0: Activity) { }
        })
    }

    companion object {

        lateinit var myApplicationContext : Context
        var isLoginOk : Boolean = false




        fun getUrl(relativePath: String): String = GlideUtils.getUrl(BASE_HTTP_URL, relativePath)
        fun getWsUrl(relativePath: String): String = GlideUtils.getUrl(BASE_WS_URL, relativePath)

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