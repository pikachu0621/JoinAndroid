package com.mayunfeng.join.bean

data class UserLoginBean(
    val baseTag: Any,
    val createTime: String,
    val id: Int,
    val loginToken: String,
    val updateTime: String,
    val userAccount: String,
    val userAge: Int,
    val userGrade: Int,
    val userImg: String,
    val userIntroduce: String,
    val userLimit: Boolean,
    val userName: String,
    val userPassword: String,
    val userSex: Boolean,
    val userUnit: String
)