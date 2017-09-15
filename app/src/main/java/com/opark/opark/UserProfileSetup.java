package com.opark.opark;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class UserProfileSetup extends AppCompatActivity {

    private ExpandableLayout expandableFirstName;
    private ExpandableLayout expandableLastName;
    private ExpandableLayout expandablePhoneNum;
    private ExpandableLayout expandableCarColour;
    private ExpandableLayout expandableCarBrand;
    private ExpandableLayout expandableCarModel;
    private ExpandableLayout expandableCarPlate;
    private ExpandableLayout expandableFirstLineAddress;
    private ExpandableLayout expandableSecondLineAddress;
    private ExpandableLayout expandableCity;
    private ExpandableLayout expandablePostcode;
    private ExpandableLayout expandableCountryState;
    private EditText firstName ;
    private EditText lastName ;
    private EditText phoneNum ;
    private EditText carColour ;
    private EditText carBrand ;
    private EditText carModel ;
    private EditText carPlate ;
    private EditText firstLine ;
    private EditText secondLine ;
    private EditText city ;
    private EditText postcode ;
    private EditText countryState;
    private Button profileSubmit;
    private Button buttonExpandAddressLayout;
    private Button buttonExpandNameNumLayout;
    private Button buttonExpandCarLayout;
    private Button buttonSignOut;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private User user = new User(); // Changed User from static to non static
    String firebaseUserUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_setup);

        // Retrieve FirebaseUser object via intent string extra
        // String is converted into FirebaseUser object via Gson
        firebaseUserUID = getIntent().getStringExtra("firebaseUser");

        // LINKING VARIABLES TO RESPECTIVE IDs
        bindViews();

        //Provided by Library but haven't test if useful or not
        expandableListener();

        /**EXPANDING LAYOUT**/
        //Expand Name and Num layout
        buttonExpandNameNumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameNumExpand();
            }
        });

