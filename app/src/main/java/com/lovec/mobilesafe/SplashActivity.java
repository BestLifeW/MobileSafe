package com.lovec.mobilesafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.IOUtils;
import com.lovec.mobilesafe.utils.StreamUtils;
import com.lovec.mobilesafe.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private String path = Environment.getExternalStorageDirectory().getPath();
    private static final int MS_UPDATE_DIALOG = 1;
    private static final int MS_NOTHING = 2;
    private static final int MS_SERVER_ERROR = 3;
    private static final int MS_URL_ERROR = 4;
    private static final int MS_IO_ERROR = 5;
    private static final int MS_JSON_ERROR = 6;
    private SharedPreferences sp;
    private String TAG = "这是输出日志";
    private String code;
    private String apkurl;
    private String desc;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MS_UPDATE_DIALOG:
                    //弹出对话框
                    showDialog();
                    break;
                case MS_NOTHING:
                    //进入主页
                    enterHome();
                    break;
                case MS_SERVER_ERROR:
                    //进入主页
                    ToastUtils.showToast(getApplicationContext(), "网络连接失败啦~检查一下把");
                    enterHome();
                    break;
                case MS_URL_ERROR:
                    ToastUtils.showToast(getApplicationContext(), "错误码是" + MS_URL_ERROR);
                    enterHome();
                case MS_IO_ERROR:
                    ToastUtils.showToast(getApplicationContext(), "亲，网络未连接哦~" + "错误代码" + MS_IO_ERROR);
                    enterHome();
                case MS_JSON_ERROR:
                    ToastUtils.showToast(getApplicationContext(), "错误码是" + MS_JSON_ERROR);
                    enterHome();

            }
        }
    };
    private String NETWORKURL;


    /*
    * 赶紧给onCreate 方法加上注释，要不等等找不到了
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkUpdate();
        initView();
        copyDb();

        //开启电话服务
        //放在设置里面开启了
        //startAddressService();

    }

    /*
    * 开启电话的操作
    * */
//    private void startAddressService() {
//        Intent intent = new Intent(getApplicationContext(), AddressService.class);
//        startService(intent);
//    }

    /*
    * 拷贝数据库的操作
    *
    * */
    private void copyDb() {
        File file = new File(getFilesDir(), "address.db");
        //判断文件是否存在
        if (!file.exists()) {
            //从assets目录中将数据库读取出来
            //1.获取assets的管理者
            AssetManager am = getAssets();
            InputStream in = null;
            FileOutputStream out = null;
            try {
                //2.读取数据库
                in = am.open("address.db");
                //写入流
                //getCacheDir : 获取缓存的路径
                //getFilesDir : 获取文件的路径
                out = new FileOutputStream(file);
                //3.读写操作
                //设置缓冲区
                byte[] b = new byte[1024];
                int len = -1;
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//				in.close();
//				out.close();
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }


        }


    }

    /*判断是否更新*/
    private void checkUpdate() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("update", true)) {
            update();
        } else {
            //不能让主线程睡两秒钟
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    enterHome();
                }
            }).start();
        }
    }

    //弹出一个对话框
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(code + "版本发布啦!");
        builder.setCancelable(false);
        builder.setMessage(desc);
        builder.setPositiveButton("马上下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
                Toast.makeText(SplashActivity.this, "下载中....", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("下次提醒", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //跳转到主页面
                enterHome();
            }
        });
        builder.show();
    }

    /*
    * 下载操作
    * */
    private void download() {
        HttpUtils httpUtils = new HttpUtils();
        //判断sd卡的状态

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            httpUtils.download(apkurl, path + "/mobliesafe75.apk", new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {

                    Log.i(TAG, "下载成功");
                    installAPK();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.i(TAG, "下载失败" + e);
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    /*执行安装逻辑*/
    private void installAPK() {
        Intent intent = new Intent();
        /**
         *  <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="content" /> //content : 从内容提供者中获取数据  content://
         <data android:scheme="file" /> // file : 从文件中获取数据
         <data android:mimeType="application/vnd.android.package-archive" />
         </intent-filter>
         */
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File(path + "/mobliesafe75.apk")), "application/vnd.android.package-archive");
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();

    }

    /*
        * 跳转到主页面
        * */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    //初始化布局
    private void initView() {
        TextView tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
        if (tv_splash_versionname != null) {
            tv_splash_versionname.setText("版本号:" + getVersionName());
        }
    }

    /*
    * 查看是否有最新版本 并且提醒用户是否更新
    * */
    private void update() {
        //联网操作,所以要新开进程
        new Thread() {

            private int endTime;
            private int startTime;

            public void run() {
                Message message = Message.obtain();
                startTime = (int) SystemClock.currentThreadTimeMillis();
                try {
                    //连接服务器
                    NETWORKURL = "http://wtc.xiapu.co/vesionInfo.html";
                    URL url = new URL(NETWORKURL);
                    //获取url的连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置连接超时时间
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");//POST
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        Log.i("update", "连接成功");
                        //获取返回的流信息
                        InputStream in = conn.getInputStream();
                        //将返回的流转换成字符串输出
                        String json = StreamUtils.parserStreamUtils(in);
                        //解析json操作
                        JSONObject jsonObject = new JSONObject(json);
                        code = jsonObject.getString("code");
                        apkurl = jsonObject.getString("apkurl");
                        desc = jsonObject.getString("desc");
                        Log.i(TAG, "版本号是" + code + "apk地址是" + apkurl + "描述信息是" + desc);

                        if (code.equals(getVersionName())) {
                            //没有最新版本

                            message.what = MS_NOTHING;
                        } else {
                            //有最新版本，弹出对话框 handler
                            message.what = MS_UPDATE_DIALOG;
                        }


                    } else {
                        message.what = MS_SERVER_ERROR;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = MS_URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = MS_IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = MS_JSON_ERROR;
                } finally {
                    //不管有没有异常，都睡两秒
                    endTime = (int) SystemClock.currentThreadTimeMillis();
                    int dTime = startTime - endTime;
                    if (dTime < 2000) {
                        SystemClock.sleep(2000 - dTime);
                    }
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    /*
    * 获取当前应用的版本号
    */
    private String getVersionName() {
        //通过这个包的管理者可以获得包的基础信息
        PackageManager pm = getPackageManager();
        try {
            //flags 0表示获取包里面的基础信息
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //获取到版本消息
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
