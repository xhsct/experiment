package com.example.keshe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.keshe.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class show_list extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView listView;
    //存放数据的集合
    List<list> list = new ArrayList<>();

    //定义一个数据库的对象
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        listView = (ListView) findViewById(R.id.show);
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        //获得数据库对象helper.get...
        db = helper.getReadableDatabase();
        //给ListView设置适配器
        listView.setAdapter(adapter);
        //给ListView设置监听事件
        listView.setOnItemClickListener(this);
        select();
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
        public list getItem(int position) {
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
                convertView = View.inflate(show_list.this, R.layout.show_list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //往控件上放置数据
            list work = list.get(position);//如果不是String类型的数据会报错ResourceNotFound
            holder.title.setText(work.title + "");
            holder.time.setText(work.time + "");
            //返回视图
            return convertView;
        }

        //创建一个ViewHolder
        class ViewHolder {
            //定义控件对象
            TextView title,time;
            //通过构造方法传入控件存在的View的对象
            ViewHolder(View convertView) {
                //实例化控件对象
                time = (TextView) convertView.findViewById(R.id.time_show);
                title = (TextView) convertView.findViewById(R.id.title_show);
            }
        }
    };
    @SuppressLint("Range")
    private void select() {
        //先清除页面上的数据
        list.clear();
        //再加载数据库的数据
        //这里查询所有的数据，只需要一个表名就可以了,后面的条件可以不写，获得的是一个结果集
        Cursor cursor = db.query(MySqliteOpenHelper.SQlite.TABLE_NAME, null, null, null, null, null, null);
        //结果集指向的表头前面.moveToNext()来指向下一个结果
        while (cursor.moveToNext()) {
            //获取结果集里面的数据
            //cursor.getColumnIndex()获取列名所在的列号
            list work = new list();
            //这里通过列号来获取数据
            work.title = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.title));
            work.time = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.time));
            //把找到的数据添加到List集合中
            list.add(work);
        }
        //刷新适配器
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    class list {
        String title;
        String time;
    }

}