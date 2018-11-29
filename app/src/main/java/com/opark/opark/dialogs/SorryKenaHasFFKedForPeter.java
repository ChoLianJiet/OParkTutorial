package com.opark.opark.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.KenaMap;
import com.opark.opark.LoadingScreen;
import com.opark.opark.PeterMap;
import com.opark.opark.share_parking.MapsMainActivity;

public class SorryKenaHasFFKedForPeter extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sorry")
                .setMessage("Kena has FFKed you :( Please find a new parking spot. However, we have compensate you with points!")
                .setPositiveButton("End Session", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference togetherRef = FirebaseDatabase.getInstance().getReference().child("together").child(MapsMainActivity.currentUserID);
                        togetherRef.child("pointsGainedFromSharing").setValue(LoadingScreen.pointsGainedFromLoadingScreen + (PeterMap.pointsGainedFromPeterMap)*2.5 + KenaMap.pointsGainedFromKenaMap);
                    }
                });
        return builder.create();
    }
}
