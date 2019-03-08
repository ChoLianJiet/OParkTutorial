package com.opark.opark.share_parking;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import android.content.Intent;
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
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.app.FragmentManager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.opark.opark.CustomInfoWindowAdapter;
import com.opark.opark.DirectionsParser;
import com.opark.opark.FinishUserPopUp;
import com.opark.opark.LoadingScreen;
import com.opark.opark.MarkerTag;
import com.opark.opark.PeterMap;
import com.opark.opark.ProfileNavFragment;
import com.opark.opark.RoadsParser;
import com.opark.opark.TaskRequestMarkerDirections;
import com.opark.opark.TaskRequestMarkerDirections1;
import com.opark.opark.dialogs.DecideWantToRemovePinDialog;
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


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class MapsMainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        LocationListener, GoogleMap.OnCameraMoveStartedListener,
        FeedbackModel.FeedbackModelCallback {

    //CONSTANT
    private static final String TAG = "MapsMainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    private static final int PLAY_SERVICES_REQUEST_CODE = 124;
    private static int UPDATE_INTERVAL = 0;
    private static int FASTEST_INTERVAL = 0;
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

    private static final int TWO_MINUTES = 1000 * 60 * 2;


    //MEMBER VARIALBLE
    public static View mapGroup;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    public static Location mCurrentLocation;
    public static GoogleMap mMap;
    private MapView mMapView;
    public static Button shareParkingButton;
    public static Button findParkingButton;
    private Button signOutButton;
    public static Button cancelButton;
    private FloatingActionButton recenterButton;
    public static GeoFire geoFire;
    private GeoQuery geoQuery;
    private GeoFire liveUsersGeoFire;
    private GeoFire geofencingGeoFire;
    private GeoQuery geofencingGeoQuery;
    public static double longitude;
    public static double latitude;
    private double kenaLatitude;
    private double kenaLongitude;
    private DatabaseReference parentOperationRef;
    private DatabaseReference matchmakingRef;
    private DatabaseReference geofireRef;
    private DatabaseReference liveUsersRef;
    private DatabaseReference togetherRef;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    public static String currentUserID;
    public static ArrayList<String> newArrayList = new ArrayList<>();
    public static ArrayList<String> oldArrayList = new ArrayList<>();
    public static HashSet<String> newHashSet = new HashSet<>();
    public static HashSet<String> oldHashSet = new HashSet<>();
    private Map<String, Marker> mStringMarkerMap = new HashMap<String, Marker>();
    public static Marker kenaMarker;
    private Marker lastClickedMarker;
    private ArrayList<Marker> kenaMarkerArrayList = new ArrayList<>();
    private HashMap<String, Marker> hasMapMarker = new HashMap<>();
    private ArrayList<String> getLastClickedMarkerUser = new ArrayList<>(1);
    public static LatLng peterParkerLocation;
    private LatLng kenaParkerLocation;
    private String ownMarkerID;
    public static Location peterParker = new Location("");
    public static String foundUser;
    public ProgressBar loadingCircle;
    private String adatemValue;
    private FragmentManager fragManager;
    public static UserPopUpFragment userPopUpFragment;
    private int backStackCount;


    //Pin Location
    public static ImageButton pinLocationButton;
    public static ImageButton removePinLocationButton;
    public static Double pinnedLatitude;
    public static Double pinnedLongitude;
    public static boolean locationIsPinned;
    public static DatabaseReference pinRef;

    //Fake Buttons
    private static Button fakeFindParkingButton;


    //Kena Details
    public static HashMap<String, User> kenaUserObjList = new HashMap<>();
    public static String kenaParkerName;
    public static String kenaCarModel;
    public static String kenaCarPlateNumber;
    public static String kenaCarColor;

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

    //Draw line on Google Map
    public static List<Polyline> lineArray = new ArrayList<>();

    //Bottom Sheet
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private ImageView bottomSheetCancelButton;
    private Button bottomSheetAcceptButton;
    private Button bottomSheetFindNextButton;
    private TextView searchingTextView;
    private View topLinearLayout;
    private View bottomLinearLayout;
    private ArrayList<User> bottomsSheetUserObjList = new ArrayList<>();
    private TextView bottomSheetKenaParkerName;
    private TextView bottomSheetKenaCarColor;
    private TextView bottomSheetKenaCarModel;
    private TextView bottomSheetKenaCarPlateNumber;
    private DatabaseReference peterParkerRef;
    private ValueEventListener peterParkerRefListener;

    @SuppressLint("CutPasteId")
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
        mapGroup = findViewById(R.id.map);

        View bottomSheet = findViewById(R.id.bottom_sheet_maps_main);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        bottomSheet.setVisibility(View.INVISIBLE);

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

        // Current User ID
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        acquireUserProfileAndStoreLocal();

        // Pin Location
        pinLocationButton = (ImageButton) findViewById(R.id.pin_location_button);
        removePinLocationButton = (ImageButton) findViewById(R.id.remove_pin_location_button);
        pinRef = FirebaseDatabase.getInstance().getReference().child("pinnedLocation").child(currentUserID);
        pinRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        pinnedLatitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                        pinnedLongitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                        Log.d(TAG, "onDataChange: pinnedLocation is " + pinnedLatitude + pinnedLongitude);
                        addPinMarker(mMap, pinnedLatitude, pinnedLongitude);
                        locationIsPinned = true;
                        setPinButton();
                    } else {
                        locationIsPinned = false;
                        setPinButton();
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Bottom Sheets View
        bottomSheetCancelButton = (ImageView) findViewById(R.id.bottom_sheet_cancel_button);
        bottomSheetAcceptButton = (Button) findViewById(R.id.bottom_sheet_accept_button);
        bottomSheetFindNextButton = (Button) findViewById(R.id.bottom_sheet_find_next_button);
        searchingTextView = (TextView) findViewById(R.id.searching_text_view);
        topLinearLayout = (View) findViewById(R.id.topLinearLayout);
        bottomLinearLayout = (View) findViewById(R.id.bottomLinearLayout);

        bottomSheetKenaParkerName = (TextView) findViewById(R.id.bottom_sheet_kena_name);
        bottomSheetKenaCarColor = (TextView) findViewById(R.id.bottom_sheet_kena_car_color);
        bottomSheetKenaCarModel = (TextView) findViewById(R.id.bottom_sheet_kena_car_modal);
        bottomSheetKenaCarPlateNumber = (TextView) findViewById(R.id.bottom_sheet_kena_car_plate_number);

        //Fake Buttons
        fakeFindParkingButton = (Button) findViewById(R.id.fake_find_parking_button);
        fakeFindParkingButton.setVisibility(View.INVISIBLE);


        // Recenter Button
        recenterButton = (FloatingActionButton) findViewById(R.id.recenter_button);
        recenterButton.setVisibility(View.INVISIBLE);
        loadingCircle = (ProgressBar) findViewById(R.id.progress_bar);
//        View bottomSheet = findViewById(R.id.bottom_sheet_sorry);
//        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);
        showLoading();
        FirebaseApp.initializeApp(getApplicationContext());
//        parentOperationRef = FirebaseDatabase.getInstance().getReference().child("operation");
        geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        geoFire = new GeoFire(geofireRef);
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");
        liveUsersRef = FirebaseDatabase.getInstance().getReference().child("liveUsers");
        liveUsersGeoFire = new GeoFire(liveUsersRef);
        togetherRef = FirebaseDatabase.getInstance().getReference().child("together");
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
        signOutButton = (Button) findViewById(R.id.sign_out_button);

        setBottomSheetBehavior();

//        setArtificialGeofence();

        fakeFindParkingButton.setOnClickListener(null);

        pinLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                locationIsPinned = true;
                setPinMarker();
            }
        });

        removePinLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                removePinMarker();
            }
        });

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
                shareParkingButton.setOnClickListener(null);
                markerInMiddle = false;
                recenterButton.setVisibility(View.INVISIBLE);
                showLoading();
