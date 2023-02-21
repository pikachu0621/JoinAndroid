package com.mayunfeng.adminuser.adapter

import com.mayunfeng.adminuser.databinding.ItemTestBinding
import com.pikachu.utils.adapter.QuickAdapter

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.adminuser.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class TestAdapter(`data`: MutableList<String> ? = MutableList(20) { "20" } ) : QuickAdapter<ItemTestBinding, String>(`data`) {
    override fun onQuickBindView(
        binding: ItemTestBinding,
        itemData: String?,
        position: Int,
        `data`: MutableList<String>
    ) {

    }
}