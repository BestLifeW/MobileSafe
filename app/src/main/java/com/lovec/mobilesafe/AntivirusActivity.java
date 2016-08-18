package com.lovec.mobilesafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lovec.mobilesafe.dao.AntiVirusDao;
import com.lovec.mobilesafe.utils.MD5Utils;

import java.util.ArrayList;
import java.util.List;

public class AntivirusActivity extends AppCompatActivity {

    private ImageView iv_antivirus_scanner;
    private ProgressBar pb_antivirus_progressbar;
    private TextView tv_antivirus_text;
    private LinearLayout ll_antivirus_safeapks;
    private static final String TAG = "AntivirusActivity";
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);

        iv_antivirus_scanner = (ImageView) findViewById(R.id.iv_antivirus_scanner);
        pb_antivirus_progressbar = (ProgressBar) findViewById(R.id.pb_antivirus_progressbar);
        tv_antivirus_text = (TextView) findViewById(R.id.tv_antivirus_text);
        ll_antivirus_safeapks = (LinearLayout) findViewById(R.id.ll_antivirus_safeapks);
        list = new ArrayList<>();
        //设置旋转动画
        //fromDegrees : 旋转开始的角度
        //toDegrees : 结束的角度
        //后面四个:是以自身变化还是以父控件变化
        //Animation.RELATIVE_TO_SELF : 以自身旋转
        //Animation.RELATIVE_TO_PARENT : 以父控件旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);//持续时间
        rotateAnimation.setRepeatCount(Animation.INFINITE);//设置旋转的次数,INFINITE:一直旋转

        //设置动画插补器 解决卡顿
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        rotateAnimation.setInterpolator(linearInterpolator);
        iv_antivirus_scanner.startAnimation(rotateAnimation);


        scanner();
    }

    //扫描程序
    private void scanner() {
        //获取包的管理者
        final PackageManager pm = getPackageManager();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(100);
                //获取所有的安装信息
                List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
                pb_antivirus_progressbar.setMax(installedPackages.size());
                int count = 0;
                for (final PackageInfo packageInfo : installedPackages) {
                    SystemClock.sleep(100);
                    count++;
                    pb_antivirus_progressbar.setProgress(count);
                    //设置扫描显示的名称
                    final String name = packageInfo.applicationInfo.loadLabel(pm).toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_antivirus_text.setText("正在扫描:" + name);

                            //获取应用程序的签名，进行MD5加密
                            Signature[] signatures = packageInfo.signatures;
                            String charString = signatures[0].toCharsString();//签名信息
                            String passwordMD5 = MD5Utils.passwordMD5(charString);
                            boolean b = AntiVirusDao.queryAntiVirus(getApplicationContext(), passwordMD5);
                            TextView textView = new TextView(getApplicationContext());
                            //判断操作
                            if (b) {
                                textView.setTextColor(Color.RED);
                                list.add(packageInfo.packageName);
                            } else {
                                textView.setTextColor(Color.BLACK);
                            }
                            Log.i(TAG, "run: " + passwordMD5);

                            textView.setTextColor(Color.BLACK);
                            textView.setText(name);
                            //ll_antivirus_safeapks.addView(textView);
                            ll_antivirus_safeapks.addView(textView, 0);//添加到线性布局的哪个位置
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (list.size() > 0) {
                            tv_antivirus_text.setText("扫描完成,发现" + list.size() + "个病毒");

                            AlertDialog.Builder builder = new AlertDialog.Builder(AntivirusActivity.this);
                            builder.setTitle("提醒！发现" + list.size() + "个病毒");
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setMessage("是否卸载病毒应用？");
                            builder.setPositiveButton("确认?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (String packagname : list) {
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.DELETE");
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.setData(Uri.parse("package:" + packagname));
                                        startActivity(intent);
                                    }
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("取消", null);
                            builder.show();
                        } else {
                            tv_antivirus_text.setText("扫描完成,未发现病毒");
                        }
                        iv_antivirus_scanner.clearAnimation();
                    }
                });
            }
        }).start();


    }
}
