package com.lovec.mobilesafe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
        tv_splash_versionname.setText("版本号:" + getVersionName());
    }


    /*
    * 获取当前应用的版本号
    */
    private String getVersionName() {
        //通过这个包的管理者可以获得包的基础信息
        PackageManager pm = getPackageManager();

        try {
            //flags 0表示获取包里面的基础信息
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //获取到版本消息
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
