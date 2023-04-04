package com.mayunfeng.join.ui.adapter

import com.mayunfeng.join.R
import com.mayunfeng.join.bean.StartSignBean
import com.mayunfeng.join.bean.UserSignTable
import com.mayunfeng.join.databinding.ItemSignUserBinding
import com.mayunfeng.join.databinding.UiItemPhoto1Binding
import com.mayunfeng.join.ui.fragment.MyStartSignUserFragment
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.LogsUtils

class MyStartSignUserAdapter(private val clickItem: (itemData: UserSignTable) -> Unit): QuickAdapter<ItemSignUserBinding, UserSignTable>(null) {

    private var isExpire: Boolean = false

    fun setIsExpire(isExpire: Boolean){
        this.isExpire = isExpire
        refresh()
    }

    override fun onQuickBindView(
        binding: ItemSignUserBinding,
        itemData: UserSignTable,
        position: Int,
        data: MutableList<UserSignTable>
    ) {
       GlideUtils.with(context).loadHeaderToken(itemData.userTable.userImg).into(binding.groupImage)
        binding.ff.text = itemData.userTable.userName

        val color1 = context.resources.getColor(R.color.color_main_top1)
        val color2 = context.resources.getColor(R.color.color_main_top3)
        val color3 = context.resources.getColor(R.color.color_main_top6)
        val drawable1 = context.resources.getDrawable(R.drawable.ic_sign_user_complete)
        val drawable2 = context.resources.getDrawable(R.drawable.ic_sign_user_wait)
        val drawable3 = context.resources.getDrawable(R.drawable.ic_sign_user_incomplete)

        if (itemData.signComplete){
            binding.groupImage.borderColor = color1
            binding.ff.setTextColor(color1)
            binding.state.setImageDrawable(drawable1)
        } else if (isExpire) {
            // 未完成 并且已过期
            binding.groupImage.borderColor = color3
            binding.ff.setTextColor(color3)
            binding.state.setImageDrawable(drawable3)
        } else {
            binding.groupImage.borderColor = color2
            binding.ff.setTextColor(color2)
            binding.state.setImageDrawable(drawable2)
        }
        binding.root.setOnClickListener { clickItem(itemData) }
    }


}