package com.opark.opark;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class MapsMainActivity extends FragmentActivity implements OnMapReadyCallback {

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

    //CONSTANT
    private static final String TAG = "MapsMainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    final int REQUEST_CODE = 123;
    long MIN_TIME = 5000;
    long MIN_DISTANCE = 1000;
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    String ADATEM0 = "0";
    String ADATEM1 = "1";
    public static String USER_ID_PREFS;
    public static String USER_ID_KEY;

    //MEMBER VARIALBLE
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private Button shareParkingButton;
    private Button findParkingButton;
    private Button signOutButton;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private double longitude;
    private double latitude;
    private DatabaseReference matchmakingRef;
    private String currentUserID;
    private Query adatemQueryList;
    private static ArrayList<String> newArrayList = new ArrayList<>();
    private static ArrayList<String> oldArrayList = new ArrayList<>();
    private HashSet<String> newHashSet = new HashSet<>();
    private HashSet<String> oldHashSet = new HashSet<>();
    private int firstUser = 0;
    private static String foundUser;
    private ProgressBar loadingCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);

        getLocationPermission();

        shareParkingButton = (Button) findViewById(R.id.share_parking_button);
        findParkingButton = (Button) findViewById(R.id.find_parking_button);
        shareParkingButton.setVisibility(View.INVISIBLE);
        findParkingButton.setVisibility(View.INVISIBLE);
        loadingCircle = (ProgressBar) findViewById(R.id.progress_bar);
        loadingCircle.setVisibility(View.VISIBLE);
        FirebaseApp.initializeApp(getApplicationContext());
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        geoFire = new GeoFire(geofireRef);
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        signOutButton = (Button) findViewById(R.id.sign_out_button);

        getAndSetCurrentLocation();

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

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("I'm here!").icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_car)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode){
            case REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //initiate map
                    initMap();
                }
            }
        }
//        if(requestCode == REQUEST_CODE){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Log.i("OPark","onRequestPermissionResult(): Permission Granted");
//                getAndSetCurrentLocation();
//            } else {
//                Log.i("OPark","Permission denied =(");
//            }
//        }
    }


    //GET CURRENT LOCATION
    private void getAndSetCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("OPark", "onLocationChanged() callback received");

                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Log.i("OPark","longitude is: " + longitude);
                Log.i("OPark","latitude is " + latitude);

                geoFire.setLocation(currentUserID, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully as lat[" + latitude + "], lon[" + longitude + "]!");
                            shareParkingButton.setVisibility(View.VISIBLE);
                            findParkingButton.setVisibility(View.VISIBLE);
                            loadingCircle.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Log.i("OPark", "onProviderDisabled() callback received");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    private void setMatchkingFolderInDatabse(){

        String key = matchmakingRef.child(currentUserID+"/SessionKey").push().getKey();
        Matchmaking bothAdatemAndKey = new Matchmaking(ADATEM0,key);
        matchmakingRef.child(currentUserID).setValue(bothAdatemAndKey);
        Log.i("Opark", "Session key is:" + key);
    }

    //FIND OTHER USERS LOCATION
    private void findOtherUsersLocation() {
        loadingCircle.setVisibility(View.VISIBLE);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 1.0);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));

                oldHashSet.addAll(oldArrayList);
                oldArrayList.clear();
                oldArrayList.addAll(oldHashSet);

                System.out.print("oldArrayList consists " + oldArrayList);
                //the user shared parking//
                if (oldArrayList.contains(key)) {
                    newArrayList.remove(key);

                } else if (currentUserID.equals(key)) {

                }   else {

                    newArrayList.add(key);
                    newHashSet.addAll(newArrayList);
                    newArrayList.clear();
                    newArrayList.addAll(newHashSet);

                }

                for (firstUser = 0; firstUser < newArrayList.size(); firstUser++) {
                    foundUser = newArrayList.get(firstUser);
                    break;
                }

                if (newArrayList.isEmpty()){

                    newArrayList.addAll(oldArrayList);
                    newHashSet.addAll(newArrayList);
                    newArrayList.clear();
                    newArrayList.addAll(newHashSet);
                    oldArrayList.clear();

                } else {
                    Log.i("OPark","newArrayList is not empty lalala");
                }

                if (foundUser == null) {
                    System.out.println("There are no foundUsers");
                    Intent intent2 = new Intent(MapsMainActivity.this, NoUserPopUp.class);
                    startActivity(intent2);
                    loadingCircle.setVisibility(View.INVISIBLE);
                } else {
                    adatemQueryList = matchmakingRef.child(foundUser);
                    System.out.println("found user is " + foundUser);
                    adatemQueryList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                if (newArrayList != null) {

                                    Log.i("Opark", "newArrayList is not empty. Keys are " + newArrayList);
                                    Log.i("OPark", "adatem value is " + dataSnapshot);

                                    String adatemValue = dataSnapshot.child("adatem").getValue().toString();
                                    Log.i("OPark", foundUser + "'s adatemValue is " + adatemValue);


                                    if (adatemValue.equals(ADATEM0)) {

                                        System.out.println("Found Adatem 0!");
                                        Log.i("OPark", "foundUser is " + foundUser);
                                        matchmakingRef.child(foundUser).child("adatem").setValue("1");

                                        saveFoundUserId();

                                        loadingCircle.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(MapsMainActivity.this, UserPopUp.class);
                                        startActivity(intent);


                                    } else if (adatemValue.equals(ADATEM1)) {

                                        System.out.println("Adatem is not 1, get next key");


                                    } else {
                                        System.out.println("Can't find users with Adatem 0! =( arrayList got key but all 1");

                                        Intent intent2 = new Intent(MapsMainActivity.this, NoUserPopUp.class);
                                        startActivity(intent2);

                                        loadingCircle.setVisibility(View.INVISIBLE);
                                    }

                                    adatemQueryList.removeEventListener(this);


                                } else if (newArrayList == null) {

                                    Log.i("Opark","newArrayList is empty");
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
                        newHashSet.addAll(newArrayList);
                        newArrayList.clear();
                        newArrayList.addAll(newHashSet);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                for (int firstUser = 0; firstUser < newArrayList.size(); firstUser++) {
                    matchmakingRef.child(newArrayList.get(firstUser)).addListenerForSingleValueEvent(exitListener);
                    break;
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

    private void saveFoundUserId(){
        SharedPreferences prefs = getSharedPreferences(USER_ID_PREFS,0);
        prefs.edit().putString(USER_ID_KEY, foundUser).apply();
    }

    private void signOut(){
        Log.d("signout", "signoutbutton Clicked");
        UserProfileSetup.mAuth = FirebaseAuth.getInstance();
        UserProfileSetup.mAuth.signOut();
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
}
