package com.mayunfeng.join.api

import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.UserLoginBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.api
 * @Author:         pkpk.run
 * @Description:    null
 */
interface UserApi {


    // 登录/注册
    @FormUrlEncoded
    @POST("/myf-user-api/login-user")
    fun login(
        @Field("account") account: String?,
        @Field("password") password: String?
    ): Observable<BaseBean<UserLoginBean>>


    // 根据token 获取用户数据
    @GET("/myf-user-api/user-info-token")
    fun userInfo(
        @Query("token") account: String?
    ): Observable<BaseBean<UserLoginBean>>


}