package com.smart.smarthome;

import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    static final int GET_BAR_CODE = 1;


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


        /*
        TextView textTab = new TextView(this);
        textTab.setText("Food");
        textTab.setTextSize(18);
        textTab.setTypeface(Typeface.DEFAULT_BOLD);
        textTab.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        TextView textTab2 = new TextView(this);
        textTab2.setText("Drink");
        textTab2.setTextSize(18);
        textTab2.setTypeface(Typeface.DEFAULT_BOLD);
        textTab2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        TextView textTab3 = new TextView(this);
        textTab3.setText("Food");
        textTab3.setTextSize(18);
        textTab3.setTypeface(Typeface.DEFAULT_BOLD);
        textTab.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

*/

        TextView textTab = new TextView(this);
        textTab.setText("Health & Beauty");
        textTab.setTextSize(12);
        textTab.setTypeface(Typeface.DEFAULT_BOLD);
        textTab.setTextColor(Color.BLACK);
        textTab.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        TextView textTab2 = new TextView(this);
        textTab2.setText("Household");
        textTab2.setTextSize(12);
        textTab2.setTextColor(Color.BLACK);
        textTab2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("Tab 111");
        tab1.setIndicator("Food");
        Intent intent = new Intent(MainActivity.this, Tab1.class);
        tab1.setContent(intent);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tab 222");
        tab2.setIndicator("Drink");
        Intent intent2 = new Intent(MainActivity.this, Tab2.class);
        tab2.setContent(intent2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("Tab333");
        tab3.setIndicator("Health & Beauty");
        Intent intent3 = new Intent(MainActivity.this, Tab3.class);
        tab3.setContent(intent3);

        TabHost.TabSpec tab4 = tabHost.newTabSpec("Tab 444");
        tab4.setIndicator("HouseHold Item");
        Intent intent4 = new Intent(MainActivity.this, Tab4.class);
        tab4.setContent(intent4);

        TabHost.TabSpec tab5 = tabHost.newTabSpec("Tab 555");
        tab5.setIndicator("Etc.");
        Intent intent5 = new Intent(MainActivity.this, Tab5.class);
        tab5.setContent(intent5);



        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);
        tabHost.addTab(tab5);

        TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        View tabView = tw.getChildTabViewAt(2);
        TextView tv = (TextView)tabView.findViewById(android.R.id.title);
        tv.setText("Health & Beauty");
        tv.setTextSize(12);
        tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        tv.setSingleLine();

        TabWidget tw2 = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        View tabView2 = tw2.getChildTabViewAt(3);
        TextView tv2 = (TextView)tabView2.findViewById(android.R.id.title);
        tv2.setText("HouseHold");
        tv2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        tv2.setTextSize(12);








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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_BAR_CODE) {
            if (resultCode == RESULT_OK) {
                final String barcodeValue2 = data.getStringExtra("Barcode");
                DatabaseReference mDatabse = FirebaseDatabase.getInstance().getReference().child("system").child("items");
                Query queryRef = mDatabse.orderByChild("Barcode").equalTo(barcodeValue2);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("OK ?");
                            builder.setMessage(barcodeValue2);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(MainActivity.this, ShowBarcode.class);
                                    i.putExtra("key", postSnapshot.getKey());
                                    startActivity(i);
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            } else {
                Toast.makeText(MainActivity.this, "Barcode not Found", Toast.LENGTH_LONG).show();
            }
        }
    }

}