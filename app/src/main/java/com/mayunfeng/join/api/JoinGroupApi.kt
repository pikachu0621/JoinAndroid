package com.mayunfeng.join.api

import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.bean.LGroupBean
import com.mayunfeng.join.bean.UserLoginBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.api
 * @Author:         pkpk.run
 * @Description:    null
 */
interface JoinGroupApi {

    /**
     * 加入组
     * @param groupId 组id
     */
    @GET("/myf-join-group-api/join-group/{groupId}")
    fun sendJoinGroup(@Path("groupId") groupId: Long): Observable<BaseBean<GroupBean>>


    /**
     * 退出组
     * @param groupId 组id
     */
    @GET("/myf-join-group-api/out-group/{groupId}")
    fun sendOutGroup(@Path("groupId") groupId: Long): Observable<BaseBean<String>>


    /**
     * 获取我加入的组
     */
    @GET("/myf-join-group-api/user-join")
    fun sendMyJoinGroup(): Observable<BaseBean<ArrayList<GroupBean>>>


    /**
     * 获取某个组的所有人
     * @param groupId 组id
     *
     */
    @GET("/myf-join-group-api/group-all-user/{groupId}")
    fun queryJoinGroupAllUser(@Path("groupId") groupId: Long): Observable<BaseBean<LGroupBean<ArrayList<UserLoginBean>>>>

}