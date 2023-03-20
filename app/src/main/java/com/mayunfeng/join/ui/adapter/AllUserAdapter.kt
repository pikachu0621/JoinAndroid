package com.mayunfeng.join.ui.adapter

import android.view.View
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ItemAllUserBinding
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
        binding.title.text = itemData.userName
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