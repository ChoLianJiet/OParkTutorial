package com.opark.opark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pressButtonToSwipeCard = (Button) findViewById(R.id.button_to_card_swipe);

        pressButtonToSwipeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.opark.opark.MainActivity.this,com.opark.opark.CardSwipeView.CardStackView.class);
                finish();
                startActivity(intent);
            }
        });
    }

    //Comment for github push test

}
