package com.pkpk.join.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.View
import com.pkpk.join.R
import com.pkpk.join.databinding.ItemDrawerNavBinding
import com.pkpk.join.ui.activity.*
import com.pikachu.utils.adapter.QuickAdapter

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */

data class MainDrawerItemData(
    val iconR: Int,
    val title: Int,
    val activityClz: Class<out Activity>? = null,
    val partition: Int? = null
) {
    companion object {
        fun addUserItem(isRootUser: Boolean): List<MainDrawerItemData> =
            arrayListOf<MainDrawerItemData>().apply {
                add(MainDrawerItemData(0, 0, partition =  R.string.drawer_list_group_user))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_start, R.string.drawer_list_group_user_sign_in, AdminUserStartActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_add_group, R.string.drawer_list_group_user_join_group, MyJoinGroupActivity::class.java))
                // add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_info, R.string.drawer_list_group_user_historical_record, SignInfoListActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_msg, R.string.drawer_list_group_user_info, AdminMsgActivity::class.java))
                if (!isRootUser) return@apply
                add(MainDrawerItemData(0, 0, partition = R.string.drawer_list_group_creator))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_add_group, R.string.drawer_list_group_creator_group, CreateGroupActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_group, R.string.drawer_list_group_creator_my_group, MyCreateGroupActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_start, R.string.drawer_list_group_creator_start_sign, StartSignActivity::class.java))
                // add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_group, R.string.drawer_list_group_creator_my_ok_group, StartSignActivity::class.java))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_review, R.string.drawer_list_group_creator_sign_info, SignInfoListActivity::class.java))
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
        binding.appCompatTextView4.text = if (itemData.title == 0) "" else context.resources.getString(itemData.title)
        itemData.partition ?. let {
            binding.conPartition.text = context.resources.getString(it)
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