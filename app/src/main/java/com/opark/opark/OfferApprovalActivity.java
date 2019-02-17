package com.opark.opark;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.merchant_side.merchant_class.Merchant;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.merchant_side.merchant_offer.OfferDetailsActivity;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class OfferApprovalActivity extends AppCompatActivity {
    final long ONE_MEGABYTE = 1024 * 1024;
    private RecyclerView offerRecView;
    private List<MerchantOffer> merchantOfferList = new ArrayList<>();
    private DatabaseReference offerlistDatabaseRef;
    private MerchantOfferAdapter merchantOfferAdapter;
    private ConfirmPreRedeem confirmPreRedeem;
    final Bundle detailsBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_approval);

        bindViews();

        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");


        offerRecView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        offerRecView.setLayoutManager(llm);
        initializeData(offerRecView);


    }

    private void bindViews() {

        offerRecView = findViewById(R.id.offer_rec_view);
    }


    private void initializeData(final RecyclerView offerRecView) {

        Log.d("INITDATA", "initializeData: initialising data");


//        merchantOffer = new ArrayList<>();
        offerlistDatabaseRef.child("offer-waiting-approval").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("tab", "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
                Log.d("tab", "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
                Log.d("tab", "onChildAdded: datasnapshot " + String.valueOf(dataSnapshot.child("offerCost").getValue()));
                Log.d("tab", "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
                Log.d("tab", "onChildAdded: not adding " + dataSnapshot.hasChild("merchantsName"));

//                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue())));

                if (!dataSnapshot.getKey().equals("merchantsName")) {
                    merchantOfferList.add(dataSnapshot.getValue(MerchantOffer.class));


                    Log.d("INITDATA", "Data added as class");


                    merchantOfferAdapter = new MerchantOfferAdapter(merchantOfferList, new MerchantOfferAdapter.ButtonClicked() {
                        @Override
                        public void onButtonClicked(View v, int position) {
                            try {
                                Log.d("", "onButtonClicked:  ");
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                confirmPreRedeem = new ConfirmPreRedeem();
                                confirmPreRedeem.show(fragmentManager, "");
//                            confirmPreRedeem.getDialog().setCancelable(false);
                                //                            deductPointsForRedemption();


                                Log.d("tab", "Rewards Fragment Button Clicked " + position);

                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new MerchantOfferAdapter.CardClicked() {
                        @Override
                        public void onCardClicked(View v, int position) {
                            final Intent offerDetailsIntent = new Intent(OfferApprovalActivity.this, OfferDetailsActivity.class);


                            Log.d("cardclick", "onCardClicked: " +merchantOfferList.get(position).getMerchantOfferTitle() );
                            Log.d("cardclick", "onCardClicked: " +merchantOfferList.get(position).getMerchantName() );


                            StorageReference descriptionStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/"+ merchantOfferList.get(position).getMerchantName()
                                    + "/"+merchantOfferList.get(position).getMerchantOfferTitle() ).child("desc.txt");


//                            descriptionStoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                @Override
//                                public void onSuccess(byte[] bytes) {
//
//
//                                    String offerDescription = (new Gson().fromJson(new String(bytes), String.class));
//
////                                   String offerDescription = new String(bytes,Charset.forName("utf-8"));
//
//                                    Log.d("cardclick", "onSuccess:  " + offerDescription);
//
//                                    detailsBundle.putString("offerdescription",offerDescription);
//
//
//                                    Log.d("cardclick", "onSuccess:  " + detailsBundle.getString("offerdescription"));
//
//
//
////                                    offerDetailsIntent.putExtras(detailsBundle);
////
////                                    startActivity(offerDetailsIntent);
//
//                                }
//                            });



                            detailsBundle.putString("offertitle",merchantOfferList.get(position).getMerchantOfferTitle());
                            Log.d("cardclick", "title:  " + detailsBundle.getString("offertitle"));
                            detailsBundle.putString("merchantconame",merchantOfferList.get(position).getMerchantName());
                            detailsBundle.putString("merchantemail",merchantOfferList.get(position).getMerchantEmail());
                            detailsBundle.putString("merchantcontact",merchantOfferList.get(position).getMerchantContact());
                            detailsBundle.putString("offercost",merchantOfferList.get(position).getOfferCost());
                            detailsBundle.putString("expirydate",merchantOfferList.get(position).getExpiryDate());

                            offerDetailsIntent.putExtras(detailsBundle);

                            startActivity(offerDetailsIntent);






                        }
                    });

                    offerRecView.setAdapter(merchantOfferAdapter);

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

    }

}
