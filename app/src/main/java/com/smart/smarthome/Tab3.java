package com.smart.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.LinearLayout;

public class Tab3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear3);
        linearLayout.setBackgroundColor(Color.parseColor("#3982d7"));
    }
}
