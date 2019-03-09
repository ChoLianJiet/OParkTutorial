package com.opark.opark;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;
import com.opark.opark.rewards_redemption.RewardsFragment;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class CategoriesOfferFragment extends Fragment {
    final List<MerchantOffer> merchantOffer = new ArrayList<>();
    final List<MerchantOffer> merchantOffer1 = new ArrayList<>();
    MerchantOffer thisMerchantOffer = new MerchantOffer();
    private RecyclerView fnbRecView;
    private CardView fnbCategory;
    private RecyclerView hnbRecView;
    private CardView hnbCategory;
    private RecyclerView servicesRecView;
    private CardView servicesCategory;
    private RecyclerView travelRecView;
    private CardView travelCategory;
    private RecyclerView entertainmentRecView;
    private CardView entertainmentCategory;
    private RecyclerView shoppingRecView;
    private CardView shoppingCategory;
    private DatabaseReference offerlistDatabaseRef1;
    private DatabaseReference offerlistDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser thisUser;
    private String redeemUid;
    private StorageReference userPointsStorageRef;
    public static MerchantOfferAdapter merchantOfferAdapter;
    private boolean isExpanded;
    public static ConfirmPreRedeem confirmPreRedeem;
    public int expandedHeight;
    private LinearLayout catParent;
    private CarryForwardNestedScrollView categoryScrollView;
    public static String selectedCategory;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_offer, container, false);

        isExpanded = false;
        bindViews(view);

        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offercategory");
        offerlistDatabaseRef1 = FirebaseDatabase.getInstance().getReference().child("offerlist/approved-offers");

        mAuth = FirebaseAuth.getInstance();
        thisUser = mAuth.getCurrentUser();
        redeemUid = thisUser.getUid();
        userPointsStorageRef = FirebaseStorage.getInstance().getReference().child("users/" + redeemUid + "/points.txt");

        hnbCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedCategory = "Health & Beauty";
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                hnbRecView.setHasFixedSize(true);
                hnbRecView.setLayoutManager(linearLayoutManager);
                if (isExpanded) {
                    Log.d("category", "onClick:is Expanded ");
                    keepRecView(hnbRecView, linearLayoutManager.getHeight());
                    merchantOffer.clear();

                    setViewGoneWithFadeIn(fnbRecView, fnbCategory);
                    setViewGoneWithFadeIn(travelRecView, travelCategory);
                    setViewGoneWithFadeIn(servicesRecView, servicesCategory);
                    setViewGoneWithFadeIn(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeIn(shoppingRecView, shoppingCategory);
                } else {
                    Log.d("category", "CategoryCard Click: should expand  ");


                    initializeData1(hnbRecView, "Health & Beauty", linearLayoutManager);

                    setViewGoneWithFadeOut(fnbRecView, fnbCategory);
                    setViewGoneWithFadeOut(travelRecView, travelCategory);
                    setViewGoneWithFadeOut(servicesRecView, servicesCategory);
                    setViewGoneWithFadeOut(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeOut(shoppingRecView, shoppingCategory);
                    isExpanded = true;

                }


            }
        });


        fnbCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCategory = "Food & Beverages";
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                fnbRecView.setHasFixedSize(true);
                fnbRecView.setLayoutManager(linearLayoutManager);
                if (isExpanded) {
                    Log.d("category", "onClick:is Expanded ");
                    keepRecView(fnbRecView, linearLayoutManager.getHeight());
                    merchantOffer.clear();

                    setViewGoneWithFadeIn(hnbRecView, hnbCategory);
                    setViewGoneWithFadeIn(travelRecView, travelCategory);
                    setViewGoneWithFadeIn(servicesRecView, servicesCategory);
                    setViewGoneWithFadeIn(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeIn(shoppingRecView, shoppingCategory);
                } else {
                    Log.d("category", "CategoryCard Click: should expand  ");


                    initializeData1(fnbRecView, "Food & Beverages", linearLayoutManager);

                    setViewGoneWithFadeOut(hnbRecView, hnbCategory);
                    setViewGoneWithFadeOut(travelRecView, travelCategory);
                    setViewGoneWithFadeOut(servicesRecView, servicesCategory);
                    setViewGoneWithFadeOut(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeOut(shoppingRecView, shoppingCategory);
                    isExpanded = true;

                }


            }
        });


        travelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedCategory = "Travel";

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                travelRecView.setHasFixedSize(true);
                travelRecView.setLayoutManager(linearLayoutManager);
                if (isExpanded) {
                    Log.d("category", "onClick:is Expanded ");
                    keepRecView(travelRecView, linearLayoutManager.getHeight());
                    merchantOffer.clear();

                    setViewGoneWithFadeIn(hnbRecView, hnbCategory);
                    setViewGoneWithFadeIn(fnbRecView, fnbCategory);
                    setViewGoneWithFadeIn(servicesRecView, servicesCategory);
                    setViewGoneWithFadeIn(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeIn(shoppingRecView, shoppingCategory);
                } else {
                    Log.d("category", "CategoryCard Click: should expand  ");


                    initializeData1(travelRecView, "Travel", linearLayoutManager);

                    setViewGoneWithFadeOut(hnbRecView, hnbCategory);
                    setViewGoneWithFadeOut(fnbRecView, fnbCategory);
                    setViewGoneWithFadeOut(servicesRecView, servicesCategory);
                    setViewGoneWithFadeOut(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeOut(shoppingRecView, shoppingCategory);
                    isExpanded = true;

                }


            }
        });


        servicesCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCategory = "Services";

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                servicesRecView.setHasFixedSize(true);
                servicesRecView.setLayoutManager(linearLayoutManager);
                if (isExpanded) {
                    Log.d("category", "onClick:is Expanded ");
                    keepRecView(servicesRecView, linearLayoutManager.getHeight());
                    merchantOffer.clear();

                    setViewGoneWithFadeIn(hnbRecView, hnbCategory);
                    setViewGoneWithFadeIn(travelRecView, travelCategory);
                    setViewGoneWithFadeIn(fnbRecView, fnbCategory);
                    setViewGoneWithFadeIn(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeIn(shoppingRecView, shoppingCategory);
                } else {
                    Log.d("category", "CategoryCard Click: should expand  ");


                    initializeData1(servicesRecView, "Services", linearLayoutManager);

                    setViewGoneWithFadeOut(hnbRecView, hnbCategory);
                    setViewGoneWithFadeOut(travelRecView, travelCategory);
                    setViewGoneWithFadeOut(fnbRecView, fnbCategory);
                    setViewGoneWithFadeOut(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeOut(shoppingRecView, shoppingCategory);
                    isExpanded = true;

                }


            }
        });


        entertainmentCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCategory = "Entertainment";

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                entertainmentRecView.setHasFixedSize(true);
                entertainmentRecView.setLayoutManager(linearLayoutManager);
                if (isExpanded) {
                    Log.d("category", "onClick:is Expanded ");
                    keepRecView(entertainmentRecView, linearLayoutManager.getHeight());
                    merchantOffer.clear();

                    setViewGoneWithFadeIn(hnbRecView, hnbCategory);
                    setViewGoneWithFadeIn(travelRecView, travelCategory);
                    setViewGoneWithFadeIn(servicesRecView, servicesCategory);
                    setViewGoneWithFadeIn(fnbRecView, fnbCategory);
                    setViewGoneWithFadeIn(shoppingRecView, shoppingCategory);
                } else {
                    Log.d("category", "CategoryCard Click: should expand  ");


                    initializeData1(entertainmentRecView, "Entertainment", linearLayoutManager);

                    setViewGoneWithFadeOut(hnbRecView, hnbCategory);
                    setViewGoneWithFadeOut(travelRecView, travelCategory);
                    setViewGoneWithFadeOut(servicesRecView, servicesCategory);
                    setViewGoneWithFadeOut(fnbRecView, fnbCategory);
                    setViewGoneWithFadeOut(shoppingRecView, shoppingCategory);
                    isExpanded = true;

                }


            }
        });

        shoppingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCategory = "Shopping";

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                shoppingRecView.setHasFixedSize(true);
                shoppingRecView.setLayoutManager(linearLayoutManager);
                if (isExpanded) {
                    Log.d("category", "onClick:is Expanded ");
                    keepRecView(shoppingRecView, linearLayoutManager.getHeight());
                    merchantOffer.clear();

                    setViewGoneWithFadeIn(hnbRecView, hnbCategory);
                    setViewGoneWithFadeIn(travelRecView, travelCategory);
                    setViewGoneWithFadeIn(servicesRecView, servicesCategory);
                    setViewGoneWithFadeIn(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeIn(fnbRecView, fnbCategory);
                } else {
                    Log.d("category", "CategoryCard Click: should expand  ");


                    initializeData1(shoppingRecView, "Shopping", linearLayoutManager);

                    setViewGoneWithFadeOut(hnbRecView, hnbCategory);
                    setViewGoneWithFadeOut(travelRecView, travelCategory);
                    setViewGoneWithFadeOut(servicesRecView, servicesCategory);
                    setViewGoneWithFadeOut(entertainmentRecView, entertainmentCategory);
                    setViewGoneWithFadeOut(fnbRecView, fnbCategory);
                    isExpanded = true;

                }


            }
        });


        return view;

    }


    private void bindViews(View view) {

        fnbCategory = view.findViewById(R.id.fnb_category);
        fnbRecView = view.findViewById(R.id.fnb_recview);
        categoryScrollView = view.findViewById(R.id.category_scroll_view);
        hnbCategory = view.findViewById(R.id.hnb_category);
        hnbRecView = view.findViewById(R.id.hnb_recview);
        shoppingCategory = view.findViewById(R.id.shopping_category);
        shoppingRecView = view.findViewById(R.id.shopping_recview);
        servicesCategory = view.findViewById(R.id.services_category);
        servicesRecView = view.findViewById(R.id.services_recview);
        travelCategory = view.findViewById(R.id.travel_category);
        travelRecView = view.findViewById(R.id.travel_recview);
        entertainmentCategory = view.findViewById(R.id.ent_category);
        entertainmentRecView = view.findViewById(R.id.ent_recview);

//        fnbCategory = view.findViewById(R.id.fnb_category);
//        fnbRecView = view.findViewById(R.id.fnb_recview);
//        catParent = view.findViewById(R.id.category_linearparent);
    }


    private void expandRecView(final RecyclerView recview, int expandedHeight) {

//        recview.measure(0,0);
        Log.d("category", "expandRecView: getHeight ");

        ValueAnimator recviewExpand = ValueAnimator.ofInt(0, expandedHeight);
        recviewExpand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();

                recview.getLayoutParams().height = animatedValue;

                recview.requestLayout();


            }
        });

        recviewExpand.setDuration(400);

        recview.measure(0, 0);
        expandedHeight = recview.getMeasuredHeight();


        recviewExpand.start();


    }


    private void setViewGoneWithFadeIn(final RecyclerView recView, final CardView cardView) {
        recView.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        AnimatorSet setViewGoneAnimatorSet = new AnimatorSet();

        ObjectAnimator fadeRecView = ObjectAnimator.ofFloat(recView, "alpha", 0f, 1f);
        ObjectAnimator fadeCardView = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f);


        setViewGoneAnimatorSet.playTogether(fadeRecView, fadeCardView);

        setViewGoneAnimatorSet.setDuration(300);

        setViewGoneAnimatorSet.start();


    }


    private void setViewGoneWithFadeOut(final RecyclerView recView, final CardView cardView) {

        AnimatorSet setViewGoneAnimatorSet = new AnimatorSet();

        ObjectAnimator fadeRecView = ObjectAnimator.ofFloat(recView, "alpha", 1f, 0f);
        ObjectAnimator fadeCardView = ObjectAnimator.ofFloat(cardView, "alpha", 1f, 0f);


        setViewGoneAnimatorSet.playTogether(fadeRecView, fadeCardView);

        setViewGoneAnimatorSet.setDuration(150);

        setViewGoneAnimatorSet.start();

        setViewGoneAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                Log.d("anim", "onAnimationStart: " + cardView.getTranslationY());
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d("anim", "onAnimationEnd: ");
                recView.setVisibility(GONE);
                cardView.setVisibility(GONE);


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    private void keepRecView(final RecyclerView recview, int expandedHeight) {

        recview.measure(0, 0);

        ValueAnimator recviewExpand = ValueAnimator.ofInt(expandedHeight, 0);
        recviewExpand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                recview.getLayoutParams().height = animatedValue;

                recview.requestLayout();

            }
        });

        recviewExpand.setDuration(150);


        recviewExpand.start();


        isExpanded = false;


    }

    private void initializeData(final RecyclerView recview, String category, final LinearLayoutManager linearLayoutManager) {

        Log.d("INITDATA", "initializeData: initialising data");


        offerlistDatabaseRef.child(category).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("category", "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
                Log.d("category", "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
                Log.d("category", "onChildAdded: datasnapshot " + String.valueOf(dataSnapshot.child("offerCost").getValue()));
                Log.d("category", "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
                Log.d("category", "onChildAdded: not adding " + dataSnapshot.hasChild("merchantsName"));

//                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue())));

                if (!dataSnapshot.getKey().equals("merchantsName")) {
                    merchantOffer.add(dataSnapshot.getValue(MerchantOffer.class));

                    Log.d("category", "Data added as class " + merchantOffer);


                    merchantOfferAdapter = new MerchantOfferAdapter(merchantOffer, new MerchantOfferAdapter.ButtonClicked() {
                        @Override
                        public void onButtonClicked(View v, int position) {
                            try {
                                Log.d("", "onButtonClicked:  ");
                                FragmentManager fragmentManager = getFragmentManager();
                                confirmPreRedeem = new ConfirmPreRedeem();
                                confirmPreRedeem.show(fragmentManager, "");


                                Log.d("tab", "Rewards Fragment Button Clicked " + position);

                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new MerchantOfferAdapter.CardClicked() {
                        @Override
                        public void onCardClicked(View v, int position) {

                        }
                    });


//                    if(!isExpanded){
//                        Log.d("category", "expanding recview:  ");


                    Log.d("category", "recview child    + " + recview.getChildCount());

                    recview.setAdapter(merchantOfferAdapter);


//                        if (recview.getChildCount() == 0 ){
//                            expandRecView(recview,701);
//                        } else {

                    Log.d("measure", "onChildAdded: first item position  " + linearLayoutManager.findFirstVisibleItemPosition());
//                                        recview.measure(RecyclerView.MeasureSpec.makeMeasureSpec(0,RecyclerView.MeasureSpec.EXACTLY),RecyclerView.MeasureSpec.makeMeasureSpec(recview.getHeight(), View.MeasureSpec.EXACTLY));
                    expandRecView(recview, linearLayoutManager.getHeight() + 701);


//                        }

//                }

                    RewardsFragment.appBarLayout.setExpanded(false);

//                    }
                }

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initializeData1(final RecyclerView recview, String category, final LinearLayoutManager linearLayoutManager) {

        Log.d("INITDATA", "initializeData: initialising data");


        offerlistDatabaseRef1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if (dataSnapshot.getValue(MerchantOffer.class).getOfferCategories().equals(selectedCategory) && !merchantOffer.contains(dataSnapshot.getValue(MerchantOffer.class))) {
                    merchantOffer.add(dataSnapshot.getValue(MerchantOffer.class));

                    Log.d("category", "Data added as class " + merchantOffer);


                    merchantOfferAdapter = new MerchantOfferAdapter(merchantOffer, new MerchantOfferAdapter.ButtonClicked() {
                        @Override
                        public void onButtonClicked(View v, int position) {
                            try {
                                Log.d("", "onButtonClicked:  ");
                                FragmentManager fragmentManager = getFragmentManager();
                                confirmPreRedeem = new ConfirmPreRedeem();
                                confirmPreRedeem.show(fragmentManager, "");


                                Log.d("tab", "Rewards Fragment Button Clicked " + position);

                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new MerchantOfferAdapter.CardClicked() {
                        @Override
                        public void onCardClicked(View v, int position) {

                        }
                    });


                    Log.d("category", "recview child    + " + recview.getChildCount());

                    recview.setAdapter(merchantOfferAdapter);


//

//                    Log.d("measure", "LLM measuring child height :   " + linearLayoutManager.getDecoratedMeasuredHeight(recview.getChildAt(1)));


                    Log.d("measure", "onChildAdded: first item height  " + linearLayoutManager.findFirstVisibleItemPosition());


//                                        recview.measure(RecyclerView.MeasureSpec.makeMeasureSpec(0,RecyclerView.MeasureSpec.EXACTLY),RecyclerView.MeasureSpec.makeMeasureSpec(recview.getHeight(), View.MeasureSpec.EXACTLY));
                    expandRecView(recview, linearLayoutManager.getHeight() + 720);

                    RewardsFragment.appBarLayout.setExpanded(false);

                }

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
