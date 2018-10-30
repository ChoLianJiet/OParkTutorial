package com.opark.opark;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
    String userEmailAddress;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Toolbar toolbar;
   private ProgressBar loadingCircle;
    private LinearLayout nameNumLayout1, nameNumlayout2 , carLayout;
    private ArrayList<User> userObjList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view=  inflater.inflate(R.layout.profile_nav_fragment, parent, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmailAddress = currentUser.getEmail();
         final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        bindViews(view);
//        toolbar.setTitle("Profile");

        Log.d("View","Viewbinding success");

//        StorageReference userRef = FirebaseStorage.getInstance().getReference().child("users/" + firebaseUserUID + "/profile.txt");

        Log.d("UID","User get success");

//        DownloadUserProfileFrag profileFrag = new DownloadUserProfileFrag();
//        profileFrag.execute(currentUserID);

        profileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(getActivity(),ProfileEdit.class);
                editProfileIntent.putExtra("firebaseUser",currentUserID);
                startActivity(editProfileIntent);


            }
        });


//        final long ONE_MEGABYTE = 1024 * 1024;
//        userRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Log.d("Byte","getByte success");
//
//
//                try {
////                    userObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
////                    Log.d("Gson","Gsonfrom json success");
//
//
////                        for (int i = 0; i < MapsMainActivity.userObjList.size(); i++){
//                            Log.d("I","Iteration success");
////                            Log.i("Hello","heyhey" + MapsMainActivity.userObjList.get(i).getUserName().getFirstName());
////                            firstName.setText(userObjList.get(i).getUserName().getFirstName());
                            lastName.setText(MapsMainActivity.lastName);
                            userEmail.setText(userEmailAddress);
                            phoneNum.setText(MapsMainActivity.phoneNum);
                            carColour.setText(MapsMainActivity.carColour);
                            carBrand.setText(MapsMainActivity.carBrand);
                            carModel.setText(MapsMainActivity.carModel);
                            carPlate.setText(MapsMainActivity.carPlate);
////                            firstLine.setText(userObjList.get(i).getUserAddress().getFir stline());
////                            secondLine.setText(userObjList.get(i).getUserAddress().getSecondline());
////                            Log.d("secondline","secondline success");
////                            city.setText(userObjList.get(i).getUserAddress().getCity());
////
////                            postcode.setText(userObjList.get(i).getUserAddress().getPostcode());
////                            countryState.setText(userObjList.get(i).getUserAddress().getCountryState());
//
////                    }
//
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });




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

//        loadingCircle = (ProgressBar) view.findViewById(R.id.progress_bar);
//        nameNumLayout1=(LinearLayout) view.findViewById(R.id.name_num_1);
//        nameNumlayout2=(LinearLayout) view.findViewById(R.id.name_num_2);
//        carLayout= (LinearLayout) view.findViewById(R.id.car_1);
//        firstName = (TextView) view.findViewById(R.id.first_name_edit);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_in_maps_main);

        lastName = (TextView) view.findViewById(R.id.last_name_edit);
        userEmail = view.findViewById(R.id.email_text);
        phoneNum = (TextView) view.findViewById(R.id.phone_num_edit);
        carColour = (TextView) view.findViewById(R.id.car_colour_edit);
        carBrand = (TextView) view.findViewById(R.id.car_brand_edit);
        carModel = (TextView) view.findViewById(R.id.car_model_edit);
        carPlate = (TextView) view.findViewById(R.id.car_plate_edit);
        profileEditButton =(Button) view.findViewById(R.id.edit_profile_details);


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


/*


    private class DownloadUserProfileFrag extends AsyncTask<String,Void,ArrayList<User>>  {
        //        private TextView firstName, lastName, phoneNum;
//        private TextView carColour, carBrand, carModel, carPlate;
//        private TextView firstLine, secondLine, city, postcode, countryState;
        ArrayList<User> userProfContent = new ArrayList<>();


        protected ArrayList<User> doInBackground(String...currentUserID) {
            Log.d("DUPF","I am in DUPF");



            StorageReference userRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID [0]+ "/profile.txt");

            Log.d("DUPF", "current user id is " + currentUserID[0] +"\n userRef is"+ userRef);


            final long ONE_MEGABYTE = 1024 * 1024;
            userRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.d("DUPF","getByte success");


                    try {
                        userProfContent.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
//                        Log.d("DUPF","Gsonfrom json success" + "\n " +userObjList.get(1));
//                            Log.i("Hello","heyhey" + MapsMainAct/ivity.userObjList.get(i).getUserName().getFirstName());
//                            firstName.setText(userObjList.get(i).getUserName().getFirstName());

////                            firstLine.setText(userObjList.get(i).getUserAddress().getFirstline());
////                            secondLine.setText(userObjList.get(i).getUserAddress().getSecondline());
////                            Log.d("secondline","secondline success");
////                            city.setText(userObjList.get(i).getUserAddress().getCity());
////
////                            postcode.setText(userObjList.get(i).getUserAddress().getPostcode());
////                            countryState.setText(userObjList.get(i).getUserAddress().getCountryState());

//                    }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any erro
                    Log.d("DUPF","download failed" + exception);
                }
            });
            Log.d("DUPF", "returning "+userProfContent );

            return userProfContent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            carLayout.setVisibility(GONE);
            nameNumLayout1.setVisibility(GONE);
            nameNumlayout2.setVisibility(GONE);
            loadingCircle.setVisibility(View.VISIBLE);
        }

            @Override
            protected void onPostExecute(ArrayList<User> userDownloaded) {
                try {
                    super.onPostExecute(userDownloaded);
                    Log.d("DUPF", "onPostExecute: entered ");
                    for (int i = 0; i < userDownloaded.size(); i++) {
                        lastName.setText(userDownloaded.get(i).userName.getFirstName());
                        phoneNum.setText(userDownloaded.get(i).userName.getPhoneNum());
                        carColour.setText(userDownloaded.get(i).userCar.getCarColour());
                        carBrand.setText(userDownloaded.get(i).userCar.getCarBrand());
                        carModel.setText(userDownloaded.get(i).userCar.getCarModel());
                        carPlate.setText(userDownloaded.get(i).userCar.getCarPlate());

                    }
                    loadingCircle.setVisibility(GONE);
                } catch (IndexOutOfBoundsException e)
                { Log.d("DUPF", "error is " + e );}
            }



}

*/




}









