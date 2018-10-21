package com.opark.opark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import com.opark.opark.merchant_side.MerchUploadOfferActivity;

public class MerchActivity extends AppCompatActivity {
    ImageButton merchProfImgButton;
    ImageButton uploadOfferImgButton;
    ImageButton contactSupportImgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merch);
        bindViews();

        uploadOfferImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent uploadOfferIntent = new Intent(MerchActivity.this, MerchUploadOfferActivity.class);
                startActivity(uploadOfferIntent);


            }
        });




    }

    private void bindViews() {
        merchProfImgButton = findViewById(R.id.merchant_profile);
        uploadOfferImgButton = findViewById(R.id.upload_offer);
        contactSupportImgButton = findViewById(R.id.contact_support);


    }


}
