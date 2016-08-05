package com.lovec.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class Setup4Activity extends SetUpBaseActivitiy {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
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
