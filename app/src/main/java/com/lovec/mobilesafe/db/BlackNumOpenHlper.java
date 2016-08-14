package com.lovec.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lovec on 2016/8/12.
 */
public class BlackNumOpenHlper extends SQLiteOpenHelper {

    public static final String DB_NAME = "blacknum.db";
    public static final String TABLE_NAME = "info";

    public BlackNumOpenHlper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    //创建表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建字段 num
        db.execSQL("create table " + TABLE_NAME + "(_id integer primary key autoincrement,blacknum varchar(20),mode varchar(2))");
    }

    //数据库版本变化的调用  改变版本号
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
