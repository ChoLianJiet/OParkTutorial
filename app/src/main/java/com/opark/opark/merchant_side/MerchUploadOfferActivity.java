package com.opark.opark.merchant_side;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.opark.opark.R;
import com.opark.opark.model.User;
import com.opark.opark.model.merchant_class.Merchant;
import com.opark.opark.model.merchant_offer.MerchantOffer;
import com.opark.opark.share_parking.MapsMainActivity;
import com.opark.opark.share_parking.Matchmaking;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MerchUploadOfferActivity extends AppCompatActivity {
  private static final int GALLERY = 55;
  private static final String TAG = "MerchUploadOfferActivity";
  private static final int CAMERAOPEN = 88;
  private ImageButton offerPicture;
  private Uri filePath;
  private String currentMerchantEmail;
  private String offerTitle;
  private Button uploadButton;
  private Bitmap offerImageForUpload;
  private EditText offerTitleEntry;
  private EditText offerRedemptionCost;
  private FirebaseStorage merchantOfferStorage;
  private StorageReference merchantOfferStorageReference;
  private DatabaseReference offerlistDatabaseRef;
  private FirebaseAuth mAuth;
  public static ArrayList<Merchant> merchObjList = new ArrayList<>();
  private FirebaseUser currentMerchantFirebaseUser;


  private String merchantCoName;
  private String merchantCoPhone;
  private String merchantCoAddress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_merch_upload_offer);

    mAuth = FirebaseAuth.getInstance();
    currentMerchantFirebaseUser = mAuth.getCurrentUser();
    currentMerchantEmail = currentMerchantFirebaseUser.getEmail();
    Log.d(TAG, "onCreate : Merch Email" + currentMerchantEmail);

    offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");
    Log.d(TAG, "onCreate: database Ref" + offerlistDatabaseRef);
    getMerchantDetails(currentMerchantEmail);


    merchantOfferStorage = FirebaseStorage.getInstance();
    merchantOfferStorageReference = merchantOfferStorage.getReference();
    bindViews();

    Merchant thisMerchant = new Merchant(merchantCoName , merchantCoPhone,  currentMerchantEmail, merchantCoAddress);
    Log.d(TAG, "onCreate: Merch Name" + thisMerchant.merchCoName);

    offerPicture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showPictureDialog();
      }
    });

    uploadButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setFolderInDatabase();
//                uploadImage();
      }
    });


  }

  private void bindViews() {
    offerPicture = findViewById(R.id.offer_picture);
    offerTitleEntry=findViewById(R.id.offer_title);
    offerRedemptionCost=findViewById(R.id.points_cost);
    uploadButton = findViewById(R.id.upload_offer_button);
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
    if(requestCode == GALLERY && resultCode == RESULT_OK
            && data != null && data.getData() != null )
    {
      filePath = data.getData();
      try {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
        offerPicture.setImageBitmap(bitmap);
        offerImageForUpload = bitmap;
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }

    } else if (requestCode == CAMERAOPEN) {
      Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
      offerPicture.setImageBitmap(thumbnail);
//            saveImage(thumbnail);
      offerImageForUpload  = thumbnail;
      Toast.makeText(MerchUploadOfferActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
    }
  }

  private void uploadImage() {

    if(offerImageForUpload != null)
    {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      offerImageForUpload.compress(Bitmap.CompressFormat.JPEG, 100, baos);
      byte[] data = baos.toByteArray();


      offerTitle= offerTitleEntry.getText().toString();
      Log.d(TAG, "uploadImage: offerTitle" + offerTitle);
      StorageReference ref = merchantOfferStorageReference.child("merchants/" + "offerlist/"+ offerTitle +"/"+ "offerImage");
      ref.putBytes(data)
              .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss();

                  Toast.makeText(MerchUploadOfferActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
              })
              .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
                  Toast.makeText(MerchUploadOfferActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              })
              .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                  double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                          .getTotalByteCount());
//                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
              });
    }
  }


  private void setFolderInDatabase() {

    Merchant thisMerchant = new Merchant(merchantCoName , merchantCoPhone,  currentMerchantEmail, merchantCoAddress);
    offerTitle = offerTitleEntry.getText().toString();

//    String key = offerlistDatabaseRef.child("offerlist/" + offerTitle ).push().getKey();
//    Log.d(TAG, "setFolderInDatabase: " +key );
    Log.d(TAG, "setFolderInDatabase: merch Name" + merchantCoName);
    MerchantOffer merchantOfferData = new MerchantOffer();
    merchantOfferData.setMerchantName(merchantCoName);
    merchantOfferData.setMerchantOfferTitle(offerTitle);
    merchantOfferData.setMerchantAddress(merchantCoAddress);
    merchantOfferData.setMerchantContact(merchantCoPhone);
    merchantOfferData.setOfferCost(offerRedemptionCost.getText().toString());
    merchantOfferData.setOfferImage(BitMapToString(offerImageForUpload));
    Log.d(TAG, "setFolderInDatabase: offer image" + offerImageForUpload);

    offerlistDatabaseRef.child(offerTitle).setValue(merchantOfferData);
    offerlistDatabaseRef.child(offerTitle).child("redeemCount").setValue(0);
  }


  private void getMerchantDetails(String email ){
    StorageReference merchRef = FirebaseStorage.getInstance().getReference().child("merchants/" + "merchantlist/" + email + "/merchProf.txt");

    final long ONE_MEGABYTE = 1024 * 1024;
    merchRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
      @Override
      public void onSuccess(byte[] bytes) {
        Log.d("Byte","getByte success");
        try {
          merchObjList.add(new Gson().fromJson(new String(bytes, "UTF-8"), Merchant.class));
          Log.d("Gson","Gsonfrom json success");


          for (int i = 0; i < merchObjList.size(); i++) {

            merchantCoName = (merchObjList.get(i).getMerchCoName());
            Log.d(TAG, "onSuccess: merch Name" + merchantCoName);
            merchantCoPhone = (merchObjList.get(i).getMerchContact());
            merchantCoAddress =(merchObjList.get(i).getMerchCoAddress());
            Log.d(TAG, "Iteration success" + i);


          }
        } catch (UnsupportedEncodingException e) {
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception exception) {
        // Handle any errors
      }
    });




  }

  public String BitMapToString(Bitmap bitmap){
    ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
    byte [] b=ByteStream.toByteArray();
    String temp= Base64.encodeToString(b, Base64.DEFAULT);
    return temp;
  }

}