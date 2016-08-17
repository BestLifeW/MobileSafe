package com.lovec.mobilesafe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovec.mobilesafe.been.AppInfo;
import com.lovec.mobilesafe.engine.AppEngine;
import com.lovec.mobilesafe.utils.AppUtil;
import com.lovec.mobilesafe.utils.DensityUtil;
import com.lovec.mobilesafe.utils.MyAsycnTaks;
import com.lovec.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SoftManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv_softmanager_application;
    private ProgressBar loading;
    private List<AppInfo> list;
    private ArrayList<AppInfo> userappinfo;
    private ArrayList<AppInfo> systeminfo;
    private TextView tv_softmanager_userorsystem;
    AppInfo appInfo = null;
    private PopupWindow popupWindow;
    private Myadapter adapter;
    private static final String TAG = "SoftManagerActivity";
    private TextView tv_softmanager_rom;
    private TextView tv_softmanager_sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);

        lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
        loading = (ProgressBar) findViewById(R.id.loading);
        tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);

        tv_softmanager_rom = (TextView) findViewById(R.id.tv_softmanager_rom);
        tv_softmanager_sd = (TextView) findViewById(R.id.tv_softmanager_sd);

        long availablesROM = AppUtil.getAvailablesROM();
        long availablesSD = AppUtil.getAvailablesSD();

        String ROM = Formatter.formatShortFileSize(getApplicationContext(), availablesROM);
        String SD = Formatter.formatShortFileSize(getApplicationContext(), availablesSD);
        tv_softmanager_rom.setText("内存可用:" + ROM);
        tv_softmanager_sd.setText("SD卡可用" + SD);
        fillData();
        listviewOnscroll();
        listviewItemClick();
    }

    /*
    * list条目的点击事件
    * */
    private void listviewItemClick() {
        lv_softmanager_application.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //view 条目的view 对象
                //弹出气泡
                //判断用户程序
                if (position == 0 || position == userappinfo.size() + 1) {
                    return;
                }
                //获取条目所对应的

                if (position <= userappinfo.size()) {
                    appInfo = userappinfo.get(position - 1);
                } else {
                    appInfo = systeminfo.get(position - userappinfo.size() - 2);
                }
                // 弹出新气泡前 删除原先的
                hidePopuwindow();
                //3弹出气泡
                View contentView = View.inflate(getApplicationContext(), R.layout.popu_window, null);
                popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] location = new int[2];
                view.getLocationInWindow(location);
                int x = location[0];
                int y = location[1];
                popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, x + DensityUtil.dip2qx(getApplication(), 50), y + DensityUtil.dip2qx(getApplication(), 50));

                ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(200);

                AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
                alphaAnimation.setDuration(200);

                //true 使用动画查补器
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);
                contentView.setAnimation(animationSet);

                //初始化控件
                LinearLayout ll_popuwindow_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_uninstall);
                LinearLayout ll_popuwindow_start = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_start);
                LinearLayout ll_popuwindow_share = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_share);
                LinearLayout ll_popuwindow_detail = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_detail);
                //给控件设置点击事件
                ll_popuwindow_uninstall.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_start.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_share.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_detail.setOnClickListener(SoftManagerActivity.this);
            }
        });
    }

    private void hidePopuwindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
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
                hidePopuwindow();
                if (userappinfo != null && systeminfo != null) {
                    if (firstVisibleItem >= userappinfo.size() + 1) {
                        tv_softmanager_userorsystem.setText("系统应用" + systeminfo.size());
                    } else {
                        tv_softmanager_userorsystem.setText("用户应用" + userappinfo.size());
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
                if (adapter == null) {
                    adapter = new Myadapter();
                    lv_softmanager_application.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinBack() {
                list = AppEngine.getAppInfos(getApplicationContext());
                userappinfo = new ArrayList<AppInfo>();
                systeminfo = new ArrayList<AppInfo>();

                for (AppInfo appInfo : list) {
                    if (appInfo.isUser()) {
                        userappinfo.add(appInfo);
                    } else {
                        systeminfo.add(appInfo);
                    }
                }

            }
        }.excute();
    }

    //气泡点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_popuwindow_uninstall:
                System.out.println("卸载");
                uninstall();
                break;
            case R.id.ll_popuwindow_start:
                System.out.println("启动");
                start();
                break;
            case R.id.ll_popuwindow_share:
                System.out.println("分享");
                share();
                break;
            case R.id.ll_popuwindow_detail:
                System.out.println("详情");
                detail();
                break;
        }
        hidePopuwindow();
    }

    /*
    * 分享 功能
    * */
    private void share() {
        Log.i(TAG, "share: 分享啦");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "发现一个很牛x软件" + appInfo.getName() + appInfo.getPackageName() + ",下载地址:www.baidu.com,自己去搜");

        startActivity(intent);

    }

    /*
    * 详情界面
    * */
    private void detail() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
        startActivity(intent);
    }

    /*
    * 启动操作
    * */
    private void start() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackageName());
        if (intent != null) {
            startActivity(intent);
        } else {
            ToastUtils.showToast(getApplicationContext(), "系统的非安卓应用");
        }

    }

    /*
    * 卸载功能
    * */
    private void uninstall() {
        /**
         * <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <action android:name="android.intent.action.DELETE" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="package" />
         </intent-filter>
         */
        if (appInfo.isUser()) {


            //判断自己的应用 是否是 不让卸载
            if (!appInfo.getPackageName().equals(getPackageName())) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
                startActivityForResult(intent, 0);
            } else {
                ToastUtils.showToast(getApplicationContext(), "自己怎么卸载自己呢");
            }
        } else {
            ToastUtils.showToast(getApplicationContext(), "要卸载系统程序，请先root");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();


    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userappinfo.size() + systeminfo.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position == 0) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText("用户应用" + userappinfo.size());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                return textView;
            } else if (position == (userappinfo.size() + 1)) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText("系统应用" + systeminfo.size());
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
            AppInfo appInfo = null;
            if (position <= userappinfo.size()) {
                appInfo = userappinfo.get(position - 1);
            } else {
                appInfo = systeminfo.get(position - userappinfo.size() - 2);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePopuwindow();
    }
}