//                mBottomSheetBehavior.setPeekHeight(200);
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                searchMarkerForKenaOneByOne();

            }
        });

        recenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Recentered Button is Pressed");
                markerInMiddle = true;
                LatLng latlong = new LatLng(peterParkerLocation.latitude, peterParkerLocation.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, DEFAULT_ZOOM));
                CameraPosition cam = CameraPosition.builder()
                        .target(mk.getPosition())
                        .zoom(DEFAULT_ZOOM)
                        .bearing(floatBearing)
                        .tilt(DEFAULT_TILT)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam));
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
                MapsMainActivity.mMap.getUiSettings().setScrollGesturesEnabled(true);
                MapsMainActivity.mMap.getUiSettings().setZoomGesturesEnabled(true);
                setOnMarkerPressable();
                markerInMiddle = true;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(peterParkerLocation, DEFAULT_ZOOM));
                setRecenterButton();
                cancelButton.setVisibility(View.GONE);
                cancelUser();
                reverseExchangeButtons(shareParkingButton, findParkingButton, fakeFindParkingButton);
            }
        });

        bottomSheetAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetFindNextButton.setOnClickListener(null);
                bottomSheetAcceptUser();
            }
        });

        bottomSheetFindNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetAcceptButton.setOnClickListener(null);
                bottomSheetFindNextUser();

            }
        });

        bottomSheetCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setPeekHeight(0);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cancelUser();
            }
        });


        Log.d(TAG, "onCreate: current uid" + currentUserID);

        feedbackObj = new FeedbackModel(getApplicationContext(), this);
        feedbackObj.getLastFeedbackCompleted();

    }


    private void acquireUserProfileAndStoreLocal() {

        StorageReference userRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/profile.txt");
        StorageReference userPointRef = FirebaseStorage.getInstance().getReference().child("users/" + currentUserID + "/points.txt");
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
                } catch (JsonSyntaxException e) {
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
                } catch (NullPointerException e) {

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
                Log.d("Byte", "getByte success");


                try {
                    userObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
                    Log.d("Gson", "Gsonfrom json success");


                    for (int i = 0; i < MapsMainActivity.userObjList.size(); i++) {
                        Log.d("I", "Iteration success");
                        Log.i("Hello", "heyhey" + MapsMainActivity.userObjList.get(i).getUserName().getFirstName());
//                            firstName.setText(userObjList.get(i).getUserName().getFirstName());
                        lastName = (MapsMainActivity.userObjList.get(i).getUserName().getLastName());
                        phoneNum = (MapsMainActivity.userObjList.get(i).getUserName().getPhoneNum());
                        carColour = (MapsMainActivity.userObjList.get(i).getUserCar().getCarColour());
                        carBrand = (MapsMainActivity.userObjList.get(i).getUserCar().getCarBrand());
                        carModel = (MapsMainActivity.userObjList.get(i).getUserCar().getCarModel());
                        carPlate = (MapsMainActivity.userObjList.get(i).getUserCar().getCarPlate());
                        Log.d(TAG, "lastName variable is " + lastName);
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

                        if (menuItem == oldMenuItem) {
                            Log.d(TAG, "menuitem is displayed already");
//                            Log.d("navmenuitem","menu item ischecked and return false");
                            return false;
                        } else {
//                           backStackCount= navFragmentManager.getBackStackEntryCount();
//                            Log.d(TAG, "onNavigationItemSelected: backstack count is " +backStackCount);
                            Log.d(TAG, "menuitem is not displayed");
//                            Log.d("navmenuitem","menu item isnotchecked and setChecked(false) and return true");
                            try {
                                try {
                                    backStackCount = navFragmentManager.getBackStackEntryCount();
                                    Log.d(TAG, "onNavigationItemSelected: backstack count is " + backStackCount);
                                    if (backStackCount != 0) {
                                        navFragmentManager.popBackStack();
                                    }

                                } catch (NullPointerException e) {
                                    Log.d(TAG, "onNavigationItemSelected: NullPoint");
                                }

                            } catch (IllegalStateException e) {
                                Log.d(TAG, "first selected pop");
                            }

                            selectDrawerItem(menuItem);

                            mDrawer.closeDrawer(GravityCompat.START);

                            oldMenuItem = menuItem;
                            return true;

                        }

                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch (menuItem.getItemId()) {
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
                Car car = new Car("Blue", "Toyota", "Harrier", "BJT 2883");
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
        if ((frag != null)) {

            navFragmentManager = getSupportFragmentManager();
            navFragmentManager.beginTransaction()
                    .replace(R.id.nav_view_selection_container, frag)
                    .addToBackStack(null)
                    .commit();
            Log.d(TAG, "executing fragment transaction , added to backstack");
            mapContainer.setVisibility(View.GONE);
            currentFragment = frag;
            Log.d(TAG, "executeFragmentTransaction: currentFragment is " + currentFragment);
        } else {
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
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.setOnCameraMoveStartedListener(this);
        setOnMarkerPressable();
        onPressWhenRedHueIsPresent();
    }

    private void setOnMarkerPressable() {

//        try {
//            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsMainActivity.this));
//        } catch (NullPointerException e) {
//            Log.d(TAG, "onMapReady: setInfoWindowAdapter" + e);
//        }

//        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
//            @Override
//            public void onInfoWindowLongClick(Marker marker) {
//                foundUser = ((MarkerTag) marker.getTag()).getUID();
//                matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM2);
//                Intent intent = new Intent(MapsMainActivity.this, PeterMap.class);
//                startActivity(intent);
//            }
//        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                showLoading();
                exchangeButtons(shareParkingButton, findParkingButton, fakeFindParkingButton);
//                if (marker.equals(mk) || marker.equals(pinMk)) {
//                    return true;
//                } else {
//                    try {
//
//                        if (lastClickedMarker != null) {
//                            if (lastClickedMarker.equals(marker)) {
//                                Log.d(TAG, "onMarkerClick: 2nd marker is clicked but same as first marker " + ((MarkerTag) lastClickedMarker.getTag()).getUID());
//                                marker.showInfoWindow();
//                                lastClickedMarker = marker;
//                            } else {
//                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                                marker.showInfoWindow();
//                                Log.d(TAG, "onMarkerClick: 2nd marker is clicked first marker is " + ((MarkerTag) lastClickedMarker.getTag()).getUID());
//                                String foundUser = ((MarkerTag) lastClickedMarker.getTag()).getUID();
//                                matchmakingRef.child(foundUser).child("peterParker").setValue("NotOccupiedByAnyoneYet");
//                                matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM0);
//                                lastClickedMarker = marker;
//                            }
//                        } else {
//                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                            marker.showInfoWindow();
//                            lastClickedMarker = marker;
//                        }
                foundUser = ((MarkerTag) marker.getTag()).getUID();
                Log.d(TAG, "onMarkerClick: foundUser is " + foundUser);
//                        Log.d("founduser", " fu is :  " + foundUser);
//                        matchmakingRef.child(foundUser).child("peterParker").setValue(currentUserID);
//                        matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM1);
//                    } catch (NullPointerException e) {
//
//                    }
//                }
                try {
                    matchmakingRef.child(foundUser).child("peterParker").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String a = dataSnapshot.getValue().toString();
                            if (a.equals("NotOccupiedByAnyoneYet")) {
                                try {
                                    markerInMiddle = false;
                                    Marker marker = mStringMarkerMap.get(foundUser);
                                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                    Log.d(TAG, "searchMarkerForKenaOneByOne: foundUser is " + foundUser);
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_ZOOM));
                                    matchmakingRef.child(foundUser).child("peterParker").setValue(currentUserID);
                                    matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM1);
                                    String url = getRequestUrl(mk.getPosition(), marker.getPosition());
                                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                                    taskRequestDirections.execute(url);

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "onDataChange: showloading keep loading");

                                }
                            } else {
//                            newArrayList.remove(foundUser);
//                            searchMarkerForKenaOneByOne();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (NullPointerException e) {
                    Log.d(TAG, "onDataChange: showloading keep loading 1");
                    dismissLoading();
                    reverseExchangeButtons(shareParkingButton,findParkingButton,fakeFindParkingButton);
                }
                return false;
            }
        });
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission");
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        if (mLastLocation != null) {
//            latitude = mLastLocation.getLatitude();
//            longitude = mLastLocation.getLongitude();


            liveUsersGeoFire.setLocation(currentUserID, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully on liveUsersRef as lat[" + latitude + "], lon[" + longitude + "]!");
                        loadLocationForThisUser();
                        findOtherUsersLocation();
                    }
                }
            });
            //Update to firebase
