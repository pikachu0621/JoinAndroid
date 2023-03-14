package com.mayunfeng.join.utils.retrofit

import io.reactivex.rxjava3.disposables.Disposable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils.retrofit
 * @Author:         pkpk.run
 * @Description:    null
 */
interface QuickRtObserverListener<T> : RtObserverListener<T> {

    override fun onRetrofitSubscribe(d: Disposable){
        onStart(d)
    }

    override  fun onRetrofitNext(t: T){
        onComplete(t)
    }

    override  fun onRetrofitError(t: T?, isHandled: Boolean, e: Throwable){
        onError(t, isHandled, e)
    }

    override fun onRetrofitComplete(t: T){ }


    fun onStart(d: Disposable)

    fun onComplete(t: T)

    fun onError(t: T?, isHandled: Boolean, e: Throwable)
}