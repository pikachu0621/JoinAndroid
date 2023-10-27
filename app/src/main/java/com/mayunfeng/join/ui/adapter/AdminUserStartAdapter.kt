package com.mayunfeng.join.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.mayunfeng.join.R
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.bean.StartSignBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.bean.UserSignTable
import com.mayunfeng.join.databinding.ItemMySignInfoBinding
import com.mayunfeng.join.ui.activity.GroupInfoActivity
import com.mayunfeng.join.ui.activity.UserInfoActivity
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.base.BaseActivity
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.TimeUtils

class AdminUserStartAdapter(
    private val clickItem: (itemData: UserSignTable) -> Unit,
    private val clickUser: (itemData: UserLoginBean) -> Unit,
    private val clickGroup: (itemData: GroupBean) -> Unit
): QuickAdapter<ItemMySignInfoBinding, UserSignTable>(null) {

    @SuppressLint("SetTextI18n")
    override fun onQuickBindView(
        binding: ItemMySignInfoBinding,
        itemData: UserSignTable,
        position: Int,
        data: MutableList<UserSignTable>
    ) {
        val startSignInfo = itemData.startSignInfo
        val signGroupInfo = itemData.startSignInfo.signGroupInfo
        val userTable = itemData.startSignInfo.userTable
        val myUserTable = itemData.userTable

        binding.title.text = startSignInfo.signTitle
        binding.time.text = TimeUtils.strToStr(startSignInfo.createTime, "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm")
        binding.content.text = startSignInfo.signContent

        GlideUtils.with(context).loadHeaderToken(userTable.userImg).into(binding.sendUserImg)
        binding.userNickname.text = "${context.resources.getString(R.string.start_sign_initiator_user)}：${userTable.userNickname}"
        binding.userNickname.setOnClickListener { clickUser(userTable) }
        if (signGroupInfo == null) {
            binding.sendGroupImg.visibility = View.GONE
            binding.groupName.text = context.resources.getString(R.string.start_sign_group_nul)
            binding.groupName.setTextColor(context.resources.getColor(R.color.color_main_top6))
            binding.groupName.setOnClickListener {}
        } else {
            binding.sendGroupImg.visibility = View.VISIBLE
            GlideUtils.with(context).loadHeaderToken(signGroupInfo.groupImg)
                .into(binding.sendGroupImg)
            binding.groupName.text = "${context.resources.getString(R.string.start_sign_initiator_group)}：${signGroupInfo.groupName}"
            binding.groupName.setTextColor(context.resources.getColor(R.color.color_grey1))
            binding.groupName.setOnClickListener { clickGroup(signGroupInfo) }
        }
        when (startSignInfo.signType) {
            0 -> binding.type.setImageResource(R.drawable.ic_start_sign_nul_psw)
            1 -> binding.type.setImageResource(R.drawable.ic_start_sign_num_psw)
            2 -> binding.type.setImageResource(R.drawable.ic_start_sign_qrc)
            3 -> binding.type.setImageResource(R.drawable.ic_start_sign_psw_gesture)
        }
        binding.root.setOnClickListener { clickItem(itemData) }
    }


}