package com.opark.opark;

import android.content.Intent;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opark.opark.card_swipe.MainActivityCardSwipe;
import com.opark.opark.share_parking.MapsMainActivity;

public class LoadingScreen extends AppCompatActivity {

    String ADATEM2 = "2";
    String TAG = "LoadingScreen";

    private DatabaseReference matchmakingRef;
    private String currentUserID;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");

        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation);
        lottieAnimationView.setImageAssetsFolder("images/");
        lottieAnimationView.setAnimation("squareboi_loading_animation.json");
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();

        cancelButton = (Button) findViewById(R.id.cancel_button);

//        animate(animationView);

        matchmakingRef.child(currentUserID).child("adatem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                String adatemValue = dataSnapshot.getValue().toString();
                if (adatemValue.equals("2")) {
                    Intent intent = new Intent(LoadingScreen.this, KenaMap.class);
                    startActivity(intent);
                    finish();
                } else {

                }} catch (NullPointerException e){
                    System.out.println(e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchmakingRef.child(currentUserID).child("adatem").setValue("Not Available");
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        matchmakingRef.child(currentUserID).child("adatem").setValue("Not Available");
        finish();
    }
}
