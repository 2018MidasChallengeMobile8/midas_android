package com.xema.midas.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xema.midas.R;
import com.xema.midas.adapter.ListCardAdapter;
import com.xema.midas.databinding.FragmentListBinding;
import com.xema.midas.model.ListCardItems;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    private RecyclerView.Adapter mAdapter;
    private RequestManager mRequestManager;
    private ArrayList<ListCardItems> listCardItems;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("my_home","onCreateView()");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list,container,false);
        //이미지뷰 둥글게 하기
        binding.setFmlist(this);
        setRecyclerView();
        return binding.getRoot();
    }

    private void setRecyclerView()
    {
        setList();
        mRequestManager = Glide.with(this);
        mAdapter = new ListCardAdapter(listCardItems,mRequestManager);
        binding.listRecycle.setHasFixedSize(true);

        binding.listRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.listRecycle.setAdapter(mAdapter);
    }

    private void setList()
    {
        listCardItems.clear();
        listCardItems = new ArrayList<>();

        listCardItems.add(new ListCardItems("제목제목제목","내용내용니용","2018-01-01",
                "http://cfile23.uf.tistory.com/image/2657B9505809B4B634FF66"));
        listCardItems.add(new ListCardItems("제목제목제목","내용내용니용","2018-01-01",
                "http://cfile23.uf.tistory.com/image/2657B9505809B4B634FF66"));
        mAdapter.notifyDataSetChanged();
    }
}
