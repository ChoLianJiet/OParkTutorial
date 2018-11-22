package com.opark.opark.merchant_side;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.glomadrian.codeinputlib.CodeInput;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.R;
import com.opark.opark.merchant_side.merchant_class.Merchant;
import com.opark.opark.model.RewardsPreredemption;
import com.opark.opark.model.User;

import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MerchActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher, AdapterView.OnItemSelectedListener {
    private static final String TAG = "MerchActivity";
    private DatabaseReference offerlistDatabaseRef;
    private DatabaseReference offerlistMerchantDataRef;
    private StorageReference merchantProfileNameRef;
    private String merchantProfileName;
    private String merchantEmail;
    private String offerSelected;
    private String redeemedKey;

    private EditText code1;
    private EditText code2;
    private EditText code3;
    private EditText code4;
    private EditText code5;
    private EditText code6;
    private EditText mPinHiddenEditText;

    private Spinner whichOffer;
    ImageButton merchProfImgButton;
    ImageButton uploadOfferImgButton;
    ImageButton contactSupportImgButton;
    TextView amountRedeemedTextView;
    TextView errorCode;
    StorageReference rewardStoRef;
    String rewardsCodeString;
    String redeemUserId;
    String tobeDelete;
    int keyLength;

    long amountRedeemed;


    DatabaseReference amountRedeemedDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merch);


        bindViews();

        setPINListeners();


        merchantEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        merchantProfileNameRef = FirebaseStorage.getInstance().getReference().child("merchants/merchantlist/" + merchantEmail + "/merchProf.txt");

        Log.d(TAG, "onCreate:  merchantProfileRef " + merchantProfileNameRef);
        merchantProfileNameRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final long ONE_MEGABYTE = 1024 * 1024;
                merchantProfileNameRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(TAG, "onSuccess:  ");
                        try {
                            merchantProfileName = new Gson().fromJson(new String(bytes, "UTF-8"), Merchant.class).getMerchCoName();
                            Log.d(TAG, "onSuccess:merch name is  "+ merchantProfileName);

                            getMerchStuff();





                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });







       /* rewardsCode.addOnCompleteListener(new OnCodeCompleteListener() {
            @Override
            public void onCompleted(String code) {

                rewardsCodeString=  rewardsCode.getCode();

                Log.d("redeem", "merchants entry" + rewardsCodeString);
                rewardStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/kuihmuih 1 ringgit/userredemptioncode/" +rewardsCodeString);
                rewardStoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        final long ONE_MEGABYTE = 1024 * 1024;
                        rewardStoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Log.d("Byte", "getByte success");

                                rewardsCode.setEditable(true);




                                try {
                                    redeemUserId = new Gson().fromJson(new String(bytes, "UTF-8"), String.class);
                                    offerlistDatabaseRef.child("kuihmuih 1 ringgit").child("redeemedUsers").push().setValue(redeemUserId);

                                } catch (UnsupportedEncodingException e) {e.printStackTrace();}


                            }

                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        rewardsCode.setEditable(true);
                        rewardsCode.setError("Code Incorrect");
                    }
                });


            }
        });*/








        uploadOfferImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent uploadOfferIntent = new Intent(MerchActivity.this, MerchUploadOfferActivity.class);
                startActivity(uploadOfferIntent);


            }
        });




    }

    private void bindViews() {
        amountRedeemedTextView = findViewById(R.id.amount_redeemed);
        merchProfImgButton = findViewById(R.id.merchant_profile);
        uploadOfferImgButton = findViewById(R.id.upload_offer);
        contactSupportImgButton = findViewById(R.id.contact_support);
        code1 = (EditText) findViewById(R.id.code1);
        code2 = (EditText) findViewById(R.id.code2);
        code3 = (EditText) findViewById(R.id.code3);
        code4 = (EditText) findViewById(R.id.code4);
        code5 = (EditText) findViewById(R.id.code5);
        code6 = (EditText) findViewById(R.id.code6);
        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);
