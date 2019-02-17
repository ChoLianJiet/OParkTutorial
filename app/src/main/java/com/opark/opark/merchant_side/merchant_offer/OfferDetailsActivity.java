package com.opark.opark.merchant_side.merchant_offer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.OfferApprovalActivity;
import com.opark.opark.R;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;

public class OfferDetailsActivity extends AppCompatActivity {
    final long ONE_MEGABYTE = 1024 * 1024;

    TextView offerTitle;
    String offerTitleString;
    String merchantCoNameString;
    TextView offerDescription;
    TextView merchantCoName;
    TextView offerCost;
    TextView merchantContact;
    TextView offerExpiry;
    LinearLayout offerDetailLinearLayout;
    Button approveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);





        Intent offerIntent = getIntent();

        bindViews();






        final LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation);
        lottieAnimationView.setImageAssetsFolder("images/");
        lottieAnimationView.setAnimation("squareboi_loading_animation.json");
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();


        final Bundle offerDetails = offerIntent.getExtras();
        Log.d("details", "onCreate:  " + offerDetails.getString("offerdescription"));


//        offerTitle.setText(offerDetails.getString("offertitle"));
//        offerDescription.setText(offerDetails.getString("offerdescription"));
//        offerExpiry.setText(offerDetails.getString("expirydate"));
//        merchantCoName.setText(offerDetails.getString("merchantconame"));
//        merchantContact.setText(offerDetails.getString("merchantcontact"));
//        offerCost.setText(offerDetails.getString("offercost"));


        StorageReference descriptionStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/" + (offerDetails.getString("merchantconame"))
                + "/" + (offerDetails.getString("offertitle"))).child("desc.txt");
        descriptionStoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                String offerDescriptionString = (new Gson().fromJson(new String(bytes), String.class));

                offerDescription.setText(offerDescriptionString);

                offerTitle.setText(offerDetails.getString("offertitle"));

                offerExpiry.setText(offerDetails.getString("expirydate"));
                merchantCoName.setText(offerDetails.getString("merchantconame"));
                merchantContact.setText(offerDetails.getString("merchantcontact"));
                offerCost.setText(offerDetails.getString("offercost"));

                offerDetailLinearLayout.setVisibility(View.VISIBLE);
                lottieAnimationView.cancelAnimation();
                lottieAnimationView.setVisibility(View.INVISIBLE);

            }
        });


        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveOffer(offerDetails);

            }
        });

    }

    private void bindViews() {

        offerTitle = findViewById(R.id.offer_title);
        offerDescription = findViewById(R.id.offer_description);
        offerExpiry = findViewById(R.id.offer_expiry_date);
        merchantCoName = findViewById(R.id.merchant_co_name);
        merchantContact = findViewById(R.id.merchant_contact);
        offerCost = findViewById(R.id.offer_cost);
        offerDetailLinearLayout = findViewById(R.id.offer_detail_linearlayout);
        approveButton = findViewById(R.id.approve_button);


    }


    private void approveOffer(Bundle offerDetails) {

        Log.d("tab", offerDetails.getString("offertitle"));
        try {
            FirebaseDatabase.getInstance().getReference().child("offerlist/offer-waiting-approval")
                    .child(offerDetails.getString("offertitle")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("tab", "onDataChange: " + dataSnapshot.getValue());

                    MerchantOffer merchantOffer = dataSnapshot.getValue(MerchantOffer.class);


                    FirebaseDatabase.getInstance().getReference().child("offerlist/approved-offers/" + merchantOffer.getMerchantOfferTitle()).setValue(merchantOffer);


                    FirebaseDatabase.getInstance().getReference().child("offerlist/offer-waiting-approval")
                            .child(merchantOffer.getMerchantOfferTitle()).removeValue();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e ){

        }

//        merchantOffer = new ArrayList<>();
//        FirebaseDatabase.getInstance().getReference().child("offerlist/offer-waiting-approval")
//                .child(offerDetails.getString("offertitle")).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("tab", "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
//                Log.d("tab", "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
//                Log.d("tab", "onChildAdded: datasnapshot " + String.valueOf(dataSnapshot.child("offerCost").getValue()));
//                Log.d("tab", "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
//                Log.d("tab", "onChildAdded: not adding " + dataSnapshot.hasChild("merchantsName"));
//
////                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue())));
//
//                dataSnapshot.getValue(MerchantOffer.class);
//
//                FirebaseDatabase.getInstance().getReference().child("approved-offers/" + offerTitle).setValue(dataSnapshot.getValue(MerchantOffer.class));
//
//
//
//                Log.d("INITDATA", "Data added as class");
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



    }

}




