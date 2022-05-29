package com.example.keshe;
import com.example.keshe.MainActivity.* ;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keshe.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class show_list extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView listView;
    //存放数据的集合
    List<list> list = new ArrayList<>();
    //定义一个数据库的对象
    SQLiteDatabase db;
    ImageButton delete;

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
//        delete = findViewById(R.id.delete_show);
//        delete.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Log.e("day", "onClick: ");
//            }
//        });
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
            boolean a = false;
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
            if (work.state.equals("done")){
                a = true;
            }
            holder.ifdone.setChecked(a);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = list.get(position).id;
                    int rows = db.delete(MySqliteOpenHelper.SQlite.TABLE_NAME, MySqliteOpenHelper.SQlite.ID + " = ?", new String[]{id + ""});
                    //在集合中也删除这条数据
                    if (rows > 0) {
                        list.remove(position);
                    }
                    //刷新适配器
                    adapter.notifyDataSetChanged();
                }
            });
            holder.title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(show_list.this,add_list.class);
                    intent.putExtra("type","1");
                    intent.putExtra("content",work.content);
                    intent.putExtra("id",work.id+"");
                    Log.d("Main", "onLongClick: "+work.id);
                    intent.putExtra("title",work.title);
                    intent.putExtra("time",work.time);
//                    Toast.makeText(show_list.this, "chuanshu", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    return false;
                }
            });
            holder.ifdone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (holder.ifdone.isChecked()){
                        db.execSQL("update todolist set state = ? where id = ?"
                                , new String[]{"done",list.get(position).id+""});
                    }
                }
            });
            //返回视图
            return convertView;
        }


        //创建一个ViewHolder
        class ViewHolder {
            //定义控件对象
            TextView title,time;
            ImageButton deleteBtn;
            CheckBox ifdone;
            //通过构造方法传入控件存在的View的对象
            ViewHolder(View convertView) {
                //实例化控件对象
                time = (TextView) convertView.findViewById(R.id.time_show);
                title = (TextView) convertView.findViewById(R.id.title_show);
                deleteBtn = (ImageButton) convertView.findViewById(R.id.delete_show);
                ifdone = (CheckBox) convertView.findViewById(R.id.select_show);
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
            list task = new list();
            //这里通过列号来获取数据
            task.id = cursor.getInt(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.ID));
            task.title = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.title));
            task.time = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.time));
            task.state = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.state));
            task.content = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.content));
            task.image = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.image));
            //把找到的数据添加到List集合中
            list.add(task);
        }
        //刷新适配器
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    class list {
        int id;
        String title;
        String time;
        String state;
        String content;
        String image;
    }

}