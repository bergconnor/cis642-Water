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


import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.*;
import com.google.android.gms.common.api.GoogleApiClient.*;
//import gms.drive.*;
import com.google.android.gms.drive.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.content.pm.PackageManager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.location.*;

public class aboutAct extends FragmentActivity
        implements ConnectionCallbacks,OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this ,
                            this )
                    .addApi(LocationServices.API)

                    //.addApi(Drive.API)
                    //.addScope(Drive.SCOPE_FILE)
                    .build();}

        String s1 = "asf";//android.os.Build.VERSION.SDK;      // API Level
        String s2 = android.os.Build.DEVICE;           // Device
        String s3 = android.os.Build.MODEL;            // Model
        String s4 = android.os.Build.PRODUCT;          // Product

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        textView = new TextView(this);
        textView.setTextSize(40);
        //message = s1+"/n***"+s2+"/n***"+s3+"/n***"+s4+"/n***";
        message = "";
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        //textView.setText(currentDateTimeString+"ttttt"+ message);

       // findViewById("myS1".setText());

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_about);
        layout.addView(textView);
    }

    public void getMyTimeDate(View view) {


        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        textView.setText(currentDateTimeString);
        //TextView.setText(currentDateTimeString);

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    //@Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // ...
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        //TextView mLatitudeText = new TextView(this);
        //TextView mLongitudeText = new TextView(this);
            /*
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    123 );
            // LocationServices.MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }
        /*
         mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            }
*/      /*
        Intent intent = getIntent();
        String message = "khglkj";//intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_mu2);
        layout.addView(textView);
*/
    }

    @Override
    public void onConnectionSuspended(int cause){}


    public void dontdonanything(View view) {
        // Do something in response to button
        //Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //TextView textView = (TextView) findViewById(R.id.textView);
        // textView.setTextSize(40);
        //textView.setText("got something in!!");

        //ViewGroup layout = (ViewGroup) findViewById(R.id.activity_mu2);
        //ayout.addView(textView);

    }
    public void sendMessage(View view) {
        // Do something in response to button click

        //TextView textView = (TextView) findViewById(R.id.textView);
        //textView.setTextSize(40);
        //textView.setText("got something in!!");

        TextView mLatitudeText = (TextView) findViewById(R.id.textView8);
        TextView mLongitudeText = (TextView) findViewById(R.id.textView7);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    123 );
            // LocationServices.MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }
        TextView textView = new TextView(this);
        textView.setTextSize(70);
        textView.setText("a");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //mLongitudeText.setText("1");
        mLongitudeText.setTextSize(40);
        mLatitudeText.setTextSize(40);

        mLatitudeText.setText("Latitude: "+"is not computed yet");

        mLongitudeText.setText("Longitude"+"is not computed yet");

        if (mLastLocation != null) {
            mLatitudeText.setText("Latitude: "+String.valueOf(mLastLocation.getLatitude()));

            mLongitudeText.setText("Longitude"+String.valueOf(mLastLocation.getLongitude()));
            //mLongitudeText.setText("2");
            textView.setText("b");
        }
        //mLongitudeText.setText("2");
        //TextView textView = mLatitudeText;
        //Intent intent = getIntent();
        String message = "khglkj";//intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //textView.setTextSize(10);
        //textView.setText("a");
        //mLongitudeText.setText("heehhehe");
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_about);
        layout.addView(textView);
        //layout.addView(mLongitudeText);


        //mLatitudeText.setText("got nothign :(( in!!");
    }
}
