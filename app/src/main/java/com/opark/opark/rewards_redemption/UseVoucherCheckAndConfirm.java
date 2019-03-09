package com.opark.opark.rewards_redemption;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.R;


import static com.opark.opark.rewards_redemption.RewardsPocketFragment.currentUserId;
import static com.opark.opark.rewards_redemption.RewardsPocketFragment.merchantCoName;
import static com.opark.opark.rewards_redemption.RewardsPocketFragment.preredemptionCode;

public class UseVoucherCheckAndConfirm extends DialogFragment {


    private static final String TAG = "UseVoucherCheckAndConfirm";
    private Button confirmButton;
    private TextView useVoucherErrorText;
    private TextView useVoucherText;
    private Dialog dialog;
    private String merchantOfferTitle;
    String preredemptionCode;
    DatabaseReference usingVoucher;
    String key;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_using_voucher_dialog_fragment,container,false);
//        getDialog().setCanceledOnTouchOutside(false);
//        getDialog().setCancelable(false);

        Bundle bundle = getArguments();
       key = bundle.getString("pushkey");
       merchantOfferTitle = bundle.getString("merchantoffertitle");
       preredemptionCode = bundle.getString("redemptioncode");

             dialog =getDialog();
        bindViews(view);

        DatabaseReference redemptionCodeDataRef = FirebaseDatabase.getInstance().getReference().child("offerlist/using-voucher");


        Log.d(TAG, " all " + currentUserId +" "+ preredemptionCode);
        usingVoucher = FirebaseDatabase.getInstance().getReference().child("offerlist").child("using-voucher")
                                .child(merchantOfferTitle).child(currentUserId);

        usingVoucher.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                        try{

                            Log.d(TAG, " all " + currentUserId +" "+ preredemptionCode);
                            Log.d(TAG, "key:  " + dataSnapshot.getKey());
                            Log.d(TAG, "value:  " + dataSnapshot.getValue());

                            confirmButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (dataSnapshot.getValue()==null){

                                        usingVoucher.setValue(preredemptionCode);



                                        Log.d(TAG, "set value");


                                        dialog.dismiss();
                                    }



                                }
                            });

                        if (dataSnapshot.getKey().equals(currentUserId) && (!dataSnapshot.getValue().toString().equals(preredemptionCode))){

                            Log.d(TAG, "onDataChange: USER REDEEMING ITEM " );


                           DatabaseReference redeemStatus = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist").child(currentUserId)
                            .child(key).child("redeemStatus");


//                           redeemStatus.setValue("redeem-other-first");
                            useVoucherErrorText.setVisibility(View.VISIBLE);
//                            useVoucherText.setText(R.string.using_voucher_error);
                            confirmButton.setVisibility(View.INVISIBLE);
                        }
                        else if (dataSnapshot.getValue().toString().equals(preredemptionCode)){
                            useVoucherText.setText("Please complete the redemption of this voucher with the merchant");
                        }
                        }catch (NullPointerException e ){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });

        return view;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void bindViews(View view){
        confirmButton = view.findViewById(R.id.confirm_use_voucher_button);
        useVoucherErrorText = view.findViewById(R.id.use_voucher_error_text);

        useVoucherText = view.findViewById(R.id.inform_text);
    }

}
