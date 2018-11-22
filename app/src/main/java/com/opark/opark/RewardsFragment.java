package com.opark.opark;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.model.RewardsPreredemption;
import com.opark.opark.model.RewardsRecord;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RewardsFragment extends Fragment implements MerchantOfferAdapter.ButtonClicked {
    private static final String TAG = "RewardsFragment";
    private List<MerchantOffer> merchantOffer = new ArrayList<>();
    MerchantOffer thisMerchantOffer = new MerchantOffer();
    private DatabaseReference offerlistDatabaseRef;
    private DatabaseReference rewardsDatabaseRef;


    private StorageReference userPointsStorageRef;
    private StorageReference rewardsRedemptionRecord;


    private FirebaseAuth mAuth;
    private FirebaseUser thisUser;
    private String redeemUid;
    public static String merchantName;
    public static String merchantOfferTitle;
    public static String merchantAddress;
    public static String merchantOfferImageUrl;
    final long ONE_MEGABYTE = 1024 * 1024;
    private int userPoints;
    private int userPointsBefRed;
    private int pointsAfterRedemption;
    public static int redeemCost;
    public static String merchantContact;
    public static String rewardsMerchant;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;



    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_offers_recycler_view,container,false);

//try {
        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");
    RecyclerView merchantRecView = (RecyclerView) view.findViewById(R.id.merchant_offer_recview);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    merchantRecView.setHasFixedSize(true);
    merchantRecView.setLayoutManager(linearLayoutManager);

    mAuth = FirebaseAuth.getInstance();
    thisUser = mAuth.getCurrentUser();
    redeemUid = thisUser.getUid();
    userPointsStorageRef = FirebaseStorage.getInstance().getReference().child("users/" + redeemUid + "/points.txt");




    initializeData(merchantRecView);

        return view;
    }



    private void initializeData(final RecyclerView merchantRecView){

        Log.d("INITDATA", "initializeData: initialising data");


//        merchantOffer = new ArrayList<>();
        offerlistDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
                Log.d(TAG, "onChildAdded: datasnapshot " +  String.valueOf(dataSnapshot.child("offerCost").getValue()));
                Log.d(TAG, "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());


                    Log.d(TAG, "onChildAdded: not adding " + dataSnapshot.hasChild("merchantsName"));

//                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue())));

                if (!dataSnapshot.getKey().equals("merchantsName")) {
                    merchantOffer.add(dataSnapshot.getValue(MerchantOffer.class));

                    Log.d("INITDATA", "Data added as class");


                    final MerchantOfferAdapter merchantOfferAdapter = new MerchantOfferAdapter(merchantOffer, new MerchantOfferAdapter.ButtonClicked() {
                        @Override
                        public void onButtonClicked(View v, int position) {

                            deductPointsForRedemption();

                            Log.d(TAG, "Rewards Fragment Button Clicked " + position);
                        }


                    });

                    merchantRecView.setAdapter(merchantOfferAdapter);

                }

                }

//           String.valueOf(dataSnapshot.child("offerImage").getValue())

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

//        merchantOffer.add(new MerchantOffer("Kenny Rogers 10 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000","sd;fadsijf"));
//        merchantOffer.add(new MerchantOffer("Kenny Rogers 20 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000",));
//        merchantOffer.add(new MerchantOffer("Kenny Rogers 40 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000",));
    }




    @Override
    public void onButtonClicked(View v, int position) {

        Log.d(TAG, "onButtonClicked:  Button is clicked");


    }




    private void userPreRedemption(){


        SecureRandom random = new SecureRandom();
        String preRedemptionCode = new BigInteger(30, random).toString(32).toUpperCase();
        Log.d("redeem", "userPreRedemption:  "+ preRedemptionCode);



        DatabaseReference preRedemptionDataRef = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/"+ redeemUid );
        RewardsPocketOffer rewardsPocketUpdate = new RewardsPocketOffer(merchantOfferTitle,merchantName,merchantAddress,merchantContact,preRedemptionCode,merchantOfferImageUrl);

       String preRedeemedKey = preRedemptionDataRef.push().getKey();
       preRedemptionDataRef.child(preRedeemedKey).setValue(rewardsPocketUpdate);

        RewardsPreredemption thisRewardPreredeemed = new RewardsPreredemption(redeemUid,preRedeemedKey);


        StorageReference preRedemptionStoRef= FirebaseStorage.getInstance().getReference().child("merchants/offerlist/"+merchantName + "/"+merchantOfferTitle+"/userredemptioncode/"+preRedemptionCode);
        objToByteStreamUpload(thisRewardPreredeemed ,preRedemptionStoRef);


    }



    private void recordRewardsRedemption(){



        Long timestamp = System.currentTimeMillis()/1000;

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp* 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

        RewardsRecord thisMatchmakingRecord = new RewardsRecord(merchantOfferTitle,rewardsMerchant,date,redeemCost,userPointsBefRed, userPoints);
        rewardsRedemptionRecord = FirebaseStorage.getInstance().getReference().child("users/" + redeemUid + "/rewardsrecord/" + date);



        objToByteStreamUpload(thisMatchmakingRecord,rewardsRedemptionRecord);





    }


    private void deductPointsForRedemption(){


        userPointsStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    userPoints = (new Gson().fromJson(new String(bytes, "UTF-8"), Integer.class));
                    userPointsBefRed = userPoints;
                    Log.d(TAG, "Gsonfrom json success, Points is " + userPoints);

                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                if (userPoints>=redeemCost){
                    Log.d("redeem", "redeem cost " + redeemCost);
                pointsAfterRedemption = (int) (Math.ceil(userPoints) - redeemCost );


                objToByteStreamUpload(pointsAfterRedemption,userPointsStorageRef);

                DatabaseReference userPointsDataRef = FirebaseDatabase.getInstance().getReference().child("users/userPoints/" + redeemUid);
                userPointsDataRef.setValue(pointsAfterRedemption);



                /*User preredemption before Verification of code by Merchant*/
                userPreRedemption();
//                offerlistDatabaseRef.child(merchantOfferTitle).child("redeemedUsers").push().setValue(redeemUid);

                Log.d("redeem","Points uploaded from Redeem is " + pointsAfterRedemption);}

                else {



                    Log.d("redeem", "redeem cost " + redeemCost);
                    Log.d("redeem","Points insufficient " + userPoints);
                    InsufficientPointsDialog insufficientPointsDialog = new InsufficientPointsDialog(getContext());
                    insufficientPointsDialog.show();
                return;
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"fragment is not created, exception: " + exception);
            }
        });

    }


    public void objToByteStreamUpload(Object obj, StorageReference destination){

        String objStr = new Gson().toJson(obj);
        InputStream in = new ByteArrayInputStream(objStr.getBytes(Charset.forName("UTF-8")));
        UploadTask uploadTask = destination.putStream(in);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure: Failure to upload in storage ");
                // Use analytics to find out why is the error
                // then only implement the best corresponding measures


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                try {
                    Toast.makeText(getContext(), "Your Rewards have been Redeemed", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Profile update successful!");
                    // Use analytics to calculate the success rate
                } catch (NullPointerException e ){
                    e.printStackTrace();
                }
            }
        });
    }







}
