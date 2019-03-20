package com.example.agnohendrix.androidonlinequizapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Model.Question;
import com.example.agnohendrix.androidonlinequizapp.ViewHolder.QuestionsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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
            protected void onBindViewHolder(@NonNull final QuestionsViewHolder holder, final int position, @NonNull final Question model) {
                holder.question_category.setText(this.getSnapshots().getSnapshot(position).getKey());
                holder.question.setText(model.getQuestion());
                

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.question.setTextColor(Color.YELLOW);
                        holder.question_category.setTextColor(Color.YELLOW);
                        Toast.makeText(getContext(), model.getCorrectAnswer(), Toast.LENGTH_LONG).show();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Question " + getSnapshots().getSnapshot(position).getKey());
                        alertDialog.setMessage("Modify question");
                        View modifyQuestion = inflater.inflate(R.layout.modify_question, null);

                        TextView question = modifyQuestion.findViewById(R.id.m_question);
                        TextView qCat = modifyQuestion.findViewById(R.id.m_question_category);
                        TextView qAnswerA = modifyQuestion.findViewById(R.id.m_answerA);
                        TextView qAnswerB = modifyQuestion.findViewById(R.id.m_answerB);
                        TextView qAnswerC = modifyQuestion.findViewById(R.id.m_answerC);
                        TextView qAnswerD = modifyQuestion.findViewById(R.id.m_answerD);
                        TextView qCorrectAnswer = modifyQuestion.findViewById(R.id.m_correct_answer);
                        ImageView qImage = modifyQuestion.findViewById(R.id.m_question_image);

                        question.setText(model.getQuestion());
                        qCat.setText(model.getCategoryId());
                        qAnswerA.setText(model.getAnswerA());
                        qAnswerB.setText(model.getAnswerB());
                        qAnswerC.setText(model.getAnswerC());
                        qAnswerD.setText(model.getAnswerD());
                        qCorrectAnswer.setText(model.getCorrectAnswer());
                        if(model.getIsImageQuestion().equals("true")){
                            Picasso.get().load(model.getImage()).into(qImage);
                            qImage.setVisibility(View.VISIBLE);
                        } else {
                            qImage.setVisibility(View.INVISIBLE);
                        }

                        alertDialog.setView(modifyQuestion);

                        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                dialogInterface.dismiss();
                                holder.question.setTextColor(Color.BLACK);
                                holder.question_category.setTextColor(Color.BLACK);
                            }
                        });
                        alertDialog.show();
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
