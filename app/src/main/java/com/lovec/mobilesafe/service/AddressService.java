package com.lovec.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.lovec.mobilesafe.dao.AddressDao;
import com.lovec.mobilesafe.utils.ToastUtils;

public class AddressService extends Service {
    private MyPhoneStateListener myPhoneStateListener;
    private TelephonyManager telephonyManager;
    private MyOutCallingPhoneReceiver myOutCallingPhoneReceiver;
    private static final String TAG = "AddressService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myPhoneStateListener = new MyPhoneStateListener();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        myOutCallingPhoneReceiver = new MyOutCallingPhoneReceiver();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
        try {

            registerReceiver(myOutCallingPhoneReceiver, intentFilter);
            Log.i(TAG, "onCreate: 广播注册成功");
        } catch (Exception e) {
            Log.i(TAG, "onCreate: 广播注册失败");
        }
        Log.i(TAG, "onCreate: 服务开启成功");

    }


    private class MyOutCallingPhoneReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();
            String location = AddressDao.queryAddress(phone, context);
            ToastUtils.CustomToast(context, location);
            //ToastUtils.CustomToast(context, "123");
            if (!TextUtils.isEmpty(location)) {
                // 显示toast
                ToastUtils.CustomToast(context, location);
            }
            Log.i(TAG, "电话拨打出去了！" + location);
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        //state 电话状态
        //incomingNumber 来电电话
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    Log.i(TAG, "电话铃声响");
                    String location = AddressDao.queryAddress(incomingNumber, getApplicationContext());
                    if (!TextUtils.isEmpty(location)) {
                        ToastUtils.CustomToast(getApplicationContext(), location);
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE://空闲
                    Log.i(TAG, "电话空闲了");
                    ToastUtils.hideToast();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接听
                    Log.i(TAG, "电话接听了");
                    //ToastUtils.hideToast();
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);

        }
    }

    @Override
    public void onDestroy() {
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(myOutCallingPhoneReceiver);
        Log.i(TAG, "广播关闭成功");
        super.onDestroy();
    }
}
