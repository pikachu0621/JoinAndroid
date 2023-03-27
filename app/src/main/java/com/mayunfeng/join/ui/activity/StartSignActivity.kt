package com.mayunfeng.join.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.mayunfeng.join.R
import com.mayunfeng.join.api.JoinGroupApi
import com.mayunfeng.join.api.SignApi
import com.mayunfeng.join.base.AppBaseActivity
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.bean.StartSignBean
import com.mayunfeng.join.bean.UserLoginBean
import com.mayunfeng.join.databinding.ActivityStartSignBinding
import com.mayunfeng.join.ui.fragment.StartSignInfoFragment
import com.mayunfeng.join.ui.fragment.StartSignMyCreateGroupFragment
import com.mayunfeng.join.ui.fragment.StartSignTypeFragment
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.pikachu.utils.adapter.PagerAdapter
import com.shuhart.stepview.StepView

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
                showToast("未选择组")
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
                binding.join.text = if (position == 2){
                    getString(R.string.start_sign)
                } else {
                    getString(R.string.start_sign_next_step)
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    override fun onSendError(t: BaseBean<StartSignBean>?, e: Throwable) {
        showToast(t?.reason ?: e.message)
    }


    //  todo 创建完成
    override fun onSendComplete(t: BaseBean<StartSignBean>) {

    }
}
