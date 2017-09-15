package com.opark.opark;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {



    private FirebaseAuth mAuth;
    // UI references.

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CallbackManager callbackManager;
    LoginButton loginButton;
    private Button registerButton;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("113991652589123");
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in");
                }
                else{
                    System.out.println("User not logged in");
                }
            }
        };





        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_login_autocompletetextview) ;
        mPasswordView = (EditText) findViewById(R.id.password_edit_text) ;
        registerButton = (Button) findViewById(R.id.register_button);



        // attempt login
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser(v);
            }
        });



        //FACEBOOK LOGIN BUTTON
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");


        //FACEBOOK HASH KEY Generating
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.opark.opark",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }



        // FACEBOOK CALLBACK MANAGER
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getApplicationContext(), loginResult.toString(), Toast.LENGTH_LONG).show();
                        Log.d("facebook login", "facebook:onSuccess:" + loginResult);

                        handleFacebookAccessToken(loginResult.getAccessToken());
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }


                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Error in register callback : " + exception.toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);

        updateUI(currentUser);
    }



    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {

            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/profile.txt");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // file exists
                    Log.d("login","user logged in");
                    Log.d("urlget", "uri: " + uri.toString());
                    Toast.makeText(LoginActivity.this, "Log in Successful! :D ",
                            Toast.LENGTH_SHORT).show();

                    //TODO Add Intent to Card Swipe onSuccess

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //file not found
                    String firebaseUserUID = mAuth.getCurrentUser().getUid();
                    Log.d("urlget","New User, No profile");
                    Intent intent = new Intent(LoginActivity.this, UserProfileSetup.class);
                    intent.putExtra("firebaseUser",firebaseUserUID);
                    finish();
                    startActivity(intent);
                }
            });}
         else {
            // No user is signed in
            Log.d("login","no user");
            return;
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("facebook token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("facebook token", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("facebook token", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
    }

    // Executed when Sign in button pressed
    public void signInRegisteredUser(View v)   {
        attemptLogin();

    }



    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.opark.opark.RegisterActivity.class);
        finish();
        startActivity(intent);
    }


// Attempt Login Method
    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.equals("")|| password.equals(""))
        {
            return;
        }

        Toast.makeText(this,"Login you to Opark !",Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d ("OparkLogin", "Log in successfull ? " + task.isSuccessful() );

                if (!task.isSuccessful()){
                    Log.d("OparkLogin", "Problem signing in : " + task.getException());
                    showErrorDialog("Problem signing you in. Try again maybe ? ");
                }
                else {
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    updateUI(currentUser);

                }
            }
        });


    }



// AlertDialog


    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}












    



