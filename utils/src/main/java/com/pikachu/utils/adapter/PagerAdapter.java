package com.pikachu.utils.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author pkpk.run
 * @project 通用工程
 * @package com.pikachu.utils.adapter
 * @date 2021/9/8
 * @description 略
 */
public class PagerAdapter extends FragmentPagerAdapter {



    private final List<Fragment> fragments;
    private final List<String> tab;

    public PagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        this(fm, fragments,null);
    }

    public PagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, List<String> tab) {
        super(fm);
        this.fragments = fragments;
        this.tab = tab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tab != null)
            return tab.get(position);
        return super.getPageTitle(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) { }
}
