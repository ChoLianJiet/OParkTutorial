package com.opark.opark;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.model.User;
import com.opark.opark.share_parking.MapsMainActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class UserPopUp extends Activity {

    final String TAG = "UserPopUp";
    public static String USER_DECLINE_PREFS;
    public static String USER_DECLINE_KEY;
    public static String DECLINED = "declined";
    final long ONE_MEGABYTE = 1024 * 1024;

    PopupWindow window;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    private DatabaseReference matchmakingRef;
    private DatabaseReference geofireRef;
    private DatabaseReference togetherRef;
    TextView kenaParkerName;
    TextView carModel;
    TextView carPlateNumber;
    TextView carColor;
    ArrayList<User> userObjList = new ArrayList<>();
    String foundUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_user);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING);
        Button acceptButton = (Button) findViewById(R.id.accept);
        Button declineButton = (Button) findViewById(R.id.decline);
        FloatingActionButton xButton = (FloatingActionButton) findViewById(R.id.floatingXButton);

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsMainActivity.kenaMarker.remove();
                MapsMainActivity.kenaMarker = null;
                returnToMain();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptUser();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineUser();
            }
        });


        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");

        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");

        kenaParkerName = (TextView) findViewById(R.id.kenaName);
        carModel = (TextView) findViewById(R.id.carModel);
        carPlateNumber = (TextView) findViewById(R.id.carPlateNumber);
        carColor = (TextView) findViewById(R.id.carColor);

        SharedPreferences prefs = getSharedPreferences(MapsMainActivity.USER_ID_PREFS,MODE_PRIVATE);
        foundUser = prefs.getString(MapsMainActivity.USER_ID_KEY, null);

        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");

        setKenaDetailsOnWindow();

    }

//    private void initView(){
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        float density = dm.density;
//        int width = (int) (dm.widthPixels - (2 * 18 * density));
//        int height = (int) (dm.heightPixels - (338 * density));
//        getWindow().setLayout(width,height);
//    }

    private void setKenaDetailsOnWindow(){
        final StorageReference getKenaProfileRef = storageRef.child("users/" + foundUser + "/profile.txt");
        getKenaProfileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    userObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
                    Log.d("Gson", "Gsonfrom json success");

                    kenaParkerName.setText(userObjList.get(0).getUserName().getFirstName() + userObjList.get(0).getUserName().getLastName());
                    carModel.setText(userObjList.get(0).getUserCar().getCarBrand() + userObjList.get(0).getUserCar().getCarModel());
                    carPlateNumber.setText(userObjList.get(0).getUserCar().getCarPlate());
                    carColor.setText(userObjList.get(0).getUserCar().getCarColour());
                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    private void acceptUser(){

        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM2);
        togetherRef.child(foundUser).setValue(MapsMainActivity.currentUserID);
        Intent intent = new Intent(UserPopUp.this,PeterMap.class);
        startActivity(intent);
        finish();
    }

    private void declineUser(){

        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM0);
        MapsMainActivity.newArrayList.remove(foundUser);

        MapsMainActivity.oldArrayList.add(foundUser);
        MapsMainActivity.oldHashSet.addAll(MapsMainActivity.oldArrayList);
        MapsMainActivity.oldArrayList.clear();
        MapsMainActivity.oldArrayList.addAll(MapsMainActivity.oldHashSet);
        MapsMainActivity.oldHashSet.clear();

        MapsMainActivity.kenaMarker.remove();
        MapsMainActivity.kenaMarker = null;

        Log.i(TAG,foundUser + " has been removed from newArrayList due to declining");
        Log.i(TAG,"MapsMainActivity newArraylist " + MapsMainActivity.newArrayList);
        Log.i(TAG,"MapsMainActivity oldArraylist" + MapsMainActivity.oldArrayList);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnToMain();
    }

    private void returnToMain(){

        finish();
        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM0);
    }

}
