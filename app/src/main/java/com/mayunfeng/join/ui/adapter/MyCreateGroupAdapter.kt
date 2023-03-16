package com.mayunfeng.join.ui.adapter

import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.databinding.ItemMyCreateBinding
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.TimeUtils

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.ui.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class MyCreateGroupAdapter(`data`: MutableList<GroupBean>? = null) :
    QuickAdapter<ItemMyCreateBinding, GroupBean>(`data`) {
    override fun onQuickBindView(
        binding: ItemMyCreateBinding,
        itemData: GroupBean,
        position: Int,
        data: MutableList<GroupBean>,
    ) {
        GlideUtils.with(context)
            .loadHeaderToken(itemData.groupImg)
            .into(binding.groupImage)
        binding.title.text = itemData.groupName
        binding.tvAdmin.text = itemData.groupType
        binding.people.text = "${ itemData.groupPeople }人"
        binding.time.text = TimeUtils.strToStr(itemData.createTime, "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm")
    }
}