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
    val id: Int,
    val signContent: String,
    val signKey: String,
    val signMap: String,
    val signTime: Int,
    val signTitle: String,
    val signType: Int,
    val userId: Int
): Serializable