//            geoFire.setLocation(currentUserID, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
//                @Override
//                public void onComplete(String key, DatabaseError error) {
//                    if (error != null) {
//                        System.err.println("There was an error saving the location to GeoFire: " + error);
//                    } else {
//                        System.out.println("Location saved on server successfully as lat[" + latitude + "], lon[" + longitude + "]!");
//                        loadLocationForThisUser();
//                        acquireUserProfileAndStoreLocal();
//                        dismissLoading();
//                    }
//                }
//            });
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
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        if (isBetterLocation(location, mLastLocation)) {
            Log.d(TAG, "onLocationChanged: is triggered");
            mLastLocation = location;
            if (mLastLocation.hasAccuracy() && mLastLocation.getAccuracy() <= 100) {
                Log.d(TAG, "onLocationChanged: is triggered, mLastLocation has Accuracy");
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                if (markerInMiddle) {
                    updateCameraBearing(mMap, peterParkerLocation.latitude, peterParkerLocation.longitude, new LatLng(latitude, longitude));
                }
                displayLocation();
            }
        } else {
            Log.d(TAG, "onLocationChanged: is triggered, mLastLocation cannot be relied");
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            if (markerInMiddle) {
                updateCameraBearing(mMap, peterParkerLocation.latitude, peterParkerLocation.longitude, new LatLng(latitude, longitude));
            }
            displayLocation();
        }

    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            Log.d(TAG, "isBetterLocation: currentBestLocation is null, return true");
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        Log.d(TAG, "isBetterLocation: location.getTime is " + location.getTime());
        Log.d(TAG, "isBetterLocation: currentBestLocation.getTime is " + currentBestLocation.getTime());
        Log.d(TAG, "isBetterLocation: timeDelta is " + timeDelta);
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            Log.d(TAG, "isBetterLocation: isSignificantyNewer, return true");
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            Log.d(TAG, "isBetterLocation: isSignificantyOlder, return false");
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        Log.d(TAG, "isBetterLocation: location.getAccuracy is " + location.getAccuracy());
        Log.d(TAG, "isBetterLocation: currentBestLocation.getAccuracy is " + currentBestLocation.getAccuracy());
        Log.d(TAG, "isBetterLocation: accuracyDelta is " + accuracyDelta);
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());
        Log.d(TAG, "isBetterLocation: is it from same provider? It is " + isFromSameProvider);

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            Log.d(TAG, "isBetterLocation: isMoreAccurate, return true");
            return true;
        } else if (isNewer && !isLessAccurate) {
            Log.d(TAG, "isBetterLocation: isNewer and !isLessAccurate, return true");
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            Log.d(TAG, "isBetterLocation: isNewer and !isSignifivantlyLessAccurate and isFromSameProvider, return true");
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        setPinButton();
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
        if (this.position == 124) {
            findOtherUsersLocation();
            this.position = 0;
        }
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

        if (foundUser != null) {
            try {
                matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM0);
                matchmakingRef.child(foundUser).child("peterParker").setValue("NotOccupiedByAnyoneYet");
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d(TAG, "onDataChange: showloading keep loading 2");
            }
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

    private void setArtificialGeofence() {
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
                                findOtherUsersLocation();
                                shareParkingButton.setVisibility(View.VISIBLE);
                                findParkingButton.setVisibility(View.VISIBLE);
//                                mBottomSheetBehavior.setHideable(true);
//                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
//                mBottomSheetBehavior.setHideable(false);
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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

        liveUsersRef.child(currentUserID).child("l").addValueEventListener(new ValueEventListener() {
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
                dismissLoading();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    //Share Parking Pressed will set adatem and sessionKey in database
    private void setMatchkingFolderInDatabse() {

        matchmakingRef.child(currentUserID + "/sessionKey").setValue(currentUserID);
        matchmakingRef.child(currentUserID + "/peterParker").setValue("NotOccupiedByAnyoneYet");
        matchmakingRef.child(currentUserID + "/adatem").setValue(ADATEM0);
    }

    //FIND OTHER USERS LOCATION
    private void findOtherUsersLocation() {
        geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 100.0);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {

//                    final StorageReference getKenaProfileRef = storageRef.child("users/" + key + "/profile.txt");
//                    getKenaProfileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                        @Override
//                        public void onSuccess(byte[] bytes) {
//
////                            try {
////                                kenaUserObjList.put(key, new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
////                                Log.d(TAG, "Gsonfrom json success");
////                                Log.d(TAG, "onSuccess: kenaUserObjList has " + kenaUserObjList);
////
////                                kenaParkerName = kenaUserObjList.get(key).getUserName().getFirstName() + kenaUserObjList.get(key).getUserName().getLastName();
////                                kenaCarModel = kenaUserObjList.get(key).getUserCar().getCarBrand() + kenaUserObjList.get(key).getUserCar().getCarModel();
////                                kenaCarPlateNumber = kenaUserObjList.get(key).getUserCar().getCarPlate();
////                                kenaCarColor = kenaUserObjList.get(key).getUserCar().getCarColour();
////
////                                Log.d(TAG, "onSuccess: kenaParkerName is " + kenaParkerName);
////                                Log.d(TAG, "onSuccess: kenaCarDetails is " + kenaCarColor + " " + kenaCarModel + " (" + kenaCarPlateNumber + ") ");
////
////
////                            } catch (UnsupportedEncodingException e) {
////                                e.printStackTrace();
////                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            Log.d(TAG, "fragment is not created, exception: " + exception);
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
//                        @Override
//                        public void onComplete(@NonNull Task<byte[]> task) {
                final MarkerTag yourMarkerTag = new MarkerTag();
                yourMarkerTag.setUID(key);
//                            if (!kenaParkerName.equals("")) {
//                                yourMarkerTag.setKenaParkerName(kenaParkerName);
//                            }
//                            if (!kenaCarColor.equals("") || !kenaCarModel.equals("") || !kenaCarPlateNumber.equals(""))
//                                yourMarkerTag.setKenaParkerCarDetails(kenaCarColor + " " + kenaCarModel + " (" + kenaCarPlateNumber + ") ");

                if (!key.equals(currentUserID)) {
                    matchmakingRef.child(key).child("adatem").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                String adatemValue = dataSnapshot.getValue().toString();
                                if (adatemValue.equals(ADATEM0)) {
                                    if (newArrayList.contains(key)) {
                                        newArrayList.remove(key);
                                        mStringMarkerMap.remove(key);

                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(location.latitude, location.longitude))
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                                        mStringMarkerMap.put(key, marker);
                                        marker.setTag(yourMarkerTag);
                                        kenaMarkerArrayList.add(marker);
                                        for (int i = 0; i < kenaMarkerArrayList.size(); i++)
                                            if (!mStringMarkerMap.containsValue(kenaMarkerArrayList.get(i))) {
                                                kenaMarkerArrayList.get(i).remove();
                                                kenaMarkerArrayList.remove(i);
                                            }

                                        // For Search Button To Search
//                                                if (!oldArrayList.contains(key)) {

                                        newArrayList.add(key);
//                                                    newHashSet.addAll(newArrayList);
//                                                    newArrayList.clear();
//                                                    newArrayList.addAll(newHashSet);
//                                                    newHashSet.clear();
                                        Log.d(TAG, "newArrayList consist of " + newArrayList);
                                    } else {
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(location.latitude, location.longitude))
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                                        mStringMarkerMap.put(key, marker);
                                        marker.setTag(yourMarkerTag);
                                        kenaMarkerArrayList.add(marker);
                                        for (int i = 0; i < kenaMarkerArrayList.size(); i++)
                                            if (!mStringMarkerMap.containsValue(kenaMarkerArrayList.get(i))) {
                                                kenaMarkerArrayList.get(i).remove();
                                                kenaMarkerArrayList.remove(i);
                                            }

                                        // For Search Button To Search
//                                                if (!oldArrayList.contains(key)) {

                                        newArrayList.add(key);
//                                                    newHashSet.addAll(newArrayList);
//                                                    newArrayList.clear();
//                                                    newArrayList.addAll(newHashSet);
//                                                    newHashSet.clear();
                                        Log.d(TAG, "newArrayList consist of " + newArrayList);
                                    }
//                                                }

                                } else if (!adatemValue.equals(ADATEM0)) {

                                    if (adatemValue.equals(ADATEM1)) {
                                        matchmakingRef.child(key).child("peterParker").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                try {
                                                    String peterParkerValue = dataSnapshot.getValue().toString();
                                                    if (peterParkerValue.equals(currentUserID)) {
//                                                            Marker marker = mMap.addMarker(new MarkerOptions()
//                                                                    .position(new LatLng(location.latitude, location.longitude))
//                                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                                                            marker.showInfoWindow();
//                                                            mStringMarkerMap.put(key, marker);
//                                                            marker.setTag(yourMarkerTag);
//                                                            kenaMarkerArrayList.add(marker);
//                                                            for (int i = 0; i < kenaMarkerArrayList.size(); i++)
//                                                                if (!mStringMarkerMap.containsValue(kenaMarkerArrayList.get(i))) {
//                                                                    kenaMarkerArrayList.get(i).remove();
//                                                                    kenaMarkerArrayList.remove(i);
//                                                                }
                                                    } else {
                                                        Marker marker = mStringMarkerMap.get(key);
                                                        if (marker != null) {
                                                            marker.remove();
                                                            mStringMarkerMap.remove(key);
                                                            Log.d(TAG, "onDataChange: marker has been removed " + marker);
                                                        }
                                                        if (newArrayList.contains(key)) {
                                                            // For Search Button To Search
                                                            newArrayList.remove(key);
//                                                                        newHashSet.addAll(newArrayList);
//                                                                        newArrayList.clear();
//                                                                        newArrayList.addAll(newHashSet);
//                                                                        newHashSet.clear();
                                                            Log.d(TAG, "newArrayList consist of " + newArrayList);
                                                        }

//                                                                    if (oldArrayList.contains(key)) {
//                                                                        oldArrayList.remove(key);
//                                                                        oldHashSet.addAll(oldArrayList);
//                                                                        oldArrayList.clear();
//                                                                        oldArrayList.addAll(oldHashSet);
//                                                                        oldHashSet.clear();
//                                                                        Log.d(TAG, "oldArrayList consist of " + oldArrayList);
//                                                                    }
                                                    }
                                                } catch (NullPointerException e) {
                                                    e.printStackTrace();
                                                    Log.d(TAG, "onDataChange: showloading keep loading 3");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        Marker marker = mStringMarkerMap.get(key);
                                        if (marker != null) {
                                            marker.remove();
                                            mStringMarkerMap.remove(key);
                                            Log.d(TAG, "onDataChange: marker has been removed " + marker);
                                        }

                                        if (newArrayList.contains(key)) {
                                            // For Search Button To Search
                                            newArrayList.remove(key);
//                                                        newHashSet.addAll(newArrayList);
//                                                        newArrayList.clear();
//                                                        newArrayList.addAll(newHashSet);
//                                                        newHashSet.clear();
                                            Log.d(TAG, "newArrayList consist of " + newArrayList);
                                        }

//                                                    if (oldArrayList.contains(key)) {
//                                                        oldArrayList.remove(key);
//                                                        oldHashSet.addAll(oldArrayList);
//                                                        oldArrayList.clear();
//                                                        oldArrayList.addAll(oldHashSet);
//                                                        oldHashSet.clear();
//                                                        Log.d(TAG, "oldArrayList consist of " + oldArrayList);
//                                                    }
                                    }
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                Log.d(TAG, "onDataChange: showloading keep loading 4");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
//                        }
//                    });
            }


            @Override
            public void onKeyExited(final String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));

                Marker marker = mStringMarkerMap.get(key);
                if (marker != null) {
                    marker.remove();
                    mStringMarkerMap.remove(key);
                    Log.d(TAG, "onKeyExited: marker has been removed " + marker);
                }

                if (newArrayList.contains(key)) {
                    // For Search Button To Search
                    newArrayList.remove(key);
//                    newHashSet.addAll(newArrayList);
//                    newArrayList.clear();
//                    newArrayList.addAll(newHashSet);
//                    newHashSet.clear();
                    Log.d(TAG, "newArrayList consist of " + newArrayList);
                }

//                if (oldArrayList.contains(key)) {
//                    oldArrayList.remove(key);
//                    oldHashSet.addAll(oldArrayList);
//                    oldArrayList.clear();
//                    oldArrayList.addAll(oldHashSet);
//                    oldHashSet.clear();
//                    Log.d(TAG, "oldArrayList consist of " + oldArrayList);
//                }
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


    private void searchMarkerForKenaOneByOne() {
        searchingTextView.setVisibility(View.VISIBLE);
//        mBottomSheetBehavior.setPeekHeight(200);
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

//        topLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                topLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                View hidden = topLinearLayout.getRootView();
//                mBottomSheetBehavior.setPeekHeight(hidden.getBottom());
//            }
//        });

//        fragManager = getSupportFragmentManager();
        try {
            if (!newArrayList.isEmpty()) {
                foundUser = newArrayList.get(0);
                matchmakingRef.child(foundUser).child("adatem").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String adatemValue = dataSnapshot.getValue().toString();
                            if (adatemValue.equals(ADATEM0)) {
                                matchmakingRef.child(foundUser).child("peterParker").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String a = dataSnapshot.getValue().toString();
                                        if (a.equals("NotOccupiedByAnyoneYet")) {
                                            try {
                                                markerInMiddle = false;
                                                Marker marker = mStringMarkerMap.get(foundUser);
                                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                                Log.d(TAG, "searchMarkerForKenaOneByOne: foundUser is " + foundUser);
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_ZOOM));
                                                matchmakingRef.child(foundUser).child("peterParker").setValue(currentUserID);
                                                matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM1);
                                                String url = getRequestUrl(mk.getPosition(), marker.getPosition());
                                                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                                                taskRequestDirections.execute(url);
                                            } catch (NullPointerException e) {
                                                e.printStackTrace();
                                                Log.d(TAG, "onDataChange: showloading keep loading 5");
                                            }
                                        } else {
//                            newArrayList.remove(foundUser);
//                            searchMarkerForKenaOneByOne();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

//            } else if (newArrayList.isEmpty() && !oldArrayList.isEmpty()) {
//                dismissLoading();
////                try {
////                    fragManager.beginTransaction()
////                            .remove(userPopUpFragment)
////                            .commit();
////                } catch (NullPointerException e) {
////
////                }
////                Intent intent = new Intent(MapsMainActivity.this, FinishUserPopUp.class);
////                startActivity(intent);
//                newArrayList.addAll(oldArrayList);
//                newHashSet.addAll(newArrayList);
//                newArrayList.clear();
//                newArrayList.addAll(newHashSet);
//                newHashSet.clear();
//
//                findOtherUsersLocation();
//
//                searchMarkerForKenaOneByOne();

                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onDataChange: showloading keep loading 6");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                dismissLoading();
                Intent intent = new Intent(MapsMainActivity.this, NoUserPopUp.class);
                startActivity(intent);
//                Thread.sleep(5000);
//                searchMarkerForKenaOneByOne();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, "onDataChange: showloading keep loading 6");
        }

    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        //Value of dest
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //Google API key
        String key = "key=" + getString(R.string.map_api_key);
        //Build the full param
        String param = str_org + "&" + str_dest + "&" + key;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        Log.d(TAG, "getRequestUrl: Direction API url is: " + url);
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);

        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(final List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            Log.d(TAG, "displayKenaDetailsOnWindow is called");

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageRef = firebaseStorage.getReference();

            final StorageReference getKenaProfileRef = storageRef.child("users/" + foundUser + "/profile.txt");
            getKenaProfileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {

                    try {
                        bottomsSheetUserObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), User.class));
                        Log.d(TAG, "Gsonfrom json success");

                        bottomSheetKenaParkerName.setText(bottomsSheetUserObjList.get(0).getUserName().getFirstName() + bottomsSheetUserObjList.get(0).getUserName().getLastName());
                        bottomSheetKenaCarModel.setText(bottomsSheetUserObjList.get(0).getUserCar().getCarBrand() + bottomsSheetUserObjList.get(0).getUserCar().getCarModel());
                        bottomSheetKenaCarPlateNumber.setText(bottomsSheetUserObjList.get(0).getUserCar().getCarPlate());
                        bottomSheetKenaCarColor.setText(bottomsSheetUserObjList.get(0).getUserCar().getCarColour());

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "fragment is not created, exception: " + exception);
                }
            }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @Override
                public void onComplete(@NonNull Task<byte[]> task) {
//                listenToDatabase();
                    bottomsSheetUserObjList.clear();
                    ArrayList points = null;
                    PolylineOptions polylineOptions = null;

                    for (List<HashMap<String, String>> path : lists) {
                        points = new ArrayList();
                        polylineOptions = new PolylineOptions();

                        for (HashMap<String, String> point : path) {
                            double lat = Double.parseDouble(point.get("lat"));
                            double lon = Double.parseDouble(point.get("lon"));

                            points.add(new LatLng(lat, lon));
                        }

                        polylineOptions.addAll(points);
                        polylineOptions.width(15);
                        polylineOptions.color(Color.BLUE);
                        polylineOptions.geodesic(true);
                    }

                    if (polylineOptions != null) {
                        Polyline line = mMap.addPolyline(polylineOptions);
                        lineArray.add(line);
                        LatLng firstLatLngPoly = (LatLng) points.get(0);
                        LatLng lastLatLngPoly = (LatLng) points.get(points.size() - 1);

                        double latDelta = firstLatLngPoly.latitude - lastLatLngPoly.latitude;
                        double lonDelta = firstLatLngPoly.longitude - lastLatLngPoly.longitude;

                        double updatedFirstLat;
                        double updatedFirstLon;
                        double updatedLastLat;
                        double updatedLastLon;

                        LatLng updatedFirstLatLngPoly;
                        LatLng updatedLastLatLngPoly;

                        if (latDelta >= 0 && lonDelta >= 0) {
                            Log.d(TAG, "onPostExecute: first quadrant");
                            updatedFirstLat = firstLatLngPoly.latitude;
                            updatedFirstLon = firstLatLngPoly.longitude + 0.005;
                            updatedLastLat = lastLatLngPoly.latitude;
                            updatedLastLon = lastLatLngPoly.longitude - 0.005;
                            updatedFirstLatLngPoly = new LatLng(updatedFirstLat, updatedFirstLon);
                            updatedLastLatLngPoly = new LatLng(updatedLastLat, updatedLastLon);
                            executeFindParkers(updatedFirstLatLngPoly, updatedLastLatLngPoly);
                        } else if (latDelta < 0 && lonDelta >= 0) {
                            Log.d(TAG, "onPostExecute: second quadrant");
                            updatedFirstLat = firstLatLngPoly.latitude;
                            updatedFirstLon = firstLatLngPoly.longitude + 0.005;
                            updatedLastLat = lastLatLngPoly.latitude;
                            updatedLastLon = lastLatLngPoly.longitude - 0.005;
                            updatedFirstLatLngPoly = new LatLng(updatedFirstLat, updatedFirstLon);
                            updatedLastLatLngPoly = new LatLng(updatedLastLat, updatedLastLon);
                            executeFindParkers(updatedFirstLatLngPoly, updatedLastLatLngPoly);
                        } else if (latDelta < 0 && lonDelta < 0) {
                            Log.d(TAG, "onPostExecute: third quadrant");
                            updatedFirstLat = firstLatLngPoly.latitude;
                            updatedFirstLon = firstLatLngPoly.longitude - 0.005;
                            updatedLastLat = lastLatLngPoly.latitude;
                            updatedLastLon = lastLatLngPoly.longitude + 0.005;
                            updatedFirstLatLngPoly = new LatLng(updatedFirstLat, updatedFirstLon);
                            updatedLastLatLngPoly = new LatLng(updatedLastLat, updatedLastLon);
                            executeFindParkers(updatedFirstLatLngPoly, updatedLastLatLngPoly);
                        } else if (latDelta >= 0 && lonDelta < 0) {
                            Log.d(TAG, "onPostExecute: four quadrant");
                            updatedFirstLat = firstLatLngPoly.latitude;
                            updatedFirstLon = firstLatLngPoly.longitude - 0.005;
                            updatedLastLat = lastLatLngPoly.latitude;
                            updatedLastLon = lastLatLngPoly.longitude + 0.005;
                            updatedFirstLatLngPoly = new LatLng(updatedFirstLat, updatedFirstLon);
                            updatedLastLatLngPoly = new LatLng(updatedLastLat, updatedLastLon);
                            executeFindParkers(updatedFirstLatLngPoly, updatedLastLatLngPoly);
                        }

                    } else {
                        Log.d(TAG, "onPostExecute: polylineOptions Directions not found! ");
                    }
                }

            });
        }
    }

    private void executeFindParkers(LatLng org, LatLng dest) {
        bottomSheetListenToDatabase();

        bottomSheetAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetFindNextButton.setOnClickListener(null);
                bottomSheetAcceptUser();
            }
        });


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(org);
        builder.include(dest);
        // Add your locations to bounds using builder.include, maybe in a loop
        LatLngBounds bounds = builder.build();
        //Then construct a cameraUpdate
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        //Then move the camera
        mMap.animateCamera(cameraUpdate);
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((LatLng)points.get(points.size()/2),DEFAULT_ZOOM+10f));
        exchangeButtons(shareParkingButton, findParkingButton, fakeFindParkingButton);
