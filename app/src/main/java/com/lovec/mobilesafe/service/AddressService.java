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

import com.lovec.mobilesafe.dao.AddressDao;
import com.lovec.mobilesafe.utils.ToastUtils;

public class AddressService extends Service {
    private MyPhoneStateListener myPhoneStateListener;
    private TelephonyManager telephonyManager;
    private MyOutCallingPhoneReceiver myOutCallingPhoneReceiver;

    public AddressService() {
    }

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
        registerReceiver(myOutCallingPhoneReceiver, intentFilter);
    }


    private class MyOutCallingPhoneReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();
            String location = AddressDao.queryAddress(phone, context);
            ToastUtils.CustomToast(context, location);
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        //state 电话状态
        //incomingNumber 来电电话
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    String location = AddressDao.queryAddress(incomingNumber, getApplicationContext());
                    if (!TextUtils.isEmpty(location)) {
                        ToastUtils.showToast(getApplicationContext(), location);
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE://空闲
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接听
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);

        }
    }

    @Override
    public void onDestroy() {
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(myOutCallingPhoneReceiver);
        super.onDestroy();
    }
}
