package com.opark.opark;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Yz on 21-Sep-17.
 */

public class ProfileNavFragment extends Fragment {

    private TextView firstName, lastName, phoneNum;
    private TextView carColour, carBrand, carModel, carPlate;
    private TextView firstLine, secondLine, city, postcode, countryState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view=  inflater.inflate(R.layout.profile_nav_fragment, parent, false);


        bindViews(view);







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



    public static String getStringFromFile (File fl) throws Exception {

        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;

    }


    private void profileEdit (View v) {



    }


}







