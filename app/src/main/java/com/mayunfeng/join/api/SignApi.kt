package com.mayunfeng.join.api

import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.StartSignBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


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
    fun sendStartSign(@Field("group-id") groupId : Long,
                      @Field("sign-title") signTitle: String?,
                      @Field("sign-content") signContent: String?,
                      @Field("sign-type") signType: Int,
                      @Field("sign-key") signKey: String?,
                      @Field("sign-time") signTime: Long): Observable<BaseBean<StartSignBean>>


    /**
     * 获取全部签到数据
     *
     */
    @GET("/myf-sign-api/all-info")
    fun sendAllInfo(): Observable<BaseBean<ArrayList<StartSignBean>>>





}