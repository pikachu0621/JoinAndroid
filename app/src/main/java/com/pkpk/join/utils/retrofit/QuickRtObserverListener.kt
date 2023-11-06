package com.pkpk.join.utils.retrofit

import io.reactivex.rxjava3.disposables.Disposable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.utils.retrofit
 * @Author:         pkpk.run
 * @Description:    null
 */
interface QuickRtObserverListener<T> : RtObserverListener<T> {

    override fun onRetrofitSubscribe(d: Disposable){
        onSendStart(d)
    }

    override  fun onRetrofitNext(t: T){
        onSendComplete(t)
    }

    override  fun onRetrofitError(t: T?, e: Throwable){
        onSendError(t, e)
    }

    override fun onRetrofitComplete(t: T){}


    /**
     * 开始时
     */
    fun onSendStart(d: Disposable){

    }

    /**
     * 完成时
     */
    fun onSendComplete(t: T)

    /**
     * 失败时
     */
    fun onSendError(t: T?, e: Throwable){

    }
}