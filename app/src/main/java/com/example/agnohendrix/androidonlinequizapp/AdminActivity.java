package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Model.Category;
import com.example.agnohendrix.androidonlinequizapp.dummy.DummyContent;
import com.facebook.login.LoginManager;

public class AdminActivity extends AppCompatActivity implements QuestionsFragment.OnListFragmentInteractionListener {

    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        bnv = findViewById(R.id.menu_admin);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch(menuItem.getItemId()){
                    case R.id.action_questions:
                        //Insert questions fragment;
                        selectedFragment = new QuestionsFragment();
                        break;
                    case R.id.action_users:
                        selectedFragment = new RankingFragment();
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
        Common.currentUser.unset();
        LoginManager.getInstance().logOut();
        Intent back = new Intent(AdminActivity.this, MainActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(back);
        finish();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
