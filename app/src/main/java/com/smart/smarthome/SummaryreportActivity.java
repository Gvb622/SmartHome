package com.smart.smarthome;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

public class SummaryreportActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {


   TextView timeTextView;
   TextView begindateTextView,untildateTextView;
    int m,y,d;
    ImageView monthly;

    static public String begindate;
    static public String untildate;
    static public String month;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    double saleprice2;
    double retailprice2;
    TextView saleprice;
    TextView retailprice;
    TextView savemoney;
    boolean buttononeclick = false;
    boolean buttontwoclick = false;
    FirebaseUser user;







    int currentButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summaryreport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        begindateTextView = (TextView)findViewById(R.id.begin_date_textview);
        untildateTextView = (TextView) findViewById(R.id.until_date_textview);
        ImageView begindateButton = (ImageView)findViewById(R.id.begin_date_button);
        ImageView untildateButton = (ImageView)findViewById(R.id.until_date_button);

        saleprice = (TextView) findViewById(R.id.summary_sale);
        retailprice = (TextView) findViewById(R.id.summary_retail);
        savemoney = (TextView) findViewById(R.id.summary_save);


        begindateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentButton =1;
                java.util.Calendar now = java.util.Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        SummaryreportActivity.this,
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(java.util.Calendar.DAY_OF_MONTH)
                );

                dpd.setAccentColor(Color.parseColor("#d32f2f"));
                dpd.setTitle("Pick Begin date");
                dpd.setMaxDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        untildateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentButton =2;
                java.util.Calendar now = java.util.Calendar.getInstance();
                java.util.Calendar min = java.util.Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        SummaryreportActivity.this,
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(java.util.Calendar.DAY_OF_MONTH)
                );

                dpd.setAccentColor(Color.parseColor("#d32f2f"));
                dpd.setTitle("Pick Until date");
                min.set(y, m-1, d);
                dpd.setMinDate(min);
                dpd.setMaxDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Report report = postSnapshot.getValue(Report.class);
                    saleprice2 += report.getTotalBuyPrice();
                    retailprice2 += Double.parseDouble(report.getTotalPrice());
                }
                saleprice.setText(saleprice2+"");
                retailprice.setText(retailprice2+"");
                savemoney.setText(String.valueOf(retailprice2-saleprice2));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






         monthly = (ImageView) findViewById(R.id.report_month);
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tt = new Intent (SummaryreportActivity.this,MonthlyReportActivity.class);
                startActivity(tt);
            }
        });

        ImageView weekly = (ImageView) findViewById(R.id.report_week);
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentButton = 3;
                java.util.Calendar now = java.util.Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        SummaryreportActivity.this,
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(java.util.Calendar.DAY_OF_MONTH)
                );

                dpd.setAccentColor(Color.parseColor("#d32f2f"));
                dpd.setTitle("Pick Month");
                dpd.setMaxDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        ImageView daily = (ImageView) findViewById(R.id.report_day);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tt = new Intent (SummaryreportActivity.this,DailyReports.class);
                startActivity(tt);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        timeTextView.setText(time);
    }


    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(currentButton==1) {

            String date = "" + dayOfMonth + "/" + (++monthOfYear) + "/" + year;

            if (dayOfMonth == 1) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "01";
                }else{
                    begindate = "" + year + monthOfYear + "01";
                }
            } else if (dayOfMonth == 2) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "02";
                }else{
                    begindate = "" + year + monthOfYear + "02";
                }
            } else if (dayOfMonth == 3) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "03";
                }else{
                    begindate = "" + year + monthOfYear + "03";
                }
            } else if (dayOfMonth == 4) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "04";
                }else{
                    begindate = "" + year + monthOfYear + "04";
                }
            } else if (dayOfMonth == 5) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "05";
                }else{
                    begindate = "" + year + monthOfYear + "05";
                }
            } else if (dayOfMonth == 6) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "06";
                }else{
                    begindate = "" + year + monthOfYear + "06";
                }
            } else if (dayOfMonth == 7) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "07";
                }else{
                    begindate = "" + year + monthOfYear + "07";
                }
            } else if (dayOfMonth == 8) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "08";
                }else{
                    begindate = "" + year + monthOfYear + "08";
                }
            } else if (dayOfMonth == 9) {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + "09";
                }else{
                    begindate = "" + year + monthOfYear + "09";
                }
            } else {
                if(monthOfYear < 10){
                    begindate = "" + year +"0"+ monthOfYear + dayOfMonth;
                }else{
                    begindate = "" + year + monthOfYear + dayOfMonth;
                }
            }
            m = monthOfYear;
            y = year;
            d = dayOfMonth;
            begindateTextView.setText(date);
            buttononeclick = true;



        }else if (currentButton == 2){
            String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
            if (dayOfMonth == 1) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "01";
                }else{
                    untildate = "" + year + monthOfYear + "01";
                }
            } else if (dayOfMonth == 2) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "02";
                }else{
                    untildate = "" + year + monthOfYear + "02";
                }
            } else if (dayOfMonth == 3) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "03";
                }else{
                    untildate = "" + year + monthOfYear + "03";
                }
            } else if (dayOfMonth == 4) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "04";
                }else{
                    untildate = "" + year + monthOfYear + "04";
                }
            } else if (dayOfMonth == 5) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "05";
                }else{
                    untildate = "" + year + monthOfYear + "05";
                }
            } else if (dayOfMonth == 6) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "06";
                }else{
                    untildate = "" + year + monthOfYear + "06";
                }
            } else if (dayOfMonth == 7) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "07";
                }else{
                    untildate = "" + year + monthOfYear + "07";
                }
            } else if (dayOfMonth == 8) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "08";
                }else{
                    untildate = "" + year + monthOfYear + "08";
                }
            } else if (dayOfMonth == 9) {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + "09";
                }else{
                    untildate = "" + year + monthOfYear + "09";
                }
            } else {
                if(monthOfYear < 10){
                    untildate = "" + year +"0"+ monthOfYear + dayOfMonth;
                }else{
                    untildate = "" + year + monthOfYear + dayOfMonth;
                }
            }
            System.out.println(untildate);

            untildateTextView.setText(date);
            buttontwoclick = true;

        }else{

            if (monthOfYear == 0){
                month = "01";
            }else if (monthOfYear== 1) {
                month = "02";
            }else if (monthOfYear== 2) {
                month = "03";
            }else if (monthOfYear== 3) {
                month = "04";
            }else if (monthOfYear== 4) {
                month = "05";
            }else if (monthOfYear== 5) {
                month = "06";
            }else if (monthOfYear== 6) {
                month = "07";
            }else if (monthOfYear== 7) {
                month = "08";
            }else if (monthOfYear== 8) {
                month = "09";
            }else if (monthOfYear== 9) {
                month = "10";
            }else if (monthOfYear== 10) {
                month = "11";
            }else if (monthOfYear== 11) {
                month = "12";
            }
            System.out.println(month);
            Intent tt = new Intent (SummaryreportActivity.this,WeeklyReports.class);
            startActivity(tt);

        }
    }

    public void onDateSet2(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        untildateTextView.setText(date);
    }




    public void onClick(View view) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.summaryreport, menu);
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
            Intent bar = new Intent(SummaryreportActivity.this,MainActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(SummaryreportActivity.this,ShoppinglistShowlistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(SummaryreportActivity.this,CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(SummaryreportActivity.this,LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(SummaryreportActivity.this,SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(SummaryreportActivity.this,SettingActivity.class);
            startActivity(bar);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
