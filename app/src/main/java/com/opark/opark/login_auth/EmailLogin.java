package com.opark.opark.login_auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.opark.opark.BuildConfig;
import com.opark.opark.R;
import com.opark.opark.UserProfileSetup;
import com.opark.opark.share_parking.MapsMainActivity;

import static com.facebook.FacebookSdk.getApplicationContext;


/** * Created by Supriya on 9/11/2016. */
public class EmailLogin extends Fragment {

    private FirebaseAuth mAuth;
    // UI references.

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CallbackManager callbackManager;
    LoginButton facebookloginButton;
    private Button registerButton;
    private Button signInButton;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private int position;

    @Nullable    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_login_relative,container,false);


        mEmailView = (AutoCompleteTextView) v.findViewById(R.id.email_login_autocompletetextview);
        mPasswordView = (EditText) v.findViewById(R.id.password_edit_text);
        registerButton = (Button) v.findViewById(R.id.register_button);
        facebookloginButton = (LoginButton) v.findViewById(R.id.facebook_login_button);
        signInButton = (Button) v.findViewById(R.id.sign_in_button);
        signInButton = (Button) v.findViewById(R.id.sign_in_button);

        FacebookSdk.setApplicationId("113991652589123");
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    System.out.println("User logged in");
                } else {
                    System.out.println("User not logged in");
                }
            }
        };





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


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInRegisteredUser(v);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser(v);
            }
        });


        //FACEBOOK LOGIN BUTTON
        facebookloginButton.setReadPermissions("email", "public_profile");

        facebookloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInRegisteredUser(v);
            }
        });
        //FACEBOOK HASH KEY Generating
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.opark.opark",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }


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
                        Toast.makeText(getContext(), "Error in register callback : " + exception.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        return v;
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.d("emaillogin","Fragment is created");
//    }

//    @Override
//    public void onViewCreated(View v,  @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(v, savedInstanceState);
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        mAuth.addAuthStateListener(mAuthListener);
//
//        updateUI(currentUser);
//    }


    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {

            final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/profile.txt");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // file exists
                    Log.d("login", "user logged in");
                    Log.d("urlget", "uri: " + uri.toString());
                    Toast.makeText(getActivity().getApplicationContext(), "Log in Successful! :D ",
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
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("facebook token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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
//                            Toast.makeText(getApp, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
    }

    // Executed when Sign in button pressed

    public void signInRegisteredUser(View v) {

        Log.d("loginbutton","loginbutton pressed") ;
        attemptLogin();

    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(getActivity().getApplicationContext(), com.opark.opark.RegisterActivity.class);
        getActivity().finish();
        startActivity(intent);
    }


    // Attempt Login Method
    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.equals("") || password.equals("")) {
            return;
        }

//        Toast.makeText(this, "Login you to Opark !", Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("OparkLogin", "Log in successfull ? " + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.d("OparkLogin", "Problem signing in : " + task.getException());
                    showErrorDialog("Problem signing you in. Try again maybe ? ");
                } else {
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    updateUI(currentUser);

                }
            }
        });


    }


// AlertDialog


    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getActivity().getApplicationContext())
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }







}






