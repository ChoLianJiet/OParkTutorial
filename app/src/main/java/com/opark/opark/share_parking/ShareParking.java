package com.opark.opark.share_parking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opark.opark.R;
import com.opark.opark.UserPopUp;

import java.util.ArrayList;

public class ShareParking extends AppCompatActivity {

    //CONSTANT
    final int REQUEST_CODE = 123;
    long MIN_TIME = 5000;
    long MIN_DISTANCE = 1000;
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    //MEMBER VARIABLE
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    private double longitude;
    private double latitude;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private DatabaseReference matchmakingRef;
    private String userKey;
    public String currentUserID;
    public ArrayList<String> newArrayList;
    public String kenaParker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_parking);

        Button shareParkingButton = (Button) findViewById(R.id.share_parking_button);
        Button findParkingButton = (Button) findViewById(R.id.find_parking_button);
        ImageButton mapButton = (ImageButton) findViewById(R.id.toMapButton);
        FirebaseApp.initializeApp(getApplicationContext());
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        geoFire = new GeoFire(geofireRef);
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");

        findOtherUsersLocation();

        shareParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                setCurrentLocation();
                setMatchkingFolderInDatabse();
            }
        });

        findParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findParking();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.opark.opark.share_parking.ShareParking.this, com.opark.opark.motion_vehicle_tracker.Map.class);
                startActivity(intent);
            }
        });
    }

    //GET CURRENT LOCATION
    private void getCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("OPark", "onLocationChanged() callback received");

                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Log.d("OPark","longitude is: " + longitude);
                Log.d("Opark","latitude is " + latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Log.d("OPark", "onProviderDisabled() callback received");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("OPark","onRequestPermissionResult(): Permission Granted");
                getCurrentLocation();
            } else {
                Log.d("OPark","Permission denied =(");
            }
        }
    }

    //SET CURRENT LOCATION
    private void setCurrentLocation() {

        geoFire.setLocation(currentUserID, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });
    }

    //FIND OTHER USERS LOCATION (PASSIVELY)
    private void findOtherUsersLocation() {

        geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 1.0);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                userKey = key;

            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
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

    private void setMatchkingFolderInDatabse(){

        String Adatem = "0";
        String key = matchmakingRef.child(currentUserID+"/SessionKey").push().getKey();
        Matchmaking bothAdatemAndKey = new Matchmaking(Adatem,key);
        matchmakingRef.child(currentUserID).setValue(bothAdatemAndKey);
        Log.i("Opark", "Session key is:" + key);

    }

    private void findParking() {

        try {
            newArrayList = new ArrayList<>();
            newArrayList.add(userKey);
            notify();
            Log.d("Opark", "the keys in the area are" + userKey);

            if (newArrayList != null) {
                for (int firstUser = 0; firstUser < newArrayList.size(); firstUser++) {
                    if (matchmakingRef.child(newArrayList.get(firstUser) + "/Adatem").equals("0")) {
                        kenaParker = newArrayList.get(firstUser);
                        matchmakingRef.child(kenaParker + "/Adatem").setValue("1");
                        Intent intent = new Intent(ShareParking.this, UserPopUp.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //TODO: else what will happen
                    }
                }
            }

        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
