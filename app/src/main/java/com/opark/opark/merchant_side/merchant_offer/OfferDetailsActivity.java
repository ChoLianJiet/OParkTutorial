package com.opark.opark.merchant_side.merchant_offer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.opark.opark.OfferApprovalActivity;
import com.opark.opark.R;
import com.opark.opark.rewards_redemption.ConfirmPreRedeem;

import java.util.ArrayList;
import java.util.List;

public class OfferDetailsActivity extends AppCompatActivity {
    final long ONE_MEGABYTE = 1024 * 1024;

    TextView offerTitle;
    String offerTitleString;
    String merchantCoNameString;
    TextView merchantEmail;
    TextView firstLine,secondLine,city,postcode,state;

    TextView offerDescription;
    TextView merchantCoName;
    TextView offerCost;
    TextView merchantContact;
    TextView offerExpiry;
    LinearLayout offerDetailLinearLayout;
    Button approveButton;
    String landingImg;
    //    String[] imgUrls = new String[]{} ;
    List<String> imgUrls = new ArrayList<>();
    String imgUrl2, imgUrl3;
    Context context;
    private int dotscount;
    private ImageView[] dots;


    LinearLayout sliderDotspanel;

    Task<Void> alltask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details1);


        Intent offerIntent = getIntent();
        final Bundle offerDetails = offerIntent.getExtras();


        context = getApplicationContext();


        bindViews();


        final LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation);
        lottieAnimationView.setImageAssetsFolder("images/");
        lottieAnimationView.setAnimation("squareboi_loading_animation.json");
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();


        Log.d("details", "onCreate:  " + offerDetails.getString("offerdescription"));


        offerTitle.setText(offerDetails.getString("offertitle"));
        offerDescription.setText(offerDetails.getString("offerdescription"));
        offerExpiry.setText(offerDetails.getString("expirydate"));
        merchantCoName.setText(offerDetails.getString("merchantconame"));
        merchantContact.setText(offerDetails.getString("merchantcontact"));
        firstLine.setText(offerDetails.getString("firstline"));
        secondLine.setText(offerDetails.getString("secondline"));
        city.setText(offerDetails.getString("city"));
        state.setText(offerDetails.getString("state"));
        postcode.setText(offerDetails.getString("postcode"));

        offerCost.setText(offerDetails.getString("offercost"));



        final StorageReference imagesStoRef2 = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/" + (offerDetails.getString("merchantconame"))
                + "/" + (offerDetails.getString("offertitle")) + "/offerImage2.jpg");
        final StorageReference imagesStoRef3 = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/" + (offerDetails.getString("merchantconame"))
                + "/" + (offerDetails.getString("offertitle")) + "/offerImage3.jpg");

//        imagesStoRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                imgUrl2= uri.toString();
//                Log.d("viewpager", "onSuccess:  imgURl2" +imgUrl2 );
//            }
//        });
//        imagesStoRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                imgUrl3=uri.toString();
//            }
//        });


//        landingImg = offerDetails.getString("landingImgUrl");

//        imgUrls.add(landingImg);
//        imgUrls.add(imgUrl2);
//        imgUrls.add(imgUrl3);
//
//        Log.d("viewpager", "imgUrls: " + landingImg);
//        Log.d("viewpager", "imgurl2 " + imgUrl2);
//        Log.d("viewpager", "imgurl3 " + imgUrl3);



