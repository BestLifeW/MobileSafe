package com.lovec.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lovec.mobilesafe.R;

/**
 * Created by lovec on 2016/7/28.
 */
public class ToastUtils {

    private static WindowManager windowManager;
    private static View view;

    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /*
    * @return
    * */
    public static void CustomToast(Context context, String content) {

        int imageID[] = new int[]{R.drawable.call_locate_blue, R.drawable.call_locate_gray, R.drawable.call_locate_green, R.drawable.call_locate_orange, R.drawable.call_locate_white};
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        int which = sp.getInt("which", 0);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        view = View.inflate(context, R.layout.custom_toast, null);

        TextView textView = (TextView) view.findViewById(R.id.toast_text);    // 得到textview
        view.setBackgroundResource(imageID[which]);
        textView.setText(content);     //设置文本类荣，就是你想给用户看的提示数据
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; //宽度包裹内容
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //没有焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  // 不可触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持当前屏幕
        params.format = PixelFormat.TRANSLUCENT; // 透明
        params.type = WindowManager.LayoutParams.TYPE_TOAST; // 执行toast的类型
        params.gravity = Gravity.LEFT | Gravity.TOP;

        params.x = 120;//不是坐标,表示的距离边框的距离,根据gravity来设置的,如果gravity是left表示距离左边框的距离,如果是right表示距离有边框的距离
        params.y = 100;//跟x的含义

        //2.将view对象添加到windowManager中
        //params : layoutparams  控件的属性
        //将params属性设置给view对象,并添加到windowManager中
        windowManager.addView(view, params);
    }

    public static void hideToast() {
        if (windowManager != null && view != null) {
        windowManager.removeView(view);//移出控件
        windowManager = null;
        view = null;
        }
    }
}
