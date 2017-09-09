package com.opark.opark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private Button buttonExpandCarLayout;
    private Button buttonExpandNameNumLayout;
    private Button buttonExpandAddressLayout;

    private EditText firstName;
    private EditText lastName;
    private EditText phoneNum;
    private EditText carColour;
    private EditText carBrand;
    private EditText carModel;
    private EditText carPlate;
    private EditText firstLine;
    private EditText secondLine;
    private EditText city;
    private EditText postcode;
    private EditText countryState;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_setup);

        buttonExpandAddressLayout =(Button) findViewById(R.id.expand_address_button);
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

        expandableFirstName.collapse();
        expandableLastName.collapse();
        expandablePhoneNum.collapse();

        expandableCarColour.collapse();
        expandableCarBrand.collapse();
        expandableCarPlate.collapse();
        expandableCarModel.collapse();

        expandableFirstLineAddress.collapse();
        expandableSecondLineAddress.collapse();
        expandableCity.collapse();
        expandablePostcode.collapse();
        expandableCountryState.collapse();


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




    }
}