//        if (userPopUpFragment instanceof UserPopUpFragment) {
//            userPopUpFragment = new UserPopUpFragment();
//        } else {
//            userPopUpFragment = new UserPopUpFragment();
//        }
//
//        fragManager.beginTransaction()
//                .replace(R.id.popupuser, userPopUpFragment, "userPopUpFragment")
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//                .show(userPopUpFragment)
//                .commit();

        backgroundNotClickable();
        searchingTextView.setVisibility(View.INVISIBLE);
//        mBottomSheetBehavior.setPeekHeight(bottomLinearLayout.getBottom());

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setPeekHeight(0);
        dismissLoading();
    }


    private void setKenaMarker(LatLng thisLocation) {

        kenaMarker = mMap.addMarker(new MarkerOptions()
                .position(thisLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(thisLocation, DEFAULT_ZOOM, DEFAULT_TILT, 0)));
        Log.d(TAG, "kenaMarker is set!");
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

        final LatLng latlong = new LatLng(lat, lon);
        if (markerCount == 1) {
//            String url = getRequestMarkerUrl(latlong);
//            Log.d(TAG, "addMarker: url is " + url);
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.get(url, new JsonHttpResponseHandler() {
//                LatLng routes;
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    Log.d(TAG, "onSuccess: JSONObject " + response);
//                    try {
//                        RoadsParser roadsParser = RoadsParser.fromJson(response);
//                        routes = roadsParser.getLatLng();
//                        Log.d(TAG, "onSuccess: routes is " + routes);
//                        if (markerInMiddle) {
//                            updateCameraBearing(mMap, mk.getPosition().latitude, mk.getPosition().longitude, new LatLng(routes.latitude, routes.longitude));
//                        }
            animateMarker(lat, lon, mk);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                    Log.d(TAG, "onFailure: Status Code: " + statusCode);
//                    animateMarker(latlong.latitude, latlong.longitude, mk);
//                }

//            });
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, DEFAULT_ZOOM));
//            updateCameraBearing(googleMap, lat, lon,latlong);
        } else if (markerCount == 0) {
//            Set Custom BitMap for Pointer
//            String url = getRequestMarkerUrl(latlong);
//            TaskRequestMarkerDirections taskRequestMarkerDirections = new TaskRequestMarkerDirections();
//            taskRequestMarkerDirections.execute(url);

//            latalonga = taskRequestMarkerDirections.getLatLng();
//            Log.d(TAG, "addMarker: latalonga is " + latalonga);
            int height = 70;
            int width = 70;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.gps_arrow_icon);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            mk = mMap.addMarker(new MarkerOptions().position(latlong)
                    .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlong, DEFAULT_ZOOM, DEFAULT_TILT, 0)));

            markerCount = 1;
            markerInMiddle = true;
            final MarkerTag yourMarkerTag = new MarkerTag();
            yourMarkerTag.setUID(null);
            mk.setTag(yourMarkerTag);


            if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            startLocationUpdates();
        }
    }

    public static Marker pinMk = null;

    // Add A Human Pointer To The MAp
    public void addPinMarker(GoogleMap googleMap, double lat, double lon) {

        LatLng latlong = new LatLng(lat, lon);

        //Set Custom BitMap for Pointer
        int height = 80;
        int width = 45;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.icon_car);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        mMap = googleMap;

        pinMk = mMap.addMarker(new MarkerOptions().position(latlong)
                .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlong, DEFAULT_ZOOM, DEFAULT_TILT, 0)));
        final MarkerTag yourPinMarkerTag = new MarkerTag();
        yourPinMarkerTag.setUID(null);
        pinMk.setTag(yourPinMarkerTag);
    }

    private String getRequestMarkerUrl(LatLng markerLocation) {
        //Value of marker location
        String point = "points=" + markerLocation.latitude + "," + markerLocation.longitude;
        //Google API key
        String key = "key=" + getString(R.string.map_api_key);
        //Create url to request
        String url = "https://roads.googleapis.com/v1/nearestRoads?" + point + "&" + key;
        Log.d(TAG, "getRequestMarkerUrl: Direction API url is: " + url);
        return url;
    }

