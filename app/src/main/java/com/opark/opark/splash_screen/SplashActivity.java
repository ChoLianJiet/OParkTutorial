package com.opark.opark.splash_screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.UserProfileSetup;
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
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    System.out.println("User logged in");
                    Toast.makeText(SplashActivity.this, "User here", Toast.LENGTH_SHORT).show();

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
                   updateUI(currentUser);
                   /* Create an Intent that will start the Menu-Activity. */
//                   Intent mainIntent = new Intent(SplashActivity.this, MapsMainActivity.class);
//                   SplashActivity.this.startActivity(mainIntent);
//                   SplashActivity.this.finish();
               }
           }, SPLASH_DISPLAY_LENGTH);
       }
       else {

           Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
           SplashActivity.this.startActivity(intent);
           SplashActivity.this.finish();
       }
    }







    private void updateUI(FirebaseUser currentUser) {
        try {
            if (currentUser != null) {

                final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/profile.txt");
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // file exists
                        Log.d("login", "user logged in");
                        Log.d("urlget", "uri: " + uri.toString());



                        Intent intent = new Intent(SplashActivity.this, MapsMainActivity.class);
                        intent.putExtra("firebaseUser", currentUserID);
                        finish();
                        startActivity(intent);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //file not found
                        String firebaseUserUID = mAuth.getCurrentUser().getUid();
                        Log.d("urlget", "New User, No profile");
                        Intent intent = new Intent(SplashActivity.this, UserProfileSetup.class);
                        intent.putExtra("firebaseUser", firebaseUserUID);
                        finish();
                        startActivity(intent);
                    }
                });
            } else {
                // No user is signed in
                Log.d("login", "no user");
                return;
            }
        } catch (NullPointerException e) {

            Log.d("Login Null", "login null caught");


        }
    }
}