//        rewardsCode =  findViewById(R.id.rewards_code);
        errorCode = findViewById(R.id.error_code_text);
        whichOffer = findViewById(R.id.which_offer_spinner);


    }










    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.append("");

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    public void hideSoftKeyboard(EditText editText){
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {


        if (s.length() == 0) {
//            setFocusedPinBackground(code1);
            errorCode.setVisibility(View.INVISIBLE);

            Log.d("redeem", "no word" + s.toString());
            Log.d("redeem", "hidden: " + mPinHiddenEditText.getText().toString());

            code1.setText("");
        } else if (s.length() == 1) {
//            setFocusedPinBackground(code2);
            Log.d("redeem", "1 word" + s.toString());
            Log.d("redeem", "hidden: " + mPinHiddenEditText.getText().toString());

            code1.setText(s.charAt(0) + "");
            code2.setText("");
            code3.setText("");
            code4.setText("");
            code5.setText("");
            code6.setText("");

        } else if (s.length() == 2) {

            Log.d("redeem", "2 word" + s.toString());
            Log.d("redeem", "hidden: " + mPinHiddenEditText.getText().toString());


//            setFocusedPinBackground(code3);
            code2.setText(s.charAt(1) + "");
            code3.setText("");
            code4.setText("");
            code5.setText("");
            code6.setText("");

        } else if (s.length() == 3) {

            Log.d("redeem", "3 word" + s.toString());
            Log.d("redeem", "hidden: " + mPinHiddenEditText.getText().toString());

//            setFocusedPinBackground(code4);
            code3.setText(s.charAt(2) + "");
            code4.setText("");
            code5.setText("");
            code6.setText("");

        } else if (s.length() == 4) {

            Log.d("redeem", "4 word" + s.toString());
            Log.d("redeem", "hidden: " + mPinHiddenEditText.getText().toString());

//            setFocusedPinBackground(code5);
            code4.setText(s.charAt(3) + "");
            code5.setText("");
            code6.setText("");

        } else if (s.length() == 5) {
            Log.d("redeem", "5 word" + s.toString());
            Log.d("redeem", "hidden: " + mPinHiddenEditText.getText().toString());

//            setDefaultPinBackground(code6);
            code5.setText(s.charAt(4) + "");
            code6.setText("");

        } else if (s.length() == 6) {
            Log.d("redeem", "6 word" + s.toString());
            Log.d("redeem", "hidden: " + mPinHiddenEditText.getText().toString());

//            setDefaultPinBackground(code6);
            code6.setText(s.charAt(5) + "");

            Log.d("redeem", "hidden: " + mPinHiddenEditText.getText().toString());

            Log.d("redeem", "s.toString(): " + s.toString());


            hideSoftKeyboard(code6);

            rewardsCodeString= s.toString();
            Log.d("redeem", "merchants entry" + rewardsCodeString);
            rewardStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/"+ merchantProfileName + "/" + offerSelected + "/userredemptioncode/" +rewardsCodeString);
            rewardStoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    final long ONE_MEGABYTE = 1024 * 1024;
                    rewardStoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Log.d("Byte", "getByte success");

                            errorCode.setVisibility(View.INVISIBLE);
                            mPinHiddenEditText.setText("");
                            code1.setText("");
                            code2.setText("");
                            code3.setText("");
                            code4.setText("");
                            code5.setText("");
                            code6.setText("");


                            try {
                                redeemUserId = new Gson().fromJson(new String(bytes, "UTF-8"), RewardsPreredemption.class).getRedeemedUserId();
                                redeemedKey = new Gson().fromJson(new String(bytes, "UTF-8"), RewardsPreredemption.class).getPushKey();

                                offerlistDatabaseRef.child(offerSelected).child("redeemedUsers").push().setValue(redeemUserId);
                                rewardStoRef.delete();
                                DatabaseReference userPreRedemList = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/" + redeemUserId +"/" +redeemedKey);
                                userPreRedemList.removeValue();

                            } catch (UnsupportedEncodingException e) {e.printStackTrace();}


                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG, "onFailure: " + offerSelected);

                    Log.d(TAG, "onFailure: rewards Storef "+ rewardStoRef);
                            errorCode.setVisibility(View.VISIBLE);





                }
            });







        }



    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.code1:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);

                }
                break;

            case R.id.code2:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);

                }
                break;

            case R.id.code3:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);


                }
                break;

            case R.id.code4:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);

                }
                break;

            case R.id.code5:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);

                }
            case R.id.code6:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);

                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {


                        keyLength = mPinHiddenEditText.getText().length();
                        StringBuilder delete ;

                        switch(keyLength){

                            case 6:
                                Log.d("redeem", "onKey: key length " + keyLength);
                                Log.d("redeem", "hidden text before delete : " + mPinHiddenEditText.getText().toString());
                                tobeDelete=  mPinHiddenEditText.getText().toString();
//
                                 delete = new StringBuilder(tobeDelete);
                                delete.deleteCharAt(5);
                                mPinHiddenEditText.setText(delete.toString());
                                code6.setText("");



                                Log.d("redeem", "delete 6 :  " + delete.toString() );
                                Log.d("redeem", "mPinHiddenEditText " +mPinHiddenEditText.getText().toString());


                                break;

                            case 5:
                                Log.d("redeem", "onKey: key length " + keyLength);

                                Log.d("redeem", "hidden text before delete : " + mPinHiddenEditText.getText().toString());
                                tobeDelete=  mPinHiddenEditText.getText().toString();

                                delete = new StringBuilder(tobeDelete);
                                delete.deleteCharAt(4);
                                mPinHiddenEditText.setText(delete.toString());

                                code5.setText("");
                                Log.d("redeem", "delete 5 :  " + delete.toString() );
                                Log.d("redeem", "mPinHiddenEditText " +mPinHiddenEditText.getText().toString());


                                break;


                            case 4:
                                Log.d("redeem", "onKey: key length " + keyLength);

                                Log.d("redeem", "hidden text before delete : " + mPinHiddenEditText.getText().toString());


                                tobeDelete=  mPinHiddenEditText.getText().toString();
//
                                delete = new StringBuilder(tobeDelete);
                                delete.deleteCharAt(3);
                                mPinHiddenEditText.setText(delete.toString());

                                code4.setText("");

                                Log.d("redeem", "delete 4 :  " + delete.toString() );
                                Log.d("redeem", "mPinHiddenEditText " +mPinHiddenEditText.getText().toString());


                                break;

                            case 3:
                                Log.d("redeem", "onKey: key length " + keyLength);

                                Log.d("redeem", "hidden text before delete : " + mPinHiddenEditText.getText().toString());
                                tobeDelete=  mPinHiddenEditText.getText().toString();
//
                                delete = new StringBuilder(tobeDelete);
                                delete.deleteCharAt(2);
                                mPinHiddenEditText.setText(delete.toString());

                                code3.setText("");
                                Log.d("redeem", "delete 3 :  " + delete.toString() );
                                Log.d("redeem", "mPinHiddenEditText " +mPinHiddenEditText.getText().toString());
                                Log.d("redeem", "onKey: key length " + keyLength);


                                break;



                            case 2:
                                Log.d("redeem", "************** key length " + keyLength);

                                Log.d("redeem", "hidden text before delete case2 : " + mPinHiddenEditText.getText().toString());

//
                                tobeDelete=  mPinHiddenEditText.getText().toString();
//
                                delete = new StringBuilder(tobeDelete);
                                delete.deleteCharAt(1);
                                mPinHiddenEditText.setText(delete.toString());
//
                                code2.setText("");

                                Log.d("redeem", "code2 has focus ?? " + code2.hasFocus());

//                                showSoftKeyboard(code2);
//                                code1.setText(delete.toString());
                                Log.d("redeem", "delete 2 :  " + delete.toString() );
                                Log.d("redeem", "mPinHiddenEditText " +mPinHiddenEditText.getText().toString());
                                Log.d("redeem", "*********** key length after " + keyLength);


                                break;



                            case 1:
                                Log.d("redeem", "************** key length " + keyLength);

                                Log.d("redeem", "hidden text before delete : " + mPinHiddenEditText.getText().toString());


                                tobeDelete=  mPinHiddenEditText.getText().toString();

                                delete = new StringBuilder(tobeDelete);
                                delete.deleteCharAt(0);
                                mPinHiddenEditText.setText(delete.toString());

                                code1.setText("");
                                Log.d("redeem", "delete 1 :  " + delete.toString() );
                                Log.d("redeem", "mPinHiddenEditText " +mPinHiddenEditText.getText().toString());
                                Log.d("redeem", "************** key length " + keyLength);

                                break;


                            default:

                                break;

                        }

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }




    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        code1.setOnFocusChangeListener(this);
        code2.setOnFocusChangeListener(this);
        code3.setOnFocusChangeListener(this);
        code4.setOnFocusChangeListener(this);
        code5.setOnFocusChangeListener(this);
        code6.setOnFocusChangeListener(this);

        code1.setOnKeyListener(this);
        code2.setOnKeyListener(this);
        code3.setOnKeyListener(this);
        code4.setOnKeyListener(this);
        code5.setOnKeyListener(this);
        code6.setOnKeyListener(this);

        mPinHiddenEditText.setOnKeyListener(this);
    }






    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);

    }