// Expand Address Layout
        buttonExpandAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               addressExpand();
            }
        });

 //Expand Car layout
        buttonExpandCarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               carExpand();
            }
        });

        
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("signout", "signoutbutton Clicked");
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(UserProfileSetup.this, LoginActivity.class);
                finish();
                startActivity(intent);
                    }
                });


        profileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectUserName();
                collectCar();
                collectAddress();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                StorageReference userFolder = storageRef.child("users/" + firebaseUserUID + "/profile.txt");
                objToByteStreamUpload(user, userFolder);

                //TODO intent to card swipe or other page ? maybe upload prof pic ?
                Intent intent = new Intent(UserProfileSetup.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });


    }

    private void collectUserName(){

        user.userName.firstName = firstName.getText().toString();
        user.userName.lastName = lastName.getText().toString();
        user.userName.phoneNum = phoneNum.getText().toString();
    }

    private void collectCar(){
        user.userCar.carColour = carColour.getText().toString();
        user.userCar.carBrand = carBrand.getText().toString();
        user.userCar.carModel = carModel.getText().toString();
        user.userCar.carPlate = carPlate.getText().toString();

    }

    private void collectAddress(){
        user.userAddress.firstline = firstLine.getText().toString();
        user.userAddress.secondline = secondLine.getText().toString();
        user.userAddress.city = city.getText().toString();
        user.userAddress.postcode = postcode.getText().toString();
        user.userAddress.countryState = countryState.getText().toString();
    }


    // On Back Pressed
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(UserProfileSetup.this, LoginActivity.class);
        startActivity(intent);
    }

    public void objToByteStreamUpload(Object obj, StorageReference destination){

        String objStr = new Gson().toJson(obj);
        InputStream in = new ByteArrayInputStream(objStr.getBytes( Charset.forName("UTF-8")));
        UploadTask uploadTask = destination.putStream(in);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                showErrorDialog("Failed to update your profile. Try again maybe? ");
                // Use analytics to find out why is the error
                // then only implement the best corresponding measures


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Profile update successful!", Toast.LENGTH_LONG).show();
                Log.i("Hello", "Profile update successful!");
                // Use analytics to calculate the success rate
            }
        });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void bindViews(){

        firstName = (EditText) findViewById(R.id.edittext_first_name);
        lastName = (EditText) findViewById(R.id.edittext_last_name);
        phoneNum = (EditText) findViewById(R.id.edittext_phone_num);
        carColour = (EditText) findViewById(R.id.edittext_car_colour);
        carBrand = (EditText) findViewById(R.id.edittext_car_brand);
        carModel = (EditText) findViewById(R.id.edittext_car_model);
        carPlate = (EditText) findViewById(R.id.edittext_car_plate);
        firstLine = (EditText) findViewById(R.id.edittext_first_line);
        secondLine = (EditText) findViewById(R.id.edittext_second_line);
        city = (EditText) findViewById(R.id.edittext_city);
        postcode = (EditText) findViewById(R.id.edittext_postcode);
        countryState = (EditText) findViewById(R.id.edittext_state);
         buttonExpandAddressLayout = (Button) findViewById(R.id.expand_address_button);
         buttonExpandNameNumLayout = (Button) findViewById(R.id.expand_name_button);
         buttonExpandCarLayout = (Button) findViewById(R.id.expand_car_button);
        expandableFirstName = (ExpandableLayout) findViewById(R.id.expand_first_name);
        expandableLastName = (ExpandableLayout) findViewById(R.id.expand_last_name);
        expandablePhoneNum = (ExpandableLayout) findViewById(R.id.expand_phone_number);
        expandableCarColour =(ExpandableLayout) findViewById(R.id.expand_car_colour);
        expandableCarBrand = (ExpandableLayout) findViewById(R.id.expand_car_brand);
        expandableCarModel = (ExpandableLayout) findViewById(R.id.expand_car_model);
        expandableCarPlate = (ExpandableLayout) findViewById(R.id.expand_car_plate);
        expandableFirstLineAddress = (ExpandableLayout) findViewById(R.id.expand_firstline_address);
        expandableSecondLineAddress = (ExpandableLayout) findViewById(R.id.expand_secondline_address);
        expandableCity = (ExpandableLayout) findViewById(R.id.expand_city_address);
        expandablePostcode = (ExpandableLayout) findViewById(R.id.expand_postcode_address);
        expandableCountryState = (ExpandableLayout) findViewById(R.id.expand_countryState_address);
        profileSubmit = (Button) findViewById(R.id.profile_submit_button) ;
        buttonSignOut = (Button) findViewById(R.id.not_you_sign_out_button);
    }

    private void expandableListener(){

        expandableFirstName.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableLastName.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandablePhoneNum.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableCarColour.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableCarBrand.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableCarModel.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableCarPlate.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableFirstLineAddress.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableSecondLineAddress.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableCity.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandablePostcode.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });
        expandableCountryState.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });

    }

    private void nameNumExpand(){
        expandableFirstName.expand();
        expandableLastName.expand();
        expandablePhoneNum.expand();

        expandableCarColour.collapse();
        expandableCarBrand.collapse();
        expandableCarPlate.collapse();
        expandableCarModel.collapse();

        expandableFirstLineAddress.collapse();
        expandableSecondLineAddress.collapse();
        expandableCity.collapse();
        expandablePostcode.collapse();
        expandableCountryState.collapse();
    }

    private void addressExpand(){
        expandableFirstName.collapse();
        expandableLastName.collapse();
        expandablePhoneNum.collapse();

        expandableCarColour.collapse();
        expandableCarBrand.collapse();
        expandableCarPlate.collapse();
        expandableCarModel.collapse();

        expandableFirstLineAddress.expand();
        expandableSecondLineAddress.expand();
        expandableCity.expand();
        expandablePostcode.expand();
        expandableCountryState.expand();

    }

    private void carExpand(){
        expandableFirstName.collapse();
        expandableLastName.collapse();
        expandablePhoneNum.collapse();

        expandableCarColour.expand();
        expandableCarBrand.expand();
        expandableCarPlate.expand();
        expandableCarModel.expand();

        expandableFirstLineAddress.collapse();
        expandableSecondLineAddress.collapse();
        expandableCity.collapse();
        expandablePostcode.collapse();
        expandableCountryState.collapse();
    }

}
