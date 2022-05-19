package com.example.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.view.ViewGroup;
import android.content.DialogInterface;
import java.util.ArrayList;

import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener{
    //定义ListView布局
    ListView listView;
    //存放数据的集合
    List<User> list = new ArrayList<>();

    //定义一个数据库的对象
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.main_lv);
        //实例化数据库对象
        //要通过SQLiteHelper对象,这里使用的是自己定义的类
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        //获得数据库对象helper.get...
        db = helper.getReadableDatabase();
        //给ListView设置适配器
        listView.setAdapter(adapter);
        //给ListView设置监听事件
        listView.setOnItemClickListener(this);
        //刷新数据页面
        select();
    }


    public void insert(View v) {

        final EditText et_name = (EditText) this.findViewById(R.id.dialog_username);
        final EditText et_info = (EditText) this.findViewById(R.id.dialog_info);

        String name = et_name.getText().toString().trim();
        String info = et_info.getText().toString().trim();
        insertData(name, info);
        adapter.notifyDataSetChanged();


    }

    private void insertData(String name,  String info)  //
    {
        // 执行插入语句
        db.execSQL("insert into userInfo values(null , ? , ?)"
                , new String[]{name, info});
    }

    public void select(View v) {
        select();
    }

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
            User user = new User();
            //这里通过列号来获取数据
            user.id = cursor.getInt(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.ID));
            user.username = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.USER_USERNAME));
            user.info = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.USER_INFO));
            //把找到的数据添加到List集合中
            list.add(user);
        }
        //刷新适配器
        adapter.notifyDataSetChanged();
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
        public User getItem(int position) {
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
            User user = list.get(position);//如果不是String类型的数据会报错ResourceNotFound
            holder.info.setText(user.info + "");
            holder.username.setText(user.username + "");
            holder.id.setText(user.id + "");
            //返回视图
            return convertView;
        }

        //创建一个ViewHolder
        class ViewHolder {
            //定义控件对象
            TextView  id,info,  username;
            //通过构造方法传入控件存在的View的对象
            ViewHolder(View convertView) {
                //实例化控件对象
                id = (TextView) convertView.findViewById(R.id.id);
                info = (TextView) convertView.findViewById(R.id.info);
                username = (TextView) convertView.findViewById(R.id.username);
            }
        }
    };


    //长按ListView里面的条目时触发的方法,这里删除数据
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //弹出一个对话框，询问是否删除数据
        new AlertDialog.Builder(this).
                setTitle("警告").
                setMessage("是否确定删除信息！").
                setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定后，删除信息
                        //获取点中的条目的用户的ID值
                        int id = list.get(position).id;
                        int rows = db.delete(MySqliteOpenHelper.SQlite.TABLE_NAME, MySqliteOpenHelper.SQlite.ID + " = ?", new String[]{id + ""});
                        //在集合中也删除这条数据
                        if (rows > 0) {
                            list.remove(position);
                        }
                        //刷新适配器
                        adapter.notifyDataSetChanged();
                    }
                }).show();
//        return true;
    }

    //定义一个内部类方便数据的集体存放
    class User {
        int id;
        String username;
        String info;
    }


    //页面关闭时，关闭数据库的连接
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
