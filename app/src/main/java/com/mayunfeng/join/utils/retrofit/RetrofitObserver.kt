package com.mayunfeng.join.utils.retrofit

import com.mayunfeng.join.HTTP_OK
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.utils.ReflectionUtils
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.reflect.Field
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
    private val myJsonStatusProperty: KCallable<Int>,
    private var myJsonStatusCode: Int = 200
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
        onRetrofitSubscribe(d)
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
            onRetrofitError(
                t,
                Throwable("errorCode == null || errorCode.toInt() != myJsonStatusCode")
            )
            error = true
            return
        }
        this.t = t
        onRetrofitNext(t)
    }

    /**
     * 失败结束
     *
     * @param e
     */
    final override fun onError(e: Throwable) {
        onRetrofitError(null, e)
    }

    /**
     * 成功结束
     *
     */
    final override fun onComplete() {
        if (error) return
        onRetrofitComplete(t)
    }


    abstract fun onRetrofitSubscribe(d: Disposable)

    abstract fun onRetrofitNext(t: T)

    abstract fun onRetrofitError(t: T?, e: Throwable)

    abstract fun onRetrofitComplete(t: T)

}