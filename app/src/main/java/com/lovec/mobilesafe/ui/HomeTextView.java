package com.lovec.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lovec on 2016/8/1.
 */
public class HomeTextView extends TextView {
    //在代码时执行
    public HomeTextView(Context context) {
        super(context);
    }

    //在布局文件使用的时候调用(用的最多)
    public HomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //在布局文件使用的时候调用，比两个参数多了个样式文件
    public HomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //设置自定义的TextView自动获取焦点

    @Override
    public boolean isFocused() {
        //返回true获取焦点
        return true;
    }
}
