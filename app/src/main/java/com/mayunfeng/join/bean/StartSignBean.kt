package com.mayunfeng.join.bean

import java.io.Serializable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.bean
 * @Author:         pkpk.run
 * @Description:    null
 */
data class StartSignBean(
    val baseTag: Any?,
    val createTime: String,
    val groupId: Int,
    val id: Long,
    val signContent: String,
    val signKey: String,
    val signMap: String,
    val signTitle: String,
    val signType: Int,
    val signRatio: Float,
    val signExpire: Boolean,
    val signHaveCompletedPeople: Int,
    val signNotCompletedPeople: Int,
    val signAllPeople: Int,
    val signGroupInfo: GroupBean?,
): Serializable