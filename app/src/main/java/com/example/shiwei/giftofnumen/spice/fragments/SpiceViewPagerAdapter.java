package com.example.shiwei.giftofnumen.spice.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiwei on 2016/6/27.
 */
public class SpiceViewPagerAdapter extends FragmentPagerAdapter {
    private List<String> title=new ArrayList<>();
    private List<Fragment> fragments=new ArrayList<>();
    public SpiceViewPagerAdapter(FragmentManager fm, List<String> title, List<Fragment> fragmentPager) {
        super(fm);
        this.fragments=fragmentPager;
        this.title=title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
