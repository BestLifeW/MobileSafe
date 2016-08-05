package com.lovec.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lovec.mobilesafe.utils.ToastUtils;

public class Setup3Activity extends SetUpBaseActivitiy {

    private EditText et_setup3_safenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_setup3_safenum = (EditText) findViewById(R.id.et_setup3_safenum);
        et_setup3_safenum.setText(sp.getString("safenum", ""));

    }

    @Override
    public void next_activity() {
        String safenum = et_setup3_safenum.getText().toString().trim();
        if (TextUtils.isEmpty(safenum)) {
            ToastUtils.showToast(this, "请输入安全号码");
            return;
        }
        sp.edit().putString("safenum", safenum).apply();

        Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }


    /*选择联系人的按钮操作*/
    public void selectContacts(View view) {
        Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收联系人界面传递过来的数据
        if (data != null) {
            String num = data.getStringExtra("num");
            et_setup3_safenum.setText(num);
        }

    }
}
