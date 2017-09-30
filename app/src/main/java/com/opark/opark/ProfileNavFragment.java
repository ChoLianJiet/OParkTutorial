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

        firstName = (TextView) view.findViewById(R.id.first_name_edit);
        firstName.setText("");

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
    }







