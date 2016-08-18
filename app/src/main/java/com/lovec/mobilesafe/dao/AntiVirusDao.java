package com.lovec.mobilesafe.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by lovec on 2016/8/18.
 */
public class AntiVirusDao {

    //查询病毒库 是否含有传过来的md5信息
    public static boolean queryAntiVirus(Context context, String MD5) {
        boolean ishave = false;
        File file = new File(context.getFilesDir(), "antivirus.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("datable", null, "md5=?", new String[]{MD5}, null, null, null);
        if (cursor.moveToNext()) {
            ishave = true;
        }
        cursor.close();
        database.close();
        return ishave;
    }
}
