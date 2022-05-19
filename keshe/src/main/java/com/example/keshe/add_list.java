package com.example.keshe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

public class add_list extends AppCompatActivity {
    public EditText title_list;
    public EditText content_list;
    public String curdata;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        TextView textView = findViewById(R.id.data);
        Intent intent =  getIntent();
        curdata = intent.getStringExtra("curdata");
        textView.setText(curdata);
    }

    public void tianjia(View view) {
        title_list = findViewById(R.id.title_list);
        content_list = findViewById(R.id.content_list);
        String title = title_list.getText().toString();
        String content = title_list.getText().toString();
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        db = helper.getWritableDatabase();
        db.execSQL("insert into todolist values(null , ? , ?,?,null,null)"
                , new String[]{curdata, title,content});
        db.close();
        startActivity(new Intent(this,MainActivity.class));

    }

    public void cancel(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }
}