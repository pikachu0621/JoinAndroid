package com.mayunfeng.join.bean

import java.io.Serializable

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
    var userName: String,
    var userSex: Boolean,
    var userUnit: String
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
                "")
    }
}