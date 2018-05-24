package com.xema.midas.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.xema.midas.R;
import com.xema.midas.activity.ListContentActivity;
import com.xema.midas.model.Posts;

import java.util.ArrayList;

public class ListCardAdapter extends RecyclerView.Adapter<ListCardAdapter.ViewHolder>
{

    private ArrayList<Posts> list;
    private RequestManager requestManager;

    public  ListCardAdapter(ArrayList<Posts> list, RequestManager requestManager)
    {
        this.list = list;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contents_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.txt_title.setText(list.get(position).getTitle());
            holder.txt_content.setText(list.get(position).getText());
            requestManager.load(list.get(position).getImage()).into(holder.card_im);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView card_im;
        public TextView txt_title;
        public TextView txt_content;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            card_im = (ImageView)itemView.findViewById(R.id.iv_main_image);
            txt_title = (TextView)itemView.findViewById(R.id.tv_title);
            txt_content = (TextView)itemView.findViewById(R.id.tv_location);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ListContentActivity.class);
            if(card_im.getDrawable() == null)
            {
                Log.d("abcde","사진 없음");
            }
            else {
                Bundle b = new Bundle();

                Bitmap bitmap = ((BitmapDrawable) card_im.getDrawable()).getBitmap();
                b.putParcelable("image",bitmap);
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        }
    }
}
