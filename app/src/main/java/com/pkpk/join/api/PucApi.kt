package com.pkpk.join.api

import com.pkpk.join.API_PUBLIC
import com.pkpk.join.bean.BaseBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.api
 * @Author:         pkpk.run
 * @Description:    null
 */
interface PucApi {


    /**
     * 获取组类型
     *
     * @return {@linkplain Observable<BaseBean<Array<String>>>}
     */
    @GET("$API_PUBLIC/puc-group-type")
    fun sendGroupType(): Observable<BaseBean<Array<String>>>



}