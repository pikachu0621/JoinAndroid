package com.pkpk.join

// 192.168.0.105
// 42.192.221.73


// 42.192.221.73
// 192.168.31.20
// 127.0.0.1
// 服务端 ip 地址
const val BASE_ADDRESS = "42.192.221.73"
// 端口
const val BASE_PORT = "8012"

const val BASE_HTTP = "http"
const val BASE_WS = "ws"
const val BASE_HTTP_URL = "${BASE_HTTP}://${BASE_ADDRESS}:${BASE_PORT}"
const val BASE_WS_URL = "${BASE_WS}://${BASE_ADDRESS}:${BASE_PORT}"
const val HTTP_OK = 200
const val TOKEN_ERROR_CODE = -3
const val TOKEN_ERROR_KEY = "token"
const val AES_PASSWORD = "pkpk"
const val DEVICE_INFO_PARAMETER = "device"
const val PREFIX = "pk"
//====================================================================================================================//
const val API_GROUP = "/${PREFIX}-group-api"
const val API_JOIN_GROUP = "/${PREFIX}-join-group-api"
const val API_PUBLIC = "/${PREFIX}-puc-api"
const val API_START_SIGN = "/${PREFIX}-sign-api"
const val API_USER = "/${PREFIX}-user-api"
const val API_USER_SIGN = "/${PREFIX}-user-sign-api"
//====================================================================================================================//