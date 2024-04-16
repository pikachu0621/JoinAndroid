package com.pkpk.join.api

import com.pkpk.join.API_JOIN_GROUP
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.GroupBean
import com.pkpk.join.bean.LGroupBean
import com.pkpk.join.bean.UserLoginBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.api
 * @Author:         pkpk.run
 * @Description:    null
 */
interface JoinGroupApi {

    /**
     * 加入组
     * @param groupId 组id
     */
    @GET("$API_JOIN_GROUP/join-group")
    fun sendJoinGroup(@Query("groupId") groupId: Long, @Query("groupVerify") groupVerifyCode: String?): Observable<BaseBean<GroupBean>>


    /**
     * 退出组
     * @param groupId 组id
     */
    @GET("$API_JOIN_GROUP/out-group/{groupId}")
    fun sendOutGroup(@Path("groupId") groupId: Long): Observable<BaseBean<String>>


    /**
     * 获取我加入的组
     */
    @GET("$API_JOIN_GROUP/user-join")
    fun sendMyJoinGroup(): Observable<BaseBean<ArrayList<GroupBean>>>


    /**
     * 获取某个组的所有人
     * @param groupId 组id
     *
     */
    @GET("$API_JOIN_GROUP/group-all-user/{groupId}")
    fun queryJoinGroupAllUser(@Path("groupId") groupId: Long): Observable<BaseBean<LGroupBean<ArrayList<UserLoginBean>>>>

}