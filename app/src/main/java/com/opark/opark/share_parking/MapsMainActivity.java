package com.opark.opark.share_parking;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
//import android.app.FragmentManager;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.opark.opark.BrandsOfferFragment;
import com.opark.opark.BrandsOfferFragment1;
import com.opark.opark.CustomInfoWindowAdapter;
import com.opark.opark.LoadingScreen;
import com.opark.opark.MarkerTag;
import com.opark.opark.PeterMap;
import com.opark.opark.ProfileNavFragment;
import com.opark.opark.ShowBrandOffer;
import com.opark.opark.rewards_redemption.RewardsFragment;
import com.opark.opark.rewards_redemption.RewardsPocketFragment;
import com.opark.opark.feedback.FeedbackDialog;
import com.opark.opark.feedback.FeedbackModel;
import com.opark.opark.login_auth.LoginActivity;
import com.opark.opark.NoUserPopUp;
import com.opark.opark.R;
import com.opark.opark.UserPopUpFragment;
import com.opark.opark.UserProfileSetup;
import com.opark.opark.login_auth.PhoneAuth;
import com.opark.opark.model.Car;
import com.opark.opark.model.User;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.opark.opark.rewards_redemption.RewardsFragment.merchantOfferAdapter;

public class MapsMainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        LocationListener, GoogleMap.OnCameraMoveStartedListener,UserPopUpFragment.OnUserPopUpFragmentListener,
        FeedbackModel.FeedbackModelCallback{

    //CONSTANT
    private static final String TAG = "MapsMainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    private static final int PLAY_SERVICES_REQUEST_CODE = 124;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;
    private float DEFAULT_ZOOM = 18f;
    private float DEFAULT_TILT = 45f;
    final long ONE_MEGABYTE = 1024 * 1024;
    private String GEOFENCE_ID = "myGeofenceID";

    public static String ADATEM0 = "0";
    public static String ADATEM1 = "1";
    public static String ADATEM2 = "2";
    public static String USER_ID_PREFS;
    public static String USER_ID_KEY;

    //MEMBER VARIALBLE

    private BottomSheetBehavior mBottomSheetBehavior;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    public static GoogleMap mMap;
    public static Button shareParkingButton;
    public static Button findParkingButton;
    private Button signOutButton;
    private Button cancelButton;
    private FloatingActionButton recenterButton;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private GeoFire geofencingGeoFire;
    private GeoQuery geofencingGeoQuery;
    private double longitude;
    private double latitude;
    private double kenaLatitude;
    private double kenaLongitude;
    private DatabaseReference matchmakingRef;
    private DatabaseReference geofireRef;
    private DatabaseReference togetherRef;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    public static String currentUserID;
    public static ArrayList<String> newArrayList = new ArrayList<>();
    public static ArrayList<String> oldArrayList = new ArrayList<>();
    public static HashSet<String> newHashSet = new HashSet<>();
    public static HashSet<String> oldHashSet = new HashSet<>();
    public static Marker kenaMarker;
    private ArrayList<Marker> kenaMarkerArrayList = new ArrayList<>();
    public static HashMap<String,Marker> hasMapMarker = new HashMap<>();
    public static LatLng peterParkerLocation;
    private LatLng kenaParkerLocation;
    private String ownMarkerID;
    public static Location peterParker = new Location("");
    public static String foundUser;
    public ProgressBar loadingCircle;
    private String adatemValue;
    public static UserPopUpFragment userPopUpFragment;
    public static UserPopUpFragment userPopUpFragment1;
    private PopupWindow popUpWindow;
    private LayoutInflater layoutInflater;
    private int backStackCount;
    private static Context infoWindowContextForMapsMain;


    //Kena Details
    public static ArrayList<User> kenaUserObjList = new ArrayList<>();
    private String kenaParkerName;
    private String kenaCarModel;
    private String kenaCarPlateNumber;
    private String kenaCarColor;

    //NavDrawer variables
    public static DrawerLayout mDrawer;
    public static Toolbar toolbar;
    public static NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem oldMenuItem;
    public static FragmentManager navFragmentManager;
    private RelativeLayout mapContainer;
    private Fragment fragment = null;
    private FragmentTransaction ft;
    private ProfileNavFragment userProfilePage;
    private RewardsFragment rewardsPageFrag;
    private RewardsPocketFragment rewardsPockFrag;
    private Fragment currentFragment = null;
    private PhoneAuth phoneAuth;
    TextView userPointsTextView;
    View headerView;
    int userPoints;

    //User Profile
    public static ArrayList<User> userObjList = new ArrayList<>();
    public static String lastName, phoneNum, carColour, carPlate, carBrand, carModel;

    //Feedback Model object
    public FeedbackModel feedbackObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportFragmentManager();
        getLocationPermission();

        shareParkingButton = (Button) findViewById(R.id.share_parking_button);
        findParkingButton = (Button) findViewById(R.id.find_parking_button);
        mapContainer = (RelativeLayout) findViewById(R.id.map_page_container);
        cancelButton = (Button) findViewById(R.id.cancel_map_main_button);
        cancelButton.setVisibility(View.INVISIBLE);

        //INVISIBLE PARKINGBUTTON
//        shareParkingButton.setVisibility(View.INVISIBLE);
//        findParkingButton.setVisibility(View.INVISIBLE);
        //Toolbar
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar_in_maps_main);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Map");

