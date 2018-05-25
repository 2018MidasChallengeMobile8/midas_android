package com.xema.midas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.xema.midas.R;
import com.xema.midas.fragment.MyPageFragment;
import com.xema.midas.fragment.PostFragment;

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
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragment();
        initListeners();
    }

    private void initFragment() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        vpMain.setAdapter(mSectionsPagerAdapter);
        tlMain.setupWithViewPager(vpMain);
    }

    private void initListeners() {
        ivAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, PostUploadActivity.class);
            startActivity(intent);
        });
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
                    fragment = new PostFragment();
                    break;
                case 1:
                    fragment = new Fragment();
                    break;
                case 2:
                    fragment = new Fragment();
                    break;
                case 3:
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
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "HOME";
            else if (position == 1) return "GALLERY";
            else if (position == 2) return "CHART";
            else if (position == 3) return "MYPAGE";
            else return "";
        }
    }
}
