package com.example.agnohendrix.androidonlinequizapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Model.Question;
import com.example.agnohendrix.androidonlinequizapp.ViewHolder.QuestionsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class QuestionsFragment extends Fragment {


    View myFragment;

    RecyclerView listQuestions;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Question, QuestionsViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questions;


    public QuestionsFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_questions, container, false);

        listQuestions = myFragment.findViewById(R.id.listQuestions);
        layoutManager = new LinearLayoutManager(container.getContext());
        listQuestions.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>()
                        .setQuery(questions,Question.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Question, QuestionsViewHolder>(options) {
            @Override
            //Populates inflated items
            protected void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position, @NonNull final Question model) {
                holder.question_category.setText(model.getCategoryId());
                holder.question.setText(model.getQuestion());
                holder.question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), model.getCorrectAnswer(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @NonNull
            @Override
            public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_layout, viewGroup, false);
                QuestionsViewHolder viewHolder = new QuestionsViewHolder(view);
                return viewHolder;
            }
        };

        adapter.notifyDataSetChanged();
        listQuestions.setAdapter(adapter);
        adapter.startListening();
        return myFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}