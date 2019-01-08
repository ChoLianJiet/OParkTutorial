package com.opark.opark.rewards_redemption.CategoryOffer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.opark.opark.R;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.rewards_redemption.RewardsFragment;

import java.util.List;

public class CategoryOfferTypeAdapter extends RecyclerView.Adapter<CategoryOfferTypeAdapter.CategoryOfferTypeAdapterViewHolder> {
    private Context context;

    public CategoryOfferTypeAdapter(Context context) {
        this.context = context;
    }


    public class CategoryOfferTypeAdapterViewHolder extends RecyclerView.ViewHolder {
        MerchantOffer merchantOffer;
        CardView offerCategoryCard;
        RecyclerView categoryOfferRecview;


        public CategoryOfferTypeAdapterViewHolder(View itemView) {
            super(itemView);

            offerCategoryCard = itemView.findViewById(R.id.category_card);
            categoryOfferRecview = itemView.findViewById(R.id.category1_recview);

        }

}



    @NonNull
    @Override
    public CategoryOfferTypeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryOfferTypeAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
