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
import com.opark.opark.merchant_side.MerchActivity;
import com.opark.opark.merchant_side.MerchProfileSetup;
import com.opark.opark.UserProfileSetup;
import com.opark.opark.login_auth.LoginActivity;
import com.opark.opark.R;
import com.opark.opark.login_auth.MerchWaitingApproval;
import com.opark.opark.share_parking.MapsMainActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private final int SPLASH_DISPLAY_LENGTH = 10;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static boolean hasMerchantProfile;
    public static boolean isMerchantApproved;
    public static boolean isMerchantWhileSplash;
    private  FirebaseUser oParkMerchantFirebase;
    private String currentMerchantEmail;

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

try {
    oParkMerchantFirebase = mAuth.getCurrentUser();
    currentMerchantEmail = oParkMerchantFirebase.getEmail();
}catch  (NullPointerException e ){}
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

       if (currentUser != null) {
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {


                   updateUI(currentUser);

               }
           }, SPLASH_DISPLAY_LENGTH);
       }
       else {

           Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
           SplashActivity.this.startActivity(intent);
           SplashActivity.this.finish();
       }
    }


    public void updateUI(FirebaseUser currentUser) {
        try {
            if (currentUser != null) {

                final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.d(TAG, "updateUI: currentUserID" + currentUserID);

                checkIsMerchantOrUser(currentUserID);


        }else {
                // No user is signed in
                Log.d(TAG, "updateUI: NoUser");
                return;
            } }catch (NullPointerException e) {

            Log.d("Login Null", "login null caught");
        }
    }



    /**below are methods to check if logged in is a User or Merchant**/

    private void checkIsMerchantOrUser(final String userID){

        StorageReference merchRef = FirebaseStorage.getInstance().getReference().child("merchants/" +"merchantlist/"+ currentMerchantEmail + "/isMerchant.txt");
        merchRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // file exists
                Log.d(TAG, "is Merchant");
                Log.d(TAG, "uri: " + uri.toString());

                isMerchantWhileSplash =true;
                checkHasMerchProfile(userID);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //file not found
                Log.d(TAG, "not Merchant");
                isMerchantWhileSplash=false;
                checkIsUser(userID);
            }
        });
    }

    private void checkHasMerchProfile(final String userID){

        final StorageReference hasMerchProf = FirebaseStorage.getInstance().getReference().child("merchants/" + "merchantlist/" + currentMerchantEmail + "/merchProf.txt");
        hasMerchProf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                hasMerchantProfile = true;
                Log.d(TAG, "Merchant has profile ");
                checkIsMerchApproved(userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Merchant has no profile");
                hasMerchantProfile = false;
                //MERCH PROFILE SETUP
                Intent merchProfSetupIntent = new Intent(SplashActivity.this, MerchProfileSetup.class);
                finish();
                SplashActivity.this.startActivity(merchProfSetupIntent);
            }
        });

}

    private void checkIsMerchApproved(String userID){
        final StorageReference isApproved = FirebaseStorage.getInstance().getReference().child("merchants/" + "merchantlist/" + currentMerchantEmail + "/isApproved.txt");
        isApproved.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess: approved merchant");
                isMerchantApproved = true;
                //TODO intent to MerchActivity
                Intent merchActIntent = new Intent(SplashActivity.this, MerchActivity.class);
                finish();
                startActivity(merchActIntent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: not approved");
                isMerchantApproved = false;
                //TODO intent to merchwaiting approval
                Intent merchApprovIntent = new Intent(SplashActivity.this, MerchWaitingApproval.class);
                finish();
                startActivity(merchApprovIntent);

            }
        });
    }

   private void checkIsUser(final String userID){

                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + userID + "/profile.txt");
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // file exists
                        Log.d("login", "user logged in");
                        Log.d("urlget", "uri: " + uri.toString());

                        Intent intent = new Intent(SplashActivity.this, MapsMainActivity.class);
                        intent.putExtra("firebaseUser", userID);
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
            }
        }



