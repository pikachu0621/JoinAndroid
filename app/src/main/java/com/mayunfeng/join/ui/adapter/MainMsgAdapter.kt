package com.mayunfeng.join.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import com.mayunfeng.join.R
import com.mayunfeng.join.bean.UserSignTable
import com.mayunfeng.join.databinding.ItemMainMsgBinding
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.utils.TimeUtils

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class MainMsgAdapter( private val clickItem: (itemData: UserSignTable) -> Unit) : QuickAdapter<ItemMainMsgBinding, UserSignTable>(null) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onQuickBindView(
        binding: ItemMainMsgBinding,
        itemData: UserSignTable,
        position: Int,
        `data`: MutableList<UserSignTable>
    ) {
        val startSignInfo = itemData.startSignInfo
        binding.title.text = startSignInfo.signTitle
        binding.time.text = TimeUtils.strToStr(startSignInfo.createTime, "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm")
        binding.content.text = startSignInfo.signContent
        if (itemData.signComplete){
            binding.state.setImageResource(R.drawable.ic_sign_user_complete)
            binding.stateBg.setBackgroundResource(R.drawable.dr_main_list_msg1_bg)
            binding.click.setTextColor(context.resources.getColor(R.color.color_main_top1))
            binding.click.text = context.resources.getString(R.string.start_sign_user_sign_ok)
            binding.root.setOnClickListener { }
        } else if (startSignInfo.signExpire) {
            // 未完成 并且已过期
            binding.state.setImageResource(R.drawable.ic_sign_user_incomplete)
            binding.stateBg.setBackgroundResource(R.drawable.dr_main_list_msg3_bg)
            binding.click.setTextColor(context.resources.getColor(R.color.color_main_top6))
            binding.click.text = context.resources.getString(R.string.start_sign_user_sign_expired)
            binding.root.setOnClickListener {}
        } else {
            binding.state.setImageResource(R.drawable.ic_sign_user_wait)
            binding.stateBg.setBackgroundResource(R.drawable.dr_main_list_msg2_bg)
            binding.click.setTextColor(context.resources.getColor(R.color.color_main_top3))
            binding.click.text = context.resources.getString(R.string.start_sign_user_sign_goto)
            binding.root.setOnClickListener { clickItem(itemData) }
        }
        binding.type.text = context.resources.getString(R.string.main_list_item_sign)
    }
}