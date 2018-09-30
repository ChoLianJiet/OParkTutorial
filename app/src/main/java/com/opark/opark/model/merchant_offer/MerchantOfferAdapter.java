package com.opark.opark.model.merchant_offer;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.opark.opark.R;

import org.w3c.dom.Text;

import java.util.List;

public class MerchantOfferAdapter extends RecyclerView.Adapter<MerchantOfferAdapter.MerchantOfferAdapterViewHolder> {
    public static class MerchantOfferAdapterViewHolder extends RecyclerView.ViewHolder {

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
     public MerchantOfferAdapter(List<MerchantOffer> merchantOfferList){
        this.merchantOfferList = merchantOfferList;


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
    public void onBindViewHolder(@NonNull MerchantOfferAdapterViewHolder holder, int i) {
            holder.merchantOfferTitle.setText(merchantOfferList.get(i).getMerchantOfferTitle());
            holder.merchantName.setText(merchantOfferList.get(i).getMerchantName());
            holder.merchantAddress.setText(merchantOfferList.get(i).getMerchantAddress());
            holder.merchantNumber.setText(merchantOfferList.get(i).getMerchantContact());
            holder.merchantOfferImage.setImageResource(merchantOfferList.get(i).getOfferImage());

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
