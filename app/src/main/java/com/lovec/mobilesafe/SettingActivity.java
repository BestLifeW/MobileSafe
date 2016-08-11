package com.lovec.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.lovec.mobilesafe.service.AddressService;
import com.lovec.mobilesafe.ui.SettingView;
import com.lovec.mobilesafe.utils.AddressUtils;

/**
 * Created by lovec on 2016/8/1.
 */
public class SettingActivity extends Activity {

    private SettingView sv_setting_update;
    private SharedPreferences sp;
    private SettingView sv_setting_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        sp = getSharedPreferences("config", MODE_PRIVATE);

        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
        update();
    }

    @Override
    protected void onStart() {
        super.onStart();
        address();
    }

    private void address() {
        //回显操作
        if (AddressUtils.isRunningService("com.lovec.mobilesafe.service.AddressService", getApplicationContext())) {
            //开启服务
            sv_setting_address.setChecked(true);
        } else {
            //关闭服务
            sv_setting_address.setChecked(false);
        }


        sv_setting_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddressService.class);
                if (sv_setting_address.isChecked()) {
                    //之前的状态,关闭更新
                    stopService(intent);
                    sv_setting_address.setChecked(false);
                } else {
                    //打开提示更新
                    startService(intent);
                    sv_setting_address.setChecked(true);
                }
            }
        });
    }

    /*
    * 提示更新的操作
    * */
    private void update() {
        boolean update = sp.getBoolean("update", true);
        if (update) {
            // sv_setting_update.setDes("处于打开更新状态");
            sv_setting_update.setChecked(true);
        } else {
            // sv_setting_update.setDes("处于关闭更新状态");
            sv_setting_update.setChecked(false);
        }

        // sv_setting_update.setTitle("提示更新");

        //点击checkbox发现描述信息没有改变，因为checkbox自带点击事件，，于是在
        sv_setting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editor edit = sp.edit();
                //更改状态
                if (sv_setting_update.isChecked()) {
                    //处于开启中，点击关闭提示更新
                    //sv_setting_update.setDes("处于关闭更新状态");
                    sv_setting_update.setChecked(false);
                    edit.putBoolean("update", false);

                } else {
                    //打开提示更新
                    //sv_setting_update.setDes("处于打开更新状态");
                    sv_setting_update.setChecked(true);
                    edit.putBoolean("update", true);
                }
                edit.apply();
            }
        });
    }
}
