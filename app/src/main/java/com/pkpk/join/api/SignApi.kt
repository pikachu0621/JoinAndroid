package com.pkpk.join.api

import com.pkpk.join.API_START_SIGN
import com.pkpk.join.API_USER_SIGN
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.StartSignBean
import com.pkpk.join.bean.UserSignBean
import com.pkpk.join.bean.UserSignTable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*


/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.api
 * @Author:         pkpk.run
 * @Description:    null
 */
interface SignApi {

    /**
     * 发起签到
     *
     * @param groupId       组的ID-在哪个组发起的
     * @param signTitle     标题
     * @param signContent   内容
     * @param signType      类型      [0-无密码打卡] [1-签到码打卡] [2-二维码打卡] [3-手势打卡]
     * @param signKey       密码      [-1 无密码]
     * @param signTime      有效时长   [-1 无时间限制] 单位秒(s)
     *
     */
    @FormUrlEncoded
    @POST("$API_START_SIGN/start")
    fun sendStartSign(
        @Field("group-id") groupId: Long,
        @Field("sign-title") signTitle: String?,
        @Field("sign-content") signContent: String?,
        @Field("sign-type") signType: Int,
        @Field("sign-key") signKey: String?,
        @Field("sign-time") signTime: Long
    ): Observable<BaseBean<StartSignBean>>


    /**
     * 获取全部签到数据
     *
     */
    @GET("$API_START_SIGN/all-info")
    fun sendAllInfo(): Observable<BaseBean<ArrayList<StartSignBean>>>


    /**
     * 删除签到
     * @param signId
     */
    @GET("$API_START_SIGN/delete-sign/{signId}")
    fun sendDelSign(@Path("signId") signId: Long): Observable<BaseBean<ArrayList<StartSignBean>>>


    ///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *  获取全部用户签到数据
     * @param signId
     */
    @GET("$API_USER_SIGN/all-info/{signId}")
    fun sendAllUserInfo(@Path("signId") signId: Long): Observable<BaseBean<UserSignBean>>


    /**
     * 获取 用户签到数据
     */
    @GET("$API_USER_SIGN/my-sign-info")
    fun sendMySignInfo(): Observable<BaseBean<ArrayList<UserSignTable>>>



    /**
     * 获取 该用户全部签到任务
     */
    @GET("$API_USER_SIGN/my-sign-all-info")
    fun sendMySignAllInfo(): Observable<BaseBean<ArrayList<UserSignTable>>>

    /**
     * 用户签到
     */
    @GET("$API_USER_SIGN/start-sign")
    fun sendStartSign(
        @Query("sign-id") signId: Long,
        @Query("key") key: String? = null
    ): Observable<BaseBean<Boolean>>

}