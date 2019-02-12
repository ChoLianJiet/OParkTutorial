package com.opark.opark;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opark.opark.R;
import com.opark.opark.rewards_redemption.RewardsFragment;

public class BrandContainerFragment extends Fragment {




    private static final String TAG = "RootFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.brand_container_fragment, container, false);



        Log.d(TAG, "fragman:  " + RewardsFragment.rewardsFragmentManager);


        BrandsOfferFragment1 brand = new BrandsOfferFragment1();

        if (getChildFragmentManager() !=null ) {

            Log.d("fm", "not null ");
//            Log.d(TAG, " " + getChildFragmentManager().beginTransaction().add(R.id.root_frame,brand).commit());

            getChildFragmentManager().beginTransaction().add(R.id.root_frame,brand).commit();


        }
        /*
         * When this container fragment is created, we fill it with our first
         * "real" fragment
         */



        return view;
    }
}
