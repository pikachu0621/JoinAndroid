package com.pkpk.join.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pkpk.join.R
import com.pkpk.join.api.SignApi
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseBean
import com.pkpk.join.bean.StartSignBean
import com.pkpk.join.bean.UserLoginBean
import com.pkpk.join.databinding.ActivityStartSignBinding
import com.pkpk.join.ui.fragment.StartSignInfoFragment
import com.pkpk.join.ui.fragment.StartSignMyCreateGroupFragment
import com.pkpk.join.ui.fragment.StartSignTypeFragment
import com.pkpk.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.pkpk.join.utils.retrofit.QuickRtObserverListener
import com.pkpk.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.adapter.PagerAdapter

class StartSignActivity : AppBaseActivity<ActivityStartSignBinding, UserLoginBean>(),
    QuickRtObserverListener<BaseBean<StartSignBean>> {



    private val signApi: SignApi = RetrofitManager.getInstance().create(SignApi::class.java)

    private val startSignMyCreateGroupFragment: StartSignMyCreateGroupFragment = StartSignMyCreateGroupFragment.newInstance()
    private val startSignInfoFragment: StartSignInfoFragment = StartSignInfoFragment.newInstance()
    private val startSignTypeFragment: StartSignTypeFragment = StartSignTypeFragment.newInstance()

    override fun onAppCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initUi()
    }

    private fun initUi() {
        binding.vpr.offscreenPageLimit = 3
        binding.stepView.setSteps(arrayListOf<String>().apply {
            add(getString(R.string.start_sign_choose_group))
            add(getString(R.string.start_sign_fill_info))
            add(getString(R.string.start_sign_sign_type))
        })
        binding.vpr.adapter = PagerAdapter(supportFragmentManager, arrayListOf<Fragment>().apply {
            add(startSignMyCreateGroupFragment)
            add(startSignInfoFragment)
            add(startSignTypeFragment)
        })
        binding.join.setOnClickListener {
            if (binding.vpr.currentItem != 2) {
                binding.vpr.currentItem = ++ binding.vpr.currentItem
                return@setOnClickListener
            }
            //
            val groupId = startSignMyCreateGroupFragment.getGroupId()
            if ( groupId <= 0) {
                showToast(R.string.start_sign_not_group)
                binding.vpr.currentItem = 0
                return@setOnClickListener
            }

            signApi.sendStartSign(
                groupId,
                startSignInfoFragment.getTitle(),
                startSignInfoFragment.getContent(),
                startSignTypeFragment.getSingStartType().type,
                startSignTypeFragment.getPws(),
                startSignInfoFragment.getTime()
            ).mySubscribeMainThread(this@StartSignActivity, this)
            // showToast("加载${startSignInfoFragment.getTime()}")
        }




        binding.vpr.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                binding.stepView.go(position, true)
                binding.join.text = if (position == 2) getString(R.string.start_sign) else getString(R.string.start_sign_next_step)
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    override fun onSendError(t: BaseBean<StartSignBean>?, e: Throwable) {
        showToast(t?.reason ?: e.message)
    }


    //  todo 创建完成
    override fun onSendComplete(t: BaseBean<StartSignBean>) {
        finish()
        MyStartSignInfoActivity.startActivity(this, t.result!!.id, t.result!!.signExpire)
    }
}
