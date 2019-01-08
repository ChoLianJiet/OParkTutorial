package com.opark.opark;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.share_parking.MapsMainActivity;

import java.util.Map;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    String TAG = "CustomInfoWindowAdapter";

    private final View mWindow;
    private Context mContext;
    private Button acceptButton;
    private String foundUser;
    public static TextView tvUserIDView;
    private DatabaseReference matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
    private DatabaseReference togetherRef = FirebaseDatabase.getInstance().getReference().child("together");

    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);
    }

    private void rendorWindowText(final Marker marker, final View view, final String fndUser){

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        acceptButton = (Button) view.findViewById(R.id.info_window_accept);
        tvUserIDView = (TextView) view.findViewById(R.id.user_id_view);
        try {
        tvUserIDView.setText(((MarkerTag) marker.getTag()).getUID());


            if (((MarkerTag) marker.getTag()).getYesNoTag().equals("yes")) {
                acceptButton.setVisibility(View.VISIBLE);

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d("LONG", "CLICKED long and fndUser is " + fndUser);

                            foundUser = fndUser;
                            Log.d(TAG, "onInfoWindowLongClick: entry foundUser is " + foundUser);


                        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM2);
                        togetherRef.child(foundUser).child("peter").setValue(MapsMainActivity.currentUserID);
                        Intent intent = new Intent(view.getContext(),PeterMap.class);
                        view.getContext().startActivity(intent);
                        return true;
                    }
                });

            } else if (((MarkerTag) marker.getTag()).getYesNoTag().equals("no")) {
                acceptButton.setVisibility(View.INVISIBLE);
                view.setClickable(false);
                view.setLongClickable(false);
                view.setOnClickListener(null);
            } else if (((MarkerTag) marker.getTag()).getYesNoTag().equals("own")) {
//                MapsMainActivity.mMap.setInfoWindowAdapter(null);
            }

            if (!title.equals("")) {
                tvTitle.setText(title);
            } else {

            }

            String snippit = marker.getSnippet();
            TextView tvSnippit = (TextView) view.findViewById(R.id.snippit);

            if (!snippit.equals("")) {
                tvSnippit.setText(snippit);
            } else {

            }
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
