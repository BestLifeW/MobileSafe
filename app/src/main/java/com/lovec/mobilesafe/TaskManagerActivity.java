package com.lovec.mobilesafe;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovec.mobilesafe.been.TaskInfo;
import com.lovec.mobilesafe.engine.TaskEngine;
import com.lovec.mobilesafe.utils.MyAsycnTaks;
import com.lovec.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {
    private static final String TAG = "TaskManagerActivity";
    private ListView lv_taskmanager_processes;
    private ProgressBar loading;
    private List<TaskInfo> list;
    private ArrayList<TaskInfo> userappinfo;
    private ArrayList<TaskInfo> systeminfo;
    private Myadapter adapter;
    private TaskInfo taskInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager);

        lv_taskmanager_processes = (ListView) findViewById(R.id.lv_taskmanager_processes);
        loading = (ProgressBar) findViewById(R.id.loading);

        //加载数据
        fillData();
        listviewItemClick();
    }

    /*
    listview 条目点击事件
    * */
    private void listviewItemClick() {
        lv_taskmanager_processes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == userappinfo.size() + 1) {
                    return;
                }
                //获取条目所对应的

                if (position <= userappinfo.size()) {
                    taskInfo = userappinfo.get(position - 1);
                } else {
                    taskInfo = systeminfo.get(position - userappinfo.size() - 2);
                }

                if (taskInfo.isChecked()) {
                    taskInfo.setChecked(false);
                } else {
                    if (!taskInfo.getPackageName().equals(getPackageName())) {
                        taskInfo.setChecked(true);
                    }

                }
                //更新条目
                //adapter.notifyDataSetChanged();
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(taskInfo.isChecked());
            }
        });
    }


    /*
    * 加载数据
    * */
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
                    lv_taskmanager_processes.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinBack() {
                list = TaskEngine.getTaskAllInfo(getApplicationContext());
                userappinfo = new ArrayList<TaskInfo>();
                systeminfo = new ArrayList<TaskInfo>();

                for (TaskInfo taskInfo : list) {
                    if (taskInfo.isUser()) {
                        userappinfo.add(taskInfo);
                        Log.i(TAG, userappinfo.toString());
                    } else {
                        systeminfo.add(taskInfo);
                        Log.i(TAG, systeminfo.toString());
                    }
                }

            }
        }.excute();
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
                textView.setText("用户进程" + userappinfo.size());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                return textView;
            } else if (position == (userappinfo.size() + 1)) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText("系统进程" + systeminfo.size());
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
                view = View.inflate(getApplicationContext(), R.layout.item_taskmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemtaskmanager_icon = (ImageView) view.findViewById(R.id.iv_itemtaskmanager_icon);
                viewHolder.tv_itemtaskmanager_name = (TextView) view.findViewById(R.id.tv_itemtaskmanager_name);
                viewHolder.tv_itemtaskmanager_ram = (TextView) view.findViewById(R.id.tv_itemtaskmanager_ram);
                viewHolder.cb_itemtaskmanager_ischecked = (CheckBox) view.findViewById(R.id.cb_itemtaskmanager_ischecked);
                //将viewholer和view对象绑定
                view.setTag(viewHolder);
            }
            //1.获取应用程序的信息
            TaskInfo taskinfo = null;
            if (position <= userappinfo.size()) {
                taskinfo = userappinfo.get(position - 1);
            } else {
                taskinfo = systeminfo.get(position - userappinfo.size() - 2);
                boolean b = taskinfo == null;
                Log.i(TAG, b + "");
            }
            //设置显示数据
            long ramSize = taskinfo.getRamSize();
            String fileSize = Formatter.formatShortFileSize(getApplicationContext(), ramSize);
            if (taskinfo.getIcon() == null) {
                viewHolder.iv_itemtaskmanager_icon.setImageResource(R.mipmap.ic_launcher);
            } else {
                viewHolder.iv_itemtaskmanager_icon.setImageDrawable(taskinfo.getIcon());
            }
            if (TextUtils.isEmpty(taskinfo.getName())) {
                viewHolder.tv_itemtaskmanager_name.setText(taskinfo.getPackageName());
            } else {
                viewHolder.tv_itemtaskmanager_name.setText(taskinfo.getName());
            }
            viewHolder.tv_itemtaskmanager_ram.setText("内存占用" + fileSize);
            if (taskinfo.isChecked()) {
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(true);
            } else {
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(false);
            }
            if (taskinfo.getPackageName().equals(getPackageName())) {
                viewHolder.cb_itemtaskmanager_ischecked.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.cb_itemtaskmanager_ischecked.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_itemtaskmanager_icon;
        TextView tv_itemtaskmanager_name, tv_itemtaskmanager_ram;
        CheckBox cb_itemtaskmanager_ischecked;
    }


    /*
    * 全选的操作
    * */
    public void all(View view) {
        for (int i = 0; i < userappinfo.size(); i++) {
            if (!userappinfo.get(i).getPackageName().equals(getPackageName())) {

                userappinfo.get(i).setChecked(true);
            }
        }
        for (int i = 0; i < systeminfo.size(); i++) {
            systeminfo.get(i).setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }

    /*
    *
    * */
    public void cancel(View view) {
        for (int i = 0; i < userappinfo.size(); i++) {
            userappinfo.get(i).setChecked(false);
        }
        for (int i = 0; i < systeminfo.size(); i++) {
            systeminfo.get(i).setChecked(false);
        }
        adapter.notifyDataSetChanged();
    }

    /*
    * 清理的操作
    * */
    public void clear(View view) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<TaskInfo> deleteTaskInfo = new ArrayList<>();
        for (int i = 0; i < userappinfo.size(); i++) {
            if (userappinfo.get(i).isChecked()) {
                activityManager.killBackgroundProcesses(userappinfo.get(i).getPackageName());
                deleteTaskInfo.add(userappinfo.get(i));
            }

        }
        for (int i = 0; i < systeminfo.size(); i++) {
            if (systeminfo.get(i).isChecked()) {
                activityManager.killBackgroundProcesses(systeminfo.get(i).getPackageName());
                deleteTaskInfo.add(systeminfo.get(i));
            }

        }
        for (TaskInfo info : deleteTaskInfo) {
            if (taskInfo.isUser()) {
                userappinfo.remove(taskInfo);
            } else {
                systeminfo.remove(taskInfo);
            }
        }
        ToastUtils.showToast(getApplicationContext(), "共清理了" + deleteTaskInfo.size() + "进程" + "释放了");
        deleteTaskInfo.clear();
        deleteTaskInfo = null;
        adapter.notifyDataSetChanged();
    }
}
