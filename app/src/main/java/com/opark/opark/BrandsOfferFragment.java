package com.opark.opark;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;
import com.opark.opark.rewards_redemption.RewardsFragment;

import java.util.ArrayList;
import java.util.List;


public class BrandsOfferFragment extends Fragment {
    final List<BrandsAlphabet> brandsAlphabets = new ArrayList<>();
    FrameLayout frameContainer;

    private RecyclerView brandsAlphabetRecView;
    private  BrandsAlphabet brandsAlphabet;
    private DatabaseReference brandsTitleAlphabetDatabase;
    public static BrandsOfferTItleAlphabetAdapter brandsOfferTItleAlphabetAdapter;

    public static FragmentManager brandsOfferFragMan;
    public static FragmentTransaction brandsOfferFragTrans;
    public static BrandsOfferFragment1  brandsOfferFragment1 = new BrandsOfferFragment1();
    public static ShowBrandOffer showBrandOffer = new ShowBrandOffer();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brands_title_view,container,false);


        bindViews(view);

        brandsOfferFragMan =getChildFragmentManager();
//        brandsOfferFragTrans = brandsOfferFragMan.beginTransaction();
        brandsOfferFragMan.beginTransaction().add(R.id.frame_container, brandsOfferFragment1,"try").commit();
//        getChildFragmentManager().beginTransaction().replace(R.id.frame_container, new BrandsOfferFragment1()).commit();



        return view;
    }




   private void  bindViews(View view){


        frameContainer = view.findViewById(R.id.frame_container);




     }
}
