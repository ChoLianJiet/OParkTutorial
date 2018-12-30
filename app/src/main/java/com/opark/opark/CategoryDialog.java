package com.opark.opark;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opark.opark.R;
import com.opark.opark.merchant_side.MerchUploadOfferActivity;

public class CategoryDialog extends DialogFragment {

    private TextView fNb, healthNBeauty, shopping, services, travel, entertainment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_category_dialog, container, false);
        getDialog().setCanceledOnTouchOutside(false);

        bindViews(view);

        setCategoryOnClick();


        return view;
    }

    private void bindViews(View view) {

        fNb =view.findViewById(R.id.f_b_category);
        healthNBeauty= view.findViewById(R.id.health_beauty_category);
        shopping = view.findViewById(R.id.shopping_category);
        services = view.findViewById(R.id.services_category);
        travel = view.findViewById(R.id.travel_category);
        entertainment = view.findViewById(R.id.entertainment_category);

    }



    private void setCategoryOnClick(){
        fNb.setOnClickListener(categoryOnClick);
        healthNBeauty.setOnClickListener(categoryOnClick);
        shopping.setOnClickListener(categoryOnClick);
        services.setOnClickListener(categoryOnClick);
        travel.setOnClickListener(categoryOnClick);
        entertainment.setOnClickListener(categoryOnClick);


    }





    private View.OnClickListener categoryOnClick = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId() /*to get clicked view id**/) {
                case R.id.f_b_category:

                    MerchUploadOfferActivity.offerCategory.setText(fNb.getText().toString());
                    dismiss();
                    break;
                case R.id.health_beauty_category:

                    MerchUploadOfferActivity.offerCategory.setText(healthNBeauty.getText().toString());
                    dismiss();
                    break;
                case R.id.shopping_category:

                    MerchUploadOfferActivity.offerCategory.setText(shopping.getText().toString());
                    dismiss();
                    break;

                case R.id.services_category:
                    MerchUploadOfferActivity.offerCategory.setText(services.getText().toString());
                    dismiss();

                    break;

                case R.id.travel_category:

                    MerchUploadOfferActivity.offerCategory.setText(travel.getText().toString());
                    dismiss();
                    break;

                case R.id.entertainment_category:
                    MerchUploadOfferActivity.offerCategory.setText(entertainment.getText().toString());
                    dismiss();

                default:
                    break;
            }
        }
    };
}
