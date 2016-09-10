package com.smart.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;


public class Tab2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear2);
        linearLayout.setBackgroundColor(Color.parseColor("#00b06b"));
    }
}
