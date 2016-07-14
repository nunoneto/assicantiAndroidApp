package com.nunoneto.assicanti.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class OptionalsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private String[] pageTitles;

    public OptionalsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public OptionalsPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] pageTitles) {
        super(fm);
        this.fragments = fragments;
        this.pageTitles = pageTitles;
    }

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
        return pageTitles[position];
    }
}
