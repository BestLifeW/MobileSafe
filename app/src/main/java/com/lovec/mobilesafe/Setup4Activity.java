package com.lovec.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Setup4Activity extends SetUpBaseActivitiy {


    private CheckBox cb_setup4_protected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cb_setup4_protected = (CheckBox) findViewById(R.id.cb_setup4_protected);
        if (sp.getBoolean("protected", false)) {
            cb_setup4_protected.setText("您已经开启了防盗保护");
            cb_setup4_protected.setChecked(true);
        } else {
            cb_setup4_protected.setText("您未开启防盗保护");
            cb_setup4_protected.setChecked(false);
        }

        cb_setup4_protected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            // CompoundButton:checkbox
            // isChecked :改变之后的值
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_setup4_protected.setText("您已经开启了防盗保护");
                    cb_setup4_protected.setChecked(true);
                    sp.edit().putBoolean("protected", true).apply();
                } else {
                    cb_setup4_protected.setText("您未开启防盗保护");
                    cb_setup4_protected.setChecked(false);
                    sp.edit().putBoolean("protected", false).apply();
                }
            }
        });
    }


    @Override
    public void next_activity() {
        //保存 false
        sp.edit().putBoolean("first", false).apply();
        Intent intent = new Intent(getApplicationContext(), LostfindActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
