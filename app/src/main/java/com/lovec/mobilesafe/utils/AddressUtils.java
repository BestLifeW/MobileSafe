package com.lovec.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;


public class AddressUtils {

    /**
     * 判断服务是否运行
     *
     * @param name    接收一个服务的类名
     * @param context 接收一个上下文
     * @return boolean值 是否开启
     */
    public static boolean isRunningService(String name, Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
            //获取控件的标识
            ComponentName service = runningService.service;
            String className = service.getClassName();
            //将获取到的服务类名，跟传递过来的服务类名进行比较

            if (name.equals(className)) {
                return true;
            }
        }
        return false;
    }
}
