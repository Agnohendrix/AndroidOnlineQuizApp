package com.example.agnohendrix.androidonlinequizapp.Model;

public class Question {
    private String Question,
            AnswerA,
            AnswerB,
            AnswerC,
            AnswerD,
            CorrectAnswer,
            Image;

    private boolean IsImageQuestion;

    private int CategoryId;


    public Question(String question, String answerA, String answerB, String answerC, String answerD, String correctAnswer, String image, boolean isImageQuestion, int categoryId) {
        Question = question;
        AnswerA = answerA;
        AnswerB = answerB;
        AnswerC = answerC;
        AnswerD = answerD;
        CorrectAnswer = correctAnswer;
        Image = image;
        IsImageQuestion = isImageQuestion;
        CategoryId = categoryId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswerA() {
        return AnswerA;
    }

    public void setAnswerA(String answerA) {
        AnswerA = answerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }

    public void setAnswerB(String answerB) {
        AnswerB = answerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }

    public void setAnswerC(String answerC) {
        AnswerC = answerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }

    public void setAnswerD(String answerD) {
        AnswerD = answerD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public boolean isImageQuestion() {
        return IsImageQuestion;
    }

    public void setImageQuestion(boolean imageQuestion) {
        IsImageQuestion = imageQuestion;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }
}
