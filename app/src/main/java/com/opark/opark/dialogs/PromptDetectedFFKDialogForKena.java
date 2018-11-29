package com.opark.opark.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.share_parking.MapsMainActivity;

public class PromptDetectedFFKDialogForKena extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("FFK Detected :(")
                .setMessage("We have detected that you are trying to FFK. Are you leaving without exchanging parking? FFK will cause your rating to below")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}
