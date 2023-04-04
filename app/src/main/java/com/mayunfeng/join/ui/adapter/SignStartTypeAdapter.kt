package com.mayunfeng.join.ui.adapter

import com.mayunfeng.join.R
import com.mayunfeng.join.databinding.ItemStartSignTypeBinding
import com.pikachu.utils.adapter.QuickAdapter

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.ui.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */

enum class SingStartType(
    val type: Int
){
    PASSWORD_NOT(0),
    PASSWORD_NUM(1),
    PASSWORD_QRC(2),
    PASSWORD_GESTURE(3)
}

data class SignStartTypeBean(
    val title: Int,
    val icon: Int,
    val type: SingStartType,
    var choose: Boolean = false
)
class SignStartTypeAdapter(
    private val clickItem:(itemData: SignStartTypeBean) -> Boolean,
): QuickAdapter<ItemStartSignTypeBinding, SignStartTypeBean>(arrayListOf<SignStartTypeBean>().apply {
    add(SignStartTypeBean(R.string.start_sign_type_nul_pws, R.drawable.ic_start_sign_nul_psw, SingStartType.PASSWORD_NOT, true))
    add(SignStartTypeBean(R.string.start_sign_type_num_pws, R.drawable.ic_start_sign_num_psw, SingStartType.PASSWORD_NUM))
    add(SignStartTypeBean(R.string.start_sign_type_sqc_pws, R.drawable.ic_start_sign_qrc, SingStartType.PASSWORD_QRC))
    add(SignStartTypeBean(R.string.start_sign_type_gesture_pws, R.drawable.ic_start_sign_psw_gesture, SingStartType.PASSWORD_GESTURE))
}) {

    override fun onQuickBindView(
        binding: ItemStartSignTypeBinding,
        itemData: SignStartTypeBean,
        position: Int,
        `data`: MutableList<SignStartTypeBean>,
    ) {
        binding.appCompatImageView4.setImageResource(itemData.icon)
        binding.ff.text = context.resources.getString(itemData.title)
        binding.radioButton.isChecked = itemData.choose
        binding.root.setOnClickListener {
            if (clickItem(itemData)) {
                `data`.forEach { it.choose = false }
                itemData.choose = true
                refresh()
            }
        }
    }

    fun setSingStartType(type: SingStartType?){
        type?:return
        data.forEach {
            it.choose = it.type == type
        }
        refresh()
    }

}