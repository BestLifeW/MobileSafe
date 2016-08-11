package com.lovec.mobilesafe.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lovec.mobilesafe.R;

/**
 * Created by lovec on 2016/7/28.
 */
public class ToastUtils {

    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /*
    * @return
    * */
    public static void CustomToast(Context context, String content) {
        View view = View.inflate(context, R.layout.custom_toast, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_text);    // 得到textview
        textView.setText(content);     //设置文本类荣，就是你想给用户看的提示数据
        Toast toast = new Toast(context);     //创建一个toast
        toast.setDuration(Toast.LENGTH_SHORT);          //设置toast显示时间，整数值
        toast.setGravity(Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);    //toast的显示位置，这里居中显示
        toast.setView(view);     //設置其显示的view,
        toast.show();             //显示toast
    }
}
