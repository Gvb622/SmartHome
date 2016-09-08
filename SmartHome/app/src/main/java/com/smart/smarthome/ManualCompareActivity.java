package com.smart.smarthome;

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

public class ManualCompareActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button Cancel;
    EditText nameProductCompare;
    EditText pricecomparemanual;
    EditText volumecomparemanual;
    EditText unitcomparemanual;
    Button AddCompare;
    String nameproduct;
    Double pricecompare;
    Integer volumecompare,unitcompare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_compare);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Cancel = (Button)findViewById(R.id.CancelCompare);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bar = new Intent(ManualCompareActivity.this,CalculatorActivity.class);
                startActivity(bar);
            }
        });

        nameProductCompare = (EditText) findViewById(R.id.nameProductCompare);
        pricecomparemanual = (EditText) findViewById(R.id.pricecomparemanual);
        volumecomparemanual = (EditText) findViewById(R.id.volumecomparemanual);
        unitcomparemanual = (EditText) findViewById(R.id.unitcomparemanual);
        AddCompare = (Button) findViewById(R.id.AddCompare);


        AddCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameproduct = nameProductCompare.getText().toString();
                pricecompare = Double.parseDouble(pricecomparemanual.getText().toString());
                volumecompare = Integer.parseInt(volumecomparemanual.getText().toString());
                unitcompare = Integer.parseInt(unitcomparemanual.getText().toString());

                Intent i = new Intent(getApplicationContext(), CompareActivity.class);

                i.putExtra("nameproduct",nameproduct);
                i.putExtra("pricecompare",pricecompare);
                i.putExtra("volumecompare",volumecompare);
                i.putExtra("unitcompare",unitcompare);
                startActivity(i);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.manual_compare, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
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
            Intent bar = new Intent(ManualCompareActivity.this,InventoryActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(ManualCompareActivity.this,ShoppinglistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(ManualCompareActivity.this,CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(ManualCompareActivity.this,LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(ManualCompareActivity.this,SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(ManualCompareActivity.this,SettingActivity.class);
            startActivity(bar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
