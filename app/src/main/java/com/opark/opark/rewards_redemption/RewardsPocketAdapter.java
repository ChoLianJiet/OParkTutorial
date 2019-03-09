package com.opark.opark.rewards_redemption;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opark.opark.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RewardsPocketAdapter extends RecyclerView.Adapter<RewardsPocketAdapter.RewardsPocketViewHolder> {


    private static final String TAG = "RewardsPocketAdapter";
    public UseVoucher mUseVoucher;
    String currentUserId;
    DatabaseReference offerlistDatabaseRef;

    public interface UseVoucher {
        void useVoucher(View v, String pushKey, String merchantOfferTitle, String redemptionCode);



    }
    ChildEventListener offerlistChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull final DataSnapshot dataSnapshot3, @Nullable String s) {
            Log.d(TAG, "onChildAdded: datasnapshot key" + dataSnapshot3.getKey());
            Log.d(TAG, "onChildAdded: datasnapshot children" + dataSnapshot3.getChildren());
            Log.d(TAG, "onChildAdded:  datasnapshot value " + dataSnapshot3.getValue());
            Log.d(TAG, "onChildAdded: s = " + s);






            rewardsRedeemedList.add(dataSnapshot3.getValue(RewardsPocketOffer.class));
//
//                    Log.d("INITDATA", "Data added as class");   Log.d(TAG, "onDataChange:  datasnapshot " + dataSnapshot3.getChildren());
////                                dataSnapshot.getChildren(currentUserId);
//
//
//
////                               dataSnapshot.getValue(RewardsPocketOffer.class).setRedeemStatus("using-voucher");
//
//                Log.d(TAG, "onDataChange: " + dataSnapshot3.getValue(RewardsPocketOffer.class).getRedeemStatus());
//
//                 dataSnapshot3.getKey();
//
//
//                if (dataSnapshot3.getValue(RewardsPocketOffer.class).getRedeemStatus().equals("redeem-other-first")) {
//
//                    Log.d(TAG, "redeem other first");
//
//                } else if (dataSnapshot3 == null) {
//
//
//                    Log.d(TAG, "data1 = null ");
//                } else
//
//
//
//
//                Log.d(TAG, "onDataChange:  " + dataSnapshot3.getValue(RewardsPocketOffer.class).getMerchantOfferTitle());
//



            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            Log.d(TAG, "onChildChanged:  ");
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            String removedItem =  dataSnapshot.getValue(RewardsPocketOffer.class).getPreRedemptionCode();
            Log.d(TAG, "onChildRemoved: key  " + dataSnapshot.getValue(RewardsPocketOffer.class).getPreRedemptionCode());
            for (RewardsPocketOffer rewardsPocketOffer : rewardsRedeemedList){
                if (rewardsPocketOffer.getPreRedemptionCode().equals(removedItem)){
                    rewardsRedeemedList.remove(rewardsPocketOffer);
                    notifyDataSetChanged();
                    break;
                }
            }



//                rewardsPocketOffers.remove(dataSnapshot.getValue(RewardsPocketOffer.class));
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.d(TAG, "onChildMoved:  ");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    };


    public class RewardsPocketViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        CardView rewardsPocketCardView;
        TextView merchantOfferTitle;
        TextView merchantName;
        TextView merchantNumber;
        TextView merchantFirst, merchantSecond, merchantCity, merchantState, merchantPostcode;
        TextView rewardsRedemptionCode;
        ImageView merchantOfferImage;
        TextView expiryDate;

        RewardsPocketOffer rewardsPocketOffer;

        public RewardsPocketViewHolder(View view) {
            super(view);
            rewardsPocketCardView = (CardView) view.findViewById(R.id.rewards_pocket_card_view);
            merchantOfferTitle = (TextView) view.findViewById(R.id.r_merchant_offer_title);
            merchantName = (TextView) view.findViewById(R.id.r_merchant_name);
            merchantNumber = view.findViewById(R.id.r_merchant_number);
            merchantFirst = view.findViewById(R.id.merchant_first);
            merchantSecond = view.findViewById(R.id.merchant_second);
            merchantCity = view.findViewById(R.id.merchant_city);
            merchantState = view.findViewById(R.id.merchant_state);
            merchantPostcode = view.findViewById(R.id.merchant_postcode);
            merchantOfferImage = view.findViewById(R.id.r_merchant_offer_image);
            rewardsRedemptionCode = view.findViewById(R.id.r_redemption_code);
                linearLayout = view.findViewById(R.id.click_here);
                expiryDate = view.findViewById(R.id.r_expiry_date);

        }
    }


    List<RewardsPocketOffer> rewardsRedeemedList;

    public RewardsPocketAdapter(final List<RewardsPocketOffer> rewardsRedeemedList, final UseVoucher mUseVoucher, final String currentUserId) {
        this.rewardsRedeemedList = rewardsRedeemedList;
        this.mUseVoucher = mUseVoucher;
        this.currentUserId = currentUserId;





        /** inner listener**/
         offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/" + currentUserId);

        offerlistDatabaseRef.addChildEventListener(offerlistChildEventListener);

    }


    @NonNull
    @Override
    public RewardsPocketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_pocket_card_view, parent, false);
        RewardsPocketAdapter.RewardsPocketViewHolder rewardsPocketViewHolder = new RewardsPocketViewHolder(v);


        return rewardsPocketViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final RewardsPocketViewHolder holder,  int i) {


        holder.merchantOfferTitle.setText(rewardsRedeemedList.get(i).getMerchantOfferTitle());
        holder.merchantName.setText(rewardsRedeemedList.get(i).getMerchantName());
        holder.merchantFirst.setText(rewardsRedeemedList.get(i).getMerchantAddress().getFirstline());
        holder.merchantSecond.setText(rewardsRedeemedList.get(i).getMerchantAddress().getSecondline());
        holder.merchantCity.setText(rewardsRedeemedList.get(i).getMerchantAddress().getCity());
        holder.merchantState.setText(rewardsRedeemedList.get(i).getMerchantAddress().getCountryState());
        holder.merchantPostcode.setText(rewardsRedeemedList.get(i).getMerchantAddress().getPostcode());
        holder.expiryDate.setText(rewardsRedeemedList.get(i).getExpiryDate());

        holder.rewardsRedemptionCode.setText(rewardsRedeemedList.get(holder.getLayoutPosition()).getPreRedemptionCode());

        final String pushKey = rewardsRedeemedList.get(holder.getLayoutPosition()).getPushKey();
        final String merchOfferTitle = rewardsRedeemedList.get(holder.getLayoutPosition()).getMerchantOfferTitle();
        final String preRedemptionCode = rewardsRedeemedList.get(i).getPreRedemptionCode();

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUseVoucher.useVoucher(v, pushKey , merchOfferTitle
                        ,preRedemptionCode);

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





}
