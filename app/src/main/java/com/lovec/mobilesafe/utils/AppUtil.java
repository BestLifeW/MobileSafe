package com.lovec.mobilesafe.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by lovec on 2016/8/15.
 */
public class AppUtil {
    public static long getAvailablesSD(){
        File path = Environment.getExternalStorageDirectory();

        StatFs statFs = new StatFs(path.getPath());
        long blockSize = statFs.getBlockSize();
        long blockCount = statFs.getBlockCount();
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        return availableBlocksLong*blockSize;
    }
    public static long getAvailablesROM(){
        File path = Environment.getDataDirectory();

        StatFs statFs = new StatFs(path.getPath());
        long blockSize = statFs.getBlockSize();
        long blockCount = statFs.getBlockCount();
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        return availableBlocksLong*blockSize;
    }
}