//    @SuppressLint("StaticFieldLeak")
//    public class TaskRequestMarkerDirections extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String responseString = "";
//            try {
//                responseString = requestDirection(strings[0]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Log.d(TAG, "doInBackground: Success! JSON is " + responseString);
//            return responseString;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            //Parse json here
//            TaskParserMarker taskParser = new TaskParserMarker();
//            taskParser.execute(s);
//
//        }
//    }
//
//    public class TaskParserMarker extends AsyncTask<String, Void, LatLng> {
//        @Override
//        protected LatLng doInBackground(String... strings) {
//            JSONObject jsonObject = null;
//            LatLng routes = null;
//            try {
//                jsonObject = new JSONObject(strings[0]);
//                RoadsParser roadsParser = RoadsParser.fromJson(jsonObject);
//                routes = roadsParser.getLatLng();
////                Log.d(TAG, "doInBackground: JSON do in background is " + jsonObject);
////                String gsonLatLng = (new Gson().fromJson(jsonObject, LatLng.class));
////                RoadsParser roadsParser = new RoadsParser();
////                routes.add((List<HashMap<Double, Double>>) roadsParser.parse(jsonObject));
//                Log.d(TAG, "doInBackground: routes are " + routes);
//                return routes;
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return null;
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(LatLng latLng) {
//            Log.d(TAG, "onPostExecute: latlong to plot own marker is " + latLng);
//            executePlotOwnMarker(latLng);
//
//        }
//    }

//    private void executePlotOwnMarker(LatLng latlong) {
////        LatLng latlong = new LatLng(lat, lon);
//        if (markerCount == 1) {
//            animateMarker(latlong.latitude, latlong.longitude, mk);
////            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, DEFAULT_ZOOM));
////            updateCameraBearing(googleMap, lat, lon,latlong);
//        } else if (markerCount == 0) {
//            //Set Custom BitMap for Pointer
//
////            latalonga = taskRequestMarkerDirections.getLatLng();
////            Log.d(TAG, "addMarker: latalonga is " + latalonga);
//            int height = 70;
//            int width = 70;
//            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.gps_arrow_icon);
//            Bitmap b = bitmapdraw.getBitmap();
//            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//            mk = mMap.addMarker(new MarkerOptions().position(latlong)
//                    .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlong, DEFAULT_ZOOM, DEFAULT_TILT, 0)));
//
//            markerCount = 1;
//            markerInMiddle = true;
//            final MarkerTag yourMarkerTag = new MarkerTag();
//            yourMarkerTag.setUID(null);
//            mk.setTag(yourMarkerTag);
//
//
//            if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                return;
//            }
//            startLocationUpdates();
//        }
//    }

    public static void animateMarker(final double lat, final double lon, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(lat, lon);

            final float startRotation = marker.getRotation();
            final Location destination = new Location("");
            destination.setLatitude(lat);
            destination.setLongitude(lon);

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

    double bearing;
    float floatBearing;

    private void updateCameraBearing(GoogleMap googleMap, double lat, double lng, LatLng latLng) {
        Log.d(TAG, "updateCameraBearing: is called");
        Log.d(TAG, "updateCameraBearing: new lat is " + lat + " while old latitude is " + latLng.latitude);
        Log.d(TAG, "updateCameraBearing: new lng is " + lng + " while old longitude is " + latLng.longitude);
        if (googleMap == null) return;
        Location oldLoc = new Location("");
        oldLoc.setLatitude(lat);
        oldLoc.setLongitude(lng);

        Location newLoc = new Location("");
        newLoc.setLatitude(latLng.latitude);
        newLoc.setLongitude(latLng.longitude);

        double xAxisDiff = latLng.longitude - lng;
        double yAxisDiff = latLng.latitude - lat;

        Log.d(TAG, "updateCameraBearing: xAxisDiff is " + xAxisDiff);
        Log.d(TAG, "updateCameraBearing: yAxisDiff is " + yAxisDiff);

        bearing = oldLoc.bearingTo(newLoc);
        floatBearing = (float) bearing;
        Log.d(TAG, "updateCameraBearing: bearing is " + floatBearing);
        CameraPosition cam = CameraPosition.builder()
                .target(latLng)
                .zoom(DEFAULT_ZOOM)
                .bearing(floatBearing)
                .tilt(DEFAULT_TILT)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam));

    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            markerInMiddle = false;
            Log.d(TAG, "markerInMiddle is " + markerInMiddle);
            setRecenterButton();
        }
    }

    public static int position = 0;

