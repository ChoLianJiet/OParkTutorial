package com.opark.opark;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.login_auth.LoginActivity;
import com.opark.opark.merchant_side.MerchProfileSetup;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity" ;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Switch merchantSwitch;
    private Switch userSwitch;
    public static boolean isMerchant ;
    public static boolean isUser;

    // Firebase instance variables
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        merchantSwitch = (Switch) findViewById(R.id.merchant_switch);
        userSwitch = findViewById(R.id.user_switch);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });



        mAuth = FirebaseAuth.getInstance();


        userSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ((isChecked)&&(merchantSwitch.isChecked())){
                    userSwitch.setChecked(true);
                    merchantSwitch.setChecked(false);
//                    merchantSwitch.toggle();

                    Log.d("switch", "onCheckedChanged: user " + userSwitch.isChecked());
                    Log.d("switch", "onCheckedChanged: merch" + merchantSwitch.isChecked());

                }

                else if ((isChecked)&&(!merchantSwitch.isChecked())){
                    userSwitch.setChecked(true);
                    merchantSwitch.setChecked(false);
                    Log.d("switch", "onCheckedChanged: user" + userSwitch.isChecked());
                    Log.d("switch", "onCheckedChanged: merch" + merchantSwitch.isChecked());

                }




            }
        });



        merchantSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ((isChecked)&&(userSwitch.isChecked())){
                    merchantSwitch.setChecked(true);
                    userSwitch.setChecked(false);
//                    merchantSwitch.toggle();

                    Log.d("switch", "onCheckedChanged: user " + userSwitch.isChecked());
                    Log.d("switch", "onCheckedChanged: merch" + merchantSwitch.isChecked());

                }

                else if ((isChecked)&&(!userSwitch.isChecked())){
                    merchantSwitch.setChecked(true);
                    userSwitch.setChecked(false);
                    Log.d("switch", "onCheckedChanged: user" + userSwitch.isChecked());
                    Log.d("switch", "onCheckedChanged: merch" + merchantSwitch.isChecked());

                }




            }
        });


    }


    // Executed when Sign Up button is pressed.
    public void signUp(View v) {
        attemptRegistration();
    }



    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);




        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        Log.d(TAG, "outside:  Password is" +password);

        Log.d(TAG, "outside TextUtils not empty" + (TextUtils.isEmpty(password)));
        Log.d(TAG, "outside Password not Valid" + !isPasswordValid(password)) ;
        Log.d(TAG, "password lower " + password.toLowerCase());
        Log.d(TAG, "password upper " + password.toUpperCase());

        Log.d(TAG, "outside: password not all lower case" + !password.equals(password.toLowerCase()));
        Log.d(TAG, "outside: password not all Upper case" + !password.equals(password.toUpperCase()));

        Log.d(TAG, "outside: password matches d+" + !password.matches(".*\\d+.*"));

        Log.d(TAG, "everthing : " + ((TextUtils.isEmpty(password) || !isPasswordValid(password)) || password.equals(password.toLowerCase()) ||
                password.equals(password.toUpperCase()) || !password.matches(".*\\d+.*")));


        boolean cancel = false;
        View focusView = null;

        //TODO to check the logic
        // Check for a valid password, if the user entered one.
        if ((TextUtils.isEmpty(password) || !isPasswordValid(password)) || password.equals(password.toLowerCase()) ||
                password.equals(password.toUpperCase()) || !password.matches(".*\\d+.*"))
        {
            Log.d(TAG, "attemptRegistration:  Password is" +password);
            Log.d(TAG, "attemptRegistration: isEmpty ? " +!TextUtils.isEmpty(password) );
            Log.d(TAG, "attemptRegistration:  is Valid ? " + !isPasswordValid(password));

            Log.d(TAG, "password lower " + password.toLowerCase());
            Log.d(TAG, "password upper " + password.toUpperCase());

            Log.d(TAG, "attemptRegistration: password lower case" + !password.equals(password.toLowerCase()));
            Log.d(TAG, "attemptRegistration: password Upper case" + !password.equals(password.toUpperCase()));

            Log.d(TAG, "attemptRegistration: d+ " + !password.matches(".*\\d+.*"));

            mPasswordView.setError(getString(R.string.error_invalid_password));

            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))  {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            createFirebaseUser();

        }
    }


    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return (email.contains("@") && email.contains("."));
    }

    private boolean isPasswordValid(String password) {
        //password confirm and password length > 4
        String confirmPassword = mConfirmPasswordView.getText().toString();






        return confirmPassword.equals(password) && password.length() >= 6;
    }


    // Firebase User creation

    private void    createFirebaseUser(){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        isMerchant=checkIsMerchant();
        isUser= checkIsUser();
        if((!isMerchant)&&(!isUser)){

            showErrorDialog("Identity","Please select User or Merchant");
            Log.d("error", "onComplete: Please select either merchant or user");

           } else {

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("OparkRegister", " createUser onComplete" +  task.isSuccessful());

                if (!task.isSuccessful()){
                    Log.d ("OparkRegister", "createUser Failed");
                    showErrorDialog("Something went wrong","Registration attempt failed");

                } else {
                    isMerchant=checkIsMerchant();
                    isUser= checkIsUser();


                    if ((isMerchant )&& (!isUser)){

                        Log.d("identity", "onComplete: is merchant" );

                        //Create merchant temp file for checking
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        final String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        StorageReference merchFolder = storageRef.child("merchants/" + "merchantlist/" + currentUserEmail  + "/isMerchant.txt");
                        objToByteStreamUpload(isMerchant, merchFolder);


                        Intent merchantIntent = new Intent(RegisterActivity.this,MerchProfileSetup.class);
                        finish();
                        startActivity(merchantIntent);


                    }else if ((isUser)&&(!isMerchant)){
//

                        Log.d("identity", "onComplete: is user" );

                        Intent intent = new Intent (RegisterActivity.this, UserProfileSetup.class);
                        finish();
                        startActivity(intent);}


                    else {

                        Log.d("error", "onComplete: Please select either merchant or user");


                    }


                }



            }
        });}
        }



// On Back Pressed
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }



    private boolean checkIsMerchant(){
        if (merchantSwitch.isChecked()){

            Log.d("switch", "checkIsMerchant:  is merchant" + merchantSwitch.isChecked());

            return  true;
        }else  return false;


    }

    private boolean checkIsUser(){
        if (userSwitch.isChecked()){

            Log.d("switch", "checkIsMerchant:  is user" + userSwitch.isChecked());

//            merchantSwitch.setChecked(false);
//            merchantSwitch.toggle();
            return  true;
        }else  return false;


    }

// Error Dialog

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void objToByteStreamUpload(Object obj, StorageReference destination){

        String objStr = new Gson().toJson(obj);
        InputStream in = new ByteArrayInputStream(objStr.getBytes(Charset.forName("UTF-8")));
        UploadTask uploadTask = destination.putStream(in);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                showErrorDialog("Profile Registration Failed.","Failed to update your profile. Try again maybe? ");
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

}