// Inflate the header view at runtime


        // mDrawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();



        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup mDrawer view
        setupDrawerContent(nvDrawer);
//        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header_drawer_activity_main);


        headerView = nvDrawer.getHeaderView(0);

        userPointsTextView = headerView.findViewById(R.id.user_point_header);


        recenterButton = (FloatingActionButton) findViewById(R.id.recenter_button);
        recenterButton.setVisibility(View.INVISIBLE);
        loadingCircle = (ProgressBar) findViewById(R.id.progress_bar);
//        View bottomSheet = findViewById(R.id.bottom_sheet_sorry);
//        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);
        showLoading();
        FirebaseApp.initializeApp(getApplicationContext());
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        geoFire = new GeoFire(geofireRef);
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
        signOutButton = (Button) findViewById(R.id.sign_out_button);

//        setArtificialGeofence();

        shareParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMatchkingFolderInDatabse();
                shareParkingButton.setVisibility(View.INVISIBLE);
                findParkingButton.setVisibility(View.INVISIBLE);
                shareParking();
            }
        });

        findParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarkerForKenaOneByOne();
            }
        });

        recenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Recentered Button is Pressed");
                markerInMiddle = true;
                LatLng latlong = new LatLng(peterParkerLocation.latitude, peterParkerLocation.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, DEFAULT_ZOOM));
                Log.d(TAG, "markerInMiddle is " + markerInMiddle);
                setRecenterButton();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButton.setVisibility(View.GONE);
                cancelUser();
                reverseExchangeButtons(shareParkingButton,findParkingButton);
            }
        });



        Log.d(TAG, "onCreate: current uid" + currentUserID);

        feedbackObj = new FeedbackModel(getApplicationContext(), this);
        feedbackObj.getLastFeedbackCompleted();

    }





   private void acquireUserProfileAndStoreLocal(){

       StorageReference userRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/profile.txt");
       StorageReference userPointRef =FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/points.txt");
        final DatabaseReference userPointDataRef = FirebaseDatabase.getInstance().getReference().child("users/userPoints/" + currentUserID);




       final long ONE_MEGABYTE = 1024 * 1024;

       userPointRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
           @Override
           public void onSuccess(byte[] bytes) {


               try {

                   try {
                       userPoints = (new Gson().fromJson(new String(bytes, "UTF-8"), Integer.class));
                       Log.d(TAG, "onSuccess: " + userPoints);
                       userPointsTextView.setText(String.valueOf(userPoints));


                   } catch (UnsupportedEncodingException e) {
                       e.printStackTrace();
                   }
               }catch (JsonSyntaxException e){
                   e.printStackTrace();
               }



           }
       }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {

                                   }
                               }
       );



       userPointDataRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Log.d(TAG, "onDataChange: inside value listener");
               Log.d(TAG, "onDataChange: datasnapshot" + dataSnapshot.getKey());
               Log.d(TAG, "onDataChange: datasnapshot value" + dataSnapshot.getValue());


               try {
                   userPoints = dataSnapshot.getValue(Integer.class);
//                userPoints = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                   userPointsTextView.setText(String.valueOf(userPoints));


                   Log.d(TAG, "onDataChange: userPoints" + userPoints);
               } catch (NullPointerException e ){

                   userPointDataRef.setValue(userPoints);

               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

               Log.d(TAG, "onCancelled: " + databaseError);
           }
       });


       userRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
           @Override
           public void onSuccess(byte[] bytes) {
               Log.d("Byte","getByte success");


               try {
                   userObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
                   Log.d("Gson","Gsonfrom json success");


                   for (int i = 0; i < MapsMainActivity.userObjList.size(); i++) {
                       Log.d("I", "Iteration success");
                       Log.i("Hello", "heyhey" + MapsMainActivity.userObjList.get(i).getUserName().getFirstName());
//                            firstName.setText(userObjList.get(i).getUserName().getFirstName());
                       lastName = (MapsMainActivity.userObjList.get(i).getUserName().getLastName());
                       phoneNum = (MapsMainActivity.userObjList.get(i).getUserName().getPhoneNum());
                       carColour =(MapsMainActivity.userObjList.get(i).getUserCar().getCarColour());
                       carBrand = (MapsMainActivity.userObjList.get(i).getUserCar().getCarBrand());
                       carModel = (MapsMainActivity.userObjList.get(i).getUserCar().getCarModel());
                       carPlate= (MapsMainActivity.userObjList.get(i).getUserCar().getCarPlate());
                       Log.d(TAG,"lastName variable is " + lastName);
                   }
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception exception) {
               // Handle any errors
           }
       });

   }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the mDrawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