//    @Override
//    public void onArticleSelected(int position) {
//        this.position = position;
//        if (this.position == 123) {
//            Log.d(TAG, "this position is really " + position);
//            showLoading();
//            searchMarkerForKenaOneByOne();
//            this.position = 0;
//        } else {
//            // do nothing
//            Log.d(TAG, "this position is " + this.position);
//        }
//
//    }

    @Override
    public void onLastFeedbackCompleted(boolean isCompleted, Car car) {
        if (!isCompleted) {
            FeedbackDialog dialog = new FeedbackDialog();
            dialog.setArguments(dialog.setCarDetails(car));
            dialog.show(getSupportFragmentManager(), "");
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();


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

    @Override
    public void onBackPressed() {
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (fragment != null) {


            Log.d("backpress", "Back Pressed when fragment is " + fragment);
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
            fragment = null;
            oldMenuItem = null;


            mDrawer.closeDrawer(GravityCompat.START);

        }
    }

    private void cancelUser() {

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
                shareParkingButton.setOnClickListener(null);
                markerInMiddle = false;
                recenterButton.setVisibility(View.INVISIBLE);
                showLoading();
                mBottomSheetBehavior.setPeekHeight(200);
                searchMarkerForKenaOneByOne();

            }
        });
        cancelBottomSheetListenToDatabase();
        MapsMainActivity.mMap.getUiSettings().setScrollGesturesEnabled(true);
        MapsMainActivity.mMap.getUiSettings().setZoomGesturesEnabled(true);
        bottomSheetKenaParkerName.setText(null);
        bottomSheetKenaCarColor.setText(null);
        bottomSheetKenaCarModel.setText(null);
        bottomSheetKenaCarPlateNumber.setText(null);
        setOnMarkerPressable();
        markerInMiddle = true;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(peterParkerLocation, DEFAULT_ZOOM));
        setRecenterButton();
        try {
            matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM0);
            matchmakingRef.child(foundUser).child("peterParker").setValue("NotOccupiedByAnyoneYet");

