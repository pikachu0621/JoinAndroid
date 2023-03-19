package com.mayunfeng.join.ui.adapter

import android.view.View
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ItemGroupInfoUserBinding
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.utils.GlideUtils


class GroupInfoUserAdapter(
    private val clickUser: ( itemData: UserLoginBean) -> Unit,
    private val clickMore: () -> Unit
) : QuickAdapter<ItemGroupInfoUserBinding, UserLoginBean>(null) {
    private var w: Float = 0F

    fun setItemWide(w: Int){
        this.w = w / 5F
        refresh()
    }

    override fun setData(`data`: MutableList<UserLoginBean>) {
        if(`data`.size > 4)
            refreshData(`data`.subList(0, 4))
        `data`.add(UserLoginBean.createNull())
        refreshData(`data`)
    }


    override fun onQuickBindView(
        binding: ItemGroupInfoUserBinding,
        itemData: UserLoginBean,
        position: Int,
        `data`: MutableList<UserLoginBean>
    ) {
        binding.root.layoutParams.width = w.toInt()

        if (position == `data`.size - 1){
            binding.more.visibility = View.VISIBLE
            binding.my.visibility = View.GONE
            binding.root.setOnClickListener { clickMore() }
            return
        }
        binding.root.setOnClickListener { clickUser(itemData) }
        binding.more.visibility = View.GONE
        binding.my.visibility = View.VISIBLE
        GlideUtils.with(context).loadHeaderToken(itemData.userImg).into(binding.groupUser4)
        binding.userName.text = itemData.userName

    }

}