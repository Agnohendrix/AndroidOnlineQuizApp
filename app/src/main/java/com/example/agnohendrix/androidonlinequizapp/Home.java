package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;

import static java.security.AccessController.getContext;

public class Home extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch(menuItem.getItemId()){
                    case R.id.action_category:
                        selectedFragment = CategoryFragment.newInstance();
                        break;
                    case R.id.action_ranking:
                        selectedFragment = RankingFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        setDefaultFragment();


    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(Home.this, "Hai premuto indietro", Toast.LENGTH_SHORT).show();
        Common.currentUser.unset();
        Intent back = new Intent(Home.this, MainActivity.class);
        startActivity(back);
        finish();
    }
}
