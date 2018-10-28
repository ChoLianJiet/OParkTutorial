package com.opark.opark.merchant_side;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.R;
import com.opark.opark.merchant_side.merchant_class.Merchant;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class MerchProfileSetup extends AppCompatActivity {
    private static final String TAG = "MerchProfileSetup";
    Button merchProfSubmitButton;
    String currentMerchantEmail;
    FirebaseUser oParkMerchantFirebase;
    Merchant oParkMerchant = new Merchant();
    FirebaseAuth mAuth;
    EditText coName;
    EditText coRegNum;
    EditText personSignUp;
    EditText coPhone;
    EditText coAddress;
    TextView coEmail;
    EditText[] MerchProfileEntryEditText = new EditText[5];
    String[] MerchProfilEntryString = new String[6];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merch_profile_setup);

        mAuth = FirebaseAuth.getInstance();
        oParkMerchantFirebase = mAuth.getCurrentUser();
        currentMerchantEmail = oParkMerchantFirebase.getEmail();
        Log.d(TAG, "onCreate: " + currentMerchantEmail);

        final String merchantID = mAuth.getCurrentUser().getUid();

        bindviews();
        coEmail.setText(currentMerchantEmail);


        merchProfSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptProfileSetup(merchantID);
            }
        });





    }



    private void collectMerchEntry(){




        MerchProfileEntryEditText[0] = coName;
        MerchProfileEntryEditText[1] = coRegNum;
        MerchProfileEntryEditText[2] = coPhone;
        MerchProfileEntryEditText[3] = coAddress;
        MerchProfileEntryEditText[4] = personSignUp;

        MerchProfilEntryString[0] = oParkMerchant.merchCoName = coName.getText().toString();
        MerchProfilEntryString[1] = oParkMerchant.merchCoRegNumber= coRegNum.getText().toString();
        MerchProfilEntryString[2] = oParkMerchant.merchContact = coPhone.getText().toString();
        MerchProfilEntryString[3] = oParkMerchant.merchCoAddress = coAddress.getText().toString();
        MerchProfilEntryString[4] = oParkMerchant.merchSignUpPerson =personSignUp.getText().toString();
        MerchProfilEntryString[5] = oParkMerchant.merchEmail = coEmail.getText().toString() ;


    }


    private boolean emptyCheck (String[] string, EditText[] editTexts , int size){

        boolean cancelSetup =false;
        View focusView =null;
        View focusView1 =null;

        for (int i = 0; i < size; i++){
            if ((string[i].isEmpty())) {
                editTexts[i].setError(getString(R.string.error_field_required));
                focusView = editTexts[i];
                cancelSetup = true;
//                if(string[5].isEmpty()){
//                    coEmail.setError(getString(R.string.error_field_required));
//                    focusView1=coEmail;
//                    cancelSetup=true;
//                }

            }


            if (cancelSetup) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
//                assert focusView1 != null;
//                focusView1.requestFocus();

            }

        }

        return cancelSetup;

    }




    public void objToByteStreamUpload(Object obj, StorageReference destination){

        String objStr = new Gson().toJson(obj);
        InputStream in = new ByteArrayInputStream(objStr.getBytes(Charset.forName("UTF-8")));
        UploadTask uploadTask = destination.putStream(in);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                showErrorDialog("Failed to update your profile. Try again maybe? ");
                // Use analytics to find out why is the error
                // then only implement the best corresponding measures


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Profile update successful!", Toast.LENGTH_LONG).show();
                Log.i("Hello", "Profile update successful!");
                // Use analytics to calculate the success rate
            }
        });
    }


    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")

                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void bindviews(){
        merchProfSubmitButton=findViewById(R.id.merch_profile_submit_button);
        coName=findViewById(R.id.merch_co_name);
        coRegNum=findViewById(R.id.merch_co_number);
        coPhone = findViewById(R.id.merch_co_phone_number);
        coAddress = findViewById(R.id.merch_co_address);
        personSignUp = findViewById(R.id.merch_sign_up_person);
        coEmail = findViewById(R.id.merch_email);

    }










    private void attemptProfileSetup(String merchantID) {


        boolean checkMerchEntrySetup = false;

        collectMerchEntry();




        checkMerchEntrySetup= emptyCheck(MerchProfilEntryString,MerchProfileEntryEditText, 5);


// do empty check if returns true, break operation
        if(checkMerchEntrySetup) {
            return;
        }


        else {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            StorageReference userFolder = storageRef.child("merchants/" + "merchantlist/" + currentMerchantEmail + "/merchProf.txt");
            objToByteStreamUpload(oParkMerchant, userFolder);

            Log.d(TAG, "attemptProfileSetup: uploaded to storage");

            //TODO intent to card swipe or other page ? maybe upload prof pic ?
//            Intent intent = new Intent(MerchProfileSetup.this, LoginActivity.class);
//            finish();
//            startActivity(intent);}
    }
}


}
