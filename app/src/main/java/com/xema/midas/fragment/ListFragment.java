package com.xema.midas.fragment;

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
import com.xema.midas.model.Posts;
import com.xema.midas.network.RestClient;
import com.xema.midas.network.service.AccountService;

import java.util.ArrayList;

public class ListFragment extends android.support.v4.app.Fragment {
    private FragmentListBinding binding;
    private RecyclerView.Adapter mAdapter;
    private RequestManager mRequestManager;
    private ArrayList<ListCardItems> listCardItems;
    private ArrayList<Posts> post_list;
    private AccountService service;
    private RestClient<AccountService> restClient;


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
        mAdapter = new ListCardAdapter(post_list,mRequestManager);
        binding.listRecycle.setHasFixedSize(true);

        binding.listRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.listRecycle.setAdapter(mAdapter);
    }

    private void setList()
    {
//        listCardItems.clear();
        post_list = new ArrayList<>();
        post_list.add(new Posts("제목제목제목이다","내용내용이다","https://st.depositphotos.com/1029110/2779/v/950/depositphotos_27794673-stock-illustration-money-icons-set.jpg"));
        post_list.add(new Posts("제목제목제목이다","내용내용이다","https://st.depositphotos.com/1029110/2779/v/950/depositphotos_27794673-stock-illustration-money-icons-set.jpg"));
        post_list.add(new Posts("제목제목제목이다","내용내용이다","https://st.depositphotos.com/1029110/2779/v/950/depositphotos_27794673-stock-illustration-money-icons-set.jpg"));
        post_list.add(new Posts("제목제목제목이다","내용내용이다","https://st.depositphotos.com/1029110/2779/v/950/depositphotos_27794673-stock-illustration-money-icons-set.jpg"));
        /*Call<ArrayList<Posts>> call = service.getPosts(1,1);

        call.enqueue(new Callback<ArrayList<Posts>>() {
            @Override
            public void onResponse(Call<ArrayList<Posts>> call, Response<ArrayList<Posts>> response) {
                post_list.addAll(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Posts>> call, Throwable t) {
                Log.d("list_frag",t.getMessage());
            }
        });*/

//       mAdapter.notifyDataSetChanged();
    }
}
