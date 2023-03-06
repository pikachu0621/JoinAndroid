package com.mayunfeng.join.adapter

import com.mayunfeng.join.databinding.ItemTestBinding
import com.pikachu.utils.adapter.QuickAdapter

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class TestAdapter(`data`: MutableList<String> ? = MutableList(20) { "20" } ) : QuickAdapter<ItemTestBinding, String>(`data`) {
    override fun onQuickBindView(
        binding: ItemTestBinding,
        itemData: String,
        position: Int,
        `data`: MutableList<String>
    ) {

    }
}