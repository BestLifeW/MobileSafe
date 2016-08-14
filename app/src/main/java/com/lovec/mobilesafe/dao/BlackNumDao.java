package com.lovec.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lovec.mobilesafe.been.BlacknumInfo;
import com.lovec.mobilesafe.db.BlackNumOpenHlper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovec on 2016/8/12.
 */
public class BlackNumDao {
    public static final int CALL = 0;
    public static final int SMS = 1;
    public static final int ALL = 2;
    private final BlackNumOpenHlper blackNumOpenHlper;
    //增删改查
    private static final String TAG = "BlackNumDao";

    public BlackNumDao(Context context) {
        blackNumOpenHlper = new BlackNumOpenHlper(context);
    }

    //增
    public void addBlackNum(String blacknum, int mode) {
        //1，获取数据库
        //2，contentValues 代表的是 要放入的值
        Log.i(TAG, "addBlackNum: 准备添加数据");
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("blacknum", blacknum);
        values.put("mode", mode);
        database.insert(blackNumOpenHlper.TABLE_NAME, null, values);
        database.close();
        Log.i(TAG, "addBlackNum: 添加数据数据成功，号码是" + blacknum + "模式是" + mode);
    }

    /*
    * 更新操作
    * */
    public void updateBlackNum(String blacknum, int mode) {
        // 获取数据库
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);

        database.update(blackNumOpenHlper.TABLE_NAME, values, "blacknum=?", new String[]{blacknum});

        database.close();

    }

    //查
    public int queryBlackNumMode(String blackNum) {
        int mode = -1;
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        Cursor cursor = database.query(blackNumOpenHlper.TABLE_NAME, new String[]{"mode"}, "blackNum=?", new String[]{blackNum}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return mode;
    }

    //删除
    public void deleteBlackNum(String blacknum) {
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        database.delete(blackNumOpenHlper.TABLE_NAME, "blacknum=?", new String[]{blacknum});
        database.close();
    }

    //查询所有的内容
    public List<BlacknumInfo> queryAllBlackNum() {
        List<BlacknumInfo> list = new ArrayList<BlacknumInfo>();
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        Cursor cursor = database.query(blackNumOpenHlper.TABLE_NAME, new String[]{"blacknum", "mode"}, null, null, null, null, "_id desc");//desc 倒叙 asc 正续
        while (cursor.moveToNext()) {
            //获取数据
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlacknumInfo blacknumInfo = new BlacknumInfo(blacknum, mode);
            list.add(blacknumInfo);
        }
        cursor.close();
        database.close();
        return list;
    }

    public List<BlacknumInfo> getPartBlackNum(int maxNum, int startIndex) {
        List<BlacknumInfo> list = new ArrayList<BlacknumInfo>();
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        //Cursor cursor = database.query(blackNumOpenHlper.TABLE_NAME, new String[]{"blacknum", "mode"}, null, null, null, null, "_id desc");//desc 倒叙 asc 正续
        Cursor cursor = database.rawQuery("select blacknum,mode from info order by _id desc limit ? offset ?", new String[]{maxNum + "", startIndex + ""});
        while (cursor.moveToNext()) {
            //获取数据
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlacknumInfo blacknumInfo = new BlacknumInfo(blacknum, mode);
            list.add(blacknumInfo);
        }
        cursor.close();
        database.close();
        return list;
    }
}
