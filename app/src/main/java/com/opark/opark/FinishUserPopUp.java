package com.opark.opark;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.opark.opark.share_parking.MapsMainActivity;

public class FinishUserPopUp extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_user_pop_up);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button backButton = (Button) findViewById(R.id.restart_searching_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMainDrawerAndResearch();
            }
        });

    }

    @Override
    public void onBackPressed() {
        returnToMainDrawerAndResearch();
        super.onBackPressed();
    }

    private void returnToMainDrawerAndResearch(){
        MapsMainActivity.newArrayList.addAll(MapsMainActivity.oldArrayList);
        MapsMainActivity.newHashSet.addAll(MapsMainActivity.newArrayList);
        MapsMainActivity.newArrayList.clear();
        MapsMainActivity.newArrayList.addAll(MapsMainActivity.newHashSet);
        MapsMainActivity.newHashSet.clear();
        reverseExchangeButtons(MapsMainActivity.shareParkingButton,MapsMainActivity.findParkingButton);
        MapsMainActivity.position = 124;
        finish();
    }

    public void reverseExchangeButtons(Button btn1, Button btn2) {
        ObjectAnimator animationBtn1 = ObjectAnimator.ofFloat(btn1, "translationX", -15f);
        animationBtn1.setDuration(500);
        animationBtn1.start();

        ObjectAnimator animationBtn2 = ObjectAnimator.ofFloat(btn2, "translationX", 15f);
        animationBtn2.setDuration(500);
        animationBtn2.start();

        MapsMainActivity.cancelButton.setVisibility(View.INVISIBLE);
        MapsMainActivity.cancelButton.setAnimation(null);
    }

}