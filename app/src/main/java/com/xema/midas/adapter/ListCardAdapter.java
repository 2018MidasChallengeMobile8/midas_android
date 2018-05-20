package com.xema.midas.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.xema.midas.R;
import com.xema.midas.model.ListCardItems;

import java.util.ArrayList;

public class ListCardAdapter extends RecyclerView.Adapter<ListCardAdapter.ViewHolder>
{

    private ArrayList<ListCardItems> list;
    private RequestManager requestManager;

    public  ListCardAdapter(ArrayList<ListCardItems> list, RequestManager requestManager)
    {
        this.list = list;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.txt_title.setText(list.get(position).getTitle());
            holder.txt_content.setText(list.get(position).getContent());
            holder.txt_date.setText(list.get(position).getDate());
            requestManager.load(list.get(position).getIm_url()).into(holder.card_im);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView card_im;
        public TextView txt_title;
        public TextView txt_content;
        public TextView txt_date;

        public ViewHolder(View itemView) {
            super(itemView);
            card_im = (ImageView)itemView.findViewById(R.id.list_card_im_card);
            txt_title = (TextView)itemView.findViewById(R.id.list_card_txt_title);
            txt_content = (TextView)itemView.findViewById(R.id.list_card_txt_content);
            txt_date = (TextView)itemView.findViewById(R.id.list_card_txt_date);
        }
    }
}
