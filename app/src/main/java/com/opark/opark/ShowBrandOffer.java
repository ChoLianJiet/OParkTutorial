package com.opark.opark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.merchant_side.merchant_offer.OfferDetailsActivity;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;

import java.util.ArrayList;
import java.util.List;

public class ShowBrandOffer extends Fragment {

    private static final String TAG = "ShowBrandOffer";

    public static RecyclerView brandsOfferRecview;
    private TextView ShowBrandOfferTitle;
    final TaskCompletionSource<Task<List<String>>> source = new TaskCompletionSource<>();
    ConfirmPreRedeem confirmPreRedeem;
    private List<MerchantOffer> merchantOfferListFromShowBrandOffer = new ArrayList<>();
     public static List<String> offerTitleByBrands = new ArrayList<>();
     public static List<MerchantOffer> merchantOfferChecktitleList = new ArrayList<>();
     boolean listenfirst = false;
    public static FragmentManager fragmentManager;
    public static Task<List<String>> task;
    final Bundle detailsBundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.show_brand_offer,container,false);

            bindviews(view);

            fragmentManager = getFragmentManager();
            ShowBrandOfferTitle.setText(BrandsOfferFragment.unityName);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        brandsOfferRecview.setLayoutManager(llm);
        brandsOfferRecview.setHasFixedSize(true);

        Log.d(TAG, "onCreateView: " + BrandsOfferFragment.unityName);


        retrieveOffersOfThisBrand();
//        Tasks.call(new OfferByThisBrand());
        /**THIS IS WHERE THE TWO LISTENERS ARE **/




        ShowBrandOfferTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: parent fragment is   " + getParentFragment());

//                FragmentManager fm = getParentFragment().getFragmentManager();
                offerTitleByBrands.clear();
                merchantOfferChecktitleList.clear();
                Fragment fragment = BrandsOfferFragmentHost.brandsOfferFragMan.findFragmentByTag("showingbrand");
                Fragment fragment1 = BrandsOfferFragmentHost.brandsOfferFragment;
                Log.d(TAG, "onClick:  fragment is " +  fragment + " Fragment 1 "  +  fragment1);


//                BrandsOfferFragmentHost.brandsOfferFragMan.beginTransaction().remove(fragment)
                BrandsOfferFragmentHost.brandsOfferFragMan.beginTransaction().replace(R.id.frame_container,fragment1)
//                        .re(R.id.frame_container,BrandsOfferFragmentHost.brandsOfferFragMan.findFragmentByTag("try"))
//                BrandsOfferFragmentHost.brandsOfferFragMan.popBackStackImmediate("showingBrand",0);
                        .commit();



            }
        });

        return view;
    }




    private void bindviews(View view){


        ShowBrandOfferTitle = view.findViewById(R.id.brand_title_in_show_brand_offer);
        brandsOfferRecview = view.findViewById(R.id.brands_offer_recview);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach:  ");
        clearArrayList();



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        offerTitleByBrands.clear();
        merchantOfferChecktitleList.clear();
    }



    public static void clearArrayList(){


        BrandsOfferFragment.brandsNameListStringK.clear();
        BrandsOfferFragment.brandsNamesListK.clear();
        BrandsOfferFragment.brandsOfferK.clear();
        BrandsOfferFragment.brandsNameListStringM.clear();
        BrandsOfferFragment.brandsNamesListM.clear();
        BrandsOfferFragment.brandsOfferM.clear();
    }





    public void retrieveOffersOfThisBrand(){

        final DatabaseReference offerlistDataRef = FirebaseDatabase.getInstance().getReference().child("offerlist/approved-offers");

        offerlistDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d(TAG, "\t 1 offertitlebybrands  \n" + offerTitleByBrands);
                Log.d(TAG, "\t 2 checklist before  \n" + merchantOfferChecktitleList);


                if ( dataSnapshot.getValue(MerchantOffer.class).getMerchantName().equals(BrandsOfferFragment.unityName)/*thisBrandsOffer.contains(dataSnapshot.getKey()) */&& !merchantOfferChecktitleList.contains(dataSnapshot.getValue(MerchantOffer.class))) {


//                            if (merchantOfferChecktitleList.get(i).getMerchantOfferTitle().equals(dataSnapshot.getKey()))
                    merchantOfferChecktitleList.add(dataSnapshot.getValue(MerchantOffer.class));


                    Log.d(TAG, "\t 3 checklist after  \n" + merchantOfferChecktitleList);

                }


                brandsOfferRecview.setAdapter(new MerchantOfferAdapter(merchantOfferChecktitleList, new MerchantOfferAdapter.ButtonClicked() {
                    @Override
                    public void onButtonClicked(View v, int position) {
                        try {
                            Log.d("", "onButtonClicked:  ");

                            confirmPreRedeem = new ConfirmPreRedeem();
                            confirmPreRedeem.show(fragmentManager, "");


                            Log.d("tab", "Rewards Fragment Button Clicked " + position);

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                }, new MerchantOfferAdapter.CardClicked() {
                    @Override
                    public void onCardClicked(View v, int position) {

                        String landingImgUrl = merchantOfferChecktitleList.get(position).getOfferImage();
                        final Intent offerDetailsIntent = new Intent(getActivity(), OfferDetailsActivity.class);

                        detailsBundle.putString("offertitle",merchantOfferChecktitleList.get(position).getMerchantOfferTitle());
                        Log.d("cardclick", "title:  " + detailsBundle.getString("offertitle"));
                        detailsBundle.putString("merchantconame",merchantOfferChecktitleList.get(position).getMerchantName());
                        detailsBundle.putString("merchantemail",merchantOfferChecktitleList.get(position).getMerchantEmail());
                        detailsBundle.putString("merchantcontact",merchantOfferChecktitleList.get(position).getMerchantContact());
                        detailsBundle.putString("offercost",merchantOfferChecktitleList.get(position).getOfferCost());
                        detailsBundle.putString("landingImgUrl",landingImgUrl);
                        detailsBundle.putString("expirydate",merchantOfferChecktitleList.get(position).getExpiryDate());
                        detailsBundle.putString("merchantfirst", merchantOfferChecktitleList.get(position).getMerchantAddress().getFirstline());
                        detailsBundle.putString("merchantsecond", merchantOfferChecktitleList.get(position).getMerchantAddress().getSecondline());
                        detailsBundle.putString("merchantcity", merchantOfferChecktitleList.get(position).getMerchantAddress().getCity());
                        detailsBundle.putString("merchantstate", merchantOfferChecktitleList.get(position).getMerchantAddress().getCountryState());
                        detailsBundle.putString("merchantpostcode", merchantOfferChecktitleList.get(position).getMerchantAddress().getPostcode());
                        detailsBundle.putString("firstline",merchantOfferChecktitleList.get(position).getMerchantAddress().getFirstline());
                        detailsBundle.putString("secondline",merchantOfferChecktitleList.get(position).getMerchantAddress().getSecondline());
                        detailsBundle.putString("city",merchantOfferChecktitleList.get(position).getMerchantAddress().getCity());
                        detailsBundle.putString("state",merchantOfferChecktitleList.get(position).getMerchantAddress().getCountryState());
                        detailsBundle.putString("postcode",merchantOfferChecktitleList.get(position).getMerchantAddress().getPostcode());

                        offerDetailsIntent.putExtras(detailsBundle);
                        startActivity(offerDetailsIntent);




                    }
                }));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
