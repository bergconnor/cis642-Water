package com.example.myfirst.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
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
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final int CAMERA_REQUEST = 1888;
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

    /*public void doPic(View view) {
        Intent intent = new Intent(this, takingPic.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "Will be taking a picture here";
        intent.putExtra(EXTRA_MESSAGE, message);
        int requestCode = 1571;
        startActivityForResult(intent, requestCode);


    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bMap, pic;
        if (data != null) {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                pic = (Bitmap) data.getExtras().get("data");
                bMap = Bitmap.createBitmap(pic, 0, 0, pic.getWidth()/2, pic.getHeight()/2);

                Intent intent = new Intent(this, SheetsActivity.class);
                Bundle extras = new Bundle();

//                String first =
//                        "http://api.wunderground.com/api/0375e5a05ebf577c/conditions/q/";
//                String lat = "39.183609";
//                String lon = "-96.571671";
//                String last = ".json";
//                String weatherQuery = first + lat + "," + lon + last;
//                JSONTask task = new JSONTask();
//                task.execute(weatherQuery);

                String contents = "";
                String testName = "";
                String serialNum = "";

                try {
                    int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];

                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    Reader reader = new MultiFormatReader();
                    Result result = reader.decode(bitmap);
                    contents = result.getText();
                    String lines[] = contents.split("\\r?\\n");
                    testName = lines[0].replace("Test", "");
                    serialNum = lines[1].replace("Serial No. ", "");

                    String date = DateFormat.getDateTimeInstance().format(new Date());
                    Double latitude = 39.190611;
                    Double longitude = -96.584056;

                    String temp = "61.4";//weatherData[0];//"61.2";
                    String precip = "0.0";

                    extras.putString("EXTRA_DATE", date);
                    extras.putDouble("EXTRA_LATITUDE", latitude);
                    extras.putDouble("EXTRA_LONGITUDE", longitude);
                    extras.putString("EXTRA_TEMPERATURE", temp);
                    extras.putString("EXTRA_PRECIPITATION", precip);
                    extras.putString("EXTRA_TEST", testName);
                    extras.putString("EXTRA_SERIAL", serialNum);

                    intent.putExtras(extras);
                    startActivity(intent);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    new AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage(ex.toString())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
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