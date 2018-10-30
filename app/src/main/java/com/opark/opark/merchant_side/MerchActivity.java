package com.opark.opark.merchant_side;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opark.opark.R;

public class MerchActivity extends AppCompatActivity {
    ImageButton merchProfImgButton;
    ImageButton uploadOfferImgButton;
    ImageButton contactSupportImgButton;
    TextView amountRedeemedTextView;
    long amountRedeemed;


    DatabaseReference amountRedeemedDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merch);

        bindViews();


        amountRedeemedDatabase = FirebaseDatabase.getInstance().getReference("offerlist/Beth yjjkkj/redeemedUsers");
        amountRedeemedDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                amountRedeemed = dataSnapshot.getChildrenCount();
                amountRedeemedTextView.setText(String.valueOf(amountRedeemed));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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


    }


}