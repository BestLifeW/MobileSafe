package com.lovec.mobilesafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void queryaddress(View view) {
        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
        startActivity(intent);
    }
}
