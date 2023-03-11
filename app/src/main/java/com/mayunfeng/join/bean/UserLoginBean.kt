package com.mayunfeng.join.bean

import java.io.Serializable

data class UserLoginBean(
    var baseTag: Any,
    var createTime: String,
    var id: Int,
    var loginToken: String,
    var updateTime: String,
    var userAccount: String,
    var userAge: Int,
    var userGrade: Int,
    var userImg: String,
    var userIntroduce: String,
    var userLimit: Boolean,
    var userName: String,
    var userPassword: String,
    var userSex: Boolean,
    var userUnit: String
): Serializable