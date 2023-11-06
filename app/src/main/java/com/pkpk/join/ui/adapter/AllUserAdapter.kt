package com.pkpk.join.ui.adapter

import android.view.View
import com.pkpk.join.bean.UserLoginBean
import com.pkpk.join.databinding.ItemAllUserBinding
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.utils.GlideUtils

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.ui.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class AllUserAdapter(private val clickItem:(itemData: UserLoginBean) -> Unit,
                     private val clickMore:(itemData: UserLoginBean) -> Unit,
                     `data`: MutableList<UserLoginBean>? = null):
    QuickAdapter<ItemAllUserBinding, UserLoginBean>(`data`) {
    private var isCreateUser = false

    /**
     * 是否开启编辑
     */
    fun setOpenEdit(isCreateUser: Boolean){
        this.isCreateUser = isCreateUser
        refresh()
    }

    override fun onQuickBindView(
        binding: ItemAllUserBinding,
        itemData: UserLoginBean,
        position: Int,
        data: MutableList<UserLoginBean>,
    ) {
        GlideUtils.with(context)
            .loadHeaderToken(itemData.userImg)
            .into(binding.userImage)
        binding.title.text = itemData.userNickname
        binding.tvAdmin.visibility =  if (position == 0) View.VISIBLE else View.GONE
        binding.root.setOnClickListener { clickItem(itemData) }
        if (isCreateUser && position != 0) {
            binding.more.visibility = View.VISIBLE
            binding.more.setOnClickListener { clickMore(itemData) }
            return
        }
        binding.more.visibility = View.GONE
    }
}