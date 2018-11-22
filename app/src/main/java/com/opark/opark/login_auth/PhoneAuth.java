package com.opark.opark.login_auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.R;
import com.opark.opark.RegisterActivity;
import com.opark.opark.UserProfileSetup;
import com.opark.opark.share_parking.MapsMainActivity;

import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


/** * Created by Supriya on 9/11/2016. */
public class PhoneAuth extends Fragment {

    private FirebaseAuth mAuth;
    private EditText phoneEntry;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneAuthCallback;
    private String userPhoneNumber;
    private EditText verificationCodeEntry;
    private String mVerificationId;
    private Button verifyButton;
    private boolean mVerificationInProgress;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Button sendCodeButton;
    private String vCode;
    private FirebaseUser user;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final String TAG = "PhoneAuth";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    @Nullable    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.phoneauth_page,null);

        verificationCodeEntry = (EditText) v.findViewById(R.id.verification_code_entry);
        phoneEntry = (EditText) v.findViewById(R.id.phone_num_enter);
        verifyButton = (Button) v.findViewById(R.id.verify_button);
        sendCodeButton = (Button) v.findViewById(R.id.send_v_code);


        mAuth = FirebaseAuth.getInstance();



        sendCodeButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {

                                                  userPhoneNumber = phoneEntry.getText().toString().trim();

                                                  if (userPhoneNumber.isEmpty() || (userPhoneNumber.length() < 8)) {
                                                      Log.d(TAG, "Phone entry invalid");
                                                      phoneEntry.setError("Enter a valid mobile");
                                                      phoneEntry.requestFocus();

                                                      return;
                                                  }
                                                  else {
//                              sendCodeButton.startAnimation();
                                                      sendVerificationCode(userPhoneNumber);
                                                      sendCodeButton.setVisibility(View.GONE);
                                                      Log.d(TAG, "Phone Number Entered");
                                                  }
                                              }
                                          }
        );

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vCode = verificationCodeEntry.getText().toString().trim();
                verifyVerificationCode(vCode);




            }
        });


        phoneAuthCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                vCode = credential.getSmsCode();


/*                 This callback will be invoked in two situations:
                 1 - Instant verification. In some cases the phone number can be instantly
                     verified without needing to send or enter a verification code.

                 2 - Auto-retrieval. On some devices Google Play services can automatically
                     detect the incoming verification SMS and perform verification without
                     user action.*/



                if (vCode != null) {
                    verificationCodeEntry.setText(vCode);
                    //verifying the code
                    verifyVerificationCode(vCode);
                }

                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                phoneEntry.setText("");
                verifyButton.setVisibility(View.GONE);
                sendCodeButton.setVisibility(View.VISIBLE);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }


            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                super.onCodeSent(mVerificationId, token);
                Log.d(TAG, "onCodeSent:" + verificationId);
                verifyButton.setVisibility(View.VISIBLE);


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


            }
        };






        return v;

    }











/*
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            LoginActivity.mViewPager.setCurrentItem(LoginActivity.mViewPager.getCurrentItem() - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/






    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }



    private void sendVerificationCode(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+60"+ phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                phoneAuthCallback);        // OnVerificationStateChangedCallbacks
    }






    //Sign in User
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Log.d(TAG, "sign in phone auth credential successful");
                            user = mAuth.getCurrentUser();
                            updateUI(user);
//                            Intent intent = new Intent(getActivity(), MapsMainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            getActivity().finish();
//                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            phoneEntry.setError("Please check the validity of your number");
                            phoneEntry.requestFocus();


                        }
                    }
                });
    }



    private void updateUI(FirebaseUser currentUser) {
        try {
            if (currentUser != null) {

                final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/profile.txt");
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // file exists
                        Log.d("login", "user logged in");
                        Log.d("urlget", "uri: " + uri.toString());
                        Toast.makeText(getActivity(), "Log in Successful! :D ",
                                Toast.LENGTH_SHORT).show();


                        //TODO Add Intent to Drawer Activity onSuccess
                        Intent intent = new Intent(getActivity(), MapsMainActivity.class);
                        intent.putExtra("firebaseUser", currentUserID);
                        getActivity().finish();
                        startActivity(intent);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //file not found
                        String firebaseUserUID = mAuth.getCurrentUser().getUid();
                        Log.d("urlget", "New User, No profile");
                        Intent intent = new Intent(getActivity(), UserProfileSetup.class);
                        intent.putExtra("firebaseUser", firebaseUserUID);
                        getActivity().finish();
                        startActivity(intent);
                    }
                });
            } else {
                // No user is signed in
                Log.d("login", "no user");
                return;
            }
        } catch (NullPointerException e) {

            Log.d("Login Null", "login null caught");


        }
    }
}