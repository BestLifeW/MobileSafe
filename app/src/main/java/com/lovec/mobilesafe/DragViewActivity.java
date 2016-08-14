package com.lovec.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class DragViewActivity extends Activity {

    private LinearLayout ll_dragview_toast;
    private static final String TAG = "DragViewActivity";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        ll_dragview_toast = (LinearLayout) findViewById(R.id.ll_dragview_toast);
        setTouch();
    }

    /*
    * 设置触摸事件
    *
    * */
    private void setTouch() {

        ll_dragview_toast.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下 记住xy坐标
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        Log.i(TAG, "我被按下啦");
                        break;
                    case MotionEvent.ACTION_MOVE://移动 移动到新的位置 获得新的XY坐标
                        int newX = (int) event.getX();
                        int newY = (int) event.getY();
                        //计算偏移量
                        int dx = newX - startX;
                        int dy = newY - startY;
                        int l = ll_dragview_toast.getLeft();
                        int t = ll_dragview_toast.getTop();
                        l += dx;
                        t += dy;
                        Log.i(TAG, "我被移动了");
                        int r = l + ll_dragview_toast.getWidth();
                        int b = t + ll_dragview_toast.getHeight();
                        //更新开始坐标
                        startX = newX;
                        startY = newY;
                        //重新绘制
                        ll_dragview_toast.layout(l, t, r, b);
                        break;
                    case MotionEvent.ACTION_UP://抬起动作 记住控件坐标
                        int x = ll_dragview_toast.getLeft();
                        int y = ll_dragview_toast.getTop();
                        sp.edit().putInt("x", x).putInt("y", y).apply();

                        break;
                }

                return true;
            }
        });
    }
}
