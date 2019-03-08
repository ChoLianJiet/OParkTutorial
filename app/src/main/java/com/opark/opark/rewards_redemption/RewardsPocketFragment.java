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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.R;

import java.util.ArrayList;
import java.util.List;

public class RewardsPocketFragment extends Fragment implements RewardsPocketAdapter.UseVoucher {

    private static final String TAG = "RewardsPocketFragment";
    private List<RewardsPocketOffer> rewardsPocketOffers = new ArrayList<>();
    RewardsPocketOffer thisMerchantOffer = new RewardsPocketOffer();
    private DatabaseReference offerlistDatabaseRef;
    private DatabaseReference rewardsDatabaseRef;

    RewardsPocketAdapter rewardsPocketAdapter;
    public static String preredemptionCode;

    private StorageReference userPointsStorageRef;
    private StorageReference rewardsRedemptionRecord;

    public static String merchantOfferTitle, merchantCoName;

    private FirebaseAuth mAuth;
    private FirebaseUser thisUser;
    public static String currentUserId;
    public static String pushKey;

    private UseVoucherCheckAndConfirm useVoucherCheckAndConfirm;

    final long ONE_MEGABYTE = 1024 * 1024;
    int positionClicked;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rewards_pocket_rec_view, container, false);

//try {
        RecyclerView merchantRecView = (RecyclerView) view.findViewById(R.id.rewards_pocket_recview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        merchantRecView.setHasFixedSize(true);
        merchantRecView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        thisUser = mAuth.getCurrentUser();
        currentUserId = thisUser.getUid();

        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/" + currentUserId);

        /*rewardsPocketAdapter = new RewardsPocketAdapter(rewardsPocketOffers, new RewardsPocketAdapter.UseVoucher() {
            @Override
            public void useVoucher(View v, int position) {

                preredemptionCode = rewardsPocketOffers.get(position).getPreRedemptionCode();
                merchantOfferTitle = rewardsPocketOffers.get(position).getMerchantOfferTitle();
                merchantCoName = rewardsPocketOffers.get(position).getMerchantName();



            }
        }, currentUserId);*/


        initializeData(merchantRecView);

//        merchantRecView.setAdapter(rewardsPocketAdapter);


        return view;
    }


    private void initializeData(final RecyclerView merchantRecView) {

        Log.d("INITDATA", "initializeData: initialising data");


//        merchantOffer = new ArrayList<>();
        offerlistDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot3, @Nullable String s) {
                Log.d(TAG, "onChildAdded: datasnapshot key" + dataSnapshot3.getKey());
                Log.d(TAG, "onChildAdded: datasnapshot children" + dataSnapshot3.getChildren());
                Log.d(TAG, "onChildAdded:  datasnapshot value " + dataSnapshot3.getValue());


                rewardsPocketOffers.add(dataSnapshot3.getValue(RewardsPocketOffer.class));

                Log.d("INITDATA", "Data added as class");


                rewardsPocketAdapter = new RewardsPocketAdapter(rewardsPocketOffers, new RewardsPocketAdapter.UseVoucher() {
                    @Override
                    public void useVoucher(View v, int position) {

                        positionClicked = position;

                        Log.d(TAG, "useVoucher:  " + v + position);
                        Log.d(TAG, "useVoucher:  " + rewardsPocketOffers.get(position).getPreRedemptionCode());

                        preredemptionCode = rewardsPocketOffers.get(position).getPreRedemptionCode();
                        merchantOfferTitle = rewardsPocketOffers.get(position).getMerchantOfferTitle();
                        merchantCoName = rewardsPocketOffers.get(position).getMerchantName();

                        /*
                         *//*final DatabaseReference usingVoucher = FirebaseDatabase.getInstance().getReference().child("offerlist").child("using-voucher")
                                .child(merchantOfferTitle).child(currentUserId);*//*
                         *//*
                        final StorageReference usingVoucher = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/"+merchantCoName)
                                .child(merchantOfferTitle).child("using-voucher");*/

                           /* final DatabaseReference redeemStatus = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist").child(currentUserId);


                            redeemStatus.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
*/
                        Log.d(TAG, "onDataChange:  datasnapshot " + dataSnapshot3.getChildren());
//                                dataSnapshot.getChildren(currentUserId);



//                               dataSnapshot.getValue(RewardsPocketOffer.class).setRedeemStatus("using-voucher");

                            Log.d(TAG, "onDataChange: " + dataSnapshot3.getValue(RewardsPocketOffer.class).getRedeemStatus());

                            pushKey = dataSnapshot3.getKey();


                            if (dataSnapshot3.getValue(RewardsPocketOffer.class).getRedeemStatus().equals("redeem-other-first")) {

                                Log.d(TAG, "redeem other first");

                            } else if (dataSnapshot3 == null) {


                                Log.d(TAG, "data1 = null ");
                            } else

                                offerlistDatabaseRef.child(pushKey).child("redeemStatus").setValue("usingVoucher");

                            try {


                                FragmentManager fragmentManager = getFragmentManager();
                                useVoucherCheckAndConfirm = new UseVoucherCheckAndConfirm();
                                Log.d(TAG, "fragman null or not :  " + fragmentManager);
                                useVoucherCheckAndConfirm.show(fragmentManager, "");


                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                            Log.d(TAG, "onDataChange:  " + dataSnapshot3.getValue(RewardsPocketOffer.class).getMerchantOfferTitle());


                        }




                              /*  @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/

//

            });




//                    rewardsPocketAdapter.notifyDataSetChanged();

                        merchantRecView.setAdapter(rewardsPocketAdapter);
}


//           String.valueOf(dataSnapshot.child("offerImage").getValue())

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        Log.d(TAG, "onChildRemoved:  " + dataSnapshot);

        try {
//                    int index = rewardsPocketOffers.indexOf(dataSnapshot);

            Log.d(TAG, "onChildRemoved: index " + dataSnapshot.getValue(RewardsPocketOffer.class));


            rewardsPocketOffers.remove(dataSnapshot.getValue(RewardsPocketOffer.class));


            rewardsPocketAdapter.notifyItemRemoved(positionClicked);
            rewardsPocketAdapter.notifyDataSetChanged();

//                rewardsPocketOffers.remove(dataSnapshot.getValue(RewardsPocketOffer.class));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
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


@Override
public void useVoucher(View v,int position){


        Log.d(TAG,"useVoucher:  External use Voucher ` ");
        }
        }
