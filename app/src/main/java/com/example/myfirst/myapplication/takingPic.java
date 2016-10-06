package com.example.myfirst.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class takingPic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_pic);  //activity_taking_pic

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_taking_pic);
        layout.addView(textView);

    }

    public void sendInfo(View view) {

        Intent intent = new Intent();
        Bundle extras = new Bundle();

        String date = DateFormat.getDateTimeInstance().format(new Date());

        String latitude = "42.032974";
        String longitude = "-103.820801";

        extras.putString("EXTRA_DATE", date);
        extras.putString("EXTRA_LATITUDE", latitude);
        extras.putString("EXTRA_LONGITUDE", longitude);
        intent.putExtras(extras);

        setResult(RESULT_OK, intent);
        finish();
    }
}
