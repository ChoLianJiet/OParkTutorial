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
    private TextView tvUserIDView;
    private TextView tvTitle;
    private TextView tvSnippit;
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
        tvTitle = (TextView) view.findViewById(R.id.title_id_view);
        tvSnippit = (TextView) view.findViewById(R.id.snippit_id_view);
        acceptButton = (Button) view.findViewById(R.id.info_window_accept);
        tvUserIDView = (TextView) view.findViewById(R.id.user_id_view);
        try {

            tvUserIDView.setText(((MarkerTag) marker.getTag()).getUID());
            tvTitle.setText(((MarkerTag) marker.getTag()).getKenaParkerName());
            tvSnippit.setText(((MarkerTag) marker.getTag()).getKenaParkerCarDetails());

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
