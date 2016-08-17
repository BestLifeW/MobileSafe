package com.lovec.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.lovec.mobilesafe.ui.SettingVIew;
import com.lovec.mobilesafe.utils.ToastUtils;

public class Setup2Activity extends SetUpBaseActivitiy {

    private SettingVIew sv_setup2_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        sv_setup2_sim = (SettingVIew) findViewById(R.id.sv_setup2_sim);


        if (TextUtils.isEmpty(sp.getString("sim", ""))) {
            sv_setup2_sim.setChecked(false);
        } else {
            sv_setup2_sim.setChecked(true);
        }


        sv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //绑定SIM卡
                //根据checkbox的状态
                if (sv_setup2_sim.isChecked()) {
                    //解绑
                    sv_setup2_sim.setChecked(false);
                    sp.edit().putString("sim", "").apply();

                } else {
                    //绑定
                    TelephonyManager tel = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    //tel.getLine1Number();//获取SIM卡绑定的电话号码,中国不好用
                    String sim = tel.getSimSerialNumber();// 获取sim卡序列号
                    sp.edit().putString("sim", sim).apply();
                    sv_setup2_sim.setChecked(true);
                }
            }
        });
    }


    @Override
    public void next_activity() {
        //判断用户是否绑定Sim卡 如果没有的话，提醒
        if (sv_setup2_sim.isChecked()) {
            Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
        } else {
            ToastUtils.showToast(getApplicationContext(), "请绑定Sim卡");
        }

    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
