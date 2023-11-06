package com.pkpk.join.bean

import java.io.Serializable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.bean
 * @Author:         pkpk.run
 * @Description:    null
 */
data class StartSignBean(
    val baseTag: Any?,
    val userId: Long,
    val signTime: Long,
    val signTimeRemaining: Long,
    val createTime: String,
    val groupId: Long,
    val id: Long,
    val signContent: String,
    val signKey: String,
    val signMap: String,
    val signTitle: String,
    val signType: Int, // 0 无密码打卡   1 签到码打卡    2 二维码打卡     3 手势打卡
    val signRatio: Float,
    val signExpire: Boolean,
    val signHaveCompletedPeople: Int,
    val signNotCompletedPeople: Int,
    val signAllPeople: Int,
    val userTable: UserLoginBean,
    val signGroupInfo: GroupBean?,
): Serializable