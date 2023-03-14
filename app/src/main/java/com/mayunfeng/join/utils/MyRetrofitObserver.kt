package com.mayunfeng.join.utils

import android.app.Activity
import com.mayunfeng.join.HTTP_OK
import com.mayunfeng.join.R
import com.mayunfeng.join.TOKEN_ERROR_CODE
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitObserver
import com.mayunfeng.join.utils.retrofit.RtObserverListener
import com.pikachu.utils.utils.ToastUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils
 * @Author:         pkpk.run
 * @Description:    null
 */
class MyRetrofitObserver<T : Any>(activity: Activity, retrofitObserver: RtObserverListener<T>) :
    RetrofitObserver<T>(retrofitObserver, BaseBean<Any>::error_code, HTTP_OK, {
        if (it == TOKEN_ERROR_CODE) {
            UserUtils.loginTokenOut(activity)
            ToastUtils.showToast(activity.getString(R.string.login_user_token_failure))
        }
        it == TOKEN_ERROR_CODE
    }){

        companion object{
            fun <T: Any> Observable<T>.mySubscribeMainThread(activity: Activity,
                                                           quick: QuickRtObserverListener<T>) {
                this.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .retry(1)  //请求失败重连次数
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(MyRetrofitObserver(activity, quick))
            }
        }
    }