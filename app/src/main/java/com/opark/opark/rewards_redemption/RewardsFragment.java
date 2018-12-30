package com.opark.opark.rewards_redemption;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.opark.opark.AllOfferFragment;
import com.opark.opark.InsufficientPointsDialog;
import com.opark.opark.R;
import com.opark.opark.SearchViewLinLay;
import com.opark.opark.TabLayoutPagerAdapter;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.share_parking.MapsMainActivity;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RewardsFragment extends Fragment implements MerchantOfferAdapter.ButtonClicked, TextWatcher {
    private static final String TAG = "RewardsFragment";
    private List<MerchantOffer> merchantOffer = new ArrayList<>();
    MerchantOffer thisMerchantOffer = new MerchantOffer();
    private DatabaseReference offerlistDatabaseRef;
    private DatabaseReference rewardsDatabaseRef;

    public static StorageReference userPointsStorageRef;
    public static StorageReference rewardsRedemptionRecord;

    public static ConfirmPreRedeem confirmPreRedeem ;

    private FirebaseAuth mAuth;
    private FirebaseUser thisUser;
    public static String redeemUid;
    public static String merchantName;
    public static String merchantOfferTitle;
    public static String merchantAddress;
    public static String merchantOfferImageUrl;
    static final long ONE_MEGABYTE = 1024 * 1024;
    private static int userPoints;
    private static int userPointsBefRed;
    private static int pointsAfterRedemption;
    public static int redeemCost;
    public static String merchantContact;
    public static String rewardsMerchant;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public RecyclerView merchantRecView;


    private SearchViewLinLay searchViewLinLay;
    private SearchViewLinLay searchViewLinLayTop;
    private static Context context;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private boolean isHideToolbarView = false;
    private EditText editTextSearch;
    private boolean editTextSearchIsExpanded;
    private EditText editTextSearchBig;
    boolean isCollapsed = false;
    public static FragmentManager rewardsFragmentManager;
    private TabLayout tabLayout;

    private ViewPager mViewPager;



    TextView expanding;
    private int expWidth;
    LinearLayout expandingLayout;

    public static MerchantOfferAdapter merchantOfferAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_offers_recycler_view,container,false);

        RewardsFragment.context = getContext();
//try {


        editTextSearchIsExpanded=false;

        bindViews(view);



//        getActivity().setSupportActionBar(mToolbar);
//        (getActivity()).getActionBar();





//        searchViewLinLay = view.findViewById(R.id.float_header_view);
//        searchViewLinLayTop =view.findViewById(R.id.toolbar_header_view);


//        TextView testText = view.findViewById(R.id.test_text);


        coordinatorLayout.measure(0,0);
        mToolbar.measure(0,0);

        expWidth = coordinatorLayout.getMeasuredWidth();
        Log.d("exp", "expw " + expWidth);

//

        editTextSearchBig.addTextChangedListener(this);
        editTextSearch.addTextChangedListener(this);




//        editTextSearch = searchViewLinLay.findViewById(R.id.search_textview);
//
//
//        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (editTextSearch.hasFocus()) {
//
//                    Log.d(TAG, "onFocusChange:  has focus");
//                appBarLayout.setExpanded(false);
//                collapsingToolbarLayout.offsetTopAndBottom(0);
//                }
//            }
//        });
//        testText.setClickable(true);
//
//        testText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                appBarLayout.setExpanded(false);
////                animateSlideLayout();
//
////                collapsingToolbarLayout.offsetTopAndBottom(0);
//            }
//        });





appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
    @Override
    public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {

        float maxScrollDistance=appBarLayout.getTotalScrollRange();

        float verticalOffsetFloat = (float) verticalOffset;

        float verticalOffsetTemp = verticalOffsetFloat;

        float collapseThreshold = maxScrollDistance*15/100;

        if (verticalOffsetFloat < (-maxScrollDistance) / 2 && isCollapsed && verticalOffsetFloat!=0){

                expandToolbar();

//            appBarLayout.setExpanded(false);

            Log.d("offset", "isCollapsed" + isCollapsed + "Vertical OffSet =" +verticalOffset + "\nmaxScrollD =" + maxScrollDistance);
            Log.d("offset", "onOffsetChanged: Collapsing");

//            appBarLayout.setExpanded(true);
//            appBarLayout.offsetTopAndBottom(0);

//            verticalOffsetFloat = 0;




        }
//
//        else if (verticalOffset <- maxScrollDistance / 2 ){
//
//            Log.d("offset", "onOffsetChanged: Vertical OffSet full" +verticalOffset + "maxScrollD =" + maxScrollDistance);
//            Log.d("offset", "onOffsetChanged: Expanding");
//
//            appBarLayout.setExpanded(true);
//            appBarLayout.offsetTopAndBottom(0);
//
//            isCollapsed = false;
//        }




        Log.d("offset", "VertOffsetNow:  " + verticalOffset + " max Scroll " + maxScrollDistance);
        if (verticalOffsetFloat==0&& editTextSearchIsExpanded){
            Log.d("offset", "onOffsetChanged: Vertical OffSet full" +verticalOffset + "maxScrollD =" + maxScrollDistance);
        animateSlideOutLayout();

        isCollapsed= false;}

        else if ( verticalOffsetFloat == -maxScrollDistance && !editTextSearchIsExpanded){
            Log.d("offset", "onOffsetChanged: verticalOffset = maxScroll");

            animateSlideLayout();
            isCollapsed=true;



        }


    }

});

        mToolbar.setTitle(null);
//        mToolbarLayout.setTitle("Rewards");
//        mToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


//        initUi();

        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");

//    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//    merchantRecView.setHasFixedSize(true);
//    merchantRecView.setLayoutManager(linearLayoutManager);

    mAuth = FirebaseAuth.getInstance();
    thisUser = mAuth.getCurrentUser();
    redeemUid = thisUser.getUid();
    userPointsStorageRef = FirebaseStorage.getInstance().getReference().child("users/" + redeemUid + "/points.txt");

    addTabToTabLayout();

    setUpTabLayoutViewPager(mViewPager);
    tabLayout.setupWithViewPager(mViewPager);
    mViewPager.setOffscreenPageLimit(3);

//    setupViewPager(mViewPager);

//    initializeData(merchantRecView);



        return view;
    }


//
//    private void initializeData(final RecyclerView merchantRecView){
//
//        Log.d("INITDATA", "initializeData: initialising data");
//
//
////        merchantOffer = new ArrayList<>();
//        offerlistDatabaseRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d(TAG, "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
//                Log.d(TAG, "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
//                Log.d(TAG, "onChildAdded: datasnapshot " +  String.valueOf(dataSnapshot.child("offerCost").getValue()));
//                Log.d(TAG, "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
//                Log.d(TAG, "onChildAdded: not adding " + dataSnapshot.hasChild("merchantsName"));
//
////                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue())));
//
//                if (!dataSnapshot.getKey().equals("merchantsName")) {
//                    merchantOffer.add(dataSnapshot.getValue(MerchantOffer.class));
//
//                    Log.d("INITDATA", "Data added as class");
//
//
//                      merchantOfferAdapter = new MerchantOfferAdapter(merchantOffer, new MerchantOfferAdapter.ButtonClicked() {
//                        @Override
//                        public void onButtonClicked(View v, int position) {
//                            try {
//                                FragmentManager fragmentManager = getFragmentManager();
//                                confirmPreRedeem = new ConfirmPreRedeem();
//                                confirmPreRedeem.show(fragmentManager, "");
////                            confirmPreRedeem.getDialog().setCancelable(false);
//                                //                            deductPointsForRedemption();
//
//
//                                Log.d(TAG, "Rewards Fragment Button Clicked " + position);
//
//                            } catch (IllegalStateException e){
//                                e.printStackTrace();
//                            }
//                        }
//
//
//                    });
//
//                    merchantRecView.setAdapter(merchantOfferAdapter);
//
//                }
//
//                }
//
////           String.valueOf(dataSnapshot.child("offerImage").getValue())
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
////        merchantOffer.add(new MerchantOffer("Kenny Rogers 10 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000","sd;fadsijf"));
////        merchantOffer.add(new MerchantOffer("Kenny Rogers 20 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000",));
////        merchantOffer.add(new MerchantOffer("Kenny Rogers 40 % off", "Kenny Rogers","10 JLN SB INDAH, SERI KEMBANGAN","+60165555555", "1000",));
//    }




    @Override
    public void onButtonClicked(View v, int position) {

        Log.d(TAG, "onButtonClicked:  Button is clicked");


    }




