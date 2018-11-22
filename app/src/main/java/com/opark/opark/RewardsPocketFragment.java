package com.opark.opark;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;

import java.util.ArrayList;
import java.util.List;

public class RewardsPocketFragment extends Fragment {

    private static final String TAG = "RewardsPocketFragment";
    private List<RewardsPocketOffer> rewardsPocketOffers = new ArrayList<>();
    RewardsPocketOffer thisMerchantOffer = new RewardsPocketOffer();
    private DatabaseReference offerlistDatabaseRef;
    private DatabaseReference rewardsDatabaseRef;

    final RewardsPocketAdapter rewardsPocketAdapter = new RewardsPocketAdapter(rewardsPocketOffers);


    private StorageReference userPointsStorageRef;
    private StorageReference rewardsRedemptionRecord;


    private FirebaseAuth mAuth;
    private FirebaseUser thisUser;
    private String currentUserId;

    final long ONE_MEGABYTE = 1024 * 1024;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rewards_pocket_rec_view,container,false);

//try {
        RecyclerView merchantRecView = (RecyclerView) view.findViewById(R.id.rewards_pocket_recview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        merchantRecView.setHasFixedSize(true);
        merchantRecView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        thisUser = mAuth.getCurrentUser();
        currentUserId = thisUser.getUid();

        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/"+ currentUserId  );



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
                Log.d(TAG, "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
                Log.d(TAG, "onChildAdded: s = " + s);





                        rewardsPocketOffers.add(dataSnapshot.getValue(RewardsPocketOffer.class));

                        Log.d("INITDATA", "Data added as class");







                        merchantRecView.setAdapter(rewardsPocketAdapter);
                    }






//           String.valueOf(dataSnapshot.child("offerImage").getValue())

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, "onChildRemoved:  " + dataSnapshot);

                try {
//                    int index = rewardsPocketOffers.indexOf(dataSnapshot);

                    Log.d(TAG, "onChildRemoved: index " + dataSnapshot.getValue(RewardsPocketOffer.class));
                    rewardsPocketOffers.remove(dataSnapshot.getValue(RewardsPocketOffer.class));

                    rewardsPocketAdapter.notifyDataSetChanged();

//                rewardsPocketOffers.remove(dataSnapshot.getValue(RewardsPocketOffer.class));
                } catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
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
}
