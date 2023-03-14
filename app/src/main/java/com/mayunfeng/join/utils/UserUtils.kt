package com.mayunfeng.join.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.mayunfeng.join.TOKEN_ERROR_KEY
import com.mayunfeng.join.ui.activity.LoginActivity
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.AppManagerUtils
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.SharedPreferencesUtils
import kotlin.math.log

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils
 * @Author:         pkpk.run
 * @Description:    null
 */
object UserUtils {

    fun writeUserAccount(userAccount: String) =
        SharedPreferencesUtils.write("user_account", userAccount)

    fun writeLoginToken(loginToken: String) =
        SharedPreferencesUtils.write("login_token", loginToken)

    fun readUserAccount(): String? = SharedPreferencesUtils.readString("user_account")

    fun readLoginToken(): String? = SharedPreferencesUtils.readString("login_token")


    fun loginTokenOut(activity: Activity){
        writeLoginToken("")
        RetrofitManager.getInstance().addHeader(TOKEN_ERROR_KEY, "")
        LoginActivity.startLoginActivity(activity)
        AppManagerUtils.getAppManager().finishAllActivity(activity.javaClass, LoginActivity::class.java)
        // context.startActivity(Intent(context, LoginActivity::class.java))
    }


    fun loginTokenInit(loginToken: String?){
        var token: String? = loginToken
        if (loginToken == null){
            token = readLoginToken()
        }
        if (token == null) return
        writeLoginToken(token)
        RetrofitManager.getInstance().addHeader(TOKEN_ERROR_KEY, token)
        GlideUtils.initToken(TOKEN_ERROR_KEY, token)
    }


}