package com.opark.opark.share_parking;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.LoginActivity;
import com.opark.opark.NoUserPopUp;
import com.opark.opark.R;
import com.opark.opark.UserPopUp;
import com.opark.opark.UserProfileSetup;
import com.opark.opark.model.User;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

import static com.opark.opark.UserPopUp.userDeclined;

public class MapsMainActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        LocationListener {

    //CONSTANT
    private static final String TAG = "MapsMainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    private static final int PLAY_SERVICES_REQUEST_CODE = 124;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;
    private float DEFAULT_ZOOM = 18f;
    //    long MIN_TIME = 5000;
//    long MIN_DISTANCE = 1000;
//    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    String ADATEM0 = "0";
    String ADATEM1 = "1";
    public static String USER_ID_PREFS;
    public static String USER_ID_KEY;

    //MEMBER VARIALBLE
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap mMap;
    private Button shareParkingButton;
    private Button findParkingButton;
    private Button signOutButton;
    private Button recenterButton;
    LocationManager mLocationManager;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private double longitude;
    private double latitude;
    private DatabaseReference matchmakingRef;
    private DatabaseReference geofireRef;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    ArrayList<User> userObjList = new ArrayList<>();
    TextView kenaParkerName;
    TextView carModel;
    TextView carPlateNumber;
    TextView carColor;
    private String currentUserID;
    private Query adatemQueryList;
    public static ArrayList<String> newArrayList = new ArrayList<>();
    public static ArrayList<String> oldArrayList = new ArrayList<>();
    public static HashSet<String> newHashSet = new HashSet<>();
    public static HashSet<String> oldHashSet = new HashSet<>();
    private Location peterParker;
    private int i ;
    public static String foundUser;
    private ProgressBar loadingCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be uPosed.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationPermission();

       bindViews();


        loadLocationForThisUser();
//        getAndSetCurrentLocation();

        shareParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMatchkingFolderInDatabse();
            }
        });

        findParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOtherUsersLocation();
            }
        });

        recenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Recentered Button is Pressed");
                LatLng latlong = new LatLng(latitude,longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,DEFAULT_ZOOM));
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Map is ready",Toast.LENGTH_SHORT).show();
        mMap = googleMap;
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission");
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            FINE_LOCATION,COARSE_LOCATION},MY_PERMISSION_REQUEST_CODE);
        } else {
            if(checkPlayServices()){
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(checkPlayServices()){
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
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            //Update to firebase
            geoFire.setLocation(currentUserID, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully as lat[" + latitude + "], lon[" + longitude + "]!");
                        shareParkingButton.setVisibility(View.VISIBLE);
                        findParkingButton.setVisibility(View.VISIBLE);
                        recenterButton.setVisibility(View.VISIBLE);
                        loadingCircle.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
//
            Log.d("Test","Couldn't load location");
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
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_REQUEST_CODE).show();
            } else {
                Toast.makeText(this,"This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (com.google.android.gms.location.LocationListener) this);
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

    /**OnStart**/
    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }


    /**OnStop**/
    @Override
    protected void onStop() {
        if(mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    /**OnResume**/
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**OnDestroy**/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        geofireRef.child(currentUserID).removeValue();
    }

    //Retrieve location for peterParker
    private void loadLocationForThisUser(){
        geofireRef.child(currentUserID).child("l").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    peterParker = new Location("");
                    peterParker.setLatitude(latitude);
                    peterParker.setLongitude(longitude);
                }

                LatLng peterParkerLocation = new LatLng(latitude,longitude);
                //Marker for peterParker
                addMarker(mMap,latitude,longitude);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Share Parking Pressed will set adatem and sessionKey in database
    private void setMatchkingFolderInDatabse(){

        String key = matchmakingRef.child(currentUserID+"/SessionKey").push().getKey();
        Matchmaking bothAdatemAndKey = new Matchmaking(ADATEM0,key);
        matchmakingRef.child(currentUserID).setValue(bothAdatemAndKey);
        Log.d("OPark", "Session key is:" + key);
    }

    //GET CURRENT LOCATION
//    private void getAndSetCurrentLocation() {
//
//        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        mLocationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                Log.d("OPark", "onLocationChanged() callback received");
//
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
//
//                Log.d("OPark","longitude is: " + longitude);
//                Log.d("OPark","latitude is " + latitude);
//
//                geoFire.setLocation(currentUserID, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
//                    @Override
//                    public void onComplete(String key, DatabaseError error) {
//                        if (error != null) {
//                            System.err.println("There was an error saving the location to GeoFire: " + error);
//                        } else {
//                            System.out.println("Location saved on server successfully as lat[" + latitude + "], lon[" + longitude + "]!");
//                            shareParkingButton.setVisibility(View.VISIBLE);
//                            findParkingButton.setVisibility(View.VISIBLE);
//                            loadingCircle.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//                Log.d("OPark", "onProviderDisabled() callback received");
//            }
//        };
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_CODE);
//
//            return;
//        }
//        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
//    }

    //FIND OTHER USERS LOCATION
    private void findOtherUsersLocation() {
        loadingCircle.setVisibility(View.VISIBLE);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 1.0);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d("OPark","\n\nat the beginning , \n\n  oldArrayList " + oldArrayList);
                Log.d("OPark","\n\nat the beginning , \n\n   newArrayList " + newArrayList);


                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                //kenaParker's location
                double foundLatitude = location.latitude;
                double foundLongitude = location.longitude;
                Location kenaParker = new Location("");
                kenaParker.setLatitude(foundLatitude);
                kenaParker.setLongitude(foundLongitude);
                LatLng kenaParkerLocation = new LatLng(foundLatitude, foundLongitude);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kenaParkerLocation,DEFAULT_ZOOM));

                //Function calculates distance between peterParker and kenaParker
                distance(peterParker,kenaParker);

                if (currentUserID.equals(key)) {

                    Log.d("SelfDetection", "this is myself " + (currentUserID));
                    newArrayList.remove(key);
                    oldArrayList.remove(key);
                    Log.d("SelfDetection", " After removed self from new & old array list " + " \n\nnew array List = " + newArrayList + "\nold Array List = " + oldArrayList + "\n\n");
                }
                //Marker for kenaParker
                if(!key.equals(currentUserID)){

                    //Add kenaParker Marker
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(kenaParkerLocation)
                            .title("KenaParker")
                            .snippet("Distance " + new DecimalFormat("#.#").format(distance(peterParker, kenaParker) / 1000) + "m")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                } else {

                }

//                 Hash removes duplicates (reorganising array list)
                oldHashSet.addAll(oldArrayList);
                oldArrayList.clear();
                oldArrayList.addAll(oldHashSet);
                Log.d("OPark", "oldArrayList after first reorganisation = " +(oldArrayList));


//                Log.d("oldArrayList", "oldArrayList consists" + oldArrayList);
                //the user shared parking//

                /** old Array List = Declined user
                   new Array List = Just entered
                 **/


                if (oldArrayList.contains(key)) {
                    newArrayList.remove(key);
                    newHashSet.remove(key);

                } else if (currentUserID.equals(key)) {

                    // If old arraylist contain self then remove self from new arraylist and new hashlist

                    Log.d("SelfDetection", "this is myself "+ (currentUserID));
                    newArrayList.remove(key);
                    newHashSet.remove(key);
                    oldArrayList.remove(key);
                    oldHashSet.remove(key);
                    Log.d("SelfDetection", "removed self from new & old array & hash list");

                }   else {if (!currentUserID.equals(key) && !oldArrayList.contains(key)) {

                    Log.d("OPark", "First detection and adding to newArrayList" + (key));
                    newArrayList.add(key);
                    newHashSet.addAll(newArrayList);
                    newArrayList.clear();
                    newArrayList.addAll(newHashSet);
                    Log.d("OPark", "Added to newArraylist at line 513 = " + newArrayList);}

                }

                //TODO Double check logic  --  0
                //At this point newArrayList already has the right amount of users
                for (i = 0; i < newArrayList.size(); i++) {
                    Log.d("OPark", "size of array is " + newArrayList.size());
                    Log.d("OPark","I am in Loop " + i);
                    foundUser = newArrayList.get(i);
                    Log.d("OPark", "foundUser is " + (foundUser));


                }

                if (newArrayList.isEmpty()){

                    newArrayList.addAll(oldArrayList);
                    newHashSet.addAll(newArrayList);
                    newArrayList.clear();
                    newArrayList.addAll(newHashSet);
                    oldArrayList.clear();

                    Log.d("OPark", "if new arraylist is empty \nadd old array list to new, now old is " + oldArrayList);
//                } else {
//                    Log.d("OPark","newArrayList is not empty lalala");
                }

                if (foundUser == null) {
                    System.out.println("There are no foundUsers");
                    Intent intent2 = new Intent(MapsMainActivity.this, NoUserPopUp.class);
                    startActivity(intent2);
                    loadingCircle.setVisibility(View.INVISIBLE);
                } else {
                    adatemQueryList = matchmakingRef.child(foundUser);

                    adatemQueryList.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                Log.d("OPark", "\nnewArrayList is not empty. Keys are " + newArrayList);
                                Log.d("OPark", "\n\t\tadatem value is " + dataSnapshot);

                                String adatemValue = dataSnapshot.child("adatem").getValue().toString();
                                Log.d("OPark", foundUser + "'s adatemValue is " + adatemValue);

                                if ((newArrayList != null) && (adatemValue.equals(ADATEM0))) {

//                                    Log.d("NewArrayList", "\nnewArrayList is not empty. Keys are " + newArrayList);
//                                    Log.d("NewArrayList", "\n\t\tadatem value is " + dataSnapshot);
//
//                                    String adatemValue = dataSnapshot.child("adatem").getValue().toString();
//                                    Log.d("OPark", foundUser + "'s adatemValue is " + adatemValue);


//                                    if (adatemValue.equals(ADATEM0)) {
                                    //Setting adatem0 to 1
                                    System.out.println("Found Adatem 0!");
                                    Log.d("OPark", "foundUser is " + foundUser);
                                    matchmakingRef.child(foundUser).child("adatem").setValue("1");

                                    saveFoundUserId();

                                    loadingCircle.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(MapsMainActivity.this, UserPopUp.class);
                                    startActivity(intent);


                                    } else if (adatemValue.equals(ADATEM1)) {
                                        //TODO add code
                                        Log.d("OPark","Adatem is 1, get next key");
//                                        i++;

                                    } else {
                                        System.out.println("Can't find users with Adatem 0! =( arrayList got key but all 1");

                                        Intent intent2 = new Intent(MapsMainActivity.this, NoUserPopUp.class);
                                        startActivity(intent2);

                                        loadingCircle.setVisibility(View.INVISIBLE);
                                    }

                                    adatemQueryList.removeEventListener(this);


//                                } else
                                    if (newArrayList == null) {

                                    Log.d("Opark","newArrayList is empty");
                                    System.out.println("Can't find users with Adatem 0! =(");

                                    Intent intent2 = new Intent(MapsMainActivity.this, NoUserPopUp.class);
                                    startActivity(intent2);

                                    loadingCircle.setVisibility(View.INVISIBLE);

                                }


                            } catch (NullPointerException e) {
                                System.out.println(e);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onKeyExited(final String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
                ValueEventListener exitListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newArrayList.remove(key);
                        newHashSet.remove(key);
                        //newHashSet.addAll(newArrayList);
                        //newArrayList.clear();
                        //newArrayList.addAll(newHashSet);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                for (int firstUser = 0; firstUser < newArrayList.size(); firstUser++) {
                    matchmakingRef.child(newArrayList.get(firstUser)).addListenerForSingleValueEvent(exitListener);
//                    break;
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
    }

    private double distance(Location peterParker, Location kenaParker){
        double theta = peterParker.getLongitude() - kenaParker.getLongitude();
        double dist = Math.sin(deg2rad(peterParker.getLatitude()))
                * Math.sin(deg2rad(kenaParker.getLatitude()))
                * Math.cos(deg2rad(peterParker.getLatitude()))
                * Math.cos(deg2rad((peterParker.getLatitude())))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);
    }

    private double rad2deg (double rad){
        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg){
        return (deg * Math.PI / 180.0);
    }

    int markerCount = 0;
    private Marker mk = null;
    // Add A Map Pointer To The MAp
    public void addMarker(GoogleMap googleMap, double lat, double lon) {

        LatLng latlong = new LatLng(lat, lon);

        if(markerCount==1){
            animateMarker(mLastLocation,mk);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong));
        }

        else if (markerCount==0){
            //Set Custom BitMap for Pointer
            int height = 80;
            int width = 45;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.icon_car);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            mMap = googleMap;

