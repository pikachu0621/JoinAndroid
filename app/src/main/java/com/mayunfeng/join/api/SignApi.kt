package com.mayunfeng.join.api

import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.StartSignBean
import com.mayunfeng.join.bean.UserSignBean
import com.mayunfeng.join.bean.UserSignTable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*


/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.api
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
    @POST("/myf-sign-api/start")
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
    @GET("/myf-sign-api/all-info")
    fun sendAllInfo(): Observable<BaseBean<ArrayList<StartSignBean>>>


    /**
     * 删除签到
     * @param signId
     */
    @GET("/myf-sign-api/delete-sign/{signId}")
    fun sendDelSign(@Path("signId") signId: Long): Observable<BaseBean<ArrayList<StartSignBean>>>


    ///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *  获取全部用户签到数据
     * @param signId
     */
    @GET("/myf-user-sign-api/all-info/{signId}")
    fun sendAllUserInfo(@Path("signId") signId: Long): Observable<BaseBean<UserSignBean>>


    /**
     * 获取 用户签到数据
     */
    @GET("/myf-user-sign-api/my-sign-info")
    fun sendMySignInfo(): Observable<BaseBean<ArrayList<UserSignTable>>>


    /**
     * 用户签到
     */
    @GET("/myf-user-sign-api/start-sign")
    fun sendStartSign(
        @Query("sign-id") signId: Long,
        @Query("key") key: String? = null
    ): Observable<BaseBean<Boolean>>

}