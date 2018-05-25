package com.xema.midas.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.xema.midas.R;
import com.xema.midas.common.GlideRequests;
import com.xema.midas.common.PreferenceHelper;
import com.xema.midas.model.ApiResult;
import com.xema.midas.model.Post;
import com.xema.midas.model.Profile;
import com.xema.midas.network.ApiUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Profile profile = PreferenceHelper.loadMyProfile(mContext);
                if (profile == null) return false;

                if (profile.getUid() == post.getProfile().getUid()) {
                    PopupMenu menu = new PopupMenu(mContext, holder.itemView);
                    ((Activity) mContext).getMenuInflater().inflate(R.menu.menu_delete, menu.getMenu());
                    menu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.menu_delete) {
                            attemptDeletePost(post, position);
                        }
                        return false;
                    });
                    menu.show();
                }
                return false;
            }
        });
    }

    private void attemptDeletePost(Post post, int pos) {
        ApiUtil.getPostService().deletePost(PreferenceHelper.loadId(mContext), PreferenceHelper.loadPw(mContext), post.getId()).enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(@NonNull Call<ApiResult> call, Response<ApiResult> response) {
                if (response.code() == 200) {
                    Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    mDataList.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, getItemCount());
                }else {
                    Toast.makeText(mContext, mContext.getString(R.string.error_common), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResult> call, @NonNull Throwable t) {
                Toast.makeText(mContext, mContext.getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
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
