package com.lovec.mobilesafe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovec.mobilesafe.been.AppInfo;
import com.lovec.mobilesafe.engine.AppEngine;
import com.lovec.mobilesafe.utils.MyAsycnTaks;

import java.util.ArrayList;
import java.util.List;

public class SoftManagerActivity extends AppCompatActivity {
    private ListView lv_softmanager_application;
    private ProgressBar loading;
    private List<AppInfo> list;
    private ArrayList<AppInfo> userinfo;
    private ArrayList<AppInfo> systeminfo;
    private TextView tv_softmanager_userorsystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);

        lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
        loading = (ProgressBar) findViewById(R.id.loading);
        tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);


        fillData();
        listviewOnscroll();
    }

    //监听listview 滑动事件
    private void listviewOnscroll() {
        lv_softmanager_application.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            //滑动的时候调用
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userinfo != null && systeminfo != null) {
                    if (firstVisibleItem == userinfo.size() + 1) {
                        tv_softmanager_userorsystem.setText("系统应用" + systeminfo.size());
                    } else {
                        tv_softmanager_userorsystem.setText("用户应用" + userinfo.size());
                    }
                }
            }
        });
    }

    private void fillData() {
        new MyAsycnTaks() {

            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void postTask() {
                lv_softmanager_application.setAdapter(new Myadapter());
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinBack() {
                list = AppEngine.getAppInfos(getApplicationContext());
                userinfo = new ArrayList<AppInfo>();
                systeminfo = new ArrayList<AppInfo>();

                for (AppInfo appInfo : list) {
                    if (appInfo.isUser()) {
                        userinfo.add(appInfo);
                    } else {
                        systeminfo.add(appInfo);
                    }
                }

            }
        }.excute();
    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return userinfo.size() + systeminfo.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position == 0) {
                TextView textView = new TextView(getApplicationContext());
                //textView.setText("用户应用" + userinfo.size());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                return textView;
            } else if (position == (userinfo.size() + 1)) {
                TextView textView = new TextView(getApplicationContext());
                //textView.setText("系统应用" + userinfo.size());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemsoftmanage_icon = (ImageView) view.findViewById(R.id.iv_itemsoftmanage_icon);
                viewHolder.tv_softmanager_name = (TextView) view.findViewById(R.id.tv_softmanager_name);
                viewHolder.tv_softmanager_issd = (TextView) view.findViewById(R.id.tv_softmanager_issd);
                viewHolder.tv_softmanager_version = (TextView) view.findViewById(R.id.tv_softmanager_version);
                //将viewholer和view对象绑定
                view.setTag(viewHolder);
            }
            //1.获取应用程序的信息
            AppInfo appInfo;
            if (position <= userinfo.size()) {
                appInfo = userinfo.get(position - 1);
            } else {
                appInfo = systeminfo.get(position - userinfo.size() - 2);
            }
            //设置显示数据
            viewHolder.iv_itemsoftmanage_icon.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_softmanager_name.setText(appInfo.getName());
            boolean sd = appInfo.isSD();
            if (sd) {
                viewHolder.tv_softmanager_issd.setText("SD卡");
            } else {
                viewHolder.tv_softmanager_issd.setText("手机内存");
            }
            viewHolder.tv_softmanager_version.setText(appInfo.getVersionName());
            return view;
        }

    }

    static class ViewHolder {
        ImageView iv_itemsoftmanage_icon;
        TextView tv_softmanager_name, tv_softmanager_issd, tv_softmanager_version;
    }
}

