package com.opark.opark;

import android.content.Context;
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
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opark.opark.merchant_side.merchant_class.Merchant;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;

import java.util.ArrayList;
import java.util.List;

public class ShowBrandOffer extends Fragment  {

    private static final String TAG = "ShowBrandOffer";

    private RecyclerView brandsOfferRecview;
    private TextView ShowBrandOfferTitle;

    ConfirmPreRedeem confirmPreRedeem;
    private List<MerchantOffer> merchantOfferListFromShowBrandOffer = new ArrayList<>();
     final private List<String> offerTitleByBrands = new ArrayList<>();
     List<MerchantOffer> merchantOfferChecktitleList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.show_brand_offer,container,false);

        Log.d(TAG, "Show brand offer oncreateView");
            bindviews(view);


            ShowBrandOfferTitle.setText(BrandsOfferFragment1.unityName);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        brandsOfferRecview.setLayoutManager(llm);
        brandsOfferRecview.setHasFixedSize(true);

        Log.d(TAG, "onCreateView: " + BrandsOfferFragment1.unityName);
        DatabaseReference listenBrandSelected = FirebaseDatabase.getInstance().getReference().child("offerlist/merchantsName/" + BrandsOfferFragment1.unityName);








        listenBrandSelected.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot1, @Nullable String s) {
                Log.d(TAG, "onChildAdded: " + dataSnapshot1.getValue().toString());



                offerTitleByBrands.add(dataSnapshot1.getValue().toString());
                Log.d(TAG, "offertitlebybrands: " + offerTitleByBrands.size());



//                offerlistDataRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Log.d(TAG, "datasnapshot value event listener: merchant name" + dataSnapshot.getValue());
//
//
//                        Log.d(TAG, "datasnapshot value event listener: merchant name" + dataSnapshot.getValue(MerchantOffer.class));
//                        Log.d(TAG, "datasnapshot value event listener: 0th in the offerarray" + offerTitleByBrands.get(0));
//
//
//
//                        if ( offerTitleByBrands.contains(dataSnapshot.getValue(MerchantOffer.class).getMerchantOfferTitle()) && !merchantOfferChecktitleList.contains(dataSnapshot.getValue(MerchantOffer.class))  )
//                            merchantOfferChecktitleList.add(dataSnapshot.getValue(MerchantOffer.class));
//
//                        Log.d("testaddin", "onDataChange: check title list " +  merchantOfferChecktitleList.size());
//
//
//                        brandsOfferRecview.setAdapter( new MerchantOfferAdapter(merchantOfferChecktitleList, new MerchantOfferAdapter.ButtonClicked() {
//                            @Override
//                            public void onButtonClicked(View v, int position) {
//
//                            }
//                        }));
////                        Log.d(TAG, "datasnapshot value event listener: 0th in the offerarray" + offerTitleByBrands.get(0));
////
////
////                        merchantOfferChecktitleList.add(dataSnapshot.getValue(MerchantOffer.class));
////
////                        Log.d(TAG, "onDataChange: check title list " +  merchantOfferChecktitleList.get(0).getMerchantOfferTitle().toString());
//
//
////                        for (int i = 0; i < offerTitleByBrands.size(); i++) {
////
////                            Log.d(TAG, "onDataChange:  For loop"+ offerTitleByBrands.get(i) + " i is now " + i);
////                            if (dataSnapshot.getValue(MerchantOffer.class).getMerchantOfferTitle().equals(offerTitleByBrands.get(i).toString())){
////
////                                Log.d(TAG, "onDataChange:  DataSnapshot Name Tally");
////                                merchantOfferListFromShowBrandOffer.add(dataSnapshot.getValue(MerchantOffer.class));
////
////                                Log.d(TAG, "merchantOfferLIstFromshowbrandoffer" + merchantOfferListFromShowBrandOffer.size());
//////                                MerchantOfferAdapter moaFromShowBrandOffer = new MerchantOfferAdapter(merchantOfferListFromShowBrandOffer, new MerchantOfferAdapter.ButtonClicked() {
//////                                    @Override
//////                                    public void onButtonClicked(View v, int position) {
//////
//////                                    }
//////                                });
////
////
////                                brandsOfferRecview.setAdapter( new MerchantOfferAdapter(merchantOfferListFromShowBrandOffer, new MerchantOfferAdapter.ButtonClicked() {
////                                    @Override
////                                    public void onButtonClicked(View v, int position) {
////
////                                    }
////                                }));
//////                                brandsOfferRecview.setAdapter( moaFromShowBrandOffer);
////
////                            }
////                        }
//
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });


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


        final DatabaseReference offerlistDataRef = FirebaseDatabase.getInstance().getReference().child("offerlist");

        offerlistDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d(TAG, "merchant name" + dataSnapshot.getValue(MerchantOffer.class).getMerchantOfferTitle());
                Log.d(TAG, "offer title :  \n" + dataSnapshot.getKey());
                Log.d(TAG, "offertitlebybrands  \n" + offerTitleByBrands);
                Log.d(TAG, "merchantofferlist  \n"  + merchantOfferChecktitleList);

