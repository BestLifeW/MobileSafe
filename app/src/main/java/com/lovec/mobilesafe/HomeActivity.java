package com.lovec.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lovec on 2016/7/28.
 */
public class HomeActivity extends Activity {

    private GridView gv_home_gridview;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        initView();
    }

    /*
    * 初始化View*/
    private void initView() {
        gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
        gv_home_gridview.setAdapter(new Myadapter());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 8:
                        intent = new Intent(mContext, SettingActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    /*
    给Gridview设置适配器
    * */
    private class Myadapter extends BaseAdapter {
        int[] imageId = {R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};
        String[] names = {"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
                "高级工具", "设置中心"};

        //设置条目个数
        @Override
        public int getCount() {
            return names.length;
        }

        //设置对应的数据
        @Override
        public Object getItem(int position) {
            return null;
        }

        //获取条目的Id
        @Override
        public long getItemId(int position) {
            return position;
        }

        //获取条目的View
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //TextView textView =new TextView(mContext);
            //textView.setText("你们都是辣鸡");
            View view = View.inflate(mContext, R.layout.item_home, null);
            ImageView iv_itemhome_icon = (ImageView) view.findViewById(R.id.iv_itemhome_icon);
            TextView tv_itemhome_text = (TextView) view.findViewById(R.id.tv_itemhome_text);

            iv_itemhome_icon.setImageResource(imageId[position]);
            tv_itemhome_text.setText(names[position]);
            return view;
        }
    }
}
