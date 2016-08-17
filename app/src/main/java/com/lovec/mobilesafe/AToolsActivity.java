package com.lovec.mobilesafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.lovec.mobilesafe.engine.SMSEngine;

public class AToolsActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ProgressBar pb_atools_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void queryaddress(View view) {
        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
        startActivity(intent);
    }

    public void backupsms(View v) {
        //progressdialog是可以在子线程中更新
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setCancelable(false);//不能取消
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		/*progressDialog.setMax(max);//设置最大进度
//		progressDialog.setProgress(value);//设置当前进度*/
//		progressDialog.show();
        //读取短信
        //备份短信
        new Thread() {
            public void run() {
                //3.我们接受刷子,进行刷牙,刷鞋,刷马桶.....
                SMSEngine.getAllSMS(getApplicationContext(), new SMSEngine.ShowProgress() {

                    @Override
                    public void setProgress(int progerss) {
                        //progressDialog.setProgress(progerss);
                        pb_atools_sms.setProgress(progerss);
                    }

                    @Override
                    public void setMax(int max) {
                        //progressDialog.setMax(max);
                        pb_atools_sms.setMax(max);
                    }
                });
                //progressDialog.dismiss();
            }
        }.start();


    }
}
