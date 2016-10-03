package com.example.myfirst.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class aboutAct extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        String s1 = "asf";//android.os.Build.VERSION.SDK;      // API Level
        String s2 = android.os.Build.DEVICE;           // Device
        String s3 = android.os.Build.MODEL;            // Model
        String s4 = android.os.Build.PRODUCT;          // Product

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        textView = new TextView(this);
        textView.setTextSize(40);
        message = s1+"/n***"+s2+"/n***"+s3+"/n***"+s4+"/n***";
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        textView.setText(currentDateTimeString+"ttttt"+ message);

       // findViewById("myS1".setText());

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_about);
        layout.addView(textView);
    }

    public void getMyTimeDate(View view) {


        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        textView.setText(currentDateTimeString);
        //TextView.setText(currentDateTimeString);

    }
}