//            fragManager.popBackStack();
//            FragmentTransaction transaction = fragManager.beginTransaction();
//            transaction.remove(MapsMainActivity.userPopUpFragment);
            Log.d(TAG, "userPopUpFragment is removed");
            Log.d(TAG, "userPopUpFragment and userPopUpFragment1 is removed");
//            mMap.clear();
//            displayLocation();
            findOtherUsersLocation();
//            transaction.commit();
            for (Polyline line : lineArray) {
                line.remove();
            }
            lineArray.clear();
            mBottomSheetBehavior.setPeekHeight(0);
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } catch (NullPointerException e) {

        }
        reverseExchangeButtons(shareParkingButton, findParkingButton, fakeFindParkingButton);
        foundUser = null;
    }

    private void exchangeButtons(Button btn1, Button btn2, Button btn3) {

//        cancelButton.setVisibility(View.VISIBLE);
//        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
//        cancelButton.startAnimation(animZoomIn);

        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        pinLocationButton.setVisibility(View.INVISIBLE);

        ObjectAnimator animationBtn1 = ObjectAnimator.ofFloat(btn1, "translationX", 250f);
        animationBtn1.setDuration(500);


        ObjectAnimator animationBtn2 = ObjectAnimator.ofFloat(btn2, "translationX", -250f);
        animationBtn2.setDuration(500);

        ObjectAnimator animationBtn3 = ObjectAnimator.ofFloat(btn3, "translationX", -250f);
        animationBtn3.setDuration(500);

        animationBtn1.start();
        animationBtn2.start();
        animationBtn3.start();

    }

    public void reverseExchangeButtons(Button btn1, Button btn2, Button btn3) {
        ObjectAnimator animationBtn1 = ObjectAnimator.ofFloat(btn1, "translationX", -15f);
        animationBtn1.setDuration(500);
        animationBtn1.start();

        ObjectAnimator animationBtn2 = ObjectAnimator.ofFloat(btn2, "translationX", 15f);
        animationBtn2.setDuration(500);
        animationBtn2.start();

        ObjectAnimator animationBtn3 = ObjectAnimator.ofFloat(btn3, "translationX", 15f);
        animationBtn3.setDuration(500);
        animationBtn3.start();

        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        pinLocationButton.setVisibility(View.VISIBLE);

        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setAnimation(null);
    }

    private void onPressWhenRedHueIsPresent() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    if (lastClickedMarker != null) {
                        String foundUser = ((MarkerTag) lastClickedMarker.getTag()).getUID();
                        matchmakingRef.child(foundUser).child("peterParker").setValue("NotOccupiedByAnyoneYet");
                        matchmakingRef.child(foundUser).child("adatem").setValue(ADATEM0);
                        lastClickedMarker = null;

                    } else {

                    }
                } catch (NullPointerException e) {

                }
                foundUser = null;
            }
        });
    }

    private void setPinMarker() {
        pinRef.child("latitude").setValue(peterParkerLocation.latitude);
        pinRef.child("longitude").setValue(peterParkerLocation.longitude);
        pinRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        pinnedLatitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                        pinnedLongitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                        Log.d(TAG, "onDataChange: pinnedLocation is " + pinnedLatitude + pinnedLongitude);
                        addPinMarker(mMap, pinnedLatitude, pinnedLongitude);
                        locationIsPinned = true;
                        setPinButton();
                    } else {
                        locationIsPinned = false;
                        setPinButton();
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addPinMarker(mMap, peterParkerLocation.latitude, peterParkerLocation.longitude);
        setPinButton();
        dismissLoading();
    }

    private void removePinMarker() {
        DecideWantToRemovePinDialog removePinDialog = new DecideWantToRemovePinDialog();
        removePinDialog.show(getSupportFragmentManager(), "Remove Pin Alert Dialog");
        dismissLoading();
    }

    public static void setPinButton() {
        if (locationIsPinned) {
            pinLocationButton.setVisibility(View.INVISIBLE);
            removePinLocationButton.setVisibility(View.VISIBLE);
            fakeFindParkingButton.setVisibility(View.VISIBLE);
        } else {
            pinLocationButton.setVisibility(View.VISIBLE);
            removePinLocationButton.setVisibility(View.INVISIBLE);
            fakeFindParkingButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setBottomSheetBehavior() {
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (mBottomSheetBehavior.getPeekHeight() == 200) {
                        } else {
                            cancelUser();
                        }
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (mBottomSheetBehavior.getPeekHeight() == 200) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

    }

    public void backgroundNotClickable() {
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.setInfoWindowAdapter(null);
        mMap.setOnInfoWindowLongClickListener(null);
        mMap.setOnMarkerClickListener(null);
        mMap.setOnMapClickListener(null);

    }

    private void bottomSheetAcceptUser() {
        cancelBottomSheetListenToDatabase();
        matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM2);
        togetherRef.child(foundUser).child("peter").setValue(MapsMainActivity.currentUserID);

        bottomSheetFindNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetAcceptButton.setOnClickListener(null);
                bottomSheetFindNextUser();

            }
        });

        reverseExchangeButtons(shareParkingButton, findParkingButton, fakeFindParkingButton);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        Intent intent = new Intent(this, PeterMap.class);
        startActivity(intent);

    }

    private void bottomSheetFindNextUser() {
        try {
            cancelBottomSheetListenToDatabase();

            mBottomSheetBehavior.setPeekHeight(200);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            Log.d(TAG, "bottomSheetFindNextUser: bottom sheet height is " + mBottomSheetBehavior.getPeekHeight());


            bottomSheetKenaParkerName.setText(null);
            bottomSheetKenaCarColor.setText(null);
            bottomSheetKenaCarModel.setText(null);
            bottomSheetKenaCarPlateNumber.setText(null);

            for (Polyline line : lineArray) {
                line.remove();
            }
            lineArray.clear();
            matchmakingRef.child(foundUser).child("adatem").setValue(MapsMainActivity.ADATEM0);
            matchmakingRef.child(foundUser).child("peterParker").setValue("NotOccupiedByAnyoneYet");
            Log.i(TAG, "MapsMainActivity oldArraylist" + MapsMainActivity.oldArrayList);
            newArrayList.remove(foundUser);
            findOtherUsersLocation();
            searchMarkerForKenaOneByOne();
        } catch (NullPointerException e) {

        }
    }

    private void bottomSheetListenToDatabase() {
        try {

            peterParkerRef = matchmakingRef.child(foundUser).child("peterParker");

            peterParkerRefListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        final String peterParker = dataSnapshot.getValue().toString();
                        if (!peterParker.equals(currentUserID)) {
                            mBottomSheetBehavior.setPeekHeight(200);
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            Log.d(TAG, "bottomSheetFindNextUser: bottom sheet height is " + mBottomSheetBehavior.getPeekHeight());


                            bottomSheetKenaParkerName.setText(null);
                            bottomSheetKenaCarColor.setText(null);
                            bottomSheetKenaCarModel.setText(null);
                            bottomSheetKenaCarPlateNumber.setText(null);

                            for (Polyline line : lineArray) {
                                line.remove();
                            }
                            lineArray.clear();
                            newArrayList.remove(foundUser);
                            findOtherUsersLocation();
                            searchMarkerForKenaOneByOne();
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        mBottomSheetBehavior.setPeekHeight(200);
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Log.d(TAG, "bottomSheetFindNextUser: bottom sheet height is " + mBottomSheetBehavior.getPeekHeight());


                        bottomSheetKenaParkerName.setText(null);
                        bottomSheetKenaCarColor.setText(null);
                        bottomSheetKenaCarModel.setText(null);
                        bottomSheetKenaCarPlateNumber.setText(null);

                        for (Polyline line : lineArray) {
                            line.remove();
                        }
                        lineArray.clear();
                        newArrayList.remove(foundUser);
                        cancelBottomSheetListenToDatabase();
                        findOtherUsersLocation();
                        searchMarkerForKenaOneByOne();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }

            ;
            peterParkerRef.addValueEventListener(peterParkerRefListener);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void cancelBottomSheetListenToDatabase() {
        peterParkerRef.removeEventListener(peterParkerRefListener);
    }

}