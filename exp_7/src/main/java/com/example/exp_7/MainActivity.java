package com.example.exp_7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    private ListView img_list;
    private Button photo_btn;

    private List<String> img_names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_list = findViewById(R.id.img_list);
        photo_btn = findViewById(R.id.photo_btn);

        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                startActivityForResult(intent,1);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        for (String img_name : img_names) {
            Log.d("ttt","img:  "+ img_name);
        }
        adapter adapter = new adapter(this, img_names);
        img_list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                img_names.add(data.getStringExtra("photoName"));
            }
        }
    }
}