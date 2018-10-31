package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Start extends AppCompatActivity {

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
    }
}
