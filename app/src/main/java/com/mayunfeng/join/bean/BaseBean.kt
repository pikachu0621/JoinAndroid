package com.mayunfeng.join.bean

import com.google.gson.annotations.Expose
import java.io.Serializable

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.bean
 * @Author:         pkpk.run
 * @Description:    null
 */
data class BaseBean<T>(
    val reason: String,
    val error_code: Int,
    var result: T?
): Serializable