package com.example.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity2 extends AppCompatActivity {

    private final String Tag = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(Tag, Tag+"-->onStart()");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(Tag, Tag+"-->onRestart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(Tag, Tag+"-->onResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(Tag, Tag+"-->onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(Tag, Tag+"-->onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(Tag, Tag+"-->onDestroy()");
    }
}