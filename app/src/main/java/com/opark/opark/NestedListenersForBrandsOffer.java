package com.opark.opark;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class NestedListenersForBrandsOffer implements Callable {

    long ref;
    List<String> thisBrandOffers = new ArrayList<>();
    ConfirmPreRedeem confirmPreRedeem;
    private static final String TAG = "NestedListenersForBrand";
    @Override
    public Object call() throws Exception {



        DatabaseReference listenBrandSelected = FirebaseDatabase.getInstance().getReference().child("offerlist/merchantsName/" + BrandsOfferFragment.unityName);

            listenBrandSelected.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                        ref =dataSnapshot.getChildrenCount();
                    thisBrandOffers.add(String.valueOf(dataSnapshot1.getValue()));
                        Log.d(TAG, "onDataChange:  " + thisBrandOffers);

                        final DatabaseReference offerlistDataRef = FirebaseDatabase.getInstance().getReference().child("offerlist/approved-offers");

/*
                        offerlistDataRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                Log.d(TAG, "\t 1 offertitlebybrands  \n" + offerTitleByBrands);
                                Log.d(TAG, "\t 2 checklist before  \n" + merchantOfferChecktitleList);


                                if (thisBrandOffers.contains(dataSnapshot.getKey()) && !merchantOfferChecktitleList.contains(dataSnapshot.getValue(MerchantOffer.class))) {


//                            if (merchantOfferChecktitleList.get(i).getMerchantOfferTitle().equals(dataSnapshot.getKey()))
                                    merchantOfferChecktitleList.add(dataSnapshot.getValue(MerchantOffer.class));


                                    Log.d(TAG, "\t 3 checklist after  \n" + merchantOfferChecktitleList);

                                }


                                brandsOfferRecview.setAdapter(new MerchantOfferAdapter(merchantOfferChecktitleList, new MerchantOfferAdapter.ButtonClicked() {
                                    @Override
                                    public void onButtonClicked(View v, int position) {
                                        try {
                                            Log.d("", "onButtonClicked:  ");

                                            confirmPreRedeem = new ConfirmPreRedeem();
                                            confirmPreRedeem.show(fragmentManager, "");


                                            Log.d("tab", "Rewards Fragment Button Clicked " + position);

                                        } catch (IllegalStateException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new MerchantOfferAdapter.CardClicked() {
                                    @Override
                                    public void onCardClicked(View v, int position) {

                                    }
                                }));

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
*/


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//        listenBrandSelected.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot1, @Nullable String s) {
//
//                Log.d(TAG, "onChildAdded: " +dataSnapshot1.getChildrenCount());
//                Log.d(TAG, "datasnapshot data is " +  dataSnapshot1.getValue());
//                thisBrandOffers.add(dataSnapshot1.getValue().toString());
//
//                Log.d(TAG, "onChildAdded:  " + thisBrandOffers );
//
//
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        Log.d(TAG, "call: " + thisBrandOffers);

        return thisBrandOffers;
    }
}
