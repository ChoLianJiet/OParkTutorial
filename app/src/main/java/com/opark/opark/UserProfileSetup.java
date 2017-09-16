package com.opark.opark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.opark.opark.card_swipe.MainActivityCardSwipe;

import net.cachapa.expandablelayout.ExpandableLayout;

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




    private static  User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_setup);



        // LINKING VARIABLES TO RESPECTIVE IDs
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
        Button buttonExpandAddressLayout = (Button) findViewById(R.id.expand_address_button);
        Button buttonExpandNameNumLayout = (Button) findViewById(R.id.expand_name_button);
        Button buttonExpandCarLayout = (Button) findViewById(R.id.expand_car_button);
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


        //Provided by Library but haven't test if useful or not
        expandableFirstName.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

//                user.userName.firstName = firstName.getText().toString();
                Log.d("ExpansionListener","userName state:" + state );

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




 



        /**EXPANDING LAYOUT**/
        //Expand Name and Num layout
        buttonExpandNameNumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });



// Expand Address Layout
        buttonExpandAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

 //Expand Car layout
        buttonExpandCarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


        profileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectUserName();
                collectCar();
                collectAddress();
                //TODO add in all other methods to package it as a JSON
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


}
