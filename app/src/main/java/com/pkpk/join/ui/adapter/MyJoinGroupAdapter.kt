package com.pkpk.join.ui.adapter

import android.annotation.SuppressLint
import com.pkpk.join.R
import com.pkpk.join.bean.GroupBean
import com.pkpk.join.databinding.ItemMyJoinBinding
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.TimeUtils

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.ui.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class MyJoinGroupAdapter(private val clickItem:(itemData: GroupBean) -> Unit,
                         `data`: MutableList<GroupBean>? = null):
    QuickAdapter<ItemMyJoinBinding, GroupBean>(`data`) {
    @SuppressLint("SetTextI18n")
    override fun onQuickBindView(
        binding: ItemMyJoinBinding,
        itemData: GroupBean,
        position: Int,
        data: MutableList<GroupBean>,
    ) {
        GlideUtils.with(context)
            .loadHeaderToken(itemData.groupImg)
            .into(binding.groupImage)
        binding.title.text = itemData.groupName
        binding.tvAdmin.text = itemData.groupType
        binding.people.text = "${ itemData.groupPeople }${context.resources.getString(R.string.group_info_people)}"
        binding.time.text = TimeUtils.strToStr(itemData.createTime, "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm")
        binding.root.setOnClickListener { clickItem(itemData) }
    }
}