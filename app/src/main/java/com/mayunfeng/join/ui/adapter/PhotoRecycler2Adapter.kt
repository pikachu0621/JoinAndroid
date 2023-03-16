package com.mayunfeng.join.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target
import com.mayunfeng.join.R
import com.mayunfeng.join.databinding.UiItemPhoto2Binding
import com.pikachu.utils.adapter.QuickAdapter
import com.pikachu.utils.photo.GetPhotoUtils
import com.pikachu.utils.photo.PhotoModule

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.adapter
 * @Author:         pkpk.run
 * @Description:    null
 */
class PhotoRecycler2Adapter(
    data: List<PhotoModule?>?,
    @GetPhotoUtils.Type type: Int,
    context: Context,
    onItemClickListener: OnItemClickListener,
) : QuickAdapter<UiItemPhoto2Binding, PhotoModule?>(data) {
    private val onItemClickListener: OnItemClickListener
    private val type: Int
    private val color: Int
    private val colorBg: Int
    private var position = 0

    interface OnItemClickListener {
        fun onItemClick(
            binding: UiItemPhoto2Binding,
            itemData: PhotoModule,
            position: Int,
            data: MutableList<PhotoModule?>,
        )
    }

    init {
        this.type = type
        this.context = context
        this.onItemClickListener = onItemClickListener
        color = context.resources.getColor(R.color.color_secondary)
        colorBg = context.resources.getColor(R.color.color_bg)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onQuickBindView(
        binding: UiItemPhoto2Binding,
        itemData: PhotoModule,
        position: Int,
        `data`: MutableList<PhotoModule?>,
    ) {
        if (type == GetPhotoUtils.Type.Audio) {
            Glide.with(context)
                .load(R.drawable.ic_photo_music)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .into(binding.p9)
        } else {
            Glide.with(context)
                .load(itemData.file)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .into(binding.p9)
        }
        binding.root.setBackgroundColor(if (position == this.position) color else colorBg)
        binding.p10.text = itemData.name + " (" + itemData.files.size + ")"
        binding.root.setOnClickListener { v ->
            this.position = position
            v.setBackgroundColor(colorBg)
            onItemClickListener.onItemClick(binding, itemData, position, `data`)
            notifyDataSetChanged()
        }
    }
}
