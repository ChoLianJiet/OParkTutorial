package com.opark.opark;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.model.User;
import com.opark.opark.motion_vehicle_tracker.Map;
import com.opark.opark.share_parking.MapsMainActivity;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

public class UserPopUpFragment extends Fragment {

    final String TAG = "UserPopUpFragment";
    public static String KENA_DETAILS_ID_PREFS;
    public static String KENA_DETAILS_ID_KEY;
    final long ONE_MEGABYTE = 1024 * 1024;

    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    private DatabaseReference matchmakingRef;
    private DatabaseReference geofireRef;
    private DatabaseReference togetherRef;

    FrameLayout mFrameLayout;
    public static TextView kenaParkerName;
    public static TextView carModel;
    public static TextView carPlateNumber;
    public static TextView carColor;
    ArrayList<User> userObjList = new ArrayList<>();
    String foundUser;
    Button acceptButton;
    Button declineButton;
    FloatingActionButton xButton;
    OnUserPopUpFragmentListener mCallback;
    List<String> words = new ArrayList<>();

    public interface OnUserPopUpFragmentListener {
        void onArticleSelected(int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_pop_up_fragment, container, false);

        bindViews(view);

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsMainActivity.kenaMarker.remove();
                int position = 124;
                mCallback.onArticleSelected(position);
                returnToMain();
                matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM0);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptUser();
                int position = 124;
                mCallback.onArticleSelected(position);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineUser();
                int position = 123;
                mCallback.onArticleSelected(position);
            }
        });

        setKenaDetailsOnWindow();

        listenToDatabase();

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        if (context instanceof OnUserPopUpFragmentListener){
            mCallback = (OnUserPopUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnUserPopUpFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"Fragment is created");
    }

    private void bindViews(View view){

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");

        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");

        kenaParkerName = (TextView) view.findViewById(R.id.kenaName);
        carModel = (TextView) view.findViewById(R.id.carModel);
        carPlateNumber = (TextView) view.findViewById(R.id.carPlateNumber);
        carColor = (TextView) view.findViewById(R.id.carColor);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.popupuser);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MapsMainActivity.USER_ID_PREFS, Context.MODE_PRIVATE);
        foundUser = prefs.getString(MapsMainActivity.USER_ID_KEY, null);



        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");

        acceptButton = (Button) view.findViewById(R.id.accept);
        declineButton = (Button) view.findViewById(R.id.decline);
        xButton = (FloatingActionButton) view.findViewById(R.id.floatingXButton);

    }

    public void setKenaDetailsOnWindow(){

        Log.d(TAG,"displayKenaDetailsOnWindow is called");

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();

        final StorageReference getKenaProfileRef = storageRef.child("users/" + foundUser + "/profile.txt");
        getKenaProfileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    userObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
                    Log.d(TAG, "Gsonfrom json success");

                    kenaParkerName.setText(userObjList.get(0).getUserName().getFirstName() + userObjList.get(0).getUserName().getLastName());
                    carModel.setText(userObjList.get(0).getUserCar().getCarBrand() + userObjList.get(0).getUserCar().getCarModel());
                    carPlateNumber.setText(userObjList.get(0).getUserCar().getCarPlate());
                    carColor.setText(userObjList.get(0).getUserCar().getCarColour());

                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"fragment is not created, exception: " + exception);
            }
        });
    }

    private void acceptUser(){

        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM2);
        togetherRef.child(foundUser).child("peter").setValue(MapsMainActivity.currentUserID);

        Intent intent = new Intent(getActivity(),PeterMap.class);
        startActivity(intent);

    }

    private void declineUser(){

        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM0);
        MapsMainActivity.newArrayList.remove(foundUser);

        MapsMainActivity.oldArrayList.add(foundUser);
        MapsMainActivity.oldHashSet.addAll(MapsMainActivity.oldArrayList);
        MapsMainActivity.oldArrayList.clear();
        MapsMainActivity.oldArrayList.addAll(MapsMainActivity.oldHashSet);
        MapsMainActivity.oldHashSet.clear();

        MapsMainActivity.kenaMarker.remove();

        Log.i(TAG,foundUser + " has been removed from newArrayList due to declining");
        Log.i(TAG,"MapsMainActivity newArraylist " + MapsMainActivity.newArrayList);
        Log.i(TAG,"MapsMainActivity oldArraylist" + MapsMainActivity.oldArrayList);

    }


    private void returnToMain(){

        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
        FragmentTransaction transaction = manager.beginTransaction();

        if (MapsMainActivity.position != 123){
            transaction.remove(MapsMainActivity.userPopUpFragment);
            Log.d(TAG,"userPopUpFragment is removed");
        } else {
            transaction.remove(MapsMainActivity.userPopUpFragment);
            transaction.remove(MapsMainActivity.userPopUpFragment1);
            Log.d(TAG,"userPopUpFragment and userPopUpFragment1 is removed");
        }
        MapsMainActivity.kenaMarker.remove();
        transaction.commit();
    }

    private void listenToDatabase(){

        matchmakingRef.child(foundUser).child("adatem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        String adatemValue = dataSnapshot.getValue().toString();
                        if (adatemValue.equals("Not Available") || adatemValue == MapsMainActivity.ADATEM0) {
//                            MapsMainActivity.kenaMarker.remove();
                            returnToMain();
                            int position = 123;
                            mCallback.onArticleSelected(position);
                            MapsMainActivity.kenaMarker.remove();
                        }
                    } catch (NullPointerException e) {
                        System.out.println(e);
                    }
            }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
        });
    }

}
