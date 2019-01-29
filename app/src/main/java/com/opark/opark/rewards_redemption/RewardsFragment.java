package com.opark.opark.rewards_redemption;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.R;
import com.opark.opark.TabLayoutPagerAdapter;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;

import java.util.ArrayList;
import java.util.List;

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



    private static Context context;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public static AppBarLayout appBarLayout;
    private boolean isHideToolbarView = false;
    private EditText editTextSearch;
    private boolean editTextSearchIsExpanded;
    private EditText editTextSearchBig;
    boolean isCollapsed = false;
    public static FragmentManager rewardsFragmentManager;
    public static TabLayout tabLayout;

    public static ViewPager mViewPager;
    private NestedScrollView nestedScrollView;



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



        Log.d(TAG, "childfragmean  " + getActivity().getFragmentManager().getBackStackEntryCount());

        rewardsFragmentManager = getFragmentManager();

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








appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
    @Override
    public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {

        float maxScrollDistance=appBarLayout.getTotalScrollRange();

        float verticalOffsetFloat = (float) verticalOffset;

        float verticalOffsetTemp = verticalOffsetFloat;

        float collapseThreshold = maxScrollDistance*15/100;

        if (verticalOffsetFloat < (-maxScrollDistance) / 2 && isCollapsed && verticalOffsetFloat!=0){

                expandToolbar();


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

//        mToolbar.setTitle("Rewards");
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






    @Override
    public void onButtonClicked(View v, int position) {

        Log.d(TAG, "onButtonClicked:  Button is clicked");


    }




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
//        nestedScrollView = view.findViewById(R.id.nest_scrollview);
//        nestedScrollView.setFillViewport(true);
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
