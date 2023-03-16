package com.mayunfeng.join.utils.retrofit

import io.reactivex.rxjava3.disposables.Disposable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils.retrofit
 * @Author:         pkpk.run
 * @Description:    拦截器
 */
interface RetrofitInterceptor<T> {

    fun onRetrofitSubscribe(d: Disposable): Boolean{
        return false
    }


    fun onRetrofitNext(t: T): Boolean{
        return false
    }

    /**
     * 错误号时
     */
    fun onRetrofitCode(code: Int?): Boolean{
        return false
    }


    fun onRetrofitError(t: T?, e: Throwable): Boolean{
        return false
    }

    fun onRetrofitComplete(t: T): Boolean{
        return false
    }

}