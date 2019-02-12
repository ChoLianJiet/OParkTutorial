package com.opark.opark.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.MaskFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import com.opark.opark.share_parking.MapsMainActivity;

public class DecideWantToRemovePinDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Removing Pinned Location")
                .setMessage("You have already pinned your car at a location, are you sure you want to remove pin?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MapsMainActivity.pinMk.remove();
                        MapsMainActivity.pinRef.removeValue();
                        MapsMainActivity.locationIsPinned = false;
                        MapsMainActivity.setPinButton();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
