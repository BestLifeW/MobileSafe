package com.lovec.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovec.mobilesafe.R;

/**
 * Created by lovec on 2016/8/1.
 */
public class SettingClickView extends RelativeLayout {

    private TextView iv_setting_title;
    private TextView iv_setting_des;

    public SettingClickView(Context context) {
        super(context);
        init();
    }

    //AttributeSet 保存控件的所有属性
    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }
    private void init() {
        View view = View.inflate(getContext(), R.layout.settingclickview, this);
        //初始化控件
        iv_setting_title = (TextView) view.findViewById(R.id.iv_setting_title);
        iv_setting_des = (TextView) view.findViewById(R.id.iv_setting_des);

    }
    public void setTitle(String title) {
        iv_setting_title.setText(title);
    }

    public void setDes(String des) {
        iv_setting_des.setText(des);
    }

}
