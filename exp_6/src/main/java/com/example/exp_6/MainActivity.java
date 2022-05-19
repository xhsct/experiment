package com.example.exp_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }

    public void add(View view) {
        Intent intent = new Intent(this,MainActivity2.class);
        startActivityForResult(intent,1);
    }

    public void delete(View view) {
        Intent intent = new Intent(this,MainActivity3.class);
        startActivityForResult(intent,1);
    }

}