//    private static void userPreRedemption(){
//
//
//        SecureRandom random = new SecureRandom();
//        String preRedemptionCode = new BigInteger(30, random).toString(32).toUpperCase();
//        if (preRedemptionCode.contains("")&& (preRedemptionCode.length()!= 6)){
//            Log.d(TAG, "userPreRedemption: ERROR CODE GENERATION");
//            userPreRedemption();}
//        else {
//            Log.d("redeem", "userPreRedemption:  " + preRedemptionCode);
//
//
//            DatabaseReference preRedemptionDataRef = FirebaseDatabase.getInstance().getReference().child("users/pre-redeemedlist/" + redeemUid);
//            RewardsPocketOffer rewardsPocketUpdate = new RewardsPocketOffer(merchantOfferTitle, merchantName, merchantAddress, merchantContact, preRedemptionCode, merchantOfferImageUrl);
//
//            String preRedeemedKey = preRedemptionDataRef.push().getKey();
//            preRedemptionDataRef.child(preRedeemedKey).setValue(rewardsPocketUpdate);
//
//            RewardsPreredemption thisRewardPreredeemed = new RewardsPreredemption(redeemUid, preRedeemedKey);
//
//
//            StorageReference preRedemptionStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/" + merchantName + "/" + merchantOfferTitle + "/userredemptioncode/" + preRedemptionCode);
//            objToByteStreamUpload(thisRewardPreredeemed, preRedemptionStoRef);
//        }
//
//    }





//    public static void deductPointsForRedemption(){
//
//
//        userPointsStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                try{
//                try {
//                    userPoints = (new Gson().fromJson(new String(bytes, "UTF-8"), Integer.class));
//                    userPointsBefRed = userPoints;
//                    Log.d(TAG, "Gsonfrom json success, Points is " + userPoints);
//
//                } catch (UnsupportedEncodingException e){
//                    e.printStackTrace();
//                }} catch (JsonSyntaxException e ){
//                    e.printStackTrace();
//            }
//                if (userPoints>=redeemCost){
//                    Log.d("redeem", "redeem cost " + redeemCost);
//                pointsAfterRedemption = (int) (Math.ceil(userPoints) - redeemCost );
//
//
//                objToByteStreamUpload(pointsAfterRedemption,userPointsStorageRef);
//
//                DatabaseReference userPointsDataRef = FirebaseDatabase.getInstance().getReference().child("users/userPoints/" + redeemUid);
//                userPointsDataRef.setValue(pointsAfterRedemption);
//
//
//
//                /*User preredemption before Verification of code by Merchant*/
//                userPreRedemption();
////                offerlistDatabaseRef.child(merchantOfferTitle).child("redeemedUsers").push().setValue(redeemUid);
//
//                Log.d("redeem","Points uploaded from Redeem is " + pointsAfterRedemption);}
//
//                else {
//
//
//                    Log.d("redeem", "redeem cost " + redeemCost);
//                    Log.d("redeem","Points insufficient " + userPoints);
//                    InsufficientPointsDialog insufficientPointsDialog = new InsufficientPointsDialog(getAppContext());
//                    insufficientPointsDialog.show();
//                return;
//                }
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Log.d(TAG,"fragment is not created, exception: " + exception);
//            }
//        }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
//            @Override
//            public void onComplete(@NonNull Task<byte[]> task) {
//                Log.d(TAG, "onComplete:  Complete JOr outside completed Calc");
//                confirmPreRedeem.getDialog().dismiss();
//
//            }
//
//
//
//        });
//
//    }


