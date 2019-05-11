package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.facebook.login.LoginManager;

public class AdminActivity extends AppCompatActivity {

    BottomNavigationView bnv;

    ImageView addQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
/*
        addQ = findViewById(R.id.add_questions);

        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "PremiStoMinchiaDiPiù", Toast.LENGTH_LONG).show();
            }
        });
*/

        bnv = findViewById(R.id.menu_admin);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch(menuItem.getItemId()){
                    case R.id.action_questions:
                        //Loads Question Fragment
                        selectedFragment = new QuestionsFragment();
                        break;
                    case R.id.action_users:
                        //selectedFragment = new RankingFragment();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });
        setDefaultFragment();
        Toast.makeText(AdminActivity.this, "Salve padrone", Toast.LENGTH_LONG).show();
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new QuestionsFragment());
        transaction.commit();
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
