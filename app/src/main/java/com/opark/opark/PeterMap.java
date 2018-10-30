package com.opark.opark;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opark.opark.chat.ChatAdapter;
import com.opark.opark.chat.ChatMessage;
import com.opark.opark.share_parking.MapsMainActivity;

public class PeterMap extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnCameraMoveStartedListener {

    final String TAG = "PeterMap";

    private float DEFAULT_TILT = 45f;
    private float DEFAULT_ZOOM = 17f;

    GoogleMap mMap;

    Chronometer mChronometer;

    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView kenaParkerName;
    private TextView kenaCarModel;
    private TextView kenaCarPlateNumber;
    private TextView kenaCarColor;

    private FloatingActionButton recenterButton;
    private Location mLastLocation = new Location("");
    private String currentUserId;
    DatabaseReference geofireRef;
    DatabaseReference matchmakingRef;
    DatabaseReference togetherRef;
    private String foundUser;
    double currentUserLatitude;
    double currentUserLongitude;
    double foundUserLatitude;
    double foundUserLongitude;
    private LatLng foundUserLocation;
    private LatLng currentUserLocation;
    Marker peterMarker;
    public static double pointsGainedFromPeterMap;

    //Chat
    private EditText chatEditText;
    private FloatingActionButton fab;
    private ChatAdapter mAdapter;
    private ListView chatListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.peter_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be uPosed.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FirebaseApp.initializeApp(getApplicationContext());

        View bottomSheet = findViewById(R.id.bottom_sheet_peter);
        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();

        kenaParkerName = findViewById(R.id.kena_name);
        kenaCarColor = findViewById(R.id.kena_car_color);
        kenaCarModel = findViewById(R.id.kena_car_modal);
        kenaCarPlateNumber = findViewById(R.id.kena_car_plate_number);


        this.kenaParkerName.setText(UserPopUpFragment.kenaParkerName.getText().toString());
        this.kenaCarModel.setText(UserPopUpFragment.carModel.getText().toString());
        this.kenaCarPlateNumber.setText(UserPopUpFragment.carPlateNumber.getText().toString());
        this.kenaCarColor.setText(UserPopUpFragment.carColor.getText().toString());

        //Chat
        chatListView = (ListView) findViewById(R.id.list_view);
        chatEditText = (EditText) findViewById(R.id.input_text) ;
        fab = (FloatingActionButton) findViewById(R.id.fab) ;

        recenterButton = (FloatingActionButton) findViewById(R.id.recenter_button);
        recenterButton.setVisibility(View.INVISIBLE);

        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getCurrentUserLocation();
        getFoundUserLocation();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = chatEditText.getText().toString();
                if(!text.equals("")){
                    ChatMessage chat = new ChatMessage(text,FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    FirebaseDatabase.getInstance().getReference().child("together").child(MapsMainActivity.foundUser).child("messages").push().setValue(chat);
                    chatEditText.setText("");
                }

            }
        });

        recenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Recentered Button is Pressed");
                markerInMiddle = true;
                LatLng latlong = new LatLng(currentUserLatitude,currentUserLongitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,DEFAULT_ZOOM));
                Log.d(TAG,"markerInMiddle is " + markerInMiddle);
                setRecenterButton();
            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        setRecenterButton();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
//                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        recenterButton.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        recenterButton.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        recenterButton.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        chatListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraMoveStartedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new ChatAdapter(this,FirebaseDatabase.getInstance().getReference().child("together").child(foundUser),UserPopUpFragment.kenaParkerName.getText().toString());
        chatListView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.cleanup();
    }

    private void getCurrentUserLocation(){
        geofireRef.child(currentUserId).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot subscriptionDataSnapshot : dataSnapshot.getChildren()) {

                    if (subscriptionDataSnapshot.getKey().equals("0")) {
                        currentUserLatitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                        System.out.println("currentUserLatitude is: " + currentUserLatitude);
                    }

                    if (subscriptionDataSnapshot.getKey().equals("1")) {
                        currentUserLongitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                        System.out.println("currentUserLongitude is: " + currentUserLongitude);
                    }


                }
                currentUserLocation = new LatLng(currentUserLatitude,currentUserLongitude);
                addMarker(mMap,currentUserLatitude,currentUserLongitude);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getFoundUserLocation(){
        foundUser = MapsMainActivity.foundUser;
        geofireRef.child(foundUser).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot subscriptionDataSnapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "subscriptionDataSnapshot is: " + dataSnapshot);

                    if (subscriptionDataSnapshot.getKey().equals("0")) {
                        foundUserLatitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                        Log.d(TAG, "foundUserLatitude is: " + foundUserLatitude);
                    }

                    if (subscriptionDataSnapshot.getKey().equals("1")) {
                        foundUserLongitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                        Log.d(TAG, "foundUserLongitude is: " + foundUserLongitude);
                    }

                    foundUserLocation = new LatLng(foundUserLatitude, foundUserLongitude);
                    Log.d(TAG, "foundUserLocation is: " + foundUserLocation);

                }

                setKenaMarker(foundUserLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
    private void setKenaMarker(LatLng thisLocation){

        peterMarker = mMap.addMarker(new MarkerOptions()
                .position(thisLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(thisLocation,17f,DEFAULT_TILT,0)));
    }

    private void setRecenterButton(){
        if(markerInMiddle == true){
            recenterButton.setVisibility(View.INVISIBLE);
        } else {
            recenterButton.setVisibility(View.VISIBLE);
        }
    }


    int markerCount = 0;
    private Marker mk = null;
    boolean markerInMiddle = false;
    // Add A Map Pointer To The Map
    public void addMarker(GoogleMap googleMap, double lat, double lon) {
        Log.d(TAG,"markerCount is " + markerCount);
        LatLng latlong = new LatLng(lat, lon);

        mLastLocation.setLatitude(currentUserLocation.latitude);
        mLastLocation.setLongitude(currentUserLocation.longitude);

        if(markerCount==1 && markerInMiddle == true){
            animateMarker(mLastLocation,mk);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong));
        } else if (markerCount == 1 && markerInMiddle == false) {
            animateMarker(mLastLocation, mk);
        } else if (markerCount==0){
            //Set Custom BitMap for Pointer
            int height = 80;
            int width = 45;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.icon_car);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            mMap = googleMap;

            mk = mMap.addMarker(new MarkerOptions().position(latlong)
                    .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlong, DEFAULT_ZOOM, DEFAULT_TILT, 0)));
            //Set Marker Count to 1 after first marker is created
            markerCount = 1;
            markerInMiddle = true;
            Log.d(TAG,"markerCount is " + markerCount);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            setRecenterButton();
        }
    }

    public static void animateMarker(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(),destination.getLongitude());

            final float startRotation = marker.getRotation();

            final PeterMap.LatLngInterpolator latLngInterpolator = new PeterMap.LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();

        }
    }

    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE){
            markerInMiddle = false;
            Log.d(TAG,"markerInMiddle is " + markerInMiddle);
            setRecenterButton();
        }
    }

    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements PeterMap.LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    public void expandWhenBottomSheetIsClicked(View v){
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onBackPressed() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // do nothing
    }

}
