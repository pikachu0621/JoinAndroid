package com.mayunfeng.join.ui.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.king.zxing.util.CodeUtils
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.GroupBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityQrcodeDisplayBinding
import com.mayunfeng.join.utils.UserUtils
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.GlideUtils
import java.io.Serializable
import java.sql.Timestamp
import kotlin.random.Random

/**
 * 需要传 group id
 */
class QRCodeDisplayActivity : AppBaseActivity<ActivityQrcodeDisplayBinding, Serializable>() {

    private var group: GroupBean? = null
    override fun onAppCreate(savedInstanceState: Bundle?) {
        group = getSerializableExtra(JumpType.J0, GroupBean::class.java)
        if (group == null) finish()
        binding.qrcImg.post {
            binding.qrcImg.setImageBitmap(CodeUtils.createQRCode(createQrStr(group!!.id), binding.qrcImg.width))
        }
        binding.appCompatTextView9.text = group!!.groupName
        binding.userName.text = "ID: ${UserUtils.encryptGroupId(group!!.id)}"
        GlideUtils.with(context).loadHeaderToken(group!!.groupImg).into(binding.userImage)
    }

    companion object{
        fun createQrStr(id: Long): String{
            val t = (Timestamp(System.currentTimeMillis()).time / 1000).toString()
            val r = Random.nextInt(100001, 200000).toString()
            return "$id#$t$r"
        }

        fun parseQrStr(str: String?): Long {
            if (str.isNullOrEmpty()) return 0
            if (!str.contains("#")) return 0
            return  try {
                str.substring(0, str.indexOf("#")).toLong()
            } catch (e: Exception){ 0 }
        }

        fun startQRCodeDisplayActivity(activity: Activity, groupBean: GroupBean) {
            activity.startActivity(Intent(activity, QRCodeDisplayActivity::class.java).apply {
                putExtra(JumpType.J0, groupBean)
            })
        }
    }
}