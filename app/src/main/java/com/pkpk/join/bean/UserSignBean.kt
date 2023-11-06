package com.pkpk.join.bean

import java.io.Serializable

data class UserSignBean(
    val startSignTable: StartSignBean,
    val userSignTable: ArrayList<UserSignTable>
): Serializable

data class UserSignTable(
    val baseTag: Any,
    val createTime: String,
    val id: Long,
    val signComplete: Boolean,
    val signId: Long,
    val signTime: String,
    val userId: Long,
    val userTable: UserLoginBean,
    val startSignInfo: StartSignBean
): Serializable