//                        try{
//                        navFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//                            @Override
//                            public void onBackStackChanged() {
//                                navFragmentManager.popBackStack();
//                            }
//                        });} catch (NullPointerException e ){Log.d(TAG, "OnBackStackChangedListener null execption catch");}

                        if (menuItem == oldMenuItem){
                            Log.d(TAG,"menuitem is displayed already");
//                            Log.d("navmenuitem","menu item ischecked and return false");
                            return false;
                        }
                        else {
//                           backStackCount= navFragmentManager.getBackStackEntryCount();
//                            Log.d(TAG, "onNavigationItemSelected: backstack count is " +backStackCount);
                            Log.d(TAG,"menuitem is not displayed");
//                            Log.d("navmenuitem","menu item isnotchecked and setChecked(false) and return true");
                            try{
                                    try{
                                backStackCount= navFragmentManager.getBackStackEntryCount();
                                Log.d(TAG, "onNavigationItemSelected: backstack count is " +backStackCount);
                                if( backStackCount !=0){
                                navFragmentManager.popBackStack();}

                               }
                                    catch (NullPointerException e ){
                                        Log.d(TAG, "onNavigationItemSelected: NullPoint");
                                    }

                                } catch (IllegalStateException e ){Log.d(TAG,"first selected pop");}

                                selectDrawerItem(menuItem);

                            mDrawer.closeDrawer(GravityCompat.START);

                            oldMenuItem =menuItem;
                            return true;

                        }

                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                userProfilePage = new ProfileNavFragment();
                fragment = userProfilePage;
                executeFragmentTransaction(fragment);
                break;

            case R.id.nav_second_fragment:
                rewardsPageFrag = new RewardsFragment();
                fragment = rewardsPageFrag;
                executeFragmentTransaction(fragment);
                    break;

            case R.id.nav_third_fragment:
                rewardsPockFrag = new RewardsPocketFragment();
                fragment = rewardsPockFrag;
                executeFragmentTransaction(fragment);
                break;
            default:
//                fragmentClass = FirstFragment.class;
                FeedbackDialog dialog = new FeedbackDialog();
                Car car = new Car("Blue","Toyota","Harrier","BJT 2883");
                dialog.setArguments(dialog.setCarDetails(car));
                dialog.show(getSupportFragmentManager(), "");
        }

        //replacing the fragment


