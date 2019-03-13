package com.example.agnohendrix.androidonlinequizapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.agnohendrix.androidonlinequizapp.Interface.ItemClickListener;
import com.example.agnohendrix.androidonlinequizapp.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView ranking_name, ranking_score;

    private ItemClickListener itemClickListener;


    public RankingViewHolder(View itemView){
        super(itemView);
        ranking_name = itemView.findViewById(R.id.txt_name);
        ranking_score = itemView.findViewById(R.id.txt_score);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
