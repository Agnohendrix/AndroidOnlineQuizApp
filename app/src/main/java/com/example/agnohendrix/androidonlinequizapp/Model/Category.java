package com.example.agnohendrix.androidonlinequizapp.Model;

public class Category {
    private String name;
    private String image;

    public Category(){

    }

    public Category(String name, String image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
