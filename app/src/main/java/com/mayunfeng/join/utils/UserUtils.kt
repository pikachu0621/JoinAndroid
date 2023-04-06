package com.mayunfeng.join.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import com.mayunfeng.join.Application
import com.mayunfeng.join.TOKEN_ERROR_KEY
import com.mayunfeng.join.service.WebSocketService
import com.mayunfeng.join.ui.activity.LoginActivity
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.utils.AESBCBUtils
import com.pikachu.utils.utils.AppManagerUtils
import com.pikachu.utils.utils.GlideUtils
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


    fun loginTokenOut(activity: Activity){
        writeLoginToken("")
        RetrofitManager.getInstance().addHeader(TOKEN_ERROR_KEY, "")
        LoginActivity.startLoginActivity(activity)
        AppManagerUtils.getAppManager().finishAllActivity(activity.javaClass, LoginActivity::class.java)
        Application.isLoginOk = false
        WebSocketService.getWebSocketService()?.cancel()
        activity.stopService(Intent(activity, WebSocketService::class.java))
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


    fun copyStr(context: Context, str: String){
        val myClipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        myClipboard.setPrimaryClip(ClipData.newPlainText("content", str))
    }

    fun encryptGroupId(groupId: Long): String{
        return  AESBCBUtils.bytesToHexStr("id:$groupId".toByteArray(Charsets.UTF_8))
        // return AESBCBUtils.encrypt("$groupId") ?: ""
    }


    fun decryptGroupId(groupIdAes: String): Long {
        val decrypt = AESBCBUtils.hexStrToBytes(groupIdAes).toString(Charsets.UTF_8) ?: return -1
        return try {
            decrypt.substring(decrypt.indexOf(":") + 1).toLong()
        } catch (e: Exception) {
            -1
        }
    }

}