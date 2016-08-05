package com.lovec.mobilesafe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class SetUpBaseActivitiy extends AppCompatActivity {

    private GestureDetector gestureDetector;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.获取手势识别器
        gestureDetector = new GestureDetector(this, new MyOnGestureListener());
        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    //e1:按下的事件，保存按下的坐标  e2:抬起的事件，保存抬起的坐标 velocityX:在X轴上的移动速率
    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startX = e1.getRawX();
            //得到抬起的x坐标
            float endX = e2.getRawX();
            //得到按下的Y坐标
            float startY = e1.getRawY();
            //得到抬起的y坐标
            float endY = e2.getRawY();
            //判断是否是斜滑
            if ((Math.abs(startY - endY)) > 50) {
                Toast.makeText(getApplicationContext(), "你小子又乱滑了,别闹了....", Toast.LENGTH_SHORT).show();
                return false;
            }
            //下一步
            if ((startX - endX) > 100) {
                next_activity();
            }
            //上一步
            if ((endX - startX) > 100) {
                pre_activity();
            }
            //true if the event is consumed, else false
            //true : 事件执行     false:拦截事件,事件不执行
            return true;
        }
    }


    public void pre(View view) {
        pre_activity();
    }


    public void next(View view) {
        next_activity();
    }


    public abstract void next_activity();

    public abstract void pre_activity();

    //监听物理按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            pre_activity();
        return super.onKeyDown(keyCode, event);

    }
}