//
//                        for(int i =0 ; i < offerTitleByBrands.size(); i ++ ){
//                            if (!merchantOfferChecktitleList.get(i).getMerchantOfferTitle().equals(dataSnapshot.getKey())){
//                                merchantOfferChecktitleList.add(dataSnapshot.getValue(MerchantOffer.class));
//                            }
//
//
//                        }


//                        merchantOfferChecktitleList =   new ArrayList<>(offerTitleByBrands.size());


//                        if ( offerTitleByBrands.contains(dataSnapshot.getValue(MerchantOffer.class).getMerchantOfferTitle()) && !merchantOfferChecktitleList.contains(dataSnapshot.getValue(MerchantOffer.class))  )
//                        {  merchantOfferChecktitleList.add(dataSnapshot.getValue(MerchantOffer.class));}


//                        for ( int i = 0 ; i< merchantOfferChecktitleList.size(); i++) {


                if (offerTitleByBrands.contains(dataSnapshot.getKey()) && !merchantOfferChecktitleList.contains(dataSnapshot.getValue(MerchantOffer.class))) {


//                            if (merchantOfferChecktitleList.get(i).getMerchantOfferTitle().equals(dataSnapshot.getKey()))
                    merchantOfferChecktitleList.add(dataSnapshot.getValue(MerchantOffer.class));

                    Log.d(TAG, " \n\t\t offertitlebybrands contains key & check title list added datasnapshot :  ");


                }

//                        }




                Log.d(TAG, " check title list \n" +  merchantOfferChecktitleList.size());


                brandsOfferRecview.setAdapter( new MerchantOfferAdapter(merchantOfferChecktitleList, new MerchantOfferAdapter.ButtonClicked() {
                    @Override
                    public void onButtonClicked(View v, int position) {
                        try {
                            Log.d("", "onButtonClicked:  ");
                            FragmentManager fragmentManager = getFragmentManager();
                            confirmPreRedeem = new ConfirmPreRedeem();
                            confirmPreRedeem.show(fragmentManager, "");
//                            confirmPreRedeem.getDialog().setCancelable(false);
                            //                            deductPointsForRedemption();


                            Log.d("tab", "Rewards Fragment Button Clicked " + position);

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                }, new MerchantOfferAdapter.CardClicked() {
                    @Override
                    public void onCardClicked(View v, int position) {

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

//
//        brandsOfferRecview.setAdapter( new MerchantOfferAdapter(BrandsOfferFragment1.brandsOfferList, new MerchantOfferAdapter.ButtonClicked() {
//            @Override
//            public void onButtonClicked(View v, int position) {
//
//            }
//        }));

        ShowBrandOfferTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: parent fragment is   " + getParentFragment());

//                FragmentManager fm = getParentFragment().getFragmentManager();
                offerTitleByBrands.clear();
                        merchantOfferChecktitleList.clear();
                Fragment fragment = BrandsOfferFragment.brandsOfferFragMan.findFragmentByTag("showingbrand");
                Fragment fragment1 = BrandsOfferFragment.brandsOfferFragment1;
                Log.d(TAG, "onClick:  fragment is " +  fragment + " Fragment 1 "  +  fragment1);


//                BrandsOfferFragment.brandsOfferFragMan.beginTransaction().remove(fragment)
                BrandsOfferFragment.brandsOfferFragMan.beginTransaction().replace(R.id.frame_container,fragment1)
//                        .re(R.id.frame_container,BrandsOfferFragment.brandsOfferFragMan.findFragmentByTag("try"))

//                BrandsOfferFragment.brandsOfferFragMan.popBackStackImmediate("showingBrand",0);
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


        BrandsOfferFragment1.brandsNameListStringK.clear();
        BrandsOfferFragment1.brandsNamesListK.clear();
        BrandsOfferFragment1.brandsOfferK.clear();
        BrandsOfferFragment1.brandsNameListStringM.clear();
        BrandsOfferFragment1.brandsNamesListM.clear();
        BrandsOfferFragment1.brandsOfferM.clear();
    }
}
