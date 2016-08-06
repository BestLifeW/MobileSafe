package com.lovec.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostfindActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private TextView tv_lostfind_safenum;
    private ImageView iv_lostfind_protected;


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
            initView();
        }

    }

    private void initView() {
        tv_lostfind_safenum = (TextView) findViewById(R.id.tv_lostfind_safenum);
        tv_lostfind_safenum.setText(sp.getString("safenum", ""));
        iv_lostfind_protected = (ImageView) findViewById(R.id.iv_lostfind_protected);
        boolean b = sp.getBoolean("protected", false);
        if (b) {
            iv_lostfind_protected.setImageResource(R.drawable.lock);
        } else {

            iv_lostfind_protected.setImageResource(R.drawable.unlock);
        }

    }

    public void resetup(View view) {
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
    }
}
