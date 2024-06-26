package com.pkpk.join.api

import com.pkpk.join.API_USER
import com.pkpk.join.TOKEN_ERROR_KEY
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.UserLoginBean
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.api
 * @Author:         pkpk.run
 * @Description:    用户API
 */
interface UserApi {


    /**
     * 登录/注册
     *
     * @param account 用户账号
     * @param password 密码
     *
     * @return {@linkplain BaseBean<UserLoginBean>}
     */
    @FormUrlEncoded
    @POST("$API_USER/login-user")
    fun sendLogin(
        @Field("account") account: String?,
        @Field("password") password: String?
    ): Observable<BaseBean<UserLoginBean>>


    /**
     * 根据token 获取用户数据
     *
     * @param token token
     *
     * @return BaseBean<UserLoginBean>
     */
    @GET("$API_USER/user-info")
    fun sendUserInfo(@Header(TOKEN_ERROR_KEY) token: String): Observable<BaseBean<UserLoginBean>>





    /**
     * 修改头像
     *
     * @param img 用户头像
     *
     * @return {@linkplain BaseBean<UserLoginBean>}
     */
    @Multipart
    @POST("$API_USER/edit-img")
    fun sendEditImage(
        @Part img: MultipartBody.Part?,  // 不带其他参数的
    ): Observable<BaseBean<UserLoginBean>>




    /**
     * 修改 用户名字
     *
     * 需要token
     * @param nickname nickname
     *
     * @return BaseBean<UserLoginBean>
     */
    @GET("$API_USER/edit-nickname")
    fun sendEditName(@Query("nickname") nickname: String?): Observable<BaseBean<UserLoginBean>>


    /**
     * 修改用户信息开放 null 自动更改
     *
     * @param isOpen 是否开放
     */
    @GET("$API_USER/edit-open")
    fun sendEditOpen(@Query("open") isOpen: Boolean? = null): Observable<BaseBean<UserLoginBean>>



    /**
     * 修改 性别
     *
     * 需要token
     * @param sex false = 女  true = 男
     *
     * @return BaseBean<UserLoginBean>
     */
    @GET("$API_USER/edit-sex")
    fun sendEditSex(@Query("sex") sex: Boolean?): Observable<BaseBean<UserLoginBean>>


    /**
     * 修改 性别年龄
     *
     * 需要token
     * @param birth 出生日期  yyyy-MM-dd
     *
     * @return BaseBean<UserLoginBean>
     */
    @GET("$API_USER/edit-birth")
    fun sendEditBirth(@Query("birth") birth: String?): Observable<BaseBean<UserLoginBean>>


    /**
     * 修改 单位
     *
     * 需要token
     * @param unit 个性签名
     *
     * @return BaseBean<UserLoginBean>
     */
    @GET("$API_USER/edit-unit")
    fun sendEditUnit(@Query("unit") unit: String?): Observable<BaseBean<UserLoginBean>>


    /**
     * 修改 个性签名
     * 需要token
     * @param ird 个性签名
     *
     * @return BaseBean<UserLoginBean>
     */
    @GET("$API_USER/edit-ird")
    fun sendEditIrd(@Query("ird") ird: String?): Observable<BaseBean<UserLoginBean>>


    /**
     * 修改 密码
     *
     * 需要token
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     *
     * @return BaseBean<UserLoginBean>
     */
    @FormUrlEncoded
    @POST("$API_USER/edit-password")
    fun sendEditPassword(
        @Field("old-password") oldPassword: String?,
        @Field("new-password") newPassword: String?
    ): Observable<BaseBean<UserLoginBean>>


    @GET("$API_USER/out-login")
    fun outLogin(): Observable<BaseBean<Boolean>>
}