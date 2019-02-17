package com.opark.opark.merchant_side;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.R;
import com.opark.opark.merchant_side.merchant_class.Merchant;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;


public class MoreImageUploadActivity extends AppCompatActivity {
    private static final int GALLERY = 55;
    private static final int CAMERAOPEN = 88;
    private ImageButton moreImage2;
    private ImageButton moreImage3;
    private ImageButton moreImage4;
    private FirebaseStorage merchantOfferStorage;
    private StorageReference merchantOfferStorageReference;
    private StorageReference merchantOfferImageRefFromSto;
    private DatabaseReference offerlistDatabaseRef;
    private FirebaseAuth mAuth;
    private Uri filePath;
    private Uri imgFilePath2;
    private Uri imgFilePath3;
    private Uri imgFilePath4;

    private String currentMerchantEmail;
    private FirebaseUser currentMerchantFirebaseUser;
    private ProgressBar mProgressBar;
    private  UploadProgressBarDialog uploadProgressBarDialog;
    private StorageTask mUploadTask;


    private Button uploadButton;
    private int imgButtonRef;
    private Intent thisIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_image_upload);

        thisIntent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        currentMerchantFirebaseUser = mAuth.getCurrentUser();
        currentMerchantEmail = currentMerchantFirebaseUser.getEmail();

        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");


        merchantOfferStorage = FirebaseStorage.getInstance();
        merchantOfferStorageReference = merchantOfferStorage.getReference();



        bindViews();


        moreImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        imgButtonRef = 2;
                        showPictureDialog();

            }
        });
        moreImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgButtonRef = 3;
                showPictureDialog();

            }
        });
        moreImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgButtonRef = 4;
                showPictureDialog();

            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(imgFilePath2!=null){
                uploadFile(imgFilePath2,2);}
                if(imgFilePath3!=null){
                    uploadFile(imgFilePath3,3);}
                if(imgFilePath4!=null){
                    uploadFile(imgFilePath4,4);}




            }
        });





    }




    private void bindViews(){

        moreImage2=findViewById(R.id.offer_picture2);
        moreImage3=findViewById(R.id.offer_picture3);
        moreImage4=findViewById(R.id.offer_picture4);

        uploadButton = findViewById(R.id.upload_more_image_button );
        mProgressBar = findViewById(R.id.progress_bar_more_img);


    }


    private String getFileExtension (Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile( Uri imgFilePath,int imgNum) {
        Bundle merchDetails = thisIntent.getExtras();
        String merchantCoName =  merchDetails.getString("merchantname");
        String offerTitle = merchDetails.getString("offertitle");


        FragmentManager fragmentManager = getSupportFragmentManager();
        uploadProgressBarDialog = new UploadProgressBarDialog();
        uploadProgressBarDialog.show(fragmentManager, "");


        uploadAllImg(imgFilePath,imgNum,merchantCoName,offerTitle);



    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),GALLERY);
    }


    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERAOPEN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            switch (imgButtonRef) {
                case 2:
                    Picasso.get().load(filePath).into(moreImage2);
                    imgFilePath2=filePath;
                    break;

                case 3:
                    Picasso.get().load(filePath).into(moreImage3);
                    imgFilePath3=filePath;

                    break;
                case 4:
                    Picasso.get().load(filePath).into(moreImage4);
                    imgFilePath4=filePath;

                    break;

                default:
                    break;
            }


//        } else if (requestCode == CAMERAOPEN) {
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            offerPicture.setImageBitmap(thumbnail);
//            offerImageForUpload  = thumbnail;
//            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
//        }
        }


    }


    private void uploadAllImg ( Uri imgFilePath,int imgNo, String merchantCoName, String offerTitle ){

        if (imgFilePath != null) {
            final StorageReference fileReference = merchantOfferStorageReference.child("merchants/offerlist/"+ merchantCoName + "/"+offerTitle).child("offerImage"+
                    imgNo + "." + getFileExtension(imgFilePath));

            merchantOfferImageRefFromSto = fileReference;



            mUploadTask = fileReference.putFile(imgFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    uploadProgressBarDialog.dismiss();
                                    mProgressBar.setProgress(0);
                                    Intent intentback = new Intent(MoreImageUploadActivity.this, MerchActivity.class);
                                    startActivity(intentback);


                                }
                            }, 500);


                            Toast.makeText(MoreImageUploadActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Log.d("URI", "onSuccess: " + uri.toString());


                                }
                            });


                        }
                    })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MoreImageUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })


                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }



}
