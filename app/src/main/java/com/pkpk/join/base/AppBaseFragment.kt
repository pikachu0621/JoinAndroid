package com.pkpk.join.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.pkpk.join.bean.BaseEventBean
import com.pikachu.utils.base.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.base
 * @Author:         pkpk.run
 * @Description:    null
 */
abstract class AppBaseFragment<T : ViewBinding, ED : Serializable> : BaseFragment<T>() {




    abstract fun onAppCreateView(
        savedInstanceState: Bundle?,
        binding: T,
        activity: FragmentActivity
    )

    override fun onInitView(savedInstanceState: Bundle?, binding: T, activity: FragmentActivity) {
        EventBus.getDefault().register(this)
        onAppCreateView(savedInstanceState, binding, activity)
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


    override fun onDestroyView() {
        EventBus.getDefault().removeAllStickyEvents()
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        EventBus.getDefault().removeAllStickyEvents()
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


}