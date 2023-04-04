package com.mayunfeng.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.mayunfeng.join.R
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.databinding.ActivityMyStartSignInfoBinding
import com.mayunfeng.join.ui.fragment.MyStartSignStatisticsFragment
import com.mayunfeng.join.ui.fragment.MyStartSignUserFragment
import com.mayunfeng.join.ui.fragment.StartSignInfoFragment
import com.mayunfeng.join.ui.fragment.StartSignTypeFragment
import com.pikachu.utils.adapter.PagerAdapter
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.UiUtils

/**
 * 我发起的签到详情
 */
class MyStartSignInfoActivity : AppBaseActivity<ActivityMyStartSignInfoBinding, String>() {

    private lateinit var myStartSignUserFragment: MyStartSignUserFragment
    private lateinit var  myStartSignStatisticsFragment: MyStartSignStatisticsFragment
    private var signId = -1L
    private var signExpire: Boolean = false

        companion object {
        fun startActivity(activity: Activity, cloudSignId: Long = -1, signExpire: Boolean) {
            activity.startActivity(Intent(activity, MyStartSignInfoActivity::class.java).apply {
                putExtra(JumpType.J0, cloudSignId)
                putExtra(JumpType.J1, signExpire)
            })
        }
    }

    override fun onAppCreate(savedInstanceState: Bundle?) {
        signId = getLongExtra(JumpType.J0, -1)
        signExpire = getBooleanExtra(JumpType.J1, false)
        if (signId < 0) return finish()
        myStartSignUserFragment = MyStartSignUserFragment.newInstance(signId, signExpire)
        myStartSignStatisticsFragment = MyStartSignStatisticsFragment.newInstance(signId, signExpire)
        initUi()
    }



    private fun initUi() {
        binding.vp.adapter = PagerAdapter(supportFragmentManager, arrayListOf<Fragment>().apply {
            add(myStartSignUserFragment)
            add(myStartSignStatisticsFragment)
        })
        binding.vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.user.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    binding.user.setTextColor( resources.getColor(R.color.color_grey1) )
                    binding.user.textSize = 15F

                    binding.info.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    binding.info.setTextColor( resources.getColor(R.color.color_grey2) )
                    binding.info.textSize = 13F
                } else if (position == 1){
                    binding.info.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    binding.info.setTextColor( resources.getColor(R.color.color_grey1) )
                    binding.info.textSize =15F

                    binding.user.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    binding.user.setTextColor( resources.getColor(R.color.color_grey2) )
                    binding.user.textSize =13F
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    override fun onDestroy() {
        myStartSignUserFragment.onDestroy()
        myStartSignStatisticsFragment.onDestroy()
        super.onDestroy()
    }

}