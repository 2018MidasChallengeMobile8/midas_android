package com.xema.midas.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xema.midas.R;
import com.xema.midas.adapter.PostAdapter;
import com.xema.midas.common.GlideApp;
import com.xema.midas.model.Post;
import com.xema.midas.network.ApiUtil;
import com.xema.midas.util.EndlessScrollListener;
import com.xema.midas.util.LoadingProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xema0 on 2018-05-25.
 */

public class PostFragment extends Fragment {
    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout srlMain;

    Unbinder unbinder;

    private Context mContext;

    private List<Post> mPostList;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, null);
        unbinder = ButterKnife.bind(this, view);

        initAdapter();
        getPostList();

        return view;
    }

    private void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvMain.setLayoutManager(llm);
        rvMain.setNestedScrollingEnabled(false);
        rvMain.setHasFixedSize(true);
        mPostList = new ArrayList<>();
        mAdapter = new PostAdapter(mContext, mPostList, GlideApp.with(mContext));
        mAdapter.setHasStableIds(true);
        rvMain.setAdapter(mAdapter);

        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getPostList();
            }
        };
        rvMain.addOnScrollListener(endlessScrollListener);
        srlMain.setOnRefreshListener(() -> {
            mPostList.clear();
            mAdapter.notifyDataSetChanged();
            endlessScrollListener.resetState();
            getPostList();
        });
    }

    private void getPostList() {
        int lastPostId = -1;
        if (!mPostList.isEmpty()) {
            lastPostId = mPostList.get(mPostList.size() - 1).getId();
        }

        LoadingProgressDialog.showProgress(mContext);

        ApiUtil.getPostService().getPostList(20, lastPostId).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                srlMain.setRefreshing(false);
                LoadingProgressDialog.hideProgress();
                if (response.code() == 200) {
                    List<Post> list = response.body();
                    if (list != null) {
                        mPostList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                srlMain.setRefreshing(false);
                LoadingProgressDialog.hideProgress();
                Toast.makeText(mContext, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
