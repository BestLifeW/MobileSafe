package com.lovec.mobilesafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Setup2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }
    public void pre(View view){
        Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
        startActivity(intent);
    }
}
