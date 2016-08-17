package com.lovec.mobilesafe.service;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.lovec.mobilesafe.R;
import com.lovec.mobilesafe.receiver.MyWidget;
import com.lovec.mobilesafe.utils.TaskUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WidgetService extends Service {

    private AppWidgetManager appWidgetManager;
    private WidgetReceiver widgetReceiver;
    private Timer timer;
    private static final String TAG = "WidgetService";
    private ScreenOffReceiver screenOffReceiver;
    private ScreenOnReceiver screenOnReceiver;

    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class WidgetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            killProcess();
            Log.i(TAG, "广播开启啦！ ");
        }


    }

    /*
    * 清理进程
    * */
    private void killProcess() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (!runningAppProcess.processName.equals(getPackageName())) {
                am.killBackgroundProcesses(runningAppProcess.processName);
            }
        }
    }

    private class ScreenOnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: 解锁了");
            updateWidgets();
        }
    }

    /*
    *
    * */
    private class ScreenOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: 锁屏了");
            killProcess();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: 服务开启");


        widgetReceiver = new WidgetReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("aa.bb.cc");
        registerReceiver(widgetReceiver, intentFilter);

        //注册锁 广播接收着
        screenOffReceiver = new ScreenOffReceiver();
        //设置接收世界
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, intentFilter1);


        screenOnReceiver = new ScreenOnReceiver();
        IntentFilter screenOnIntentFilter = new IntentFilter();
        screenOnIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenOnReceiver, screenOnIntentFilter);

        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        updateWidgets();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (widgetReceiver != null) {
            unregisterReceiver(widgetReceiver);
            widgetReceiver = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (screenOffReceiver != null) {
            unregisterReceiver(screenOffReceiver);
            screenOffReceiver = null;
        }
    }

    private void updateWidgets() {
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                }
            }
        }).start();
        */
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //获取组件标识
                System.out.println("widget更新了......");
                ComponentName componentName = new ComponentName(getApplicationContext(), MyWidget.class);
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);

                views.setTextViewText(R.id.process_count, "正在运行的软件:" + TaskUtil.getProcessCount(getApplicationContext()) + "个");
                views.setTextViewText(R.id.process_memory, "可用内存:" + Formatter.formatShortFileSize(getApplicationContext(), TaskUtil.getAvailableRam(getApplicationContext())));

                //更新  远程布局 不能使用findviewById 操作

                Intent intent = new Intent();
                intent.setAction("aa.bb.cc");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
                appWidgetManager.updateAppWidget(componentName, views);
            }
        }, 2000, 2000);
    }


}
