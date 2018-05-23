package com.xema.midas.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xema.midas.R;
import com.xema.midas.fragment.MyPageFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xema0 on 2018-05-23.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tl_main)
    TabLayout tlMain;
    @BindView(R.id.vp_main)
    ViewPager vpMain;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragment();
    }

    private void initFragment() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        vpMain.setAdapter(mSectionsPagerAdapter);
        tlMain.setupWithViewPager(vpMain);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        Fragment mMyPageFragment;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mMyPageFragment = new MyPageFragment();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    // TODO: 2018-05-23 리스트 화면
                    fragment = new Fragment();
                    break;
                case 1:
                    fragment = mMyPageFragment;
                    break;
                default:
                    fragment = new Fragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "리스트";
            else if (position == 1) return "마이페이지";
            else return "";
        }
    }
}
