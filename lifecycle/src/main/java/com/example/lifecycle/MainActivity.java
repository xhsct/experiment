package com.example.lifecycle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private final String Tag = "MainActivity";
    AlertDialog tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(Tag, Tag+"-->onCreate()");
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

    public void turn(View view) {
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
    }

    public void dialog(View view) {
        tips = new AlertDialog.Builder(MainActivity.this).setTitle("提示信息").setMessage("对话框弹出").create();
        tips.show();
    }
}