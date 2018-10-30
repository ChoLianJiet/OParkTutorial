package com.opark.opark;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationServices;
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
import com.opark.opark.card_swipe.MainActivityCardSwipe;
import com.opark.opark.chat.ChatAdapter;
import com.opark.opark.chat.ChatMessage;
import com.opark.opark.share_parking.MapsMainActivity;

import java.text.DecimalFormat;
import java.util.Random;

public class KenaMap extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnCameraMoveStartedListener {

    final String TAG = "KenaMap";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private float DEFAULT_ZOOM = 18f;
    private float DEFAULT_TILT = 45f;





    GoogleMap mMap;
    Chronometer mChronometer;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView kenaParkerName;
    private TextView kenaCarModel;
    private TextView kenaCarPlateNumber;
    private TextView kenaCarColor;
    double elapsedTime;
    private Location mLastLocation = new Location("");
    private FloatingActionButton recenterButton;
    DatabaseReference geofireRef;
    DatabaseReference matchmakingRef;
    DatabaseReference togetherRef;
    String currentUserId;
    String foundUser;
    double currentUserLatitude;
    double currentUserLongitude;
    double foundUserLatitude;
    double foundUserLongitude;
    private LatLng foundUserLocation;
    private LatLng currentUserLocation;
    Marker kenaMarker;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    public static double pointsGainedFromKenaMap;

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
        setContentView(R.layout.kena_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be uPosed.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FirebaseApp.initializeApp(getApplicationContext());

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();

        View bottomSheet = findViewById(R.id.bottom_sheet_kena);
        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);

        recenterButton = (FloatingActionButton) findViewById(R.id.recenter_button);
        recenterButton.setVisibility(View.INVISIBLE);


        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");
        geoFire = new GeoFire(geofireRef);

