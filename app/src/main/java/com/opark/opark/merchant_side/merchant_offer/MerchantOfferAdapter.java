package com.opark.opark.merchant_side.merchant_offer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.R;
import com.opark.opark.RewardsFragment;
import com.opark.opark.RewardsPocketOffer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MerchantOfferAdapter extends RecyclerView.Adapter<MerchantOfferAdapter.MerchantOfferAdapterViewHolder> {
    private Context context;
    public  ButtonClicked mButtonClicked;

    public interface ButtonClicked{
        void onButtonClicked(View v, int position);



    }

    public class MerchantOfferAdapterViewHolder extends RecyclerView.ViewHolder {

        CardView merchantCardView;
        TextView merchantOfferTitle;
        TextView merchantName;
        TextView merchantNumber;
        TextView merchantAddress;
        TextView redeemCost;
        Button redeemButton;
        ImageView merchantOfferImage;
        MerchantOffer merchantOffer;


        MerchantOfferAdapterViewHolder(View itemView) {
            super(itemView);

            merchantCardView = (CardView) itemView.findViewById(R.id.merchant_offer_card_view);
            merchantOfferTitle = (TextView) itemView.findViewById(R.id.merchant_offer_title);
            merchantName = (TextView) itemView.findViewById(R.id.merchant_name);
            merchantNumber = (TextView) itemView.findViewById(R.id.merchant_number);
            merchantAddress = (TextView) itemView.findViewById(R.id.merchant_address);
            redeemCost = (TextView) itemView.findViewById(R.id.redeem_cost);
            merchantOfferImage = (ImageView) itemView.findViewById(R.id.merchant_offer_image);
            redeemButton = (Button) itemView.findViewById(R.id.redeem_button);

        }



    }



    List<MerchantOffer> merchantOfferList;
    public MerchantOfferAdapter(List<MerchantOffer> merchantOfferList, ButtonClicked buttonClicked){
        this.merchantOfferList = merchantOfferList;
        this.mButtonClicked = buttonClicked;

    }



    @Override
    public int getItemCount() {
        return merchantOfferList.size();
    }


    @NonNull
    @Override
    public MerchantOfferAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rewards_fragment, parent, false);
        MerchantOfferAdapterViewHolder merchantOfferAdapterViewHolder = new MerchantOfferAdapterViewHolder(v);


        return merchantOfferAdapterViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MerchantOfferAdapterViewHolder holder, final int i) {


        holder.merchantOfferTitle.setText(merchantOfferList.get(i).getMerchantOfferTitle());
        holder.merchantName.setText(merchantOfferList.get(i).getMerchantName());
        holder.merchantAddress.setText(merchantOfferList.get(i).getMerchantAddress());

        holder.redeemCost.setText(merchantOfferList.get(i).getOfferCost());

        holder.merchantNumber.setText(merchantOfferList.get(i).getMerchantContact());

//
        Picasso.get()
                .load(merchantOfferList.get(i).getOfferImage())
                .fit()
                .centerCrop()
                .into(holder.merchantOfferImage);


        Log.d("INITDATA", " onBINDview Complete  ");

//        holder.merchantOfferImage.setImageURI(merchantOfferList.get(i).getOfferImage());
//        holder.merchantOfferImage.setImageBitmap(StringToBitMap(merchantOfferList.get(i).getOfferImage()));

        holder.redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardsFragment.redeemCost = Integer.valueOf( merchantOfferList.get(i).getOfferCost());
                RewardsFragment.merchantName = merchantOfferList.get(i).getMerchantName();
                RewardsFragment.merchantOfferTitle = merchantOfferList.get(i).getMerchantOfferTitle();
                RewardsFragment.rewardsMerchant = merchantOfferList.get(i).getMerchantName();
                RewardsFragment.merchantAddress = merchantOfferList.get(i).getMerchantAddress();
                RewardsFragment.merchantContact = merchantOfferList.get(i).getMerchantContact();
                RewardsFragment.merchantOfferImageUrl = merchantOfferList.get(i).getOfferImage();



                mButtonClicked.onButtonClicked(v, i);

                Log.d("redeem", "onClick: " + RewardsFragment.merchantOfferTitle);
                Log.d("redeem", "MerchantOfferAdapter button : " + mButtonClicked);
                Log.d("redeem", "offercost in adaapter : " + RewardsFragment.redeemCost);


            }
        });

//

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }




    public void setOnButtonClick(ButtonClicked listener)
    {
        this.mButtonClicked=listener;
    }


}