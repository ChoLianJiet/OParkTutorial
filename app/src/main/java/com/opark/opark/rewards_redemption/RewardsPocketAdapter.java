package com.opark.opark.rewards_redemption;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.opark.opark.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RewardsPocketAdapter extends RecyclerView.Adapter<RewardsPocketAdapter.RewardsPocketViewHolder>  {





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
    public RewardsPocketAdapter(List<RewardsPocketOffer> rewardsRedeemedList){
        this.rewardsRedeemedList = rewardsRedeemedList;

    }


    @NonNull
    @Override
    public RewardsPocketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_pocket_card_view, parent, false);
        RewardsPocketAdapter.RewardsPocketViewHolder rewardsPocketViewHolder = new RewardsPocketViewHolder(v);


        return rewardsPocketViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RewardsPocketViewHolder holder, int i) {


        holder.merchantOfferTitle.setText(rewardsRedeemedList.get(i).getMerchantOfferTitle());
        holder.merchantName.setText(rewardsRedeemedList.get(i).getMerchantName());
        holder.merchantFirst.setText(rewardsRedeemedList.get(i).getMerchantAddress().getFirstline());
        holder.merchantSecond.setText(rewardsRedeemedList.get(i).getMerchantAddress().getSecondline());
        holder.merchantCity.setText(rewardsRedeemedList.get(i).getMerchantAddress().getCity());
        holder.merchantState.setText(rewardsRedeemedList.get(i).getMerchantAddress().getCountryState());
        holder.merchantPostcode.setText(rewardsRedeemedList.get(i).getMerchantAddress().getPostcode());



        holder.rewardsRedemptionCode.setText(rewardsRedeemedList.get(i).getPreRedemptionCode());
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
