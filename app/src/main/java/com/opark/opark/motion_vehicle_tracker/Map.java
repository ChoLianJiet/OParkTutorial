package com.opark.opark.motion_vehicle_tracker;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.opark.opark.LoginActivity;
import com.opark.opark.Matchmaking;
import com.opark.opark.NoUserPopUp;
import com.opark.opark.R;
import com.opark.opark.UserProfileSetup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;


public class Map extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //CONSTANT
    final int REQUEST_CODE = 123;
    long MIN_TIME = 5000;
    long MIN_DISTANCE = 1000;
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    String ADATEM0 = "0";
    String ADATEM1 = "1";
    public static String USER_ID_PREFS;
    public static String USER_ID_KEY;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int REQUEST_LOCATION = 0;

    //MEMBER VARIALBLE
    private FirebaseAuth mAuth;

    private GoogleMap mMap;
    private Button shareParkingButton;
    private Button findParkingButton;
    private Button signOutButton;
    private Marker mk = null;
    private Marker marker;
    LocationManager mLocationManager;
    android.location.LocationListener mLocationListener;
    private DatabaseReference geofireRef;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private double longitude;
    private double latitude;
    private double foundLatitude;
    private double foundLongitude;
    private DatabaseReference matchmakingRef;
    private String currentUserID;
    private Query adatemQueryList;
    private static ArrayList<String> newArrayList = new ArrayList<>();
    private static ArrayList<String> oldArrayList = new ArrayList<>();
    private static ArrayList<Pair<String,Marker>> markerArrayList = new ArrayList<Pair<String,Marker>>();
    private HashSet<String> newHashSet = new HashSet<>();
    private HashSet<String> oldHashSet = new HashSet<>();
    private int firstUser = 0;
    private static String foundUser;
    private ProgressBar loadingCircle;


    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private static final String TAG = "";
    private int markerCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        markerCount=0;

        //Check If Google Services Is Available
        if (getServicesAvailable()) {
            // Building the GoogleApi client
            //buildGoogleApiClient();
            //createLocationRequest();
            Toast.makeText(this, "Google Service Is Available!!", Toast.LENGTH_SHORT).show();
        }

        //Create The MapView Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        shareParkingButton = (Button) findViewById(R.id.share_parking_button);
        findParkingButton = (Button) findViewById(R.id.find_parking_button);
        shareParkingButton.setVisibility(View.INVISIBLE);
        findParkingButton.setVisibility(View.INVISIBLE);
        loadingCircle = (ProgressBar) findViewById(R.id.progress_bar);
        loadingCircle.setVisibility(View.VISIBLE);
        FirebaseApp.initializeApp(getApplicationContext());
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        geoFire = new GeoFire(geofireRef);
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        CardView cardView = (CardView) findViewById(R.id.cardView);
        ImageView profilePicture = (ImageView) findViewById(R.id.profilePicture);
        TextView name = (TextView) findViewById(R.id.name);
        TextView carModel = (TextView) findViewById(R.id.carModel);
        TextView carColor = (TextView) findViewById(R.id.carColor);
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


        //Get ID of the user to be tracked from intent's extra
        //String keyOfTrackedUser = getIntent().getStringExtra("userKey");
        geoFire.getLocation(currentUserID, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                Log.i("Hello location result", String.valueOf(latitude) + " " + String.valueOf(longitude));
                addMarker(mMap,latitude,longitude);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        geofireRef.child(currentUserID).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] loc = new String[2];
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                while (iter.hasNext()){
                    DataSnapshot snap = iter.next();
                    loc[Integer.parseInt(snap.getKey())] = snap.getValue().toString();
                }

                addMarker(mMap,latitude,longitude);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //randomLocationGenerator(geoFire);


    }

    //GET CURRENT LOCATION
    private void getAndSetCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("OPark", "onLocationChanged() callback received");

                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Log.i("OPark","longitude is: " + longitude);
                Log.i("OPark","latitude is " + latitude);

                geoFire.setLocation(currentUserID, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @SuppressLint("MissingPermission")
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

    /**
     * GOOGLE MAPS AND MAPS OBJECTS
     *
     * */

    // After Creating the Map Set Initial Location
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Uncomment To Show Google Location Blue Pointer
       // mMap.setMyLocationEnabled(true);
    }


    // Add A Map Pointer To The MAp
    public void addMarker(GoogleMap googleMap, double lat, double lon) {

        LatLng latlong = new LatLng(lat, lon);

        if(markerCount==1){
            animateMarker(lat,lon,mk);
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


            mk= mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin3))
                    .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 16));
            //Set Marker Count to 1 after first marker is created
            markerCount=1;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
