package com.example.myfirst.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
            dispatchCameraIntent();
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

    public void dispatchCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image.jpg");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public String getPreviousDay() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String yesterday = dateFormat.format(calendar.getTime());

        return yesterday;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK    ) {
            switch(requestCode) {
                case CAMERA_REQUEST:
                    Intent locationIntent = new Intent(this, LocationActivity.class);

                    File file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "image.jpg");
                    Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);

                    Matrix matrix = new Matrix();
                    matrix.postRotate(0);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    Bitmap croppedBitmap = Bitmap.createBitmap(rotatedBitmap, 0, 0,
                            (rotatedBitmap.getWidth()/2), (rotatedBitmap.getHeight()/2));
                    String contents = scanQRCode(croppedBitmap);
                    if (contents.length() < 1) {
                        dispatchCameraIntent();
                    }
                    else {
                        locationIntent.putExtra("QRCODE", contents);
                        startActivityForResult(locationIntent, LOCATION_REQUEST);
                    }
                    break;

                case LOCATION_REQUEST:
                    Intent sheetsIntent = new Intent(this, SheetsActivity.class);

                    String code = data.getStringExtra("QRCODE");
                    String[] testInfo = code.split("\n");
                    String name = testInfo[0].trim();
                    String serial = testInfo[1].replaceAll("\\D+","");

                    String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                    String yesterday = getPreviousDay();
                    String[] yesterdaySplit = yesterday.split("/");
                    String month = yesterdaySplit[0];
                    String day = yesterdaySplit[1];
                    String year = yesterdaySplit[2];

                    String latitude = data.getStringExtra("LATITUDE");
                    String longitude = data.getStringExtra("LONGITUDE");
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
                    String temperature = "";
                    String precipitation = "";

                    try {
                        temperature = new WeatherTask().execute(query1).get();
                        precipitation = new WeatherTask().execute(query2).get();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    sheetsIntent.putExtra("EXTRA_DATE", date);
                    sheetsIntent.putExtra("EXTRA_LATITUDE", latitude);
                    sheetsIntent.putExtra("EXTRA_LONGITUDE", longitude);
                    sheetsIntent.putExtra("EXTRA_TEMPERATURE", temperature);
                    sheetsIntent.putExtra("EXTRA_PRECIPITATION", precipitation);
                    sheetsIntent.putExtra("EXTRA_TEST", name);
                    sheetsIntent.putExtra("EXTRA_SERIAL", serial);

                    startActivity(sheetsIntent);
                    break;
            }
        }
    }

    private String scanQRCode(Bitmap image) {
        String contents;
        try {

            int[] intArray = new int[image.getWidth() * image.getHeight()];

            image.getPixels(intArray, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            LuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), intArray);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);
            contents = result.getText();
        }
        catch (Exception ex) {
            contents = "";
        }
        return contents;
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