package com.opark.opark;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.share_parking.MapsMainActivity;

public class PromptEndSessionDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Congratulations!")
                .setMessage("Yay! You have succesfully exchanged parking!")
                .setPositiveButton("End Session", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference togetherRef = FirebaseDatabase.getInstance().getReference().child("together").child(MapsMainActivity.currentUserID);
                        togetherRef.child("pointsGainedFromSharing").setValue(LoadingScreen.pointsGainedFromLoadingScreen + PeterMap.pointsGainedFromPeterMap + KenaMap.pointsGainedFromKenaMap);
                    }
                });
        return builder.create();
    }
}
