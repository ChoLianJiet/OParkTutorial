package com.opark.opark;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.dialogs.PromptDetectedFFKDialogForKena;
import com.opark.opark.dialogs.PromptEndSessionDialog;
import com.opark.opark.dialogs.PromptKenaDialog;
import com.opark.opark.model.Car;
import com.opark.opark.model.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

public class KenaMap extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnCameraMoveStartedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        LocationListener {

    final String TAG = "KenaMap";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    private static final int PLAY_SERVICES_REQUEST_CODE = 124;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;

    private float DEFAULT_ZOOM = 18f;
    private float DEFAULT_TILT = 45f;
    final long ONE_MEGABYTE = 1024 * 1024;


    ArrayList<User> userObjList = new ArrayList<>();

    GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LatLng kenaParkerLocation;
    private Location kenaParker = new Location("");
    Chronometer mChronometer;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView peterParkerName;
    private String peterCarBrand;
    private String peterCarModel;
    private TextView peterCarModelText;
    private TextView peterCarPlateNumber;
    private TextView peterCarColor;
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
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    public static double pointsGainedFromKenaMap;
    double[] fixedGeoFireLatitude = new double[1];
    double[] fixedGeoFireLongitude = new double[1];
    FirebaseStorage storage;
    StorageReference storageRef;
    User user = new User();
    private boolean peterIsInside = false;



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

        getLocationPermission();

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();

