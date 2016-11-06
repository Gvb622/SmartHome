package com.smart.smarthome;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShoppinglistShowlistActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    LocalActivityManager mLocalActivityManager;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String value;
    static final int GET_BAR_CODE = 1;

    static public boolean increase;
    static public boolean remove ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist_showlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        remove = false;
        increase = false;



        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost4);
        tabHost.setup(mLocalActivityManager);


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

                if(s.equals("Tab1")){
                    if(remove == true){
                        Tab6.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    }else{
                        Tab6.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }

                }else if(s.equals("Tab2")) {
                    if (remove == true) {

                        Tab7.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {
                        Tab7.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }
                }else if(s.equals("Tab3")) {
                    if (remove == true) {

                        Tab8.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {

                        Tab8.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }
                }else if(s.equals("Tab4")) {
                   if (remove == true) {

                        Tab9.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {

                        Tab9.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }
                }else if(s.equals("Tab5")) {
                   if (remove == true) {

                        Tab10.RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    } else {

                        Tab10.RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                    }
                }

            }
        });


        TabHost.TabSpec tab1 = tabHost.newTabSpec("Tab1");
        tab1.setIndicator("Food");
        Intent intent = new Intent(ShoppinglistShowlistActivity.this, Tab6.class);
        tab1.setContent(intent);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tab2");
        tab2.setIndicator("Drink");
        Intent intent2 = new Intent(ShoppinglistShowlistActivity.this, Tab7.class);
        tab2.setContent(intent2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("Tab3");
        tab3.setIndicator("Health & Beauty");
        Intent intent3 = new Intent(ShoppinglistShowlistActivity.this, Tab8.class);
        tab3.setContent(intent3);

        TabHost.TabSpec tab4 = tabHost.newTabSpec("Tab4");
        tab4.setIndicator("HouseHold Item");
        Intent intent4 = new Intent(ShoppinglistShowlistActivity.this, Tab9.class);
        tab4.setContent(intent4);

        TabHost.TabSpec tab5 = tabHost.newTabSpec("Tab5");
        tab5.setIndicator("Etc.");
        Intent intent5 = new Intent(ShoppinglistShowlistActivity.this, Tab10.class);
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
        tv.setTextSize(10);
        tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        tv.setLines(3);

        TabWidget tw2 = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        View tabView2 = tw2.getChildTabViewAt(3);
        TextView tv2 = (TextView)tabView2.findViewById(android.R.id.title);
        tv2.setText("HouseHold");
        tv2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        tv2.setTextSize(10);
        tv2.setLines(2);

        TabWidget tw3 = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        View tabView3 = tw3.getChildTabViewAt(1);
        TextView tv3 = (TextView)tabView3.findViewById(android.R.id.title);
        tv3.setText("Drink");
        tv3.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        tv3.setTextSize(13);


        firebaseAuth = FirebaseAuth.getInstance();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            // Handle the camera action
            Intent bar = new Intent(ShoppinglistShowlistActivity.this, MainActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(ShoppinglistShowlistActivity.this, ShoppinglistShowlistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(ShoppinglistShowlistActivity.this, CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(ShoppinglistShowlistActivity.this, LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(ShoppinglistShowlistActivity.this, SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(ShoppinglistShowlistActivity.this, SettingActivity.class);
            startActivity(bar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
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
}