//    public static void objToByteStreamUpload(Object obj, StorageReference destination){
//
//        String objStr = new Gson().toJson(obj);
//        InputStream in = new ByteArrayInputStream(objStr.getBytes(Charset.forName("UTF-8")));
//        UploadTask uploadTask = destination.putStream(in);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Log.d(TAG, "onFailure: Failure to upload in storage ");
//                // Use analytics to find out why is the error
//                // then only implement the best corresponding measures
//
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                try {
//                    Toast.makeText(getAppContext(), "Your Rewards have been Redeemed", Toast.LENGTH_LONG).show();
//                    Log.i(TAG, "Profile update successful!");
//                    // Use analytics to calculate the success rate
//                } catch (NullPointerException e ){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }



    public static Context getAppContext() {
        return RewardsFragment.context;
    }


//




//    @Override
//    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        int maxScroll = appBarLayout.getTotalScrollRange();
//        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
//
//
//        Log.d("scroll", "onOffsetChanged: percentage " +percentage);
//        if (percentage == 1f && isHideToolbarView) {
//            searchViewLinLayTop.setVisibility(View.VISIBLE);
//            isHideToolbarView = !isHideToolbarView;
//
//        } else if (percentage < 1f && !isHideToolbarView) {
//            Log.d("scroll", "onOffsetChanged: isHideToolbarView" +isHideToolbarView);
//
//            searchViewLinLayTop.setVisibility(View.GONE);
//            isHideToolbarView = !isHideToolbarView;
//        }
//
//    }



private void animateSlideLayout() {
    editTextSearch.setVisibility(View.VISIBLE);

    editTextSearch.requestFocus();


    AnimatorSet animatorSet = new AnimatorSet();


    ValueAnimator widthAnimator = ValueAnimator.ofInt(0,expWidth);
    widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int animatedValue = (int) animation.getAnimatedValue();
            expandingLayout.getLayoutParams().width = animatedValue;

            expandingLayout.requestLayout();

        }
    });
    widthAnimator.setDuration(200);
//    widthAnimator.start();
//    Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
//    expanding.startAnimation(startAnimation);
    ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(editTextSearch, "alpha", 0f, 1f);
    fadeAnim.setDuration(600);

    ObjectAnimator fadeOutAnim = ObjectAnimator.ofFloat(editTextSearchBig, "alpha", 1f, 0f);
    fadeOutAnim.setDuration(100);


//    AlphaAnimation fadeAnim = new AlphaAnimation(0,1);
//    fadeAnim.setDuration(400);


    animatorSet.playTogether(widthAnimator,fadeOutAnim,fadeAnim);

    animatorSet.start();

    editTextSearchIsExpanded = true;



    if(animatorSet.isStarted()&& animatorSet.isRunning()){

        editTextSearchBig.setVisibility(View.INVISIBLE);



            editTextSearch.setText(editTextSearchBig.getText().toString());

        editTextSearch.setSelection(editTextSearchBig.getText().length());

//            editTextSearch.requestFocus();


    }



    Log.d(TAG, "animateSlideLayout: " + animatorSet);

    if (!animatorSet.isRunning()){
        Log.d(TAG, "animateSlideOutLayout: End");
        editTextSearchBig.setVisibility(View.VISIBLE);


        animatorSet.end();
    }


}






    private void animateSlideOutLayout() {

        editTextSearchBig.setVisibility(View.VISIBLE);

        editTextSearchBig.requestFocus();


        AnimatorSet animatorSet = new AnimatorSet();

        expandingLayout.measure(0,0);
        Log.d(TAG, "animateSlideOutLayout:  getMeasuredWidth for out " + expandingLayout.getMeasuredWidth());

        ValueAnimator widthAnimator = ValueAnimator.ofInt(expandingLayout.getMeasuredWidth(),0);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                expandingLayout.getLayoutParams().width = animatedValue;

                expandingLayout.requestLayout();
            }
        });
        widthAnimator.setDuration(200);

