package com.mayunfeng.join.base

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.ActionMode
import android.view.View
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.mayunfeng.join.R
import com.mayunfeng.join.bean.BaseEventBean
import com.pikachu.utils.base.BaseActivity
import com.pikachu.utils.utils.AppManagerUtils
import com.pikachu.utils.utils.DarkModeUtils
import com.pikachu.utils.utils.UiUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable
import java.util.*


/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.base
 * @Author:         pkpk.run
 * @Description:    null
 */
abstract class AppBaseActivity<T : ViewBinding, ED : Serializable> : BaseActivity<T>() {




    companion object{
        val activityStack: Stack<Activity> = Stack<Activity>()
    }

    abstract fun onAppCreate(savedInstanceState: Bundle?)
    override fun initActivity(savedInstanceState: Bundle?) {
        // resources.getBoolean(R.bool.isStatusBar)
        setActivityWindowsInfo(!DarkModeUtils.isDarkMode(this))
        EventBus.getDefault().register(this)
        // 发布粘性事件
        //EventBus.getDefault().postSticky(asnDetailEventBus)
        // 发布普通事件
        // EventBus.getDefault().post(messageEvent);
        activityStack.add(this)
        try {
            findViewById<View>(R.id.app_back)?.setOnClickListener {
                finish()
            }
            findViewById<View>(R.id.app_status)?.let {
                it.layoutParams.height =
                UiUtils.getStatusBarHeight(this@AppBaseActivity)
            }
        } catch (_: Exception) { }
        onAppCreate(savedInstanceState)
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



    open fun setActivityWindowsInfo(isStatusBar: Boolean) {
        ImmersionBar.with(this)
            .navigationBarDarkIcon(isStatusBar)
            .statusBarDarkFont(isStatusBar)
            .fitsSystemWindows(true)
            .navigationBarColor(R.color.color_bg)
            .statusBarColor(R.color.color_bg)
            .init()
    }

    open fun currentActivity(): Activity {
        return activityStack.lastElement()
    }


    override fun onDestroy() {
        EventBus.getDefault().removeAllStickyEvents()
        EventBus.getDefault().unregister(this)
        activityStack.remove(this)
        super.onDestroy()
    }
}
