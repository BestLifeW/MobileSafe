package com.lovec.mobilesafe;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lovec.mobilesafe.been.BlacknumInfo;
import com.lovec.mobilesafe.dao.BlackNumDao;
import com.lovec.mobilesafe.utils.MyAsycnTaks;
import com.lovec.mobilesafe.utils.ToastUtils;

import java.util.List;

public class CallSmsSafeActivity extends AppCompatActivity {

    private ListView lv_callsmssafe_blacknum;
    private ProgressBar loading;
    private BlackNumDao blackNumDao;
    private List<BlacknumInfo> list;
    private MyAdapter myAdapter;
    private static final String TAG = "CallSmsSafeActivity";
    private AlertDialog dialog;
    private final int maxNum = 20;
    private int startIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsmssafe);
        blackNumDao = new BlackNumDao(getApplicationContext());


        Log.i(TAG, "preTask: 我在添加数据");

        initView();
    /*    for (int i = 0; i < 100; i++) {
            blackNumDao.addBlackNum("12345123" + i, 1);
        }
*/
        fillData();
    }

    private void fillData() {

        new MyAsycnTaks() {

            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void postTask() {
                if (myAdapter == null) {
                    myAdapter = new MyAdapter();
                    lv_callsmssafe_blacknum.setAdapter(myAdapter);
                } else {
                    myAdapter.notifyDataSetChanged();
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinBack() {
                if (list == null) {
                    //1.2.3    4.5.6
                    list = blackNumDao.getPartBlackNum(maxNum, startIndex);
                } else {
                    //addAll : 将一个集合整合到另一个集合
                    //A [1.2.3] B[4.5.6]
                    //A.addAll(B)  A [1.2.3.4.5.6]
                    list.addAll(blackNumDao.getPartBlackNum(maxNum, startIndex));
                }
            }
        }.excute();
    }

    /*
    * 初始化控件*/
    private void initView() {
        lv_callsmssafe_blacknum = (ListView) findViewById(R.id.lv_callsmssafe_blacknum);
        loading = (ProgressBar) findViewById(R.id.loading);
        lv_callsmssafe_blacknum.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当滑动状态改变的方法
            //view listview scrollState:滑动状态
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    int position = lv_callsmssafe_blacknum.getLastVisiblePosition();
                    if (position == list.size() - 1) {
                        startIndex += maxNum;
                        fillData();
                    }
                }
            }

            //滑动调用
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            final BlacknumInfo blacknumInfo = list.get(position);
            View view;
            ViewHodler viewHodler;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.item_callsmssafe, null);
                viewHodler = new ViewHodler();
                viewHodler.tv_itemcallsmssafe_blacknum = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_blacknum);
                viewHodler.tv_itemcallsmssafe_mode = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_mode);
                viewHodler.iv_itemcallsmssafe_delete = (ImageView) view.findViewById(R.id.iv_itemcallsmssafe_delete);
                view.setTag(viewHodler);
            } else {
                view = convertView;
                viewHodler = (ViewHodler) view.getTag();
            }
            viewHodler.tv_itemcallsmssafe_blacknum.setText(blacknumInfo.getBlacknum());
            int mode = blacknumInfo.getMode();
            switch (mode) {
                case BlackNumDao.CALL:
                    viewHodler.tv_itemcallsmssafe_mode.setText("电话拦截");
                    break;
                case BlackNumDao.SMS:
                    viewHodler.tv_itemcallsmssafe_mode.setText("短信拦截");
                    break;
                case BlackNumDao.ALL:
                    viewHodler.tv_itemcallsmssafe_mode.setText("全部拦截");
                    break;
            }
            viewHodler.iv_itemcallsmssafe_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallSmsSafeActivity.this);
                    builder.setMessage("确认删除？" + blacknumInfo.getBlacknum() + "？");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //数据库里面删除
                            blackNumDao.deleteBlackNum(blacknumInfo.getBlacknum());
                            //list里面删除
                            list.remove(position);
                            //界面更新
                            myAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();

                }
            });
            return view;
        }
    }

    class ViewHodler {
        TextView tv_itemcallsmssafe_blacknum, tv_itemcallsmssafe_mode;
        ImageView iv_itemcallsmssafe_delete;
    }

    public void addBlackNum(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CallSmsSafeActivity.this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknum, null);
        final EditText et_addblacknum_blacknum = (EditText) view.findViewById(R.id.et_addblacknum_blacknum);
        final RadioGroup rg_addblacknum_modes = (RadioGroup) view.findViewById(R.id.rg_addblacknum_modes);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        //添加黑名单
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blacknum = et_addblacknum_blacknum.getText().toString().trim();
                if (TextUtils.isEmpty(blacknum)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入黑名单号码");
                    return;
                }
                int mode = -1;
                int radioButtonId = rg_addblacknum_modes.getCheckedRadioButtonId();//获取选中的RadioButton的id
                switch (radioButtonId) {
                    case R.id.rb_addblacknum_tel:
                        //电话拦截
                        mode = BlackNumDao.CALL;
                        break;
                    case R.id.rb_addblacknum_sms:
                        //短信拦截
                        mode = BlackNumDao.SMS;
                        break;
                    case R.id.rb_addblacknum_all:
                        //全部拦截
                        mode = BlackNumDao.ALL;
                        break;

                }
                blackNumDao.addBlackNum(blacknum, mode);
                //添加到界面list
                list.add(0, new BlacknumInfo(blacknum, mode));//让添加的数据top显示 0 表示第一个位置
                //更新界面
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        //取消按钮
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        //builder.show();
        dialog = builder.create();
        dialog.show();
    }
}
