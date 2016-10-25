package com.smart.smarthome;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowBarcode extends AppCompatActivity {

    private ImageButton addImageButton;
    private EditText Barcode;
    private EditText Name;
    private EditText Type;
    private EditText Unit;
    private EditText Price;
    private EditText Madein;
    private EditText Volumn;
    private EditText TopsPrice;
    private EditText LotusPrice;
    private Button Submit;
    private String Type2;
    private String Quantity;
    private String Pack;
    private String value;
    private String image;
    private String barcodeValue2;
    String volume_val;
    String unit_val;
    String price_val;
    String priceTops_val;
    String priceLotus_val;
    String totalVolume_val;
    String barcodeValue;


    Spinner staticSpinner;
    Spinner staticSpinner2;
    Spinner staticSpinner3;
    Spinner staticSpinner4;



    private TextView tvQuan;
    private TextView tvQuan2;
    private TextView tvQuan3;

    private EditText Softline;
    private EditText Deadline;
    private EditText DecreaseperClick;

    String sofeline_val;
    String deadline_val;
    String decreaseperclick_val;
    String m_Text;

    private String LowVolume = "Quantity";



    private Uri imageUri = null;
    Uri downloadUrl = null;

    String mCurrentPhotoPath = null;
    private int checkLast ;

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 999;
    private static final int GET_BAR_CODE = 555;


    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_barcode);

        tvQuan = (TextView)findViewById(R.id.tvQuan);
        tvQuan2 = (TextView)findViewById(R.id.tvQuan2);
        tvQuan3 = (TextView)findViewById(R.id.tvDecrease);

        Softline = (EditText) findViewById(R.id.addLowVolumeEdit);
        Deadline = (EditText) findViewById(R.id.addLowVolumeEdit2);
        DecreaseperClick = (EditText) findViewById(R.id.addLowVolumeEdit3);

        addImageButton = (ImageButton) findViewById(R.id.addImageButton);
        Barcode = (EditText) findViewById(R.id.addBarcodeEdit);
        Name = (EditText) findViewById(R.id.addNameEdit);
        //Type = (EditText)findViewById(R.id.addTypeEdit);
        Unit = (EditText) findViewById(R.id.addUnitEdit);
        Price = (EditText) findViewById(R.id.addPriceEdit);
        Madein = (EditText) findViewById(R.id.addMadeinEdit);
        Submit = (Button) findViewById(R.id.addSubmitButton);
        Volumn = (EditText) findViewById(R.id.addVolumeEdit);
        TopsPrice = (EditText) findViewById(R.id.addPriceTopsEdit);
        LotusPrice = (EditText) findViewById(R.id.addPriceLotusEdit);

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

        staticSpinner2 = (Spinner) findViewById(R.id.static_spinnerUnit);

        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter
                .createFromResource(this, R.array.Quan_array,
                        android.R.layout.simple_spinner_item);

        staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        staticSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Quantity = (String) adapterView.getItemAtPosition(i);
                if(LowVolume.equals("Volume")) {
                    tvQuan.setText(Quantity);
                    tvQuan2.setText(Quantity);
                    tvQuan3.setText(Quantity);
                }
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
                if(LowVolume.equals("Quantity")) {
                    tvQuan.setText(Pack);
                    tvQuan2.setText(Pack);
                    tvQuan3.setText(Pack);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        staticSpinner3.setAdapter(staticAdapter3);

        staticSpinner4 = (Spinner) findViewById(R.id.static_spinnerLowVolume);

        ArrayAdapter<CharSequence> staticAdapter4 = ArrayAdapter
                .createFromResource(this, R.array.LowVolume_array,
                        android.R.layout.simple_spinner_item);

        staticAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        staticSpinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LowVolume = (String) adapterView.getItemAtPosition(i);
                if(LowVolume.equals("Quantity")){
                    tvQuan.setText(Pack);
                    tvQuan2.setText(Pack);
                    tvQuan3.setText(Pack);
                }else  if(LowVolume.equals("Volume")) {
                    tvQuan.setText(Quantity);
                    tvQuan2.setText(Quantity);
                    tvQuan3.setText(Quantity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        staticSpinner4.setAdapter(staticAdapter4);

        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");

        mProgress = new ProgressDialog(this);

        checkLast = 0;


        AlertDialog.Builder builder = new AlertDialog.Builder(ShowBarcode.this);
        builder.setTitle("How many volumn ?");
        final EditText input = new EditText(ShowBarcode.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        builder.setView(input);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                barcodeValue2 = input.getText().toString().trim();
                DatabaseReference mDatabse = FirebaseDatabase.getInstance().getReference().child("system").child("items");
                Query queryRef = mDatabse.orderByChild("Barcode").equalTo(barcodeValue2);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getChildrenCount() == 0){
                            Toast.makeText(ShowBarcode.this, "Barcode not Match in Database", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(ShowBarcode.this);
                            builder2.setTitle("No item match in Database");
                            builder2.setMessage("Scan Again ");
                            builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ShowBarcode.this, BarcodeCaptureActivity.class);
                                    startActivityForResult(intent, GET_BAR_CODE);
                                }
                            });
                            builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder2.show();

                            // TODO Try to capture barcdoe agian
                        }else {
                            for (final com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                value = postSnapshot.getKey();


                                AlertDialog.Builder builder = new AlertDialog.Builder(ShowBarcode.this);
                                builder.setTitle("OK ?");
                                builder.setMessage(barcodeValue);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("system").child("items").child(value);

                                        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                item i2 = dataSnapshot.getValue(item.class);

                                                Picasso.with(ShowBarcode.this).load(i2.getImage()).fit().into(addImageButton);
                                                Barcode.setText(i2.getBarcode());
                                                Name.setText(i2.getName());

                                                if(i2.getType().equals("Food and Ingredients") ) {
                                                    staticSpinner.setSelection(0, true);
                                                }else if(i2.getType().equals("Beverage and Drink Powder") ) {
                                                    staticSpinner.setSelection(1, true);
                                                }else if(i2.getType().equals("Health and Beauty") ) {
                                                    staticSpinner.setSelection(2, true);
                                                }else if(i2.getType().equals("Household Product")  ) {
                                                    staticSpinner.setSelection(3,true);
                                                }else if(i2.getType().equals("etc")) {
                                                    staticSpinner.setSelection(4, true);
                                                }


                                                if(i2.getQuantity().equals("cc")){
                                                    staticSpinner2.setSelection(0, true);
                                                }else if(i2.getQuantity().equals("ml")){
                                                    staticSpinner2.setSelection(1, true);
                                                }else if(i2.getQuantity().equals("gram")){
                                                    staticSpinner2.setSelection(2, true);
                                                }else if(i2.getQuantity().equals("sheet")){
                                                    staticSpinner2.setSelection(3, true);
                                                }else if(i2.getQuantity().equals("Oz.")){
                                                    staticSpinner2.setSelection(4, true);
                                                }else if(i2.getQuantity().equals("piece")){
                                                    staticSpinner2.setSelection(5, true);
                                                }

                                                if(i2.getClassifier().equals("bottles")){
                                                    staticSpinner3.setSelection(0, true);
                                                }else if(i2.getClassifier().equals("box")){
                                                    staticSpinner3.setSelection(1, true);
                                                }else if(i2.getClassifier().equals("can")){
                                                    staticSpinner3.setSelection(2, true);
                                                }else if(i2.getClassifier().equals("pack")){
                                                    staticSpinner3.setSelection(3, true);
                                                }

                                                if(i2.getLowBy().equals("Quantity")){
                                                    staticSpinner4.setSelection(0, true);
                                                }else if(i2.getLowBy().equals("Volume")){
                                                    staticSpinner4.setSelection(1, true);
                                                }else{
                                                    staticSpinner4.setSelection(0, true);
                                                }




                                                Volumn.setText(i2.getVolume());
                                                Unit.setText(i2.getUnit());
                                                Price.setText(i2.getRetailPrice());
                                                Madein.setText(i2.getMadein());
                                                image = i2.getImage();
                                                TopsPrice.setText(i2.getSalePriceTops());
                                                LotusPrice.setText(i2.getSalePriceLotus());
                                                Softline.setText(i2.getSoftline());
                                                Deadline.setText(i2.getDeadline());
                                                DecreaseperClick.setText(i2.getDecreasePerClick());

                                                checkLast = 3;
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                barcodeValue2 = "";


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();


        //Intent intent = new Intent(ShowBarcode.this, BarcodeCaptureActivity.class);
        //startActivityForResult(intent, GET_BAR_CODE);



        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence colors[] = new CharSequence[]{"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowBarcode.this);
                builder.setTitle("Choose Method");
                builder.setItems(colors, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // Ensure that there's a camera activity to handle the intent
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File f ;

                                try {
                                    f = setUpPhotoFile();
                                    mCurrentPhotoPath = f.getAbsolutePath();
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    f = null;
                                    mCurrentPhotoPath = null;
                                }
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                            }


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
                final String LowBy_val = LowVolume;


                volume_val = Volumn.getText().toString().trim();
                if(volume_val.equals("")){
                    volume_val = "0";
                }

                final String quantity_val = Quantity;

                unit_val = Unit.getText().toString().trim();

                if(unit_val.equals("")){
                    unit_val = "0";
                }

                double volume = Double.parseDouble(volume_val);
                double unitItem10   = Double.parseDouble(unit_val);
                double TotalVolume = volume * unitItem10 ;
                totalVolume_val = TotalVolume+"";

                final String pack_val = Pack;
                price_val = Price.getText().toString().trim();

                if(price_val.equals("")){
                    price_val = "0";
                }

                priceTops_val = TopsPrice.getText().toString().trim();

                if(priceTops_val.equals("")){
                    priceTops_val = price_val;
                }

                priceLotus_val = LotusPrice.getText().toString().trim();

                if(priceLotus_val.equals("")){
                    priceLotus_val = price_val;
                }

                sofeline_val = Softline.getText().toString().trim();
                if(sofeline_val.equals("")){
                    sofeline_val = "0";
                }

                deadline_val = Deadline.getText().toString().trim();
                if(deadline_val.equals("")){
                    deadline_val = "0";
                }

                decreaseperclick_val = DecreaseperClick.getText().toString().trim();
                if(decreaseperclick_val.equals("")){
                    decreaseperclick_val = "1";
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
                    newItem.child("SalePriceTops").setValue(priceTops_val);
                    newItem.child("SalePriceLotus").setValue(priceLotus_val);
                    newItem.child("Image").setValue(downloadUrl.toString());
                    newItem.child("Softline").setValue(sofeline_val);
                    newItem.child("Deadline").setValue(deadline_val);
                    newItem.child("DecreasePerClick").setValue(decreaseperclick_val);
                    newItem.child("LowBy").setValue(LowBy_val);
                    newItem.child("TotalVolume").setValue(totalVolume_val);



                    mProgress.dismiss();
                    finish();


                } else if (imageUri != null && checkLast == 1) {

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
                            newItem.child("SalePriceTops").setValue(priceTops_val);
                            newItem.child("SalePriceLotus").setValue(priceLotus_val);
                            newItem.child("Softline").setValue(sofeline_val);
                            newItem.child("Deadline").setValue(deadline_val);
                            newItem.child("DecreasePerClick").setValue(decreaseperclick_val);
                            newItem.child("LowBy").setValue(LowBy_val);
                            newItem.child("TotalVolume").setValue(totalVolume_val);



                            mProgress.dismiss();
                            finish();

                        }
                    });

                }else if (checkLast == 3) {

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
                    newItem.child("Image").setValue(image);
                    newItem.child("SalePriceTops").setValue(priceTops_val);
                    newItem.child("SalePriceLotus").setValue(priceLotus_val);
                    newItem.child("Softline").setValue(sofeline_val);
                    newItem.child("Deadline").setValue(deadline_val);
                    newItem.child("DecreasePerClick").setValue(decreaseperclick_val);
                    newItem.child("LowBy").setValue(LowBy_val);
                    newItem.child("TotalVolume").setValue(totalVolume_val);


                    mProgress.dismiss();
                    finish();

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
                    newItem.child("SalePriceTops").setValue(priceTops_val);
                    newItem.child("SalePriceLotus").setValue(priceLotus_val);
                    newItem.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/test-b32cf.appspot.com/o/Item_Image%2Fno_image_icon_6.png?alt=media&token=72197c0c-2159-4c66-a7de-7dbf6a2da4fb");
                    newItem.child("Softline").setValue(sofeline_val);
                    newItem.child("Deadline").setValue(deadline_val);
                    newItem.child("DecreasePerClick").setValue(decreaseperclick_val);
                    newItem.child("LowBy").setValue(LowBy_val);
                    newItem.child("TotalVolume").setValue(totalVolume_val);



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


            setPic();
        }

        if (requestCode == GET_BAR_CODE) {
            if (resultCode == RESULT_OK) {
                barcodeValue2 = data.getStringExtra("Barcode");
                System.out.println(barcodeValue2);
                barcodeValue = barcodeValue2;
                DatabaseReference mDatabse = FirebaseDatabase.getInstance().getReference().child("system").child("items");
                Query queryRef = mDatabse.orderByChild("Barcode").equalTo(barcodeValue2);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getChildrenCount() == 0){
                            Toast.makeText(ShowBarcode.this, "Barcode not Match in Database", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(ShowBarcode.this);
                            builder2.setTitle("No item match in Database");
                            builder2.setMessage("Scan Again ");
                            builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ShowBarcode.this, BarcodeCaptureActivity.class);
                                    startActivityForResult(intent, GET_BAR_CODE);
                                }
                            });
                            builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder2.show();

                            // TODO Try to capture barcdoe agian
                        }else {
                            for (final com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                value = postSnapshot.getKey();


                                AlertDialog.Builder builder = new AlertDialog.Builder(ShowBarcode.this);
                                builder.setTitle("OK ?");
                                builder.setMessage(barcodeValue);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("system").child("items").child(value);

                                        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                item i2 = dataSnapshot.getValue(item.class);

                                                Picasso.with(ShowBarcode.this).load(i2.getImage()).fit().into(addImageButton);
                                                Barcode.setText(i2.getBarcode());
                                                Name.setText(i2.getName());

                                                if(i2.getType().equals("Food and Ingredients") ) {
                                                    staticSpinner.setSelection(0, true);
                                                }else if(i2.getType().equals("Beverage and Drink Powder") ) {
                                                    staticSpinner.setSelection(1, true);
                                                }else if(i2.getType().equals("Health and Beauty") ) {
                                                    staticSpinner.setSelection(2, true);
                                                }else if(i2.getType().equals("Household Product")  ) {
                                                    staticSpinner.setSelection(3,true);
                                                }else if(i2.getType().equals("etc")) {
                                                    staticSpinner.setSelection(4, true);
                                                }


                                                if(i2.getQuantity().equals("cc")){
                                                    staticSpinner2.setSelection(0, true);
                                                }else if(i2.getQuantity().equals("ml")){
                                                    staticSpinner2.setSelection(1, true);
                                                }else if(i2.getQuantity().equals("gram")){
                                                    staticSpinner2.setSelection(2, true);
                                                }else if(i2.getQuantity().equals("sheet")){
                                                    staticSpinner2.setSelection(3, true);
                                                }else if(i2.getQuantity().equals("Oz.")){
                                                    staticSpinner2.setSelection(4, true);
                                                }else if(i2.getQuantity().equals("piece")){
                                                    staticSpinner2.setSelection(5, true);
                                                }

                                                if(i2.getClassifier().equals("bottles")){
                                                    staticSpinner3.setSelection(0, true);
                                                }else if(i2.getClassifier().equals("box")){
                                                    staticSpinner3.setSelection(1, true);
                                                }else if(i2.getClassifier().equals("can")){
                                                    staticSpinner3.setSelection(2, true);
                                                }else if(i2.getClassifier().equals("pack")){
                                                    staticSpinner3.setSelection(3, true);
                                                }

                                                if(i2.getLowBy().equals("Quantity")){
                                                    staticSpinner4.setSelection(0, true);
                                                }else if(i2.getLowBy().equals("Volume")){
                                                    staticSpinner4.setSelection(1, true);
                                                }




                                                Volumn.setText(i2.getVolume());
                                                Unit.setText(i2.getUnit());
                                                Price.setText(i2.getRetailPrice());
                                                Madein.setText(i2.getMadein());
                                                image = i2.getImage();
                                                TopsPrice.setText(i2.getSalePriceTops());
                                                LotusPrice.setText(i2.getSalePriceLotus());
                                                Softline.setText(i2.getSoftline());
                                                Deadline.setText(i2.getDeadline());
                                                DecreaseperClick.setText(i2.getDecreasePerClick());

                                                checkLast = 3;
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                barcodeValue2 = "";




            } else {
                Toast.makeText(ShowBarcode.this, "Barcode not Found", Toast.LENGTH_LONG).show();
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = addImageButton.getWidth();
        int targetH = addImageButton.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] dataBAOS = baos.toByteArray();
        StorageReference filepath = mStorage.child("Item_Image2").child(mCurrentPhotoPath);
        UploadTask uploadTask = filepath.putBytes(dataBAOS);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println("Good");
                Picasso.with(ShowBarcode.this).load(downloadUrl).fit().into(addImageButton);
                checkLast = 2;
            }
        });



		/* Associate the Bitmap to the ImageView */
        // addImageButton.setImageBitmap(bitmap);
        //addImageButton.setVisibility(View.VISIBLE);
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }



}
