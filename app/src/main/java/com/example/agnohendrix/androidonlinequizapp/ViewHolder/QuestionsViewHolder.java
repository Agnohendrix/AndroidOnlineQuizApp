package com.example.agnohendrix.androidonlinequizapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.agnohendrix.androidonlinequizapp.R;

public class QuestionsViewHolder extends RecyclerView.ViewHolder {

    public TextView question_category;
    public TextView question;



    public QuestionsViewHolder(@NonNull View itemView) {
        super(itemView);
        question_category = itemView.findViewById(R.id.question_cat);
        question = itemView.findViewById(R.id.question_text);
    }

}
