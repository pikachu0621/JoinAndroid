package com.mayunfeng.adminuser.adapter

import android.view.View
import com.mayunfeng.adminuser.R
import com.mayunfeng.adminuser.databinding.ItemDrawerNavBinding
import com.pikachu.utils.adapter.QuickAdapter

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */

data class MainDrawerItemData(
    val iconR: Int,
    val title: String,
    val partition: String? = null
) {
    companion object {
        fun addUserItem(isRootUser: Boolean): List<MainDrawerItemData> =
            arrayListOf<MainDrawerItemData>().apply {
                add(MainDrawerItemData(0, "", "我的"))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_start, "当前签到"))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_group, "我的组"))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_add_group, "加入组"))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_info, "历史记录"))
                add(MainDrawerItemData(R.drawable.ic_drawer_user_sign_msg, "我的信息"))
                if (!isRootUser) return@apply
                add(MainDrawerItemData(0, "", "管理员"))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_start, "发起签到"))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_add_group, "创建组"))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_group, "已创建的组"))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_review, "批阅签到"))
                add(MainDrawerItemData(R.drawable.ic_drawer_root_user_sign_info, "签到信息"))
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
    }

}