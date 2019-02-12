package com.opark.opark.rewards_redemption;

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
import android.view.animation.Animation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.BrandsAlphabet;
import com.opark.opark.BrandsName;
import com.opark.opark.BrandsOfferFragment1;
import com.opark.opark.R;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;
import com.opark.opark.rewards_redemption.RewardsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.opark.opark.share_parking.MapsMainActivity.navFragmentManager;

public class AllOfferFragment extends Fragment {

    private List<MerchantOffer> merchantOffer = new ArrayList<>();
    public static List<MerchantOffer> merchantOfferForBrands = new ArrayList<>();
    MerchantOffer thisMerchantOffer = new MerchantOffer();
    private DatabaseReference offerlistDatabaseRef;
    public static RecyclerView allRecView;
    private FirebaseAuth mAuth;
    private FirebaseUser thisUser;
    private static String redeemUid;
    public static String merchantName;
    public static String merchantOfferTitle;
    public static String merchantAddress;
    private static StorageReference userPointsStorageRef;
    public static MerchantOfferAdapter merchantOfferAdapter;
    public static ConfirmPreRedeem confirmPreRedeem ;
    int backStackCount;
    final public String TAG = "AllOfferFragment";



    public AllOfferFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_offer_recview,container,false);



        allRecView = view.findViewById(R.id.all_rec_view);

//        RewardsFragment.deductPointsForRedemption();
        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");

        mAuth = FirebaseAuth.getInstance();
        thisUser = mAuth.getCurrentUser();
        redeemUid = thisUser.getUid();
        userPointsStorageRef = FirebaseStorage.getInstance().getReference().child("users/" + redeemUid + "/points.txt");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        allRecView.setHasFixedSize(true);
        allRecView.setLayoutManager(linearLayoutManager);


        initializeData(allRecView);


        return view;


    }



    private void initializeData(final RecyclerView allRecView){

        Log.d("INITDATA", "initializeData: initialising data");


//        merchantOffer = new ArrayList<>();
        offerlistDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("tab", "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
                Log.d("tab", "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
                Log.d("tab", "onChildAdded: datasnapshot " +  String.valueOf(dataSnapshot.child("offerCost").getValue()));
                Log.d("tab", "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
                Log.d("tab", "onChildAdded: not adding " + dataSnapshot.hasChild("merchantsName"));

//                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue())));

                if (!dataSnapshot.getKey().equals("merchantsName")) {
                    merchantOffer.add(dataSnapshot.getValue(MerchantOffer.class));


                    /*For Brands Fragment*/
//                    if (dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0) == 'K'){
//                        merchantOfferForBrands.add(dataSnapshot.getValue(MerchantOffer.class));
//                        BrandsName brandsname = new BrandsName();
//
//                        brandsname.setBrandsName(dataSnapshot.getValue(MerchantOffer.class).getMerchantName());
//
//                        BrandsOfferFragment1.brandsNameListK.add(brandsname);
//
//
//                        Log.d(TAG, "'K'");
//                    }

                    Log.d("INITDATA", "Data added as class");


                    merchantOfferAdapter = new MerchantOfferAdapter(merchantOffer, new MerchantOfferAdapter.ButtonClicked() {
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
                    });

                    allRecView.setAdapter(merchantOfferAdapter);

                }

            }

//           String.valueOf(dataSnapshot.child("offerImage").getValue())

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

//        merchantOffer.add(new MerchantOffer("Kenny Rogers 10 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000","sd;fadsijf"));
//        merchantOffer.add(new MerchantOffer("Kenny Rogers 20 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000",));
//        merchantOffer.add(new MerchantOffer("Kenny Rogers 40 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000",));
    }

}
