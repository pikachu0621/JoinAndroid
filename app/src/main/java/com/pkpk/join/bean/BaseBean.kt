package com.pkpk.join.bean

import java.io.Serializable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.bean
 * @Author:         pkpk.run
 * @Description:    null
 */
data class BaseBean<T>(
    val reason: String,
    val error_code: Int,
    var result: T?
): Serializable


data class BaseEventBean<T>(
    var ben: T?,
    val key: Int?,
    val msg: String?,
    val tag: Class<*>?
): Serializable