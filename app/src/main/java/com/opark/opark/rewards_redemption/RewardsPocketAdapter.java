package com.opark.opark.rewards_redemption;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opark.opark.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RewardsPocketAdapter extends RecyclerView.Adapter<RewardsPocketAdapter.RewardsPocketViewHolder>  {


    private static final String TAG = "RewardsPocketAdapter";
    public UseVoucher mUseVoucher;
    public interface UseVoucher{
        void useVoucher(View v,int position);

    }







    public class RewardsPocketViewHolder extends RecyclerView.ViewHolder {
        CardView rewardsPocketCardView;
        TextView merchantOfferTitle;
        TextView merchantName;
        TextView merchantNumber;
        TextView merchantFirst,merchantSecond,merchantCity,merchantState,merchantPostcode;
        TextView rewardsRedemptionCode;
        ImageView merchantOfferImage;

        RewardsPocketOffer rewardsPocketOffer;

        public RewardsPocketViewHolder(View view) {
            super(view);
            rewardsPocketCardView = (CardView) view.findViewById(R.id.rewards_pocket_card_view);
            merchantOfferTitle = (TextView) view.findViewById(R.id.r_merchant_offer_title);
            merchantName = (TextView) view.findViewById(R.id.r_merchant_name);
            merchantNumber=view.findViewById(R.id.r_merchant_number);
            merchantFirst=view.findViewById(R.id.merchant_first);
            merchantSecond=view.findViewById(R.id.merchant_second);
            merchantCity=view.findViewById(R.id.merchant_city);
            merchantState= view.findViewById(R.id.merchant_state);
            merchantPostcode=view.findViewById(R.id.merchant_postcode);
            merchantOfferImage = view.findViewById(R.id.r_merchant_offer_image);
            rewardsRedemptionCode = view.findViewById(R.id.r_redemption_code);


        }
    }



    List<RewardsPocketOffer> rewardsRedeemedList;
    public RewardsPocketAdapter(List<RewardsPocketOffer> rewardsRedeemedList, UseVoucher mUseVoucher/*, String currentUserId*/){
        this.rewardsRedeemedList = rewardsRedeemedList;
        this.mUseVoucher = mUseVoucher;



        /** inner listener**/
/*              DatabaseReference offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/" + currentUserId);

        offerlistDatabaseRef.addChildEventListener();*/

    }


    @NonNull
    @Override
    public RewardsPocketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_pocket_card_view, parent, false);
        RewardsPocketAdapter.RewardsPocketViewHolder rewardsPocketViewHolder = new RewardsPocketViewHolder(v);


        return rewardsPocketViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final RewardsPocketViewHolder holder, final int i) {


        holder.merchantOfferTitle.setText(rewardsRedeemedList.get(i).getMerchantOfferTitle());
        holder.merchantName.setText(rewardsRedeemedList.get(i).getMerchantName());
        holder.merchantFirst.setText(rewardsRedeemedList.get(i).getMerchantAddress().getFirstline());
        holder.merchantSecond.setText(rewardsRedeemedList.get(i).getMerchantAddress().getSecondline());
        holder.merchantCity.setText(rewardsRedeemedList.get(i).getMerchantAddress().getCity());
        holder.merchantState.setText(rewardsRedeemedList.get(i).getMerchantAddress().getCountryState());
        holder.merchantPostcode.setText(rewardsRedeemedList.get(i).getMerchantAddress().getPostcode());



        holder.rewardsRedemptionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUseVoucher.useVoucher(v,i);
                holder.rewardsRedemptionCode.setText(rewardsRedeemedList.get(i).getPreRedemptionCode());




            }
        });
        holder.merchantNumber.setText(rewardsRedeemedList.get(i).getMerchantContact());

//
        Picasso.get()
                .load(rewardsRedeemedList.get(i).getOfferImage())
                .fit()
                .centerCrop()
                .into(holder.merchantOfferImage);


        Log.d("INITDATA", " onBINDview Complete  ");

    }

    @Override
    public int getItemCount() {
        return rewardsRedeemedList.size();
    }







