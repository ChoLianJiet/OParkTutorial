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
import android.widget.TextView;
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
import com.opark.opark.share_parking.MapsMainActivity;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.id.list;
import static android.R.string.cancel;
import static android.view.View.GONE;

public class ProfileEdit extends AppCompatActivity {

    private static final String TAG = "ProfileEdit";
    private ExpandableLayout expandableFirstName;
    private ExpandableLayout expandableLastName;
    private ExpandableLayout expandablePhoneNum;
    private ExpandableLayout expandableCarColour;
    private ExpandableLayout expandableCarBrand;
    private ExpandableLayout expandableCarModel;
    private ExpandableLayout expandableCarPlate;
    private ExpandableLayout expandableNRIC;
    private EditText firstName ;
    private EditText lastName ;
    private EditText phoneNum ;
    private EditText carColour ;
    private EditText carBrand ;
    private EditText carModel ;
    private EditText carPlate ;
    private TextView buttonSignOut;
    private Button profileSubmit;
    private Button buttonExpandNameNumLayout;
    private Button buttonExpandCarLayout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String [] carString = new String[4];
    private EditText [] carEditText =new EditText[4] ;
    private String [] nameNumString = new String[4];
    private EditText [] nameNumEditText = new EditText[4];

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

        buttonSignOut.setVisibility(GONE);
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

        //Expand Car layout
        buttonExpandCarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        profileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptProfileUpdate();
            }
        });


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


    // On Back Pressed
    @Override
    public void onBackPressed() {
        this.finish();
//        Intent intent = new Intent(ProfileEdit.this, MapsMainActivity.class);
//
//        startActivity(intent);

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
        expandableNRIC.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

            }
        });


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

//        expandableFirstLineAddress.collapse();
//        expandableSecondLineAddress.collapse();
//        expandableCity.collapse();
//        expandablePostcode.collapse();
//        expandableCountryState.collapse();
    }

//    private void addressExpand(){
//        expandableFirstName.collapse();
//        expandableLastName.collapse();
//        expandablePhoneNum.collapse();
//
//        expandableCarColour.collapse();
//        expandableCarBrand.collapse();
//        expandableCarPlate.collapse();
//        expandableCarModel.collapse();
//
////        expandableFirstLineAddress.expand();
////        expandableSecondLineAddress.expand();
////        expandableCity.expand();
////        expandablePostcode.expand();
////        expandableCountryState.expand();
//
//    }

    private void carExpand(){
        expandableFirstName.collapse();
        expandableLastName.collapse();
        expandablePhoneNum.collapse();
        expandableNRIC.collapse();

        expandableCarColour.expand();
        expandableCarBrand.expand();
        expandableCarPlate.expand();
        expandableCarModel.expand();

//        expandableFirstLineAddress.collapse();
//        expandableSecondLineAddress.collapse();
//        expandableCity.collapse();
//        expandablePostcode.collapse();
//        expandableCountryState.collapse();
    }

    private void attemptProfileUpdate() {

        boolean checkNameNum = false;
//        boolean checkAddress = false;
        boolean checkCar = false;

        collectUserName();
        collectCar();

        checkCar= emptyCheck(carString,carEditText, 4);
//        checkAddress= emptyCheck(addressString,addressEditText, 5);
        checkNameNum = emptyCheck(nameNumString,nameNumEditText,4);

// do empty check if returns true, break operation
        if((checkCar)  || (checkNameNum)) {
            Log.d("emptyCheck","is Empty");
            return;
        }


        else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            StorageReference userFolder = storageRef.child("users/" + firebaseUserUID + "/profile.txt");
            Log.d(TAG, "attemptProfileUpdate: userFolder is "+ userFolder);

            userFolder.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.d("profileUpdate", "Profile Deleted Successful");
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference userFolder = storageRef.child("users/" + firebaseUserUID + "/profile.txt");
                    objToByteStreamUpload(user, userFolder);
                    Log.d("profileUpdate","Profile update successful");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d("profileUpdate", "Profile Delete FAILED");
                    showErrorDialog("Profile delete Failed");

                }
            });


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