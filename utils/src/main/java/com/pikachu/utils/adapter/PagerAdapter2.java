package com.pikachu.utils.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * @author pkpk.run
 * @project 通用工程
 * @package com.pikachu.utils.adapter
 * @date 2021/9/8
 * @description 适用于很多的Fragment 类似列表
 *
 * viewPager2
 * Tab 用 new TabLayoutMediator()
 *  tabLayoutMediator.attach();
 *
 *
 *
 *
 */
public class PagerAdapter2 extends FragmentStateAdapter {



    private final List<Fragment> fragments;

    public PagerAdapter2(@NonNull  FragmentManager fragmentManager, @NonNull  Lifecycle lifecycle,
                         List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }

    public PagerAdapter2(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    public PagerAdapter2(@NonNull Fragment fragment, List<Fragment> fragments) {
        super(fragment);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragments != null)
            return fragments.get(position);
        return new Fragment();
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
