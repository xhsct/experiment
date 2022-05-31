package com.example.keshe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class statistic extends AppCompatActivity {
    public ImageButton data_behind,data_before;
    public Calendar calendar;
    public TextView show_data_before,show_data_behind;
    public String da_before,da_behind;
    public int start,postpone,done;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        //获得数据库对象helper.get...
        db = helper.getReadableDatabase();
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        da_behind = dateFormat.format(date);
        StringBuffer show_behind = new StringBuffer(da_behind);
        show_behind.insert(8,"日");
        show_behind.insert(6,"月");
        show_behind.insert(4,"年");
        int a = Integer.parseInt(da_behind)-100;
        da_before = a+"";
        StringBuffer show_before = new StringBuffer(da_before);
        show_before.insert(8,"日");
        show_before.insert(6,"月");
        show_before.insert(4,"年");
        data_behind = findViewById(R.id.data_behind);
        data_before = findViewById(R.id.data_before);
        show_data_before = findViewById(R.id.show_data_before);
        show_data_behind = findViewById(R.id.show_data_behind);
        show_data_before.setText(show_before);
        show_data_behind.setText(show_behind);
        count(da_before,da_behind);
        calendar = Calendar.getInstance();
        data_behind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "onClick: ");
                new DatePickerDialog(statistic.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        setTitle(year + "-" + monthOfYear + "-" + dayOfMonth);
                        String curdata = "";
                        String data = year+"年";
                        curdata += year+"";
                        Log.e("data", "0"+monthOfYear);
                        if (monthOfYear/10 == 0){
                            data += "0"+monthOfYear+"月";
                            curdata += "0"+monthOfYear;
                        }
                        else {
                            data += monthOfYear+"月";
                            curdata += monthOfYear+"";
                        }
                        if (dayOfMonth/10 == 0){
                            data += "0"+dayOfMonth+"日";
                            curdata += "0"+dayOfMonth;
                        }
                        else {
                            data += dayOfMonth+"日";
                            curdata += dayOfMonth+"";
                        }
                        da_behind = curdata;
                        show_data_behind.setText(data);
                        count(da_before,da_behind);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        data_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(statistic.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        setTitle(year + "-" + monthOfYear + "-" + dayOfMonth);
                        String curdata = "";
                        String data = year+"年";
                        curdata += year+"";
                        Log.e("data", "0"+monthOfYear);
                        if (monthOfYear/10 == 0){
                            data += "0"+monthOfYear+"月";
                            curdata += "0"+monthOfYear;
                        }
                        else {
                            data += monthOfYear+"月";
                            curdata += monthOfYear+"";
                        }
                        if (dayOfMonth/10 == 0){
                            data += "0"+dayOfMonth+"日";
                            curdata += "0"+dayOfMonth;
                        }
                        else {
                            data += dayOfMonth+"日";
                            curdata += dayOfMonth+"";
                        }
                        show_data_before.setText(data);
                        da_before = curdata;
                        count(da_before,da_behind);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    private void count(String data_before,String data_behind) {
        start = 0;
        done = 0;
        postpone = 0;
        Cursor cursor = db.query(MySqliteOpenHelper.SQlite.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range")
            String time = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.time));
//            Log.d("TAG", (time.compareTo(data_before) == 1)+"");
            @SuppressLint("Range")
            String state = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.state));
            if (Integer.parseInt(data_before)<=Integer.parseInt(time) && Integer.parseInt(time)<=Integer.parseInt(data_behind)){
                Log.d("TAG", state+"");
                if (state.equals("start")){
                    start += 1;
                }else if(state.equals("done")){
                    done += 1;
                }else if(state.equals("postpone")){
                    postpone += 1;
                }
            }
        }
        Log.d("TAG", start+""+done+""+postpone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exchange) {
            startActivity(new Intent(this,show_list.class));
            return true;
        }
        if (id == R.id.statist) {
            startActivity(new Intent(this,statistic.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}