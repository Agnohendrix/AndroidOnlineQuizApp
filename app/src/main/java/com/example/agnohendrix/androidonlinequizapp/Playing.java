package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class Playing extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;
    int progressValue=0;

    CountDownTimer mCountDown;

    int index=0, score=0, thisQuestion=0, totalQuestion=0, correctAnswer;


    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        totalQuestion = Common.questionList.size();

        //View
        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.txtTotalQuestions);
        question_text = findViewById(R.id.question_text);
        question_image = findViewById(R.id.question_image);

        progressBar = findViewById(R.id.progressBar);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        mCountDown.cancel();
        //More questions
        if(index < totalQuestion){
            Button clickedButton = (Button)v;
            //Correct answer
            if(clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())){
                score+=10;
                correctAnswer++;
                showQuestion(++index);
            } else {
                Intent intent = new Intent(this, Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE", score);
                dataSend.putInt("TOTAL",totalQuestion);
                dataSend.putInt("CORRECT",correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
            }

            txtScore.setText(String.format("%d", score));
        }
    }

    private void showQuestion(int i) {
        if(i < totalQuestion){
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            if(Common.questionList.get(i).getIsImageQuestion().equals("true")){
                Picasso.get().load(Common.questionList.get(i).getImage()).into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setText(Common.questionList.get(i).getQuestion());
            } else {
                question_text.setText(Common.questionList.get(i).getQuestion());
                question_image.setVisibility(View.INVISIBLE);
            }
            int random = new Random().nextInt(4);
            if(random == 0) {
                btnA.setText(Common.questionList.get(i).getAnswerA());
                btnB.setText(Common.questionList.get(i).getAnswerB());
                btnC.setText(Common.questionList.get(i).getAnswerC());
                btnD.setText(Common.questionList.get(i).getAnswerD());
            } else if (random == 1){
                btnA.setText(Common.questionList.get(i).getAnswerD());
                btnB.setText(Common.questionList.get(i).getAnswerC());
                btnC.setText(Common.questionList.get(i).getAnswerB());
                btnD.setText(Common.questionList.get(i).getAnswerA());
            } else if(random == 2){
                btnA.setText(Common.questionList.get(i).getAnswerC());
                btnB.setText(Common.questionList.get(i).getAnswerB());
                btnC.setText(Common.questionList.get(i).getAnswerA());
                btnD.setText(Common.questionList.get(i).getAnswerD());
            } else if(random == 3){
                btnA.setText(Common.questionList.get(i).getAnswerB());
                btnB.setText(Common.questionList.get(i).getAnswerC());
                btnC.setText(Common.questionList.get(i).getAnswerD());
                btnD.setText(Common.questionList.get(i).getAnswerA());
            }
            mCountDown.start();

        } else {
            //Final question
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();

        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);

    }
}
