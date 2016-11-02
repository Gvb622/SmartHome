package com.smart.smarthome;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import im.dacer.androidcharts.LineView;


public class MonthlyReportActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    int randomint = 12;
    int pricebuy1;
    int pricebuy2;
    int pricebuy3;
    int pricebuy4;
    int pricebuy5;
    int pricebuy6;
    int pricebuy7;
    int pricebuy8;
    int pricebuy9;
    int pricebuy10;
    int pricebuy11;
    int pricebuy12;

    int priceretail1;
    int priceretail2;
    int priceretail3;
    int priceretail4;
    int priceretail5;
    int priceretail6;
    int priceretail7;
    int priceretail8;
    int priceretail9;
    int priceretail10;
    int priceretail11;
    int priceretail12;

    ArrayList<Integer> dataList;
    ArrayList<Integer> dataList2;

    LineView lineView;


    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        lineView = (LineView)findViewById(R.id.line_view);

        //must*
        ArrayList<String> test = new ArrayList<String>();

        test.add("Jan");
        test.add("Feb");
        test.add("Mar");
        test.add("Apr");
        test.add("May");
        test.add("Jun");
        test.add("Jul");
        test.add("Aug");
        test.add("Sep");
        test.add("Oct");
        test.add("Nov");
        test.add("Dec");

        lineView.setBottomTextList(test);
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_All);

        /*Button lineButton = (Button)findViewById(R.id.line_button);
        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomSet(lineView);

            }
        });*/

        randomSet(lineView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void randomSet(final LineView lineView){


        dataList = new ArrayList<Integer>();
        dataList2 = new ArrayList<Integer>();


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report");
        Query queryRef = mDatabase.orderByChild("Time").startAt(SummaryreportActivity.begindate).endAt(SummaryreportActivity.untildate);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Report report = postSnapshot.getValue(Report.class);
                    if(report.getMonth().equals("01")){
                        pricebuy1 += report.getTotalBuyPrice();
                        priceretail1 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("02")){
                        pricebuy2 += report.getTotalBuyPrice();
                        priceretail2 += Double.parseDouble(report.getTotalPrice());


                    }else if(report.getMonth().equals("03")){
                        pricebuy3 += report.getTotalBuyPrice();
                        priceretail3 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("04")){
                        pricebuy4 += report.getTotalBuyPrice();
                        priceretail4 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("05")){
                        pricebuy5 += report.getTotalBuyPrice();
                        priceretail5 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("06")){
                        pricebuy6 += report.getTotalBuyPrice();
                        priceretail6 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("07")){
                        pricebuy7 += report.getTotalBuyPrice();
                        priceretail7 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("08")){
                        pricebuy8 += report.getTotalBuyPrice();
                        priceretail8 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("09")){
                        pricebuy9 += report.getTotalBuyPrice();
                        priceretail9 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("10")){
                        pricebuy10 += report.getTotalBuyPrice();
                        priceretail10 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("11")){
                        pricebuy11 += report.getTotalBuyPrice();
                        priceretail11 += Double.parseDouble(report.getTotalPrice());

                    }else if(report.getMonth().equals("12")){
                        pricebuy12 += report.getTotalBuyPrice();
                        priceretail12 += Double.parseDouble(report.getTotalPrice());

                    }
                }
                dataList.add(pricebuy1);
                dataList.add(pricebuy2);
                dataList.add(pricebuy3);
                dataList.add(pricebuy4);
                dataList.add(pricebuy5);
                dataList.add(pricebuy6);
                dataList.add(pricebuy7);
                dataList.add(pricebuy8);
                dataList.add(pricebuy9);
                dataList.add(pricebuy10);
                dataList.add(pricebuy11);
                dataList.add(pricebuy12);

                dataList2.add(priceretail1);
                dataList2.add(priceretail2);
                dataList2.add(priceretail3);
                dataList2.add(priceretail4);
                dataList2.add(priceretail5);
                dataList2.add(priceretail6);
                dataList2.add(priceretail7);
                dataList2.add(priceretail8);
                dataList2.add(priceretail9);
                dataList2.add(priceretail10);
                dataList2.add(priceretail11);
                dataList2.add(priceretail12);

                ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
                dataLists.add(dataList);
                dataLists.add(dataList2);
                lineView.setDataList(dataLists);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        getMenuInflater().inflate(R.menu.monthly_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            // Handle the camera action
            Intent bar = new Intent(MonthlyReportActivity.this,MainActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(MonthlyReportActivity.this,ShoppinglistShowlistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(MonthlyReportActivity.this,CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(MonthlyReportActivity.this,LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(MonthlyReportActivity.this,SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(MonthlyReportActivity.this,SettingActivity.class);
            startActivity(bar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
