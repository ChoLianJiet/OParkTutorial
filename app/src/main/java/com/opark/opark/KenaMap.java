package com.opark.opark;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;

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
import com.opark.opark.share_parking.MapsMainActivity;

import java.text.DecimalFormat;

public class KenaMap extends FragmentActivity implements OnMapReadyCallback {

    final String TAG = "KenaMap";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private float DEFAULT_ZOOM = 18f;
    private float DEFAULT_TILT = 45f;

    GoogleMap mMap;
    Chronometer mChronometer;
    private Location mLastLocation = new Location("");
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


        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getCurrentUserLocation();
        getFoundUserLocation();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

                    currentUserLocation = new LatLng(currentUserLatitude,currentUserLongitude);

                    setOwnMarker(currentUserLocation);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getFoundUserLocation(){
        togetherRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
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

                            addMarker(mMap,foundUserLocation.latitude,foundUserLocation.longitude);

                        }
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

        kenaMarker = mMap.addMarker(new MarkerOptions()
                .position(thisLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(thisLocation,17f,DEFAULT_TILT,0)));
    }


    int markerCount = 0;
    private Marker mk = null;
    // Add A Map Pointer To The Map
    public void addMarker(GoogleMap googleMap, double lat, double lon) {

        LatLng latlong = new LatLng(lat, lon);

        mLastLocation.setLatitude(foundUserLocation.latitude);
        mLastLocation.setLongitude(foundUserLocation.longitude);

        if(markerCount==1){
            animateMarker(mLastLocation,mk);
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong));
        }

        else if (markerCount==0){
            //Set Custom BitMap for Pointer
            int height = 80;
            int width = 45;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.icon_car);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            mMap = googleMap;


            mk= mMap.addMarker(new MarkerOptions().position(latlong)
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin3))
                    .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlong,DEFAULT_ZOOM,DEFAULT_TILT,0)));
            //Set Marker Count to 1 after first marker is created
            markerCount=1;

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
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

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
