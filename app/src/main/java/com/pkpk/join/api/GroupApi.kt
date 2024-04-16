package com.pkpk.join.api

import com.pkpk.join.API_GROUP
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.GroupBean
import com.pkpk.join.bean.LGroupBean
import com.pkpk.join.bean.UserLoginBean
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.api
 * @Author:         pkpk.run
 * @Description:    null
 */
interface GroupApi {

    /**
     * 创建组
     *
     * @return {@linkplain Observable<BaseBean<Array<String>>>}
     */
    @POST("$API_GROUP/create")
    fun sendCreateGroup(
        @Body body: RequestBody // 带其他参数的    不要 @Multipart
    ): Observable<BaseBean<GroupBean>>


    /**
     * 获取用户创建的群组
     */
    @GET("$API_GROUP/user-create-group")
    fun sendUserCreateGroup(): Observable<BaseBean<ArrayList<GroupBean>>>




    /**
     * 删除群组
     */
    @GET("$API_GROUP/delete-group/{id}")
    fun sendDeleteGroup(@Path("id") id: Long): Observable<BaseBean<ArrayList<GroupBean>>>




    /**
     * 编辑群组
     */
    @POST("$API_GROUP/edit-group")
    fun sendEditUserGroup(
        @Body body: RequestBody // 带其他参数的    不要 @Multipart
    ): Observable<BaseBean<GroupBean>>



    /**
     * 查询群组info
     */
    @GET("$API_GROUP/query-group/{id}")
    fun sendQueryGroupInfo(@Path("id") id: Long): Observable<BaseBean<GroupBean>>



    /**
     * 踢出某用户
     */
    @GET("$API_GROUP/remove-user-group")
    fun removeUserToGroup(
        @Query("user-id") targetUserId: Long?,
        @Query("group-id")  byGroupId: Long?
    ): Observable<BaseBean<LGroupBean<ArrayList<UserLoginBean>>>>


    /**
     * 模糊查询群组info
     */
    @GET("$API_GROUP/like-group")
    fun sendLikeGroupList(@Query("id-name") groupNameAndGroupId: String): Observable<BaseBean<ArrayList<GroupBean>>>


}