package com.opark.opark;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.R;
import com.opark.opark.merchant_side.MerchUploadOfferActivity;
import com.opark.opark.rewards_redemption.RewardsPreredemption;

public class MerchantAcceptUsingVoucher extends DialogFragment {

    private static final String TAG = "MerchantAcceptUsingVouc";


    DataSnapshot dataSnapshot;
    AcceptButtonClicked acceptButtonClicked;
    String userRedemptionCode,redeemingUserId,offerTitle,merchantProfileName,redeemedKey;

    private Button acceptButton;
    private Button declineButton;
    private TextView useVoucherUid;
    private TextView useVoucherRedemptionCode;

    Dialog dialog ;
    public interface AcceptButtonClicked{
        void acceptButtonClicked();
    }

    public MerchantAcceptUsingVoucher() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_dialog_merchant_accept_voucher, container, false);
//        getDialog().setCanceledOnTouchOutside(false);
//        getDialog().setCancelable(false);
        bindViews(view);
            dialog = getDialog();

        Bundle argument = getArguments();
        userRedemptionCode = argument.getString("redemptioncode");
        redeemingUserId = argument.getString("redeeminguser");
        offerTitle = argument.getString("offertitle");
        merchantProfileName = argument.getString("merchantprofilename");

        useVoucherUid.setText(redeemingUserId);
        useVoucherRedemptionCode.setText(userRedemptionCode);
        Log.d(TAG, "userRedemptionCode " + userRedemptionCode + " redeemingUser " + redeemingUserId);


        final DatabaseReference usingVoucher = FirebaseDatabase.getInstance().getReference().child("offerlist/using-voucher/" + offerTitle)
                .child(redeemingUserId);
        final DatabaseReference offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");
        final StorageReference rewardStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/" + merchantProfileName + "/" + offerTitle + "/userredemptioncode/" + userRedemptionCode);


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                        final long ONE_MEGABYTE = 1024 * 1024;

                        Task task = rewardStoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                redeemedKey = new Gson().fromJson(new String(bytes), RewardsPreredemption.class).getPushKey();


                                Log.d(TAG, "onSuccess: " + redeemedKey);
                                usingVoucher.removeValue();
                                offerlistDatabaseRef.child("offer-redemption-count/" + offerTitle).child("redeemedUsers").push().setValue(redeemingUserId);
                                rewardStoRef.delete();
                                DatabaseReference userPreRedemList = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/" + redeemingUserId + "/" + redeemedKey);
                                userPreRedemList.removeValue();

                                dialog.dismiss();

                            }



                        });


                Task whenAll = Tasks.whenAll(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                    }
                });



            }

        });
        return view;

    }



    private void bindViews(View view){
        acceptButton = view.findViewById(R.id.accept_voucher_accept_button);
        declineButton = view.findViewById(R.id. accept_voucher_decline_button);
        useVoucherUid= view.findViewById(R.id.accept_voucher_user_id_name);
        useVoucherRedemptionCode=view.findViewById(R.id.accept_voucher_redemption_code);

    }

}

