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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.opark.opark.R;
import com.opark.opark.merchant_side.merchant_class.Merchant;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;
import com.opark.opark.rewards_redemption.RewardsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MerchantOfferAdapter extends RecyclerView.Adapter<MerchantOfferAdapter.MerchantOfferAdapterViewHolder> implements  Filterable {
    private Context context;
    public  ButtonClicked mButtonClicked;
    List<MerchantOffer> originalList;
    public CardClicked mCardClicked;

    public interface ButtonClicked{
        void onButtonClicked(View v, int position);



    }

    public interface CardClicked{
        void onCardClicked(View v, int position);



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
    public MerchantOfferAdapter(List<MerchantOffer> merchantOfferList, ButtonClicked buttonClicked, CardClicked cardClicked){
        this.merchantOfferList = merchantOfferList;
        this.mButtonClicked = buttonClicked;
        this.originalList = new ArrayList<>(merchantOfferList);
        this.mCardClicked = cardClicked;


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
                ConfirmPreRedeem.redeemCost = Integer.valueOf( merchantOfferList.get(i).getOfferCost());
                ConfirmPreRedeem.merchantName = merchantOfferList.get(i).getMerchantName();
                ConfirmPreRedeem.merchantOfferTitle = merchantOfferList.get(i).getMerchantOfferTitle();
                ConfirmPreRedeem.rewardsMerchant = merchantOfferList.get(i).getMerchantName();
                ConfirmPreRedeem.merchantAddress = merchantOfferList.get(i).getMerchantAddress();
                ConfirmPreRedeem.merchantContact = merchantOfferList.get(i).getMerchantContact();
                ConfirmPreRedeem.merchantOfferImageUrl = merchantOfferList.get(i).getOfferImage();



                mButtonClicked.onButtonClicked(v, i);

                Log.d("redeem", "onClick: " + RewardsFragment.merchantOfferTitle);
                Log.d("redeem", "MerchantOfferAdapter button : " + mButtonClicked);
                Log.d("redeem", "offercost in adaapter : " + RewardsFragment.redeemCost);


            }
        });


        holder.merchantCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCardClicked.onCardClicked(view,i);

                Log.d("cardclick", "onClick: " + view + "i " + i);
            }
        });

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


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MerchantOffer> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalList);
                Log.d("filter", "performFiltering: Constraints is null/0 "+ originalList);
            } else {

                Log.d("filter", "performFiltering: Constraints is not null "+ originalList);
                String filterPattern = constraint.toString().toLowerCase().trim();

                Log.d("filter", "performFiltering: filterPattern " + filterPattern);
                for (MerchantOffer item : originalList) {
                    if (item.getMerchantOfferTitle().toLowerCase().contains(filterPattern)) {
                        Log.d("filter", "performFiltering: item contain filterPattern " + item.getMerchantOfferTitle().toLowerCase().contains(filterPattern));
                        filteredList.add(item);
                    }

                    //TODO to add more filters for address and merchants name
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d("filter", "publishResults: +" + results.values);
            merchantOfferList.clear();
            merchantOfferList.addAll( (List<MerchantOffer>) results.values);
            notifyDataSetChanged();
        }
    };


}