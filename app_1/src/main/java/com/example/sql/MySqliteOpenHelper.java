package com.example.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    public MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库的一个使用的表
        db.execSQL(SQlite.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //删除数据库的表，再创建数据库的表
        db.execSQL(SQlite.DELETE_TABLE);
        onCreate(db);
    }

    //定义一个数据库的基本信息，包括一些基本的使用语句
    static class SQlite {
        public static final String DB_NAME = "Test.db";//数据库名称

        public static final String TABLE_NAME = "userInfo";//要操作的表的名称

        //表格里面的数据
        public static final String ID = "id";//用户的ID

        public static final String USER_USERNAME = "username";//用户的姓名

        public static final String USER_INFO = "info";//用户的其他信息

        //创建表格使用的SQL语句
        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " + ID + " integer primary key autoincrement,"  + USER_USERNAME + " text ,"  + USER_INFO + " text " + ")";

        //删除表格使用的语句
        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME;

    }

}

