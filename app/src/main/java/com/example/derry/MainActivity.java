package com.example.derry;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "fsd";
    private ProgressBar pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button btn = findViewById(R.id.btn);
        pro = findViewById(R.id.fs);
//        btn.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "onClick: ");
//            }
//        });
//        btn.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                return false;
//            }
//        });
    }

    public void fskdjf(View view) {
        if(pro.getVisibility()==View.GONE){
            pro.setVisibility(View.VISIBLE);
        }
        else {
            pro.setVisibility(View.GONE);
        }
    }
}