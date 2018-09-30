package com.opark.opark;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.login_auth.LoginActivity;
import com.opark.opark.model.User;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class UserProfileSetup extends AppCompatActivity {

    private static final String TAG = "UserProfileSetup" ;
    private ExpandableLayout expandableFirstName;
    private ExpandableLayout expandableNRIC;
    private ExpandableLayout expandableLastName;
    private ExpandableLayout expandablePhoneNum;
    private ExpandableLayout expandableCarColour;
    private TextInputLayout carColourTextInputLayout;
    private ExpandableLayout expandableCarBrand;
    private ExpandableLayout expandableCarModel;
    private ExpandableLayout expandableCarPlate;
//    private ExpandableLayout expandableFirstLineAddress;
//    private ExpandableLayout expandableSecondLineAddress;
//    private ExpandableLayout expandableCity;
//    private ExpandableLayout expandablePostcode;
//    private ExpandableLayout expandableCountryState;
    private EditText lastName ;
    private EditText firstName ;
    private EditText phoneNum ;
    private EditText carColour ;
    private EditText carBrand ;
    private EditText carModel ;
    private EditText carPlate ;
//    private EditText firstLine ;
//    private EditText secondLine ;
//    private EditText city ;
//    private EditText postcode ;
//    private EditText countryState;
    private Button profileSubmit;
//    private Button buttonExpandAddressLayout;
    private Button buttonExpandNameNumLayout;
    private Button buttonExpandCarLayout;
    private TextView buttonSignOut;
    public static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String [] carString = new String[4];
    private EditText [] carEditText =new EditText[4] ;
    private String [] nameNumString = new String[4];
    private EditText [] nameNumEditText = new EditText[4];
//    private String [] addressString = new String [5];
//    private EditText[] addressEditText = new EditText[5];
    private int mSelectedColor;
    private int userPoints = 0;
    private EditText icNumber;

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
                if (expandableFirstName.isExpanded()&&expandableLastName.isExpanded() && expandablePhoneNum.isExpanded()){

                    expandableFirstName.collapse();
                    expandableLastName.collapse();
                    expandablePhoneNum.collapse();
                    expandableNRIC.collapse();

                }
            else
                nameNumExpand();

            }
        });

// Expand Address Layout
/*        buttonExpandAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               addressExpand();


            }
        });
*/

 //Expand Car layout
        buttonExpandCarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carColourTextInputLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int[] mColors = getResources().getIntArray(R.array.default_rainbow);

                        ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                                mColors,
                                mSelectedColor,
                                5, // Number of columns
                                ColorPickerDialog.SIZE_SMALL,
                                true // True or False to enable or disable the serpentine effect
                                //0, // stroke width
                                //Color.BLACK // stroke color
                        );
                        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

                            @Override
                            public void onColorSelected(int color) {
                                mSelectedColor = color;

                            }

                        });
                        Log.d(TAG, "onClick: Dialog setup");

                        dialog.show(getFragmentManager(), "color_dialog_test");
                    }

                });


                if (expandableCarBrand.isExpanded()&&expandableCarColour.isExpanded()&&expandableCarPlate.isExpanded()&&expandableCarModel.isExpanded())
                {
                    expandableCarBrand.collapse();
                    expandableCarColour.collapse();
                    expandableCarPlate.collapse();
                    expandableCarModel.collapse();


                }



                else
               carExpand();

            }
        });



        //TODO Signout code