//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }


    private void executeFragmentTransaction(Fragment frag) {
        if ((frag != null) ) {

            navFragmentManager= getSupportFragmentManager();
            navFragmentManager.beginTransaction()
                        .replace(R.id.nav_view_selection_container, frag)
                        .addToBackStack(null)
                         .commit();
            Log.d(TAG, "executing fragment transaction , added to backstack");
            mapContainer.setVisibility(View.GONE);
            currentFragment = frag;
            Log.d(TAG, "executeFragmentTransaction: currentFragment is " + currentFragment);
        }
        else{
            Log.d(TAG, "same fragment");
            return;
        }
    }






    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the mDrawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
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
        infoWindowContextForMapsMain = getApplicationContext();
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.setOnCameraMoveStartedListener(this);
        try {

            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsMainActivity.this));

        } catch (NullPointerException e){
            Log.d(TAG, "onMapReady: setInfoWindowAdapter" + e);
        }
        mMap.setOnInfoWindowClickListener(null);
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
                        loadLocationForThisUser();
                        acquireUserProfileAndStoreLocal();
                        dismissLoading();
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
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onDestroy() {
        if (geofireRef.child(currentUserID) != null) {
            geofireRef.child(currentUserID).removeValue();
        }

        if (matchmakingRef.child(currentUserID) != null) {
            matchmakingRef.child(currentUserID).removeValue();
        }
        super.onDestroy();
    }

    private void setRecenterButton() {
        if (markerInMiddle == true) {
            recenterButton.setVisibility(View.INVISIBLE);
        } else {
            recenterButton.setVisibility(View.VISIBLE);
        }
    }

    private void setArtificialGeofence(){
        geofencingGeoFire = new GeoFire(geofireRef);
        geofencingGeoQuery = geoFire.queryAtLocation(new GeoLocation(3.032307, 101.722998), 10.0);
        geofencingGeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (mLastLocation != null) {
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
                                loadLocationForThisUser();
                                shareParkingButton.setVisibility(View.VISIBLE);
                                findParkingButton.setVisibility(View.VISIBLE);
                                mBottomSheetBehavior.setHideable(true);
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            }
                        }
                    });
                } else {
                    Log.d("Test", "Couldn't load location");
                }
            }

            @Override
            public void onKeyExited(String key) {
                shareParkingButton.setVisibility(View.INVISIBLE);
                findParkingButton.setVisibility(View.INVISIBLE);
                mBottomSheetBehavior.setHideable(false);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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

    //Retrieve location for peterParker
    private void loadLocationForThisUser() {

        geofireRef.child(currentUserID).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals("0")) {

                        peterParker.setLatitude(Double.parseDouble(postSnapshot.getValue().toString()));
                    }

                    if (postSnapshot.getKey().equals("1")) {
                        peterParker.setLongitude(Double.parseDouble(postSnapshot.getValue().toString()));
                    }
                }

                peterParkerLocation = new LatLng(peterParker.getLatitude(), peterParker.getLongitude());
                //Marker for peterParker
                addMarker(mMap, peterParkerLocation.latitude, peterParkerLocation.longitude);
                findOtherUsersLocation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    //Share Parking Pressed will set adatem and sessionKey in database
    private void setMatchkingFolderInDatabse() {

        matchmakingRef.child(currentUserID + "/sessionKey").setValue(currentUserID);
        matchmakingRef.child(currentUserID + "/sessionKey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getValue().toString();
                Matchmaking bothAdatemAndKey = new Matchmaking(ADATEM0, key);
                matchmakingRef.child(currentUserID).setValue(bothAdatemAndKey);
                Log.i("Opark", "Session key is:" + key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //FIND OTHER USERS LOCATION
    private void findOtherUsersLocation() {
        geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 100.0);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {

                if (!key.equals(currentUserID)) {
                    matchmakingRef.child(key).child("adatem").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                adatemValue = dataSnapshot.getValue().toString();

                                if (oldArrayList.contains(key)) {
                                    //do nothing
                                    if (!adatemValue.equals(ADATEM0)) {
                                        oldArrayList.remove(key);
                                        Log.d(TAG, key + " has been removed due to becoming not 0, oldArrayList consist of " + oldArrayList);
                                    }
                                } else if (adatemValue.equals(ADATEM0)) {
                                    setKenaMarkers(new LatLng(location.latitude,location.longitude),key,ADATEM0);
                                    if(!oldArrayList.contains(key)) {
                                        newArrayList.add(key);
                                        newHashSet.addAll(newArrayList);
                                        newArrayList.clear();
                                        newArrayList.addAll(newHashSet);
                                        newHashSet.clear();

                                        Log.d(TAG, "newArrayList consist of " + newArrayList);
                                    }

                                } else if (!adatemValue.equals(ADATEM0)) {

                                    Log.d(TAG, key + " adatem has become not 0, removed from newArrayList");

                                    newArrayList.remove(key);
                                    newHashSet.addAll(newArrayList);
                                    newArrayList.clear();
                                    newArrayList.addAll(newHashSet);
                                    newHashSet.clear();
                                    if(adatemValue.equals(ADATEM1)){
                                        setKenaMarkers(new LatLng(location.latitude,location.longitude),key,ADATEM1);
                                    } else  {
                                        removeKenaMarkers(key);
                                    }

                                    Log.d(TAG, "newArrayList consist of " + newArrayList);

                                } else {
                                    Log.d(TAG, "nothing is triggered, newArrayList is not used");
                                    //do nothing
                                }
                            } catch (NullPointerException e){
                                System.out.println(e);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    //do nothing
                    Log.d(TAG, "key is currentUserId, which is " + key + ", so no need to put in newArrrayList");
                }

            }


            @Override
            public void onKeyExited(final String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));

                newArrayList.remove(key);
                newHashSet.addAll(newArrayList);
                newArrayList.clear();
                newArrayList.addAll(newHashSet);
                newHashSet.clear();
                oldArrayList.remove(key);
                Log.d(TAG, "newArrayList consist of " + newArrayList);

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


    private void setMarkerForKenaOneByOne() {
        showLoading();
        if (!newArrayList.isEmpty()) {
            exchangeButtons(shareParkingButton,findParkingButton);
            int i = 0;
            foundUser = newArrayList.get(i);

            Log.d(TAG, "foundUser is " + foundUser);
            matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM1);
            geofireRef.child(foundUser).child("l").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot subscriptionDataSnapshot : dataSnapshot.getChildren()) {

                        Log.d(TAG, "subscriptionDataSnapshot is: " + dataSnapshot);

                        if (subscriptionDataSnapshot.getKey().equals("0")) {
                            kenaLatitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                            Log.d(TAG, "foundUserLatitude is: " + kenaLatitude);
                        }

                        if (subscriptionDataSnapshot.getKey().equals("1")) {
                            kenaLongitude = Double.parseDouble(subscriptionDataSnapshot.getValue().toString());
                            Log.d(TAG, "foundUserLongitude is: " + kenaLongitude);
                        }
                    }
                    kenaParkerLocation = new LatLng(kenaLatitude, kenaLongitude);
                    Log.d(TAG, "foundUserLocation is: " + kenaParkerLocation);

                    saveFoundUserId();

                    dismissLoading();

                    if(position == 123) {
                        kenaMarker.remove();
                        userPopUpFragment1 = new UserPopUpFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.popBackStack();
                        Log.d(TAG, "setMarkerForKenaOneByOne: "+ manager.getBackStackEntryCount());
                        manager.beginTransaction()
                                .add(R.id.popupuser, userPopUpFragment1, null)
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                .show(userPopUpFragment1)
                                .addToBackStack(null)
                                .commit();
                        Log.d(TAG,"position is 123");
                        setKenaMarker(kenaParkerLocation);
                    } else {
                        userPopUpFragment = new UserPopUpFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction()
                                .add(R.id.popupuser, userPopUpFragment, null)
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                .show(userPopUpFragment)
                                .addToBackStack(null)
                                .commit();
                        Log.d(TAG,"position is not 123");
                        setKenaMarker(kenaParkerLocation);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


//            }

//            UserPopUpFragment userPopUpFragment = new UserPopUpFragment();
//            userPopUpFragment.show(getFragmentManager(),"userPopUpFragment");

//            displayFragment(foundUser);


        } else if (newArrayList.isEmpty()) {

            if (!oldArrayList.isEmpty()) {

                dismissLoading();
                newArrayList.addAll(oldArrayList);
                oldArrayList.clear();
                newHashSet.addAll(newArrayList);
                newArrayList.clear();
                newArrayList.addAll(newHashSet);
                newHashSet.clear();

                Log.d(TAG, "all oldArrayList has been added to newArrayList, oldArrayList now contains" + oldArrayList + ", while newArrayList now constains " + newArrayList);
                setMarkerForKenaOneByOne();

            } else if (oldArrayList.isEmpty()) {

                dismissLoading();
                Intent intent = new Intent(MapsMainActivity.this, NoUserPopUp.class);
                startActivity(intent);
            }
        }

    }

    private void setKenaMarker(LatLng thisLocation) {

        kenaMarker = mMap.addMarker(new MarkerOptions()
                .position(thisLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(thisLocation, DEFAULT_ZOOM, DEFAULT_TILT, 0)));
        Log.d(TAG, "kenaMarker is set!");
    }

    private void setKenaMarkers(final LatLng thisLocation, final String fndUser, final String adatemValue) {
        Log.d(TAG, "setKenaMarkers: is called and fndUser is " + fndUser);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        final MarkerTag yourMarkerTag = new MarkerTag();
        yourMarkerTag.setUID(fndUser);

        final StorageReference getKenaProfileRef = storageRef.child("users/" + fndUser + "/profile.txt");
        getKenaProfileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    kenaUserObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
                    Log.d(TAG, "Gsonfrom json success");
                    for(int i=0; i < kenaUserObjList.size(); i++ ) {
                        kenaParkerName = kenaUserObjList.get(i).getUserName().getFirstName() + kenaUserObjList.get(i).getUserName().getLastName();
                        kenaCarModel = kenaUserObjList.get(i).getUserCar().getCarBrand() + kenaUserObjList.get(i).getUserCar().getCarModel();
                        kenaCarPlateNumber = kenaUserObjList.get(i).getUserCar().getCarPlate();
                        kenaCarColor = kenaUserObjList.get(i).getUserCar().getCarColour();
                    }

                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"fragment is not created, exception: " + exception);
            }
        }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
