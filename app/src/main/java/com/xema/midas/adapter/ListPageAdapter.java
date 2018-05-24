package com.xema.midas.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xema.midas.fragment.BlankFragment;
import com.xema.midas.fragment.ListFragment;

public class ListPageAdapter extends FragmentPagerAdapter {
     /*   List<Fragment> listFragment;

    public ListPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.listFragment = list;
    }

    @Override
    public Fragment getItem(int position) {
        return  listFragment.get(position);
    }


    @Override
    public int getCount() {
        return listFragment.size();
    }*/

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