        View bottomSheet = findViewById(R.id.bottom_sheet_kena);
        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);

        recenterButton = (FloatingActionButton) findViewById(R.id.recenter_button);
        recenterButton.setVisibility(View.INVISIBLE);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");
        geoFire = new GeoFire(geofireRef);

        peterParkerName = findViewById(R.id.kena_name);
        peterCarModelText = findViewById(R.id.kena_car_modal);
        peterCarPlateNumber = findViewById(R.id.kena_car_plate_number);
        peterCarColor = findViewById(R.id.kena_car_color);

        setPeterDetailsOnBottomSheet();

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

        getCurrentUserForFixLatLng();

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
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraMoveStartedListener(this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission");
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            FINE_LOCATION}, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  ){
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            currentUserLatitude = mLastLocation.getLatitude();
            currentUserLongitude = mLastLocation.getLongitude();

            //Update to firebase
            geoFire.setLocation(currentUserId, new GeoLocation(currentUserLatitude, currentUserLongitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully as lat[" + currentUserLatitude + "], lon[" + currentUserLongitude + "]!");
                        loadLocationForThisUser();
                        CheckCurrentUserHaveGoneOutOfGeoQueryArea();
                    }
                }
            });
        } else {
            Log.d("Test", "Couldn't load location");
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_REQUEST_CODE).show();
            } else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void loadLocationForThisUser() {

        geofireRef.child(currentUserId).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals("0")) {

                        kenaParker.setLatitude(Double.parseDouble(postSnapshot.getValue().toString()));
                    }

                    if (postSnapshot.getKey().equals("1")) {
                        kenaParker.setLongitude(Double.parseDouble(postSnapshot.getValue().toString()));
                    }
                }

                kenaParkerLocation = new LatLng(kenaParker.getLatitude(), kenaParker.getLongitude());
                //Marker for peterParker
                setOwnMarker(kenaParkerLocation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void setPeterDetailsOnBottomSheet(){

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

                    peterParkerName.setText(userObjList.get(0).getUserName().getFirstName() + userObjList.get(0).getUserName().getLastName());
                    peterCarBrand = userObjList.get(0).getUserCar().getCarBrand();
                    peterCarModel = userObjList.get(0).getUserCar().getCarModel();
                    peterCarModelText.setText(peterCarBrand + peterCarModel);
                    peterCarPlateNumber.setText(userObjList.get(0).getUserCar().getCarPlate());
                    peterCarColor.setText(userObjList.get(0).getUserCar().getCarColour());


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

    private void getCurrentUserForFixLatLng(){
        geofireRef.child(currentUserId).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot subscriptionDataSnapshot : dataSnapshot.getChildren()) {

                    if (subscriptionDataSnapshot.getKey().equals("0")) {
                        fixedGeoFireLatitude[0] = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                        System.out.print("currentUserLatitude is: " + currentUserLatitude);
                    }

                    if (subscriptionDataSnapshot.getKey().equals("1")) {
                        fixedGeoFireLongitude[0] = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                        System.out.print("currentUserLongitude is: " + currentUserLongitude);
                    }

                }

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

    @Override
    public void onInfoWindowClick(Marker marker) {

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

        System.out.println("current user is " + currentUserLatitude + "," + currentUserLongitude);
        Log.d(TAG,"fixed location for geofire is " + fixedGeoFireLatitude[0] + "," + fixedGeoFireLongitude[0]);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(fixedGeoFireLatitude[0],fixedGeoFireLongitude[0]), 0.02);
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

        System.out.println("current user is " + currentUserLatitude + "," + currentUserLongitude);
        Log.d(TAG,"fixed location for geofire is " + fixedGeoFireLatitude[0] + "," + fixedGeoFireLongitude[0]);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(fixedGeoFireLatitude[0], fixedGeoFireLongitude[0]), 0.02);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if ( key.equals(foundUser)){
                    peterIsInside = true;
                    Log.d(TAG,"peterIsInside is " + peterIsInside);
                } else {
                    //do nothing
                }
            }

            @Override
            public void onKeyExited(String key) {
                if ( key.equals(foundUser)){
                    Log.d(TAG,foundUser + " has exited the area");
                    peterIsInside = false;
                } else {
                    //do nothing
                }
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

    private void CheckCurrentUserHaveGoneOutOfGeoQueryArea(){

        System.out.println("current user is " + currentUserLatitude + "," + currentUserLongitude);
        Log.d(TAG,"fixed location for geofire is (for current user out) " + fixedGeoFireLatitude[0] + "," + fixedGeoFireLongitude[0]);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(fixedGeoFireLatitude[0], fixedGeoFireLongitude[0]), 0.05);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
            }

            @Override
            public void onKeyExited(String key) {
                if (key.equals(currentUserId) && peterIsInside){
                    Log.d(TAG,"I have exited and ready to end session");
                    Intent intent = new Intent(getApplicationContext(), KenaMap.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
                    //  the activity from a service
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(intent);
                    EndSession();
                } else if (key.equals(currentUserId) && !peterIsInside) {
                    Log.d(TAG,"I have ffked and ready to end session");
                    detectedFFK();
                } else {
                    Log.d(TAG,"It is else, and peterIsInside is " + peterIsInside );

                }
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
        Log.d(TAG,"EndSession is called");
        mChronometer.stop();
        user.userName.firstName = peterParkerName.toString();
        user.userCar = new Car(peterCarColor.getText().toString(),peterCarBrand,peterCarModel,peterCarPlateNumber.getText().toString());
        Log.d(TAG,"PromptPeterNearbyMini() is called");
        PromptEndSessionDialog promptEndSessionDialog = new PromptEndSessionDialog();
        promptEndSessionDialog.show(getSupportFragmentManager(),"prompt dialog");

        StorageReference flagStorageLocation = storageRef.child("users/" + currentUserId + "/gotflag.txt");
        objToByteStreamUpload(user,flagStorageLocation);
        CalculatePoints();
        /*** Intent to Feedback***/
    }

    private void CalculatePoints(){
        int r = new Random().nextInt(10);
        elapsedTime = (SystemClock.elapsedRealtime()-mChronometer.getBase())/1000;
        Log.d(TAG,"Elapsed time is " + elapsedTime + " seconds");
        pointsGainedFromKenaMap = (2 * elapsedTime) + r;
        Log.d(TAG,"Points gained by Kena is " + pointsGainedFromKenaMap + ", the random r generated is " + r);
    }

    private void objToByteStreamUpload(Object obj, StorageReference destination){

        String objStr = new Gson().toJson(obj);
        InputStream in = new ByteArrayInputStream(objStr.getBytes(Charset.forName("UTF-8")));
        UploadTask uploadTask = destination.putStream(in);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                objToByteStreamUpload(user,storageRef.child("users/" + currentUserId + "/gotflag.txt"));


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Profile update successful!", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Flag Set");
                // Use analytics to calculate the success rate
            }
        });
    }

    private void detectedFFK(){

        togetherRef.child(currentUserId).child("FFKed").setValue("yes");

        PromptDetectedFFKDialogForKena promptFFKDialog = new PromptDetectedFFKDialogForKena();
        promptFFKDialog.show(getSupportFragmentManager(),"FFK Alert Dialog");

    }

    public void expandWhenBottomSheetIsClicked(View v){
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onBackPressed() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // do nothing
    }

    @Override
    protected void onDestroy() {
        if (peterIsInside){
            user.userName.firstName = peterParkerName.toString();
            user.userCar = new Car(peterCarColor.getText().toString(),peterCarBrand,peterCarModel,peterCarPlateNumber.getText().toString());
            StorageReference flagStorageLocation = storageRef.child("users/" + currentUserId + "/gotflag.txt");
            objToByteStreamUpload(user,flagStorageLocation);
        }
        super.onDestroy();
    }
}
