package com.mayunfeng.join.utils.retrofit


import com.mayunfeng.join.utils.ReflectionUtils
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlin.reflect.*

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils.retrofit
 * @Author:         pkpk.run
 *
 * @param myJsonStatusProperty 自定义成功状态码的变量   例子 BaseBean::error_code
 * @param myJsonStatusCode 自定义成功状态码     字段
 * @Description:
 * onSubscribe -> onNext -> onError
 *                    |
 *                    V
 *              onComplete
 */
abstract class RetrofitObserver<T : Any>(
    private val retrofitObserverInterface: RtObserverListener<T>,
    private val myJsonStatusProperty: KCallable<Int>,
    private var myJsonStatusCode: Int = 200,
    private val errorStatusCodeBefore: (code: Int) -> Boolean = { false }   // 处理状态码  true 拦截事件   false 不拦截事件
) : Observer<T> {


    private var error = false
    private lateinit var t: T

    /**
     * 开始订阅时
     * 开始加载
     *
     * @since 2.0
     */
    final override fun onSubscribe(d: Disposable) {
        error = false
        retrofitObserverInterface.onRetrofitSubscribe(d)
    }

    /**
     * 成功获取数据
     *
     * @param t
     */
    final override fun onNext(t: T) {
        val errorCode =
            ReflectionUtils.getAnyValue(t, myJsonStatusProperty.name, Integer::class.java)
        if (errorCode == null || errorCode.toInt() != myJsonStatusCode) {
            error = true
            var errorCodeBefore = false
            if (errorCode != null){
                errorCodeBefore = errorStatusCodeBefore(errorCode.toInt())
            }
            retrofitObserverInterface.onRetrofitError(
                t,
                errorCodeBefore,
                Throwable("errorCode == null || errorCode.toInt() != myJsonStatusCode")
            )
            return
        }
        this.t = t
        retrofitObserverInterface.onRetrofitNext(t)
    }

    /**
     * 失败结束
     *
     * @param e
     */
    final override fun onError(e: Throwable) {
        retrofitObserverInterface.onRetrofitError(null, false, e)
    }

    /**
     * 成功结束
     *
     */
    final override fun onComplete() {
        if (error) return
        retrofitObserverInterface.onRetrofitComplete(t)
    }
}