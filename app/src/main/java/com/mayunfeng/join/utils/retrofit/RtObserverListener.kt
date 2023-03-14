package com.mayunfeng.join.utils.retrofit

import io.reactivex.rxjava3.disposables.Disposable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils.retrofit
 * @Author:         pkpk.run
 * @Description:    null
 */
interface RtObserverListener<T> {

    fun onRetrofitSubscribe(d: Disposable)

    fun onRetrofitNext(t: T)

    fun onRetrofitError(t: T?, isHandled: Boolean, e: Throwable)

    fun onRetrofitComplete(t: T)

}