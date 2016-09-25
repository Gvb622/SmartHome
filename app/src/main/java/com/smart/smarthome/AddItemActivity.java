package com.smart.smarthome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {


    private ImageButton addImageButton;
    private EditText Barcode;
    private EditText Name;
    private EditText Type;
    private EditText Unit;
    private EditText Price;
    private EditText Madein;
    private EditText Volumn;
    private Button Submit;
    private Uri imageUri = null;
    private String Type2;
    private String Quantity;
    private String Pack;
    String volume_val;
    String unit_val;
    String price_val;
    Spinner staticSpinner;
    Spinner staticSpinner2;
    Spinner staticSpinner3;
    Uri downloadUrl = null;
    String mCurrentPhotoPath;
    private int checkLast ;


    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 999;


    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");

        mProgress = new ProgressDialog(this);

        checkLast = 0;


        addImageButton = (ImageButton) findViewById(R.id.addImageButton);
        Barcode = (EditText) findViewById(R.id.addBarcodeEdit);
        Name = (EditText) findViewById(R.id.addNameEdit);
        //Type = (EditText)findViewById(R.id.addTypeEdit);
        Unit = (EditText) findViewById(R.id.addUnitEdit);
        Price = (EditText) findViewById(R.id.addPriceEdit);
        Madein = (EditText) findViewById(R.id.addMadeinEdit);
        Submit = (Button) findViewById(R.id.addSubmitButton);
        Volumn = (EditText) findViewById(R.id.addVolumeEdit);

        staticSpinner = (Spinner) findViewById(R.id.static_spinner);

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.Type_array,
                        android.R.layout.simple_spinner_item);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Type2 = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        staticSpinner.setAdapter(staticAdapter);


        //TODO change list in array
        staticSpinner2 = (Spinner) findViewById(R.id.static_spinnerUnit);

        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter
                .createFromResource(this, R.array.Quan_array,
                        android.R.layout.simple_spinner_item);

        staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        staticSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Quantity = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        staticSpinner2.setAdapter(staticAdapter2);


        staticSpinner3 = (Spinner) findViewById(R.id.static_spinner3);

        ArrayAdapter<CharSequence> staticAdapter3 = ArrayAdapter
                .createFromResource(this, R.array.brew_array,
                        android.R.layout.simple_spinner_item);

        staticAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        staticSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Pack = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        staticSpinner3.setAdapter(staticAdapter3);


        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence colors[] = new CharSequence[]{"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
                builder.setTitle("Choose One");
                builder.setItems(colors, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST);



                        } else if (which == 1) {
                            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent, GALLERY_REQUEST);
                        }
                    }
                });
                builder.show();

            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String barcode_val = Barcode.getText().toString().trim();
                final String name_val = Name.getText().toString().trim();
                final String type_val = Type2;
                volume_val = Volumn.getText().toString().trim();
                if(volume_val.equals("")){
                    volume_val = "0";
                }

                final String quantity_val = Quantity;

                unit_val = Unit.getText().toString().trim();

                if(unit_val.equals("")){
                    unit_val = "0";
                }

                final String pack_val = Pack;
                price_val = Price.getText().toString().trim();

                if(price_val.equals("")){
                    price_val = "0";
                }


                final String madein_val = Madein.getText().toString().trim();




                if (downloadUrl != null && checkLast == 2) {

                    mProgress.setMessage("Adding to inventory . . .");
                    mProgress.show();

                    DatabaseReference newItem = mDatabase.child(type_val).push();
                    newItem.child("Barcode").setValue(barcode_val);
                    newItem.child("Name").setValue(name_val);
                    newItem.child("Volume").setValue(volume_val);
                    newItem.child("Quantity").setValue(quantity_val);
                    newItem.child("Unit").setValue(unit_val);
                    newItem.child("Classifier").setValue(pack_val);
                    newItem.child("RetailPrice").setValue(price_val);
                    newItem.child("Madein").setValue(madein_val);
                    newItem.child("Image").setValue(downloadUrl.toString());
                    mProgress.dismiss();
                    finish();


                }else  if (imageUri != null && checkLast == 1) {

                    mProgress.setMessage("Adding to inventory . . .");
                    mProgress.show();

                    StorageReference filepath = mStorage.child("Item_Image").child(imageUri.getLastPathSegment());

                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            downloadUrl = taskSnapshot.getDownloadUrl();

                            DatabaseReference newItem = mDatabase.child(type_val).push();
                            newItem.child("Barcode").setValue(barcode_val);
                            newItem.child("Name").setValue(name_val);
                            newItem.child("Volume").setValue(volume_val);
                            newItem.child("Quantity").setValue(quantity_val);
                            newItem.child("Unit").setValue(unit_val);
                            newItem.child("Classifier").setValue(pack_val);
                            newItem.child("RetailPrice").setValue(price_val);
                            newItem.child("Madein").setValue(madein_val);
                            newItem.child("Image").setValue(downloadUrl.toString());

                            mProgress.dismiss();
                            finish();

                        }
                    });

                } else {

                    mProgress.setMessage("Adding to inventory . . .");
                    mProgress.show();

                    DatabaseReference newItem = mDatabase.child(type_val).push();
                    newItem.child("Barcode").setValue(barcode_val);
                    newItem.child("Name").setValue(name_val);
                    newItem.child("Volume").setValue(volume_val);
                    newItem.child("Quantity").setValue(quantity_val);
                    newItem.child("Unit").setValue(unit_val);
                    newItem.child("Classifier").setValue(pack_val);
                    newItem.child("RetailPrice").setValue(price_val);
                    newItem.child("Madein").setValue(madein_val);
                    newItem.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/test-b32cf.appspot.com/o/Item_Image%2Fno_image_icon_6.png?alt=media&token=72197c0c-2159-4c66-a7de-7dbf6a2da4fb");

                    mProgress.dismiss();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();
            addImageButton.setImageURI(imageUri);
            checkLast = 1;

        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            Uri selectImage = data.getData();
            System.out.println(selectImage);

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();
            //addImageButton.setImageBitmap(bitmap);

            StorageReference filepath = mStorage.child("Item_Image").child("Item" + new Date().getTime());
            UploadTask uploadTask = filepath.putBytes(dataBAOS);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    Picasso.with(AddItemActivity.this).load(downloadUrl).fit().centerCrop().into(addImageButton);
                    checkLast = 2;
                }
            });




        }
    }





}
