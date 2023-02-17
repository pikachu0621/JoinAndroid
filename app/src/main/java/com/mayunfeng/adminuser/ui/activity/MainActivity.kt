package com.mayunfeng.adminuser.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.pikachu.utils.adapter.PagerAdapter2
import com.mayunfeng.adminuser.base.AppBaseActivity
import com.mayunfeng.adminuser.databinding.ActivityMainBinding
import com.mayunfeng.adminuser.ui.fragment.AppFragment
import com.mayunfeng.adminuser.ui.fragment.IndexFragment
import com.mayunfeng.adminuser.ui.fragment.MeFragment

class MainActivity : AppBaseActivity<ActivityMainBinding>() {


    override fun onAppCreate(savedInstanceState: Bundle?) {
        initNavigationFragment()
    }

    private fun initNavigationFragment() {
        binding.incNavigation.clItem1.setOnClickListener {
            binding.vpPager.currentItem = 0
        }
        binding.incNavigation.clItem2.setOnClickListener {
            binding.vpPager.currentItem = 1
        }
        binding.incNavigation.clItem3.setOnClickListener {
            binding.vpPager.currentItem = 2
        }

        binding.vpPager.adapter = PagerAdapter2(
            this,
            arrayListOf<Fragment>().apply {
                add(IndexFragment.newInstance())
                add(AppFragment.newInstance())
                add(MeFragment.newInstance())
            })

        binding.vpPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.incNavigation.cbItem1.isChecked = false
                binding.incNavigation.cbItem2.isChecked = false
                binding.incNavigation.cbItem3.isChecked = false
                binding.incNavigation.tvItem1.isChecked = false
                binding.incNavigation.tvItem2.isChecked = false
                binding.incNavigation.tvItem3.isChecked = false
                /*binding.incNavigation.tvItem1.visibility = View.GONE
                binding.incNavigation.tvItem2.visibility = View.GONE
                binding.incNavigation.tvItem3.visibility = View.GONE*/
                when (position){
                    0 ->{
                        binding.incNavigation.tvItem1.isChecked = true
                        binding.incNavigation.cbItem1.isChecked = true
                        // binding.incNavigation.tvItem1.visibility = View.VISIBLE
                    }
                    1 -> {
                        binding.incNavigation.tvItem2.isChecked = true
                        binding.incNavigation.cbItem2.isChecked = true
                        // binding.incNavigation.tvItem2.visibility = View.VISIBLE
                    }
                    2 ->{
                        binding.incNavigation.tvItem3.isChecked = true
                        binding.incNavigation.cbItem3.isChecked = true
                       // binding.incNavigation.tvItem3.visibility = View.VISIBLE
                    }
                }
            }
        })

    }


}