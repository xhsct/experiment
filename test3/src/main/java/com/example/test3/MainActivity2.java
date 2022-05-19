package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent =  getIntent();
        String message = intent.getStringExtra("message");
        Toast.makeText(this,"message:"+message,Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("message","我是回传的信息");
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}