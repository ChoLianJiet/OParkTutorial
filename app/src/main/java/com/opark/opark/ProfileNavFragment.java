package com.opark.opark;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.model.User;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.attr.key;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Yz on 21-Sep-17.
 */

public class ProfileNavFragment extends Fragment {

    private TextView firstName, lastName, phoneNum;
    private TextView carColour, carBrand, carModel, carPlate;
    private TextView firstLine, secondLine, city, postcode, countryState;
    String firebaseUserUID;
    ArrayList<User> userObjList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view=  inflater.inflate(R.layout.profile_nav_fragment, parent, false);

        FirebaseUser currentUser;
        final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseUserUID = currentUserID;

        bindViews(view);

        Log.d("View","Viewbinding success");

        StorageReference userRef = FirebaseStorage.getInstance().getReference().child("users/" + firebaseUserUID + "/profile.txt");

        Log.d("UID","User get success");


        final long ONE_MEGABYTE = 1024 * 1024;
        userRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("Byte","getByte success");


                try {
                    userObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
                    Log.d("Gson","Gsonfrom json success");


                        for (int i = 0; i < userObjList.size(); i++){
                            Log.d("I","Iteration success");
                            Log.i("Hello","heyhey" + userObjList.get(i).getUserName().getFirstName());
                            firstName.setText(userObjList.get(i).getUserName().getFirstName());
                            lastName.setText(userObjList.get(i).getUserName().getLastName());
                            phoneNum.setText(userObjList.get(i).getUserName().getPhoneNum());
                            carColour.setText(userObjList.get(i).getUserCar().getCarColour());
                            carBrand.setText(userObjList.get(i).getUserCar().getCarBrand());
                            carModel.setText(userObjList.get(i).getUserCar().getCarModel());
                            firstLine.setText(userObjList.get(i).getUserAddress().getFirstline());
                            secondLine.setText(userObjList.get(i).getUserAddress().getSecondline());
                            Log.d("secondline","secondline success");
                            city.setText(userObjList.get(i).getUserAddress().getCity());

                            postcode.setText(userObjList.get(i).getUserAddress().getPostcode());
                            countryState.setText(userObjList.get(i).getUserAddress().getCountryState());

                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




        return view;



    }
    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Profile");
    }



    private void bindViews(View view){



        firstName = (TextView) view.findViewById(R.id.first_name_edit);
        lastName = (TextView) view.findViewById(R.id.last_name_edit);
        phoneNum = (TextView) view.findViewById(R.id.phone_num_edit);
        carColour = (TextView) view.findViewById(R.id.car_colour_edit);
        carBrand = (TextView) view.findViewById(R.id.car_brand_edit);
        carModel = (TextView) view.findViewById(R.id.car_model_edit);
        carPlate = (TextView) view.findViewById(R.id.car_plate_edit);
        firstLine = (TextView) view.findViewById(R.id.first_line_edit);
        secondLine = (TextView) view.findViewById(R.id.second_line_edit);
        postcode = (TextView) view.findViewById(R.id.post_code_edit);
        city = (TextView)view.findViewById(R.id.city_edit);
        countryState = (TextView) view.findViewById(R.id.country_state_edit);

    }


    private void retrieveProfileFromStorage() {



    }





    public static String convertStreamToString(InputStream is) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {

            sb.append(line).append("\n");

        }


        reader.close();
        return sb.toString();

    }




    private void profileEdit (View v) {



    }


    public void objFromByteStreamDownload(String objStr, StorageReference destination){

       Object obj = new Gson().fromJson(objStr,String.class);
        InputStream in = new ByteArrayInputStream(objStr.getBytes( Charset.forName("UTF-8")));
        UploadTask uploadTask = destination.putStream(in);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                showErrorDialog("Failed to update your profile. Try again maybe? ");
                // Use analytics to find out why is the error
                // then only implement the best corresponding measures


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Profile update successful!", Toast.LENGTH_LONG).show();
                Log.i("Hello", "Profile update successful!");
                // Use analytics to calculate the success rate
            }
        });
    }


    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}







