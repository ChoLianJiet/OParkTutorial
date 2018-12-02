package com.opark.opark.feedback;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.model.Car;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

public class FeedbackModel {

    private static final String TAG = "FeedbackModel" ;
    private static final int FEEDBACK_SUBMISSION = 0;
    private static final int CLEAR_FEEDBACK_FLAG = 1;
    private static final int SETUP_FEEDBACK_RATING = 2;

    final long ONE_MEGABYTE = 1024 * 1024;

    private FirebaseAuth mAuth;
    private String firebaseUserUID;
    private StorageReference storageRef;
    private StorageReference userAvgRatingRef;
    private StorageReference flagStorageLocation;

    private RatingObject userRatingObj;
    private Context mContext;
    private FeedbackModelCallback mCallback;

    public FeedbackModel(Context mContext, FeedbackModelCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;

        mAuth = FirebaseAuth.getInstance();
        firebaseUserUID = mAuth.getCurrentUser().getUid();
//        ref = FirebaseDatabase.getInstance().getReference();
//        userRatingRef = ref.child("users").child("userRatings").child(firebaseUserUID);
        storageRef = FirebaseStorage.getInstance().getReference();
        userAvgRatingRef = storageRef.child("users/" + firebaseUserUID + "/rating.txt");
        flagStorageLocation = storageRef.child("users/" + firebaseUserUID + "/gotflag.txt");

    }

    public interface FeedbackModelCallback {
//        void onUserRatingReceived(double rating);
        void onLastFeedbackCompleted(boolean isCompleted, Car car);
    }


    public void updateAverageRating(int receivedRating) {

        DecimalFormat formatter = new DecimalFormat("#0.00");
//
//        userAvgRatingRef.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                userRatingObj = mutableData.getValue(RatingObject.class);
//                if (userRatingObj == null) {
//                    return Transaction.success(mutableData);
//                }
//                userRatingObj.userRating = (userRatingObj.userRating + receivedRating) / userRatingObj.ratingCount + 1;
//                userRatingObj.ratingCount = userRatingObj.ratingCount + 1;
//
//                // Set value and report transaction success
//                mutableData.setValue(userRatingObj);
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b,
//                                   DataSnapshot dataSnapshot) {
//                // Transaction completed
//                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
//            }
//        });

        userRatingObj.userRating = (userRatingObj.userRating + receivedRating) / (userRatingObj.ratingCount + 1);
        userRatingObj.ratingCount = userRatingObj.ratingCount + 1;
        objToByteStreamUpload(userRatingObj,userAvgRatingRef, FEEDBACK_SUBMISSION);
    }

    public void getAverageRating(final int receivedRating) {

//        userAvgRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userRatingObj = dataSnapshot.getValue(RatingObject.class);
//                if (userRatingObj == null) {
//                    setupUserRating();
//                    return;
//                }
//                Log.d(TAG, "Gsonfrom json success, User Rating is " + userRatingObj.userRating);
//                mCallback.onUserRatingReceived(userRatingObj.userRating);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.i(TAG, "loadRating:onCancelled", databaseError.toException());
//            }
//        });

        userAvgRatingRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                userAvgRatingRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        try {
                            userRatingObj = (new Gson().fromJson(new String(bytes, "UTF-8"), RatingObject.class));

                            Log.d(TAG, "Gsonfrom json success, User Rating is " + userRatingObj.userRating);
                            if (receivedRating != 0) updateAverageRating(receivedRating);
//                            mCallback.onUserRatingReceived(userRatingObj.userRating);

//                    Log.d(TAG,"pointsToUploadFromLoadingScreen is " + pointsToUploadFromLoadingScreen);
                        } catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG,"rating data not received, exception: " + exception);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setupUserRating();
                if (receivedRating != 0) getAverageRating(receivedRating);
            }
        });
    }

    private void objToByteStreamUpload(Object obj, StorageReference destination, final int operation){

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
                switch (operation) {
                    case FEEDBACK_SUBMISSION:
                        feedbackSubmitted();
                        break;
                    default:
                        break;
                }
                Log.i("Hello", "Profile update successful!");
                // Use analytics to calculate the success rate
            }
        });
    }

    private void feedbackSubmitted() {
        Car car = new Car("","","","");
        objToByteStreamUpload(car,flagStorageLocation,CLEAR_FEEDBACK_FLAG);
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(mContext)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setupUserRating() {

        //initial rating 5
        RatingObject newRatingObj = new RatingObject(5.00, 1);
        objToByteStreamUpload(newRatingObj, userAvgRatingRef, SETUP_FEEDBACK_RATING);
//        userRatingRef.setValue(newRatingObj);
    }

    public void getLastFeedbackCompleted() {

        flagStorageLocation.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    Car car = (new Gson().fromJson(new String(bytes, "UTF-8"), Car.class));
                    mCallback.onLastFeedbackCompleted(car.carBrand.isEmpty(), car);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