//            Location location = new Location("a");
//            location.setLatitude(peterParkerLocation.latitude);
//            location.setLongitude(peterParkerLocation.longitude);
//            mk= mMap.addMarker(new MarkerOptions().position(latlong)
//                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin3))
//                    .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlong,DEFAULT_ZOOM,DEFAULT_TILT,0)));
//            //Set Marker Count to 1 after first marker is created
//            markerCount=1;

            mk= mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin3))
                    .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong,DEFAULT_ZOOM));
            //Set Marker Count to 1 after first marker is created
            markerCount=1;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            startLocationUpdates();
        }
    }

    public static void animateMarker(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(),destination.getLongitude());

            final float startRotation = marker.getRotation();

            final MapsMainActivity.LatLngInterpolator latLngInterpolator = new MapsMainActivity.LatLngInterpolator.LinearFixed();
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

        class LinearFixed implements MapsMainActivity.LatLngInterpolator {
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
    public void onInfoWindowClick (Marker marker){
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    private void saveFoundUserId(){
        SharedPreferences prefs = getSharedPreferences(USER_ID_PREFS,0);
        prefs.edit().putString(USER_ID_KEY, foundUser).apply();
    }

    private void signOut(){
        Log.d("signout", "signoutbutton Clicked");
        UserProfileSetup.mAuth = FirebaseAuth.getInstance();
        UserProfileSetup.mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(MapsMainActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public static ArrayList<String> getNewArrayList(){
        return newArrayList;
    }

    public static ArrayList<String> getOldArrayList(){
        return oldArrayList;
    }

    private void bindViews(){


        shareParkingButton = (Button) findViewById(R.id.share_parking_button);
        findParkingButton = (Button) findViewById(R.id.find_parking_button);
        shareParkingButton.setVisibility(View.INVISIBLE);
        findParkingButton.setVisibility(View.INVISIBLE);
        recenterButton = (Button) findViewById(R.id.recenter_button);
        recenterButton.setVisibility(View.INVISIBLE);
        loadingCircle = (ProgressBar) findViewById(R.id.progress_bar);
        loadingCircle.setVisibility(View.VISIBLE);
        FirebaseApp.initializeApp(getApplicationContext());
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        geoFire = new GeoFire(geofireRef);
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
        signOutButton = (Button) findViewById(R.id.sign_out_button);

    }


}