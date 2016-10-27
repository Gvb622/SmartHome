package com.smart.smarthome;

import android.*;
import android.Manifest;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LocationbaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Awareness";
    int PLACE_PICKER_REQUEST = 2;
    LocalActivityManager mLocalActivityManager;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationbase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(LocationbaseActivity.this)
                .addApi(Awareness.API)
                .addApi(AppIndex.API).build();
        mGoogleApiClient.connect();



        /*
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(LocationbaseActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            //
            //
        }
        */

        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost4);
        tabHost.setup(mLocalActivityManager);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

                boolean increase = false;
                boolean remove = false;
                if (s.equals("Tab1")) {
                    if (increase == true) {
                        Tab16.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_white_24dp);
                        Tab16.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab16.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    } else if (remove == true) {
                        Tab16.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab16.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab16.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {
                        Tab16.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab16.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab16.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }

                } else if (s.equals("Tab2")) {
                    if (increase == true) {
                        Tab17.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_white_24dp);
                        Tab17.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab17.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    } else if (remove == true) {
                        Tab17.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab17.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab17.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {
                        Tab17.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab17.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab17.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }
                } else if (s.equals("Tab3")) {
                    if (increase == true) {
                        Tab18.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_white_24dp);
                        Tab18.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab18.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    } else if (remove == true) {
                        Tab18.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab18.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab18.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {
                        Tab18.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab18.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab18.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }
                } else if (s.equals("Tab4")) {
                    if (increase == true) {
                        Tab19.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_white_24dp);
                        Tab19.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab19.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    } else if (remove == true) {
                        Tab19.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab19.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab19.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {
                        Tab19.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab19.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab19.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }
                } else if (s.equals("Tab5")) {
                    if (increase == true) {
                        Tab20.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_white_24dp);
                        Tab20.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab20.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    } else if (remove == true) {
                        Tab20.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab20.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab20.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {
                        Tab20.IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                        Tab20.DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                        Tab20.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }
                }

            }
        });

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Tab1");
        tab1.setIndicator("Food");
        Intent intent = new Intent(LocationbaseActivity.this, Tab16.class);
        tab1.setContent(intent);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tab2");
        tab2.setIndicator("Drink");
        Intent intent2 = new Intent(LocationbaseActivity.this, Tab17.class);
        tab2.setContent(intent2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("Tab3");
        tab3.setIndicator("Health & Beauty");
        Intent intent3 = new Intent(LocationbaseActivity.this, Tab18.class);
        tab3.setContent(intent3);

        TabHost.TabSpec tab4 = tabHost.newTabSpec("Tab4");
        tab4.setIndicator("HouseHold Item");
        Intent intent4 = new Intent(LocationbaseActivity.this, Tab19.class);
        tab4.setContent(intent4);

        TabHost.TabSpec tab5 = tabHost.newTabSpec("Tab5");
        tab5.setIndicator("Etc.");
        Intent intent5 = new Intent(LocationbaseActivity.this, Tab20.class);
        tab5.setContent(intent5);


        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);
        tabHost.addTab(tab5);


        TabWidget tw = (TabWidget) tabHost.findViewById(android.R.id.tabs);
        View tabView = tw.getChildTabViewAt(2);
        TextView tv = (TextView) tabView.findViewById(android.R.id.title);
        tv.setText("Health & Beauty");
        tv.setTextSize(10);
        tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tv.setLines(3);

        TabWidget tw2 = (TabWidget) tabHost.findViewById(android.R.id.tabs);
        View tabView2 = tw2.getChildTabViewAt(3);
        TextView tv2 = (TextView) tabView2.findViewById(android.R.id.title);
        tv2.setText("HouseHold");
        tv2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tv2.setTextSize(10);
        tv2.setLines(2);

        TabWidget tw3 = (TabWidget) tabHost.findViewById(android.R.id.tabs);
        View tabView3 = tw3.getChildTabViewAt(1);
        TextView tv3 = (TextView) tabView3.findViewById(android.R.id.title);
        tv3.setText("Drink");
        tv3.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tv3.setTextSize(13);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        initSnapshots();



       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initSnapshots() {

        if (ContextCompat.checkSelfPermission(
                LocationbaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    LocationbaseActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    12345
            );
        }else {
            Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {
                            if (!locationResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not get location.");
                                return;
                            }
                            Location location = locationResult.getLocation();
                            Log.i(TAG, "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude());
                        }
                    });


            Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<PlacesResult>() {
                        @Override
                        public void onResult(@NonNull PlacesResult placesResult) {
                            if (!placesResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not get places.");
                                return;
                            }
                            List<PlaceLikelihood> placeLikelihoodList = placesResult.getPlaceLikelihoods();
                            // Show the top 5 possible location results.
                            if (placeLikelihoodList != null) {
                                boolean a = false;
                                for (int i = 0; i < 100 && i < placeLikelihoodList.size(); i++) {
                                    PlaceLikelihood p = placeLikelihoodList.get(i);
                                    //Log.i(TAG, p.getPlace().getName().toString() + ", likelihood: " + p.getLikelihood());
                                    //Log.i(TAG, p.getPlace().getPlaceTypes().toString());

                                    if(p.getPlace().getPlaceTypes().contains(43)){
                                        Toast.makeText(LocationbaseActivity.this,"Supermarket Near you : " + p.getPlace().getName(), Toast.LENGTH_SHORT).show();
                                        String g = p.getPlace().getName().toString();
                                        if(g.contains("Tesco")){
                                            String locate = "Lotus";
                                            Tab16.mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("location").child(locate);
                                        }
                                        a = true;
                                        break;
                                    }else{
                                    }

                                }
                                if(a == false) {
                                    Toast.makeText(LocationbaseActivity.this,"No Supermarket Nearby", Toast.LENGTH_SHORT).show();
                                    a = false;

                                }
                            } else {
                                Log.e(TAG, "Place is null.");
                            }
                        }
                    });
        }
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
        getMenuInflater().inflate(R.menu.locationbase, menu);
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
            Intent bar = new Intent(LocationbaseActivity.this,MainActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(LocationbaseActivity.this,ShoppinglistShowlistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(LocationbaseActivity.this,CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(LocationbaseActivity.this,LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(LocationbaseActivity.this,SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(LocationbaseActivity.this,SettingActivity.class);
            startActivity(bar);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
                // if(place.getPlaceTypes() == )
                String name = String.format("Place : %s", place.getPlaceTypes());
                Log.e(TAG, name);

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Locationbase Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.smart.smarthome/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Locationbase Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.smart.smarthome/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }

    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(!isFinishing());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
    }

}
