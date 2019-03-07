package com.opark.opark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {
    Button offerApproveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        bindViews();

        offerApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent approveIntent = new Intent(AdminActivity.this, OfferApprovalActivity.class);
                startActivity(approveIntent);




            }
        });



    }

    private void bindViews() {

        offerApproveButton = findViewById(R.id.offer_approval_button);



    }


}


