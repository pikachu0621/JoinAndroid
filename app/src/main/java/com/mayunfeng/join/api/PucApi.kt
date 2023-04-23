package com.mayunfeng.join.api

import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.bean.UserLoginBean
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.api
 * @Author:         pkpk.run
 * @Description:    null
 */
interface PucApi {


    /**
     * 获取组类型
     *
     * @return {@linkplain Observable<BaseBean<Array<String>>>}
     */
    @GET("/myf-puc-api/puc-group-type")
    fun sendGroupType(): Observable<BaseBean<Array<String>>>





    // todo b8bf3c230a63bd35
    @Multipart
    @POST("/myf-puc-api/b8bf3c230a63bd35")
    fun sendFile(
        @Part file: MultipartBody.Part,
    ): Observable<BaseBean<String>>


}