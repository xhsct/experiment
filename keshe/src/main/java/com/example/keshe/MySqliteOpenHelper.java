package com.example.keshe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    public MySqliteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建一个表
        sqLiteDatabase.execSQL(SQlite.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //销毁一个表
        sqLiteDatabase.execSQL(SQlite.DELETE_TABLE);
        onCreate(sqLiteDatabase);

    }
    //定义一个数据库的基本信息，包括一些基本的使用语句
    static class SQlite {
        public static final String DB_NAME = "todo.db";//数据库名称

        public static final String TABLE_NAME = "todolist";//要操作的表的名称

        //表格里面的数据
        public static final String ID = "id";//任务的ID

        public static final String title = "title";//任务的姓名

        public static final String content = "content";//详情内容

        public static final String time = "time"; //时间

        public static final String now_time = "now_time";

        public static final String image = "image"; //图片

        public static final String state = "state";//状态：创建 完成 延期

        //创建表格使用的SQL语句
        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " + ID + " integer primary key autoincrement,"  + time + " text ,"  + title + " text ," + content + " text ,"+ image + " text ,"+ state +" text ,"+ now_time +" text "+")";

        //删除表格使用的语句
        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME;

    }
}
