package com.smart.smarthome;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.Iterator;

public class BarcodeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String firebaseUrl = "https://test-b32cf.firebaseio.com/system/items";
    Firebase ref;
    FirebaseStorage storage;
    ProductCompare productCompare;
    static final int GET_BAR_CODE = 1;
    EditText inputUnitsScan ;
    EditText inputVolumeScan,pricecomparebarcode;
    Button buttonAddScan ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputUnitsScan = (EditText)findViewById(R.id.input_units_scan);
        inputVolumeScan = (EditText)findViewById(R.id.input_volume_scan);
        pricecomparebarcode = (EditText)findViewById(R.id.pricecomparebarcode);
        buttonAddScan = (Button)findViewById(R.id.buttonAddScan);

        Intent intent = new Intent(BarcodeActivity.this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, GET_BAR_CODE);

        Firebase.setAndroidContext(this);
        ref = new Firebase(firebaseUrl);
        storage = FirebaseStorage.getInstance();
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        final EditText inputUnitsScan = (EditText)findViewById(R.id.input_units_scan);
        final EditText inputVolumeScan = (EditText)findViewById(R.id.input_volume_scan);
        Button buttonAddScan = (Button)findViewById(R.id.buttonAddScan);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        View headerView = navigationView.getHeaderView(0);
        TextView userid = (TextView) headerView.findViewById(R.id.textView);
        userid.setText(user.getEmail());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.barcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            // Handle the camera action
            Intent bar = new Intent(BarcodeActivity.this,MainActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(BarcodeActivity.this,ShoppinglistShowlistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(BarcodeActivity.this,CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(BarcodeActivity.this,LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(BarcodeActivity.this,SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(BarcodeActivity.this,SettingActivity.class);
            startActivity(bar);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

  @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == GET_BAR_CODE) {
            if (resultCode == RESULT_OK) {
                final String barcodeValue2 = data.getStringExtra("Barcode");
                Log.i("BARCODE", barcodeValue2);
                Query queryRef = ref.orderByChild("Barcode").equalTo(barcodeValue2);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0) {
                            Toast.makeText(getApplicationContext(), "No firebase", Toast.LENGTH_SHORT).show();
                            finish();

                        }else if(dataSnapshot.getChildrenCount() != 0){
                            Log.i("TEST", "IN");
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                productCompare = new ProductCompare(child.child("Name").getValue().toString(),
                                        Double.valueOf(child.child("SalePriceLotus").getValue().toString()),
                                        0,
                                        child.child("Quantity").getValue().toString(),
                                        0,
                                        child.child("Barcode").getValue().toString());
                                ImageView imageViewScan = (ImageView) findViewById(R.id.imageViewScan);
                                Picasso.with(getApplicationContext()).load(child.child("Image").getValue().toString()).into(imageViewScan);
                                buttonAddScan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        productCompare.setPrice(Double.valueOf(pricecomparebarcode.getText().toString()));
                                        productCompare.setQuantity(Integer.valueOf(inputUnitsScan.getText().toString()));
                                        productCompare.setVolume(Integer.valueOf(inputVolumeScan.getText().toString()));
                                        ProductCompareData.add(productCompare);
                                        Intent intent = new Intent(getApplicationContext(), CompareActivity.class);
                                        startActivity(intent);
                                    }
                                });

                               /* StorageReference pathReference = storage.getReferenceFromUrl("gs://test-b32cf.appspot.com/" + child.child("Barcode").getValue().toString() + ".jpg");
                                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ImageView imageViewScan = (ImageView) findViewById(R.id.imageViewScan);
                                        Picasso.with(getApplicationContext()).load(uri.toString()).into(imageViewScan);
                                        buttonAddScan.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                productCompare.setPrice(Double.valueOf(pricecomparebarcode.getText().toString()));
                                                productCompare.setQuantity(Integer.valueOf(inputUnitsScan.getText().toString()));
                                                productCompare.setVolume(Integer.valueOf(inputVolumeScan.getText().toString()));
                                                ProductCompareData.add(productCompare);
                                                Intent intent = new Intent(getApplicationContext(), CompareActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });*/
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }else{
                finish();
            }
        }


    }
}
