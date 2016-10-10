package com.example.myfirst.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.text.DateFormat;
import java.util.Date;

public class takingPic extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;

    private String QRCodeText;


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
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        String contents = "";

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                try {
                    Bitmap bMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];

                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    Reader reader = new MultiFormatReader();
                    Result result = reader.decode(bitmap);
                    contents = result.getText();
                }
                catch (Exception ex) {

                }
            }
        }
        String date = DateFormat.getDateTimeInstance().format(new Date());

        String latitude = "42.032974";
        String longitude = "-103.820801";

        extras.putString("EXTRA_DATE", date);
        extras.putString("EXTRA_LATITUDE", latitude);
        extras.putString("EXTRA_LONGITUDE", longitude);
        extras.putString("EXTRA_QRCODE", contents);
        intent.putExtras(extras);

        setResult(RESULT_OK, intent);
        finish();
    }
}
