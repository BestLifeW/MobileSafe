package com.lovec.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends SetUpBaseActivitiy {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }



    @Override
    public void next_activity() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {

    }
}
