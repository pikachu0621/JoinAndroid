package com.mayunfeng.adminuser.mode

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.mode
 * @Author:         pkpk.run
 * @Description:    null
 */
data class MainMsgData(
    val titleStr: String, // 标题
    val timeStr: String,  // 时间
    val contentStr: String, // 内容
    val clickStr: String = "前往签到", // 点击按键文字
    val clickType: Int = 0, // 点击按键类型   0=签到   1=加入组   2=其他
) {
    companion object {
        fun getTestData() = arrayListOf<MainMsgData>().apply {
            add(MainMsgData("大学英语-2-加入组", "2023-03-01 13:20", "@你 <大学英语-2> 邀你加入组。", "前往查看", 1))
            for (i in 0..20) {
                add(MainMsgData("大学英语-2-签到", "2023-03-01 13:20", "你所在的组<大学英语-2>发起了签到，请尽快完成，以免漏签。"))
            }
        }
    }
}