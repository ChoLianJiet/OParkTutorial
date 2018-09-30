package com.opark.opark;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opark.opark.model.merchant_offer.MerchantOffer;
import com.opark.opark.model.merchant_offer.MerchantOfferAdapter;

import java.util.ArrayList;
import java.util.List;

public class RewardsFragment extends Fragment {

    private static final String TAG = "RewardsFragment";
    private List<MerchantOffer> merchantOffer;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_offers_recycler_view,container,false);

//try {

    RecyclerView merchantRecView = (RecyclerView) view.findViewById(R.id.merchant_offer_recview);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    merchantRecView.setLayoutManager(linearLayoutManager);



    initializeData();
    Log.d(TAG, "onCreateView: merchantOffer = " + merchantOffer);
    MerchantOfferAdapter merchantOfferAdapter = new MerchantOfferAdapter(merchantOffer);
    merchantRecView.setAdapter(merchantOfferAdapter);
//
//} catch (NullPointerException e ){
//    Log.d(TAG, "onCreateView: Null" + e);
//}
//
//
//




        return view;
    }


    private void initializeData(){
        merchantOffer = new ArrayList<>();
        merchantOffer.add(new MerchantOffer("Kenny Rogers 10 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000", R.drawable.maintenance_squareboi));
        merchantOffer.add(new MerchantOffer("Kenny Rogers 20 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000", R.drawable.maintenance_squareboi));
        merchantOffer.add(new MerchantOffer("Kenny Rogers 40 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000", R.drawable.maintenance_squareboi));
    }


}
