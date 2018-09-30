package com.opark.opark;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;


public class DrawerActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private BubblesManager bubblesManager;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            ChatHeadService.LocalBinder binder = (ChatHeadService.LocalBinder) service;
//            chatHeadService = binder.getService();
//            bound = true;
//            chatHeadService.minimize();
//                chatHeadService.addChatHead();
//                Log.i("ChatHead", "Called");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
//    private ChatHeadService chatHeadService ;
    private boolean bound;

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
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button shareParkingButton = (Button) findViewById(R.id.share_parking_button);
        Button findParkingButton = (Button) findViewById(R.id.find_parking_button);
        ImageButton mapButton = (ImageButton) findViewById(R.id.toMapButton);
        FirebaseApp.initializeApp(getApplicationContext());
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference geofireRef = FirebaseDatabase.getInstance().getReference().child("geofire");
        geoFire = new GeoFire(geofireRef);
        matchmakingRef = FirebaseDatabase.getInstance().getReference().child("matchmaking");

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
                findOtherUsersLocation();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if (!Settings.canDrawOverlays(getApplicationContext())) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, 100);
        } else {
            startChatHeadService();
//            initializeBubblesManager();
        }




        //add this line to display menu1 when the activity is loaded
        //displaySelectedScreen(R.id.nav_first_fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == 100) {
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
//                initializeBubblesManager();
                startChatHeadService();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        bubblesManager.recycle();
        unbindService(mConnection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            addNewBubble();
            if (bound) {
//                chatHeadService.addChatHead();
                Log.i("ChatHead", "Called");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_first_fragment:
//                fragment = new ProfileNavFragment();
                break;
//            case R.id.nav_second_fragment:
//                fragment = new Menu2();
//                break;
//            case R.id.nav_third_fragment:
//                fragment = new Menu3();
//                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }

//    private void addNewBubble() {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        BubbleLayout bubbleView = (BubbleLayout) inflater.inflate(R.layout.bubble_layout, null);
//        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
//            @Override
//            public void onBubbleRemoved(BubbleLayout bubble) { }
//        });
//        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
//
//            @Override
//            public void onBubbleClick(BubbleLayout bubble) {
//                Toast.makeText(getApplicationContext(), "Clicked !",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        bubbleView.setShouldStickToWall(true);
//        bubblesManager.addBubble(bubbleView, 60, 20);
//    }

    private void startChatHeadService() {
//        Intent intent = new Intent(this, ChatHeadService.class);
//        startService(intent);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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

    //FIND OTHER USERS LOCATION
    private void findOtherUsersLocation(){

        //UNABLE TO WORK

        geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 1.0);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
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

        //GET SINGLE LOCATION EXISTED IN FIREBASE

//        geoFire.getLocation("firebase-hq", new LocationCallback() {
//            @Override
//            public void onLocationResult(String key, GeoLocation location) {
//                if (location != null) {
//                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
//                } else {
//                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.err.println("There was an error getting the GeoFire location: " + databaseError);
//            }
//        });
    }

    private void setMatchkingFolderInDatabse(){

        matchmakingRef.child(currentUserID+"/Adatem").setValue(0);
        String key = matchmakingRef.child(currentUserID+"/SessionKey").push().getKey();
        matchmakingRef.child(currentUserID+"/SessionKey").setValue(key);
    }


}

