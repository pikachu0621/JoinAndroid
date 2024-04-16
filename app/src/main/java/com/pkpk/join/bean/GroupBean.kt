package com.pkpk.join.bean

import java.io.Serializable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.bean
 * @Author:         pkpk.run
 * @Description:    null
 */
data class GroupBean(
    val id: Long,
    val userId: Long,
    val groupImg: String,
    val groupIntroduce: String,
    val groupName: String,
    val groupType: String,
    val createTime: String,
    val groupPeople: Int,
    val groupVerifyPws: String?,
    val groupIsSearch: Boolean,
    val baseTag: Any?,
    val groupTopFourPeople: List<UserLoginBean>,
    val groupAndUser: Int,
    val groupIsVerify: Boolean,
): Serializable


data class LGroupBean<T>(
   val groupUserIsFounder: Boolean = false,
   val result: T?
): Serializable