        //Chat
        chatListView = (ListView) findViewById(R.id.list_view);
        chatEditText = (EditText) findViewById(R.id.input_text) ;
        fab = (FloatingActionButton) findViewById(R.id.fab) ;

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = chatEditText.getText().toString();
                if(!text.equals("")){
                    ChatMessage chat = new ChatMessage(text,FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    FirebaseDatabase.getInstance().getReference().child("together").child(currentUserId).child("messages").push().setValue(chat);
                    chatEditText.setText("");
                }

            }
        });

        getCurrentUserLocation();

        getFoundUserLocation();

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        setRecenterButton();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
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
        mAdapter = new ChatAdapter(this,FirebaseDatabase.getInstance().getReference().child("together").child(currentUserId),UserPopUpFragment.kenaParkerName.getText().toString());
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
                        System.out.print("currentUserLatitude is: " + currentUserLatitude);
                    }

                    if (subscriptionDataSnapshot.getKey().equals("1")) {
                        currentUserLongitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                        System.out.print("currentUserLongitude is: " + currentUserLongitude);
                    }

                }
                currentUserLocation = new LatLng(currentUserLatitude,currentUserLongitude);
                setOwnMarker(currentUserLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE){
            markerInMiddle = false;
            Log.d(TAG,"markerInMiddle is " + markerInMiddle);
            setRecenterButton();
        }
    }

    private void getFoundUserLocation(){
        togetherRef.child(currentUserId).child("peter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foundUser = dataSnapshot.getValue().toString();
                Log.d(TAG,"foundUser in KenaMap is: " + foundUser);

                geofireRef.child(foundUser).child("l").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot subscriptionDataSnapshot : dataSnapshot.getChildren()) {

                            Log.d(TAG,"subscriptionDataSnapshot is: " + dataSnapshot);

                            if (subscriptionDataSnapshot.getKey().equals("0")) {
                                foundUserLatitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                                Log.d(TAG,"foundUserLatitude is: " + foundUserLatitude);
                            }

                            if (subscriptionDataSnapshot.getKey().equals("1")) {
                                foundUserLongitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                                Log.d(TAG,"foundUserLongitude is: " + foundUserLongitude);
                            }

                            foundUserLocation = new LatLng(foundUserLatitude,foundUserLongitude);
                            Log.d(TAG,"foundUserLocation is: " + foundUserLocation);

                        }
                        addMarker(mMap,foundUserLocation.latitude,foundUserLocation.longitude);
                        CheckFoundUserHaveEnteredGeoQueryArea();
//                        CheckFoundUserHaveEnteredMiniGeoQueryArea();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    private void setOwnMarker(LatLng thisLocation){

        mk = mMap.addMarker(new MarkerOptions().position(thisLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(thisLocation, DEFAULT_ZOOM, DEFAULT_TILT, 0)));
        Log.d(TAG,"camera has been moved to middle");
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


        LatLng latlong = new LatLng(lat, lon);

        mLastLocation.setLatitude(foundUserLocation.latitude);
        mLastLocation.setLongitude(foundUserLocation.longitude);

        if(markerCount==1 && markerInMiddle == true){
            animateMarker(mLastLocation,mk);
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong));
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

            final KenaMap.LatLngInterpolator latLngInterpolator = new KenaMap.LatLngInterpolator.LinearFixed();
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

    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements KenaMap.LatLngInterpolator {
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

    private void CheckFoundUserHaveEnteredGeoQueryArea(){
        final double fixedGeoFireLatitude = currentUserLatitude;
        final double fixedGeoFireLongitude = currentUserLongitude;
        System.out.println("current user is " + currentUserLatitude + "," + currentUserLongitude);
        Log.d(TAG,"fixed location for geofire is " + fixedGeoFireLatitude + "," + fixedGeoFireLongitude);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(currentUserLatitude, currentUserLongitude), 0.02);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if ( key.equals(foundUser)){
                    PromptPeterNearby();
                    Log.d(TAG,foundUser + " has entered the area");
                } else {
                    //do nothing
                }
            }

            @Override
            public void onKeyExited(String key) {
                Log.d(TAG,foundUser + " has exited the area");
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        CheckFoundUserHaveEnteredMiniGeoQueryArea();
    }

    private void CheckFoundUserHaveEnteredMiniGeoQueryArea(){
        final double fixedGeoFireLatitude = currentUserLatitude;
        final double fixedGeoFireLongitude =currentUserLongitude;
        System.out.println("current user is " + currentUserLatitude + "," + currentUserLongitude);
        Log.d(TAG,"fixed location for geofire is " + fixedGeoFireLatitude + "," + fixedGeoFireLongitude);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(currentUserLatitude, currentUserLongitude), 0.005);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if ( key.equals(foundUser)){
                    EndSession();
                    Log.d(TAG,foundUser + " has entered the area");
                } else {
                    //do nothing
                }
            }

            @Override
            public void onKeyExited(String key) {
                Log.d(TAG,foundUser + " has exited the area");
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void PromptPeterNearby(){
        Log.d(TAG,"PromptPeterNearby() is called");
        PromptKenaDialog promptKenaDialog = new PromptKenaDialog();
        promptKenaDialog.show(getSupportFragmentManager(),"prompt dialog");
    }

    private void EndSession(){
        mChronometer.stop();
        Log.d(TAG,"PromptPeterNearbyMini() is called");
        PromptEndSessionDialog promptEndSessionDialog = new PromptEndSessionDialog();
        promptEndSessionDialog.show(getSupportFragmentManager(),"prompt dialog");
        CalculatePoints();
    }

    private void CalculatePoints(){
        int r = new Random().nextInt(10);
        elapsedTime = (SystemClock.elapsedRealtime()-mChronometer.getBase())/1000;
        Log.d(TAG,"Elapsed time is " + elapsedTime + " seconds");
        pointsGainedFromKenaMap = (2 * elapsedTime) + r;
        Log.d(TAG,"Points gained by Kena is " + pointsGainedFromKenaMap + ", the random r generated is " + r);
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
