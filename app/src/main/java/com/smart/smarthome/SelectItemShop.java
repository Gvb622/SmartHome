package com.smart.smarthome;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

public class SelectItemShop extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    LocalActivityManager mLocalActivityManager;
    private String value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        value = extras.getString("key");

        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost5);
        tabHost.setup(mLocalActivityManager);

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
        Intent intent = new Intent(SelectItemShop.this, Tab11.class);
        intent.putExtra("key",value);
        tab1.setContent(intent);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tab 222");
        tab2.setIndicator("Drink");
        Intent intent2 = new Intent(SelectItemShop.this, Tab12.class);
        intent2.putExtra("key",value);
        tab2.setContent(intent2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("Tab333");
        tab3.setIndicator("Health & Beauty");
        Intent intent3 = new Intent(SelectItemShop.this, Tab13.class);
        intent3.putExtra("key",value);
        tab3.setContent(intent3);

        TabHost.TabSpec tab4 = tabHost.newTabSpec("Tab 444");
        tab4.setIndicator("HouseHold Item");
        Intent intent4 = new Intent(SelectItemShop.this, Tab14.class);
        intent4.putExtra("key",value);
        tab4.setContent(intent4);

        TabHost.TabSpec tab5 = tabHost.newTabSpec("Tab 555");
        tab5.setIndicator("Etc.");
        Intent intent5 = new Intent(SelectItemShop.this, Tab15.class);
        intent5.putExtra("key",value);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            Intent bar = new Intent(SelectItemShop.this, InventoryActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(SelectItemShop.this, ShoppinglistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(SelectItemShop.this, CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(SelectItemShop.this, LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(SelectItemShop.this, SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(SelectItemShop.this, SettingActivity.class);
            startActivity(bar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
