package com.xema.midas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xema.midas.R;
import com.xema.midas.common.GlideRequests;
import com.xema.midas.model.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ListItemViewHolder> {
    private static final String TAG = PostAdapter.class.getSimpleName();

    private Context mContext = null;
    private List<Post> mDataList = null;
    private GlideRequests mGlideRequest;

    public PostAdapter(Context mContext, List<Post> mDataList, GlideRequests mGlideRequest) {
        super();
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mGlideRequest = mGlideRequest;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ListItemViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        final Post post = mDataList.get(position);

        if (post == null) return;

        holder.tvTitle.setText(post.getTitle());
        holder.tvDate.setText(post.getDate());
        mGlideRequest.load(post.getImage()).into(holder.ivImage);
    }

    private void attemptDeletePost(Post post, int pos) {
        // TODO: 2018-05-25
    }

    @Override
    public long getItemId(int position) {
        return mDataList != null ? mDataList.get(position).getId() : position;
    }


    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_location)
        TextView tvLocation;
        @BindView(R.id.tv_limit)
        TextView tvLimit;

        ListItemViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
