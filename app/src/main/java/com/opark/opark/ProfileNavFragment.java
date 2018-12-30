package com.opark.opark;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.opark.opark.model.MatchmakingRecord;
import com.opark.opark.model.User;
import com.opark.opark.share_parking.MapsMainActivity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import static android.view.View.GONE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Yz on 21-Sep-17.
 */

public class ProfileNavFragment extends Fragment {
    private Button profileEditButton;
    private TextView firstName, lastName, phoneNum, userEmail;
    private TextView carColour, carBrand, carModel, carPlate;
    private TextView firstLine, secondLine, city, postcode, countryState;

    private TextView lastDate, lastSharer, lastFinder, lastPoints;
    String userEmailAddress;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private FirebaseUser currentUser;
    private Toolbar toolbar;
   private ProgressBar loadingCircle;
    private LinearLayout nameNumLayout1, nameNumlayout2 , carLayout;
    private ArrayList<User> userObjList = new ArrayList<>();
    private ArrayList<MatchmakingRecord> mmObjList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view=  inflater.inflate(R.layout.profile_nav_fragment, parent, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmailAddress = currentUser.getEmail();
           currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();




        bindViews(view);





        Log.d("View","Viewbinding success");

//        StorageReference userRef = FirebaseStorage.getInstance().getReference().child("users/" + firebaseUserUID + "/profile.txt");

        Log.d("UID","User get success");



        profileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(getActivity(),ProfileEdit.class);
                editProfileIntent.putExtra("firebaseUser",currentUserID);
                startActivity(editProfileIntent);


            }
        });


                            lastName.setText(MapsMainActivity.lastName);
                            userEmail.setText(userEmailAddress);
                            phoneNum.setText(MapsMainActivity.phoneNum);
                            carColour.setText(MapsMainActivity.carColour);
                            carBrand.setText(MapsMainActivity.carBrand);
                            carModel.setText(MapsMainActivity.carModel);
                            carPlate.setText(MapsMainActivity.carPlate);
                            retrieveMatchMakingFromStorage();





        return view;



    }
    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Profile");
        toolbar.setTitle("Profile");


    }



    private void bindViews(View view){

//        loadingCircle = (ProgressBar) view.findViewById(R.id.progress_bar);
//        nameNumLayout1=(LinearLayout) view.findViewById(R.id.name_num_1);
//        nameNumlayout2=(LinearLayout) view.findViewById(R.id.name_num_2);
//        carLayout= (LinearLayout) view.findViewById(R.id.car_1);
//        firstName = (TextView) view.findViewById(R.id.first_name_edit);
       toolbar = (Toolbar) view.findViewById(R.id.toolbar_in_maps_main);

        lastDate = view.findViewById(R.id.last_mm_date);
        lastFinder = view.findViewById(R.id.last_finder);
        lastSharer = view.findViewById(R.id.last_sharer);
        lastPoints =view.findViewById(R.id.last_points);
        lastName = (TextView) view.findViewById(R.id.last_name_edit);
        userEmail = view.findViewById(R.id.email_text);
        phoneNum = (TextView) view.findViewById(R.id.phone_num_edit);
        carColour = (TextView) view.findViewById(R.id.car_colour_edit);
        carBrand = (TextView) view.findViewById(R.id.car_brand_edit);
        carModel = (TextView) view.findViewById(R.id.car_model_edit);
        carPlate = (TextView) view.findViewById(R.id.car_plate_edit);
        profileEditButton =(Button) view.findViewById(R.id.edit_profile_details);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);



    }


    private void retrieveMatchMakingFromStorage() {

        StorageReference mmRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/matchmakingrecord/"+  "latestrecord.txt");
        final long ONE_MEGABYTE = 1024 * 1024;
        mmRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    mmObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), MatchmakingRecord.class));
                    Log.d("Gson","Gsonfrom json success");

                        for (int i = 0; i < mmObjList.size(); i++){

                    lastDate.setText(mmObjList.get(i).getMatchmakingRecordTimestamp());
                    lastFinder.setText("Finder: "+mmObjList.get(i).getPeterUid());
                    lastSharer.setText("Sharer: "+mmObjList.get(i).getKenaUid());
                    lastPoints.setText(String.valueOf(mmObjList.get(i).getUserPointsGained()) );


            }

                }catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


        }

    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the mDrawer.
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}









