package com.mayunfeng.join.ui.adapter

import android.annotation.SuppressLint
import com.mayunfeng.join.R
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.databinding.ItemMyCreateBinding
import com.mayunfeng.join.databinding.ItemStartSignGroupBinding
import com.mayunfeng.join.utils.UserUtils
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
class SignMyCreateGroupAdapter(private val clickItem:(itemData: GroupBean) -> Unit,
                               `data`: MutableList<GroupBean>? = null):
    QuickAdapter<ItemStartSignGroupBinding, GroupBean>(`data`) {

    private var chooseDataId = -1L

    @SuppressLint("SetTextI18n")
    override fun onQuickBindView(
        binding: ItemStartSignGroupBinding,
        itemData: GroupBean,
        position: Int,
        data: MutableList<GroupBean>,
    ) {
        GlideUtils.with(context)
            .loadHeaderToken(itemData.groupImg)
            .into(binding.groupImage)
        binding.radioButton.isChecked = chooseDataId == itemData.id
        binding.title.text = itemData.groupName
        binding.tvAdmin.text = itemData.groupType
        binding.id.text = "ID:${UserUtils.encryptGroupId(itemData.id)}"
        binding.people.text = "${ itemData.groupPeople }${context.resources.getString(R.string.group_info_people)}"
        binding.time.text = TimeUtils.strToStr(itemData.createTime, "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm")
        binding.root.setOnClickListener {
            clickItem(itemData)
            chooseDataId = itemData.id
            refresh()
        }
    }


    fun getChooseGroupId(): Long = chooseDataId

}