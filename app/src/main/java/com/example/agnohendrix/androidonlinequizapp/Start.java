package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent = getIntent();
        String image = intent.getStringExtra("Image");
        ImageView iv = findViewById(R.id.logo);
        Picasso.get().load(image).into(iv);

        TextView tv = findViewById(R.id.category_name_start);
        tv.setText(intent.getStringExtra("Category"));

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        loadQuestion(Common.categoryId);

        btnPlay = findViewById(R.id.btn_play);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, Playing.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void loadQuestion(String categoryId) {

        if(Common.questionList.size() > 0)
            Common.questionList.clear();

        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Question quest = postSnapshot.getValue(Question.class);
                    Common.questionList.add(quest);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Collections.shuffle(Common.questionList);

    }
}
