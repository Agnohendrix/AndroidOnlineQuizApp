package com.example.agnohendrix.androidonlinequizapp.Model;

import com.google.firebase.database.PropertyName;

public class Question {

    private String Question,
            AnswerA,
            AnswerB,
            AnswerC,
            AnswerD,
            CorrectAnswer,
            Image;

    private String IsImageQuestion;

    private String CategoryId;

    public Question(){}

    public Question(String Question, String AnswerA, String AnswerB, String AnswerC, String AnswerD, String CorrectAnswer, String Image, String IsImageQuestion, String CategoryId) {
        this.Question = Question;
        this.AnswerA = AnswerA;
        this.AnswerB = AnswerB;
        this.AnswerC = AnswerC;
        this.AnswerD = AnswerD;
        this.CorrectAnswer = CorrectAnswer;
        this.Image = Image;
        this.IsImageQuestion = IsImageQuestion;
        this.CategoryId = CategoryId;
    }

    @PropertyName("Question") //Needed both on getter and setter, tells which name will be stored in firebase
    public String getQuestion() {
        return Question;
    }

    @PropertyName("Question")
    public void setQuestion(String Question) {
        this.Question = Question;
    }

    @PropertyName("AnswerA")
    public String getAnswerA() {
        return AnswerA;
    }

    @PropertyName("AnswerA")
    public void setAnswerA(String AnswerA) {
        this.AnswerA = AnswerA;
    }

    @PropertyName("AnswerB")
    public String getAnswerB() {
        return AnswerB;
    }

    @PropertyName("AnswerB")
    public void setAnswerB(String AnswerB) {
        this.AnswerB = AnswerB;
    }

    @PropertyName("AnswerC")
    public String getAnswerC() {
        return AnswerC;
    }

    @PropertyName("AnswerC")
    public void setAnswerC(String AnswerC) {
        this.AnswerC = AnswerC;
    }

    @PropertyName("AnswerD")
    public String getAnswerD() {
        return AnswerD;
    }

    @PropertyName("AnswerD")
    public void setAnswerD(String AnswerD) {
        this.AnswerD = AnswerD;
    }

    @PropertyName("CorrectAnswer")
    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    @PropertyName("CorrectAnswer")
    public void setCorrectAnswer(String CorrectAnswer) {
        this.CorrectAnswer = CorrectAnswer;
    }

    @PropertyName("Image")
    public String getImage() {
        return Image;
    }

    @PropertyName("Image")
    public void setImage(String Image) {
        this.Image = Image;
    }

    @PropertyName("IsImageQuestion")
    public String getIsImageQuestion() {
        return IsImageQuestion;
    }

    @PropertyName("IsImageQuestion")
    public void setIsImageQuestion(String IsImageQuestion) {
        this.IsImageQuestion = IsImageQuestion;
    }

    @PropertyName("CategoryId")
    public String getCategoryId() {
        return CategoryId;
    }

    @PropertyName("CategoryId")
    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }
}
