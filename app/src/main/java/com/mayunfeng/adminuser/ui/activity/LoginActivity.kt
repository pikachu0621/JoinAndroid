package com.mayunfeng.adminuser.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.pikachu.utils.adapter.PagerAdapter2
import com.mayunfeng.adminuser.base.AppBaseActivity
import com.mayunfeng.adminuser.databinding.ActivityLoginBinding
import com.mayunfeng.adminuser.ui.fragment.LoginEmailFragment
import com.mayunfeng.adminuser.ui.fragment.LoginUserFragment

class LoginActivity : AppBaseActivity<ActivityLoginBinding>() {


    override fun onAppCreate(savedInstanceState: Bundle?) {
        vp2 = binding.vpPager
        binding.vpPager.adapter = PagerAdapter2(this, arrayListOf<Fragment>().apply {
            add(LoginUserFragment.newInstance())
            add(LoginEmailFragment.newInstance())
        })
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ( keyCode == KeyEvent.KEYCODE_BACK && binding.vpPager.currentItem == 1) {
            binding.vpPager.currentItem = 0
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    companion object {
        private var vp2: ViewPager2? = null
        fun setCurrentItem(item: Int) {
            vp2?.currentItem = item
        }
    }
}