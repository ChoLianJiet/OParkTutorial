package com.opark.opark.splash_screen;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.opark.opark.login_auth.LoginActivity;
import com.opark.opark.R;
import com.opark.opark.share_parking.MapsMainActivity;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(getApplicationContext());
            mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    System.out.println("User logged in");

                }
                else {
                    System.out.println("User not logged in");

                }
            }
        };



        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

       if (currentUser != null) {
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   /* Create an Intent that will start the Menu-Activity. */
                   Intent mainIntent = new Intent(SplashActivity.this, MapsMainActivity.class);
                   SplashActivity.this.startActivity(mainIntent);
                   SplashActivity.this.finish();
               }
           }, SPLASH_DISPLAY_LENGTH);
       }
       else {

           Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
           SplashActivity.this.startActivity(intent);
           SplashActivity.this.finish();
       }
    }
}
