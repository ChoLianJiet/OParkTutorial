package com.opark.opark;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
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
import android.widget.Button;
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
import com.opark.opark.dialogs.SorryKenaHasFFKedForPeter;
import com.opark.opark.feedback.FeedbackDialog;
import com.opark.opark.model.Car;
import com.opark.opark.model.User;
import com.opark.opark.share_parking.MapsMainActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

public class PeterMap extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnCameraMoveStartedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        LocationListener {

    final String TAG = "PeterMap";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    private static final int PLAY_SERVICES_REQUEST_CODE = 124;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;

    private float DEFAULT_TILT = 45f;
    private float DEFAULT_ZOOM = 17f;
    private float RECENTRE_ZOOM = 15f;
    final long ONE_MEGABYTE = 1024 * 1024;

    GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location peterParker = new Location("");
    private LatLng peterParkerLocation;

    Chronometer mChronometer;

    ArrayList<User> userObjList = new ArrayList<>();

    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView kenaParkerName;
    private String kenaCarBrand;
    private String kenaCarModel;
    private TextView kenaCarModelText;
    private TextView kenaCarPlateNumber;
    private TextView kenaCarColor;

    private FloatingActionButton recenterButton;
    private Button intentToGoogleMapButton;
    private Location mLastLocation = new Location("");
    private String currentUserId;
    double elapsedTime;
    DatabaseReference geofireRef;
    DatabaseReference matchmakingRef;
    DatabaseReference togetherRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    GeoFire geoFire;
    GeoQuery geoQuery;
    final static double[] fixedGeoFireLatitude = new double[1];
    final static double[] fixedGeoFireLongitude = new double[1];
    private String foundUser;
    double currentUserLatitude;
    double currentUserLongitude;
    double foundUserLatitude;
    double foundUserLongitude;
    private LatLng fixLocation;
    private LatLng foundUserLocation;
    private LatLng currentUserLocation;
    Marker kenaMarker;
    public static double pointsGainedFromPeterMap;
    User user = new User();


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
        getLocationPermission();

        View bottomSheet = findViewById(R.id.bottom_sheet_peter);
        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();

        kenaParkerName = findViewById(R.id.kena_name);
        kenaCarColor = findViewById(R.id.kena_car_color);
        kenaCarModelText = findViewById(R.id.kena_car_modal);
        kenaCarPlateNumber = findViewById(R.id.kena_car_plate_number);


        intentToGoogleMapButton = (Button) findViewById(R.id.intent_to_google_map);

        recenterButton = (FloatingActionButton) findViewById(R.id.recenter_button);
        recenterButton.setVisibility(View.INVISIBLE);

        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        geoFire = new GeoFire(geofireRef);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getFoundUserLocation();
        setKenaDetailsOnBottomSheet();

        intentToGoogleMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double lat = kenaMarker.getPosition().latitude;

                double lng = kenaMarker.getPosition().longitude;
//
                String format = "google.navigation:q=" + lat + "," + lng;
//                String format = "waze://?ll="+ lat +"," + lng +"&navigate=yes";
                Uri uri = Uri.parse(format);


                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        recenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Recentered Button is Pressed");
                markerInMiddle = true;
                LatLng latlong = new LatLng(currentUserLatitude,currentUserLongitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,RECENTRE_ZOOM));
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
                        intentToGoogleMapButton.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
