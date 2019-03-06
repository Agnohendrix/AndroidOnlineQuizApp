package com.example.agnohendrix.androidonlinequizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    MaterialEditText editNewUserName, editNewPassword, editNewEmail; //For Sign Up

    MaterialEditText editUserName, editPassword; //For Sign In

    Button btnSignUp, btnSignIn, btnOffline;

    Button btnFacebook;

    FirebaseDatabase database;
    DatabaseReference users;
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();

        users = database.getReference("Users");

        editUserName = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignupDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(editUserName.getText().toString(), editPassword.getText().toString());
            }
        });
        btnOffline = findViewById(R.id.btn_offline);

        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Common.currentUser = new User("Guest", "Guest", "Guest");
                Intent homeActivity = new Intent(MainActivity.this, Home.class);
                startActivity(homeActivity);
                Log.d("Home", "Guest");
                //finish();
            }
        });

        //Facebook integration

        btnFacebook = (LoginButton) findViewById(R.id.login_button);
        ((LoginButton)btnFacebook).setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));




        ((LoginButton)btnFacebook).registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
                final Profile profile = Profile.getCurrentProfile();
                AccessToken accessToken1 = loginResult.getAccessToken();

                //Toast.makeText(MainActivity.this, profile.getFirstName() + " " + profile.getMiddleName() + " "  + profile.getLastName(), Toast.LENGTH_LONG).show();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken1,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    final String email;
                                    final String name;
                                    final String username;
                                    email = object.getString("email");
                                    name = object.getString("name");

                                    //Toast.makeText(MainActivity.this, email, Toast.LENGTH_LONG).show();
                                    username = name;

                                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.child(username).exists()){
                                                //Login
                                                User user = dataSnapshot.child(username).getValue(User.class);
                                                Common.currentUser = user;
                                                Intent homeActivity = new Intent(MainActivity.this, Home.class);
                                                startActivity(homeActivity);
                                                Log.d("Home", "Home");
                                                //finish();
                                            } else {
                                                //Registration
                                                final User reg = new User(username, "", email);
                                                users.child(reg.getUserName()).setValue(reg);
                                                //Login
                                                User user = dataSnapshot.child(username).getValue(User.class);
                                                Common.currentUser = user;
                                                Intent homeActivity = new Intent(MainActivity.this, Home.class);
                                                startActivity(homeActivity);
                                                Log.d("Home", "Home");
                                                //finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } catch (JSONException e){

                                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, link, email");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "Errore", Toast.LENGTH_LONG).show();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void signIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Checks if User exists in database
                if(dataSnapshot.child(user).exists()){
                    if(!user.isEmpty()){
                        User login = dataSnapshot.child(user).getValue(User.class);
                        //Checks password for inserted user
                        if(login.getPassword().equals(pwd)){
                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = login;
                            startActivity(homeActivity);
                            Log.d("Home", "Home");
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            Log.d("Homepwd", "Wrongpwd");
                        }
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showSignupDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        editNewUserName = sign_up_layout.findViewById(R.id.editNewUserName);
        editNewPassword = sign_up_layout.findViewById(R.id.editNewPassword);
        editNewEmail = sign_up_layout.findViewById(R.id.editNewEmail);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(editNewUserName.getText().toString(), editNewPassword.getText().toString(), editNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Checks if user already exists, according to UserName
                        if(dataSnapshot.child(user.getUserName()).exists())
                                Toast.makeText(MainActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                        else{
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "User registration success!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
