package com.opark.opark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.model.merchant_class.Merchant;
import com.opark.opark.model.merchant_offer.MerchantOffer;
import com.opark.opark.model.merchant_offer.MerchantOfferAdapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RewardsFragment extends Fragment implements MerchantOfferAdapter.ButtonClicked {
    private Button redeemButton;
    private static final String TAG = "RewardsFragment";
    private List<MerchantOffer> merchantOffer = new ArrayList<>();
    MerchantOffer thisMerchantOffer = new MerchantOffer();
    private DatabaseReference offerlistDatabaseRef;
    private StorageReference userPointsStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser thisUser;
    private String redeemUid;
    public static String merchantOfferTitle;
    final long ONE_MEGABYTE = 1024 * 1024;
    private int userPoints;
    private int pointsAfterRedemption;
    public static int redeemCost;



    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_offers_recycler_view,container,false);

//try {
        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");
    RecyclerView merchantRecView = (RecyclerView) view.findViewById(R.id.merchant_offer_recview);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    merchantRecView.setLayoutManager(linearLayoutManager);

    mAuth = FirebaseAuth.getInstance();
    thisUser = mAuth.getCurrentUser();
    redeemUid = thisUser.getUid();
    userPointsStorageRef = FirebaseStorage.getInstance().getReference().child("users/" + redeemUid + "/points.txt");



    initializeData(merchantRecView);

        return view;
    }



    private void initializeData(final RecyclerView merchantRecView){

//        merchantOffer = new ArrayList<>();
        offerlistDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
                Log.d(TAG, "onChildAdded: datasnapshot " +  String.valueOf(dataSnapshot.child("offerCost").getValue()));
                Log.d(TAG, "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
//
                Log.d(TAG, "redeem " + dataSnapshot.child("redeemCount").getValue());



//                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue()),"asdsadas"));
                merchantOffer.add(dataSnapshot.getValue(MerchantOffer.class));
//                Log.d(TAG, "onChildAdded: merchant offer " + merchantOffer.get(1));

                final MerchantOfferAdapter merchantOfferAdapter = new MerchantOfferAdapter(merchantOffer, new MerchantOfferAdapter.ButtonClicked() {
                    @Override
                    public void onButtonClicked(View v, int position) {

                        deductPointsForRedemption();

                        Log.d(TAG, "Rewards Fragment Button Clicked " + position);
                    }




                });

                merchantRecView.setAdapter(merchantOfferAdapter);
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


    private void deductPointsForRedemption(){


        userPointsStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    userPoints = (new Gson().fromJson(new String(bytes, "UTF-8"), Integer.class));
                    Log.d(TAG, "Gsonfrom json success, Points is " + userPoints);

                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                if (userPoints>redeemCost){
                pointsAfterRedemption = (int) (Math.ceil(userPoints) - redeemCost );
                objToByteStreamUpload(pointsAfterRedemption,userPointsStorageRef);
                offerlistDatabaseRef.child(merchantOfferTitle).child("redeemedUsers").push().setValue(redeemUid);

                Log.d("redeem","Points uploaded from Redeem is " + pointsAfterRedemption);}

                else {
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
                Toast.makeText(getContext(), "You've Earned Points!", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Profile update successful!");
                // Use analytics to calculate the success rate
            }
        });
    }



}