//                matchmakingRef.child(fndUser).child("sessionKey").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (adatemValue.equals(ADATEM0)){
                            kenaMarker = mMap.addMarker(new MarkerOptions()
                                    .position(thisLocation)
                                    .snippet(kenaCarColor + " " + kenaCarModel + " (" + kenaCarPlateNumber + ") " )
                                    .title(kenaParkerName)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                            yourMarkerTag.setYesNoTag("yes");
                            kenaMarker.setTag(yourMarkerTag);
                            Log.d(TAG, "onComplete: kenaMarker getTag of yourMarkerTag is " + ((MarkerTag) kenaMarker.getTag()).getYesNoTag() + " & " + ((MarkerTag) kenaMarker.getTag()).getUID());
                            hasMapMarker.put(fndUser,kenaMarker);

                            foundUser = ((MarkerTag) kenaMarker.getTag()).getUID();
                            Log.d(TAG,"foundUser MarkerTag getUID is " + foundUser);

//                            mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
//                                @Override
//                                public void onInfoWindowLongClick(Marker marker) {
//                                    Log.d("LONG", "CLICKED long and fndUser is " + fndUser);
//
//                                    foundUser = CustomInfoWindowAdapter.tvUserIDView.getText().toString();
//
//                                    matchmakingRef.child(CustomInfoWindowAdapter.tvUserIDView.getText().toString()).child("adatem").setValue(MapsMainActivity.ADATEM2);
//                                    togetherRef.child(CustomInfoWindowAdapter.tvUserIDView.getText().toString()).child("peter").setValue(MapsMainActivity.currentUserID);
//                                    Intent intent = new Intent(MapsMainActivity.this,PeterMap.class);
//                                    startActivity(intent);
//                                }
//                            });
                        } else if (adatemValue.equals(ADATEM1)) {
                            kenaMarker = mMap.addMarker(new MarkerOptions()
                                    .position(thisLocation)
                                    .snippet(kenaCarColor + " " + kenaCarModel + " (" + kenaCarPlateNumber + ") " )
                                    .title(kenaParkerName)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            yourMarkerTag.setYesNoTag("no");
                            kenaMarker.setTag(yourMarkerTag);
                            Log.d(TAG, "onComplete: kenaMarker getTag of yourMarkerTag is " + ((MarkerTag) kenaMarker.getTag()).getYesNoTag() + " & " + ((MarkerTag) kenaMarker.getTag()).getUID());
                            hasMapMarker.put(fndUser,kenaMarker);
//                            mMap.setOnInfoWindowLongClickListener(null);
                        }

                        Log.d(TAG, "kenaMarker is set! Which is " + kenaMarker);

                        Log.d(TAG,"unique key is " + fndUser);

                        Log.d(TAG, "onDataChange: hasMapMarker have " + hasMapMarker);

//                        kenaMarkerArrayList.add(kenaMarker);
//                        Log.d(TAG, "onDataChange: kenaMarkerArrayList contains " + kenaMarkerArrayList);
//                        for(int i=0; i < kenaMarkerArrayList.size(); i++ )
//                            if(!hasMapMarker.containsValue(kenaMarkerArrayList.get(i)) ){
//                                kenaMarkerArrayList.get(i).remove();
//                                Log.d(TAG, "onDataChange: marker of " + kenaMarkerArrayList.get(i) + " has been removed");
//                                Log.d(TAG, "onDataChange: after remove, kenaMarkerArrayList contains " + kenaMarkerArrayList);
//                            } else {
//
//                            }

//                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsMainActivity.this));

            }
        });


    }

    private void removeKenaMarkers(final String fndUser) {

        matchmakingRef.child(fndUser).child("sessionKey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uniqueKey = dataSnapshot.getValue().toString();
                Log.d(TAG, "unique key to remove is " + uniqueKey);
                try {
                    kenaMarker = hasMapMarker.get(uniqueKey);
                    Log.d(TAG, "onDataChange: kenaMarker in hasMapMarker to remove is " + kenaMarker);
                    kenaMarker.remove();
                    Log.d(TAG, "kenaMarker is removed!");
                } catch (NullPointerException e){
                    Log.d(TAG, "onDataChange: hasMapMarker " + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void shareParking() {
        Intent intent = new Intent(MapsMainActivity.this, LoadingScreen.class);
        startActivity(intent);
    }

    private double distance(Location peterParker, Location kenaParker) {
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

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    int markerCount = 0;
    private Marker mk = null;
    boolean markerInMiddle = false;

    // Add A Map Pointer To The MAp
    public void addMarker(GoogleMap googleMap, double lat, double lon) {

        LatLng latlong = new LatLng(lat, lon);

        if (markerCount == 1 && markerInMiddle == true) {
            animateMarker(peterParker, mk);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong));
        } else if (markerCount == 1 && markerInMiddle == false) {
            animateMarker(peterParker, mk);
        }else if (markerCount == 0) {
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
            ownMarkerID = mk.getId();
            final MarkerTag yourMarkerTag = new MarkerTag();
            yourMarkerTag.setUID(currentUserID);
            yourMarkerTag.setYesNoTag("own");
            mk.setTag(yourMarkerTag);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
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
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();

            final MapsMainActivity.LatLngInterpolator latLngInterpolator = new MapsMainActivity.LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
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

    public static int position = 0;

    @Override
    public void onArticleSelected(int position) {
        this.position = position;
        if (this.position == 123){
            Log.d(TAG,"this position is really " + position);
            setMarkerForKenaOneByOne();
            this.position = 0;
        } else {
            // do nothing
            Log.d(TAG,"this position is " + this.position);
        }

    }

    @Override
    public void onLastFeedbackCompleted(boolean isCompleted, Car car) {
        if(!isCompleted) {
            FeedbackDialog dialog = new FeedbackDialog();
            dialog.setArguments(dialog.setCarDetails(car));
            dialog.show(getSupportFragmentManager(), "");
        }
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
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();

    }


    private void saveFoundUserId() {
        SharedPreferences prefs = getSharedPreferences(USER_ID_PREFS, 0);
        prefs.edit().putString(USER_ID_KEY, foundUser).apply();
    }

    private void signOut() {
        Log.d("signout", "signoutbutton Clicked");
        LoginManager.getInstance().logOut();
        UserProfileSetup.mAuth = FirebaseAuth.getInstance();
        UserProfileSetup.mAuth.signOut();
        Intent intent = new Intent(MapsMainActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    void showLoading() {
        loadingCircle.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    void dismissLoading() {
        loadingCircle.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
//    public void displayFragment(String string) {
//
//        UserPopUpFragment fragment;
//
//        if (string == null){
//            fragment = new UserPopUpFragment();
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.add(R.id.popupuser,fragment);
//            ft.hide(fragment);
//        }
//
//        if (string == foundUser) {
//
//            fragment = new UserPopUpFragment();
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.add(R.id.popupuser,fragment);
//            ft.commit();
//        }
//    }




    @Override
    public void onBackPressed() {
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (fragment != null ){


            Log.d("backpress","Back Pressed when fragment is " + fragment  );
//            navFragmentManager = getFragmentManager();
//            ft = navFragmentManager.beginTransaction();



//            Log.d(TAG, "onBackPressed: Showbrandoffervisibl " + BrandsOfferFragment1.showBrandOffer1.isVisible());
            navFragmentManager.popBackStack();

            mapContainer.setVisibility(View.VISIBLE);
            toolbar.setTitle("Map");
                            // Update your UI here.
//                            ft.remove(userProfilePage);
//                            ft.replace(R.id.map_page_container,null);
//                            ft.commit();
            fragment=null;
            oldMenuItem = null;


            mDrawer.closeDrawer(GravityCompat.START);

        }
    }

    private void cancelUser(){
        MapsMainActivity.kenaMarker.remove();
        position = 124;
        returnToMain();
        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM0);
    }

    private void returnToMain(){

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        FragmentTransaction transaction = manager.beginTransaction();

        if (MapsMainActivity.position != 123){
            transaction.remove(MapsMainActivity.userPopUpFragment);
            Log.d(TAG,"userPopUpFragment is removed");
        } else {
            transaction.remove(MapsMainActivity.userPopUpFragment);
            transaction.remove(MapsMainActivity.userPopUpFragment1);
            Log.d(TAG,"userPopUpFragment and userPopUpFragment1 is removed");
        }
        MapsMainActivity.kenaMarker.remove();
        transaction.commit();
    }

    private void exchangeButtons(Button btn1, Button btn2) {

        cancelButton.setVisibility(View.VISIBLE);
        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        cancelButton.startAnimation(animZoomIn);

        ObjectAnimator animationBtn1 = ObjectAnimator.ofFloat(btn1,"translationX",250f);
        animationBtn1.setDuration(500);


//        else if(RewardsFragment.searchView) {}

        ObjectAnimator animationBtn2 = ObjectAnimator.ofFloat(btn2,"translationX",-250f);
        animationBtn2.setDuration(500);
        animationBtn1.start();
        animationBtn2.start();


    }

    private void reverseExchangeButtons(Button btn1, Button btn2){
        ObjectAnimator animationBtn1 = ObjectAnimator.ofFloat(btn1,"translationX",-15f);
        animationBtn1.setDuration(500);
        animationBtn1.start();

        ObjectAnimator animationBtn2 = ObjectAnimator.ofFloat(btn2,"translationX",15f);
        animationBtn2.setDuration(500);
        animationBtn2.start();

        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setAnimation(null);

    }

    public static Context getMapsMainContext(){
        return MapsMainActivity.infoWindowContextForMapsMain;
    }

}