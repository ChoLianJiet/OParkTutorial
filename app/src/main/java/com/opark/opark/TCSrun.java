package com.opark.opark;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;

import java.util.List;
import java.util.concurrent.Callable;

import static com.opark.opark.ShowBrandOffer.brandsOfferRecview;
import static com.opark.opark.ShowBrandOffer.fragmentManager;
import static com.opark.opark.ShowBrandOffer.merchantOfferChecktitleList;
import static com.opark.opark.ShowBrandOffer.offerTitleByBrands;

public class TCSrun implements Callable<Void> {

    ConfirmPreRedeem confirmPreRedeem;
    private static final String TAG = "OfferByThisBrand";
    @Override
    public Void call() throws Exception {
        final DatabaseReference offerlistDataRef = FirebaseDatabase.getInstance().getReference().child("offerlist/approved-offers");



        offerlistDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d(TAG, "\t 1 offertitlebybrands  \n" + offerTitleByBrands);
                Log.d(TAG, "\t 2 checklist before  \n" + merchantOfferChecktitleList);


                if ( dataSnapshot.getValue(MerchantOffer.class).getOfferCategories().equals(CategoriesOfferFragment.selectedCategory)/*thisBrandsOffer.contains(dataSnapshot.getKey()) */&& !merchantOfferChecktitleList.contains(dataSnapshot.getValue(MerchantOffer.class))) {


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


        Log.d(TAG, "call:  offertitlebrands" + offerTitleByBrands.size());
        return null ;
    }
}