//    private void initializeData(final List<RewardsPocketOffer> rewardsPocketOffers ) {
//
//        Log.d("INITDATA", "initializeData: initialising data");
//
//        ChildEventListener offerlistChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot3, @Nullable String s) {
//                Log.d(TAG, "onChildAdded: datasnapshot key" + dataSnapshot3.getKey());
//                Log.d(TAG, "onChildAdded: datasnapshot children" + dataSnapshot3.getChildren());
//                Log.d(TAG, "onChildAdded:  datasnapshot value " + dataSnapshot3.getValue());
//                Log.d(TAG, "onChildAdded: s = " + s);
//
//
//                if (s == null) {
//
//                } else {
//                    rewardsPocketOffers.add(dataSnapshot3.getValue(RewardsPocketOffer.class));
//
//                    Log.d("INITDATA", "Data added as class");
//
//
//                    rewardsPocketAdapter = new RewardsPocketAdapter(rewardsPocketOffers, new RewardsPocketAdapter.UseVoucher() {
//                        @Override
//                        public void useVoucher(View v, int position) {
//
//                            Log.d(TAG, "useVoucher:  " + v + position);
//                            Log.d(TAG, "useVoucher:  " + rewardsPocketOffers.get(position).getPreRedemptionCode());
//
//
//                            preredemptionCode = rewardsPocketOffers.get(position).getPreRedemptionCode();
//                            merchantOfferTitle = rewardsPocketOffers.get(position).getMerchantOfferTitle();
//                            merchantCoName = rewardsPocketOffers.get(position).getMerchantName();
//
//                        /*final DatabaseReference usingVoucher = FirebaseDatabase.getInstance().getReference().child("offerlist").child("using-voucher")
//                                .child(merchantOfferTitle).child(currentUserId);*/
///*
//                        final StorageReference usingVoucher = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/"+merchantCoName)
//                                .child(merchantOfferTitle).child("using-voucher");*/
//
//                            final DatabaseReference redeemStatus = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist").child(currentUserId);
//
//
//                            redeemStatus.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    Log.d(TAG, "onDataChange:  datasnapshot " + dataSnapshot.getChildren());
////                                dataSnapshot.getChildren(currentUserId);
//
////                               dataSnapshot.getValue(RewardsPocketOffer.class).setRedeemStatus("using-voucher");
//                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//
//                                        Log.d(TAG, "onDataChange: " + dataSnapshot1.getValue(RewardsPocketOffer.class).getRedeemStatus());
//
//                                        pushKey = dataSnapshot1.getKey();
//
//
//                                        if (dataSnapshot1.getValue(RewardsPocketOffer.class).getRedeemStatus().equals("redeem-other-first")) {
//
//                                        } else if (dataSnapshot3 == null) {
//
//
//                                            Log.d(TAG, "data1 = null ");
//                                        } else
//
//                                            redeemStatus.child(pushKey).child("redeemStatus").setValue("usingVoucher");
//
//                                        try {
//                                            FragmentManager fragmentManager = getFragmentManager();
//                                            useVoucherCheckAndConfirm = new UseVoucherCheckAndConfirm();
//                                            Log.d(TAG, "fragman null or not :  " + fragmentManager);
//                                            useVoucherCheckAndConfirm.show(fragmentManager, "");
//                                        } catch (NullPointerException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        Log.d(TAG, "onDataChange:  " + dataSnapshot1.getValue(RewardsPocketOffer.class).getMerchantOfferTitle());
//
//
//                                    }
//
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//
////
//
//
//                        }
//                    });
//
//                    notifyDataSetChanged();
//
////                        merchantRecView.setAdapter(rewardsPocketAdapter);
//                }
//            }
//
//
////           String.valueOf(dataSnapshot.child("offerImage").getValue())
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                Log.d(TAG, "onChildRemoved:  " + dataSnapshot);
//
//                try {
////                    int index = rewardsPocketOffers.indexOf(dataSnapshot);
//
//                    Log.d(TAG, "onChildRemoved: index " + dataSnapshot.getValue(RewardsPocketOffer.class));
//                    rewardsPocketOffers.remove(dataSnapshot.getValue(RewardsPocketOffer.class));
//
//                    notifyDataSetChanged();
//
////                rewardsPocketOffers.remove(dataSnapshot.getValue(RewardsPocketOffer.class));
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        };
//
//
//
//
//
////        merchantOffer = new ArrayList<>();
//
////        merchantOffer.add(new MerchantOffer("Kenny Rogers 10 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000","sd;fadsijf"));
////        merchantOffer.add(new MerchantOffer("Kenny Rogers 20 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000",));
////        merchantOffer.add(new MerchantOffer("Kenny Rogers 40 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000",));
//    }




}
