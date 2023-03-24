package com.mayunfeng.join.api

import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.bean.LGroupBean
import com.mayunfeng.join.bean.UserLoginBean
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.api
 * @Author:         pkpk.run
 * @Description:    null
 */
interface GroupApi {

    /**
     * 创建组
     *
     * @return {@linkplain Observable<BaseBean<Array<String>>>}
     */
    @POST("/myf-group-api/create")
    fun sendCreateGroup(
        @Body body: RequestBody // 带其他参数的    不要 @Multipart
    ): Observable<BaseBean<GroupBean>>


    /**
     * 获取用户创建的群组
     */
    @GET("/myf-group-api/user-create-group")
    fun sendUserCreateGroup(): Observable<BaseBean<ArrayList<GroupBean>>>




    /**
     * 删除群组
     */
    @GET("/myf-group-api/delete-group/{id}")
    fun sendDeleteGroup(@Path("id") id: Long): Observable<BaseBean<ArrayList<GroupBean>>>




    /**
     * 编辑群组
     */
    @POST("/myf-group-api/edit-group")
    fun sendEditUserGroup(
        @Body body: RequestBody // 带其他参数的    不要 @Multipart
    ): Observable<BaseBean<GroupBean>>



    /**
     * 查询群组info
     */
    @GET("/myf-group-api/query-group/{id}")
    fun sendQueryGroupInfo(@Path("id") id: Long): Observable<BaseBean<GroupBean>>



    /**
     * 踢出某用户
     */
    @GET("/myf-group-api/remove-user-group")
    fun removeUserToGroup(
        @Query("user-id") targetUserId: Long?,
        @Query("group-id")  byGroupId: Long?
    ): Observable<BaseBean<LGroupBean<ArrayList<UserLoginBean>>>>


    /**
     * 模糊查询群组info
     */
    @GET("/myf-group-api/like-group")
    fun sendLikeGroupList(@Query("id-name") groupNameAndGroupId: String): Observable<BaseBean<ArrayList<GroupBean>>>


}