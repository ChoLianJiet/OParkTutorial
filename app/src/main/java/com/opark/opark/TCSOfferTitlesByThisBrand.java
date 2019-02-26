package com.opark.opark;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.opark.opark.ShowBrandOffer.offerTitleByBrands;


public class TCSOfferTitlesByThisBrand  implements Continuation<Void, Task<List<String>>> {
    public static List<String> thisBrandOffers = new ArrayList<>();
    private static final String TAG = "TCSOfferTitlesByThisBrand";


    @Override
    public Task<List<String>> then(@NonNull Task<Void> task) throws Exception {

        final TaskCompletionSource<List<String>> tcs = new TaskCompletionSource<>();

        Log.d(TAG, "in ");
       /* DatabaseReference listenBrandSelected = FirebaseDatabase.getInstance().getReference().child("offerlist/merchantsName/" + BrandsOfferFragment.unityName);



        listenBrandSelected.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot1, @Nullable String s) {


                Log.d(TAG, "datasnapshot data is " +  dataSnapshot1.getValue());
                thisBrandOffers.add(dataSnapshot1.getValue().toString());

                tcs.setResult(thisBrandOffers);
                Log.d(TAG, "onChildAdded:  " + thisBrandOffers );



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/


      Task t= Tasks.call(new NestedListenersForBrandsOffer());

        Log.d(TAG, "then: d" + thisBrandOffers);
      thisBrandOffers =Tasks.await(Tasks.forResult( (List<String>)t .getResult()));
       tcs.setResult(thisBrandOffers);

        Log.d(TAG, "then:  " + thisBrandOffers);
        return tcs.getTask();

    }


}
