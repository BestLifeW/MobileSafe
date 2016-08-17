package com.lovec.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by lovec on 2016/8/16.
 */
public class TaskUtil {
    /*
    * 获取正在运行的进程个数
    * */

    public static int getProcessCount(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        return runningAppProcesses.size();

    }

    /*
    * 获取剩余内存
    * */
    public static long getAvailableRam(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        long availMem = outInfo.availMem;
        return availMem;
    }

    public static long getToatalRam(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        long totalMem = outInfo.totalMem;
        return totalMem;
    }
}
