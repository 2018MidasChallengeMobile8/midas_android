package com.xema.midas.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;

import com.xema.midas.fragment.BlankFragment;

public class ListPageAdapter extends FragmentStatePagerAdapter {
    public ListPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new ListFragment();
            case 1:
                return new BlankFragment();
            default:
                return null;

        }
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "리스트";
            case 1:
                return "마이페이지";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
