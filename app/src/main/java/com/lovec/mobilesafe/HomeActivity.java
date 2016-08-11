package com.lovec.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovec.mobilesafe.utils.MD5Utils;
import com.lovec.mobilesafe.utils.ToastUtils;

/**
 * Created by lovec on 2016/7/28.
 */
public class HomeActivity extends Activity {

    private GridView gv_home_gridview;
    Context mContext;
    private AlertDialog dialog;
    private SharedPreferences sp;

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
        sp = getSharedPreferences("config", MODE_PRIVATE);
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        //判断
                        if (TextUtils.isEmpty(sp.getString("password", ""))) {
                            //设置密码
                            showSetPasswordDialog();
                        } else {
                            showEnterPasswordDialog();
                        }

                        break;
                    case 7:
                        intent = new Intent(mContext, AToolsActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(mContext, SettingActivity.class);
                        startActivity(intent);
                        break;
                }

            }


        });
    }

    //显示的是输入密码的对话框
    private void showEnterPasswordDialog() {
        AlertDialog.Builder bulider = new AlertDialog.Builder(this);
        bulider.setCancelable(false);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enterpassword, null);
        final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        //设置确认按钮的逻辑
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断密码是否正确
                String password = et_setpassword_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showToast(mContext, "密码不能为空");
                    return;
                }
                String sp_password = sp.getString("password", "");
                if (MD5Utils.passwordMD5(password).equals(sp_password)) {
                    //跳转到手机防盗界面
                    Intent intent = new Intent(getApplicationContext(), LostfindActivity.class);
                    startActivity(intent);
                    //隐藏dialog
                    dialog.dismiss();
                } else {
                    ToastUtils.showToast(mContext, "密码不正确");
                }

            }
        });

        //设置取消按钮的逻辑
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bulider.setView(view);
        dialog = bulider.create();
        dialog.show();
    }

    /*展示设置密码对话框*/
    private void showSetPasswordDialog() {
        AlertDialog.Builder bulider = new AlertDialog.Builder(this);
        bulider.setCancelable(false);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setpassword, null);
        final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
        final EditText et_setpassword_confim = (EditText) view.findViewById(R.id.et_setpassword_confim);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_setpassword_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showToast(mContext, "请输入密码");
                    return;
                }
                String confimpassword = et_setpassword_confim.getText().toString().trim();
                if (confimpassword.equals(password)) {
                    //保存密码
                    sp.edit().putString("password", MD5Utils.passwordMD5(confimpassword)).commit();
                    ToastUtils.showToast(mContext, "密码设置成功");
                    dialog.dismiss();
                } else {
                    ToastUtils.showToast(mContext, "两次密码不一致");
                }
            }
        });
        //取消按钮的逻辑
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bulider.setView(view);
        dialog = bulider.create();
        dialog.show();
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
            View view = View.inflate(mContext, R.layout.item_home, null);
            ImageView iv_itemhome_icon = (ImageView) view.findViewById(R.id.iv_itemhome_icon);
            TextView tv_itemhome_text = (TextView) view.findViewById(R.id.tv_itemhome_text);

            iv_itemhome_icon.setImageResource(imageId[position]);
            tv_itemhome_text.setText(names[position]);
            return view;
        }
    }
}
