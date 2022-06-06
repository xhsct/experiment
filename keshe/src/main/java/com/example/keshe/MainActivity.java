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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Log.d("daa", new Date().getTime()+"");
        // 系统自建
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        // 系统自建
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        first_prepare();
        init_data();
        setTitle("preface");
    }
    @Override
    protected void onResume(){
        super.onResume();
        init_data();
    }

    public void init_data() {
        Map<String, Calendar> map=new HashMap<>();
        Cursor cursor = db.query(MySqliteOpenHelper.SQlite.TABLE_NAME, new String[]{"time"},null,null,"time",null,null);
        while (cursor.moveToNext()){

            @SuppressLint("Range")
            String time = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.time));
            Log.e("TAG12", time+"");
            int year = Integer.parseInt(time.substring(0,4));
            int month = Integer.parseInt(time.substring(4,6));
            int day = Integer.parseInt(time.substring(6,8));
            map.put(getSchemeCalendar(year, month, day, 0xFF40db25, "").toString(),
                    getSchemeCalendar(year, month, day,0xFF40db25, ""));
        }
        calendarView.setSchemeDate(map);

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    public void first_prepare(){
        // 获取一个当前的时间信息
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = dateFormat.format(date);
        // 当前页面用来显示事件的列表
        listView = findViewById(R.id.main_lv);
        // 建立了一个MySqlite0penHelper 用来连接数据库
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        //获得数据库对象helper.get...
        db = helper.getReadableDatabase();
        // 获取日历视图
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnCalendarSelectListener(this);
        // 将默认为当前信息 使得listview中显示当前时间的任务
        StringBuffer select_now = new StringBuffer(format);
        select_now.insert(6,"-");
        select_now.insert(4,"-");
        // 在cur_data textview中显示形式为yyyy-MM-dd的形式
        data = findViewById(R.id.cur_data);
        data.setText(select_now);
        //对listview进行监听 并且将format作为参数传入得到todo事件的列表
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        select(format);
    }

    // 创建了右上角的多选
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 具体规定了右上角的两个选项
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // 当点击add事件出发的操作
    public void add(View view) {
//        calendarView = findViewById(R.id.calendarView);
        String curdata = "";
        String show_data = "";
        curdata += Integer.toString(calendarView.getCurYear());
        show_data += Integer.toString(calendarView.getCurYear())+"年";
        if (calendarView.getCurMonth()/10==0){
            curdata += "0"+calendarView.getCurMonth();
            show_data += "0"+calendarView.getCurMonth()+"月";
        }
        else {
            curdata += Integer.toString(calendarView.getCurMonth());
            show_data += Integer.toString(calendarView.getCurMonth())+"月";
        }
        if (calendarView.getCurDay()/10==0){
            curdata += "0"+calendarView.getCurDay();
            show_data += "0"+calendarView.getCurDay()+"日";
        }
        else {
            curdata += Integer.toString(calendarView.getCurDay());
            show_data += Integer.toString(calendarView.getCurDay())+"日";
        }
        // 上面操作是对curdata show_data 进行编码，下面是通过intent进行传输时间，虽然有些没有必要传过去，但是之前没有想到直接获取当前时间
        Intent intent = new Intent(this,add_list.class);
        intent.putExtra("type","0");
        intent.putExtra("return","0");
        intent.putExtra("curdata",curdata);
        intent.putExtra("show_data",show_data);
        startActivity(intent);
        finish();
        select(curdata);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    // 根据time来查询当前任务序列
    @SuppressLint("Range")
    private void select(String time) {
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

    // 通过选择点击的日期来得到任务序列
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        String select_data=calendar.getYear()+"-";
        String select = Integer.toString(calendar.getYear());
        if (calendar.getMonth()/10==0){
            select_data += "0"+calendar.getMonth()+"-";
            select += "0"+calendar.getMonth();
        }
        else {
            select_data += Integer.toString(calendar.getMonth())+"-";
            select += Integer.toString(calendar.getMonth());
        }
        if (calendar.getDay()/10==0){
            select_data += "0"+calendar.getDay();
            select += "0"+calendar.getDay();
        }
        else {
            select_data += Integer.toString(calendar.getDay());
            select += Integer.toString(calendar.getDay());
        }
        data.setText(select_data);
        select(select);
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