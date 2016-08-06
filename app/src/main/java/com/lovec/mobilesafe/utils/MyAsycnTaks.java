package com.lovec.mobilesafe.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by lovec on 2016/8/5.
 * 异步加载框架
 */
public abstract class MyAsycnTaks {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            postTask();
        }
    };

    /*
    * 在子线程执行前的方法
    * @return
    * */
    public abstract void preTask();

    /*
    * 在子线程中执行的方法
    * */
    public abstract void doinBack();

    /*
    * 在子线程执行之后的方法
    * */
    public abstract void postTask();

    /*
    * 执行
    * */

    public void excute() {

        preTask();

        new Thread(new Runnable() {
            @Override
            public void run() {
                doinBack();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
}
