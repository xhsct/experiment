package com.example.keshe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.keshe.databinding.ActivityMainBinding;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        CalendarView.OnCalendarSelectListener, AdapterView.OnItemClickListener{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private CalendarView calendarView;
    private TextView data;
    //定义ListView布局
    ListView listView;
    //存放数据的集合
    List<work> list = new ArrayList<>();
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        listView = (ListView) findViewById(R.id.main_lv);
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        //获得数据库对象helper.get...
        db = helper.getReadableDatabase();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnCalendarSelectListener(this);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void add(View view) {
        calendarView = findViewById(R.id.calendarView);
        String curdata = "";
        curdata += Integer.toString(calendarView.getCurYear());
        if (calendarView.getCurMonth()/10==0){
            curdata += "0"+calendarView.getCurMonth();
        }
        else {
            curdata += Integer.toString(calendarView.getCurMonth());
        }
        if (calendarView.getCurDay()/10==0){
            curdata += "0"+calendarView.getCurDay();
        }
        else {
            curdata += Integer.toString(calendarView.getCurDay());
        }
        Intent intent = new Intent(this,add_list.class);
        intent.putExtra("type","0");
        intent.putExtra("curdata",curdata);
        startActivity(intent);
        select(curdata);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }
    @SuppressLint("Range")
    private void select(String time) {
        //先清除页面上的数据
        list.clear();
        String querySql="select * from todolist where time=?";
        String []args=new String[]{time};
        //再加载数据库的数据
        //这里查询所有的数据，只需要一个表名就可以了,后面的条件可以不写，获得的是一个结果集
        Cursor cursor = db.rawQuery(querySql,args);
        //结果集指向的表头前面.moveToNext()来指向下一个结果
        while (cursor.moveToNext()) {
            //获取结果集里面的数据
            //cursor.getColumnIndex()获取列名所在的列号
            work work = new work();
            //这里通过列号来获取数据
            work.time = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.now_time));
            work.title = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.title));
            //把找到的数据添加到List集合中
            list.add(work);
        }
        //刷新适配器
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
//        calendarView =findViewById(R.id.calendarView);
        String select_data=Integer.toString(calendar.getYear());
        if (calendar.getMonth()/10==0){
            select_data += "0"+calendar.getMonth();
        }
        else {
            select_data += Integer.toString(calendar.getMonth());
        }
        if (calendar.getDay()/10==0){
            select_data += "0"+calendar.getDay();
        }
        else {
            select_data += Integer.toString(calendar.getDay());
        }
        data = findViewById(R.id.cur_data);
        data.setText(select_data);
        //给ListView设置适配器
        listView.setAdapter(adapter);
        //给ListView设置监听事件
        listView.setOnItemClickListener(this);
        select(select_data);
        //刷新数据页面


//        Log.e("day",Integer.toString(calendar.getDay()));
//        Toast.makeText(this, Integer.toString(calendar.getDay()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    //定义一个内部类方便数据的集体存放
    class work {
        int id;
        String title;
        String time;
    }
    /**
     * 适配器的创建，为了显示ListView
     */
    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public work getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //定义ViewHolder对象
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //往控件上放置数据
            work work = list.get(position);//如果不是String类型的数据会报错ResourceNotFound
            holder.time.setText(work.time + "");
            holder.title.setText(work.title + "");
            //返回视图
            return convertView;
        }

        //创建一个ViewHolder
        class ViewHolder {
            //定义控件对象
            TextView  time,title;
            //通过构造方法传入控件存在的View的对象
            ViewHolder(View convertView) {
                //实例化控件对象
                time = (TextView) convertView.findViewById(R.id.time_select);
                title = (TextView) convertView.findViewById(R.id.title_select);
            }
        }
    };


}