package com.opark.opark;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class BrandsOfferFragmentHost extends Fragment {
    final List<BrandsAlphabet> brandsAlphabets = new ArrayList<>();
    FrameLayout frameContainer;

    private RecyclerView brandsAlphabetRecView;
    private  BrandsAlphabet brandsAlphabet;
    private DatabaseReference brandsTitleAlphabetDatabase;
    public static BrandsOfferTItleAlphabetAdapter brandsOfferTItleAlphabetAdapter;

    public static FragmentManager brandsOfferFragMan;
    public static FragmentTransaction brandsOfferFragTrans;
    public static BrandsOfferFragment brandsOfferFragment = new BrandsOfferFragment();
//    public static ShowBrandOffer showBrandOffer = new ShowBrandOffer();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brands_title_view,container,false);


        bindViews(view);

        brandsOfferFragMan =getChildFragmentManager();
//        brandsOfferFragTrans = brandsOfferFragMan.beginTransaction();
        brandsOfferFragMan.beginTransaction().add(R.id.frame_container, brandsOfferFragment,"try").commit();
//        getChildFragmentManager().beginTransaction().replace(R.id.frame_container, new BrandsOfferFragment()).commit();



        return view;
    }




   private void  bindViews(View view){


        frameContainer = view.findViewById(R.id.frame_container);




     }
}
