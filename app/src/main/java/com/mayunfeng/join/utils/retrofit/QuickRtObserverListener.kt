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

    override  fun onRetrofitError(t: T?, e: Throwable){
        onError(t, e)
    }

    override fun onRetrofitComplete(t: T){}


    /**
     * 开始时
     */
    fun onStart(d: Disposable){

    }

    /**
     * 完成时
     */
    fun onComplete(t: T)

    /**
     * 失败时
     */
    fun onError(t: T?, e: Throwable){

    }
}