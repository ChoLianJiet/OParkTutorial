package com.opark.opark;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.model.User;
import com.opark.opark.share_parking.MapsMainActivity;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

public class UserPopUpFragment extends Fragment {

    final String TAG = "UserPopUpFragment";
    public static String USER_DECLINE_PREFS;
    public static String USER_DECLINE_KEY;
    public static String DECLINED = "declined";
    final long ONE_MEGABYTE = 1024 * 1024;

    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    private DatabaseReference matchmakingRef;
    private DatabaseReference geofireRef;
    private DatabaseReference togetherRef;

    FrameLayout mFrameLayout;
    TextView kenaParkerName;
    TextView carModel;
    TextView carPlateNumber;
    TextView carColor;
    ArrayList<User> userObjList = new ArrayList<>();
    String foundUser;
    Button acceptButton;
    Button declineButton;
    FloatingActionButton xButton;
    UserPopUpFragment userPopUpFragment;

    public static UserPopUpFragment newInstance(){
        return new UserPopUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_pop_up_fragment, container, false);

        bindViews(view);

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsMainActivity.kenaMarker.remove();
                MapsMainActivity.kenaMarker = null;
                returnToMain();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptUser();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineUser();
            }
        });

        setKenaDetailsOnWindow();

        return view;

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
        togetherRef.child(foundUser).setValue(MapsMainActivity.currentUserID);
        Intent intent = new Intent(getActivity(),PeterMap.class);
        startActivity(intent);
        getActivity().finish();
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
        MapsMainActivity.kenaMarker = null;

        Log.i(TAG,foundUser + " has been removed from newArrayList due to declining");
        Log.i(TAG,"MapsMainActivity newArraylist " + MapsMainActivity.newArrayList);
        Log.i(TAG,"MapsMainActivity oldArraylist" + MapsMainActivity.oldArrayList);
        getActivity().finish();

    }

    private void returnToMain(){

//        mFrameLayout.setVisibility(View.INVISIBLE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(MapsMainActivity.userPopUpFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM0);
    }


}
