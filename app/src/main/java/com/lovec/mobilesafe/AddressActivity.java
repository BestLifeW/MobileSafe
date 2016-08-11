package com.lovec.mobilesafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lovec.mobilesafe.dao.AddressDao;

public class AddressActivity extends AppCompatActivity {

    private EditText et_address_num;
    private TextView tv_address_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initView();
    }

    /*
    * 初始化控件
    * */
    private void initView() {
        et_address_num = (EditText) findViewById(R.id.et_address_num);
        tv_address_result = (TextView) findViewById(R.id.tv_address_result);


        TimeQuery();
    }

    private void TimeQuery() {
        et_address_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String num = s.toString();
                String location = AddressDao.queryAddress(num, getApplicationContext());
                if (!TextUtils.isEmpty(location)) {
                    tv_address_result.setText(location);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void queryForNum(View view) {
        String number = et_address_num.getText().toString().trim();
        String result = AddressDao.queryAddress(number, getApplicationContext());
        tv_address_result.setText(result);
    }
}
