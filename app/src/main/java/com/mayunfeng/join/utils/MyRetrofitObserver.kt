package com.mayunfeng.join.utils

import android.app.Activity
import com.mayunfeng.join.HTTP_OK
import com.mayunfeng.join.R
import com.mayunfeng.join.TOKEN_ERROR_CODE
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.ui.dialog.LoadingDialog
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitInterceptor
import com.mayunfeng.join.utils.retrofit.RetrofitObserver
import com.mayunfeng.join.utils.retrofit.RtObserverListener
import com.pikachu.utils.utils.ToastUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils
 * @Author:         pkpk.run
 * @Description:    根据自己的业务 创建
 */
class MyRetrofitObserver<T : Any>(
    activity: Activity,
    retrofitObserver: RtObserverListener<T>,
    loadDialogTitle: Int = R.string.dialog_load_title, // -1 不启用 对话框
) : RetrofitObserver<T>(
    retrofitObserver,
    BaseBean<Any>::error_code,
    HTTP_OK,
    object : RetrofitInterceptor<T> {
        private var loadingDialog: LoadingDialog = LoadingDialog(
            activity,
            activity.getString(if (loadDialogTitle == -1) R.string.login_user_id else loadDialogTitle)
        )

        override fun onRetrofitSubscribe(d: Disposable): Boolean {
            if (loadDialogTitle != -1) loadingDialog.show()
            return false
        }

        override fun onRetrofitCode(code: Int?): Boolean {
            if (loadDialogTitle != -1) loadingDialog.dismiss()
            if (code != null && code == TOKEN_ERROR_CODE) {
                UserUtils.loginTokenOut(activity)
                ToastUtils.showToast(activity.getString(R.string.login_user_token_failure))
                return true
            }
            return false
        }

        override fun onRetrofitNext(t: T): Boolean {
            if (loadDialogTitle != -1) loadingDialog.dismiss()
            return false
        }

        override fun onRetrofitError(t: T?, e: Throwable): Boolean {
            if (loadDialogTitle != -1) loadingDialog.dismiss()
            return false
        }

        override fun onRetrofitComplete(t: T): Boolean {
            if (loadDialogTitle != -1) loadingDialog.dismiss()
            return false
        }
    }) {

    companion object {
        fun <T : Any> Observable<T>.mySubscribeMainThread(
            activity: Activity,
            quick: QuickRtObserverListener<T>,
            loadDialogTitle: Int = R.string.dialog_load_title, // -1 不启用
        ) {
            this.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retry(1)  //请求失败重连次数
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MyRetrofitObserver(activity, quick, loadDialogTitle))
        }
    }
}