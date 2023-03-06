package com.mayunfeng.adminuser.adapter

import android.annotation.SuppressLint
import android.view.View
import com.mayunfeng.adminuser.R
import com.mayunfeng.adminuser.databinding.ItemDrawerNavBinding
import com.mayunfeng.adminuser.databinding.ItemMainMsgBinding
import com.mayunfeng.adminuser.mode.MainMsgData
import com.pikachu.utils.adapter.QuickAdapter

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class MainMsgAdapter(`data`: MutableList<MainMsgData>? = null) :
    QuickAdapter<ItemMainMsgBinding, MainMsgData>(`data`) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onQuickBindView(
        binding: ItemMainMsgBinding,
        itemData: MainMsgData,
        position: Int,
        `data`: MutableList<MainMsgData>
    ) {
        binding.title.text = itemData.titleStr
        binding.time.text = itemData.timeStr
        binding.content.text = itemData.contentStr
        binding.click.text = itemData.clickStr
        when(itemData.clickType){
            0 -> {
                binding.type.visibility = View.VISIBLE
                binding.type.text = context.resources.getString(R.string.main_list_item_sign)
                binding.type.background = context.resources.getDrawable(R.drawable.dr_main_sign_bg,null)
            }
            1 -> {
                binding.type.visibility = View.VISIBLE
                binding.type.text = context.resources.getString(R.string.main_list_item_join)
                binding.type.background = context.resources.getDrawable(R.drawable.dr_main_join_bg,null)
            }
            else -> {
                binding.type.visibility = View.GONE
            }
        }
    }
}