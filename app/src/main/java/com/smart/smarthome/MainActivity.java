package com.smart.smarthome;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    ImageView inputbtn;
    ImageView barcodebtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonAdditem;
    private EditText editTextProductName;
    private EditText editTextBarcode;

    LocalActivityManager mLocalActivityManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost2);
        tabHost.setup(mLocalActivityManager);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("Tab 111");
        tab1.setIndicator("Tab1");
        Intent intent = new Intent(MainActivity.this, Tab1.class);
        tab1.setContent(intent);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tab 222");
        tab2.setIndicator("Tab2");
        Intent intent2 = new Intent(MainActivity.this, Tab2.class);
        tab2.setContent(intent2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("Tab 333");
        tab3.setIndicator("Tab3");
        Intent intent3 = new Intent(MainActivity.this, Tab3.class);
        tab3.setContent(intent3);


        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);



        /*TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("Jelly Bean")
                .setContent(new Intent(this, Tab2.class));
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3")
                .setIndicator("Gingerbread")
                .setContent(new Intent(this, Tab3.class));

        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);
        tabHost.addTab(tabSpec3);
        */






    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");


        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }



//        buttonLogout.setOnClickListener(this);
  //      buttonAdditem.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(!isFinishing());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
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
            Intent bar = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(MainActivity.this, ShoppinglistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(MainActivity.this, LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(MainActivity.this, SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(bar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}