//        StorageReference imagesStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/" + (offerDetails.getString("merchantconame"))
//                                        + "/" + (offerDetails.getString("offertitle")) + "/imgUrls.txt" );
//
//        imagesStoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                List<String> imgurls = (new Gson().fromJson(new List<String>(bytes), String.class));
//
//
//                Log.d("imgUrls", "onSuccess:  " + imgurls);
//            }
//        });


        StorageReference descriptionStoRef = FirebaseStorage.getInstance().getReference().child("merchants/offerlist/" + (offerDetails.getString("merchantconame"))
                + "/" + (offerDetails.getString("offertitle"))).child("desc.txt");
        descriptionStoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {


                String offerDescriptionString = (new Gson().fromJson(new String(bytes), String.class));
//
                offerDescription.setText(offerDescriptionString);
//                offerTitle.setText(offerDetails.getString("offertitle"));
//                offerExpiry.setText(offerDetails.getString("expirydate"));
//                merchantCoName.setText(offerDetails.getString("merchantconame"));
//                merchantContact.setText(offerDetails.getString("merchantcontact"));
//                offerCost.setText(offerDetails.getString("offercost"));


                Task image2 = imagesStoRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (uri != null)
                            imgUrl2 = uri.toString();
                        Log.d("viewpager", "onSuccess:  imgURl2" + imgUrl2);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("viewpager", "onFailure: image2  ");
                    }
                });
                Task image3 = imagesStoRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (uri != null)
                            imgUrl3 = uri.toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("viewpager", "onFailure:  image 3");
                    }
                });

                landingImg = offerDetails.getString("landingImgUrl");


                alltask = Tasks.whenAll(image2, image3).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (landingImg != null)
                            imgUrls.add(landingImg);

                        if (imgUrl2 != null)
                            imgUrls.add(imgUrl2);

                        if (imgUrl3 != null)
                            imgUrls.add(imgUrl3);

                        Log.d("viewpager", "onComplete:  " + imgUrls);
                        Log.d("viewpager", "onComplete: ");

                        ViewPager viewPager = findViewById(R.id.all_image_vp);
                        OfferDetailsImageViewPager adapter = new OfferDetailsImageViewPager(context, imgUrls);
                        viewPager.setAdapter(adapter);


                        offerDetailLinearLayout.setVisibility(View.VISIBLE);
                        lottieAnimationView.cancelAnimation();
                        lottieAnimationView.setVisibility(View.INVISIBLE);

                        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
                        dotscount = adapter.getCount();
                        dots = new ImageView[dotscount];


                        for(int i = 0; i < dotscount; i++){

                            dots[i] = new ImageView(context);
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dots));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);

                            sliderDotspanel.addView(dots[i], params);

                        }

                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dots));

                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                                for(int i = 0; i< dotscount; i++){
                                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dots));
                                }

                                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dots));

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });


                    }
                });





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("viewpager", "onFailure:  Description Storage");
            }
        });































        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveOffer(offerDetails);

            }
        });

    }

    private void bindViews() {

        offerTitle = findViewById(R.id.offer_title);
        offerDescription = findViewById(R.id.offer_description);
        offerExpiry = findViewById(R.id.offer_expiry_date);
        merchantCoName = findViewById(R.id.merchant_co_name);
        merchantContact = findViewById(R.id.merchant_contact);
        offerCost = findViewById(R.id.offer_cost);
        offerDetailLinearLayout = findViewById(R.id.offer_detail_linearlayout);
        approveButton = findViewById(R.id.approve_button);

        merchantEmail = findViewById(R.id.merchant_email);
        firstLine = findViewById(R.id.merchant_first_line);
        secondLine = findViewById(R.id.merchant_second_line);
        postcode=findViewById(R.id.merchant_postcode);
        city     = findViewById(R.id.merchant_city);
        state = findViewById(R.id.merchant_state);

    }


    private void approveOffer(Bundle offerDetails) {

        Log.d("tab", offerDetails.getString("offertitle"));
        try {
            FirebaseDatabase.getInstance().getReference().child("offerlist/offer-waiting-approval")
                    .child(offerDetails.getString("offertitle")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("tab", "onDataChange: " + dataSnapshot.getValue());

                    MerchantOffer merchantOffer = dataSnapshot.getValue(MerchantOffer.class);


                    FirebaseDatabase.getInstance().getReference().child("offerlist/approved-offers/" + merchantOffer.getMerchantOfferTitle()).setValue(merchantOffer);


                    FirebaseDatabase.getInstance().getReference().child("offerlist/offer-waiting-approval")
                            .child(merchantOffer.getMerchantOfferTitle()).removeValue();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e) {

        }

//        merchantOffer = new ArrayList<>();
//        FirebaseDatabase.getInstance().getReference().child("offerlist/offer-waiting-approval")
//                .child(offerDetails.getString("offertitle")).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("tab", "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
//                Log.d("tab", "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
//                Log.d("tab", "onChildAdded: datasnapshot " + String.valueOf(dataSnapshot.child("offerCost").getValue()));
//                Log.d("tab", "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
//                Log.d("tab", "onChildAdded: not adding " + dataSnapshot.hasChild("merchantsName"));
//
////                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue())));
//
//                dataSnapshot.getValue(MerchantOffer.class);
//
//                FirebaseDatabase.getInstance().getReference().child("approved-offers/" + offerTitle).setValue(dataSnapshot.getValue(MerchantOffer.class));
//
//
//
//                Log.d("INITDATA", "Data added as class");
//
//
//            }
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


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("viewpager", "onDestroy: ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }





}