//        buttonSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.d("signout", "signoutbutton Clicked");
//                mAuth = FirebaseAuth.getInstance();
//                mAuth.signOut();
//                Intent intent = new Intent(UserProfileSetup.this, LoginActivity.class);
//                finish();
//                startActivity(intent);
//                    }
//                });



    }

    private void collectUserName(){


        nameNumEditText[0]= firstName;
        nameNumEditText[1]= lastName;
        nameNumEditText[2]= phoneNum;
        nameNumEditText[3]= icNumber;


      nameNumString[0]=  user.userName.firstName = firstName.getText().toString();
      nameNumString[1]=  user.userName.lastName = lastName.getText().toString();
      nameNumString[2]=  user.userName.phoneNum = phoneNum.getText().toString();
      nameNumString[3] = user.userName.icNumber=icNumber.getText().toString();

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

    /*private void collectAddress(){
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
*/

    // On Back Pressed
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(UserProfileSetup.this, LoginActivity.class);
        startActivity(intent);
    }

    public void objToByteStreamUpload(Object obj, StorageReference destination){

        String objStr = new Gson().toJson(obj);
        InputStream in = new ByteArrayInputStream(objStr.getBytes(Charset.forName("UTF-8")));
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
        icNumber = (EditText) findViewById(R.id.edittext_user_nric);

        carColour = (EditText) findViewById(R.id.edittext_car_colour);
        carBrand = (EditText) findViewById(R.id.edittext_car_brand);
        carModel = (EditText) findViewById(R.id.edittext_car_model);
        carPlate = (EditText) findViewById(R.id.edittext_car_plate);

      /*  firstLine = (EditText) findViewById(R.id.edittext_first_line);
        secondLine = (EditText) findViewById(R.id.edittext_second_line);
        city = (EditText) findViewById(R.id.edittext_city);
        postcode = (EditText) findViewById(R.id.edittext_postcode);
        countryState = (EditText) findViewById(R.id.edittext_state);
         buttonExpandAddressLayout = (Button) findViewById(R.id.expand_address_button);*/

         buttonExpandNameNumLayout = (Button) findViewById(R.id.expand_name_button);
         buttonExpandCarLayout = (Button) findViewById(R.id.expand_car_button);
        expandableFirstName = (ExpandableLayout) findViewById(R.id.expand_first_name);
        expandableLastName = (ExpandableLayout) findViewById(R.id.expand_last_name);
        expandablePhoneNum = (ExpandableLayout) findViewById(R.id.expand_phone_number);
        expandableNRIC = (ExpandableLayout) findViewById(R.id.expand_nric) ;
        expandableCarColour =(ExpandableLayout) findViewById(R.id.expand_car_colour);
        expandableCarBrand = (ExpandableLayout) findViewById(R.id.expand_car_brand);
        expandableCarModel = (ExpandableLayout) findViewById(R.id.expand_car_model);
        expandableCarPlate = (ExpandableLayout) findViewById(R.id.expand_car_plate);
        carColourTextInputLayout = (TextInputLayout) findViewById(R.id.car_color_text_input_layout);

       /* expandableFirstLineAddress = (ExpandableLayout) findViewById(R.id.expand_firstline_address);
        expandableSecondLineAddress = (ExpandableLayout) findViewById(R.id.expand_secondline_address);
        expandableCity = (ExpandableLayout) findViewById(R.id.expand_city_address);
        expandablePostcode = (ExpandableLayout) findViewById(R.id.expand_postcode_address);
        expandableCountryState = (ExpandableLayout) findViewById(R.id.expand_countryState_address);*/

        profileSubmit = (Button) findViewById(R.id.profile_submit_button) ;
        buttonSignOut = (TextView) findViewById(R.id.not_you_sign_out_button);
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

/*
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
*/

    }

    private void nameNumExpand(){
        expandableFirstName.expand();
        expandableLastName.expand();
        expandablePhoneNum.expand();
        expandableNRIC.expand();

        expandableCarColour.collapse();
        expandableCarBrand.collapse();
        expandableCarPlate.collapse();
        expandableCarModel.collapse();

    /*    expandableFirstLineAddress.collapse();
        expandableSecondLineAddress.collapse();
        expandableCity.collapse();
        expandablePostcode.collapse();
        expandableCountryState.collapse();*/
    }

    private void addressExpand(){
        expandableFirstName.collapse();
        expandableLastName.collapse();
        expandablePhoneNum.collapse();
        expandableNRIC.collapse();

        expandableCarColour.collapse();
        expandableCarBrand.collapse();
        expandableCarPlate.collapse();
        expandableCarModel.collapse();

  /*      expandableFirstLineAddress.expand();
        expandableSecondLineAddress.expand();
        expandableCity.expand();
        expandablePostcode.expand();
        expandableCountryState.expand();*/

    }

    private void carExpand(){
        expandableFirstName.collapse();
        expandableLastName.collapse();
        expandablePhoneNum.collapse();
        expandableNRIC.collapse();

        expandableCarColour.expand();
        expandableCarBrand.expand();
        expandableCarPlate.expand();
        expandableCarModel.expand();

/*
        expandableFirstLineAddress.collapse();
        expandableSecondLineAddress.collapse();
        expandableCity.collapse();
        expandablePostcode.collapse();
        expandableCountryState.collapse();*/
    }

    public void profileSubmit(View v) {
        attemptProfileSetup();
    }

    public void signOutUser (View v) {
        attemptUserSignOut();
    }

    private void attemptUserSignOut(){
        Log.d("signout", "signoutbutton Clicked");
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(UserProfileSetup.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }




    private void attemptProfileSetup() {


        boolean checkNameNum = false;
        boolean checkAddress = false;
        boolean checkCar = false;

        collectUserName();
        collectCar();
//        collectAddress();

       checkCar= emptyCheck(carString,carEditText, 4);
//       checkAddress= emptyCheck(addressString,addressEditText, 5);
        checkNameNum = emptyCheck(nameNumString,nameNumEditText,4);

// do empty check if returns true, break operation
        if((checkCar) || (checkAddress) || (checkNameNum)) {
            return;
        }


else {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference userFolder = storageRef.child("users/" + firebaseUserUID + "/profile.txt");
        objToByteStreamUpload(user, userFolder);


        StorageReference userRewardsFolder = storageRef.child("users/" +firebaseUserUID + "/points.txt");
        objToByteStreamUpload(userPoints,userRewardsFolder);

        //TODO intent to card swipe or other page ? maybe upload prof pic ?
        Intent intent = new Intent(UserProfileSetup.this, LoginActivity.class);
        finish();
        startActivity(intent);}
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







