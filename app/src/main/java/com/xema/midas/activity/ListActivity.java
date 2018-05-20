package com.xema.midas.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.xema.midas.R;
import com.xema.midas.adapter.ListPageAdapter;
import com.xema.midas.databinding.ActivityListBinding;

public class ListActivity extends AppCompatActivity {


    private ActivityListBinding binding;
    private PagerAdapter pagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_list);
        binding.setList(this);

        pagerAdapter = new ListPageAdapter(getSupportFragmentManager());
        binding.listViewpager.setAdapter(pagerAdapter);
        /*binding.listTab.addTab(binding.listTab.newTab().setText("one"));
        binding.listTab.addTab(binding.listTab.newTab().setText("two"));*/
        binding.listTab.setupWithViewPager(binding.listViewpager);
        binding.listViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.listTab));

        //setRecyclerView();
    }


}
