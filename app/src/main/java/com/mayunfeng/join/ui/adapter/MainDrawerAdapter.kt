package com.mayunfeng.join.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.View
import com.mayunfeng.join.R
import com.mayunfeng.join.databinding.ItemDrawerNavBinding
import com.mayunfeng.join.ui.activity.*
import com.pikachu.utils.adapter.QuickAdapter

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */

data class MainDrawerItemData(
    val iconR: Int,
    val title: String,
    val activityClz: Class<out Activity>? = null,
    val partition: String? = null
) {
    companion object {
        fun addUserItem(isRootUser: Boolean): List<MainDrawerItemData> =
            arrayListOf<MainDrawerItemData>().apply {
                add(MainDrawerItemData(0, "", partition = "组员功能"))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_start, "当前签到", AdminUserStartActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_add_group, "我加入的组", MyJoinGroupActivity::class.java))
                // add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_info, "历史记录", SignInfoListActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_msg, "我的信息", AdminMsgActivity::class.java))
                if (!isRootUser) return@apply
                add(MainDrawerItemData(0, "", partition = "创建者功能"))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_add_group, "创建组", CreateGroupActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_group, "我创建的组", MyCreateGroupActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_start, "发起签到", StartSignActivity::class.java))
                // add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_group, "已创建的", StartSignActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_review, "组员签到信息", SignInfoListActivity::class.java))
                // add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_info, "签到信息/批阅签到", StartSignActivity::class.java))
            }
    }
}

class MainDrawerAdapter(isRootUser: Boolean) :
    QuickAdapter<ItemDrawerNavBinding, MainDrawerItemData>(
        MainDrawerItemData.addUserItem(isRootUser)
    ) {
    override fun onQuickBindView(
        binding: ItemDrawerNavBinding,
        itemData: MainDrawerItemData,
        position: Int,
        data: MutableList<MainDrawerItemData>
    ) {
        binding.appCompatImageView.setImageResource(itemData.iconR)
        binding.appCompatTextView4.text = itemData.title
        itemData.partition ?. let {
            binding.conPartition.text = it
            binding.conClick.visibility = View.GONE
            binding.conPartition.visibility = View.VISIBLE
        } ?: let {
            binding.conClick.visibility = View.VISIBLE
            binding.conPartition.visibility = View.GONE
        }
        binding.conClick.setOnClickListener {
            if (itemData.activityClz != null) {
                context.startActivity(Intent(context, itemData.activityClz))
            }
        }
    }

}