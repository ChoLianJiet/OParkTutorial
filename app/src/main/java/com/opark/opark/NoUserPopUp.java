package com.opark.opark;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class NoUserPopUp extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_user_pop_up);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button backButton = (Button) findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMainDrawer();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnToMainDrawer();
    }



    private void returnToMainDrawer(){
        finish();
    }

}