//                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        recenterButton.setVisibility(View.INVISIBLE);
                        intentToGoogleMapButton.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        recenterButton.setVisibility(View.INVISIBLE);
                        intentToGoogleMapButton.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        recenterButton.setVisibility(View.INVISIBLE);
                        intentToGoogleMapButton.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        detectKenaFFK();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
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

                        peterParker.setLatitude(Double.parseDouble(postSnapshot.getValue().toString()));
                        Log.d(TAG, "peter latitude is " + peterParker.getLatitude());
                    }

                    if (postSnapshot.getKey().equals("1")) {
                        peterParker.setLongitude(Double.parseDouble(postSnapshot.getValue().toString()));
                        Log.d(TAG, "peter longitude is " + peterParker.getLongitude());
                    }
                }

                peterParkerLocation = new LatLng(peterParker.getLatitude(), peterParker.getLongitude());
                //Marker for peterParker
                addMarker(mMap,peterParkerLocation.latitude,peterParkerLocation.longitude);
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

    private void getFoundUserLocation(){
        foundUser = MapsMainActivity.foundUser;
        Log.d(TAG, "getFoundUserLocation: foundUser after long click in PeterMap is " + foundUser);
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

                    Log.d(TAG, "fixed location is " + fixedGeoFireLatitude[0] + "," + fixedGeoFireLongitude[0]);
                    fixedGeoFireLatitude[0] =foundUserLatitude;
                    fixedGeoFireLongitude[0] = foundUserLongitude;
                    fixLocation = new LatLng(fixedGeoFireLatitude[0],fixedGeoFireLongitude[0]);

                }

                setKenaMarker(fixLocation);
                detectCurrentUserWentIntoSmallGeoFire();
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

    private void setKenaDetailsOnBottomSheet(){

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
                    kenaCarBrand = userObjList.get(0).getUserCar().getCarBrand();
                    kenaCarModel = userObjList.get(0).getUserCar().getCarModel();
                    kenaCarModelText.setText(kenaCarBrand + kenaCarModel);
                    kenaCarPlateNumber.setText(userObjList.get(0).getUserCar().getCarPlate());
                    kenaCarColor.setText(userObjList.get(0).getUserCar().getCarColour());


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

    private void setKenaMarker(LatLng thisLocation){

        kenaMarker = mMap.addMarker(new MarkerOptions()
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
            startLocationUpdates();
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

    @Override
    public void onInfoWindowClick(Marker marker) {

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

    private void detectKenaFFK(){
        togetherRef.child(foundUser).child("FFKed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot subscriptionDataSnapshot : dataSnapshot.getChildren()) {

                    if (subscriptionDataSnapshot.getKey().equals("yes")) {
                        FFKedEndSession();
                    } else {
                        // do nothing
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void detectCurrentUserWentIntoSmallGeoFire(){

        geoQuery = geoFire.queryAtLocation(new GeoLocation(fixedGeoFireLatitude[0], fixedGeoFireLongitude[0]),0.01);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (key.equals(currentUserId)){
//                    Intent intent = new Intent(getApplicationContext(), PeterMap.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
//                    //  the activity from a service
//                    intent.setAction(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    startActivity(intent);
                    EndSession();
                    Log.d(TAG,"I have entered the area");
                } else {
                    //do nothing
                }
            }

            @Override
            public void onKeyExited(String key) {

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

    private void EndSession(){
        Log.d(TAG,"EndSession is called");
        MapsMainActivity.foundUser = null;
        mChronometer.stop();
        user.userName.firstName = kenaParkerName.toString();
        user.userCar = new Car(kenaCarColor.getText().toString(),kenaCarBrand,kenaCarModel,kenaCarPlateNumber.getText().toString());
        CalculatePoints();
        /*** Intent to Feedback***/
        FeedbackDialog dialog = new FeedbackDialog();
        dialog.setArguments(dialog.setCarDetails(user.userCar));
        dialog.show(getSupportFragmentManager(), "");

        final StorageReference userRewardsFolder = storageRef.child("users/" +currentUserId + "/points.txt");
        userRewardsFolder.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Double currentPoints = null;
                try {
                    currentPoints = new Gson().fromJson(new String(bytes, "UTF-8"), Double.class);
                    double totalPoints = currentPoints + KenaMap.pointsGainedFromKenaMap + PeterMap.pointsGainedFromPeterMap;
                    objToByteStreamUpload(totalPoints,userRewardsFolder);
                    DatabaseReference userPointsDaRef = FirebaseDatabase.getInstance().getReference().child("users/userPoints/"+currentUserId);
                    userPointsDaRef.setValue(totalPoints);
                    Toast.makeText(getApplicationContext(), "Total points: " + totalPoints, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
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

    private void CalculatePoints(){
        int r = new Random().nextInt(10);
        elapsedTime = (SystemClock.elapsedRealtime()-mChronometer.getBase())/1000;
        Log.d(TAG,"Elapsed time is " + elapsedTime + " seconds");
        pointsGainedFromPeterMap = (0.1 * elapsedTime) + r;
        pointsGainedFromPeterMap = Math.ceil(pointsGainedFromPeterMap);
        Log.d(TAG,"Points gained by Kena is " + pointsGainedFromPeterMap + ", the random r generated is " + r);
    }

    private void FFKedEndSession(){
        SorryKenaHasFFKedForPeter sorryFFKedDialog = new SorryKenaHasFFKedForPeter();
        sorryFFKedDialog.show(getSupportFragmentManager(),"FFKed Alert Dialog");
        togetherRef.child(foundUser).child("FFKed").removeValue();
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
