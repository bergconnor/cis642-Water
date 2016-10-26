package com.example.myfirst.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final int CAMERA_REQUEST = 1571;
    private static final int LOCATION_REQUEST = 1838;

    private Button takingPicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takingPicBtn = (Button) this.findViewById(R.id.button14);
        takingPicBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }
    private static final String LOG_TAG = "MyActivity";
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
       // EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "sent ";//editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

        File myfile = getAlbumStorageDir("myalbum");
    }

    //ross danny
    /** Called when the user clicks the Send button */
    public void doNass(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "did tou beliffe NASS!!??";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent;
        String latitude, longitude, temperature, precipitation;

        if (data != null && resultCode == Activity.RESULT_OK    ) {
            switch(requestCode) {
                case CAMERA_REQUEST:
                    intent = new Intent(this, LocationActivity.class);
                    startActivityForResult(intent, LOCATION_REQUEST);
                    break;

                case LOCATION_REQUEST:
                    intent = new Intent(this, SheetsActivity.class);

                    String date = DateFormat.getDateTimeInstance().format(new Date());
                    String formattedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
                    String[] dateSplit = formattedDate.split("/");
                    String month = dateSplit[0];
                    String day = dateSplit[1];
                    String year = "20" + dateSplit[2];

                    latitude = data.getStringExtra("LATITUDE");
                    longitude = data.getStringExtra("LONGITUDE");
                    List<Address> addresses;
                    String city = "";
                    String state = "";
                    try {
                        Double lat = Double.parseDouble(latitude);
                        Double lon = Double.parseDouble(longitude);
                        Geocoder gcd = new Geocoder(this, Locale.getDefault());
                        addresses = gcd.getFromLocation(lat, lon, 1);
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        addresses = null;
                    }

                    String start =
                        "http://api.wunderground.com/api/0375e5a05ebf577c/";
                    String data1 = "conditions/q/";
                    String data2 = "history_" + year + month + day + "/q/";
                    String end = ".json";
                    String query1 = start + data1 + latitude + "," + longitude + end;
                    String query2 = start + data2 + state + "/" + city + end;
                    temperature = "";
                    precipitation = "";

                    try {
                        temperature = new WeatherTask().execute(query1).get();
                        precipitation = new WeatherTask().execute(query2).get();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (precipitation.length() < 1) {
                        precipitation = "0.00";
                    }

                    String name = "Nitrate";
                    String serial = "00001";

                    intent.putExtra("EXTRA_DATE", date);
                    intent.putExtra("EXTRA_LATITUDE", latitude);
                    intent.putExtra("EXTRA_LONGITUDE", longitude);
                    intent.putExtra("EXTRA_TEMPERATURE", temperature);
                    intent.putExtra("EXTRA_PRECIPITATION", precipitation);
                    intent.putExtra("EXTRA_TEST", name);
                    intent.putExtra("EXTRA_SERIAL", serial);

                    startActivity(intent);
                    break;
            }
        }
    }

    public void doHistory(View view) {
        Intent intent = new Intent(this, myHistoryAct.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "We will display History here";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void doQuit(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "We should be quiting now";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void getAbout(View view) {
        Intent intent = new Intent(this, aboutAct.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "phone info and app purpose here";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }
}