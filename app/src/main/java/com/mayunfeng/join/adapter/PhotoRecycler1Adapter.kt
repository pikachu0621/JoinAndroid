package com.mayunfeng.join.adapter

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target
import com.mayunfeng.join.R
import com.mayunfeng.join.databinding.UiItemPhoto1Binding
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.photo.GetPhotoUtils
import com.pikachu.utils.utils.UiUtils
import java.io.File

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class PhotoRecycler1Adapter(
    data: List<String?>?,
    @GetPhotoUtils.Type type: Int,
    activity: Activity,
    onItemClickListener: OnItemClickListener,
) : QuickAdapter<UiItemPhoto1Binding, String?>(data) {
    private val activity: Activity
    private val onItemClickListener: OnItemClickListener
    private val type: Int
    private var screenWidth: Int
    private var borderW: Int
    private var maxNum = 2 // 最大选择
    private var has: MutableList<String> = ArrayList()

     interface OnItemClickListener {
        fun onItemClick(has: MutableList<String>, position: Int, num: Int)
    }

    init {
        this.type = type
        this.activity = activity
        this.onItemClickListener = onItemClickListener
        screenWidth = UiUtils.getScreenWidth(activity) / 4
        borderW = 0
    }

    override fun onQuickBindView(
        binding: UiItemPhoto1Binding,
        itemData: String,
        position: Int,
        data: List<String?>,
    ) {
        binding.root.layoutParams = (binding.root.layoutParams as GridLayoutManager.LayoutParams).apply {
            height = screenWidth
            leftMargin = borderW
            rightMargin = borderW
            topMargin = borderW
            bottomMargin = borderW
        }
        if (type == GetPhotoUtils.Type.Audio) {
            Glide.with(context)
                .load(R.drawable.ic_photo_music)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .into(binding.p7)
            binding.p82.visibility = View.VISIBLE
            binding.p82.text = File(itemData).name
        } else {
            Glide.with(context)
                .load(itemData)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .into(binding.p7)
            binding.p82.visibility = View.GONE
        }
        binding.p8.isChecked = false
        binding.p81.visibility = View.GONE
        for (fl in has) {
            if (fl == itemData) {
                binding.p8.isChecked = true
                binding.p81.visibility = View.VISIBLE
                break
            }
        }
        binding.root.setOnClickListener {
            if (isSelected(itemData)) {
                has.remove(itemData)
            } else {
                if (has.size >= maxNum) {
                    has.removeAt(0)
                }
                has.add(itemData)
            }
            onItemClickListener.onItemClick(has, position, has.size)
            refresh()
        }
    }

    private fun isSelected(file: String): Boolean {
        for (fl in has) {
            if (fl == file) {
                return true
            }
        }
        return false
    }

    // 获取已选中几个
    fun getSelectedNum(): Int {
        return has.size
    }

    fun getHas(): List<String> {
        return has
    }

    fun setHas(has: MutableList<String>) {
        this.has = has
    }

    fun setHas() {
        has.clear()
    }

    // 设置最大选择几个
    fun setMaxNum(maxNum: Int) {
        this.maxNum = maxNum
    }

    //一行几个
    fun setMaxW(maxW: Int) {
        screenWidth = UiUtils.getScreenWidth(activity) / maxW - borderW * maxW
    }

    fun setBorderW(borderDp: Int) {
        borderW = UiUtils.dp2px(activity, borderDp.toFloat())
    }
}
