package com.pkpk.join.utils

import android.app.Activity
import com.pkpk.join.HTTP_OK
import com.pkpk.join.R
import com.pkpk.join.TOKEN_ERROR_CODE
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.ui.dialog.LoadingDialog
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitInterceptor
import com.pkpk.join.utils.retrofit.RetrofitObserver
import com.pkpk.join.utils.retrofit.RtObserverListener
import com.pikachu.utils.utils.ToastUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.utils
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
        fun <E : Any> Observable<E>.mySubscribeMainThread(
            activity: Activity,
            quick: QuickRtObserverListener<E>,
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