package com.lovec.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lovec on 2016/7/28.
 */
public class ToastUtils {

    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
