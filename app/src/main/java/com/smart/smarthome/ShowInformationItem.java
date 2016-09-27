package com.smart.smarthome;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class ShowInformationItem extends AppCompatActivity {

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
    private String image;
    private Button Submit;
    private Uri imageUri = null;
    private String TypeItem;
    private String Quantity;
    private String Pack;
    String volume_val;
    String unit_val;
    String price_val;
    String priceTops_val;
    String priceLotus_val;
    String totalVolume_val;


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

    private String LowVolume = "Quantity";

    Uri downloadUrl = null;
    String mCurrentPhotoPath;
    private int checkLast ;


    private static final int CAMERA_REQUEST = 999;

    ArrayAdapter<CharSequence> staticAdapter;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_information_item);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("key");
        final String type2 = extras.getString("Type");

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


        mProgress = new ProgressDialog(this);

        checkLast = 0;


        staticSpinner = (Spinner) findViewById(R.id.static_spinner);

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.Type_array,
                        android.R.layout.simple_spinner_item);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TypeItem = (String) adapterView.getItemAtPosition(i);
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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items").child(type2).child(value);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item i2 = dataSnapshot.getValue(item.class);

                Picasso.with(ShowInformationItem.this).load(i2.getImage()).fit().into(addImageButton);
                Barcode.setText(i2.getBarcode());
                Name.setText(i2.getName());

                if(type2.equals("Food and Ingredients") ) {
                    staticSpinner.setSelection(0, true);
                }else if(type2.equals("Beverage and Drink Powder") ) {
                    staticSpinner.setSelection(1, true);
                }else if(type2.equals("Health and Beauty") ) {
                    staticSpinner.setSelection(2, true);
                }else if(type2.equals("Household Product")  ) {
                    staticSpinner.setSelection(3,true);
                }else if(type2.equals("Etc")) {
                    staticSpinner.setSelection(4, true);
                }


                if(i2.getQuantity().equals("cc")){
                    staticSpinner2.setSelection(0, true);
                }else if(i2.getQuantity().equals("ml")){
                    staticSpinner2.setSelection(1, true);
                }else if(i2.getQuantity().equals("g.")){
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


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence colors[] = new CharSequence[]{"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowInformationItem.this);
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
                final String type_val = TypeItem;

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

                int volume = Integer.parseInt(volume_val);
                int unitItem10   = Integer.parseInt(unit_val);
                int TotalVolume = volume * unitItem10 ;
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


                if(!type_val.equals(type2) ){
                    if(checkLast == 0){
                        DatabaseReference Database3 = mDatabase2.child(type_val);
                        DatabaseReference newItem2 = Database3.push();
                        newItem2.child("Barcode").setValue(barcode_val);
                        newItem2.child("Name").setValue(name_val);
                        newItem2.child("Volume").setValue(volume_val);
                        newItem2.child("Quantity").setValue(quantity_val);
                        newItem2.child("Unit").setValue(unit_val);
                        newItem2.child("Classifier").setValue(pack_val);
                        newItem2.child("RetailPrice").setValue(price_val);
                        newItem2.child("Madein").setValue(madein_val);
                        newItem2.child("Image").setValue(image);
                        newItem2.child("SalePriceTops").setValue(priceTops_val);
                        newItem2.child("SalePriceLotus").setValue(priceLotus_val);
                        newItem2.child("Softline").setValue(sofeline_val);
                        newItem2.child("Deadline").setValue(deadline_val);
                        newItem2.child("DecreasePerClick").setValue(decreaseperclick_val);
                        newItem2.child("LowBy").setValue(LowBy_val);
                        newItem2.child("TotalVolume").setValue(totalVolume_val);

                        mProgress.dismiss();
                        mDatabase.removeValue();
                        finish();

                    }else if(imageUri != null && checkLast == 1){

                        mProgress.setMessage("Adding to inventory . . .");
                        mProgress.show();

                        StorageReference filepath = mStorage.child("Item_Image").child(imageUri.getLastPathSegment());
                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                downloadUrl = taskSnapshot.getDownloadUrl();

                                DatabaseReference Database3 = mDatabase2.child(type_val);
                                DatabaseReference newItem2 = Database3.push();
                                newItem2.child("Barcode").setValue(barcode_val);
                                newItem2.child("Name").setValue(name_val);
                                newItem2.child("Volume").setValue(volume_val);
                                newItem2.child("Quantity").setValue(quantity_val);
                                newItem2.child("Unit").setValue(unit_val);
                                newItem2.child("Classifier").setValue(pack_val);
                                newItem2.child("RetailPrice").setValue(price_val);
                                newItem2.child("Madein").setValue(madein_val);
                                newItem2.child("Image").setValue(downloadUrl.toString());
                                newItem2.child("SalePriceTops").setValue(priceTops_val);
                                newItem2.child("SalePriceLotus").setValue(priceLotus_val);
                                newItem2.child("Softline").setValue(sofeline_val);
                                newItem2.child("Deadline").setValue(deadline_val);
                                newItem2.child("DecreasePerClick").setValue(decreaseperclick_val);
                                newItem2.child("LowBy").setValue(LowBy_val);
                                newItem2.child("TotalVolume").setValue(totalVolume_val);

                                mProgress.dismiss();
                                mDatabase.removeValue();
                                finish();
                            }
                        });
                    }else if(downloadUrl != null && checkLast == 2){

                        mProgress.setMessage("Adding to inventory . . .");
                        mProgress.show();
                        DatabaseReference Database3 = mDatabase2.child(type_val);
                        DatabaseReference newItem2 = Database3.push();
                        newItem2.child("Barcode").setValue(barcode_val);
                        newItem2.child("Name").setValue(name_val);
                        newItem2.child("Volume").setValue(volume_val);
                        newItem2.child("Quantity").setValue(quantity_val);
                        newItem2.child("Unit").setValue(unit_val);
                        newItem2.child("Classifier").setValue(pack_val);
                        newItem2.child("RetailPrice").setValue(price_val);
                        newItem2.child("Madein").setValue(madein_val);
                        newItem2.child("Image").setValue(downloadUrl.toString());
                        newItem2.child("SalePriceTops").setValue(priceTops_val);
                        newItem2.child("SalePriceLotus").setValue(priceLotus_val);
                        newItem2.child("Softline").setValue(sofeline_val);
                        newItem2.child("Deadline").setValue(deadline_val);
                        newItem2.child("DecreasePerClick").setValue(decreaseperclick_val);
                        newItem2.child("LowBy").setValue(LowBy_val);
                        newItem2.child("TotalVolume").setValue(totalVolume_val);

                        mProgress.dismiss();
                        mDatabase.removeValue();
                        finish();
                    }
                }else{
                    if (downloadUrl != null && checkLast == 2) {

                        mProgress.setMessage("Adding to inventory . . .");
                        mProgress.show();

                        DatabaseReference newItem = mDatabase;
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


                    }else  if (imageUri != null && checkLast == 1) {

                        mProgress.setMessage("Adding to inventory . . .");
                        mProgress.show();

                        StorageReference filepath = mStorage.child("Item_Image").child(imageUri.getLastPathSegment());

                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                downloadUrl = taskSnapshot.getDownloadUrl();

                                DatabaseReference newItem = mDatabase;
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

                    } else {

                        mProgress.setMessage("Adding to inventory . . .");
                        mProgress.show();

                        DatabaseReference newItem = mDatabase;
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
                    }
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
                    Picasso.with(ShowInformationItem.this).load(downloadUrl).fit().centerCrop().into(addImageButton);
                    checkLast = 2;
                }
            });


        }
    }
}
