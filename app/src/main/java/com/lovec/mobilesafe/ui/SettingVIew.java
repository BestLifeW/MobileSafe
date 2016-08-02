package com.lovec.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovec.mobilesafe.R;

/**
 * Created by lovec on 2016/8/1.
 */
public class SettingView extends RelativeLayout {

    private TextView iv_setting_title;
    private TextView iv_setting_des;
    private CheckBox cb_setting_update;
    private String title;
    private String des_on;
    private String des_off;

    public SettingView(Context context) {
        super(context);
        init();
    }

    //AttributeSet 保存控件的所有属性
    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
     /*   int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            Log.i("dddddd", "SettingView:"+attrs.getAttributeValue(i));
        }*/
        title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "title");
        des_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "des_on");
        des_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "des_off");

        iv_setting_title.setText(title);
        if (isChecked()) {
            iv_setting_des.setText(des_on);
        } else {
            iv_setting_des.setText(des_off);
        }
    }

    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.settingview, this);
        //初始化控件
        iv_setting_title = (TextView) view.findViewById(R.id.iv_setting_title);
        iv_setting_des = (TextView) view.findViewById(R.id.iv_setting_des);
        cb_setting_update = (CheckBox) view.findViewById(R.id.cb_setting_update);

    }

    public void setTitle(String title) {
        iv_setting_title.setText(title);
    }

    public void setDes(String des) {
        iv_setting_des.setText(des);
    }

    public void setChecked(boolean isChecked) {
        cb_setting_update.setChecked(isChecked);
        if (isChecked()) {
            iv_setting_des.setText(des_on);
        } else {
            iv_setting_des.setText(des_off);
        }
    }

    public boolean isChecked() {
        boolean checked = cb_setting_update.isChecked();
        return checked;
    }
}
