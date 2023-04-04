package com.mayunfeng.join.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.viewbinding.ViewBinding
import com.mayunfeng.join.bean.BaseEventBean
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable

open class BaseService<ED : Serializable>  : Service()  {



    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().removeAllStickyEvents()
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


    // ------------------------------------------------------------------- eventbus

    // 发布普通事件
    fun postEventBus(ed: ED?, key: Int? = 0, msg: String? = "", clazz: Class<*>? = null){
        EventBus.getDefault().post(BaseEventBean(ed, key, msg, clazz))
    }

    // 发布粘性事件
    fun postEventBusSticky(ed: ED?, key: Int? = 0, msg: String? = "", clazz: Class<*>? = null){
        EventBus.getDefault().postSticky(BaseEventBean(ed, key, msg, clazz))
    }

    // 处理普通事件
    @kotlin.jvm.Throws
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun baseEventBus(event: BaseEventBean<ED>?) {
        event ?: return
        if (event.tag == null) {
            onEventBus(event.ben, event.key, event.msg)
            return
        }
        if (event.tag != javaClass) return
        onEventBus(event.ben, event.key, event.msg)
    }

    open fun onEventBus(event: ED?, key: Int?, msg: String?) {

    }

    //处理黏性事件
    @kotlin.jvm.Throws
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun baseStickyEventBus(event: BaseEventBean<ED>?) {
        event ?: return
        if (event.tag == null) {
            onEventBusSticky(event.ben, event.key, event.msg)
            return
        }
        if (event.tag != javaClass) return
        onEventBusSticky(event.ben, event.key, event.msg)
    }

    open fun onEventBusSticky(event: ED?, key: Int?, msg: String?) {

    }
}