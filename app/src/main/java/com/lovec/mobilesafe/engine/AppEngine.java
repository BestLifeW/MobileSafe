package com.lovec.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.lovec.mobilesafe.been.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovec on 2016/8/13.
 */
public class AppEngine {

    public static List<AppInfo> getAppInfos(Context context) {
        List<AppInfo> list = new ArrayList<AppInfo>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        for (PackageInfo aPackage : packages) {
            String packageName = aPackage.packageName;
            String versionName = aPackage.versionName;

            ApplicationInfo applicationInfo = aPackage.applicationInfo;
            Drawable icon = applicationInfo.loadIcon(packageManager);
            String name = applicationInfo.loadLabel(packageManager).toString();
            //是否是用户程序
            //获取应用程序相关的信息
            boolean isUser;
            boolean isSD;
            int flags = applicationInfo.flags;
            if ((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM) {
                //系统程序
                isUser = false;
            } else {
                //用户程序
                isUser = true;
            }
            if ((applicationInfo.FLAG_EXTERNAL_STORAGE & flags) == applicationInfo.FLAG_EXTERNAL_STORAGE) {
                //安装到SD
                isSD = true;
            } else {
                //没安装SD
                isSD = false;
            }
            AppInfo appInfo = new AppInfo(name, icon, packageName, versionName, isSD, isUser);
            list.add(appInfo);
        }
        return list;
    }
}