//            mMap.setMyLocationEnabled(true);
//            startLocationUpdates();
        }
    }

    @Override
    public void onInfoWindowClick (Marker marker){
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    private void randomLocationGenerator(GeoFire geoFire, Location origin) {

        generateLocationWithinRadius(origin,100);
        geoFire.setLocation("firebase-hq", new GeoLocation(latitude, longitude));

    }

    private GeoLocation generateLocationWithinRadius(Location currentLocation, double radius) {
        double a = currentLocation.getLongitude();
        double b = currentLocation.getLatitude();
        double r = radius;
        Random random = new Random();

        // x must be in (a-r, a + r) range
        double xMin = a - r;
        double xMax = a + r;
        double xRange = xMax - xMin;

        // get a random x within the range
        double x = xMin + random.nextDouble() * xRange;

        // circle equation is (y-b)^2 + (x-a)^2 = r^2
        // based on the above work out the range for y
        double yDelta = Math.sqrt(Math.pow(r,  2) - Math.pow((x - a), 2));
        double yMax = b + yDelta;
        double yMin = b - yDelta;
        double yRange = yMax - yMin;
        // Get a random y within its range
        double y = yMin + random.nextDouble() * yRange;

        // And finally return the location

        return new GeoLocation(x, y);
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
                foundLatitude = location.latitude;
                foundLongitude = location.longitude;
                LatLng kenaParkerLocation = new LatLng(foundLatitude, foundLongitude);
                marker = mMap.addMarker(new MarkerOptions().position(kenaParkerLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.home01_icon_location)));
                oldHashSet.addAll(oldArrayList);
                oldArrayList.clear();
                oldArrayList.addAll(oldHashSet);

                System.out.print("oldArrayList consists " + oldArrayList);
                //the user shared parking//
                if (oldArrayList.contains(key)) {
                    newArrayList.remove(key);

                } else if (currentUserID.equals(key)) {

                } else {

                    newArrayList.add(key);
                    newHashSet.addAll(newArrayList);
                    newArrayList.clear();
                    newArrayList.addAll(newHashSet);
                    markerArrayList.add(Pair.create(key,marker));

                }

                Log.d("OPark","lalala is " + markerArrayList);

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
                    Intent intent2 = new Intent(Map.this, NoUserPopUp.class);
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


                                    } else if (adatemValue.equals(ADATEM1)) {

                                        System.out.println("Adatem is not 1, get next key");


                                    } else {
                                        System.out.println("Can't find users with Adatem 0! =( arrayList got key but all 1");

                                        Intent intent2 = new Intent(Map.this, NoUserPopUp.class);
                                        startActivity(intent2);

                                        loadingCircle.setVisibility(View.INVISIBLE);
                                    }

                                    adatemQueryList.removeEventListener(this);


                                } else if (newArrayList == null) {

                                    Log.i("Opark","newArrayList is empty");
                                    System.out.println("Can't find users with Adatem 0! =(");

                                    Intent intent2 = new Intent(Map.this, NoUserPopUp.class);
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

                        marker.remove();

                        Log.d("OPark",key + "exited!");
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
        Intent intent = new Intent(Map.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public static ArrayList<String> getNewArrayList(){
        return newArrayList;
    }

    public static ArrayList<String> getOldArrayList(){
        return oldArrayList;
    }


    public boolean getServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {

            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannot Connect To Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    /**
     * LOCATION LISTENER EVENTS
     *
     * */

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            //mGoogleApiClient.connect();
        }
//        startLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getServicesAvailable();

        // Resuming the periodic location updates
        //if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            //startLocationUpdates();
        //}
    }

    @Override
    protected void onStop() {
        super.onStop();

        //if (mGoogleApiClient.isConnected()) {
        //    mGoogleApiClient.disconnect();
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        geofireRef.child(currentUserID).removeValue();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    // Creating google api client object
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    //Creating location request object
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(AppConstants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(AppConstants.FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(AppConstants.DISPLACEMENT);
    }


    //Starting the location updates
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest,  this);
        }
    }

    //Stopping location updates
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        //displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        longitude = mLastLocation.getLongitude();
        latitude = mLastLocation.getLatitude();

        Log.i("OPark","longitude is: " + longitude);
        Log.i("OPark","latitude is " + latitude);

        geoFire.setLocation(currentUserID, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @SuppressLint("MissingPermission")
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

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

    //Method to display the location on UI
    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {


            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                String loc = "" + latitude + " ," + longitude + " ";
                Toast.makeText(this,loc, Toast.LENGTH_SHORT).show();

                //Add pointer to the map at location
                addMarker(mMap,latitude,longitude);


            } else {

                Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }



    public static void animateMarker(final double latitude, final double longitude, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(latitude,longitude);

            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);

                        marker.setPosition(newPosition);

                        //marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
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

        class LinearFixed implements LatLngInterpolator {
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
}

