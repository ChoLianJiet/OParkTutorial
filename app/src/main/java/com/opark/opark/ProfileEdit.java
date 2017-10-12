package com.opark.opark;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;
import com.opark.opark.model.User;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.id.list;
import static android.R.string.cancel;

public class ProfileEdit extends AppCompatActivity {

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
    private Button profileUpdate;
    private Button buttonExpandAddressLayout;
    private Button buttonExpandNameNumLayout;
    private Button buttonExpandCarLayout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String [] carString = new String[4];
    private EditText [] carEditText =new EditText[4] ;
    private String [] nameNumString = new String[3];
    private EditText [] nameNumEditText = new EditText[3];
    private String [] addressString = new String [5];
    private EditText[] addressEditText = new EditText[5];

    private User user = new User(); // Changed User from static to non static
    String firebaseUserUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
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




    }

    private void collectUserName(){


        nameNumEditText[0]= firstName;
        nameNumEditText[1]= lastName;
        nameNumEditText[2]= phoneNum;

        nameNumString[0]=  user.userName.firstName = firstName.getText().toString();
        nameNumString[1]=  user.userName.lastName = lastName.getText().toString();
        nameNumString[2]=  user.userName.phoneNum = phoneNum.getText().toString();

        boolean cancel = false;
        View focusView = null;


    }



    private void collectCar(){
        carEditText[0]=carColour;
        carEditText[1]=carBrand ;
        carEditText[2]=carModel ;
        carEditText[3]=carPlate ;


        carString[0]= user.userCar.carColour = carColour.getText().toString();
        carString[1]= user.userCar.carBrand = carBrand.getText().toString();
        carString[2]= user.userCar.carModel = carModel.getText().toString();
        carString[3]= user.userCar.carPlate = carPlate.getText().toString();

        boolean cancel = false;
        View focusView = null;


    }

    private void collectAddress(){
        addressEditText[0]=firstLine ;
        addressEditText[1]=secondLine ;
        addressEditText[2]=city ;
        addressEditText[3]=postcode ;
        addressEditText[4]=countryState;

        addressString[0]=  user.userAddress.firstline = firstLine.getText().toString();
        addressString[1]=  user.userAddress.secondline = secondLine.getText().toString();
        addressString[2]=  user.userAddress.city = city.getText().toString();
        addressString[3]=  user.userAddress.postcode = postcode.getText().toString();
        addressString[4]=  user.userAddress.countryState = countryState.getText().toString();

        boolean cancel = false;
        View focusView = null;


    }


    // On Back Pressed
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ProfileEdit.this, DrawerActivityMain.class);
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

        firstName = (EditText) findViewById(R.id.edittext_first_name_update);
        lastName = (EditText) findViewById(R.id.edittext_last_name_update);
        phoneNum = (EditText) findViewById(R.id.edittext_phone_num_update);
        carColour = (EditText) findViewById(R.id.edittext_car_colour_update);
        carBrand = (EditText) findViewById(R.id.edittext_car_brand_update);
        carModel = (EditText) findViewById(R.id.edittext_car_model_update);
        carPlate = (EditText) findViewById(R.id.edittext_car_plate_update);
        firstLine = (EditText) findViewById(R.id.edittext_first_line_update);
        secondLine = (EditText) findViewById(R.id.edittext_second_line_update);
        city = (EditText) findViewById(R.id.edittext_city_update);
        postcode = (EditText) findViewById(R.id.edittext_postcode_update);
        countryState = (EditText) findViewById(R.id.edittext_state_update);
        buttonExpandAddressLayout = (Button) findViewById(R.id.expand_address_button_update);
        buttonExpandNameNumLayout = (Button) findViewById(R.id.expand_name_button_update);
        buttonExpandCarLayout = (Button) findViewById(R.id.expand_car_button_update);
        expandableFirstName = (ExpandableLayout) findViewById(R.id.expand_first_name_update);
        expandableLastName = (ExpandableLayout) findViewById(R.id.expand_last_name_update);
        expandablePhoneNum = (ExpandableLayout) findViewById(R.id.expand_phone_number_update);
        expandableCarColour =(ExpandableLayout) findViewById(R.id.expand_car_colour_update);
        expandableCarBrand = (ExpandableLayout) findViewById(R.id.expand_car_brand_update);
        expandableCarModel = (ExpandableLayout) findViewById(R.id.expand_car_model_update);
        expandableCarPlate = (ExpandableLayout) findViewById(R.id.expand_car_plate_update);
        expandableFirstLineAddress = (ExpandableLayout) findViewById(R.id.expand_firstline_address_update);
        expandableSecondLineAddress = (ExpandableLayout) findViewById(R.id.expand_secondline_address_update);
        expandableCity = (ExpandableLayout) findViewById(R.id.expand_city_address_update);
        expandablePostcode = (ExpandableLayout) findViewById(R.id.expand_postcode_address_update);
        expandableCountryState = (ExpandableLayout) findViewById(R.id.expand_countryState_address_update);
        profileUpdate = (Button) findViewById(R.id.confirm_profile_update_button) ;
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

    public void profileSubmit(View v) {
        attemptProfileSetup();
    }





    private void attemptProfileSetup() {


        boolean checkNameNum = false;
        boolean checkAddress = false;
        boolean checkCar = false;

        collectUserName();
        collectCar();
        collectAddress();

        checkCar= emptyCheck(carString,carEditText, 4);
        checkAddress= emptyCheck(addressString,addressEditText, 5);
        checkNameNum = emptyCheck(nameNumString,nameNumEditText,3);

// do empty check if returns true, break operation
        if((checkCar) || (checkAddress) || (checkNameNum)) {
            return;
        }


        else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            StorageReference userFolder = storageRef.child("users/" + firebaseUserUID + "/profile.txt");
            objToByteStreamUpload(user, userFolder);

            //TODO intent to card swipe or other page ? maybe upload prof pic ?
           }
    }


    // CHECK IF FIELD IS EMPTY
    private boolean emptyCheck (String[] string, EditText[] editTexts , int size){

        boolean cancel =false;
        View focusView =null;

        for (int i = 0; i < size; i++){
            if (string[i].isEmpty()) {
                editTexts[i].setError(getString(R.string.error_field_required));
                focusView = editTexts[i];
                cancel = true;
            }



            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            }

        }

        return cancel;

    }
}