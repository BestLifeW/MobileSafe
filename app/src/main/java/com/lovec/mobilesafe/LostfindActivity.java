package com.lovec.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LostfindActivity extends AppCompatActivity {
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        if (sp.getBoolean("first", true)) {
            //第一次进入，跳转到手机防盗设置模块
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        } else {
            //不是第一次，跳转到显示界面
            setContentView(R.layout.activity_lostfind);
        }


    }


}
