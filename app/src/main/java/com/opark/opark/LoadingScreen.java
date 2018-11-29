package com.opark.opark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.model.MatchmakingRecord;
import com.opark.opark.share_parking.MapsMainActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LoadingScreen extends AppCompatActivity {

    String ADATEM2 = "2";
    String TAG = "LoadingScreen";
    final long ONE_MEGABYTE = 1024 * 1024;

    private DatabaseReference matchmakingRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference userRewardsFolder;
    private String currentUserID;
    private Button cancelButton;
    private int userPoints;
    Chronometer mChronometer;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String CurrentUserID;
    private StorageReference latestMatchmakingRecord;
    private StorageReference matchMakingRecordStoRef;
    private double kenaLat;
    private double kenaLng;
    private LatLng kenaLatLng;
    double startTime;
    double elapsedTime;
    public static double pointsGainedFromLoadingScreen;
    private int pointsToUploadFromLoadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        userRewardsFolder = storageRef.child("users/" + currentUserID + "/points.txt");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        startTime = SystemClock.currentThreadTimeMillis();

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();

        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation);
        lottieAnimationView.setImageAssetsFolder("images/");
        lottieAnimationView.setAnimation("squareboi_loading_animation.json");
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();

        cancelButton = (Button) findViewById(R.id.cancel_button);

//        animate(animationView);

        matchmakingRef.child(currentUserID).child("adatem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                String adatemValue = dataSnapshot.getValue().toString();
                if (adatemValue.equals("2")) {
                    CalculatePoints();
                    Intent intent = new Intent(LoadingScreen.this, KenaMap.class);
                    startActivity(intent);
//                    finish();
                } else {

                }
                }catch (NullPointerException e ){
                        e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchmakingRef.child(currentUserID).child("adatem").setValue("Not Available");
                finish();
                recordMatchMaking();
                CalculatePoints();
                GetPointsStorage();
            }
        });
    }

    private void GetPointsStorage(){

        userRewardsFolder.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    userPoints = (new Gson().fromJson(new String(bytes, "UTF-8"), Integer.class));

                    Log.d(TAG, "Gsonfrom json success, Points is " + userPoints);


//                    Log.d(TAG,"pointsToUploadFromLoadingScreen is " + pointsToUploadFromLoadingScreen);
                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                pointsToUploadFromLoadingScreen = (int) (Math.ceil(pointsGainedFromLoadingScreen) + userPoints);
                objToByteStreamUpload(pointsToUploadFromLoadingScreen,userRewardsFolder);
                Log.d(TAG,"Points uploaded from Loading Screen is " + pointsToUploadFromLoadingScreen);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"fragment is not created, exception: " + exception);
            }
        });

    }

    private void CalculatePoints(){

        elapsedTime = ((SystemClock.currentThreadTimeMillis())-startTime)/1000;


        if(elapsedTime >= 30 && elapsedTime < 60){
            pointsGainedFromLoadingScreen = 30*2;
        } else if (elapsedTime >= 60 && elapsedTime < 90){
            pointsGainedFromLoadingScreen = (30*2) + (30*2.5);
        } else if ( elapsedTime >= 90 && elapsedTime < 120){
            pointsGainedFromLoadingScreen = (30*2) + (30*2.5) + (30*3);
        } else if ( elapsedTime == 120){
            pointsGainedFromLoadingScreen = (30*2) + (30*2.5) + (30*3) + (30*3.5);
        } else if (elapsedTime >120){
            pointsGainedFromLoadingScreen = (30*2) + (30*2.5) + (30*3) + (30*3.5) + (elapsedTime-120)*4;
        } else{
            pointsGainedFromLoadingScreen = 0;
        }

        pointsGainedFromLoadingScreen = Math.ceil(pointsGainedFromLoadingScreen);
        Log.d(TAG,"Elapsed time is " + elapsedTime);
        Log.d(TAG,"Points Gained From Loading Screen is " + pointsGainedFromLoadingScreen);
    }

    public void objToByteStreamUpload(Object obj, StorageReference destination){

        String objStr = new Gson().toJson(obj);
        InputStream in = new ByteArrayInputStream(objStr.getBytes(Charset.forName("UTF-8")));
        UploadTask uploadTask = destination.putStream(in);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure: Failure to upload in storage ");
                // Use analytics to find out why is the error
                // then only implement the best corresponding measures


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "You've Earned Points!", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Profile update successful!");
                // Use analytics to calculate the success rate
            }
        });
    }

    @Override
    public void onBackPressed() {
        matchmakingRef.child(currentUserID).child("adatem").setValue("Not Available");
        finish();
    }

    public void recordMatchMaking(){

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();

        kenaLat = MapsMainActivity.mLastLocation.getLatitude();
        kenaLng = MapsMainActivity.mLastLocation.getLongitude();
        kenaLatLng = new LatLng(kenaLat,kenaLng);
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        Long tsLong = System.currentTimeMillis()/1000;

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(tsLong* 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

        MatchmakingRecord thisMatchmakingRecord = new MatchmakingRecord("none",date,currentUserID,"none",null,kenaLatLng,elapsedTime,pointsToUploadFromLoadingScreen);
        matchMakingRecordStoRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/matchmakingrecord/" + "sharingrecord/" + date);
        latestMatchmakingRecord = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/matchmakingrecord/"+  "latestrecord.txt");
        objToByteStreamUpload(thisMatchmakingRecord,matchMakingRecordStoRef);
        objToByteStreamUpload(thisMatchmakingRecord,latestMatchmakingRecord);




    }

    @Override
    protected void onPause() {
    super.onPause();
    }

    @Override
    protected void onStop() {
    super.onStop();
    }
}
