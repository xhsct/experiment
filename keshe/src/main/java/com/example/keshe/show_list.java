package com.example.keshe;
import com.example.keshe.MainActivity.* ;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keshe.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            startActivity(new Intent(this,statistic.class));
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
            Date date = new Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String format = dateFormat.format(date);
            if (Integer.parseInt(format)-Integer.parseInt(work.time)>2 && !holder.ifdone.isChecked()){
                db.execSQL("update todolist set state = ? where id = ?"
                        , new String[]{"postpone",work.id+""});
                holder.set_background.setBackgroundColor(Color.rgb(190,0,80));
            }else {
                holder.set_background.setBackgroundColor(Color.rgb(1,87,190));
            }
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
                        ColorDrawable colorDrawable = (ColorDrawable)holder.set_background.getBackground();
                        if (colorDrawable.getColor() == Color.rgb(190,0,80)){
                            holder.set_background.setBackgroundColor(Color.rgb(1,87,190));
                        }
                        db.execSQL("update todolist set state = ? where id = ?"
                                , new String[]{"done",work.id+""});
                    }
                    else {
                        Log.d("statea", work.state+"");
                        if(work.state != "postpone"){
                        db.execSQL("update todolist set state = ? where id = ?"
                                , new String[]{"start",work.id+""});  }
                        Date date = new Date(System.currentTimeMillis());
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        String format = dateFormat.format(date);
                        if (Integer.parseInt(format)-Integer.parseInt(work.time)>2){
                            db.execSQL("update todolist set state = ? where id = ?"
                                    , new String[]{"postpone",work.id+""});
                            holder.set_background.setBackgroundColor(Color.rgb(190,0,80));
                        }
                    }
                }
            });
            holder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.linear_text.getVisibility() == View.VISIBLE){
                        holder.linear_text.setVisibility(View.GONE);
                    }
                    else if(holder.linear_text.getVisibility() == View.GONE){
                        holder.linear_text.setVisibility(View.VISIBLE);
                        holder.text_pull.setText(work.content);
                    }
                    if (holder.linear_image.getVisibility() == View.VISIBLE){
                        holder.linear_image.setVisibility(View.GONE);
                    }
                    else if(holder.linear_image.getVisibility() == View.GONE){
                        if (work.image != null){
                            holder.linear_image.setVisibility(View.VISIBLE);
                            holder.image_pull.setImageBitmap(ImageUtil.base64ToImage(work.image));
                        }
                    }
                }
            });
            //返回视图
            return convertView;
        }


        //创建一个ViewHolder
        class ViewHolder {
            //定义控件对象
            TextView title,time,text_pull;
            ImageButton deleteBtn;
            CheckBox ifdone;
            ImageView image_pull;
            LinearLayout linear_text,linear_image,set_background;
            //通过构造方法传入控件存在的View的对象
            ViewHolder(View convertView) {
                //实例化控件对象
                time = (TextView) convertView.findViewById(R.id.time_show);
                title = (TextView) convertView.findViewById(R.id.title_show);
                deleteBtn = (ImageButton) convertView.findViewById(R.id.delete_show);
                ifdone = (CheckBox) convertView.findViewById(R.id.select_show);
                linear_text = convertView.findViewById(R.id.linear_text);
                linear_image =convertView.findViewById(R.id.linear_image);
                text_pull = convertView.findViewById(R.id.text_pull);
                image_pull = convertView.findViewById(R.id.image_pull);
                set_background = convertView.findViewById(R.id.set_background);
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

    public void add(View view) {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String curdata = dateFormat.format(date);
        StringBuffer show_data = new StringBuffer(curdata);
        show_data.insert(8,"日");
        show_data.insert(6,"月");
        show_data.insert(4,"年");
        Intent intent = new Intent(this,add_list.class);
        intent.putExtra("type","0");
        intent.putExtra("return","1");
        intent.putExtra("curdata",curdata);
        intent.putExtra("show_data", (Serializable) show_data);
        startActivity(intent);
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