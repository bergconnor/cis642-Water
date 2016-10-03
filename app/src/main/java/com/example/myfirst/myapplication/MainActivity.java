package com.example.myfirst.myapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void doPic(View view) {
        Intent intent = new Intent(this, takingPic.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "Will be taking a picture here";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
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