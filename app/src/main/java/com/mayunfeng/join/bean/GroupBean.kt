package com.mayunfeng.join.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.bean
 * @Author:         pkpk.run
 * @Description:    null
 */
data class GroupBean(
    val id: Long,
    val userId: Int,
    val groupImg: String,
    val groupIntroduce: String,
    val groupName: String,
    val groupType: String,
    val createTime: String,
    val groupPeople: Int,
    val baseTag: Any?,
    val groupTopFourPeople: List<UserLoginBean>,
    val groupAndUser: Int
): Serializable


data class LGroupBean<T>(
   val groupUserIsFounder: Boolean = false,
   val result: T?
): Serializable