package com.opark.opark.user_profile_page;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.model.User;
import com.opark.opark.share_parking.MapsMainActivity;

public class DownloadUserProfile extends AsyncTask<String,Void,Void> {
    private TextView firstName, lastName, phoneNum;
    private TextView carColour, carBrand, carModel, carPlate;
    private TextView firstLine, secondLine, city, postcode, countryState;
    private String firebaseUserUID;
    @Override
    protected Void doInBackground(String... strings) {

        FirebaseUser currentUser;
        final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseUserUID = currentUserID;


        StorageReference userRef = FirebaseStorage.getInstance().getReference().child("users/" + firebaseUserUID + "/profile.txt");

        Log.d("UID","User get success");


        final long ONE_MEGABYTE = 1024 * 1024;
        userRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("Byte","getByte success");


                try {
//                    userObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
                    Log.d("Gson","Gsonfrom json success");


//                        for (int i = 0; i < MapsMainActivity.userObjList.size(); i++){
                    Log.d("I","Iteration success");
//                            Log.i("Hello","heyhey" + MapsMainAct/ivity.userObjList.get(i).getUserName().getFirstName());
//                            firstName.setText(userObjList.get(i).getUserName().getFirstName());
                    lastName.setText(MapsMainActivity.lastName);
                    phoneNum.setText(MapsMainActivity.phoneNum);
                    carColour.setText(MapsMainActivity.carColour);
                    carBrand.setText(MapsMainActivity.carBrand);
                    carModel.setText(MapsMainActivity.carModel);
                    carPlate.setText(MapsMainActivity.carPlate);
////                            firstLine.setText(userObjList.get(i).getUserAddress().getFirstline());
////                            secondLine.setText(userObjList.get(i).getUserAddress().getSecondline());
////                            Log.d("secondline","secondline success");
////                            city.setText(userObjList.get(i).getUserAddress().getCity());
////
////                            postcode.setText(userObjList.get(i).getUserAddress().getPostcode());
////                            countryState.setText(userObjList.get(i).getUserAddress().getCountryState());

//                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        return null;
    }
}
