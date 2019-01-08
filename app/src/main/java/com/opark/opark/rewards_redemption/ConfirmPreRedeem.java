package com.opark.opark.rewards_redemption;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.opark.opark.InsufficientPointsDialog;
import com.opark.opark.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;

import static com.opark.opark.rewards_redemption.RewardsFragment.ONE_MEGABYTE;
import static com.opark.opark.rewards_redemption.RewardsFragment.getAppContext;

public class ConfirmPreRedeem extends DialogFragment {

    private static final String TAG ="ConfirmPreRedeem";

    Button confirmRedeemButton, noRedeemButton;
    public Context mContext;
    private static int userPoints;
    private static int userPointsBefRed;
    private static int pointsAfterRedemption;
    public static int redeemCost;
    private static String redeemUid;
    public static String merchantName;
    public static String merchantOfferTitle;
    public static String merchantAddress;
    public static String merchantOfferImageUrl;
    public static String merchantContact;
    public static String rewardsMerchant;







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.confirm_preredeem_dialog, container, false);
        getDialog().setCanceledOnTouchOutside(false);

        bindViews(view);


        confirmRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getDialog().setCancelable(false);
                deductPointsForRedemption();
                noRedeemButton.setClickable(false);


                Log.d(TAG, "onClick: ");

            }
        });


        noRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();

            }
        });



        return view;

    }

    private void bindViews(View view) {
        confirmRedeemButton = view.findViewById(R.id.confirm_redeem_button);
        noRedeemButton = view.findViewById(R.id.no_redeem_button);


    }






    public void deductPointsForRedemption(){


        RewardsFragment.userPointsStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try{
                    try {
                        userPoints = (new Gson().fromJson(new String(bytes, "UTF-8"), Integer.class));
                        userPointsBefRed = userPoints;
                        Log.d(TAG, "Gsonfrom json success, Points is " + userPoints);

                    } catch (UnsupportedEncodingException e){
                        e.printStackTrace();
                    }} catch (JsonSyntaxException e ){
                    e.printStackTrace();
                }
                if (userPoints>=redeemCost){
                    Log.d("redeem", "redeem cost " + redeemCost);
                    pointsAfterRedemption = (int) (Math.ceil(userPoints) - redeemCost );


                    objToByteStreamUpload(pointsAfterRedemption,RewardsFragment.userPointsStorageRef);

                    DatabaseReference userPointsDataRef = FirebaseDatabase.getInstance().getReference().child("users/userPoints/" + RewardsFragment.redeemUid);
                    userPointsDataRef.setValue(pointsAfterRedemption);



                    /*User preredemption before Verification of code by Merchant*/
                    userPreRedemption();
//                offerlistDatabaseRef.child(merchantOfferTitle).child("redeemedUsers").push().setValue(redeemUid);

                    Log.d("redeem","Points uploaded from Redeem is " + pointsAfterRedemption);}

                else {


                    Log.d("redeem", "redeem cost " + redeemCost);
                    Log.d("redeem","Points insufficient " + userPoints);
                    InsufficientPointsDialog insufficientPointsDialog = new InsufficientPointsDialog(getAppContext());
                    insufficientPointsDialog.show();
                    return;
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"fragment is not created, exception: " + exception);
            }
        }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                Log.d(TAG, "onComplete:  Complete JOr outside completed Calc");
                getDialog().dismiss();

            }



        });

    }


    public static void objToByteStreamUpload(Object obj, StorageReference destination){

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
                    Toast.makeText(getAppContext(), "Your Rewards have been Redeemed", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Profile update successful!");
                    // Use analytics to calculate the success rate
                } catch (NullPointerException e ){
                    e.printStackTrace();
                }
            }
        });
    }



    private static void userPreRedemption(){


        SecureRandom random = new SecureRandom();
        String preRedemptionCode = new BigInteger(30, random).toString(32).toUpperCase();
        if (preRedemptionCode.contains("")&& (preRedemptionCode.length()!= 6)){
            Log.d(TAG, "userPreRedemption: ERROR CODE GENERATION");
            userPreRedemption();}
        else {
            Log.d("redeem", "userPreRedemption:  " + preRedemptionCode);


            DatabaseReference preRedemptionDataRef = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/" + RewardsFragment.redeemUid);
            RewardsPocketOffer rewardsPocketUpdate = new RewardsPocketOffer(merchantOfferTitle, merchantName, merchantAddress, merchantContact, preRedemptionCode, merchantOfferImageUrl);

            Log.d(TAG, "userPreRedemption: merchant OfferTitle " + merchantOfferTitle);

            String preRedeemedKey = preRedemptionDataRef.push().getKey();
            preRedemptionDataRef.child(preRedeemedKey).setValue(rewardsPocketUpdate);

            RewardsPreredemption thisRewardPreredeemed = new RewardsPreredemption(RewardsFragment.redeemUid, preRedeemedKey);


            StorageReference preRedemptionStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/" + merchantName + "/" + merchantOfferTitle + "/userredemptioncode/" + preRedemptionCode);
            objToByteStreamUpload(thisRewardPreredeemed, preRedemptionStoRef);
        }

    }

}
