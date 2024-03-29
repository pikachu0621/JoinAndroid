package com.pkpk.join.bean

import java.io.Serializable


/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.bean
 * @Author:         pkpk.run
 * @Description:    null
 */
data class UserLoginBean(
    var baseTag: Any?,
    var createTime: String,
    var id: Long,
    var loginToken: String?,
    var userAccount: String,
    var userAge: Int,
    var userBirth: String,
    var userGrade: Int,
    var userImg: String,
    var userIntroduce: String,
    var userNickname: String,
    var userSex: Boolean,
    var userUnit: String,
    var userOpenProfile: Boolean
) : Serializable {
    companion object {
        fun createNull(): UserLoginBean =
            UserLoginBean(
                null,
                "",
                0,
                "",
                "",
                0,
                "",
                0,
                "",
                "",
                "",
                false,
                "",
                true
            )
    }
}