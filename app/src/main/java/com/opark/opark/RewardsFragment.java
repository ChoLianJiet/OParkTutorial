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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opark.opark.model.merchant_class.Merchant;
import com.opark.opark.model.merchant_offer.MerchantOffer;
import com.opark.opark.model.merchant_offer.MerchantOfferAdapter;

import java.util.ArrayList;
import java.util.List;

public class RewardsFragment extends Fragment implements MerchantOfferAdapter.ButtonClicked {
    private Button redeemButton;
    private static final String TAG = "RewardsFragment";
    private List<MerchantOffer> merchantOffer = new ArrayList<>();
    MerchantOffer thisMerchantOffer = new MerchantOffer();
    private DatabaseReference offerlistDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser thisUser;
    private String redeemUid;
    public static String merchantOfferTitle;

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



    initializeData(merchantRecView);
//    Log.d(TAG, "onCreateView: merchantOffer = " + merchantOffer);
//    MerchantOfferAdapter merchantOfferAdapter = new MerchantOfferAdapter(merchantOffer, new MerchantOfferAdapter.ButtonClicked() {
//        @Override
//        public void onButtonClicked(View v, int position) {
//            Log.d(TAG, "Rewards Fragment Button Clicked");
//        }
//    });
//
//    merchantRecView.setAdapter(merchantOfferAdapter);





//
//} catch (NullPointerException e ){
//    Log.d(TAG, "onCreateView: Null" + e);
//}
//
//



//                offerlistDatabaseRef.child("redeemCount").addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        Log.d("redeem", "redeem count " +dataSnapshot.getValue());
////                       int redeemCount =  Integer.parseInt(dataSnapshot.getValue());
//                        redeemButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }});
//
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        Log.d("redeem", "redeem count " +dataSnapshot);
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



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


                        offerlistDatabaseRef.child(merchantOfferTitle).child("redeemedUsers").push().setValue(redeemUid);

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


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof MerchantOfferAdapter.ButtonClicked){
//            mButtonClicked=(MerchantOfferAdapter.ButtonClicked) context;
//
//        } else {
//            throw new RuntimeException(context.toString());
//        }
//    }
//
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mButtonClicked=null;
//    }

    public static int position =0;

    @Override
    public void onButtonClicked(View v, int position) {

        Log.d(TAG, "onButtonClicked:  Button is clicked");


    }



}