//    widthAnimator.start();
//    Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
//    expanding.startAnimation(startAnimation);
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(editTextSearch, "alpha", 1f, 0f);
        fadeAnim.setDuration(600);

        ObjectAnimator fadeInAnim = ObjectAnimator.ofFloat(editTextSearchBig, "alpha", 0, 1f);
        fadeInAnim.setDuration(300);

//    AlphaAnimation fadeAnim = new AlphaAnimation(0,1);
//    fadeAnim.setDuration(400);


        animatorSet.playTogether(fadeAnim,fadeInAnim,widthAnimator);

        animatorSet.start();
        editTextSearchIsExpanded = false;

        Log.d(TAG, "animateSlideLayout: " + animatorSet);


        if(animatorSet.isStarted()&& animatorSet.isRunning()){


            editTextSearch.setVisibility(View.INVISIBLE);

            editTextSearchBig.setText(editTextSearch.getText().toString());

            editTextSearchBig.setSelection(editTextSearch.getText().length());



        }



        if (!animatorSet.isRunning()){
            Log.d(TAG, "animateSlideOutLayout: End");

            editTextSearch.setVisibility(View.INVISIBLE);

            animatorSet.end();
        }




    }










    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "onTextChanged: ");

        if (editTextSearch.hasFocus()){
            Log.d(TAG, "onTextChanged:  small focus");
        }


            AllOfferFragment.merchantOfferAdapter.getFilter().filter(charSequence);
            AllOfferFragment.allRecView.setAdapter(AllOfferFragment.merchantOfferAdapter);

    }

//        merchantOfferAdapter.getFilter().filter(charSequence);
//        merchantRecView.setAdapter(merchantOfferAdapter);
//    }

    @Override
    public void afterTextChanged(Editable editable) {



    }

    public void expandToolbar(){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if(behavior!=null) {
            Log.d(TAG, "expandToolbar: ");
            behavior.onNestedScroll(coordinatorLayout, appBarLayout, null, 0, -10000, 0,0);
        }
    }



    private void bindViews(View view){


        coordinatorLayout = view.findViewById(R.id.coord_layout);
        mToolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar_in_recview);
        editTextSearch = view.findViewById(R.id.expanding_search);
        editTextSearchBig = view.findViewById(R.id.test_text);

        expandingLayout = view.findViewById(R.id.expanding_layout);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.offer_view_pager);
        tabLayout =view.findViewById(R.id.tab_layout);
//        merchantRecView = (RecyclerView) view.findViewById(R.id.merchant_offer_recview);
    }



    private static class OfferPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public OfferPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    private void setupViewPager(ViewPager mViewPager) {
        OfferPageAdapter adapter = new OfferPageAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment( new AllOfferFragment(), "All");
//        adapter.addFragment(DetailFragment.newInstance(getAsset("book_author.txt")), "身世");
//        adapter.addFragment(DetailFragment.newInstance(getAsset("book_menu.txt")), "性格");
//        adapter.addFragment(DetailFragment.newInstance(getAsset("lufei_home.txt")), "家谱");
//        adapter.addFragment(DetailFragment.newInstance(getAsset("lufei_friend.txt")), "海贼团");
        mViewPager.setAdapter(adapter);
    }


    private void setUpTabLayoutViewPager (ViewPager mViewPager) {


        TabLayoutPagerAdapter adapter = new TabLayoutPagerAdapter(getChildFragmentManager());


        // Set the adapter onto the view pager
        mViewPager.setAdapter(adapter);
    }


    private void addTabToTabLayout() {


        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));
        tabLayout.addTab(tabLayout.newTab().setText("Brands"));



    }

}
