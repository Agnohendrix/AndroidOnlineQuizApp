package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.facebook.login.LoginManager;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    @Override
    public void onBackPressed(){
        Common.currentUser.unset();
        LoginManager.getInstance().logOut();
        Intent back = new Intent(AdminActivity.this, MainActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(back);
        finish();
    }
}