public void getMerchStuff() {
    final ArrayList<String> allOfferOfThisMerchant = new ArrayList<>();


    Log.d(TAG, "onCreate: Merch Name " + merchantProfileName);
    offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");
    offerlistMerchantDataRef = FirebaseDatabase.getInstance().getReference().child("offerlist/merchantsName/" + merchantProfileName);
    offerlistMerchantDataRef.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            dataSnapshot.getKey();
            Log.d(TAG, "datasnapshot is  " + dataSnapshot);

            Log.d(TAG, "get Key() of push " + dataSnapshot.getKey());
            allOfferOfThisMerchant.add(String.valueOf(dataSnapshot.getValue()));
            ArrayAdapter<String> offerAdapter = new ArrayAdapter<String>(MerchActivity.this,
                    android.R.layout.simple_spinner_item, allOfferOfThisMerchant);

            whichOffer.setOnItemSelectedListener(MerchActivity.this);
            offerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            whichOffer.setAdapter(offerAdapter);




            Log.d(TAG, "all Offer of this Merchant " + allOfferOfThisMerchant);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


        Log.d(TAG, "onItemSelected:  selected Item Pos " +adapterView.getSelectedItemPosition());
        Log.d(TAG, "onItemSelected: get Item at pos " + adapterView.getItemAtPosition(position));
        offerSelected = String.valueOf(adapterView.getItemAtPosition(position)) ;
        Log.d(TAG, "onItemSelected:  offerSelected is " + offerSelected);




        amountRedeemedDatabase = FirebaseDatabase.getInstance().getReference("offerlist/" + offerSelected +  "/redeemedUsers");
        amountRedeemedDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, "onDataChange:  amound redeemed database " + amountRedeemedDatabase);
                Log.d(TAG, "offer Selected for redeemed users count " + offerSelected);
                Log.d(TAG, "onDataChange:  datasnapshot of database update redeemed users "+ dataSnapshot);
                amountRedeemed = dataSnapshot.getChildrenCount();

                amountRedeemedTextView.setText(String.valueOf(amountRedeemed));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}