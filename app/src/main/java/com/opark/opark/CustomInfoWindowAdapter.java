package com.opark.opark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.model.User;
import com.opark.opark.share_parking.MapsMainActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    String TAG = "CustomInfoWindowAdapter";
    final long ONE_MEGABYTE = 1024 * 1024;

    private final View mWindow;
    private Context mContext;
    private Button acceptButton;
    public static String foundUser;
    public static TextView tvUserIDView;
    private DatabaseReference matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
    private DatabaseReference togetherRef = FirebaseDatabase.getInstance().getReference().child("together");
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;

    //Kena Details
    public static ArrayList<User> kenaUserObjList = new ArrayList<>();
    private String kenaParkerName;
    private String kenaCarModel;
    private String kenaCarPlateNumber;
    private String kenaCarColor;

    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);
    }

    @SuppressLint("SetTextI18n")
    private void rendorWindowText(final Marker marker, final View view, final String fndUser){

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
        String title = marker.getTitle();
        final TextView tvTitle = (TextView) view.findViewById(R.id.title);
        String snippit = marker.getSnippet();
        final TextView tvSnippit = (TextView) view.findViewById(R.id.snippit);
        acceptButton = (Button) view.findViewById(R.id.info_window_accept);
        tvUserIDView = (TextView) view.findViewById(R.id.user_id_view);
        try {
//        tvUserIDView.setText(((MarkerTag) marker.getTag()).getUID());
            tvUserIDView.setText(((MarkerTag) marker.getTag()).getUID());
        String foundUser =tvUserIDView.getText().toString();

//            final StorageReference getKenaProfileRef = storageRef.child("users/" + foundUser + "/profile.txt");
//            getKenaProfileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//
//                    try {
//                        kenaUserObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
//                        Log.d(TAG, "Gsonfrom json success");
//                        for (int i = 0; i < kenaUserObjList.size(); i++) {
//                            kenaParkerName = kenaUserObjList.get(i).getUserName().getFirstName() + kenaUserObjList.get(i).getUserName().getLastName();
//                            kenaCarModel = kenaUserObjList.get(i).getUserCar().getCarBrand() + kenaUserObjList.get(i).getUserCar().getCarModel();
//                            kenaCarPlateNumber = kenaUserObjList.get(i).getUserCar().getCarPlate();
//                            kenaCarColor = kenaUserObjList.get(i).getUserCar().getCarColour();
//                        }
//
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    Log.d(TAG, "fragment is not created, exception: " + exception);
//                }
//            }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
//                @Override
//                public void onComplete(@NonNull Task<byte[]> task) {
                    tvTitle.setText(((MarkerTag) marker.getTag()).getKenaParkerName());
                    tvSnippit.setText(((MarkerTag) marker.getTag()).getKenaParkerCarDetails());
//                }
//            });


//            if (((MarkerTag) marker.getTag()).getYesNoTag().equals("yes")) {
//                acceptButton.setVisibility(View.VISIBLE);
//
//                view.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        Log.d("LONG", "CLICKED long and fndUser is " + fndUser);
//
//                            foundUser = fndUser;
//                            Log.d(TAG, "onInfoWindowLongClick: entry foundUser is " + foundUser);
//
//
//                        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM2);
//                        togetherRef.child(foundUser).child("peter").setValue(MapsMainActivity.currentUserID);
//                        Intent intent = new Intent(view.getContext(),PeterMap.class);
//                        view.getContext().startActivity(intent);
//                        return true;
//                    }
//                });
//
//            }
//            else if (((MarkerTag) marker.getTag()).getYesNoTag().equals("no")) {
//                acceptButton.setVisibility(View.INVISIBLE);
//                view.setClickable(false);
//                view.setLongClickable(false);
//                view.setOnClickListener(null);
//                view.setOnLongClickListener(null);
//                tvTitle.setText(null);
//
//                tvUserIDView.setText(null);
//            }

        } catch (NullPointerException e){
            Log.d(TAG,"Error " + e);
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendorWindowText(marker, mWindow,foundUser);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendorWindowText(marker, mWindow,foundUser);
        return mWindow;
    }
}
