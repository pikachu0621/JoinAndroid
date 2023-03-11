package com.mayunfeng.join.utils

import com.pikachu.utils.utils.SharedPreferencesUtils

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

}